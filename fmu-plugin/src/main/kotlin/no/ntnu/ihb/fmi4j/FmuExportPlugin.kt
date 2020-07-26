package no.ntnu.ihb.fmi4j

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import java.io.File

open class FmuExportPluginExt {

    var outputDir: String? = null
    var mainClass: String? = null

}

class FmuExportPlugin: Plugin<Project> {

    override fun apply(project: Project) {

        project.apply {
/*
           project.tasks.create("fatJar", Jar::class.java).apply {
                archiveBaseName.set("fatJar")
                from (project.configurations.getByName("compile").map {
                    if (it.isDirectory) it else project.zipTree(it)
                })
                with()
            }*/

            project.extensions.create("fmuExport", FmuExportPluginExt::class.java).also { ext ->

                project.task("invoke").apply {

                    group = "fmi4j"
                    dependsOn += "fatJar"

                    doLast {

                        val outputDir: File? = ext.outputDir?.let { File(it) }

                        FmuBuilder.main(arrayOf(
                                "-m", ext.mainClass!!,
                                "-f", ""
                        ))

                    }

                }

            }

        }

    }

}
