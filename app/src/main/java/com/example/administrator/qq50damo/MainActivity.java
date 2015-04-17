package com.example.administrator.qq50damo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.io.BufferedReader;


public class MainActivity extends Activity {

    private MyHrizontal myHrizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHrizontal = (MyHrizontal) findViewById(R.id.id_mymenu);


    }

    public void tog(View view) {
        myHrizontal.tog();
    }


}
