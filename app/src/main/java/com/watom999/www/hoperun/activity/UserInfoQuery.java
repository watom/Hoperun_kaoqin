package com.watom999.www.hoperun.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.watom999.www.hoperun.ETitleBar;
import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.data.DataBaseHelp;
import com.watom999.www.hoperun.data.MySQLiteHelper;
import com.watom999.www.hoperun.utils.MyToast;

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
    private EditText account01;
    private EditText password01;
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
    private String buttonText;
    private Map<String, Object> userLoginInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_query_layout);
        getSupportActionBar().hide();  //去掉标题栏
        findViews();
        mySQLiteHelper = new MySQLiteHelper(this);

        userLoginInfo = getUserLoginInfo("0");
        initView(userLoginInfo);
    }

    private void initView(Map<String, Object> userLoginInfo) {
        titlebar.setTitle("登录信息");
        titlebar.setTitleRightIconVisible(true);
        if (userLoginInfo != null && userLoginInfo.size() != 0) {
            account01.setText((String) userLoginInfo.get("login_account"));
            password01.setText((String) userLoginInfo.get("login_password"));
        } else {
            MyToast.showToast(this, "请先在主页登录");
        }
    }

    /**
     * 从数据库中获取登录的账号密码
     */
    public Map<String, Object> getUserLoginInfo(String pageid) {
        HashMap<String, Object> userLoginInfo = mySQLiteHelper.getUserLoginInfo(pageid);
        return userLoginInfo;
    }

    private void findViews() {
        titlebar = (ETitleBar) this.findViewById(R.id.titlebar);
        account01 = (EditText) this.findViewById(R.id.account01);
        password01 = (EditText) this.findViewById(R.id.password01);
        erpLl = (LinearLayout) this.findViewById(R.id.erp_ll);
        account02 = (TextView) this.findViewById(R.id.account02);
        password02 = (TextView) this.findViewById(R.id.password02);
        emailLl = (LinearLayout) this.findViewById(R.id.email_ll);
        account03 = (TextView) this.findViewById(R.id.account03);
        password03 = (TextView) this.findViewById(R.id.password03);
        yunzhijiaLl = (LinearLayout) this.findViewById(R.id.yunzhijia_ll);
        account04 = (TextView) this.findViewById(R.id.account04);
        password04 = (TextView) this.findViewById(R.id.password04);
        btnCallDetaile = (Button) this.findViewById(R.id.btn_call_detaile);
        buttonText = (String) btnCallDetaile.getText();
        btnCallDetaile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCallDetaile) {
            if (userLoginInfo != null && userLoginInfo.size() != 0) {
                switchText();
            } else {
                MyToast.showToast(this, "请先在主页登录");
            }
        }
    }

    private void switchText() {
        if (buttonText.equals("编辑")) {
            buttonText = "保存";
            btnCallDetaile.setText("保存");
            btnCallDetaile.setBackgroundResource(R.color.btn_gray);
            account01.setEnabled(true);
            password01.setEnabled(true);
        } else {
            buttonText = "编辑";
            btnCallDetaile.setText("编辑");
            btnCallDetaile.setBackgroundResource(R.color.blue);
            saveInfoData();
            account01.setEnabled(false);
            password01.setEnabled(false);
        }
    }

    private void saveInfoData() {
        String account = account01.getText().toString();
        String password = password01.getText().toString();
        mySQLiteHelper.alter("0", account, password);//可以修改账号密码
        MyToast.showToast(this, "修改完成");
    }
}
