LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := jni-test
LOCAL_SRC_FILES := testso.cpp
include $(PREBUILD_SHARED_LIBRARY)