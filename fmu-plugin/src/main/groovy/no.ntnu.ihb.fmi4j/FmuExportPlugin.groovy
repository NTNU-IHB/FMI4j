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

    @Override
    String toString() {
        return "FmuExportConfiguration{" +
                "outputDir='" + outputDir + '\'' +
                ", mainClass='" + mainClass + '\'' +
                '}';
    }
}

class FmuExportPluginExt {

    String outputDir = "."
    List<String> mainClasses = []

}

class FmuExportPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def ext = project.extensions.create("fmi4jExport", FmuExportPluginExt)

        project.task("invoke") {
            doLast {

                if (ext.mainClasses == null && ext.mainClasses.isEmpty()) {
                    throw new GradleException("No mainClass(es) defined!")
                }

                for (mainClass in ext.outputDir) {

                    String[] args = [
                            "-f", "${project.buildDir}/libs/${project.name}_shadow.jar",
                            "-m", mainClass,
                            "-d", ext.outputDir
                    ]
                    FmuBuilder.main(args)

                }
            }
        }

    }

}
