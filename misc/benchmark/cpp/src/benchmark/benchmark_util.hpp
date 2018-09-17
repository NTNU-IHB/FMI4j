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
#include <ctime>
#include <memory>
#include <iostream>
#include <string>

#include <fmi/FmuWrapper.hpp>

namespace {

    std::string getOs() {
#ifdef _WIN32
        return "win32";
#elif _WIN64
        return "win64";
#elif __linux__
        return "linux64";
#endif
    }

    bool runInstance(std::shared_ptr<FmuInstance> instance, double stop, double step_size, int vr) {
        instance->init();

        clock_t begin = clock();

        RealRead read;
        double sum = 0.0;
        int64_t iter = 0;
        while (instance->getCurrentTime() <= stop - step_size) {
            fmi2_status_t status = instance->step(step_size);

            if (status != fmi2_status_ok) {
                return false;
            }

            instance->getReal(vr, read);
            sum += read.value;
            iter++;

        }

        clock_t end = clock();

        long elapsed_secs = (long) ((double(end-begin) / CLOCKS_PER_SEC) * 1000.0);
        cout << "elapsed=" << elapsed_secs << "ms, sum=" << sum << " ,iter=" << iter << endl;

        instance->terminate();

        return true;

    }

}