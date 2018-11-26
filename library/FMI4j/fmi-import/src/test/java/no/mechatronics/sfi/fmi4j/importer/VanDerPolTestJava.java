/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

package no.mechatronics.sfi.fmi4j.importer;

import no.mechatronics.sfi.fmi4j.TestUtils;
import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmi4j.common.FmuRead;
import no.mechatronics.sfi.fmi4j.common.FmuSlave;
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable;
import no.mechatronics.sfi.fmi4j.solvers.Solver;
import no.sfi.mechatronics.fmi4j.me.ApacheSolvers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Lars Ivar Hatledal
 */
@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
public class VanDerPolTestJava {

    private static final Logger LOG = LoggerFactory.getLogger(VanDerPolTestJava.class);

    private static Fmu fmu;

    @BeforeAll
    public static void setup() throws IOException {
        final File file = new File(TestUtils.getTEST_FMUs(),
                "2.0/me/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu");
        Assertions.assertTrue(file.exists());
        fmu = Fmu.from(file);
    }

    @AfterAll
    public static void tearDown() {
        fmu.close();
    }

    @Test
    public void testVersion() {
        Assertions.assertEquals("2.0", fmu.getModelDescription().getFmiVersion());
    }

    private void runFmu(Solver solver) {

        LOG.info("Using solver: {}", solver.getName());

        FmuSlave slave = VanDerPolTestJava.fmu.asModelExchangeFmu()
                .newInstance(solver, false, true);

        RealVariable x0 = slave.getModelVariables()
                .getByName("x0").asRealVariable();

        slave.setupExperiment();
        slave.enterInitializationMode();
        slave.exitInitializationMode();

        double macroStep = 1.0 / 10;
        while (slave.getSimulationTime() < 1) {
            FmuRead<Double> read = x0.read(slave);
            Assertions.assertSame(read.getStatus(), FmiStatus.OK);
            LOG.info("t={}, x0={}", slave.getSimulationTime(), read.getValue());
            Assertions.assertTrue(slave.doStep(macroStep));
        }

        slave.terminate();
    }

    @Test
    public void testEuler() {
        runFmu(ApacheSolvers.euler(1E-3));
    }

    @Test
    public void testRungeKutta() {
        runFmu(ApacheSolvers.rk4(1E-3));
    }

    @Test
    public void testLuther() {
        runFmu(ApacheSolvers.luther(1E-3));
    }

}


