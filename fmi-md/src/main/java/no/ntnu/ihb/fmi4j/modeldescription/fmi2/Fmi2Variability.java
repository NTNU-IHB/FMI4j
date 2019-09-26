package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

public enum Fmi2Variability {

    /**
     * The value of the variable never changes.
     */
    constant,

    /**
     * The value of the variable is fixed after initialization, in other words
     * after fmi2ExitInitializationMode was called the variable value does not
     * change anymore.
     */
    fixed,

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
    tunable,

    /**
     * ModelExchange: The value of the variable is constant between external and
     * internal events (= time, state, step events defined implicitly in the
     * FMU). CoSimulation: By convention, the variable is from a "realAttribute" sampled
     * data system and its value is only changed at Communication Points (also
     * inside the slave).
     */
    discrete,

    /**
     * Only a variable of type = "Real" can be "continuous". ModelExchange: No
     * restrictions on value changes. CoSimulation: By convention, the variable
     * is from a differential
     */
    continuous;

}
