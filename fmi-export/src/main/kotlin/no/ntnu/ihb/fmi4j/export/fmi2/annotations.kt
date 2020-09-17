package no.ntnu.ihb.fmi4j.export.fmi2

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SlaveInfo(
        val modelName: String = "",
        val author: String = "",
        val version: String = "",
        val description: String = "",
        val copyright: String = "",
        val license: String = "",
        val canHandleVariableCommunicationStepSize: Boolean = true,
        val canBeInstantiatedOnlyOncePerProcess: Boolean = false,
        val needsExecutionTool: Boolean = false
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultExperiment(
        val startTime: Double = 0.0,
        val stepSize: Double = -1.0,
        val stopTime: Double = -1.0
)
