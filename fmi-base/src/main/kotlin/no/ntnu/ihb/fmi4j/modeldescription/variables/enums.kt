package no.ntnu.ihb.fmi4j.modeldescription.variables


/**
 * Enumeration that defines the causality of the variable.
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
enum class Causality {

    /**
     * Independent parameter (a data value that is constant during the
     * simulation and is provided by the environment and cannot be used in
     * connections). variability must be "fixed" or "tunable". initial must be
     * exact or not present (meaning exact).
     */
    PARAMETER,

    /**
     * A data value that is constant during the simulation and is computed
     * during initialization or when tunable parameters change. variability must
     * be "fixed" or "tunable". initial must be "approx", "calculated" or not
     * present (meaning calculated).
     */
    CALCULATED_PARAMETER,

    /**
     * The variable value can be provided from another model or slave. It is not
     * allowed to define initial.
     */
    INPUT,

    /**
     * The variable value can be used by another model or slave. The algebraic
     * relationship to the inputs is defined via the dependencies attribute of
     * <fmiModelDescription><ModelStructure><Outputs><Unknown>.
     */
    OUTPUT,

    /**
     * Local variable that is calculated from other categories or is a
     * continuoustime state (see section 2.2.8). It is not allowed to use the
     * variable value in another model or slave.
     */
    LOCAL,

    /**
     * The independent variable (usually “time”). All categories are a function
     * of this independent variable. variability must be "continuous". At most
     * one ScalarVariable of an FMU can be defined as "independent". If no
     * variable is defined as "independent", it is implicitely present with name
     * = "time" and unit = "s". If one variable is defined as "independent", it
     * must be defined as "Real" without a "start" attribute. It is not allowed
     * to call function fmi2SetReal on an "independent" variable. Instead, its
     * value is initialized with fmi2SetupExperiment and after initialization
     * set by fmi2SetTime for ModelExchange and by arguments
     * currentCommunicationPoint and communicationStepSize of fmi2DoStep for
     * CoSimulation. [The actual value can be inquired with fmi2GetReal.]
     */
    INDEPENDENT;

}



/**
 * Enumeration that defines the time dependency of the variable, in other words it defines
 * the time instants when a variable can change its value. [The purpose of this attribute is
 * to define when a result value needs to be inquired and to be stored. For example
 * discrete categories change their values only at event instants (ModelExchange) or at a
 * communication point (CoSimulation) and it is therefore only necessary to inquire them
 * with fmi2GetXXX and store them at event times]. Allowed values of this enumeration:
 * • "constant": The value of the variable never changes.
 * • "fixed": The value of the variable is fixed after initialization, in other words after
 * fmi2ExitInitializationMode was called the variable value does not change
 * anymore.
 * • "tunable": The value of the variable is constant between external events
 * (ModelExchange) and between Communication Points (CoSimulation) due to
 * changing categories with causality = "parameter" or "input" and
 * variability = "tunable". Whenever a parameter or input signal with
 * variability = "tunable" changes, then an event is triggered externally
 * (ModelExchange) or the change is performed at the next Communication Point
 * (CoSimulation) and the categories with variability = "tunable" and causality =
 * "calculatedParameter" or "output" must be newly computed.
 * • "discrete":
 * ModelExchange: The value of the variable is constant between external and internal
 * events (= time, state, step events defined implicitly in the FMU).
 * CoSimulation: By convention, the variable is from a “real” sampled data system and
 * its value is only changed at Communication Points (also inside the slave).
 * • "continuous": Only a variable of type = “Real” can be “continuous”.
 * ModelExchange: No restrictions on value changes.
 * CoSimulation: By convention, the variable is from a differential
 * The default is “continuous”.
 * [Note, the information about continuous states is defined with element
 * fmiModelDescription.ModelStructure.Derivatives]
 *
 * @author Lars Ivar Hatledal
 */
enum class Variability {

    /**
     * The value of the variable never changes.
     */
    CONSTANT,

    /**
     * The value of the variable is fixed after initialization, in other words
     * after fmi2ExitInitializationMode was called the variable value does not
     * change anymore.
     */
    FIXED,

    /**
     * The value of the variable is constant between external events
     * (ModelExchange) and between Communication Points (CoSimulation) due to
     * changing categories with causality = "parameter" or "input" and
     * variability = "tunable". Whenever a parameter or input signal with
     * variability = "tunable" changes, then an event is triggered externally
     * (ModelExchange) or the change is performed at the next Communication
     * Point (CoSimulation) and the categories with variability = "tunable" and
     * causality = "calculatedParameter" or "output" must be newly computed.
     */
    TUNABLE,

    /**
     * ModelExchange: The value of the variable is constant between external and
     * internal events (= time, state, step events defined implicitly in the
     * FMU). CoSimulation: By convention, the variable is from a “realAttribute” sampled
     * data system and its value is only changed at Communication Points (also
     * inside the slave).
     */
    DISCRETE,

    /**
     * Only a variable of type = “Real” can be “continuous”. ModelExchange: No
     * restrictions on value changes. CoSimulation: By convention, the variable
     * is from a differential
     */
    CONTINUOUS;

}



/**
 * Enumeration that defines how the variable is initialized. It is not allowed to provide a
 * value for initial if causality = "input" or "independent":
 * • = "exact": The variable is initialized with the start value (provided under Real,
 * Integer, Boolean, String or Enumeration).
 * • = "approx": The variable is an iteration variable of an algebraic loop and the
 * iteration at initialization starts with the start value.
 * • = "calculated": The variable is calculated from other categories during initialization.
 * It is not allowed to provide a “start” value.
 * If initial is not present, it is defined by the table below based on causality and
 * variability. If initial = exact or approx, or causality = ″input″ a start
 * value must be provided. If initial = calculated, or causality = ″independent″ it is
 * not allowed to provide a start value.
 * [The environment decides when to use the start value of a variable with causality =
 * ″input″. Examples: (a) automatic tests of FMUs are performed, and the FMU is tested
 * by providing the start value as constant input. (b) For a ModelExchange FMU, the
 * FMU might be part of an algebraic loop. If the input variable is iteration variable of this
 * algebraic loop, then initialization starts with its start value.].
 * If fmiSetXXX is not called on a variable with causality = ″input″ then the FMU must
 * use the start value as value of this input.
 *
 * @author Lars Ivar Hatledal
 */
enum class Initial {

    /**
     * The variable is initialized with the start value (provided under Real,
     * Integer, Boolean, String or Enumeration).
     */
    EXACT,

    /**
     * The variable is an iteration variable of an algebraic loop and the
     * iteration at initialization starts with the start value.
     */
    APPROX,

    /**
     * The variable is calculated from other categories during initialization. It
     * is not allowed to provide a “start” value.
     */
    CALCULATED,

    /**
     * Unknown initial
     */
    UNKNOWN;
}

