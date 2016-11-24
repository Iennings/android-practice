package com.example.ienning.android_art.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ienning.android_art.R;
import com.example.ienning.android_art.ui.HorizontalScrollViewEx2;
import com.example.ienning.android_art.ui.ListViewEx;

import java.util.ArrayList;

/**
 * Created by ienning on 16-11-23.
 */

public class DemoActivity_2 extends Activity {
    private static final String TAG = "Ienning";

    private HorizontalScrollViewEx2 mListContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_2);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        mListContainer = (HorizontalScrollViewEx2) findViewById(R.id.container);

        final int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        final int screenHeight = MyUtils.getScreenMetrics(this).heightPixels;

        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.content_layout2, mListContainer, false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = (TextView) layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            mListContainer.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListViewEx listViewEx = (ListViewEx) layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.content_list_item, R.id.name, datas);
        listViewEx.setAdapter(adapter);
        listViewEx.setHorizontalScrollViewEx2(mListContainer);
        listViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DemoActivity_2.this, "click item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent: " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: action: " + event.getAction());
        return super.onTouchEvent(event);
    }
}
