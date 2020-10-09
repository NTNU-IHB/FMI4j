package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.export.*
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Initial
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ScalarVariable
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability


fun interface Getter<E> {
    fun get(): E
}

fun interface Setter<E> {
    fun set(value: E)
}

enum class Fmi2VariableType {
    INTEGER,
    REAL,
    BOOLEAN,
    STRING,
    ENUMERATION
}

internal fun Fmi2ScalarVariable.type(): Fmi2VariableType {
    return when {
        integer != null -> Fmi2VariableType.INTEGER
        real != null -> Fmi2VariableType.REAL
        boolean != null -> Fmi2VariableType.BOOLEAN
        string != null -> Fmi2VariableType.STRING
        enumeration != null -> Fmi2VariableType.ENUMERATION
        else -> throw IllegalStateException()
    }
}


@Suppress("UNCHECKED_CAST")
sealed class Variable<E>(
        val name: String
) {

    internal var causality: Fmi2Causality? = null
        private set
    internal var variability: Fmi2Variability? = null
        private set
    internal var initial: Fmi2Initial? = null
        private set
    internal var description: String? = null
        private set

    var __overrideValueReference: Long? = null

    fun description(description: String?): E {
        this.description = description
        return this as E
    }

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

    internal var min: Int? = null
        private set

    internal var max: Int? = null
        private set

    internal var start: Int? = null
        private set

    internal var setter: Setter<Int>? = null
        private set

    fun min(value: Int?) = apply {
        this.min = value
    }

    fun max(value: Int?) = apply {
        this.max = value
    }

    fun start(value: Int?) = apply {
        this.start = value
    }

    fun setter(setter: Setter<Int>) = apply {
        this.setter = setter
    }

}

class IntVariables(
        name: String,
        private val values: IntVector
) : Variable<IntVariables>(name) {

    private var min: Int? = null
    private var max: Int? = null

    fun min(value: Int?) = apply {
        this.min = value
    }

    fun max(value: Int?) = apply {
        this.max = value
    }

    internal fun build(): List<IntVariable> {

        return IntRange(0, values.lastIndex).map { i ->
            IntVariable("$name[$i]") { values[i] }.also { v ->
                v.causality(causality)
                v.variability(variability)
                v.initial(initial)
                if (variability != Fmi2Variability.constant) {
                    v.setter { values[i] = it }
                }
                v.min(min)
                v.max(max)
            }
        }

    }

}

class RealVariable(
        name: String,
        val getter: Getter<Double>
) : Variable<RealVariable>(name) {

    internal var min: Double? = null
        private set

    internal var max: Double? = null
        private set

    internal var nominal: Double? = null
        private set

    internal var start: Double? = null
        private set

    internal var unit: String? = null

    internal var setter: Setter<Double>? = null
        private set


    fun min(value: Double?) = apply {
        this.min = value
    }

    fun max(value: Double?) = apply {
        this.max = value
    }

    fun nominal(value: Double?) = apply {
        this.nominal = value
    }

    fun unit(value: String?) = apply {
        this.unit = value
    }

    fun start(value: Double?) = apply {
        this.start = value
    }

    fun setter(setter: Setter<Double>) = apply {
        this.setter = setter
    }

}

class RealVariables(
        name: String,
        private val values: RealVector
) : Variable<RealVariables>(name) {

    private var min: Double? = null
    private var max: Double? = null
    private var unit: String? = null

    fun min(value: Double?) = apply {
        this.min = value
    }

    fun max(value: Double?) = apply {
        this.max = value
    }

    fun unit(value: String?) = apply {
        this.unit = value
    }

    internal fun build(): List<RealVariable> {

        return IntRange(0, values.lastIndex).map { i ->
            RealVariable("$name[$i]") { values[i] }.also { v ->
                v.causality(causality)
                v.variability(variability)
                v.initial(initial)
                if (variability != Fmi2Variability.constant) {
                    v.setter { values[i] = it }
                }
                v.unit(unit)
                v.min(min)
                v.max(max)
            }
        }

    }

}

class BooleanVariable(
        name: String,
        val getter: Getter<Boolean>
) : Variable<BooleanVariable>(name) {

    internal var start: Boolean? = null
        private set

    internal var setter: Setter<Boolean>? = null
        private set

    fun start(value: Boolean?) = apply {
        this.start = value
    }

    fun setter(setter: Setter<Boolean>) = apply {
        this.setter = setter
    }

}

class BooleanVariables(
        name: String,
        private val values: BooleanVector
) : Variable<BooleanVariables>(name) {

    internal fun build(): List<BooleanVariable> {

        return IntRange(0, values.lastIndex).map { i ->
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

    internal var start: String? = null
        private set

    internal var setter: Setter<String>? = null
        private set

    fun start(value: String?) = apply {
        this.start = value
    }

    fun setter(setter: Setter<String>) = apply {
        this.setter = setter
    }

}

class StringVariables(
        name: String,
        private val values: StringVector
) : Variable<StringVariables>(name) {

    internal fun build(): List<StringVariable> {

        return IntRange(0, values.lastIndex).map { i ->
            StringVariable("$name[$i]") { values[i] }.also { v ->
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
