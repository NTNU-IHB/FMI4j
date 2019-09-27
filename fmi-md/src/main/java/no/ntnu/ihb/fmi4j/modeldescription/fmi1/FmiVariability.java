package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

/**
 * Defines when the value of the variable changes. The purpose of this attribute is to define
 * when a result value needs to be inquired and to be stored (e.g., discrete variables
 * change their values only at events instants and it is therefore only necessary to store
 * them at event times).
 */
public enum  FmiVariability {

    /**
     * The value of the variable is fixed and does not change.
     */
    constant,

    /**
     * The value of the variable does not change after initialization (the value
     * is fixed after fmiInitialize was called).
     */
    parameter,

    /**
     * The value of the variable only changes during initialization and at event
     * instants.
     */
    discrete,

    /**
     * No restrictions on value changes. Only a variable of type = "Real" can
     * be "continuous".
     */
    continuous;

}
