package no.ntnu.ihb.fmi4j.export

interface ScalarVector {
    val size: Int
}

interface IntVector : ScalarVector {
    operator fun get(index: Int): Int
    operator fun set(index: Int, value: Int)
}

interface RealVector : ScalarVector {
    operator fun get(index: Int): Double
    operator fun set(index: Int, value: Double)
}

interface BooleanVector : ScalarVector {
    operator fun get(index: Int): Boolean
    operator fun set(index: Int, value: Boolean)
}

interface StringVector : ScalarVector {
    operator fun get(index: Int): String
    operator fun set(index: Int, value: String)
}

class IntVectorArray(
        private val array: IntArray
) : IntVector {

    override val size: Int
        get() = array.size

    override fun get(index: Int): Int {
        return array[index]
    }

    override fun set(index: Int, value: Int) {
        array[index] = value
    }
}

class RealVectorArray(
        private val array: DoubleArray
) : RealVector {

    override val size: Int
        get() = array.size

    override fun get(index: Int): Double {
        return array[index]
    }

    override fun set(index: Int, value: Double) {
        array[index] = value
    }
}

class BooleanVectorArray(
        private val array: BooleanArray
) : BooleanVector {

    override val size: Int
        get() = array.size

    override fun get(index: Int): Boolean {
        return array[index]
    }

    override fun set(index: Int, value: Boolean) {
        array[index] = value
    }
}

class StringVectorArray(
        private val array: Array<String>
) : StringVector {

    override val size: Int
        get() = array.size

    override fun get(index: Int): String {
        return array[index]
    }

    override fun set(index: Int, value: String) {
        array[index] = value
    }
}
