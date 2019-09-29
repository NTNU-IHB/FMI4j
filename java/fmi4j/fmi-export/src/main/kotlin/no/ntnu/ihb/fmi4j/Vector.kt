package no.ntnu.ihb.fmi4j


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
