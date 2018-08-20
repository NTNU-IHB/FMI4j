package example

import no.mechatronics.sfi.fmi4j.ControlledTemperature
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable

fun main(args: Array<String>) {

    ControlledTemperature.newInstance().use { slave ->

        slave.init()

        //Variables are grouped by causality and have types!
        val tempRef: RealVariable
                = slave.outputs.temperature_Reference()

        val stop = 10.0
        val stepSize = 1E-2
        while (slave.currentTime <= stop) {

            if (!slave.doStep(stepSize)) {
                break
            }

            tempRef.read().also {
                println("t=${slave.currentTime}, ${tempRef.name}=${it.value}")
            }

        }

    }

}