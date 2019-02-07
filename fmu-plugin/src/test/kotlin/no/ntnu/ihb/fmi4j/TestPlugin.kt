package no.ntnu.ihb.fmi4j

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestPlugin {

    private var temp = createTempDir("fmi4j_plugin", "")

    init {

        File(temp, "src/main/resources/fmus").apply {

            if (!mkdirs()) {
                throw RuntimeException("Unable to create directories")
            }

            val fmuName = "ControlledTemperature"
            File(TestPlugin::class.java.classLoader.getResource("ControlledTemperature.fmu").file).also {
                copyTo(File(this, fmuName))
            }
        }

        File(temp, "build.gradle").apply {
            writeText("""
                plugins {
                    id 'java'
                    id 'no.ntnu.ihb.fmi4j.FmuPlugin'
                }

            """.trimIndent())
        }

    }

    @Test
    fun test() {
        val task = "generateFMUWrappers"
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

}