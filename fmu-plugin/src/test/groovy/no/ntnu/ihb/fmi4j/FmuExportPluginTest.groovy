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

        Assertions.assertTrue(project.getTasks().getByName("invoke") != null)
    }

}
