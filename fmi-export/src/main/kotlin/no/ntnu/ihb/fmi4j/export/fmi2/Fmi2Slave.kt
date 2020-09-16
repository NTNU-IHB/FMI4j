package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.reflect.Method
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger
import javax.xml.bind.JAXB

private const val MAX_LEVEL = 8

abstract class Fmi2Slave(
        args: Map<String, Any>
) {

    val modelDescription = Fmi2ModelDescription()
    val instanceName: String = args["instanceName"] as? String ?: throw IllegalStateException("Missing 'instanceName'")

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

    protected fun registerReal(name: String, ctx: RealBuilder.() -> Unit) {
        val builder = RealBuilder(name)
        ctx.invoke(builder)
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

    }

    protected abstract fun registerVariables()

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

        registerVariables()

        check(modelDescription.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

        return this

    }

    private companion object {
        private val LOG: Logger = Logger.getLogger(Fmi2Slave::class.java.name)
    }

}
