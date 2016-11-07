package com.example.ienning.ipcways.binderpool;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.ienning.ipcways.R;

/**
 * Created by ienning on 16-11-7.
 */

public class BinderPoolActivity extends Activity {
    private static final String TAG = "Ienning";

    private ISecurityCenter securityCenter;
    private ICompute iCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        securityCenter = (ISecurityCenter) SecurityCenterlmpl.asInterface(securityBinder);
        Log.i(TAG, "doWork: visit IsecurityCenter");
        String msg = "helloworld-android";
        System.out.println("content:" + msg);
        try {
            String password = securityCenter.encrypt(msg);
            System.out.println("encry:" + password);
            System.out.println("decrypt:" + securityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doWork: visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        iCompute = (ICompute) Computelmpl.asInterface(computeBinder);
        try {
            System.out.println("6+4=" + iCompute.add(6, 4));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
