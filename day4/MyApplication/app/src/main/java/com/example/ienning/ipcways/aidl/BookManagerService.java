package com.example.ienning.ipcways.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ienning on 16-11-5.
 */

public class BookManagerService extends Service {
    private static final String TAG = "Ienning";

    private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<Book>();

    //private RemoteCallbackList<IOn>
    private RemoteCallbackList<IOnNewBookArrivedListener> listenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();
    private Binder binder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
            return bookList;
        }
        @Override
        public void addBook(Book book) throws  RemoteException {
            bookList.add(book);
        }
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
            int check = checkCallingOrSelfPermission("com.example.ienning.ipcways.permission.ACCESS_BOOK_SERVICE");
            Log.i(TAG, "onTransact: check = " + check);
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String packageName = null;

            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            Log.i(TAG, "onTransact: packageName " + packageName);
            if (!packageName.startsWith("com.example")) {
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException{
            listenerList.register(listener);
            final int N = listenerList.beginBroadcast();
            listenerList.finishBroadcast();
            Log.i(TAG, "registerListener: , current size: " + N);
        }
        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException{
            boolean success = listenerList.unregister(listener);
            if (success) {
                Log.i(TAG, "unregister success.");
            }
            else {
                Log.i(TAG, "not found, can not unregister.");
            }
            final int N = listenerList.beginBroadcast();
            listenerList.finishBroadcast();
            Log.i(TAG, "unregisterListener: , current size:" + N);
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.example.ienning.ipcways.permission.ACCESS_BOOK_SERVICE");
        Log.i(TAG, "onBind: check = " + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book(1, "Android"));
        bookList.add(new Book(2, "iOS"));
        new Thread(new ServiceWorker()).start();
    }
    @Override
    public void onDestroy() {
        isServiceDestroyed.set(true);
        super.onDestroy();
    }
    //用于通知每个订阅者更新了新书
    private void onNewBookArrived(Book book) throws RemoteException {
        bookList.add(book);
        final int N = listenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = listenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        listenerList.finishBroadcast();
    }
    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!isServiceDestroyed.get()) {
                try {
                    Thread.sleep(50000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = bookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try{
                    onNewBookArrived(newBook);
                } catch(RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
