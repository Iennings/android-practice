package com.example.ienning.ipcways.aidl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ienning.ipcways.R;

import java.util.List;

/**
 * Created by ienning on 16-11-5.
 */

public class BookManagerActivity extends Activity {
    private static final String TAG = "Ienning";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager remoteBookManager;

    @SuppressLint("HandlerLead")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "handleMessage: receive new book " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(TAG, "binderDied: tname " + Thread.currentThread().getName());
            if (remoteBookManager == null) {
                return;
            }
            remoteBookManager.asBinder().unlinkToDeath(deathRecipient, 0);
            remoteBookManager = null;
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            remoteBookManager = bookManager;
            try {
                remoteBookManager.asBinder().linkToDeath(deathRecipient, 0);
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "query book list, list type " + list.getClass().getCanonicalName());
                Log.i(TAG, "onServiceConnected: query book list:" + list.toString());
                Book newBook = new Book(3, "Android进阶");
                bookManager.addBook(newBook);
                Log.i(TAG, "onServiceConnected: add book " + newBook);
                List<Book> newList = bookManager.getBookList();
                Log.i(TAG, "onServiceConnected: query book list " + newList.toString());
                bookManager.registerListener(onNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteBookManager = null;
            Log.i(TAG, "onServiceDisconnected: . tname " + Thread.currentThread().getName());
        }
    };
    private IOnNewBookArrivedListener onNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void onButtonClick(View view) {
        Toast.makeText(this, "Click button1", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (remoteBookManager != null) {
                    try {
                        List<Book> newList = remoteBookManager.getBookList();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    protected void onDestroy() {
        if (remoteBookManager != null && remoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "unregister listener " + onNewBookArrivedListener);
                remoteBookManager.unregisterListener(onNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }
}
