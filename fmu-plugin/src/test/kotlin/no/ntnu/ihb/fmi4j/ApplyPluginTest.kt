package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnJre
import org.junit.jupiter.api.condition.JRE
import java.io.File

internal class ApplyPluginTest {

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    fun testApplyPlugin() {

        val intOut = -89
        val realOut = 2.0
        val testProjectDir = TemporaryFolder().apply { create() }

        testProjectDir.newFile("settings.gradle").apply {
            writeText("""
                pluginManagement {
                    repositories {
                        mavenCentral()
                        maven { url "https://plugins.gradle.org/m2/" }
                        maven { url "https://dl.bintray.com/ntnu-ihb/mvn" }
                        //mavenLocal()
                    }
              }
                rootProject.name = "testPlugin"
            """.trimIndent())
        }
        testProjectDir.newFile("build.gradle").apply {
            writeText("""
                plugins {
                    id "java-library" 
                    id "no.ntnu.ihb.fmi4j.fmu-export" version "0.36.0"
                }
                
                configurations.all {
                    // Check for updates every build
                    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
                }
                
                repositories {
                    mavenCentral()
                    //maven { url "https://dl.bintray.com/ntnu-ihb/mvn" }
                    //mavenLocal()
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
                
                import com.google.gson.Gson; //test import of external dependecy
                
                public class MySlave extends Fmi2Slave {

                    @ScalarVariable(causality = Fmi2Causality.output)
                    private double realOut = ${realOut};
                    
                    @ScalarVariable(causality = Fmi2Causality.output)
                    private int intOut = ${intOut};
                    
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

        println(result.output)

        Assertions.assertEquals(TaskOutcome.SUCCESS, result.task(":$taskName")?.outcome)

        val generatedFmu = File(testProjectDir.root, "build/fmus/MySlave.fmu")
        Assertions.assertTrue(generatedFmu.exists())

        Fmu.from(generatedFmu).asCoSimulationFmu().use { fmu ->
            fmu.newInstance().use {
                Assertions.assertEquals(realOut, it.readReal(0).value)
                Assertions.assertEquals(intOut, it.readInteger(0).value)
            }
        }

    }

}
