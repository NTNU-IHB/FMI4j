package no.mechatronics.sfi.fmi4j.crosscheck

import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.parser.ModelDescriptionParser
import no.sfi.mechatronics.fmi4j.me.ApacheSolvers
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Exception

private const val FMI4j_VERSION = "0.12.2"

private const val README = """
        The cross-check results have been generated with FMI4j's FmuDriver.
        To get more information download the 'fmudriver' tool from https://github.com/SFI-Mechatronics/FMI4j/releases and run:

        ```
        java -jar fmudriver.jar -h
        ```
        """

data class opt(
        var startTime: Double = 0.0,
        var stopTime: Double = 10.0,
        var stepSize: Double = 1e-3,
        var relTol: Double = 1e-3,
        var absTol: Double = 1e-3
)

object CrossChecker {

    private val LOG: Logger = LoggerFactory.getLogger(CrossChecker::class.java)

    fun parseDefaults(txt: String): opt {

        val opt = opt()

        txt.trim().split("\n").forEach { line ->

            val split = line.split(",")
            if (split.isNotEmpty()) {
                val (fst, snd) = split
                when (fst) {
                    "StartTime" -> {
                        opt.startTime = snd.toDouble()
                    }
                    "StopTime" -> {
                        opt.stopTime = snd.toDouble()
                    }
                    "StepSize" -> {
                        opt.stepSize = snd.toDouble()
                    }
                    "RelTol" -> {
                        opt.relTol = snd.toDouble()
                    }
                    "AbsTol" -> {
                        opt.absTol = snd.toDouble()
                    }
                }

            }

        }

        return opt
    }

    fun parseVariables(txt: String): Array<String> {
        return txt.split(",").let {
            it.subList(1, it.size).map { it.replace("^\"|\"$".toRegex(), "").trim() }.toTypedArray()
        }
    }


    fun crossCheck(fmuDir: File, resultDir: File): Boolean {

        val outputDir = File(resultDir, getDefaultOutputDir(fmuDir)).apply {
            if (!exists()) {
                mkdirs()
            }
        }.absolutePath

        val fmuPath = fmuDir.listFiles().find {
            it.name.endsWith(".fmu")
        }!!

        val inputData = fmuDir.listFiles().find {
            it.name.endsWith("in.csv")
        }

        val refData = fmuDir.listFiles().find {
            it.name.endsWith("ref.csv")
        }!!

        val variables = parseVariables(refData.reader().buffered().readLine())

        val defaults = parseDefaults(fmuDir.listFiles().find {
            it.name.endsWith(".opt")
        }!!.readText())

        var failedOrRejected = false

        fun reject(reason: String) {
            File(outputDir, "rejected").apply {
                createNewFile()
                writeText(reason)
            }
            LOG.warn("Rejected FMU '$fmuPath'. Reason: $reason")
            failedOrRejected = true
        }

        fun fail(reason: String) {
            File(outputDir, "failed").apply {
                createNewFile()
                writeText(reason)
            }
            LOG.warn("Failed to handle FMU '$fmuPath'. Reason: $reason")
            failedOrRejected = true
        }

        fun pass() {
            File(outputDir, "passsed").apply {
                createNewFile()
            }
        }

        try {
            val md = ModelDescriptionParser.parse(fmuPath)

            when {
                OS.LINUX.isCurrentOs && "JModelica.org" in fmuDir.absolutePath -> reject("System crashes")
                defaults.stepSize < 0 -> reject("Invalid stepSize")
                defaults.stepSize == 0.0 -> fail("Unable to handle variable step solver")
                inputData != null -> fail("Unable to handle input files")
                md.asCoSimulationModelDescription().needsExecutionTool -> reject("Requires execution tool")
                else -> FmuDriver(fmuPath, variables, outputDir).apply {

                    startTime = defaults.startTime
                    stopTime = defaults.stopTime
                    stepSize = defaults.stepSize


                }.run().also { success ->
                    if (success) {
                        pass()
                    } else {
                        fail("Unknown reason")
                    }
                }


            }

        } catch (ex: Exception) {

            fail("Program error: $ex")

        }

        File(outputDir, "README.md").apply {
            writeText(README)
        }

        return !failedOrRejected

    }

    private fun getDefaultOutputDir(fmuFile: File): String {
        var fmuFile = fmuFile
        var names = mutableListOf<String>()
        for (i in 0..2) {
            names.add(fmuFile.name)
            fmuFile = fmuFile.parentFile
        }

        names.addAll(listOf(FMI4j_VERSION, "FMI4j"))

        for (i in 0..2) {
            val name = when (fmuFile.name) {
                "CoSimulation" -> "cs"
                "ModelExchange" -> "me"
                "FMI_2.0" -> "2.0"
                else -> fmuFile.name
            }
            names.add(name)
            fmuFile = fmuFile.parentFile
        }
        return names.reverse().let { names.joinToString("/") }
    }

}

fun main(args: Array<String>) {

    if (args.size != 1) {
        return
    }

    val platform = when {
        OS.LINUX.isCurrentOs -> "linux64"
        OS.WINDOWS.isCurrentOs -> "win64"
        else -> throw AssertionError("Invalid platform..")
    }

    val crossCheckDir = "${args[0]}"
    val csPath = File("$crossCheckDir/fmus/2.0/cs/$platform")

    File("$crossCheckDir/results/2.0/cs/$platform/FMI4j/$FMI4j_VERSION").apply {
        if (exists()) deleteRecursively()
    }

    File("$crossCheckDir/results/2.0/me/$platform/FMI4j/$FMI4j_VERSION").apply {
        if (exists()) deleteRecursively()
    }


    fun crosscheck(dir: File): Int {

        var passed = 0
        dir.listFiles().forEach { vendor ->

            vendor.listFiles().forEach { version ->
                version.listFiles().forEach { fmu ->
                    if (CrossChecker.crossCheck(fmu, File(crossCheckDir, "results"))) {
                        passed++
                    }

                }

            }

        }
        return passed
    }

    println("${crosscheck(csPath)} Co-simulation FMUs passed cross-check")

}