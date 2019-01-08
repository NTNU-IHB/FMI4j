/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.ntnu.ihb.fmi4j.modeldescription.logging

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable

/**
 * @author Lars Ivar Hatledal
 */
data class LogCategory(

        @JacksonXmlProperty
        val name: String,

        @JacksonXmlProperty
        val description: String? = null

) : Serializable {

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

/**
 * @author Lars Ivar Hatledal
 */
class LogCategories : Iterable<LogCategory>, Serializable {

    val size: Int
        get() = categories.size

    override fun iterator(): Iterator<LogCategory> = categories.iterator()

    @JacksonXmlProperty(localName = "Category")
    @JacksonXmlElementWrapper(useWrapping = false)
    private val categories: List<LogCategory> = emptyList()

    operator fun contains(category: LogCategory) = categories.contains(category)

    operator fun contains(category: String) = categories.map { it.name }.contains(category)

    override fun toString(): String {
        return "LogCategories(size=$size, categories=$categories)"
    }

}
