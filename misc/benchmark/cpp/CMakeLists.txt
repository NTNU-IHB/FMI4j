cmake_minimum_required(VERSION 3.10)
project(cpp)

set(CMAKE_CXX_STANDARD 17)
list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake")

find_package(FMILIB REQUIRED)
include_directories(${FMILIB_INCLUDE_DIRS})

add_executable(cpp main.cpp FmuWrapper.cpp FmuWrapper.h FmiHelper.h)
target_link_libraries(cpp ${FMILIB_LIBRARIES})

if (UNIX)
    find_package(Boost 1.65.1 COMPONENTS filesystem system)
    if (${Boost_FOUND})
        include_directories(${Boost_INCLUDE_DIRS})
        target_link_libraries(cpp ${Boost_FILESYSTEM_LIBRARY} ${Boost_SYSTEM_LIBRARY})
    endif()
endif()
