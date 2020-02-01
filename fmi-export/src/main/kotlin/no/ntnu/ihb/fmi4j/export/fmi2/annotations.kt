package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Initial
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability

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

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScalarVariable(
        val name: String = "",
        val causality: Fmi2Causality = Fmi2Causality.local,
        val variability: Fmi2Variability = Fmi2Variability.continuous,
        val initial: Fmi2Initial = Fmi2Initial.undefined
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Real(
        val unit: Array<String> = [],
        val min: DoubleArray = [],
        val max: DoubleArray = []
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class VariableContainer
