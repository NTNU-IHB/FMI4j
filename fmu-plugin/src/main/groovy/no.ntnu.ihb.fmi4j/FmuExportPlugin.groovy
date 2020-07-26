package no.ntnu.ihb.fmi4j

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar

class FatJarTask extends Jar {

    @TaskAction
    def invoke() {

        archiveBaseName.set("${project.name}_shadow")
        from { project.configurations.compile.collect { it.isDirectory() ? it : project.zipTree(it) } }
        with jar

    }

}

class FmuExportConfiguration {
    String outputDir = "."
    String mainClass = null
}

class FmuExportPluginExt {

    List<FmuExportConfiguration> configurations = []

    def fmu(Closure<FmuExportConfiguration> closure) {
        def conf = new FmuExportConfiguration()
        closure(conf)
        configurations.add(conf)
    }

}

class FmuExportPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def ext = project.extensions.create("fmuExport", FmuExportPluginExt)

        project.task("invoke") {
            doLast {

                for (conf in ext.configurations) {

                    if (conf.mainClass == null) {
                        throw new GradleException("No mainClass defined!")
                    }

                    String[] args = [
                            "-f", "${project.buildDir}/libs/${project.name}_shadow.jar",
                            "-m", conf.mainClass,
                            "-d", conf.outputDir
                    ]
                    FmuBuilder.main(args)

                }
            }
        }

    }

}
