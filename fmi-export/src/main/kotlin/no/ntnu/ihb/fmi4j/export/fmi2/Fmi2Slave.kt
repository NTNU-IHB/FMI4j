package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.*
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Logger

abstract class Fmi2Slave(
    args: Map<String, Any>
) : Closeable {

    val modelDescription = Fmi2ModelDescription()
    val instanceName: String = args["instanceName"] as? String
        ?: throw IllegalStateException("Missing 'instanceName'")
    private val resourceLocation: String? = args["resourceLocation"] as? String

    private val intAccessors: MutableList<IntVariable> = mutableListOf()
    private val realAccessors: MutableList<RealVariable> = mutableListOf()
    private val boolAccessors: MutableList<BooleanVariable> = mutableListOf()
    private val stringAccessors: MutableList<StringVariable> = mutableListOf()

    protected open val automaticallyAssignStartValues = true

    val modelDescriptionXml: String by lazy {
        String(ByteArrayOutputStream().use { baos ->
            modelDescription.toXml(baos)
            baos.toByteArray()
        })
    }

    fun getFmuResource(name: String): File {
        return File(resourceLocation, name)
    }

    fun getVariableName(vr: Long, type: Fmi2VariableType): Long {
        return modelDescription.modelVariables.scalarVariable
            .firstOrNull { it.valueReference == vr && it.type() == type }?.valueReference
            ?: throw IllegalArgumentException("No such variable with valueReference $vr!")
    }

    fun getValueRef(name: String): Long {
        return modelDescription.modelVariables.scalarVariable
            .firstOrNull { it.name == name }?.valueReference
            ?: throw IllegalArgumentException("No such variable with name $name!")
    }

    open fun setupExperiment(startTime: Double, stopTime: Double, tolerance: Double) {}
    open fun enterInitialisationMode() {}
    open fun exitInitialisationMode() {}

    abstract fun doStep(currentTime: Double, dt: Double)
    open fun terminate() {}
    override fun close() {}

    open fun getInteger(vr: LongArray): IntArray {
        return IntArray(vr.size) { i ->
            intAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            realAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            boolAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            stringAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun getAll(intVr: LongArray?, realVr: LongArray?, boolVr: LongArray?, strVr: LongArray?): BulkRead {
        return BulkRead(
            intVr?.let { getInteger(it) },
            realVr?.let { getReal(it) },
            boolVr?.let { getBoolean(it) },
            strVr?.let { getString(it) }
        )
    }

    open fun setAll(
        intVr: LongArray?,
        intValues: IntArray?,
        realVr: LongArray?,
        realValues: DoubleArray?,
        boolVr: LongArray?,
        boolValues: BooleanArray?,
        strVr: LongArray?,
        strValues: Array<String>?
    ) {

        if (intVr != null && intValues != null) {
            setInteger(intVr, intValues)
        }
        if (realVr != null && realValues != null) {
            setReal(realVr, realValues)
        }
        if (boolVr != null && boolValues != null) {
            setBoolean(boolVr, boolValues)
        }
        if (strVr != null && strValues != null) {
            setString(strVr, strValues)
        }

    }

    open fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            intAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning(
                    "Trying to set value of " +
                            "${
                                getVariableName(
                                    vr[i],
                                    Fmi2VariableType.INTEGER
                                )
                            } on variable without a specified setter!"
                )
            }
        }
    }

    open fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            realAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning(
                    "Trying to set value of " +
                            "${getVariableName(vr[i], Fmi2VariableType.REAL)} on variable without a specified setter!"
                )
            }
        }
    }

    open fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            boolAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning(
                    "Trying to set value of " +
                            "${
                                getVariableName(
                                    vr[i],
                                    Fmi2VariableType.BOOLEAN
                                )
                            } on variable without a specified setter!"
                )
            }
        }
    }

    open fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            stringAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning(
                    "Trying to set value of " +
                            "${getVariableName(vr[i], Fmi2VariableType.STRING)} on variable without a specified setter!"
                )
            }
        }
    }

    private fun Fmi2ScalarVariable.requiresStart(): Boolean {
        return initial == Fmi2Initial.exact ||
                initial == Fmi2Initial.approx ||
                causality == Fmi2Causality.input ||
                causality == Fmi2Causality.parameter ||
                variability == Fmi2Variability.constant
    }

    protected fun integer(name: String, getter: Getter<Int>) = IntVariable(name, getter)
    protected fun real(name: String, getter: Getter<Double>) = RealVariable(name, getter)
    protected fun boolean(name: String, getter: Getter<Boolean>) = BooleanVariable(name, getter)
    protected fun string(name: String, getter: Getter<String>) = StringVariable(name, getter)


    private fun internalRegister(v: Variable<*>, vr: Long): Fmi2ScalarVariable {
        return Fmi2ScalarVariable().also { s ->
            s.name = v.name
            s.valueReference = vr
            s.description = v.description

            v.causality?.also { s.causality = it }
            v.variability?.also { s.variability = it }
            v.initial?.also { if (v.initial != Fmi2Initial.undefined) s.initial = it }

            modelDescription.modelVariables.scalarVariable.add(s)
        }
    }

    protected fun register(v: IntVariable) {

        val vr = v.__overrideValueReference ?: intAccessors.size.toLong()
        intAccessors.add(v)

        internalRegister(v, vr).apply {
            integer = Fmi2ScalarVariable.Integer().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.start = getInteger(longArrayOf(vr)).first()
                } else {
                    type.start = v.start
                }
                type.min = v.min
                type.max = v.max
            }
        }

    }

    protected fun register(v: RealVariable) {

        val vr = v.__overrideValueReference ?: realAccessors.size.toLong()
        realAccessors.add(v)

        internalRegister(v, vr).apply {
            real = Fmi2ScalarVariable.Real().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.start = getReal(longArrayOf(vr)).first()
                } else {
                    type.start = v.start
                }
                type.min = v.min
                type.max = v.max
                type.unit = v.unit
                type.nominal = v.nominal
            }
        }
    }

    protected fun register(v: BooleanVariable) {

        val vr = v.__overrideValueReference ?: boolAccessors.size.toLong()
        boolAccessors.add(v)

        internalRegister(v, vr).apply {
            boolean = Fmi2ScalarVariable.Boolean().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.isStart = getBoolean(longArrayOf(vr)).first()
                } else {
                    type.isStart = v.start
                }
            }
        }
    }

    protected fun register(v: StringVariable) {

        val vr = v.__overrideValueReference ?: stringAccessors.size.toLong()
        stringAccessors.add(v)

        internalRegister(v, vr).apply {
            string = Fmi2ScalarVariable.String().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.start = getString(longArrayOf(vr)).first()
                } else {
                    type.start = v.start
                }
            }
        }

    }

    protected open fun registerVariables() {}

    private fun registerAnnotatedVariables() {

        fun processAnnotatedField(field: Field, annotation: ScalarVariable) {

            field.isAccessible = true
            val name = if (annotation.name.isNotEmpty()) annotation.name else field.name

            when (val type = field.type) {
                Int::class, Int::class.java -> {
                    register(integer(name) { field.getInt(this) }.also { iv ->
                        if (!Modifier.isFinal(field.modifiers)) {
                            iv.setter { field.setInt(this, it) }
                        }
                        iv.applyAnnotation(annotation)
                    })
                }
                IntArray::class.java -> {
                    val values = field.get(this) as? IntArray
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                    for (index in values.indices) {
                        register(integer("${name}[$index]") { values[index] }.also { iv ->
                            iv.setter { values[index] = it }
                            iv.applyAnnotation(annotation)
                        })
                    }
                }
                Double::class, Double::class.java -> {
                    register(real(name) { field.getDouble(this) }.also { iv ->
                        if (!Modifier.isFinal(field.modifiers)) {
                            iv.setter { field.setDouble(this, it) }
                        }
                        iv.applyAnnotation(annotation)
                    })
                }
                DoubleArray::class.java -> {
                    val values = field.get(this) as? DoubleArray
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                    for (index in values.indices) {
                        register(real("${name}[$index]") { values[index] }.also { iv ->
                            iv.setter { values[index] = it }
                            iv.applyAnnotation(annotation)
                        })
                    }
                }
                Boolean::class, Boolean::class.java -> {
                    register(boolean(name) { field.getBoolean(this) }.also { iv ->
                        if (!Modifier.isFinal(field.modifiers)) {
                            iv.setter { field.setBoolean(this, it) }
                        }
                        iv.applyAnnotation(annotation)
                    })
                }
                BooleanArray::class.java -> {
                    val values = field.get(this) as? BooleanArray
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                    for (index in values.indices) {
                        register(boolean("${name}[$index]") { values[index] }.also { iv ->
                            iv.setter { values[index] = it }
                            iv.applyAnnotation(annotation)
                        })
                    }
                }
                String::class, String::class.java -> {
                    register(string(name) { field.get(this) as String }.also { iv ->
                        if (!Modifier.isFinal(field.modifiers)) {
                            iv.setter { field.set(this, it) }
                        }
                        iv.applyAnnotation(annotation)
                    })
                }
                Array<String>::class.java -> {
                    val values = field.get(this) as? Array<*>
                        ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                    for (value in values) {
                        require(value?.javaClass == String::class.java)
                    }
                    @Suppress("UNCHECKED_CAST")
                    values as Array<String>
                    for (index in values.indices) {
                        register(string("${name}[$index]") { values[index] }.also { iv ->
                            iv.setter { values[index] = it }
                            iv.applyAnnotation(annotation)
                        })
                    }
                }
                else -> {
                    when {
                        IntVector::class.java.isAssignableFrom(type) -> {
                            val values = field.get(this) as? IntVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                            for (index in 0 until values.size) {
                                register(integer("${name}[$index]") { values[index] }.also { iv ->
                                    iv.setter { values[index] = it }
                                    iv.applyAnnotation(annotation)
                                })
                            }
                        }
                        RealVector::class.java.isAssignableFrom(type) -> {
                            val values = field.get(this) as? RealVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                            for (index in 0 until values.size) {
                                register(real("${name}[$index]") { values[index] }.also { iv ->
                                    iv.setter { values[index] = it }
                                    iv.applyAnnotation(annotation)
                                })
                            }
                        }
                        BooleanVector::class.java.isAssignableFrom(type) -> {
                            val values = field.get(this) as? BooleanVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                            for (index in 0 until values.size) {
                                register(boolean("${name}[$index]") { values[index] }.also { iv ->
                                    iv.setter { values[index] = it }
                                    iv.applyAnnotation(annotation)
                                })
                            }
                        }
                        StringVector::class.java.isAssignableFrom(type) -> {
                            val values = field.get(this) as? StringVector
                                ?: throw IllegalStateException("Field ${field.name} cannot be null!")
                            for (index in 0 until values.size) {
                                register(string("${name}[$index]") { values[index] }.also { iv ->
                                    iv.setter { values[index] = it }
                                    iv.applyAnnotation(annotation)
                                })
                            }
                        }
                        else -> throw IllegalStateException("Unsupported variable type: $type")
                    }
                }
            }

        }

        var cls: Class<*> = javaClass
        do {
            cls.declaredFields.forEach { field ->
                field.getAnnotation(ScalarVariable::class.java)?.also { v ->
                    processAnnotatedField(field, v)
                }
            }
            cls = cls.superclass
        } while (cls != Fmi2Slave::class.java)

    }


    fun __define__() {

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
            cs.isCanRunAsynchronuously = false
            cs.isCanGetAndSetFMUstate = false
            cs.isCanSerializeFMUstate = false
            cs.isCanNotUseMemoryManagementFunctions = true
            cs.modelIdentifier = modelDescription.modelName
            if (slaveInfo != null) {
                cs.isNeedsExecutionTool = slaveInfo.needsExecutionTool
                cs.isCanInterpolateInputs = slaveInfo.canInterpolateInputs
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

        registerAnnotatedVariables()
        registerVariables()

        val variables = modelDescription.modelVariables.scalarVariable
        val outputs = variables.mapIndexedNotNull { i, v ->
            if (v.causality == Fmi2Causality.output) i.toLong() else null
        }
        modelDescription.modelStructure = Fmi2ModelDescription.ModelStructure().also { ms ->
            if (outputs.isNotEmpty()) {
                ms.outputs = Fmi2VariableDependency()
                outputs.forEach {
                    ms.outputs.unknown.add(Fmi2VariableDependency.Unknown().also { u ->
                        u.index = (it + 1)
                    })
                }
            }
        }

        check(modelDescription.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

    }

    private companion object {

        private val LOG: Logger = Logger.getLogger(Fmi2Slave::class.java.name)

        private fun getDateAndTime(): String {
            val now = LocalDateTime.now()
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now)
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").format(now)
            return "${dateFormat}T${timeFormat}Z"
        }

    }

}
