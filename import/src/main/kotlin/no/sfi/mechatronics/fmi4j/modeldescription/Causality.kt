package no.sfi.mechatronics.fmi4j.modeldescription

import javax.xml.bind.annotation.adapters.XmlAdapter

/**
 * Enumeration that defines the causality of the variable.
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
enum class Causality {

    /**
     * Independent parameter (a data value that is constant during the
     * simulation and is provided by the environment and cannot be used in
     * connections). variability must be "fixed" or "tunable". initial must be
     * exact or not present (meaning exact).
     */
    parameter,
    /**
     * A data value that is constant during the simulation and is computed
     * during initialization or when tunable parameters change. variability must
     * be "fixed" or "tunable". initial must be "approx", "calculated" or not
     * present (meaning calculated).
     */
    calculatedParameter,
    /**
     * The variable value can be provided from another model or slave. It is not
     * allowed to define initial.
     */
    input,
    /**
     * The variable value can be used by another model or slave. The algebraic
     * relationship to the inputs is defined via the dependencies attribute of
     * <fmiModelDescription><ModelStructure><Outputs><Unknown>.
     */
    output,
    /**
     * Local variable that is calculated from other variables or is a
     * continuoustime state (see section 2.2.8). It is not allowed to use the
     * variable value in another model or slave.
     */
    local,
    /**
     * The independent variable (usually “time”). All variables are a function
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
    independent;

}

class CausalityAdapter : XmlAdapter<String, Causality>() {

    @Override
    override fun unmarshal(v: String) : Causality {
        return Causality.valueOf(v);
    }

    @Override
    override fun marshal(v: Causality) : String {
        TODO("not implemented")
    }

}
