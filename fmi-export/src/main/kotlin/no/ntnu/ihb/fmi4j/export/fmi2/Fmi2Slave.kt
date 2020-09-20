package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.*
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.*
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.util.*
import java.util.logging.Logger
import javax.xml.bind.JAXB

abstract class Fmi2Slave(
        args: Map<String, Any>
): Closeable {

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
        String(ByteArrayOutputStream().also { bos ->
            JAXB.marshal(modelDescription, bos)
        }.toByteArray())
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

    open fun setInteger(vr: LongArray, values: IntArray) {
        for (i in vr.indices) {
            intAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.INTEGER)} on variable without a specified setter!")
            }
        }
    }

    open fun getReal(vr: LongArray): DoubleArray {
        return DoubleArray(vr.size) { i ->
            realAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun setReal(vr: LongArray, values: DoubleArray) {
        for (i in vr.indices) {
            realAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.REAL)} on variable without a specified setter!")
            }
        }
    }

    open fun getBoolean(vr: LongArray): BooleanArray {
        return BooleanArray(vr.size) { i ->
            boolAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun setBoolean(vr: LongArray, values: BooleanArray) {
        for (i in vr.indices) {
            boolAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.BOOLEAN)} on variable without a specified setter!")
            }
        }
    }

    open fun getString(vr: LongArray): Array<String> {
        return Array(vr.size) { i ->
            stringAccessors[vr[i].toInt()].getter.get()
        }
    }

    open fun setString(vr: LongArray, values: Array<String>) {
        for (i in vr.indices) {
            stringAccessors[vr[i].toInt()].apply {
                setter?.set(values[i]) ?: LOG.warning("Trying to set value of " +
                        "${getVariableName(vr[i], Fmi2VariableType.STRING)} on variable without a specified setter!")
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

    private fun internalRegister(v: Variable<*>, vr: Long): Fmi2ScalarVariable {
        return Fmi2ScalarVariable().also { s ->
            s.name = v.name
            s.valueReference = vr
            v.causality?.also { s.causality = it }
            v.variability?.also { s.variability = it }
            v.initial?.also { if (v.initial != Fmi2Initial.undefined) s.initial = it }
            modelDescription.modelVariables.scalarVariable.add(s)
        }
    }

    protected fun integer(name: String, getter: Getter<Int>) = IntVariable(name, getter)
    protected fun integer(name: String, values: IntVector) = IntVariables(name, values)
    protected fun integer(name: String, values: IntArray) = IntVariables(name, IntVectorArray(values))

    protected fun real(name: String, getter: Getter<Double>) = RealVariable(name, getter)
    protected fun real(name: String, values: RealVector) = RealVariables(name, values)
    protected fun real(name: String, values: DoubleArray) = RealVariables(name, RealVectorArray(values))

    protected fun boolean(name: String, getter: Getter<Boolean>) = BooleanVariable(name, getter)
    protected fun boolean(name: String, values: BooleanVector) = BooleanVariables(name, values)
    protected fun boolean(name: String, values: BooleanArray) = BooleanVariables(name, BooleanVectorArray(values))

    protected fun string(name: String, getter: Getter<String>) = StringVariable(name, getter)
    protected fun string(name: String, values: StringVector) = StringVariables(name, values)
    protected fun string(name: String, values: Array<String>) = StringVariables(name, StringVectorArray(values))

    protected fun register(v: IntVariable) {

        val vr = intAccessors.size.toLong()
        intAccessors.add(v)

        internalRegister(v, vr).apply {
            integer = Fmi2ScalarVariable.Integer().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.start = getInteger(longArrayOf(vr)).first()
                }
            }
        }

    }

    protected fun register(v: IntVariables) {
        v.build().forEach { register(it) }
    }

    protected fun register(v: RealVariable) {

        val vr = realAccessors.size.toLong()
        realAccessors.add(v)

        internalRegister(v, vr).apply {
            real = Fmi2ScalarVariable.Real().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.start = getReal(longArrayOf(vr)).first()
                }
            }
        }
    }

    protected fun register(v: RealVariables) {
        v.build().forEach { register(it) }
    }

    protected fun register(v: BooleanVariable) {

        val vr = boolAccessors.size.toLong()
        boolAccessors.add(v)

        internalRegister(v, vr).apply {
            boolean = Fmi2ScalarVariable.Boolean().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.isStart = getBoolean(longArrayOf(vr)).first()
                }
            }
        }
    }

    protected fun register(v: BooleanVariables) {
        v.build().forEach { register(it) }
    }

    protected fun register(v: StringVariable) {

        val vr = stringAccessors.size.toLong()
        stringAccessors.add(v)

        internalRegister(v, vr).apply {
            string = Fmi2ScalarVariable.String().also { type ->
                if (automaticallyAssignStartValues && requiresStart()) {
                    type.start = getString(longArrayOf(vr)).first()
                }
            }
        }

    }

    protected fun register(v: StringVariables) {
        v.build().forEach { register(it) }
    }

    protected abstract fun registerVariables()

    open fun __define__() {

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
            cs.isCanInterpolateInputs = slaveInfo?.canInterpolateInputs ?: false
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

        registerVariables()

        val variables = modelDescription.modelVariables.scalarVariable
        val outputs = variables.mapIndexedNotNull { i, v ->
            if (v.causality == Fmi2Causality.output) i.toLong() else null
        }
        modelDescription.modelStructure = Fmi2ModelDescription.ModelStructure().also { ms ->
            if (outputs.isNotEmpty()) {
                ms.outputs = Fmi2VariableDependency()
                outputs.forEach {
                    ms.outputs.unknown.add(Fmi2VariableDependency.Unknown().also {
                        u -> u.index = it + 1
                    })
                }
            }
        }

        check(modelDescription.modelVariables.scalarVariable.isNotEmpty()) { "No variables has been defined!" }

    }

    private companion object {
        private val LOG: Logger = Logger.getLogger(Fmi2Slave::class.java.name)
    }

}
