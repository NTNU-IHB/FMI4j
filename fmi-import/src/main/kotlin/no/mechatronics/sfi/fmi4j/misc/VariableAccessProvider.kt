package no.mechatronics.sfi.fmi4j.misc

import no.mechatronics.sfi.fmi4j.modeldescription.BooleanVariable
import no.mechatronics.sfi.fmi4j.modeldescription.IntegerVariable
import no.mechatronics.sfi.fmi4j.modeldescription.RealVariable
import no.mechatronics.sfi.fmi4j.modeldescription.StringVariable

/**
 *
 * @author Lars Ivar Hatledal
 */
interface VariableAccessProvider {

    fun getWriter(vr: Int) : VariableWriter
    fun getWriter(name: String) : VariableWriter
    fun getWriter(variable: IntegerVariable) : IntWriter
    fun getWriter(variable: RealVariable) : RealWriter
    fun getWriter(variable: StringVariable) : StringWriter
    fun getWriter(variable: BooleanVariable) : BooleanWriter

    fun getReader(vr: Int): VariableReader
    fun getReader(name: String) : VariableReader
    fun getReader(variable: IntegerVariable) : IntReader
    fun getReader(variable: RealVariable) : RealReader
    fun getReader(variable: StringVariable) : StringReader
    fun getReader(variable: BooleanVariable): BooleanReader

}