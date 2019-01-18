package no.ntnu.ihb.fmi4j.importer

import no.ntnu.ihb.fmi4j.common.FmuVariableAccessor
import no.ntnu.ihb.fmi4j.common.FmuVariableAccessorProvider
import no.ntnu.ihb.fmi4j.modeldescription.variables.RealVariable
import java.io.File

fun main() {

    val stepSize = 1E-4
    val stopTime = 25.0

    val fmuDir = "/home/laht/Downloads/CSE_DEMO_FMUS_FROM_NTNU"
    val controller = "CraneController".let{Fmu.from(File("$fmuDir/$it.fmu")).asCoSimulationFmu().newInstance(loggingOn = true)}
    val crane = "KnuckleBoomCrane".let{Fmu.from(File("$fmuDir/$it.fmu")).asCoSimulationFmu().newInstance(loggingOn = true)}

    val connections = listOf(

            controller.modelVariables.getByName("p_Crane.e[1]").asRealVariable().bind(controller) to crane.modelVariables.getByName("p_Crane.e[1]").asRealVariable().bind(crane),
            controller.modelVariables.getByName("p_Crane.e[2]").asRealVariable().bind(controller) to crane.modelVariables.getByName("p_Crane.e[2]").asRealVariable().bind(crane),
            controller.modelVariables.getByName("p_Crane.e[3]").asRealVariable().bind(controller) to crane.modelVariables.getByName("p_Crane.e[3]").asRealVariable().bind(crane),

            crane.modelVariables.getByName("p_Crane.f[1]").asRealVariable().bind(crane) to controller.modelVariables.getByName("p_Crane.f[1]").asRealVariable().bind(controller),
            crane.modelVariables.getByName("p_Crane.f[2]").asRealVariable().bind(crane) to controller.modelVariables.getByName("p_Crane.f[2]").asRealVariable().bind(controller),
            crane.modelVariables.getByName("p_Crane.f[3]").asRealVariable().bind(crane) to controller.modelVariables.getByName("p_Crane.f[3]").asRealVariable().bind(controller),

            crane.modelVariables.getByName("Act_Limits[1]").asRealVariable().bind(crane) to controller.modelVariables.getByName("Act_Limits[1]").asRealVariable().bind(controller),
            crane.modelVariables.getByName("Act_Limits[2]").asRealVariable().bind(crane) to controller.modelVariables.getByName("Act_Limits[2]").asRealVariable().bind(controller),
            crane.modelVariables.getByName("Act_Limits[3]").asRealVariable().bind(crane) to controller.modelVariables.getByName("Act_Limits[3]").asRealVariable().bind(controller)

    )

    val slaves = listOf(controller, crane).apply {
        forEach { it.simpleSetup() }
    }


    val outputs = listOf(
            crane.modelVariables.getByName("Inertia_Crane.theta_deg[1]").asRealVariable().bind(crane),
            crane.modelVariables.getByName("Inertia_Crane.theta_deg[2]").asRealVariable().bind(crane),
            crane.modelVariables.getByName("Inertia_Crane.theta_deg[3]").asRealVariable().bind(crane)
    )

    controller.modelVariables.getByName("Motor").asRealVariable().bind(controller).apply {
        write(-1.0) // actuator1 gain
    }

    controller.modelVariables.getByName("Cylinder1").asRealVariable().bind(controller).apply {
        write(1.0) // actuator2 gain
    }

    controller.modelVariables.getByName("Cylinder2").asRealVariable().bind(controller).apply {
        write(1.0) // actuator3 gain
    }

    var t = 0.0
    while ( t <= stopTime) {

        connections.forEach { (output, input) ->
            input.write(output.read())
        }

        slaves.forEach {
            it.doStep(stepSize)
        }

        t += stepSize

    }

    print("t=$t, ")
    println(outputs.map { it.read() }.toList())

    slaves.forEach { it.terminate() }

}


class InstanceVariable(
        private val variable: RealVariable,
        private val accessor: FmuVariableAccessor
) {

    fun read(): Double {
        return variable.read(accessor).value;
    }

    fun write(value: Double): Boolean {
        return variable.write(accessor, value).isOK()
    }

}

fun RealVariable.bind(accessorProvider: FmuVariableAccessorProvider): InstanceVariable {
    return InstanceVariable(this, accessorProvider.variableAccessor)
}