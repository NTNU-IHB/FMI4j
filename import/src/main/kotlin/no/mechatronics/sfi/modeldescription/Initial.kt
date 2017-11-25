package no.mechatronics.sfi.modeldescription

import javax.xml.bind.annotation.adapters.XmlAdapter

enum class Initial {
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
     * The variable is calculated from other variables during initialization. It
     * is not allowed to provide a “start” value.
     */
    calculated;
}


class InitialAdapter : XmlAdapter<String, Initial>() {

    @Override
    override fun unmarshal(v: String) : Initial {
        return Initial.valueOf(v);
    }

    @Override
    override fun marshal(v: Initial) : String {
        TODO("not implemented")
    }

}
