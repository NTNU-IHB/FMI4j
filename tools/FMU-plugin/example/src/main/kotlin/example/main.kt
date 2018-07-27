package example

import no.mechatronics.sfi.fmi4j.ControlledTemperature


fun main(args: Array<String>) {

    ControlledTemperature.newInstance().use {

        it.init()
        it.outputs.temperature_Reference().apply {
            println("$name=${read()}")
        }
    }

}