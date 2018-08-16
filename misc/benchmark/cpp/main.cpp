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

#include <iostream>
#include <ctime>
#include <memory>

#include "FmuWrapper.h"

using namespace std;

string getOs() {
#ifdef _WIN32
    return "win32";
#elif _WIN64
    return "win64";
#elif __linux__
    return "linux64";
#endif
}

bool runInstance(shared_ptr<FmuInstance> instance, double stop, double step_size, int vr) {
    instance->init();

    clock_t begin = clock();

    RealRead read;
    while (instance->getCurrentTime() <= stop - step_size) {
        fmi2_status_t status = instance->step(step_size);

        if (status != fmi2_status_ok) {
            return false;
        }

        instance->getReal(vr, read);

    }

    clock_t end = clock();

    long elapsed_secs = (long) ((double(end-begin) / CLOCKS_PER_SEC) * 1000.0);
    cout << "elapsed=" << elapsed_secs << "ms" << endl;

    instance->terminate();

    return true;

}

int main() {

    const char* TEST_FMUs = getenv("TEST_FMUs");
    if (!TEST_FMUs) {
        cout << "No env variable 'TEST_FMUs' pointing to the location of the FMI test FMUs" << endl;
        return -1;
    }

    string fmu_path = string(TEST_FMUs) + "/FMI_2.0/CoSimulation/" + getOs()
            + "/20sim/4.6.4.8004/TorsionBar/TorsionBar.fmu";
    cout << "fmu_path=" << fmu_path << endl;

    FmuWrapper fmu = FmuWrapper(fmu_path);
    shared_ptr<FmuInstance> instance = fmu.newInstance();

    double stop = 12;
    double step_size = 1E-5;
    runInstance(instance, stop, step_size, 2);

    return 0;
}