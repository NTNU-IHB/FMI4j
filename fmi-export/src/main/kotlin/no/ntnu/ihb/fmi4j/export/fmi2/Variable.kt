package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.BooleanVector
import no.ntnu.ihb.fmi4j.export.IntVector
import no.ntnu.ihb.fmi4j.export.RealVector
import no.ntnu.ihb.fmi4j.export.StringVector
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Initial
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability

sealed class Variable<E>(
        val name: String
) {

    var causality: Fmi2Causality? = null
    var variability: Fmi2Variability? = null
    var initial: Fmi2Initial? = null

    fun causality(causality: Fmi2Causality?): E {
        this.causality = causality
        return this as E
    }

    fun variability(variability: Fmi2Variability?): E {
        this.variability = variability
        return this as E
    }

    fun initial(initial: Fmi2Initial?): E {
        this.initial = initial
        return this as E
    }

}

class IntVariable(name: String) : Variable<IntVariable>(name) {

    lateinit var getter: (() -> Int)
        private set
    var setter: ((Int) -> Unit)? = null
        private set

    fun getter(getter: () -> Int) {
        this.getter = getter
    }

    fun setter(setter: (Int) -> Unit) {
        this.setter = setter
    }
}

class IntVariables(
        name: String,
        private val values: IntVector
) : Variable<IntVariables>(name) {

    lateinit var getter: (() -> Boolean)
        private set
    var setter: ((Boolean) -> Unit)? = null
        private set

    fun getter(getter: () -> Boolean) {
        this.getter = getter
    }

    fun setter(setter: (Boolean) -> Unit) {
        this.setter = setter
    }

    internal fun build(): List<IntVariable> {

        return IntRange(0, values.size - 1).map { i ->
            IntVariable("$name[$i]").apply {
                causality(causality)
                variability(variability)
                initial(initial)
                getter {
                    values[i]
                }
                if (variability != Fmi2Variability.constant) {
                    setter { values[i] = it }
                }
            }
        }

    }

}

class RealVariable(
        name: String
) : Variable<RealVariable>(name) {

    lateinit var getter: () -> Double
        private set
    var setter: ((Double) -> Unit)? = null
        private set

    var min: Double? = null
        private set

    var max: Double? = null
        private set

    fun setter(setter: (Double) -> Unit) = apply {
        this.setter = setter
    }

    fun min(value: Double?) = apply {
        this.min = value
    }

    fun max(value: Double?) = apply {
        this.min = value
    }

    fun getter(getter: () -> Double) = apply {
        this.getter = getter
    }

}

class RealVariables(
        name: String,
        private val values: RealVector
) : Variable<RealVariables>(name) {

    var min: Double? = null
        private set

    var max: Double? = null
        private set

    fun min(value: Double?) = apply {
        this.min = value
    }

    fun max(value: Double?) = apply {
        this.min = value
    }

    internal fun build(): List<RealVariable> {

        return IntRange(0, values.size - 1).map { i ->
            RealVariable("$name[$i]").apply {
                causality(causality)
                variability(variability)
                initial(initial)
                getter {
                    values[i]
                }
                if (variability != Fmi2Variability.constant) {
                    setter { values[i] = it }
                }
            }
        }

    }

}

class BooleanVariable(name: String) : Variable<BooleanVariable>(name) {

    lateinit var getter: (() -> Boolean)
        private set
    var setter: ((Boolean) -> Unit)? = null
        private set

    fun getter(getter: () -> Boolean) {
        this.getter = getter
    }

    fun setter(setter: (Boolean) -> Unit) {
        this.setter = setter
    }

}

class BooleanVariables(
        name: String,
        private val values: BooleanVector
) : Variable<BooleanVariables>(name) {

    lateinit var getter: (() -> Boolean)
        private set
    var setter: ((Boolean) -> Unit)? = null
        private set

    fun getter(getter: () -> Boolean) {
        this.getter = getter
    }

    fun setter(setter: (Boolean) -> Unit) {
        this.setter = setter
    }

    internal fun build(): List<BooleanVariable> {

        return IntRange(0, values.size - 1).map { i ->
            BooleanVariable("$name[$i]").apply {
                causality(causality)
                variability(variability)
                initial(initial)
                getter {
                    values[i]
                }
                if (variability != Fmi2Variability.constant) {
                    setter { values[i] = it }
                }
            }
        }

    }

}

class StringVariable(name: String) : Variable<StringVariable>(name) {

    lateinit var getter: (() -> String)
        private set
    var setter: ((String) -> Unit)? = null
        private set

    fun getter(getter: () -> String) {
        this.getter = getter
    }

    fun setter(setter: (String) -> Unit) {
        this.setter = setter
    }

}

class StringVariables(
        name: String,
        private val values: StringVector
) : Variable<StringVariables>(name) {

    lateinit var getter: (() -> String)
        private set
    var setter: ((String) -> Unit)? = null
        private set

    fun getter(getter: () -> String) {
        this.getter = getter
    }

    fun setter(setter: (String) -> Unit) {
        this.setter = setter
    }

    internal fun build(): List<StringVariable> {

        return IntRange(0, values.size - 1).map { i ->
            StringVariable("$name[$i]").apply {
                causality(causality)
                variability(variability)
                initial(initial)
                getter {
                    values[i]
                }
                if (variability != Fmi2Variability.constant) {
                    setter { values[i] = it }
                }
            }
        }

    }

}
