package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.IntVector
import no.ntnu.ihb.fmi4j.export.RealVector
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger
import javax.xml.bind.JAXB
import kotlin.properties.Delegates

private const val MAX_LEVEL = 8

abstract class Fmi2Slave(
        val instanceName: String
) {

    val modelDescription = Fmi2ModelDescription()
    private val accessors = mutableListOf<Accessor<*>>()

    private val isDefined = AtomicBoolean(false)
    private lateinit var resourceLocation: String

    fun getFmuResource(name: String): File {
        return File(resourceLocation, name)
    }

    fun getVariableName(vr: Long): Long {
        return modelDescription.modelVariables.scalarVariable
                .firstOrNull { it.valueReference == vr }?.valueReference
                ?: throw IllegalArgumentException("No such variable with valueReference $vr!")
    }

    fun getValueReference(name: String): Long {
        return modelDescription.modelVariables.scalarVariable
                .firstOrNull { it.name == name }?.valueReference
                ?: throw IllegalArgumentException("No such variable with name $name!")
    }

    val modelDescriptionXml by lazy {
        val bos = ByteArrayOutputStream()
        JAXB.marshal(modelDescription, bos)
        String((bos.toByteArray()))
    }

    open fun setupExperiment(startTime: Double) {}
    open fun enterInitialisationMode() {}
    open fun exitInitialisationMode() {}

    abstract fun doStep(currentTime: Double, dt: Double)
    open fun reset() {}
    open fun terminate() {}

    @Suppress("UNCHECKED_CAST")
    fun getReal(vr: Long): Double {
        return (accessors[vr.toInt()] as RealAccessor).getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            (accessors[vr[i].toInt()] as RealAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            (accessors[vr[i].toInt()] as RealAccessor).apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i])} on variable without a specified setter!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInteger(vr: Long): Int {
        return (accessors[vr.toInt()] as IntAccessor).getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getInteger(vr: LongArray): IntArray {
        return IntArray(vr.size) { i ->
            (accessors[vr[i].toInt()] as IntAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            (accessors[vr[i].toInt()] as IntAccessor).apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i])} on variable without a specified setter!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getBoolean(vr: Long): Boolean {
        return (accessors[vr.toInt()] as BoolAccessor).getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            (accessors[vr[i].toInt()] as BoolAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            (accessors[vr[i].toInt()] as BoolAccessor).apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i])} on variable without a specified setter!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getString(vr: Long): String {
        return (accessors[vr.toInt()] as StringAccessor).getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            (accessors[vr[i].toInt()] as StringAccessor).let {
                it.getter()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            (accessors[vr[i].toInt()] as StringAccessor).apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i])} on variable without a specified setter!")
            }
        }
    }

    private fun requiresStart(v: Fmi2ScalarVariable): Boolean {
        return v.initial == Fmi2Initial.exact ||
                v.initial == Fmi2Initial.approx ||
                v.causality == Fmi2Causality.input ||
                v.causality == Fmi2Causality.parameter ||
                v.variability == Fmi2Variability.constant
    }

    private fun internalRegister(v: Variable<*>): Fmi2ScalarVariable {
        return Fmi2ScalarVariable().also { s ->
            s.name = v.name
            s.valueReference = accessors.size.toLong()
            v.causality?.also { s.causality = it }
            v.variability?.also { s.variability = it }
            v.initial?.also { if (v.initial != Fmi2Initial.undefined) s.initial = it }
            accessors.add(v.accessor)
            modelDescription.modelVariables.scalarVariable.add(s)
        }

    }

    protected fun registerInteger(int: IntBuilder) {
        val build = int.build()
        internalRegister(build).also { v ->
            v.integer = Fmi2ScalarVariable.Integer().also { type ->
                if (requiresStart(v)) {
                    build.accessor.getter.invoke().also {
                        type.start = it
                    }
                }
            }
        }
    }

    protected fun registerReal(real: RealBuilder) {
        val build = real.build()
        internalRegister(build).also { v ->
            v.real = Fmi2ScalarVariable.Real().also { type ->
                if (requiresStart(v)) {
                    build.accessor.getter.invoke().also {
                        type.start = it
                    }
                }
            }
        }
    }

    protected fun registerBoolean(real: BooleanBuilder) {
        val build = real.build()
        internalRegister(build).also { v ->
            v.boolean = Fmi2ScalarVariable.Boolean().also { type ->
                if (requiresStart(v)) {
                    build.accessor.getter.invoke().also {
                        type.isStart = it
                    }
                }
            }
        }
    }

    protected fun registerString(real: StringBuilder) {
        val build = real.build()
        internalRegister(build).also { v ->
            v.string = Fmi2ScalarVariable.String().also { type ->
                if (requiresStart(v)) {
                    build.accessor.getter.invoke().also {
                        type.start = it
                    }
                }
            }
        }
    }

    private fun checkFields(cls: Class<*>, owner: Any = this, prepend: String = "", level: Int = 0) {

        if (level > MAX_LEVEL) return

        cls.declaredFields.forEach { field ->

            field.getAnnotation(VariableContainer::class.java)?.also {
                field.isAccessible = true
                field.get(owner)?.also {
                    checkFields(field.type, it, "$prepend${field.name}.", level + 1)
                }
            }

            field.getAnnotation(ScalarVariable::class.java)?.also { annotation ->

                field.isAccessible = true
                val isFinal = Modifier.isFinal(field.modifiers)

                check(!(isFinal && annotation.causality == Fmi2Causality.input))
                { "${field.name}: Illegal combination: final modifier and causality=input " }

                when (val type = field.type) {
                    Int::class, Int::class.java -> {
                        registerInteger(IntBuilder("$prepend${field.name}").also {
                            it.getter { field.getInt(owner) }
                            if (!isFinal) {
                                it.setter { field.setInt(owner, it) }
                            }
                            it.apply(annotation)
                        })
                    }
                    Double::class, Double::class.java -> {
                        registerReal(RealBuilder("$prepend${field.name}").also {
                            it.getter { field.getDouble(owner) }
                            if (!isFinal) {
                                it.setter { field.setDouble(owner, it) }
                            }
                            it.apply(annotation)
                        })
                    }
                    Boolean::class, Boolean::class.java -> {
                        registerBoolean(BooleanBuilder("$prepend${field.name}").also {
                            it.getter { field.getBoolean(owner) }
                            if (!isFinal) {
                                it.setter { field.setBoolean(owner, it) }
                            }
                            it.apply(annotation)
                        })
                    }
                    String::class, String::class.java -> {
                        registerString(StringBuilder("$prepend${field.name}").also {
                            it.getter { field.get(owner) as? String ?: "" }
                            if (!isFinal) {
                                it.setter { field.set(owner, it) }
                            }
                            it.apply(annotation)
                        })
                    }
                    IntArray::class.java -> {
                        val array = field.get(owner) as? IntArray
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerInteger(IntBuilder("$prepend${field.name}[$i]").also {
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
                            registerReal(RealBuilder("$prepend${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    BooleanArray::class.java -> {
                        val array = field.get(owner) as? BooleanArray
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerBoolean(BooleanBuilder("$prepend${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    Array<String>::class.java -> {
                        @Suppress("UNCHECKED_CAST")
                        val array = field.get(owner) as? Array<String>
                                ?: throw IllegalStateException("Field $field.name cannot be null!")
                        for (i in array.indices) {
                            registerString(StringBuilder("$prepend${field.name}[$i]").also {
                                it.getter { array[i] }
                                it.setter { array[i] = it }
                                it.apply(annotation)
                            })
                        }
                    }
                    else -> {
                        when {
                            IntVector::class.java.isAssignableFrom(type) -> {
                                val vector = field.get(owner) as? IntVector
                                        ?: throw IllegalStateException("Field $field.name cannot be null!")
                                for (i in 0 until vector.size) {
                                    registerInteger(IntBuilder("$prepend${field.name}[$i]").also {
                                        it.getter { vector[i] }
                                        it.setter { vector[i] = it }
                                    })
                                }
                            }
                            RealVector::class.java.isAssignableFrom(type) -> {
                                val vector = field.get(owner) as? RealVector
                                        ?: throw IllegalStateException("Field $field.name cannot be null!")
                                for (i in 0 until vector.size) {
                                    registerReal(RealBuilder("$prepend${field.name}[$i]").also {
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

    }

    private fun getDateAndTime(): String {
        val now = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now)
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").format(now)
        return "${dateFormat}T${timeFormat}Z"
    }

    protected open fun define() {}

    fun __define__(): Fmi2Slave {

        if (isDefined.getAndSet(true)) {
            return this
        }

        define()

        modelDescription.fmiVersion = "2.0"
        modelDescription.generationTool = "fmi4j"
        modelDescription.variableNamingConvention = "structured"
        modelDescription.guid = UUID.randomUUID().toString()
        modelDescription.generationDateAndTime = getDateAndTime()

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
            cs.isCanNotUseMemoryManagementFunctions = true
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

        var cls: Class<*>? = javaClass
        do {
            checkFields(cls!!)
            cls = cls.superclass
        } while (cls != null)

        val outputs = modelDescription.modelVariables.scalarVariable.filter { it.causality == Fmi2Causality.output }
        modelDescription.modelStructure = Fmi2ModelDescription.ModelStructure().also { ms ->
            if (outputs.isNotEmpty()) {
                ms.outputs = Fmi2VariableDependency()
                outputs.forEachIndexed { i, _ ->
                    ms.outputs.unknown.add(Fmi2VariableDependency.Unknown().also { u -> u.index = i.toLong() + 1 })
                }
            }
        }

        check(modelDescription.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

        return this

    }

    private companion object {
        private val LOG: Logger = Logger.getLogger(Fmi2Slave::class.java.name)
    }

}

internal class Accessor<T>(
        val getter: () -> T,
        val setter: ((T) -> Unit)?
)
internal typealias IntAccessor = Accessor<Int>
internal typealias RealAccessor = Accessor<Double>
internal typealias BoolAccessor = Accessor<Boolean>
internal typealias StringAccessor = Accessor<String>

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
