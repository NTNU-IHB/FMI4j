
add_executable(test1 test1.cpp)
target_link_libraries(test1 PRIVATE ${JNI_LIBRARIES})
target_include_directories(test1 PRIVATE ${JNI_INCLUDE_DIRS})

add_executable(test2 test2.cpp)
target_link_libraries(test2 PRIVATE fmi4j-export ${JNI_LIBRARIES})
target_include_directories(test2 PRIVATE ${CMAKE_SOURCE_DIR}/src/fmi4j ${JNI_INCLUDE_DIRS})
