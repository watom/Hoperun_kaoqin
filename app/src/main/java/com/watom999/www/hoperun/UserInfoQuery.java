package com.watom999.www.hoperun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/***********************************************
 *@Copyright: 2017(C), 国电通__期
 *@Author&Email: wanghaitao 1164973719@qq.com
 *@FileName: com.watom999.www.hoperun
 *@Function: 1、
 *@Description: 1、       
 *@CreatedDate: 2017/9/24 19:24
 *@UpDate: 1、
 ***********************************************/

public class UserInfoQuery extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();  //去掉标题栏
        setContentView(R.layout.userinfo_query_layout);
    }
}
