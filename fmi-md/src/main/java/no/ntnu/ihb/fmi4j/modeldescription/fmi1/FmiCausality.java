package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

/**
 * Defines how the variable is visible from the outside of the model. This information is
 * needed when the FMU is connected to other FMUs.
 */
public enum FmiCausality {

    /**
     * A value can be provided from the outside. Initially, the value is set to its
     * "start" value (see below).
     */
    input,

    /**
     * A value can be utilized in a connection
     */
    output,

    /**
     * After initialization only allowed to get value, e.g., to store the value as
     * result. It is not allowed to use this value in a connection. Before initialization, start
     * values can be set.
     */
    internal,

    /**
     * The variable does not influence the model equations. It is a tool specific
     * variable to, e.g., switch certain logging or storage features on or off. Variables with
     * this causality setting can be set with the fmiSetXXX functions at any time
     */
    none;

}
