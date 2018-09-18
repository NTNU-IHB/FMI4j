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

#ifndef FMU_PROXY_FMUWRAPPER_H
#define FMU_PROXY_FMUWRAPPER_H

#include <string>
#include <memory>
#include <fmilib.h>
#include <experimental/filesystem>


using namespace std;
namespace fs = std::experimental::filesystem;

struct RealRead {
    fmi2_status_t status;
    double value;
};


class FmuInstance {

private:

    fmi2_import_t *instance;

    double current_time = 0.0;
    bool terminated = false;

public:
    FmuInstance(fmi2_import_t* fmu);

    void init(double start = 0, double end = 0);

    fmi2_status_t step(double step_size);

    fmi2_status_t reset();

    fmi2_status_t terminate();

    double getCurrentTime() {
        return current_time;
    }

    bool isTerminated() {
        return terminated;
    }

    void getReal(unsigned int vr, RealRead& read);
    void getReal(string name, RealRead& read);

    ~FmuInstance();

};

class FmuWrapper {

private:

    fs::path tmp_path;
    fmi2_import_t* xml;
    fmi_xml_context_t* ctx;
    jm_callbacks callbacks;
    fmi_version_enu_t version;

public:
    FmuWrapper(string fmu_path);

    shared_ptr<FmuInstance> newInstance();

    ~FmuWrapper();

};

#endif //FMU_PROXY_FMUWRAPPER_H

