package no.mechatronics.sfi.fmi4j.modeldescription.misc

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable


class LogCategories : Iterable<LogCategory>, Serializable {

    val size
        get() = categories.size

    override fun iterator(): Iterator<LogCategory> = categories.iterator()

    @JacksonXmlProperty(localName = "Category")
    @JacksonXmlElementWrapper(useWrapping = false)
    private val _categories: List<LogCategory>? = null

    private val categories: List<LogCategory>
        get() = _categories ?: emptyList()

    operator fun contains(category: LogCategory) = categories.contains(category)

    operator fun contains(category: String) = categories.map { it.name }.contains(category)

    override fun toString(): String {
        return "LogCategoriesImpl(size=$size, categories=$categories)"
    }

}


/**
 * @author Lars Ivar Hatledal
 */
data class LogCategory(

        @JacksonXmlProperty
        val name: String,

        @JacksonXmlProperty
        val description: String? = null

) : Serializable

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



