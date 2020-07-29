package no.ntnu.ihb.fmi4j

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class ApplyPluginTest {

    @Test
    fun testApplyPlugin() {

        val testProjectDir = TemporaryFolder().apply { create() }

        testProjectDir.newFile("settings.gradle").apply {
            writeText("""
                pluginManagement {
                    repositories {
                        mavenLocal()
                        mavenCentral()
                    }
                }
                rootProject.name = "testPlugin"
            """.trimIndent())
        }
        testProjectDir.newFile("build.gradle").apply {
            writeText("""
                plugins {
                    id "java-library" 
                    id "no.ntnu.ihb.fmi4j.fmu-export" version "0.31.2"
                }
                
                repositories {
                    mavenCentral()
                    mavenLocal()
                }
                
                configurations.all {
                    // Check for updates every build
                    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
                }
                
                dependencies {
                    compile group: 'com.google.code.gson', name: 'gson', version: '2.8.6'
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
                
                import no.ntnu.ihb.fmi4j.export.fmi2.*;
                
                public class MySlave extends Fmi2Slave {
                
                    @ScalarVariable(causality = Fmi2Causality.output)
                    double realOut = 1.0
                    
                    public MySlave(Map<String, Object> args) {
                        super(args);
                    }
                
                    @Override
                    public void doStep(double t, double dt) {
                        double currentTime, double dt
                    }
                
                }
            """.trimIndent())
        }

        val result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("exportFmu")
                .build()

    }


}

