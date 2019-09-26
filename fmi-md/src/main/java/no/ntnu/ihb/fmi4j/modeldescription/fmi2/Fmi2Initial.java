package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

public enum Fmi2Initial {

    /**
     * The variable is initialized with the start value (provided under Real,
     * Integer, Boolean, String or Enumeration).
     */
    exact,

    /**
     * The variable is an iteration variable of an algebraic loop and the
     * iteration at initialization starts with the start value.
     */
    approx,

    /**
     * The variable is calculated from other categories during initialization. It
     * is not allowed to provide a "start" value.
     */
    calculated,

    /**
     * Undefined unknown
     */
    undefined;

}
