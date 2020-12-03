package no.ntnu.ihb.fmi4j.slaves

import no.ntnu.ihb.fmi4j.export.BulkRead
import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable

class Identity(
    args: Map<String, Any>
) : Fmi2Slave(args) {

    @ScalarVariable
    private var real: Double = 0.0

    @ScalarVariable
    private var integer: Int = 0

    @ScalarVariable
    private var boolean: Boolean = false

    @ScalarVariable
    private var string: String = ""

    @ScalarVariable
    private var setAllInvoked: Boolean = false

    @ScalarVariable
    private var getAllInvoked: Boolean = false

    override fun setAll(
        intVr: LongArray, intValues: IntArray,
        realVr: LongArray, realValues: DoubleArray,
        boolVr: LongArray, boolValues: BooleanArray,
        strVr: LongArray, strValues: Array<String>
    ) {
        super.setAll(intVr, intValues, realVr, realValues, boolVr, boolValues, strVr, strValues).also {
            setAllInvoked = true
        }
    }

    override fun getAll(intVr: LongArray, realVr: LongArray, boolVr: LongArray, strVr: LongArray): BulkRead {
        return super.getAll(intVr, realVr, boolVr, strVr).also {
            getAllInvoked = true
        }
    }

    override fun doStep(currentTime: Double, dt: Double) {
    }

}
