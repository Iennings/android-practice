LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := stlport_share
LOCAL_STATIC_LIBRARIES := stlport_static
LOCAL_SRC_FILES := stlportShare.cpp
include $(BUILD_SHARED_LIBRARY)