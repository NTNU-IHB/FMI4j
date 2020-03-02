package no.ntnu.ihb.fmi4j.export.fmi2

internal class Accessor<T>(
        val getter: () -> T,
        val setter: ((T) -> Unit)?
)
internal typealias IntAccessor = Accessor<Int>
internal typealias RealAccessor = Accessor<Double>
internal typealias BoolAccessor = Accessor<Boolean>
internal typealias StringAccessor = Accessor<String>