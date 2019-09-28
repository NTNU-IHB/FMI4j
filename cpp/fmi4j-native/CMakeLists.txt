cmake_minimum_required(VERSION 3.10)
project(fmi4j-native)

# ==============================================================================
# Build settings
# ==============================================================================

set(BUILD_SHARED_LIBS ON)

# ==============================================================================
# Global internal configuration
# ==============================================================================

# Automatically export all symbols in Windows DLLs.
set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)

# ==============================================================================
# Dependencies
# ==============================================================================
find_package(JNI REQUIRED)

add_subdirectory(src)
add_subdirectory(test)
