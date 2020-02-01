package no.ntnu.ihb.fmi4j.export


interface IntVector {

    val size: Int

    operator fun get(index: Int): Int

    operator fun set(index: Int, value: Int)

}

interface RealVector {

    val size: Int

    operator fun get(index: Int): Double

    operator fun set(index: Int, value: Double)

}

interface BooleanVector {

    val size: Int

    operator fun get(index: Int): Boolean

    operator fun set(index: Int, value: Boolean)

}

interface StringVector {

    val size: Int

    operator fun get(index: Int): String

    operator fun set(index: Int, value: String)

}
