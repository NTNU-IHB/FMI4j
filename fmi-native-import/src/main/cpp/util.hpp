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

#if defined(_MSC_VER) || defined(WIN32) || defined(__MINGW32__)
#include <windows.h>
#define DLL_HANDLE HMODULE
#else
#define DLL_HANDLE void*
#include <dlfcn.h>
#endif

#include <string>
#include <sstream>
#include <iostream>

namespace {

    template<class T>
    T load_function(DLL_HANDLE handle, const char *function_name) {
    #ifdef WIN32
        return (T) GetProcAddress(handle, function_name);
    #else
        return (T) dlsym(handle, function_name);
    #endif
    }

    std::string getLastError() {
    #ifdef WIN32
        std::ostringstream os;
        os << GetLastError();
        return os.str();
    #else
        return dlerror();
    #endif
    }

    DLL_HANDLE load_library(const char* dir, const char* libName) {
        DLL_HANDLE lib = nullptr;
        const std::string libPath = std::string(dir) + "/" + std::string(libName);
    #ifdef WIN32
        SetDllDirectory(dir);
        lib = LoadLibrary(libName);
    #else
        lib = dlopen(libPath.c_str(), RTLD_NOW | RTLD_LOCAL);
    #endif
        if (lib == nullptr) {

            const std::string err = std::string("[FMI native bridge] Fatal: Failed to load library '") + libPath + std::string("', error: ") + getLastError() ;
            std::cerr << err << std::endl;
            throw err;
        }
        return lib;
    }

}
