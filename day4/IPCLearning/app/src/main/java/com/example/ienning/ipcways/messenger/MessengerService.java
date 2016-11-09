package com.example.ienning.ipcways.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.ienning.ipcways.utils.MyConstants;

/**
 * Created by ienning on 16-11-3.
 */

public class MessengerService extends Service {
    private static final String TAG = "Ienning";
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG, "handleMessage: receive msg from Client " + msg.getData().getString("msg"));
                    /* msg.replyTo 是来自客户端的getReplyMessenger
                    //接受Message步骤
                    1.创建Messenger对象，使用的msg的变量
                    2.
                    */
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "I have received you message");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch(RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
