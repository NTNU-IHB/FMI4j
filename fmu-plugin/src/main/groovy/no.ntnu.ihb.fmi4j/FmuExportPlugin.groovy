package no.ntnu.ihb.fmi4j

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies
import org.gradle.jvm.tasks.Jar

class FmuExportPluginExt {
    String outputDir = "."
    List<String> mainClasses = []
}

class FmuExportPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def ext = project.extensions.create("fmi4jExport", FmuExportPluginExt)

        project.tasks.register("fatJar", Jar) {
            archiveBaseName.set("${project.name}_shadow")
            from { project.configurations.compile.collect { it.isDirectory() ? it : project.zipTree(it) } }
            with project.tasks.getByName('jar')
        }

        project.task("exportFmu") {

            group = "fmi4j"
            dependsOn "fatJar"

            doLast {

                if (ext.mainClasses == null && ext.mainClasses.isEmpty()) {
                    throw new GradleException("No mainClass(es) defined!")
                }

                for (mainClass in ext.mainClasses) {

                    String[] args = [
                            "-f", "${project.buildDir}/libs/${project.name}_shadow.jar",
                            "-m", mainClass,
                            "-d", ext.outputDir
                    ]
                    println args.toList()
                    println Class.forName("no.ntnu.ihb.fmi4j.FmuBuilder") == null
                    FmuBuilder.main(args)

                }
            }
        }

        /*project.repositories.maven {
            url "https://dl.bintray.com/ntnu-ihb/mvn"
        }*/
        project.getGradle().addListener(new DependencyResolutionListener() {
            @Override
            void beforeResolve(ResolvableDependencies deps) {
                def compileDefs = project.getConfigurations().getByName("compile").getDependencies()
                compileDefs.add(project.getDependencies().create("no.ntnu.ihb.fmi4j:fmi-export:0.31.3"))
                project.getGradle().removeListener(this)
            }

            @Override
            void afterResolve(ResolvableDependencies deps) {

            }
        })

    }

}
