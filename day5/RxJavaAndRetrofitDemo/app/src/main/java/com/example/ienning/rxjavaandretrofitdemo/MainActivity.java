package com.example.ienning.rxjavaandretrofitdemo;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello = (TextView) findViewById(R.id.hello);
        Log.e("Ienning", "onCreate: this is failed to load item");
        Log.e("Ienning", "onmain: " + Testso.main());
        hello.setText(Testso.main());
    }
}
