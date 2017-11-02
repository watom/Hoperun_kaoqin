package com.watom999.www.hoperun;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.watom999.www.hoperun.data.DBOpenHelper;

/***********************************************
 *@Copyright: 2017(C), 国电通__期
 *@Author&Email: wanghaitao 1164973719@qq.com
 *@FileName: com.watom999.www.hoperun
 *@Function: 1、
 *@Description: 1、
 *@CreatedDate: 2017/9/24 19:24
 *@UpDate: 1、
 ***********************************************/

public class UserInfoQuery extends AppCompatActivity {
    SQLiteDatabase writableDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();  //去掉标题栏
        setContentView(R.layout.userinfo_query_layout);
        Button button = (Button) findViewById(R.id.btn_call_detaile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOpenHelper wode = new DBOpenHelper(UserInfoQuery.this);
                writableDatabase = wode.getWritableDatabase();
                insert(writableDatabase);
                query(writableDatabase);
            }
        });
    }

    private void insert(SQLiteDatabase db) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加用户名
        cValue.put("sname", "xiaoming");
        //添加密码
        cValue.put("snumber", "01005");
        //调用insert()方法插入数据
        db.insert("stu_table", null, cValue);
    }

    private void query(SQLiteDatabase db) {
        //查询获得游标
        Cursor cursor = db.query("stu_table", null, null, null, null, null, null);
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.move(i);
                int id = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                //输出用户信息
                System.out.println(id + ":" + username + ":" + password);
            }
        }
    }
}
