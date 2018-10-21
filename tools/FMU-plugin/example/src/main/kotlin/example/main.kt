package example

import no.mechatronics.sfi.fmi4j.ControlledTemperature
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable

fun main(args: Array<String>) {

    ControlledTemperature.newInstance().use { slave ->

        slave.setupExperiment()
        slave.enterInitializationMode()
        slave.exitInitializationMode()

        //Variables are grouped by causality and have types!
        val tempRef: RealVariable
                = slave.outputs.temperature_Reference()

        val stop = 0.1
        val stepSize = 1E-4
        while (slave.simulationTime <= stop) {

            if (!slave.doStep(stepSize)) {
                break
            }

            tempRef.read(slave).also {
                println("t=${slave.simulationTime}, ${tempRef.name}=${it.value}")
            }

        }

    }

}