package com.watom999.www.hoperun.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.watom999.www.hoperun.ETitleBar;
import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.data.DataBaseHelp;
import com.watom999.www.hoperun.data.MySQLiteHelper;

import java.util.HashMap;
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

public class UserInfoQuery extends AppCompatActivity implements View.OnClickListener {
    private ETitleBar titlebar;
    private TextView account01;
    private TextView password01;
    private LinearLayout erpLl;
    private TextView account02;
    private TextView password02;
    private LinearLayout emailLl;
    private TextView account03;
    private TextView password03;
    private LinearLayout yunzhijiaLl;
    private TextView account04;
    private TextView password04;
    private Button btnCallDetaile;
    private MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();  //去掉标题栏
        titlebar.setTitle("登录信息");
        titlebar.setTitleRightIconVisible(true);
        setContentView(R.layout.userinfo_query_layout);
        mySQLiteHelper = new MySQLiteHelper(this);
        findViews();
    }

    /**
     * 从数据库中获取登录的账号密码
     */
    public Map<String, Object> getUserLoginInfo(String pageid) {
        HashMap<String, Object> userLoginInfo = mySQLiteHelper.getUserLoginInfo(pageid);
        return userLoginInfo;
    }
        private void findViews() {
            titlebar = (ETitleBar)findViewById( R.id.titlebar );
            account01 = (TextView)findViewById( R.id.account01 );
            password01 = (TextView)findViewById( R.id.password01 );
            erpLl = (LinearLayout)findViewById( R.id.erp_ll );
            account02 = (TextView)findViewById( R.id.account02 );
            password02 = (TextView)findViewById( R.id.password02 );
            emailLl = (LinearLayout)findViewById( R.id.email_ll );
            account03 = (TextView)findViewById( R.id.account03 );
            password03 = (TextView)findViewById( R.id.password03 );
            yunzhijiaLl = (LinearLayout)findViewById( R.id.yunzhijia_ll );
            account04 = (TextView)findViewById( R.id.account04 );
            password04 = (TextView)findViewById( R.id.password04 );
            btnCallDetaile = (Button)findViewById( R.id.btn_call_detaile );

            btnCallDetaile.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            if ( v == btnCallDetaile ) {
            //修改账号密码
//            mySQLiteHelper.alter(pageid, account, password);
            }
        }


    private void insert() {
//        DataBaseHelp dataBaseHelp = new DataBaseHelp(UserInfoQuery.this);
//        dataBaseHelp.insert("person", new String[]{"name", "accout"}, new String[]{"zhangsan", "jaj"});
    }

    private void query() {
    }
}
