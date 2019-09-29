package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

abstract class Fmi2Slave {

    private val defined = AtomicBoolean(false)
    private val vrRef = AtomicLong(0L)
    private val accessors = mutableMapOf<Long, Accessor<*>>()
    protected val modelDescription = Fmi2ModelDescription()

    fun setupExperiment(startTime: Double): Boolean {
        println("setupExperiment, startTime=$startTime")
        return true
    }

    fun enterInitialisationMode(): Boolean {
        println("enterInitialisationMode")
        return true
    }

    fun exitInitialisationMode(): Boolean {
        println("exitInitialisationMode")
        return true
    }

    abstract fun doStep(currentTime: Double, dt: Double): Boolean

    fun reset(): Boolean {
        return false
    }

    fun terminate(): Boolean {
        return true
    }

    @Suppress("UNCHECKED_CAST")
    fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            (accessors.getValue(vr[i]) as RealAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            (accessors.getValue(vr[i]) as RealAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInteger(vr: LongArray): IntArray {
        return IntArray(vr.size) { i ->
            (accessors.getValue(vr[i]) as IntAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            (accessors.getValue(vr[i]) as IntAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            (accessors.getValue(vr[i]) as BoolAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            (accessors.getValue(vr[i]) as BoolAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            (accessors.getValue(vr[i]) as StringAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            (accessors.getValue(vr[i]) as StringAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    fun define(): Fmi2Slave {

        if (defined.getAndSet(true)) {
            return this
        }

        val md = modelDescription
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

        javaClass.declaredFields.forEach { field ->

            field.getAnnotation(ScalarVariable::class.java)?.also { annotation ->

                field.isAccessible = true

                fun apply(f: (Fmi2ScalarVariable) -> Unit): Fmi2ScalarVariable {
                    return Fmi2ScalarVariable().apply {
                        name = field.name
                        valueReference = vrRef.getAndIncrement()
                        causality = annotation.causality
                        variability = annotation.variability
                        initial = if (annotation.initial == Fmi2Initial.undefined) null else (annotation.initial)
                        f.invoke(this)
                    }.also { md.modelVariables.scalarVariable.add(it) }
                }

                fun apply(index: Int, f: (Fmi2ScalarVariable) -> Unit) {
                    apply(f).apply {
                        name = "${field.name}[$index]"
                    }
                }

                when (val type = field.type) {
                    Int::class, Int::class.java -> {
                        apply { v ->
                            v.integer = Fmi2ScalarVariable.Integer().also { i ->
                                (field.get(this) as? Int)?.also { i.start = it }
                            }
                            accessors[v.valueReference] = IntAccessor({ field.getInt(this) }, { field.setInt(this, it) })
                        }
                    }
                    Double::class, Double::class.java -> {
                        apply { v ->
                            v.real = Fmi2ScalarVariable.Real().also { i ->
                                (field.get(this) as? Double)?.also { i.start = it }
                            }
                            accessors[v.valueReference] = RealAccessor({ field.getDouble(this) }, { field.setDouble(this, it) })
                        }
                    }
                    Boolean::class, Boolean::class.java -> {
                        apply { v ->
                            v.boolean = Fmi2ScalarVariable.Boolean().also { i ->
                                (field.get(this) as? Boolean)?.also { i.isStart = it }
                            }
                            accessors[v.valueReference] = BoolAccessor({ field.getBoolean(this) }, { field.setBoolean(this, it) })
                        }
                    }
                    String::class, String::class.java -> {
                        apply { v ->
                            v.string = Fmi2ScalarVariable.String().also { i ->
                                (field.get(this) as? String)?.also { i.start = it }
                            }
                            accessors[v.valueReference] = StringAccessor({ field.get(this) as String }, { field.set(this, it) })
                        }
                    }
                    IntArray::class, IntArray::class.java -> {
                        val array = field.get(this) as? IntArray
                                ?: throw IllegalStateException("${field.name} cannot be null!")
                        for (i in array.indices) {
                            apply(i) { v ->
                                v.integer = Fmi2ScalarVariable.Integer().also { integer ->
                                    integer.start = array[i]
                                }
                                accessors[v.valueReference] = IntAccessor({ array[i] }, { array[i] = it })
                            }
                        }
                    }
                    DoubleArray::class, DoubleArray::class.java -> {
                        val array = field.get(this) as? DoubleArray
                                ?: throw IllegalStateException("${field.name} cannot be null!")
                        for (i in array.indices) {
                            apply(i) { v ->
                                v.real = Fmi2ScalarVariable.Real().also { real ->
                                    real.start = array[i]
                                }
                                accessors[v.valueReference] = RealAccessor({ array[i] }, { array[i] = it })
                            }
                        }
                    }
                    BooleanArray::class, BooleanArray::class.java -> {
                        val array = field.get(this) as? BooleanArray
                                ?: throw IllegalStateException("${field.name} cannot be null!")
                        for (i in array.indices) {
                            apply(i) { v ->
                                v.boolean = Fmi2ScalarVariable.Boolean().also { boolean ->
                                    boolean.isStart = array[i]
                                }
                                accessors[v.valueReference] = BoolAccessor({ array[i] }, { array[i] = it })
                            }
                        }
                    }
                    Array<String>::class, Array<String>::class.java -> {
                        @Suppress("UNCHECKED_CAST")
                        val array = field.get(this) as? Array<String>
                                ?: throw IllegalStateException("${field.name} cannot be null!")
                        for (i in array.indices) {
                            apply(i) { v ->
                                v.string = Fmi2ScalarVariable.String().also { string ->
                                    string.start = array[i]
                                }
                                accessors[v.valueReference] = StringAccessor({ array[i] }, { array[i] = it })
                            }
                        }
                    }

                    else -> {

                        when {
                            IntVector::class.java.isAssignableFrom(type) -> {
                                val vector = field.get(this) as? IntVector
                                        ?: throw IllegalStateException("${field.name} cannot be null!")
                                for (i in 0 until vector.size) {
                                    apply(i) { v ->
                                        v.integer = Fmi2ScalarVariable.Integer().also { integer ->
                                            integer.start = vector[i]
                                        }
                                        accessors[v.valueReference] = IntAccessor({ vector[i] }, { vector[i] = it })
                                    }

                                }
                            }
                            RealVector::class.java.isAssignableFrom(type) -> {

                                val vector = field.get(this) as? RealVector
                                        ?: throw IllegalStateException("${field.name} cannot be null!")
                                for (i in 0 until vector.size) {
                                    apply(i) { v ->
                                        v.real = Fmi2ScalarVariable.Real().also { real ->
                                            real.start = vector[i]
                                        }
                                        accessors[v.valueReference] = RealAccessor({ vector[i] }, { vector[i] = it })
                                    }

                                }

                            }
                            else -> throw IllegalStateException("Unsupported variable type: $type")
                        }
                    }
                }

            }

        }

        val outputs = md.modelVariables.scalarVariable.filter { it.causality == Fmi2Causality.output }
        md.modelStructure = Fmi2ModelDescription.ModelStructure().also { ms ->
            if (outputs.isNotEmpty()) {
                ms.outputs = Fmi2VariableDependency()
                outputs.forEachIndexed { i, _ ->
                    ms.outputs.unknown.add(Fmi2VariableDependency.Unknown().also { u -> u.index = i.toLong() })
                }
            }
        }

        check(md.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

        return this
    }
}


sealed class Accessor<T>(
        val getter: () -> T,
        val setter: ((T) -> Unit)?
)

class IntAccessor(
        getter: () -> Int,
        setter: ((Int) -> Unit)?
) : Accessor<Int>(getter, setter)

class RealAccessor(
        getter: () -> Double,
        setter: ((Double) -> Unit)?
) : Accessor<Double>(getter, setter)

class BoolAccessor(
        getter: () -> Boolean,
        setter: ((Boolean) -> Unit)?
) : Accessor<Boolean>(getter, setter)

class StringAccessor(
        getter: () -> String,
        setter: ((String) -> Unit)?
) : Accessor<String>(getter, setter)
