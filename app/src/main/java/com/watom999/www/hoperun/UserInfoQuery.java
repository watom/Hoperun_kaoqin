package com.watom999.www.hoperun;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.watom999.www.hoperun.data.DataBaseHelp;

import java.util.Map;

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
//                insert();
//                query();
            }
        });
    }

    private void insert() {
//        DataBaseHelp dataBaseHelp = new DataBaseHelp(UserInfoQuery.this);
//        dataBaseHelp.insert("person", new String[]{"name", "accout"}, new String[]{"zhangsan", "jaj"});
    }

    private void query() {
//        DataBaseHelp dataBaseHelp = new DataBaseHelp(UserInfoQuery.this);
//        Map map = dataBaseHelp.queryItemMap("select * from person", new String[]{"name", "accout"});
////        //查询获得游标
////        Cursor cursor = db.query("person", null, null, null, null, null, null);//第一种方式
//
//        Cursor cursor = db.rawQuery("select * from person", null);//第二种方式
//
//        DataBaseHelp dataBaseHelp = new DataBaseHelp(UserInfoQuery.this);
//        //判断游标是否为空
//        Toast.makeText(this,"nihao ",Toast.LENGTH_LONG).show();
//        if (cursor.moveToFirst()) {
//            //遍历游标
//            for (int i = 0; i < cursor.getCount(); i++) {
//                cursor.move(i);
//                int id = cursor.getInt(0);
//                String username = cursor.getString(1);
//                String password = cursor.getString(2);
//                Toast.makeText(this,"id="+id+", username="+username+", password="+password,Toast.LENGTH_LONG).show();
//            }
//        }
//        cursor.close();
//        db.close();
    }
}
