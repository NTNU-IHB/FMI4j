cmake_minimum_required(VERSION 3.12)


SET(VCPKG_ROOT $ENV{VCPKG_ROOT} CACHE STRING "VCPKG_ROOT")
if(NOT DEFINED VCPKG_ROOT)
    message(FATAL_ERROR "VCPKG_ROOT is not set!")
endif()

if(NOT DEFINED CMAKE_TOOLCHAIN_FILE)
    set(CMAKE_TOOLCHAIN_FILE "${VCPKG_ROOT}/scripts/buildsystems/vcpkg.cmake"
            CACHE STRING "")
endif()


project(Benchmark)

set(CMAKE_CXX_STANDARD 17)
list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake")

find_package(FMILIB REQUIRED)
include_directories(${FMILIB_INCLUDE_DIRS})

add_executable(Benchmark main.cpp FmuWrapper.cpp FmuWrapper.h FmiHelper.h)
target_link_libraries(Benchmark ${FMILIB_LIBRARIES})

find_package(Boost COMPONENTS filesystem system REQUIRED)
include_directories(${Boost_INCLUDE_DIRS})
target_link_libraries(Benchmark Boost::filesystem Boost::system)
if (UNIX)
    target_link_libraries(Benchmark dl)
endif()

