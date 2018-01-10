package no.mechatronics.sfi.fmi4j.misc

import no.mechatronics.sfi.fmi4j.modeldescription.IModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.IntegerVariable
import no.mechatronics.sfi.fmi4j.modeldescription.RealVariable
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper

class VariableAccessor internal constructor(
        private val modelDescription: IModelDescription,
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : VariableReader, VariableWriter {

    override fun asIntWriter(): IntWriter {

        val variable = modelDescription.modelVariables.getByValueReference(valueReference) ?: throw IllegalArgumentException()
        if (variable is IntegerVariable) {
            return IntWriterImpl(wrapper, valueReference)
        } else {
            throw IllegalStateException("Variable '${variable.name}}' is not of type ${variable.typeName}")
        }

    }
    override fun asRealWriter(): RealWriter {

        val variable = modelDescription.modelVariables.getByValueReference(valueReference) ?: throw IllegalArgumentException()
        if (variable is RealVariable) {
            return RealWriterImpl(wrapper, valueReference)
        } else {
            throw IllegalStateException("Variable '${variable.name}}' is not of type ${variable.typeName}")
        }

    }
    override fun asStringWriter(): StringWriter {

        val variable = modelDescription.modelVariables.getByValueReference(valueReference) ?: throw IllegalArgumentException()
        if (variable is IntegerVariable) {
            return StringWriterImpl(wrapper, valueReference)
        } else {
            throw IllegalStateException("Variable '${variable.name}}' is not of type ${variable.typeName}")
        }

    }
    override fun asBooleanWriter(): BooleanWriter {

        val variable = modelDescription.modelVariables.getByValueReference(valueReference) ?: throw IllegalArgumentException()
        if (variable is IntegerVariable) {
            return BooleanWriterImpl(wrapper, valueReference)
        } else {
            throw IllegalStateException("Variable '${variable.name}}' is not of type ${variable.typeName}")
        }

    }

    override fun asIntReader() = IntReaderImpl(wrapper, valueReference)
    override fun asRealReader() = RealReaderImpl(wrapper, valueReference)
    override fun asStringReader() = StringReaderImpl(wrapper, valueReference)
    override fun asBooleanReader() = BooleanReaderImpl(wrapper, valueReference)

}
