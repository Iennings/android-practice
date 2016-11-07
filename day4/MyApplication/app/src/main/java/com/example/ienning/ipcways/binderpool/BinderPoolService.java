package com.example.ienning.ipcways.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by ienning on 16-11-7.
 */

public class BinderPoolService extends Service {
    private static final String TAG = "Ienning";
    private Binder binderPool = new BinderPool.BinderPoollmpl();
    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
