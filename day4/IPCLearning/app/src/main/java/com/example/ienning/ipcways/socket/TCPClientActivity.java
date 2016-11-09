package com.example.ienning.ipcways.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ienning.ipcways.R;
import com.example.ienning.ipcways.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ienning on 16-11-5.
 */

public class TCPClientActivity extends Activity implements View.OnClickListener {
    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private PrintWriter printWriter;
    private Socket clientSocket;
    private Handler handlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG: {
                    messageTextView.setText(messageTextView.getText() + (String) msg.obj);
                    break;
                }
                case MESSAGE_SOCKET_CONNECTED: {
                    sendButton.setEnabled(true);
                    break;
                }
                default:
                    break;
            }
        }
    };
    private TextView messageTextView;
    private Button sendButton;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        messageTextView = (TextView) findViewById(R.id.msg_container);
        sendButton = (Button) findViewById(R.id.send);
        messageEditText = (EditText) findViewById(R.id.msg);
        sendButton.setOnClickListener(this);
        Intent service = new Intent(this, TCPServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        if(clientSocket != null) {
            try {
                clientSocket.shutdownInput();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
    @Override
    public void onClick(View view) {
        if (view == sendButton) {
            final String msg = messageEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && printWriter != null) {
                //msg
                printWriter.println(msg);
                messageEditText.setText("");
                String time = formatDateTime(System.currentTimeMillis());
                final String showedMsg = "self " + time + ":" + msg + "\n";
                messageTextView.setText(messageTextView.getText() + showedMsg);
            }
        }
    }
    @SuppressWarnings("SimpleDateFormat")
    private String formatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss").format(new Date(time));
    }
    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                clientSocket = socket;
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                handlder.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed, retry...");
            }
        }
        try {
            // 接收服务器消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                System.out.println("receive :"+ msg);
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showMsg = "server " + time + ":" + msg + "\n";
                    //发送消息给handler
                    handlder.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showMsg).sendToTarget();
                }
            }
            System.out.println("quit...");
            MyUtils.close(printWriter);
            MyUtils.close(br);
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
