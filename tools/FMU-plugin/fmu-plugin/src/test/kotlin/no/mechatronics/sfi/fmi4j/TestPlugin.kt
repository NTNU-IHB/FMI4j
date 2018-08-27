package no.mechatronics.sfi.fmi4j

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestPlugin {

    private var temp = createTempDir("fmi4j_plugin", "")

    init {

        File(temp, "src/main/resources/fmus").apply {

            if (!mkdirs()) {
                throw RuntimeException("Unable to create directories")
            }

            val fmuName = "ControlledTemperature.fmu"
            val fmu = File(getTEST_FMUs(),
                    "FMI_2.0/CoSimulation/${getOs()}" +
                            "/20sim/4.6.4.8004/ControlledTemperature/$fmuName")

            fmu.copyTo(File(this, fmuName))
        }

        File(temp, "build.gradle").apply {
            writeText("""
                plugins {
                    id 'java'
                    id 'no.mechatronics.sfi.fmi4j.FmuPlugin'
                }

            """.trimIndent())
        }

    }

    @Test
    fun test() {
        val task = "generateSources"
        gradle(task).also {
            println(it.output)
        }
    }

    @AfterAll
    fun tearDown() {
        temp.deleteRecursively()
    }

    /**
     * Helper method that runs a Gradle task in the temp
     * @param arguments the task arguments to execute
     * @param isSuccessExpected boolean representing whether or not the build is supposed to fail
     * @return the task's BuildResult
     */
    private fun gradle(isSuccessExpected: Boolean = true, vararg arguments: String = arrayOf("tasks")): BuildResult {
        val runner = GradleRunner.create()
                .withArguments("tasks", *arguments, "--stacktrace")
                .withProjectDir(temp)
                .withPluginClasspath()
                .withDebug(true)
        return if (isSuccessExpected) runner.build() else runner.buildAndFail()
    }

    private fun gradle(vararg arguments: String): BuildResult {
        return gradle(true, *arguments)
    }

    private fun getTEST_FMUs(): String {
        return System.getenv("TEST_FMUs") ?: throw IllegalStateException("TEST_FMUs not found on PATH!")
    }

    private fun getOs(): String {

        return when {
            OS.LINUX.isCurrentOs -> "linux64"
            OS.WINDOWS.isCurrentOs -> "win64"
            OS.MAC.isCurrentOs -> "darwin64"
            else -> throw IllegalStateException("Unsupported OS")
        }

    }

}