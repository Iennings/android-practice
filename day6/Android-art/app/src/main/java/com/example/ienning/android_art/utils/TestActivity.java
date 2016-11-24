package com.example.ienning.android_art.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ienning.android_art.R;

/**
 * Created by ienning on 16-11-23.
 */

public class TestActivity extends Activity implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "Iennning TestActivity";

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FNAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;

    private Button mButton1;
    private View mButton2;

    private int mCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handlerMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO: {
                    mCount++;
                    if (mCount <= FNAME_COUNT) {
                        float fraction = mCount / (float) FNAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        mButton1.scrollTo(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }
    private void initView() {
        mButton1 = (Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(this);
        mButton2 = (View) findViewById(R.id.button2);
        mButton2.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(TAG, "onWindowFocusChanged: button1.left = " + mButton1.getLeft());
            Log.d(TAG, "onWindowFocusChanged: button1.x=" + mButton1.getX());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mButton1) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: aot");
        super.onStart();
    }
}
