package no.mechatronics.sfi.fmi4j.importer.jni

import no.mechatronics.sfi.fmi4j.importer.misc.currentOS
import no.mechatronics.sfi.fmi4j.importer.misc.extractTo
import no.mechatronics.sfi.fmi4j.importer.misc.libExtension
import no.mechatronics.sfi.fmi4j.jni.FmiLibrary
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import java.io.File
import kotlin.system.measureTimeMillis


fun main(args: Array<String>) {

//    val file = File("${System.getenv("TEST_FMUs")}" +
//            "/FMI_2.0/CoSimulation/$currentOS/20sim/4.6.4.8004/" +
//            "ControlledTemperature/ControlledTemperature.fmu")
//    val dt = 1E-4
//    val stop = 10.0
//    val vr = 47

    val file = File("${System.getenv("TEST_FMUs")}" +
            "/FMI_2.0/CoSimulation/$currentOS/20sim/4.6.4.8004/" +
            "TorsionBar/TorsionBar.fmu")
    val dt = 1E-5
    val stop = 12.0
    val vr = intArrayOf(2)

    val temp = createTempDir(file.nameWithoutExtension).also {
        file.extractTo(it)
    }

    try {

        val md = ModelDescriptionParser.parse(File(temp, "modelDescription.xml")
                .readText()).asCoSimulationModelDescription()

        val libName = "${temp.absolutePath}/binaries/$currentOS/${md.modelIdentifier}.$libExtension"
        FmiLibrary(libName).use { lib ->

            println(lib.fmiVersion)
            println(lib.typesPlatform)

            val d = DoubleArray(1)

            for (i in 0..2) {
                val c = lib.instantiate(md.modelIdentifier, 1, md.guid, File(temp, "resources").absolutePath, false, false)

                val status = lib.setupExperiment(c, false, 1E-3, 0.0, 0.0)

                lib.enterInitializationMode(c)
                lib.exitInitializationMode(c)

                measureTimeMillis {

                    var t = 0.0
                    while (t <= stop) {

                        lib.getReal(c, vr, d)
//                        println("t=$t, value=${Arrays.toString(d)}")

                        val status = lib.step(c, t, dt, true)
                        t += dt
                    }

                }.also {
                    println("${it}ms")
                }

                lib.terminate(c)
            }

        }


    } finally {
        temp.deleteRecursively()
    }

}

