package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.invoke
import java.io.File

open class FmuPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        target.run {
            tasks {
                "generateSources"(Task::class) {
                    doLast {
                        compileSources(target)
                    }
                }

            }
        }

    }

    private fun compileSources(target: Project) {

        val srcDir = File(target.projectDir, "src/main/resources/fmus")
        if (!srcDir.exists()) {
            throw IllegalArgumentException("No such file: '$srcDir'")
        }

        srcDir.listFiles().forEach { file ->

            if (file.name.toLowerCase().endsWith(".fmu")) {
                val md = ModelDescriptionParser.parse(file)
                val src = CodeGenerator(md).generateBody()

                println(src)

            }

        }
    }

}



