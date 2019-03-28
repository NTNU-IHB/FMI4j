package no.ntnu.ihb.fmi4j.modeldescription


/**
 * Provides default settings for the integrator, such as stop time and
 * relative tolerance.
 *
 * DefaultExperiment consists of the optional default start time, stop time, relative tolerance, and step size
 * for the first simulation run. A tool may ignore this information. However, it is convenient for a user that
 * startTime, stopTime, tolerance and stepSize have already a meaningful default value for the model at
 * hand. Furthermore, for CoSimulation the stepSize defines the preferred communicationStepSize.
 *
 * @author Lars Ivar Hatledal
 */
data class DefaultExperiment(

        /**
         * Default start time of simulation
         */
        val startTime: Double,

        /**
         * Default stop time of simulation
         */
        val stopTime: Double,

        /**
         * Default relative integration tolerance
         */
        val tolerance: Double,

        /***
         * ModelExchange: Default step size for fixed step integrators
         * CoSimulation: Preferred communicationStepSize
         */
        val stepSize: Double
)

typealias TypeDefinitions = List<SimpleType>

data class SimpleType(

        /**
         * Name of SimpleType element.
         * "name" must be unique with respect to all other elements
         * of the TypeDefinitions list. Furthermore, "name" of a SimpleType
         * must bee different to all "name"s of ScalarVariable
         */
        val name: String,

        /**
         * Description of the SimpleType
         */
        val description: String?

)


typealias UnitDefinitions = List<Unit>

data class Unit(

        val name: String,

        val baseUnit: BaseUnit?,

        val displayUnits: List<DisplayUnit>?
)

data class DisplayUnit(

        /**
         * Name of DisplayUnit element
         */
        val name: String,

        val factor: Double = 1.0,

        val offset: Double = 0.0

)

data class BaseUnit(

        /**
         * Exponent of SI base unit "kg"
         */
        val kg: Int = 0,

        /**
         * Exponent of SI base unit "m"
         */
        val m: Int = 0,

        /**
         * Exponent of SI base unit "s"
         */

        val s: Int = 0,

        /**
         * Exponent of SI base unit "A"
         */
        val A: Int = 0,

        /**
         * Exponent of SI base unit "K"
         */
        val K: Int = 0,

        /**
         * Exponent of SI base unit "mol"
         */
        val mol: Int = 0,

        /**
         * Exponent of SI base unit "cd"
         */
        val cd: Int = 0,

        /**
         * Exponent of SI base unit "rad"
         */
        val rad: Int = 0,

        val factor: Double = 1.0,

        val offset: Double = 0.0

)

typealias SourceFiles = List<SourceFile>

data class SourceFile(

        /**
         * Name of the file including the path to the sources
         * directory, using forward slash as separator
         */
        val name: String

)

typealias LogCategories = List<LogCategory>

data class LogCategory(

        val name: String,

        val description: String?

) {

    companion object {
        /**
         * Log all events (during initialization and simulation).
         */
        const val LOG_EVENTS = "logEvents"

        /**
         * Log the solution of linear systems of equations if the solution is singular
         * (and the tool picked one solution of the infinitely many solutions).
         */
        const val LOG_SINGULAR_LINEAR_SYSTEMS = "logSingularLinearSystems"

        /**
         * Log the solution of nonlinear systems of equations.
         */
        const val LOG_NON_LINEAR_SYSTEMS = "logNonlinearSystems"

        /**
         * Log the dynamic selection of states.
         */
        const val LOG_DYNAMIC_STATE_SELECTION = "logDynamicStateSelection"

        /**
         * Log messages when returning fmi2Warning status from any function.
         */
        const val LOG_STATUS_WARNING = "logStatusWarning"

        /**
         * Log messages when returning fmi2Discard status from any function.
         */
        const val LOG_STATUS_DISCARD = "logStatusDiscard"

        /**
         * Log messages when returning fmi2Error status from any function.
         */
        const val LOG_STATUS_ERROR = "logStatusError"

        /**
         * Log messages when returning fmi2Fatal status from any function.
         */
        const val LOG_STATUS_FATAL = "logStatusFatal"

        /**
         * Log messages when returning fmi2Pending status from any function.
         */
        const val LOG_STATUS_PENDING = "logStatusPending"

        /**
         * Log all messages
         */
        const val LOG_ALL = "logAll"
    }

}
