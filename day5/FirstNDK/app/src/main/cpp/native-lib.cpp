#include <jni.h>
#include <stdio.h>
#include <string>

extern "C"
/*
jstring
Java_com_example_ienning_firstndk_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject  this ) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
*/
{
    void callJavaMethod(JNIEnv *env, jobject thiz) {
        jclass clazz = env->FindClass("com/example/ienning/firstndk/MainActivity");
        if (clazz == NULL) {
            printf("find class MainActivity error!");
            return;
        }
        jmethodID id = env->GetStaticMethodID(clazz, "methodCalledByJni", "(Ljava/lang/String;)V");
        if (id == NULL) {
            printf("find method methodCalledByJni error!");
        }
        jstring msg = env->NewStringUTF("msg send by callJavaMethod in native-lib.cpp.");
        env->CallStaticVoidMethod(clazz, id, msg);
    }

    jstring Java_com_example_ienning_firstndk_MainActivity_get(JNIEnv *env, jobject thiz) {
        printf("invoke get in c++\n");
        callJavaMethod(env, thiz);
        return env->NewStringUTF("Hello from JNI in libjni-native-lib.so!");
    }

    void Java_com_example_ienning_firstndk_MainActivity_set(JNIEnv *env, jobject thiz, jstring string) {
        printf("invoke set from c++\n");
        char *str = (char *) env->GetStringUTFChars(string, NULL);
        printf("%s\n", str);
        env->ReleaseStringUTFChars(string, str);
    }
}