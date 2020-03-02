package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.*
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger
import javax.xml.bind.JAXB

private const val MAX_LEVEL = 8

abstract class Fmi2Slave(
        val instanceName: String
) {

    val modelDescription = Fmi2ModelDescription()
    private val intAccessors: MutableList<IntAccessor> = mutableListOf()
    private val realAccessors: MutableList<RealAccessor> = mutableListOf()
    private val boolAccessors: MutableList<BoolAccessor> = mutableListOf()
    private val stringAccessors: MutableList<StringAccessor> = mutableListOf()

    private val isDefined = AtomicBoolean(false)
    private lateinit var resourceLocation: String

    fun getFmuResource(name: String): File {
        return File(resourceLocation, name)
    }

    fun getVariableName(vr: Long, type: Fmi2VariableType): Long {
        return modelDescription.modelVariables.scalarVariable
                .firstOrNull { it.valueReference == vr && it.type() == type }?.valueReference
                ?: throw IllegalArgumentException("No such variable with valueReference $vr!")
    }

    fun getValueReference(name: String): Long {
        return modelDescription.modelVariables.scalarVariable
                .firstOrNull { it.name == name }?.valueReference
                ?: throw IllegalArgumentException("No such variable with name $name!")
    }

    val modelDescriptionXml: String by lazy {
        String(ByteArrayOutputStream().also { bos ->
            JAXB.marshal(modelDescription, bos)
        }.toByteArray())
    }

    open fun setupExperiment(startTime: Double) {}
    open fun enterInitialisationMode() {}
    open fun exitInitialisationMode() {}

    abstract fun doStep(currentTime: Double, dt: Double)
    open fun terminate() {}

    @Suppress("UNCHECKED_CAST")
    fun getReal(vr: Long): Double {
        return realAccessors[vr.toInt()].getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            realAccessors[vr[i].toInt()].getter()
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            realAccessors[vr[i].toInt()].apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.REAL)} on variable without a specified setter!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInteger(vr: Long): Int {
        return intAccessors[vr.toInt()].getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getInteger(vr: LongArray): IntArray {
        return IntArray(vr.size) { i ->
            intAccessors[vr[i].toInt()].getter()
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            intAccessors[vr[i].toInt()].apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.INTEGER)} on variable without a specified setter!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getBoolean(vr: Long): Boolean {
        return boolAccessors[vr.toInt()].getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            boolAccessors[vr[i].toInt()].getter()
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            boolAccessors[vr[i].toInt()].apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.BOOLEAN)} on variable without a specified setter!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getString(vr: Long): String {
        return stringAccessors[vr.toInt()].getter()
    }

    @Suppress("UNCHECKED_CAST")
    open fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            stringAccessors[vr[i].toInt()].getter()
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            stringAccessors[vr[i].toInt()].apply {
                setter?.invoke(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.STRING)} on variable without a specified setter!")
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
            v.causality?.also { s.causality = it }
            v.variability?.also { s.variability = it }
            v.initial?.also { if (v.initial != Fmi2Initial.undefined) s.initial = it }
            modelDescription.modelVariables.scalarVariable.add(s)
        }

    }

    protected fun registerInteger(int: IntBuilder) {
        val build = int.build()
        internalRegister(build).also { v ->
            v.valueReference = intAccessors.size.toLong().also {
                intAccessors.add(build.accessor)
            }
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
            v.valueReference = realAccessors.size.toLong()
            realAccessors.add(build.accessor)
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
            v.valueReference = boolAccessors.size.toLong()
            boolAccessors.add(build.accessor)
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
            v.valueReference = stringAccessors.size.toLong()
            stringAccessors.add(build.accessor)
            v.string = Fmi2ScalarVariable.String().also { type ->
                if (requiresStart(v)) {
                    build.accessor.getter.invoke().also {
                        type.start = it
                    }
                }
            }
        }
    }

    private fun processMethods(owner: Any, methods: Array<Method>) {

        fun variableName(method: Method): String? {
            val methodName = method.name
            return when {
                methodName.startsWith("get") -> {
                    methodName
                            .replaceFirst("get", "")
                            .replace("_", ".")
                            .decapitalize()
                }
                methodName.startsWith("set") -> {
                    methodName
                            .replaceFirst("set", "")
                            .replace("_", ".")
                            .decapitalize()
                }
                else -> null
            }
        }

        fun hasSetter(method: Method, getterAnnotation: ScalarVariableGetter): Boolean {
            val causality = getterAnnotation.causality
            val variability = getterAnnotation.variability

            if (!method.returnType.isPrimitive) return false

            return (causality == Fmi2Causality.input) ||
                    (causality == Fmi2Causality.parameter && variability != Fmi2Variability.constant)
        }

        methods.forEach { getterMethod ->
            getterMethod.annotations.mapNotNull {
                if (it is ScalarVariableGetter) it else null
            }.forEach { getterAnnotation ->

                val variableName = variableName(getterMethod)
                        ?: throw IllegalStateException("Illegal method name: ${getterMethod.name}")

                val hasSetter = hasSetter(getterMethod, getterAnnotation)
                val setterMethod = if (hasSetter) {
                    methods.find {
                        variableName(it) == variableName && it.getAnnotation(ScalarVariableSetter::class.java) != null
                    }!!.also {
                        check(it.parameterCount == 1)
                        check(it.parameterTypes[0] == getterMethod.returnType)
                    }
                } else {
                    null
                }

                when (val type = getterMethod.returnType) {
                    Int::class, Int::class.java -> {
                        registerInteger(IntBuilder(variableName).also {
                            it.getter { getterMethod.invoke(owner) as Int }
                            if (hasSetter) {
                                it.setter { value -> setterMethod!!.invoke(owner, value) }
                            }
                            it.apply(getterAnnotation)
                        })
                    }
                    Double::class, Double::class.java -> {
                        registerReal(RealBuilder(variableName).also {
                            it.getter { getterMethod.invoke(owner) as Double }
                            if (hasSetter) {
                                it.setter { value -> setterMethod!!.invoke(owner, value) }
                            }
                            it.apply(getterAnnotation)
                        })
                    }
                    Boolean::class, Boolean::class.java -> {
                        registerBoolean(BooleanBuilder(variableName).also {
                            it.getter { getterMethod.invoke(owner) as Boolean }
                            if (hasSetter) {
                                it.setter { value -> setterMethod!!.invoke(owner, value) }
                            }
                            it.apply(getterAnnotation)
                        })
                    }
                    String::class, String::class.java -> {
                        registerString(StringBuilder(variableName).also {
                            it.getter { getterMethod.invoke(owner) as String }
                            if (hasSetter) {
                                it.setter { value -> setterMethod!!.invoke(owner, value) }
                            }
                            it.apply(getterAnnotation)
                        })
                    }
                    else -> when {
                        IntVector::class.java.isAssignableFrom(type) -> {
                            val vector = getterMethod.invoke(owner) as? IntVector
                                    ?: throw IllegalStateException("Invoking $getterMethod resulted in a NPE!")
                            for (i in 0 until vector.size) {
                                registerInteger(IntBuilder("$variableName[$i]").also {
                                    it.getter { vector[i] }
                                    it.setter { value -> vector[i] = value }
                                    it.apply(getterAnnotation)
                                })
                            }
                        }
                        RealVector::class.java.isAssignableFrom(type) -> {
                            val vector = getterMethod.invoke(owner) as? RealVector
                                    ?: throw IllegalStateException("Invoking $getterMethod resulted in a NPE!")
                            for (i in 0 until vector.size) {
                                registerReal(RealBuilder("$variableName[$i]").also {
                                    it.getter { vector[i] }
                                    it.setter { value -> vector[i] = value }
                                    it.apply(getterAnnotation)
                                })
                            }
                        }
                        else -> throw IllegalStateException("Unsupported variable type: $type")
                    }
                }

            }

        }

    }

    private fun processAnnotatedField(owner: Any, field: Field, annotation: ScalarVariable, prepend: String) {

        field.isAccessible = true
        val isFinal = Modifier.isFinal(field.modifiers)

        check(!(isFinal && annotation.causality == Fmi2Causality.input))
        { "${field.name}: Illegal combination: final modifier and causality=input " }
        check(!(isFinal && annotation.causality == Fmi2Causality.parameter))
        { "${field.name}: Illegal combination: final modifier and causality=parameter " }
        check(!(isFinal && annotation.causality == Fmi2Causality.calculatedParameter))
        { "${field.name}: Illegal combination: final modifier and causality=calculatedParameter " }

        when (val type = field.type) {
            Int::class, Int::class.java -> {
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                registerInteger(IntBuilder("$prepend$variableName").also {
                    it.getter { field.getInt(owner) }
                    if (!isFinal) {
                        it.setter { value -> field.setInt(owner, value) }
                    }
                    it.apply(annotation)
                })
            }
            Double::class, Double::class.java -> {
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                registerReal(RealBuilder("$prepend$variableName").also {
                    it.getter { field.getDouble(owner) }
                    if (!isFinal) {
                        it.setter { value -> field.setDouble(owner, value) }
                    }
                    it.apply(annotation)
                })
            }
            Boolean::class, Boolean::class.java -> {
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                registerBoolean(BooleanBuilder("$prepend$variableName").also {
                    it.getter { field.getBoolean(owner) }
                    if (!isFinal) {
                        it.setter { value -> field.setBoolean(owner, value) }
                    }
                    it.apply(annotation)
                })
            }
            String::class, String::class.java -> {
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                registerString(StringBuilder("$prepend$variableName").also {
                    it.getter { field.get(owner) as? String ?: "" }
                    if (!isFinal) {
                        it.setter { value -> field.set(owner, value) }
                    }
                    it.apply(annotation)
                })
            }
            IntArray::class.java -> {
                val array = field.get(owner) as? IntArray
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                for (i in array.indices) {
                    registerInteger(IntBuilder("$prepend$variableName[$i]").also {
                        it.getter { array[i] }
                        it.setter { value -> array[i] = value }
                        it.apply(annotation)
                    })
                }
            }
            DoubleArray::class.java -> {
                val array = field.get(this) as? DoubleArray
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                for (i in array.indices) {
                    registerReal(RealBuilder("$prepend$variableName[$i]").also {
                        it.getter { array[i] }
                        it.setter { value -> array[i] = value }
                        it.apply(annotation)
                    })
                }
            }
            BooleanArray::class.java -> {
                val array = field.get(owner) as? BooleanArray
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                for (i in array.indices) {
                    registerBoolean(BooleanBuilder("$prepend$variableName[$i]").also {
                        it.getter { array[i] }
                        it.setter { value -> array[i] = value }
                        it.apply(annotation)
                    })
                }
            }
            Array<String>::class.java -> {
                @Suppress("UNCHECKED_CAST")
                val array = field.get(owner) as? Array<String>
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                for (i in array.indices) {
                    registerString(StringBuilder("$prepend$variableName[$i]").also {
                        it.getter { array[i] }
                        it.setter { value -> array[i] = value }
                        it.apply(annotation)
                    })
                }
            }
            else -> {
                when {
                    IntVector::class.java.isAssignableFrom(type) -> {
                        val vector = field.get(owner) as? IntVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                        val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                        for (i in 0 until vector.size) {
                            registerInteger(IntBuilder("$prepend$variableName[$i]").also {
                                it.getter { vector[i] }
                                it.setter { value -> vector[i] = value }
                                it.apply(annotation)
                            })
                        }
                    }
                    RealVector::class.java.isAssignableFrom(type) -> {
                        val vector = field.get(owner) as? RealVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                        val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                        for (i in 0 until vector.size) {
                            registerReal(RealBuilder("$prepend$variableName[$i]").also {
                                it.getter { vector[i] }
                                it.setter { value -> vector[i] = value }
                                it.apply(annotation)
                            })
                        }
                    }
                    BooleanVector::class.java.isAssignableFrom(type) -> {
                        val vector = field.get(owner) as? BooleanVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                        val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                        for (i in 0 until vector.size) {
                            registerBoolean(BooleanBuilder("$prepend$variableName[$i]").also {
                                it.getter { vector[i] }
                                it.setter { value -> vector[i] = value }
                                it.apply(annotation)
                            })
                        }
                    }
                    StringVector::class.java.isAssignableFrom(type) -> {
                        val vector = field.get(owner) as? StringVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                        val variableName = if (annotation.name.isNotEmpty()) annotation.name else field.name
                        for (i in 0 until vector.size) {
                            registerString(StringBuilder("$prepend$variableName[$i]").also {
                                it.getter { vector[i] }
                                it.setter { value -> vector[i] = value }
                                it.apply(annotation)
                            })
                        }
                    }
                    else -> throw IllegalStateException("Unsupported variable type: $type")
                }
            }
        }

    }

    private fun searchForVariables(cls: Class<*>, owner: Any = this, prepend: String = "", level: Int = 0) {

        if (level > MAX_LEVEL) return

        cls.declaredFields.forEach { field ->

            field.getAnnotation(VariableContainer::class.java)?.also {
                field.isAccessible = true
                field.get(owner)?.also {
                    searchForVariables(field.type, it, "$prepend${field.name}.", level + 1)
                }
            }

            field.getAnnotation(ScalarVariable::class.java)?.also { annotation ->
                processAnnotatedField(owner, field, annotation, prepend)
            }

        }

        processMethods(owner, cls.declaredMethods.apply { sortBy { it.name } })

    }

    private fun getDateAndTime(): String {
        val now = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now)
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").format(now)
        return "${dateFormat}T${timeFormat}Z"
    }

    fun __define__(): Fmi2Slave {

        if (isDefined.getAndSet(true)) {
            return this
        }

        modelDescription.fmiVersion = "2.0"
        modelDescription.generationTool = "FMI4j"
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
            searchForVariables(cls!!)
            cls = cls.superclass
        } while (cls != null)

        val outputs = modelDescription.modelVariables.scalarVariable.mapIndexedNotNull { i, v ->
            if (v.causality == Fmi2Causality.output) i.toLong() else null
        }
        modelDescription.modelStructure = Fmi2ModelDescription.ModelStructure().also { ms ->
            if (outputs.isNotEmpty()) {
                ms.outputs = Fmi2VariableDependency()
                outputs.forEach {
                    ms.outputs.unknown.add(Fmi2VariableDependency.Unknown().also { u -> u.index = it + 1 })
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
