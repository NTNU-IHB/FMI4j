package example

import no.mechatronics.sfi.fmi4j.ControlledTemperature
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable

fun main(args: Array<String>) {

    ControlledTemperature.newInstance().use { instance ->

        instance.init()

        //Variables are grouped by causality and have types!
        val tempRef: RealVariable
                = instance.outputs.temperature_Reference()

        val stop = 10.0
        val stepSize = 1E-2
        while (instance.currentTime <= stop) {

            if (!instance.doStep(stepSize)) {
                break;
            }

            val read = tempRef.read()
            println("t=${instance.currentTime}, ${tempRef.name}=${read.value}")

        }

    }

}