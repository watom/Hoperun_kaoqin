package com.watom999.www.hoperun.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.watom999.www.hoperun.R;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class AboutUSActivity extends AppCompatActivity {
    private TextView aboutus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_layout);
        aboutus = findViewById(R.id.tv_aboutus);
        fanction();
    }

    private void fanction(){
        //设置文本的超链接功能
        SpannableString spannableString = new SpannableString("有问题给我说一下，可以发邮件，谢谢支持。");
        spannableString.setSpan(new URLSpan("mailto:1164973719@qq.com"),11,14,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        aboutus.append(spannableString);
        aboutus.setMovementMethod(LinkMovementMethod.getInstance());  // 重要：设置文字为可点击状态
    }
}
