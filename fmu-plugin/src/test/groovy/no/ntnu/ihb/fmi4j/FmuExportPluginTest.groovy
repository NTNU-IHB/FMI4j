package no.ntnu.ihb.fmi4j

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FmuExportPluginTest {

    @Test
    void fmuExportPluginAddsInvokeTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getPluginManager().apply("no.ntnu.ihb.fmi4j.fmu-export")

        def invokeTask = project.getTasks().getByName("invoke")
        Assertions.assertTrue(invokeTask != null)
    }

    @Test
    void testExt() {
        def ext = new FmuExportPluginExt()
        ext.outputDir = "out"
        ext.mainClasses += "main"

        Assertions.assertEquals(1, ext.mainClasses.size())
        Assertions.assertEquals("out", ext.outputDir)
        Assertions.assertEquals("main", ext.mainClasses[0])
    }

}
