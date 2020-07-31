package no.ntnu.ihb.fmi4j.plugin

import no.ntnu.ihb.fmi4j.FmuBuilder
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies
import org.gradle.api.file.CopySpec
import org.gradle.jvm.tasks.Jar

open class FmuExportPluginExt {
    var outputDir = "."
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

                for (mainClass in ext.mainClasses) {

                    val args = arrayOf(
                            "-f", "${project.buildDir}/libs/${project.name}_shadow.jar",
                            "-m", mainClass,
                            "-d", ext.outputDir
                    )
                    println(args.toList())
                    FmuBuilder.main(args)

                }
            }
        }

        project.gradle.addListener(object: DependencyResolutionListener {

            override fun beforeResolve(deps: ResolvableDependencies) {
                val compileDefs = project.configurations.getByName("implementation").dependencies
                compileDefs.add(project.dependencies.create("no.ntnu.ihb.fmi4j:fmi-export:0.31.3"))
                project.gradle.removeListener(this)
            }

            override fun afterResolve(deps: ResolvableDependencies) {

            }
        })
    }
}
