package no.ntnu.ihb.fmi4j

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class ApplyPluginTest {

    @Test
    @Disabled
    fun testApplyPlugin() {

        val testProjectDir = TemporaryFolder().apply { create() }

        testProjectDir.newFile("settings.gradle").apply {
            writeText("""
                rootProject.name = "testPlugin"
            """.trimIndent())
        }
        testProjectDir.newFile("build.gradle").apply {
            writeText("""
                plugins {
                    id "java-library" 
                    id "no.ntnu.ihb.fmi4j.fmu-export" version "0.31.2"
                }
                
                dependencies {
                    compile group: 'com.google.code.gson', name: 'gson', version: '2.8.6'
                }
                
                fmi4jExport {
                    fmu {
                        mainClasses += "no.ntnu.ihb.MySlave.java"
                    }
                }
            """.trimIndent())
        }

        testProjectDir.newFolder("src", "main", "java", "no", "ntnu", "ihb")
        testProjectDir.newFile("src/main/java/no/ntnu/ihb/JavaSlave.java").apply { }
        File("MySlave.java").apply {
            writeText("""
                package src.main.java.no.ntnu.ihb;
                
                public class MySlave {
                
                }
            """.trimIndent())
        }

        val result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("fmi4jExport")
                .build()

    }


}

