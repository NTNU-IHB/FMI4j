package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Initial
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability


internal class Variable<E>(
        val name: String,
        val accessor: Accessor<E>,
        val causality: Fmi2Causality?,
        val variability: Fmi2Variability?,
        val initial: Fmi2Initial?
)

sealed class VariableBuilder<E>(
        private val name: String
) {

    protected var getter: (() -> E)? = null
    protected var setter: ((E) -> Unit)? = null
    var causality: Fmi2Causality? = null
    var variability: Fmi2Variability? = null
    var initial: Fmi2Initial? = null

   /* fun getter(getter: () -> E): VariableBuilder<E> {
        this.getter = getter
        return this
    }

    fun setter(setter: (E) -> Unit): VariableBuilder<E> {
        this.setter = setter
        return this
    }*/

/*
    internal fun apply(annotation: ScalarVariable) {
        causality = annotation.causality
        variability = annotation.variability
        initial = annotation.initial
    }

    internal fun apply(annotation: ScalarVariableGetter) {
        causality = annotation.causality
        variability = annotation.variability
        initial = annotation.initial
    }
*/

    internal fun build(): Variable<E> {
        val getter = getter
        checkNotNull(getter) { "getter cannot be null!" }

        return Variable(name, Accessor(getter, setter), causality, variability, initial)
    }

}

class IntBuilder(name: String) : VariableBuilder<Int>(name)
class RealBuilder(name: String) : VariableBuilder<Double>(name) {

    fun getter(getter: () -> Double) {
        this.getter = getter
    }


}
class BooleanBuilder(name: String) : VariableBuilder<Boolean>(name)
class StringBuilder(name: String) : VariableBuilder<String>(name)
