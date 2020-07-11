package no.ntnu.ihb.fmi4j

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class FmuExportPluginExt {

    var outputDir: File? = null

}

class FmuExportPlugin: Plugin<Project> {

    override fun apply(project: Project) {

        project.apply {

            project.extensions.create("fmi4j", FmuExportPluginExt::class.java).also { ext ->

            }

        }

    }

}
