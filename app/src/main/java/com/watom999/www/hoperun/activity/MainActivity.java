package com.watom999.www.hoperun.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.data.MySQLiteHelper;
import com.watom999.www.hoperun.entity.UserInfoEntity;
import com.watom999.www.hoperun.fragment.Fragment00;
import com.watom999.www.hoperun.fragment.Fragment01;
import com.watom999.www.hoperun.fragment.Fragment02;
import com.watom999.www.hoperun.fragment.Fragment03;
import com.watom999.www.hoperun.fragment.Fragment04;
import com.watom999.www.hoperun.netty.WebVisitActivity;
import com.watom999.www.hoperun.utils.DisposeData;
import com.watom999.www.hoperun.utils.Logout;
import com.watom999.www.hoperun.utils.MyToast;
import com.watom999.www.hoperun.utils.WebViewTool;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationBar.OnTabSelectedListener {
    WebView webView01;
    WebSettings webSettings;
    MySQLiteHelper mySQLiteHelper;
    Map<String, Object> userLoginInfo;
    String page_type = "0";//0:主页面 1：ERP系统 2：邮箱 3：云之家
    private Context context;
    private FloatingActionButton fab;
    private Toast myToast;
    private UserInfoEntity userInfoEntity;
    private long insert;
    private PackageManager packageManager;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationBar bottomNavigationBar;
    Fragment currentFragment;
    Fragment00 fragment00;
    Fragment01 fragment01;
    Fragment02 fragment02;
    Fragment03 fragment03;
    Fragment04 fragment04;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //保存数据
                    if (DisposeData.getInstace().isURLChange(webView01.getUrl(), String.valueOf(msg.arg1))) {
                        insert = mySQLiteHelper.insert(userInfoEntity);
                        if (insert == -1) {
                            MyToast.showToast(MainActivity.this, "保存数据失败");
                        } else {
                            MyToast.showToast(MainActivity.this, "保存数据成功");
                        }
                    } else {
                        MyToast.showToast(MainActivity.this, "密码或账号不对，请重新登录");
                    }

                    break;
                case 2:
                    //直接登录
                    if (DisposeData.getInstace().isURLChange(webView01.getUrl(), String.valueOf(msg.arg1))) {
                        fab.hide();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        findView();
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否是第一次登陆，如果是第一次登陆，弹出Dialog，如果不是弹出登陆账号列表
                switch (currentFragment.getTag()) {
                    case "fragment00":
                        //是云之家网页页面登录
                        loginWay("云之家", "3");
                        break;
                    case "fragment01":
                        //是邮箱页面登录
                        loginWay("公司邮箱", "2");
                        break;
                    case "fragment02":
                        //登录考勤
                        loginWay("考勤", "0");
                        break;
                    case "fragment03":

                        break;
                    case "fragment04":
                        //是EHR系统的登录
                        loginWay("人事系统", "1");
                        break;
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //------------------------------------
        //initWebView("http://elec.hoperun.com:8106/eoffice_pc/");//在线地址
//        new WebViewTool(this).initWebView(webView01, "file:///android_asset/kaoqin01.html");//本地地址:
        page_type = "0";
        //------------------------------------
        TextView timeView = (TextView) findViewById(R.id.time);
        //Date date = DateUtil.getDate(new Date());
        //String myDate = "";
        //if (!(date == null)) {
        //    myDate= DateUtil.format(date);
        //}
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        initFragment();
//        webView01 = findViewById(R.id.where_webview);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .setActiveColor(R.color.newcolor_shenhui01)
                .setInActiveColor(R.color.newcolor_shenlv)
                .setBarBackgroundColor("#3F51B5");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.fx_none2x, "云家"))
                .addItem(new BottomNavigationItem(R.mipmap.yd_none2x, "邮箱"))
                .addItem(new BottomNavigationItem(R.mipmap.sy_none2x, "考勤"))
                .addItem(new BottomNavigationItem(R.mipmap.wd_none2x, "钉钉"))
                .addItem(new BottomNavigationItem(R.mipmap.kf_none2x, "EHR"))
                .setFirstSelectedPosition(2)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragment() {
        fragment00 = new Fragment00();
        fragment01 = new Fragment01();
        fragment02 = new Fragment02();
        fragment03 = new Fragment03();
        fragment04 = new Fragment04();
        currentFragment = fragment02;
        getFragmentManager().beginTransaction().add(R.id.frame_layout, currentFragment, "fragment02").commit();
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                switchFragment(fragment00);
                break;
            case 1:
                switchFragment(fragment01);
                break;
            case 2:
                switchFragment(fragment02);
                break;
            case 3:
                switchFragment(fragment03);
                break;
            case 4:
                switchFragment(fragment04);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (currentFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getFragmentManager().beginTransaction().hide(currentFragment)
                        .add(R.id.frame_layout, fragment, fragment.getClass().getSimpleName()).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        }
    }

    @Override
    protected void onDestroy() {
        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        //rootLayout.removeView(webView);
        webView01.destroy();
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_0) {
            return true;
        } else if (id == R.id.action_settings_1) {
            startActivity(new Intent(this, UserInfoQuery.class));
        } else if (id == R.id.action_settings_2) {
            startActivity(new Intent(this, WebVisitActivity.class));

        } else if (id == R.id.action_settings_3) {

        } else if (id == R.id.action_settings_4) {

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_ehr) {
            new WebViewTool(this).initWebView(webView01, "http://ehr.hoperun.com:8018/HRIS/Portal/Home.do#nv_1");
            fab.hide();
//            new WebViewTool(this).initWebView(webView01,"file:///android_asset/系统登录.htm");
            page_type = "1";
        } else if (id == R.id.nav_gallery) {
            new WebViewTool(this).initWebView(webView01, "http://mail.hoperun.com/");
            fab.hide();
//            new WebViewTool(this).initWebView(webView01,"file:///android_asset/263企业邮箱-登录入口.htm");
            page_type = "2";
        } else if (id == R.id.nav_slideshow) {
//            new WebViewTool(this).initWebView(webView01,"https://www.yunzhijia.com/home/?m=open&a=login");
//            new WebViewTool(this).initWebView(webView01,"file:///android_asset/金蝶云之家帐号在线登录-云之家官网.htm");
            new WebViewTool(this).openLocalApp("com.kdweibo.client", "云之家");
            page_type = "3";
        } else if (id == R.id.nav_bigevent) {
            new WebViewTool(this).openLocalApp("com.alibaba.android.rimet", "钉钉");  //打开钉钉
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, TimingLaunchActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(this, ShareAppActivity.class));
        } else if (id == R.id.nav_relate) {
            startActivity(new Intent(this, AboutUSActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loginWay(String whichpage, String pageFlag) {
        userLoginInfo = DisposeData.getInstace().getUserLoginInfo(pageFlag);
        String account = (String) userLoginInfo.get("login_account");
        String password = (String) userLoginInfo.get("login_password");
        if (account == null && password == null) {
            //第一次登陆/重新登录，需要输入登陆账号，记住账号。
            dialogFirstLogin(whichpage, pageFlag);
        } else {
            //非第一次/非重新登陆，判断是登录的是哪个页面，根据页面提取账号密码，直接登录。
            login(currentFragment, account, password);
        }
    }

    /**
     * 首次登陆和添加新的联系人时弹出该对话框
     */
    private void dialogFirstLogin(final String whichPage, final String pageFlag) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.alert_dialog_2editview, null);
        customizeDialog.setTitle(whichPage);
        customizeDialog.setIcon(R.mipmap.ic_launcher);
        customizeDialog.setMessage("      登陆成功后会保存您的" + whichPage + "登录信息！");
        customizeDialog.setView(dialogView);
        //获取EditView中的输入内容
        final EditText accountEditText = dialogView.findViewById(R.id.input_id);
        final EditText passwordEditText = dialogView.findViewById(R.id.input_password);
        customizeDialog.setPositiveButton("登陆",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //登陆并保存密码
                        String account = accountEditText.getText().toString().trim();
                        String password = passwordEditText.getText().toString().trim();
                        login(fragment02, account, password);
//                        saveLoginInfo(pageFlag, account, password);
                    }
                });
        customizeDialog.show();
    }


    private void login(Fragment currentFragment, String account, String password) {
        switch (currentFragment.getTag()) {
            case "fragment00":

                break;
            case "fragment01":

                break;
            case "fragment02":
                fragment02.login(account, password);
                break;
            case "fragment03":

                break;
            case "fragment04":

                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {}
        String currentUrl = webView01.getUrl();
        Boolean equalStr = !DisposeData.getInstace().isEqualStr(currentUrl, "file:///android_asset/kaoqin01.html");
        if (currentUrl != null && equalStr) {
            new WebViewTool(this).initWebView(webView01, "file:///android_asset/kaoqin01.html");//本地考勤地址
            fab.show();
            return false;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
