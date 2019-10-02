
#include "../src/fmi/fmi2Functions.h"
#include "../src/fmi4j/SlaveInstance.hpp"

#include <iostream>
#include <string>
#include <vector>

namespace
{

std::string to_string(fmi2Status status)
{
    switch (status) {
        case fmi2OK:
            return "OK";
        case fmi2Warning:
            return "Warning";
        case fmi2Discard:
            return "Discard";
        case fmi2Error:
            return "Error";
        case fmi2Fatal:
            return "Fatal";
        case fmi2Pending:
            return "Pending";
        default:
            return "Unknown";
    }
}

void logger(void* fmi2ComponentEnvironment, fmi2String instance_name, fmi2Status status, fmi2String category,
    fmi2String message, ...)
{

    char msg[1000];
    va_list argp;

    va_start(argp, message);
    vsprintf(msg, message, argp);
    va_end(argp);

    std::cout << ("[FMI callback logger] status=" + to_string(status) + ", instanceName=" + instance_name +
        ", category=" + category + ", message=" + msg);
}

const fmi2CallbackFunctions callback = {
    logger, calloc, free, nullptr, nullptr};

} // namespace

int main()
{

    try {
        std::cout << "version: " << fmi2GetVersion() << std::endl;
        auto c = fmi2Instantiate("", fmi2CoSimulation, "guid", R"(file://D:\Development\FMI4j\java\fmi4j\fmu-slaves\build\libs)", &callback, 0, 0);
        if (c == nullptr) {
            return -1;
        }

        fmi2SetupExperiment(c, false, 0.0, 0.0, false, 0.0);
        fmi2EnterInitializationMode(c);
        fmi2ExitInitializationMode(c);
        fmi2DoStep(c, 0, 0.1, true);

        {
            std::vector<fmi2ValueReference >vr = {0};
            std::vector<fmi2Real > ref(1);
            fmi2GetReal(c, vr.data(), vr.size(), ref.data());
            std::cout << ref[0] << std::endl;
        }

        {
            std::vector<fmi2ValueReference >vr = {1};
            std::vector<fmi2Integer > ref(1);
            fmi2GetInteger(c, vr.data(), vr.size(), ref.data());
            std::cout << ref[0] << std::endl;
        }


    } catch (const std::exception& e) {
        std::cout << e.what() << std::endl;
    }

    return 0;
}