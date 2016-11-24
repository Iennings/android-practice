package com.example.ienning.android_art.utils;

import android.os.Environment;

/**
 * Created by ienning on 16-11-23.
 */

public class MyConstants {
    public static final String CHAPTER_2_PATH = Environment.getExternalStorageDirectory().getPath() + "/ienning/chapter2";

    public static final String CACHE_FILE_PATH = CHAPTER_2_PATH + "usercache";

    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;
}
