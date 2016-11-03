package com.example.ienning.ipcways;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ienning.ipcways.messenger.MessengerActivity;
import com.example.ienning.ipcways.model.User;
import com.example.ienning.ipcways.utils.MyConstants;
import com.example.ienning.ipcways.utils.MyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by ienning on 16-11-2.
 */

public class SecondActivity extends Activity implements View.OnClickListener{
    private Button toThird;
    private Button toMessenger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        toMessenger = (Button) findViewById(R.id.to_messenge);
        toThird = (Button) findViewById(R.id.to_thrid);
        toMessenger.setOnClickListener(this);
        toThird.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_messenge:
                Intent intent = new Intent(SecondActivity.this, MessengerActivity.class);
                startActivity(intent);
                break;
            case R.id.to_thrid:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        User user = (User) getIntent().getSerializableExtra("extra_user");
        Log.i("Ienning", "onResume: user " + user);
        recoverFromFile();
    }
    private void recoverFromFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = null;
                File cacheFile = new File(MyConstants.CACHE_FILE_PATH);
                if (cacheFile.exists()) {
                    ObjectInputStream objectInputStream = null;
                    try {
                        objectInputStream = new ObjectInputStream(new FileInputStream(cacheFile));
                        user = (User) objectInputStream.readObject();
                        Log.i("Ienning", "recoverFromFile: user is " + user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        MyUtils.close(objectInputStream);
                    }
                }
            }
        }).start();
    }
}
