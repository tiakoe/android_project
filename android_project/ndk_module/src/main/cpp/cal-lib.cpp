#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_a_ndk_1module_CalUtil_getMsg(JNIEnv *env, jobject thiz) {
    std::string hello = "欢迎来到NDK";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_a_ndk_1module_CalUtil_getNumber(JNIEnv *env, jobject thiz) {
    return 2020;
}
