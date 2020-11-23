package no.ntnu.ihb.fmi4j.plugin

import no.ntnu.ihb.fmi4j.FmuBuilder
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies
import org.gradle.api.file.CopySpec
import org.gradle.jvm.tasks.Jar
import java.io.File
import java.net.URI

open class FmuExportPluginExt {
    var version = "0.34.6"
    var outputDir: String? = null
    var mainClasses: MutableList<String> = mutableListOf()
}

class FmuExportPlugin: Plugin<Project> {

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun apply(project: Project) {

        val ext = project.extensions.create("fmi4jExport", FmuExportPluginExt::class.java)

        project.tasks.register("fatJar", Jar::class.java) {
            it.archiveBaseName.set("${project.name}_shadow")
            it.from(project.configurations.getByName("compile").map { file ->
                if (file.isDirectory) file else project.zipTree(file)
            })
            it.with(project.tasks.getByName("jar") as CopySpec)
        }

        project.task("exportFmu").apply {

            group = "fmi4j"
            dependsOn("fatJar")

            doLast {

                if (ext.mainClasses.isEmpty()) {
                    throw GradleException("No mainClass(es) defined!")
                }

                val defaultOutputDir by lazy {
                    File(project.buildDir, "fmus").apply {
                        mkdirs()
                    }
                }
                val outputDir = ext.outputDir ?: defaultOutputDir.absolutePath

                for (mainClass in ext.mainClasses) {

                    val args = arrayOf(
                            "-f", "${project.buildDir}/libs/${project.name}_shadow.jar",
                            "-m", mainClass,
                            "-d", outputDir
                    )
                    FmuBuilder.main(args)

                }
            }
        }

        project.repositories.maven {
            it.url=URI("https://dl.bintray.com/ntnu-ihb/mvn")
        }
        project.gradle.addListener(object : DependencyResolutionListener {

            override fun beforeResolve(deps: ResolvableDependencies) {
                val compileDefs = project.configurations.getByName("compile").dependencies
                compileDefs.add(project.dependencies.create("no.ntnu.ihb.fmi4j:fmi-export:${ext.version}"))
                project.gradle.removeListener(this)
            }

            override fun afterResolve(deps: ResolvableDependencies) {

            }

        })
    }
}
