package no.ntnu.ihb.fmi4j.fmudriver

import no.ntnu.ihb.fmi4j.modeldescription.parser.ModelDescriptionParser
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

private const val FMI4j_VERSION = "0.13.1"

private const val README = """
        The cross-check results have been generated with FMI4j's FmuDriver.
        To get more information download the 'fmudriver' tool from https://github.com/SFI-Mechatronics/FMI4j/releases and run:

        ```
        java -jar fmudriver.jar -h
        ```
        """

data class XCOptions(
        var startTime: Double = 0.0,
        var stopTime: Double = 10.0,
        var stepSize: Double = 1e-3,
        var relTol: Double = 1e-3,
        var absTol: Double = 1e-3
) {

    companion object {

        fun parse(txt: String): XCOptions {

            return XCOptions().apply {

                txt.trim().split("\n").forEach { line ->

                    val split = line.split(",")
                    if (split.isNotEmpty()) {
                        val (fst, snd) = split
                        when (fst) {
                            "StartTime" -> {
                                startTime = snd.toDouble()
                            }
                            "StopTime" -> {
                                stopTime = snd.toDouble()
                            }
                            "StepSize" -> {
                                stepSize = snd.toDouble()
                            }
                            "RelTol" -> {
                                relTol = snd.toDouble()
                            }
                            "AbsTol" -> {
                                absTol = snd.toDouble()
                            }
                        }

                    }

                }

            }

        }

    }
}

object CrossChecker {

    private val LOG: Logger = LoggerFactory.getLogger(CrossChecker::class.java)

    private fun parseVariables(txt: String): List<String>{
        return txt.split(",").let {
            it.subList(1, it.size).map { it.replace("^\"|\"$".toRegex(), "").trim() }
        }
    }


    private fun crossCheck(fmuDir: File, resultDir: File): Boolean {

        val outputFolder = File(resultDir, getDefaultOutputDir(fmuDir)).apply {
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

        val defaults = XCOptions.parse(fmuDir.listFiles().find {
            it.name.endsWith(".opt")
        }!!.readText())

        var failedOrRejected = false

        fun reject(reason: String) {
            File(outputFolder, "rejected").apply {
                createNewFile()
                writeText(reason)
            }
            LOG.warn("Rejected FMU '$fmuPath'. Reason: $reason")
            failedOrRejected = true
        }

        fun fail(reason: String) {
            File(outputFolder, "failed").apply {
                createNewFile()
                writeText(reason)
            }
            LOG.warn("Failed to handle FMU '$fmuPath'. Reason: $reason")
            failedOrRejected = true
        }

        fun pass() {
            File(outputFolder, "passed").apply {
                createNewFile()
            }
            LOG.info("FMU '$fmuPath' passed crosscheck")
        }

        try {
            val md = ModelDescriptionParser.parse(fmuPath)

            val options = DriverOptions(
                    startTime = defaults.startTime,
                    stopTime = defaults.stopTime,
                    stepSize = defaults.stepSize,

                    outputVariables = variables,
                    outputFolder = outputFolder,

                    failOnLargeSize = true
            )

            when {
                OS.LINUX.isCurrentOs && "JModelica.org" in fmuDir.absolutePath -> reject("System crashes.")
                defaults.stepSize < 0 -> reject("Invalid stepSize (stepSize < 0).")
                defaults.startTime >= defaults.stopTime -> reject("Invalid start and or stop time (startTime >= stopTime).")
                defaults.stepSize == 0.0 -> fail("Don't know how to handle variable step solver (stepsize=0.0).")
                inputData != null -> fail("Unable to handle input files yet.")
                md.asCoSimulationModelDescription().needsExecutionTool -> reject("FMU requires execution tool.")
                else -> FmuDriver(fmuPath, options).run()
            }

            if (!failedOrRejected) {
                pass()
            }

        } catch (ex: Exception) {

            when (ex) {
                is Rejection -> reject(ex.reason)
                is Failure -> fail(ex.reason)
                else -> fail("Program error: $ex")
            }
        }

        File(outputFolder, "README.md").apply {
            writeText(README)
        }

        return !failedOrRejected

    }

    private fun getDefaultOutputDir(fmuFile: File): String {
        var currentFile = fmuFile
        var names = mutableListOf<String>()
        for (i in 0..2) {
            names.add(currentFile.name)
            currentFile = currentFile.parentFile
        }

        names.addAll(listOf(FMI4j_VERSION, "FMI4j"))

        for (i in 0..2) {
            names.add(currentFile.name)
            currentFile = currentFile.parentFile
        }
        return names.reverse().let { names.joinToString("/") }
    }

    fun run(crossCheckDir: String) {

        val platform = when {
            OS.LINUX.isCurrentOs -> "linux64"
            OS.WINDOWS.isCurrentOs -> "win64"
            else -> throw AssertionError("Invalid platform..")
        }

        val csPath = File("$crossCheckDir/fmus/2.0/cs/$platform")

        File("$crossCheckDir/results/2.0/cs/$platform/FMI4j/$FMI4j_VERSION").apply {
            if (exists()) deleteRecursively()
        }

        File("$crossCheckDir/results/2.0/me/$platform/FMI4j/$FMI4j_VERSION").apply {
            if (exists()) deleteRecursively()
        }

        fun crosscheck(dir: File): Int {

            var numPassed = AtomicInteger(0)

            val fmus = mutableListOf<File>();
            dir.listFiles().forEach { vendor ->
                vendor.listFiles().forEach { version ->
                    version.listFiles().forEach { fmu ->
                        fmus.add(fmu)

                    }
                }

            }

            fmus.parallelStream().forEach { fmu ->
                if (CrossChecker.crossCheck(fmu, File(crossCheckDir, "results"))) {
                    numPassed.incrementAndGet()
                }
            }

            return numPassed.get()
        }

        LOG.info("${crosscheck(csPath)} Co-simulation FMUs passed cross-check")
    }

}

fun main(args: Array<String>) {

    if (args.size != 1) {
        return
    }

    CrossChecker.run(args[0])

}