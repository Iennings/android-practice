package com.example.ienning.ipcways.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ienning on 16-11-7.
 */

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private Context context;
    private IBinderPool binderPool;
    private static volatile BinderPool instance;
    private CountDownLatch connectBinderPoolCountDownLatch;

    private BinderPool(Context context) {
        this.context = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    private synchronized void connectBinderPoolService() {
        connectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(context, BinderPoolService.class);
        context.bindService(service, binderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            connectBinderPoolCountDownLatch.await();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * query binder by binderCode from binder pool
     *
     * @param binderCode
     *             the unique token of binder
     * @return binder who's token is binderCode<br>
     *     return null when not found or BinderPoolService died.
     */
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (binderPool != null) {
                binder  = binderPool.queryBinder(binderCode);
            }
        } catch(RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
    private ServiceConnection binderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //这个service是BinderPoolService中onBind()返回的bindPool
            binderPool = IBinderPool.Stub.asInterface(service);
            try {
                binderPool.asBinder().linkToDeath(binderPoolDeathRecipient, 0);
            } catch(RemoteException e) {
                e.printStackTrace();
            }
            connectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private IBinder.DeathRecipient binderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(TAG, "binderDied: ");
            binderPool.asBinder().unlinkToDeath(binderPoolDeathRecipient, 0);
            binderPool = null;
            connectBinderPoolService();
        }
    };
    //创建BinderPoollmpl静态类继承IBinderPool.Stub，让客户端进行匹配，这个就是相当一个Binder池，
    public static class BinderPoollmpl extends IBinderPool.Stub {
        public BinderPoollmpl() {
            super();
        }
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER: {
                    binder = new SecurityCenterlmpl();
                    break;
                }
                case BINDER_COMPUTE: {
                    binder = new Computelmpl();
                    break;
                }
                default:
                    break;
            }
            return binder;
        }
    }
}
