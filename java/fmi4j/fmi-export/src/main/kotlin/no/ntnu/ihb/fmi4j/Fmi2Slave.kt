package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class Fmi2Slave {

    private val defined = AtomicBoolean(false)
    private val accessors = mutableListOf<Accessor<*>>()
    protected val modelDescription = Fmi2ModelDescription()

    open fun setupExperiment(startTime: Double): Boolean {
        println("setupExperiment, startTime=$startTime")
        return true
    }

    open fun enterInitialisationMode(): Boolean {
        println("enterInitialisationMode")
        return true
    }

    open fun exitInitialisationMode(): Boolean {
        println("exitInitialisationMode")
        return true
    }

    abstract fun doStep(currentTime: Double, dt: Double): Boolean

    open fun reset(): Boolean {
        return false
    }

    open fun terminate(): Boolean {
        return true
    }

    @Suppress("UNCHECKED_CAST")
    fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            (accessors[i] as RealAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            (accessors[i] as RealAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInteger(vr: LongArray): IntArray {
        return IntArray(vr.size) { i ->
            (accessors[i] as IntAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            (accessors[i] as IntAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            (accessors[i] as BoolAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            (accessors[i] as BoolAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            (accessors[i] as StringAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            (accessors[i] as StringAccessor).apply {
                setter?.invoke(values[i])
            }
        }
    }

    private fun internalRegister(v: Variable<*>): Fmi2ScalarVariable {
        return Fmi2ScalarVariable().also { s ->
            s.name = v.name
            s.valueReference = accessors.size.toLong()
            v.causality?.also { s.causality = it }
            v.variability?.also { s.variability = it }
            v.initial?.also { if (s.initial != Fmi2Initial.undefined) s.initial = it }

            accessors.add(v.accessor)
            modelDescription.modelVariables.scalarVariable.add(s)
        }

    }

    protected fun registerInteger(int: IntBuilder) {
        val build = int.build()
        internalRegister(build).also { v ->
            v.integer = Fmi2ScalarVariable.Integer().also { type ->
                type.start = build.accessor.getter.invoke()
            }
        }
    }

    protected fun registerReal(real: RealBuilder) {
        val build = real.build()
        internalRegister(build).also { v ->
            v.real = Fmi2ScalarVariable.Real().also { type ->
                type.start = build.accessor.getter.invoke()
            }
        }
    }

    protected fun registerBoolean(real: BooleanBuilder) {
        val build = real.build()
        internalRegister(build).also { v ->
            v.boolean = Fmi2ScalarVariable.Boolean().also { type ->
                type.isStart = build.accessor.getter.invoke()
            }
        }
    }

    protected fun registerString(real: StringBuilder) {
        val build = real.build()
        internalRegister(build).also { v ->
            v.string = Fmi2ScalarVariable.String().also { real ->
                real.start = build.accessor.getter.invoke()
            }
        }
    }

    fun define(): Fmi2Slave {

        if (defined.getAndSet(true)) {
            return this
        }

        modelDescription.fmiVersion = "2.0"
        modelDescription.generationTool = "fmi4j"
        modelDescription.variableNamingConvention = "structured"
        modelDescription.guid = UUID.randomUUID().toString()

        val slaveInfo = javaClass.getAnnotation(SlaveInfo::class.java)

        modelDescription.modelName = slaveInfo?.modelName ?: javaClass.simpleName
        if (slaveInfo != null) {
            modelDescription.author = if (slaveInfo.author.isNotEmpty()) slaveInfo.author else null
            modelDescription.version = if (slaveInfo.version.isNotEmpty()) slaveInfo.version else null
            modelDescription.copyright = if (slaveInfo.copyright.isNotEmpty()) slaveInfo.copyright else null
            modelDescription.license = if (slaveInfo.license.isNotEmpty()) slaveInfo.license else null
            modelDescription.description = if (slaveInfo.description.isNotEmpty()) slaveInfo.description else null
        }

        modelDescription.modelVariables = Fmi2ModelDescription.ModelVariables()
        modelDescription.coSimulation = Fmi2ModelDescription.CoSimulation().also { cs ->
            cs.isCanGetAndSetFMUstate = false
            cs.isCanSerializeFMUstate = false
            cs.isCanInterpolateInputs = false
            cs.modelIdentifier = modelDescription.modelName
            if (slaveInfo != null) {
                cs.isNeedsExecutionTool = slaveInfo.needsExecutionTool
                cs.isCanBeInstantiatedOnlyOncePerProcess = slaveInfo.canBeInstantiatedOnlyOncePerProcess
                cs.isCanHandleVariableCommunicationStepSize = slaveInfo.canHandleVariableCommunicationStepSize
            }
        }

        javaClass.getAnnotation(DefaultExperiment::class.java)?.also { de ->
            modelDescription.defaultExperiment = Fmi2ModelDescription.DefaultExperiment().apply {
                if (de.startTime >= 0) startTime = de.startTime
                if (de.stepSize > 0) stepSize = de.stepSize
                if (de.stopTime > startTime) stopTime = de.stopTime
            }
        }

        javaClass.declaredFields.forEach { field ->

            field.getAnnotation(ScalarVariable::class.java)?.also { annotation ->

                field.isAccessible = true

                when (val type = field.type) {
                    Int::class, Int::class.java -> {
                        registerInteger(IntBuilder(field.name).also {
                            it.getter { field.getInt(this) }
                            it.setter { field.setInt(this, it) }
                            it.apply(annotation)
                        })
                    }
                    Double::class, Double::class.java -> {
                        registerReal(RealBuilder(field.name).also {
                            it.getter { field .getDouble(this) }
                            it.setter { field.setDouble(this, it) }
                            it.apply(annotation)
                        })
                    }
                    Boolean::class, Boolean::class.java -> {
                        registerBoolean(BooleanBuilder(field.name).also {
                            it.getter { field.getBoolean(this) }
                            it.setter { field.setBoolean(this, it) }
                            it.apply(annotation)
                        })
                    }
                    String::class, String::class.java -> {
                        registerString(StringBuilder(field.name).also {
                            it.getter { field.get(this) as? String ?: "" }
                            it.setter { field.set(this, it) }
                            it.apply(annotation)
                        })
                    }
                    IntArray::class.java -> {
                        val array = field.get(this) as? IntArray
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerInteger(IntBuilder("${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    DoubleArray::class.java -> {
                        val array = field.get(this) as? DoubleArray
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerReal(RealBuilder("${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    BooleanArray::class.java -> {
                        val array = field.get(this) as? BooleanArray
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerBoolean(BooleanBuilder("${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    Array<String>::class.java -> {
                        val array = field.get(this) as? Array<String>
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerString(StringBuilder("${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    else -> {
                        when {
                            IntVector::class.java.isAssignableFrom(type) -> {
                                val vector = field.get(this) as? IntVector
                                        ?: throw IllegalStateException("Field $field.name cannot be null!")
                                for (i in 0 until vector.size) {
                                    registerInteger(IntBuilder("${field.name}[$i]").also {
                                        it.getter { vector[i] }
                                        it.setter { vector[i] = it }
                                    })
                                }
                            }
                            RealVector::class.java.isAssignableFrom(type) -> {
                                val vector = field.get(this) as? RealVector
                                        ?: throw IllegalStateException("Field $field.name cannot be null!")
                                for (i in 0 until vector.size) {
                                    registerReal(RealBuilder("${field.name}[$i]").also {
                                        it.getter { vector[i] }
                                        it.setter { vector[i] = it }
                                    })
                                }
                            }
                            else -> throw IllegalStateException("Unsupported variable type: $type")
                        }
                    }
                }

            }

        }

        val outputs = modelDescription.modelVariables.scalarVariable.filter { it.causality == Fmi2Causality.output }
        modelDescription.modelStructure = Fmi2ModelDescription.ModelStructure().also { ms ->
            if (outputs.isNotEmpty()) {
                ms.outputs = Fmi2VariableDependency()
                outputs.forEachIndexed { i, _ ->
                    ms.outputs.unknown.add(Fmi2VariableDependency.Unknown().also { u -> u.index = i.toLong() })
                }
            }
        }

        check(modelDescription.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

        return this
    }

}

internal class Accessor<T>(
        val getter: () -> T,
        val setter: ((T) -> Unit)?
)
internal typealias IntAccessor = Accessor<Int>
internal  typealias RealAccessor = Accessor<Double>
internal  typealias BoolAccessor = Accessor<Boolean>
internal  typealias StringAccessor = Accessor<String>

internal class Variable<E>(
        val name: String,
        val accessor: Accessor<E>,
        val causality: Fmi2Causality?,
        val variability: Fmi2Variability?,
        val initial: Fmi2Initial?
)

class VariableBuilder<E>(
        private val name: String
) {

    private var getter: (() -> E)? = null
    private var setter: ((E) -> Unit)? = null
    private var causality: Fmi2Causality? = null
    private var variability: Fmi2Variability? = null
    private var initial: Fmi2Initial? = null

    fun getter(getter: () -> E): VariableBuilder<E> {
        this.getter = getter
        return this
    }

    fun setter(setter: (E) -> Unit): VariableBuilder<E> {
        this.setter = setter
        return this
    }

    internal fun apply(annotation: ScalarVariable) {
        causality = annotation.causality
        variability = annotation.variability
        initial = annotation.initial
    }

    internal fun build(): Variable<E> {

        val getter = getter
        checkNotNull(getter) { "getter cannot be null!" }

        return Variable(name, Accessor(getter, setter), causality, variability, initial)

    }

}

internal typealias IntBuilder = VariableBuilder<Int>
internal typealias RealBuilder = VariableBuilder<Double>
internal typealias BooleanBuilder = VariableBuilder<Boolean>
internal typealias StringBuilder = VariableBuilder<String>
