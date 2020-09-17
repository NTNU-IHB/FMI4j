package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.BooleanVector
import no.ntnu.ihb.fmi4j.export.IntVector
import no.ntnu.ihb.fmi4j.export.RealVector
import no.ntnu.ihb.fmi4j.export.StringVector
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Initial
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability


fun interface Getter<E> {
    fun get(): E
}

fun interface Setter<E> {
    fun set(value: E)
}

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

class IntVariable(
        name: String,
        val getter: Getter<Int>
) : Variable<IntVariable>(name) {

    var setter: Setter<Int>? = null
        private set

    fun setter(setter: Setter<Int>) = apply {
        this.setter = setter
    }
}

class IntVariables(
        name: String,
        private val values: IntVector
) : Variable<IntVariables>(name) {

    internal fun build(): List<IntVariable> {

        return IntRange(0, values.size - 1).map { i ->
            IntVariable("$name[$i]", {values[i]}).also { v ->
                v.causality(causality)
                v.variability(variability)
                v.initial(initial)
                if (variability != Fmi2Variability.constant) {
                    v.setter { values[i] = it }
                }
            }
        }

    }

}

class RealVariable(
        name: String,
        val getter: Getter<Double>
) : Variable<RealVariable>(name) {

    var setter: Setter<Double>? = null
        private set

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

    fun setter(setter: Setter<Double>) = apply {
        this.setter = setter
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
            RealVariable("$name[$i]", {values[i]}).also { v ->
                v.causality(causality)
                v.variability(variability)
                v.initial(initial)
                if (variability != Fmi2Variability.constant) {
                    v.setter { values[i] = it }
                }
            }
        }

    }

}

class BooleanVariable(
        name: String,
        val getter: Getter<Boolean>
) : Variable<BooleanVariable>(name) {

    var setter: Setter<Boolean>? = null
        private set

    fun setter(setter: Setter<Boolean>) = apply {
        this.setter = setter
    }

}

class BooleanVariables(
        name: String,
        private val values: BooleanVector
) : Variable<BooleanVariables>(name) {

    internal fun build(): List<BooleanVariable> {

        return IntRange(0, values.size - 1).map { i ->
            BooleanVariable("$name[$i]") { values[i] }.also { v ->
                v.causality(causality)
                v.variability(variability)
                v.initial(initial)
                if (variability != Fmi2Variability.constant) {
                    v.setter { values[i] = it }
                }
            }
        }

    }

}

class StringVariable(
        name: String,
        val getter: Getter<String>
) : Variable<StringVariable>(name) {

    var setter: Setter<String>? = null
        private set

    fun setter(setter: Setter<String>) = apply {
        this.setter = setter
    }

}

class StringVariables(
        name: String,
        private val values: StringVector
) : Variable<StringVariables>(name) {

    internal fun build(): List<StringVariable> {

        return IntRange(0, values.size - 1).map { i ->
            StringVariable("$name[$i]") {values[i]}.also { v ->
                v.causality(causality)
                v.variability(variability)
                v.initial(initial)
                if (variability != Fmi2Variability.constant) {
                    v.setter { values[i] = it }
                }
            }
        }

    }

}
