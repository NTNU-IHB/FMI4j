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

package no.ntnu.ihb.fmi4j.importer.me.vendors.fmusdk

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.solvers.Solver
import no.ntnu.ihb.fmi4j.solvers.apache.ApacheSolvers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.LoggerFactory

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VanDerPolTest {

    companion object {

        private val LOG = LoggerFactory.getLogger(VanDerPolTest::class.java)

        private const val stop = 1.0
        private const val macroStep = 1.0 / 10

        private val fmu = TestFMUs.fmi20().me()
                .vendor("FMUSDK").version("2.0.4")
                .name("vanDerPol").fmu().asModelExchangeFmu()

    }

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    private fun runFmu(solver: Solver) {

        LOG.info("Using solver: '${solver.name}'")

        fmu.newInstance(solver).use { slave ->

            val variableName = "x0"
            val x0 = slave.modelVariables
                    .getByName(variableName).asRealVariable()

            Assertions.assertTrue(slave.simpleSetup())

            while (slave.simulationTime <= stop) {
                val read = x0.read(slave)
                Assertions.assertTrue(read.status === FmiStatus.OK)
                LOG.info("t=${slave.simulationTime}, $variableName=${read.value}")
                Assertions.assertTrue(slave.doStep(macroStep))
            }

        }

    }

    @Test
    fun testEuler() {
        runFmu(ApacheSolvers.euler(1E-3))
    }

    @Test
    fun testRungeKutta() {
        runFmu(ApacheSolvers.rk4(1E-3))
    }

    @Test
    fun testLuther() {
        runFmu(ApacheSolvers.luther(1E-3))
    }

    @Test
    fun testMidpoint() {
        runFmu(ApacheSolvers.midpoint(1E-3))
    }

}