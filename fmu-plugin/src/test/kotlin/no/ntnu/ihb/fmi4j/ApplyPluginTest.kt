package no.ntnu.ihb.fmi4j

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File

internal class ApplyPluginTest {

    @Test
    @DisabledOnOs(OS.LINUX)
    fun testApplyPlugin() {

        val testProjectDir = TemporaryFolder().apply { create() }

        testProjectDir.newFile("settings.gradle").apply {
            writeText("""
                pluginManagement {
                    repositories {
                        mavenCentral()
                        mavenLocal()
                    }
                }
                rootProject.name = "testPlugin"
            """.trimIndent())
        }
        testProjectDir.newFile("build.gradle").apply {
            writeText("""
                plugins {
                    id "java-library" 
                    id "no.ntnu.ihb.fmi4j.fmu-export" version "0.31.3"
                }
                
                configurations.all {
                    // Check for updates every build
                    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
                }
                
                repositories {
                    mavenCentral()
                    mavenLocal()
                }

                dependencies {
                    implementation group: 'com.google.code.gson', name: 'gson', version: '2.8.6'
                }
                
                fmi4jExport {
                    mainClasses += "no.ntnu.ihb.MySlave"
                }
            """.trimIndent())
        }

        testProjectDir.newFolder("src", "main", "java", "no", "ntnu", "ihb")
        testProjectDir.newFile("src/main/java/no/ntnu/ihb/MySlave.java").apply {
            writeText("""
                package no.ntnu.ihb;
                
                import java.util.Map;
                import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave;
                import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable;
                import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
                
                public class MySlave extends Fmi2Slave {
                
                    @ScalarVariable(causality = Fmi2Causality.output)
                    double realOut = 1.0;
                    
                    public MySlave(Map<String, Object> args) {
                        super(args);
                    }
                
                    @Override
                    public void doStep(double t, double dt) {
                    }
                
                }
            """.trimIndent())
        }

        val taskName = "exportFmu"
        val result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(taskName)
                .build()

        Assertions.assertEquals(TaskOutcome.SUCCESS, result.task(":$taskName")?.outcome)
        Assertions.assertTrue(File(testProjectDir.root, "build/fmus/MySlave.fmu").exists())

    }

}
