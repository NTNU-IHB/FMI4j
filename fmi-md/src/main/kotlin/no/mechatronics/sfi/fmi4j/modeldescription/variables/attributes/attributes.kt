package no.mechatronics.sfi.fmi4j.modeldescription.variables.attributes

import no.mechatronics.sfi.fmi4j.modeldescription.variables.ScalarVariable
import java.io.Serializable
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute


interface TypedAttribute<E>: Serializable {

    /**
     * Initial or guess value of variable. This value is also stored in the C functions
     * [Therefore, calling fmi2SetXXX to set start values is only necessary, if a different
     * value as stored in the xml file is desired.] The interpretation of start is defined by
     * ScalarVariable / initial. A different start value can be provided with an
     * fmi2SetXXX function before fmi2ExitInitializationMode is called (but not
     * for categories with variability = ″constant″).
     * [The standard approach is to set the start value before
     * fmi2EnterInitializationMode. However, if the initialization shall be modified
     * in the calling environment (e.g. changing from initialization of states to steadystate
     * initialization), it is also possible to use the start value as iteration variable of
     * an algebraic loop: Via an additional condition in the environment, such as 𝑥̇ = 0,
     * the actual start value is determined.]
     */
    var start: E?

}

interface BoundedTypedAttribute<E> : TypedAttribute<E> {

    /**
     * Minimum value of variable (variable Value ≥ min). If not defined, the
     * minimum is the largest negative number that can be represented on the
     * machine. The min definition is an information from the FMU to the
     * environment defining the region in which the FMU is designed to operate, see
     * also comment after this table.
     */
    val min: E?

    /**
     * Maximum value of variable (variableValue ≤ max). If not defined, the
     * maximum is the largest positive number that can be represented on the
     * machine. The max definition is an information from the FMU to the
     * environment defining the region in which the FMU is designed to operate, see
     * also comment after this table.
     */
    val max: E?
}


/**
 * @author Lars Ivar Hatledal
 */
@XmlAccessorType(XmlAccessType.FIELD)
internal class IntegerAttribute internal constructor(): BoundedTypedAttribute<Int> {

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val min: Int? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val max: Int? = null

    /**
     * @see ScalarVariable.start
     */
    @XmlAttribute
    override var start: Int? = null

}

@XmlAccessorType(XmlAccessType.FIELD)
internal class RealAttribute internal constructor() : BoundedTypedAttribute<Double> {

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val min: Double? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val max: Double? = null


    /**
     * @see ScalarVariable.start
     */
    @XmlAttribute
    override var start: Double? = null


    /**
     * Nominal value of variable. If not defined and no other information about the
     * nominal value is available, then nominal = 1 is assumed.
     * [The nominal value of a variable can be, for example used to determine the
     * absolute tolerance for this variable as needed by numerical algorithms:
     * absoluteTolerance = nominal*tolerance*0.01
     * where tolerance is, e.g., the relative tolerance defined in
     * <DefaultExperiment>, see section 2.2.5.]
     */
    @XmlAttribute
    val nominal : Double?  = null

    /**
     * If present, this variable is the derivative of variable with ScalarVariable index "derivative",
     */
    @XmlAttribute
    val derivative: Int? = null

    /**
     * If true, indicates that the variable gets during time integration much larger
     * than its nominal value nominal. [Typical examples are the monotonically
     * increasing rotation angles of crank shafts and the longitudinal position of a
     * vehicle along the track in long distance simulations. This information can, for
     * example, be used to increase numerical stability and accuracy by setting the
     * corresponding bound for the relative error to zero (relative tolerance = 0.0), if
     * the corresponding variable or an alias of it is a continuous state variable.]
     */
    @XmlAttribute
    val unbounded: Boolean? = null

    /**
     * Only for Model exchange
     * <br>
     * If true, state can be reinitialized at an event by the FMU. If false, state will never be reinitialized at an event by the FMU
     */
    @XmlAttribute
    val reinit: Boolean = false

    /**
     * Physical quantity of the variable, for example “Angle”, or “Energy”. The
     * quantity names are not standardized.
     */
    @XmlAttribute
    val quantity: String? = null

    /**
     * Unit of the variable defined with UnitDefinitions.Unit.name that is used
     * for the model equations [, for example “N.m”: in this case a Unit.name =
     * "N.m" must be present under UnitDefinitions].
     */
    @XmlAttribute
    val unit: String? = null

    /**
     * Default display unit. The conversion to the “unit” is defined with the element
     * “<fmiModelDescription><UnitDefinitions>”. If the corresponding
     * “displayUnit” is not defined under <UnitDefinitions> <Unit>
     * <DisplayUnit>, then displayUnit is ignored. It is an error if
     * displayUnit is defined in element Real, but unit is not, or unit is not
     * defined under <UnitDefinitions><Unit>.
     */
    @XmlAttribute
    val displayUnit: String? = null

    /**
     * If this attribute is true, then the “offset” of “displayUnit” must be ignored
     * (for example 10 degree Celsius = 10 Kelvin if “relativeQuantity = true”
     * and not 283,15 Kelvin).
     */
    @XmlAttribute
    val relativeQuantity: String? = null

}

/**
 * @author Lars Ivar Hatledal
 */
@XmlAccessorType(XmlAccessType.FIELD)
internal class StringAttribute internal constructor(): TypedAttribute<String> {

    /**
     * @see ScalarVariable.start
     */
    @XmlAttribute
    override var start: String? = null

}

/**
 * @author Lars Ivar Hateldal
 */
@XmlAccessorType(XmlAccessType.FIELD)
internal class BooleanAttribute internal constructor(): TypedAttribute<Boolean> {

    /**
     * @see ScalarVariable.start
     */
    @XmlAttribute
    override var start: Boolean? = null

}
