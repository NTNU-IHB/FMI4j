package example

import no.mechatronics.sfi.fmi4j.ControlledTemperature


fun main(args: Array<String>) {

    ControlledTemperature.newInstance().use {

        it.init()
        println(it.outputs.temperature_Reference().read())

    }

}