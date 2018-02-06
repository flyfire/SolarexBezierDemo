package com.solarexsoft.solarexbezierdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.solarexsoft.solarexbezier.Bezier1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bezier1 view = new Bezier1(this);
        setContentView(view);
    }
}
