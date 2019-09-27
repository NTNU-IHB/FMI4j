package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.util.*

abstract class Fmi2Slave {

    private val variables = mutableMapOf<Long, Var<*>>()

    val modelDescription: Fmi2ModelDescription by lazy {

        Fmi2ModelDescription().also { md ->

            md.fmiVersion = "2.0"
            md.generationTool = "fmi4j"
            md.variableNamingConvention = "structured"
            md.guid = UUID.randomUUID().toString()

            val slaveInfo = javaClass.getAnnotation(SlaveInfo::class.java)

            md.modelName = slaveInfo?.modelName ?: javaClass.simpleName
            if (slaveInfo != null) {
                md.author = if (slaveInfo.author.isNotEmpty()) slaveInfo.author else null
                md.version = if (slaveInfo.version.isNotEmpty()) slaveInfo.version else null
                md.copyright = if (slaveInfo.copyright.isNotEmpty()) slaveInfo.copyright else null
                md.license = if (slaveInfo.license.isNotEmpty()) slaveInfo.license else null
                md.description = if (slaveInfo.description.isNotEmpty()) slaveInfo.description else null
            }

            md.modelVariables = Fmi2ModelDescription.ModelVariables()
            md.coSimulation = Fmi2ModelDescription.CoSimulation().also { cs ->
                cs.isCanGetAndSetFMUstate = false
                cs.isCanSerializeFMUstate = false
                cs.isCanInterpolateInputs = false
                cs.modelIdentifier = md.modelName
                if (slaveInfo != null) {
                    cs.isNeedsExecutionTool = slaveInfo.needsExecutionTool
                    cs.isCanBeInstantiatedOnlyOncePerProcess = slaveInfo.canBeInstantiatedOnlyOncePerProcess
                    cs.isCanHandleVariableCommunicationStepSize = slaveInfo.canHandleVariableCommunicationStepSize
                }
            }

            javaClass.getAnnotation(DefaultExperiment::class.java)?.also { de ->
                md.defaultExperiment = Fmi2ModelDescription.DefaultExperiment().apply {
                    if (de.startTime >= 0) startTime = de.startTime
                    if (de.stepSize > 0) stepSize = de.stepSize
                    if (de.stopTime > startTime) stopTime = de.stopTime
                }
            }


            var vr = 0L

            javaClass.declaredFields.forEach { field ->

                field.getAnnotation(ScalarVariable::class.java)?.also { annotation ->

                    field.isAccessible = true

                    fun apply(f: (Fmi2ScalarVariable) -> Unit) {
                        md.modelVariables.scalarVariable.add(Fmi2ScalarVariable().also { v ->
                            v.name = field.name
                            v.valueReference = vr++
                            v.causality = annotation.causality
                            v.variability = annotation.variability
                            v.initial = if (annotation.initial == Fmi2Initial.undefined) null else (annotation.initial)
                            f.invoke(v)
                        })
                    }

                    when (val type = field.type) {
                        Int::class, Int::class.java -> {
                            variables[vr] = IntVar({ field.getInt(this) }, { field.setInt(this, it) })
                            apply { v ->
                                v.integer = Fmi2ScalarVariable.Integer().also { i ->
                                    (field.get(this) as? Int)?.also { i.start = it }
                                }
                            }
                        }
                        Double::class, Double::class.java -> {
                            variables[vr] = RealVar({ field.getDouble(this) }, { field.setDouble(this, it) })
                            apply { v ->
                                v.real = Fmi2ScalarVariable.Real().also { i ->
                                    (field.get(this) as? Double)?.also { i.start = it }
                                }
                            }
                        }
                        Boolean::class, Boolean::class.java -> {
                            variables[vr] = BoolVar({ field.getBoolean(this) }, { field.setBoolean(this, it) })
                            apply { v ->
                                v.boolean = Fmi2ScalarVariable.Boolean().also { i ->
                                    (field.get(this) as? Boolean)?.also { i.isStart = it }
                                }
                            }
                        }
                        String::class, String::class.java -> {
                            variables[vr] = StringVar({ field.get(this) as String }, { field.set(this, it) })
                            apply { v ->
                                v.string = Fmi2ScalarVariable.String().also { i ->
                                    (field.get(this) as? String)?.also { i.start = it }
                                }
                            }
                        }
                        else -> throw IllegalStateException("Unsupported variable type: $type")
                    }

                }

                field.getAnnotation(ScalarVariables::class.java)?.also { annotation ->

                    field.isAccessible = true

                    fun apply(index: Int, f: (Fmi2ScalarVariable) -> Unit) {
                        md.modelVariables.scalarVariable.add(Fmi2ScalarVariable().also { v ->
                            v.name = "${field.name}[$index]"
                            v.valueReference = vr++
                            v.causality = annotation.causality
                            v.variability = annotation.variability
                            v.initial = if (annotation.initial == Fmi2Initial.undefined) null else (annotation.initial)
                            f.invoke(v)
                        })
                    }

                    when (val type = field.type) {
                        IntArray::class, IntArray::class.java -> {
                            val array = field.get(this) as? IntArray
                                    ?: throw IllegalStateException("${field.name} cannot be null!")
                            for (i in 0 until annotation.size) {
                                variables[vr] = IntVar({ array[i] }, { array[i] = it })
                                apply(i) { v ->
                                    v.integer = Fmi2ScalarVariable.Integer().also { integer ->
                                        array.also { integer.start = it[i] }
                                    }
                                }
                            }
                        }
                        DoubleArray::class, DoubleArray::class.java -> {
                            val array = field.get(this) as? DoubleArray
                                    ?: throw IllegalStateException("${field.name} cannot be null!")
                            for (i in 0 until annotation.size) {
                                variables[vr] = RealVar({ array[i] }, { array[i] = it })
                                apply(i) { v ->
                                    v.real = Fmi2ScalarVariable.Real().also { real ->
                                        array.also { real.start = it[i] }
                                    }
                                }
                            }
                        }
                        BooleanArray::class, BooleanArray::class.java -> {
                            val array = field.get(this) as? BooleanArray
                                    ?: throw IllegalStateException("${field.name} cannot be null!")
                            for (i in 0 until annotation.size) {
                                variables[vr] = BoolVar({ array[i] }, { array[i] = it })
                                apply(i) { v ->
                                    v.boolean = Fmi2ScalarVariable.Boolean().also { boolean ->
                                        array.also { boolean.isStart = it[i] }
                                    }
                                }
                            }
                        }
                        Array<String>::class, Array<String>::class.java -> {
                            @Suppress("UNCHECKED_CAST")
                            val array = field.get(this) as? Array<String>
                                    ?: throw IllegalStateException("${field.name} cannot be null!")
                            for (i in 0 until annotation.size) {
                                variables[vr] = StringVar({ array[i] }, { array[i] = it })
                                apply(i) { v ->
                                    v.string = Fmi2ScalarVariable.String().also { string ->
                                        array.also { string.start = it[i] }
                                    }
                                }
                            }
                        }
                        else -> throw IllegalStateException("Unsupported variable type: $type")
                    }

                }

            }

            check(md.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

        }
    }

    fun setupExperiment(startTime: Double) {}

    fun enterInitialisationMode() {}

    fun exitInitialisationMode() {}

    abstract fun doStep(dt: Double)

    fun reset() {}

    fun terminate() {}

    @Suppress("UNCHECKED_CAST")
    fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            (variables.getValue(vr[i]) as RealVar).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            (variables.getValue(vr[i]) as RealVar).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInteger(vr: LongArray): IntArray {
        return IntArray(vr.size) { i ->
            (variables.getValue(vr[i]) as IntVar).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            (variables.getValue(vr[i]) as IntVar).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            (variables.getValue(vr[i]) as BoolVar).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            (variables.getValue(vr[i]) as BoolVar).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            (variables.getValue(vr[i]) as StringVar).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            (variables.getValue(vr[i]) as StringVar).apply {
                setter?.invoke(values[i])
            }
        }
    }

}

class Var<T>(
        val getter: () -> T,
        val setter: ((T) -> Unit)?
)

typealias IntVar = Var<Int>
typealias RealVar = Var<Double>
typealias BoolVar = Var<Boolean>
typealias StringVar = Var<String>
