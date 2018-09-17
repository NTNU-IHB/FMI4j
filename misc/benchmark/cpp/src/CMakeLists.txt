
include_directories(../include)

add_library(fmi
        fmi/FmuWrapper.cpp
        ../include/fmi/FmiHelper.hpp
        ../include/fmi/FmuWrapper.hpp)
target_include_directories(fmi PUBLIC ${FMILIB_INCLUDE_DIRS})
target_link_libraries(fmi Boost::system Boost::filesystem ${FMILIB_LIBRARIES})
if (UNIX)
    target_link_libraries(fmi dl)
endif()

add_executable(TorsionBarTest
        benchmark/benchmark_util.hpp
        benchmark/TorsionBarTest.cpp)
target_link_libraries(TorsionBarTest fmi)

add_executable(ControlledTemperatureTest
        benchmark/benchmark_util.hpp
        benchmark/ControlledTemperatureTest.cpp)
target_link_libraries(ControlledTemperatureTest fmi)


