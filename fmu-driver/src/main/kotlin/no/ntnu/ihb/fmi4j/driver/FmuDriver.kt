/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.driver

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu


/**
 * @author Lars Ivar Hatledal
 */
class FmuDriver(
        private val fmu: Fmu,
        private val options: DriverOptions
) {

    fun run() {
        if (options.modelExchange) {
            if (options.modelExchange && !fmu.supportsModelExchange) {
                throw Failure("FMU does not support Model Exchange!")
            }
            SlaveDriver(fmu.asModelExchangeFmu()
                    .newInstance(options.solver), options, fmu.name).run()
        } else {
            if (!options.modelExchange && !fmu.supportsCoSimulation) {
                throw Failure("FMU does not support Co-simulation!")
            }
            SlaveDriver(fmu.asCoSimulationFmu().newInstance(), options, fmu.name).run()
        }
    }

}
