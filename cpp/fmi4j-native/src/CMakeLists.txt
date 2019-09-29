
file(GLOB public_headers "${CMAKE_SOURCE_DIR}/include/*.h|*.hpp")
file(GLOB private_headers "${CMAKE_CURRENT_SOURCE_DIR}/fmi4j/*.hpp")
file(GLOB sources
        "${CMAKE_CURRENT_SOURCE_DIR}/*.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/fmi4j/*.cpp"
        )

set(publicHeadersFull)
foreach (h IN LISTS publicHeaders)
  list(APPEND publicHeadersFull "${publicHeaderDir}/${h}")
endforeach ()
set(privateHeadersFull)
foreach (h IN LISTS privateHeaders)
  list(APPEND privateHeadersFull "${privateHeaderDir}/${h}")
endforeach ()
set(allSources ${publicHeadersFull} ${privateHeadersFull} ${sources})

add_library(fmi4j-export ${allSources})
target_compile_features(fmi4j-export PUBLIC "cxx_std_17")
target_include_directories(fmi4j-export
        PUBLIC
        "$<BUILD_INTERFACE:${CMAKE_SOURCE_DIR}/include>"
        PRIVATE
        "${JNI_INCLUDE_DIRS}"
        "${CMAKE_CURRENT_SOURCE_DIR}"
        )

target_link_libraries(fmi4j-export
        PRIVATE
        "${JNI_LIBRARIES}"
        )
if (WIN32)

  if (CMAKE_SIZEOF_VOID_P EQUAL 4)
    SET(PLATFORM win32)
  elseif (CMAKE_SIZEOF_VOID_P EQUAL 8)
    SET(PLATFORM win64)
  else ()
    message(FATAL_ERROR "Unknown platform..")
  endif ()

  set_target_properties(fmi4j-export
          PROPERTIES
          RUNTIME_OUTPUT_DIRECTORY_RELEASE "${CMAKE_SOURCE_DIR}/../../java/fmi4j/fmu-builder/src/main/resources/binaries/${PLATFORM}"
          )
elseif (UNIX)
  set_target_properties(fmi4j-export
          PROPERTIES
          LIBRARY_OUTPUT_DIRECTORY "${CMAKE_SOURCE_DIR}/../../java/fmi4j/fmu-builder/src/main/resources/binaries/linux64"
          )
endif ()
