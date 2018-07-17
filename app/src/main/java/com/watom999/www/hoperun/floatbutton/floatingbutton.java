package com.watom999.www.hoperun.floatbutton;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.watom999.www.hoperun.R;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public class floatingbutton extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floatbutton_layout);
        findView();
    }

    private void findView() {
        FloatingActionButton fb =  findViewById(R.id.fab_01);
        fb.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fea700")));
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
