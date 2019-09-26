package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.util.*
import java.util.concurrent.atomic.AtomicLong

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FmiSlaveInfo(
        val name: String = "",
        val author: String = "",
        val version: String = "",
        val description: String = "",
        val copyright: String = "",
        val license: String = ""
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScalarVariable(
        val causality: Fmi2Causality = Fmi2Causality.local,
        val variability: Fmi2Variability = Fmi2Variability.continuous,
        val initial: Fmi2Initial = Fmi2Initial.undefined
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScalarVariables(
        val size: Int,
        val causality: Fmi2Causality = Fmi2Causality.local,
        val variability: Fmi2Variability = Fmi2Variability.continuous,
        val initial: Fmi2Initial = Fmi2Initial.undefined
)

abstract class FmiSlave {

    private val variables = mutableMapOf<Long, Var<*>>()

    val modelDescription: FmiModelDescription by lazy {

        val slaveInfo = javaClass.getAnnotation(FmiSlaveInfo::class.java)
                ?: throw IllegalStateException("No ${FmiSlaveInfo::class.java.simpleName} present!")

        FmiModelDescription().also { md ->

            md.fmiVersion = "2.0"
            md.guid = UUID.randomUUID().toString()
            md.modelName = slaveInfo.name
            md.author = if (slaveInfo.author.isNotEmpty()) slaveInfo.author else null
            md.version = if (slaveInfo.version.isNotEmpty()) slaveInfo.version else null
            md.copyright = if (slaveInfo.copyright.isNotEmpty()) slaveInfo.copyright else null
            md.license = if (slaveInfo.license.isNotEmpty()) slaveInfo.license else null
            md.description = if (slaveInfo.description.isNotEmpty()) slaveInfo.description else null
            md.modelVariables = FmiModelDescription.ModelVariables()
            md.coSimulation = FmiModelDescription.CoSimulation()
            md.generationTool = "fmi4j"
            md.variableNamingConvention = "structured"

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
                            variables[vr] = IntVar({field.getInt(this)}, {field.setInt(this, it)})
                            apply { v ->
                                v.integer = Fmi2ScalarVariable.Integer().also { i ->
                                    (field.get(this) as? Int)?.also { i.start = it }
                                }
                            }
                        }
                        Double::class, Double::class.java -> {
                            variables[vr] = RealVar({field.getDouble(this)}, {field.setDouble(this, it)})
                            apply { v ->
                                v.real = Fmi2ScalarVariable.Real().also { i ->
                                    (field.get(this) as? Double)?.also { i.start = it }
                                }
                            }
                        }
                        Boolean::class, Boolean::class.java -> {
                            variables[vr] = BoolVar({field.getBoolean(this)}, {field.setBoolean(this, it)})
                            apply { v ->
                                v.boolean = Fmi2ScalarVariable.Boolean().also { i ->
                                    (field.get(this) as? Boolean)?.also { i.isStart = it }
                                }
                            }
                        }
                        String::class, String::class.java -> {
                            variables[vr] = StringVar({field.get(this) as String}, {field.set(this, it)})
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
                            for (i in 0 until annotation.size) {
                                variables[vr] = IntVar({(field.get(this) as IntArray)[i]}, {(field.get(this) as IntArray)[i] = it})
                                apply(i) { v ->
                                    v.integer = Fmi2ScalarVariable.Integer().also { v ->
                                        array?.also { v.start = it[i] }
                                    }
                                }
                            }
                        }
                        DoubleArray::class, DoubleArray::class.java -> {
                            val array = field.get(this) as? DoubleArray
                            for (i in 0 until annotation.size) {
                                variables[vr] = RealVar({(field.get(this) as DoubleArray)[i]}, {(field.get(this) as DoubleArray)[i] = it})
                                apply(i) { v ->
                                    v.real = Fmi2ScalarVariable.Real().also { v ->
                                        array?.also { v.start = it[i] }
                                    }
                                }
                            }
                        }
                        BooleanArray::class, BooleanArray::class.java -> {
                            val array = field.get(this) as? BooleanArray
                            for (i in 0 until annotation.size) {
                                variables[vr] = BoolVar({(field.get(this) as BooleanArray)[i]}, {(field.get(this) as BooleanArray)[i] = it})
                                apply(i) { v ->
                                    v.boolean = Fmi2ScalarVariable.Boolean().also { v ->
                                        array?.also { v.isStart = it[i] }
                                    }
                                }
                            }
                        }
                        Array<String>::class, Array<String>::class.java -> {
                            val array = field.get(this) as? Array<String>
                            for (i in 0 until annotation.size) {
                                variables[vr] = StringVar({(field.get(this) as Array<String>)[i]}, {(field.get(this) as Array<String>)[i] = it})
                                apply(i) { v ->
                                    v.string = Fmi2ScalarVariable.String().also { v ->
                                        array?.also { v.start = it[i] }
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
