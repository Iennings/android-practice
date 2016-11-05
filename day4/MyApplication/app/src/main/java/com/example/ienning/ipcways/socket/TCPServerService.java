package com.example.ienning.ipcways.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.ienning.ipcways.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by ienning on 16-11-5.
 */

public class TCPServerService extends Service {
    private boolean isServiceDestroyed = false;

    private String[] definedMessages = new String[] {
            "hello",
            "what's you name?",
            "let's play something",
            "do you konw? I can chat with many people at the same time"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        isServiceDestroyed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {
        @SuppressWarnings("resource")
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                System.err.println("establish tcp server failed, port 8688");
                e.printStackTrace();
                return;
            }
            while (!isServiceDestroyed) {
                try {
                    // 接收客户端请求
                    final Socket client = serverSocket.accept();
                    System.out.println("accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch(IOException e) {
                                e.printStackTrace();
                            }
                        };
                    }.start();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void responseClient(Socket client) throws IOException {
        // 用于接受客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!isServiceDestroyed) {
            String str = in.readLine();
            System.out.println("msg from client:" + str);
            if(str == null) {
                break;
            }
            int i = new Random().nextInt(definedMessages.length);
            String msg = definedMessages[i];
            out.println("send: " + msg);
        }
        System.out.println("client quit.");
        //关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }
}
