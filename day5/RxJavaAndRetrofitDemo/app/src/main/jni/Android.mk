LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE = := testso
LOCAL_SRC_FILES += libtestso.so
incldue $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jni-test
LOCAL_SRC_FILES := testso.cpp
LOCAL_STATIC_LIBRARIES := testso

LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY);