cmake_minimum_required(VERSION 3.4.1)
#cal-lib.cpp   默认路径，如：src/main/cpp/cal-lib.cpp
add_library(cal-lib SHARED cal-lib.cpp)
target_link_libraries(cal-lib)
