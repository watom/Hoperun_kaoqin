package com.watom999.www.hoperun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;
import android.widget.Toast;

import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.data.MySQLiteHelper;
import com.watom999.www.hoperun.entity.UserInfoEntity;
import com.watom999.www.hoperun.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //保存数据
                    if (isURLChange(webView01.getUrl(), String.valueOf(msg.arg1))) {
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
                    if (isURLChange(webView01.getUrl(), String.valueOf(msg.arg1))) {
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView01 = (WebView) findViewById(R.id.where_webview);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(page_type);//判断是否是第一次登陆，如果是第一次登陆，弹出Dialog，如果不是弹出登陆账号列表
                Snackbar.make(view, "登陆信息已填入，请点击表单上的登录按钮", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //------------------------------------
        //initWebView("http://elec.hoperun.com:8106/eoffice_pc/");//在线地址
        initWebView("file:///android_asset/kaoqin01.html");//本地地址:
        page_type = "0";
        //------------------------------------

        TextView timeView = (TextView) findViewById(R.id.time);
//        Date date = DateUtil.getDate(new Date());
//        String myDate = "";
//        if (!(date == null)) {
//            myDate= DateUtil.format(date);
//        }
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    /**
     * 安卓原生代码
     */
    private void initWebView(String url) {
        // 加载网页 H5,html,自定义浏览器，或者网页播放器
        // webView01 = new WebView(this);
        // 设置WebSettings支持javascript
        webSettings = webView01.getSettings();
        //在本地浏览器的页面里面有页面时，也会调用webview，不会调用手机浏览器
        webView01.setWebViewClient(new WebViewClient());
        //设置为可调用js方法
        webSettings.setJavaScriptEnabled(true);
        //不调用浏览器 而调用自己的内部的浏览器
        webView01.setWebViewClient(new WebViewClient() {
            /**
             * 当一个新的url即将加载到当前的WebView中时，给主机应用程序一个接管控件的机会。
             * @param view
             * @param request
             * @return
             */
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //当前的路径穿进去处理
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        //加载网络上的HTML文件
        webView01.loadUrl(url);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setUseWideViewPort(true);//扩大比例的缩放
        //将提供的Java对象注入到此WebView中。可以让JS通过android的这个字段调用这个java对象中的方法
        webView01.addJavascriptInterface(new androidLoginInterface(), "Android");

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         *  1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
         *  2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }


    class androidLoginInterface {
        @JavascriptInterface  //这里的JavascriptInterface注解很重要
        public void showToast() {
            Toast.makeText(MainActivity.this, "js调用安卓代码", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, UserInfoQuery.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
//        rootLayout.removeView(webView);
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
            initWebView("http://ehr.hoperun.com:8018/HRIS/Portal/Home.do#nv_1");
            fab.hide();
//            initWebView("file:///android_asset/系统登录.htm");
            page_type = "1";
        } else if (id == R.id.nav_gallery) {
            initWebView("http://mail.hoperun.com/");
            fab.hide();
//            initWebView("file:///android_asset/263企业邮箱-登录入口.htm");
            page_type = "2";
        } else if (id == R.id.nav_slideshow) {
            initWebView("https://www.yunzhijia.com/home/?m=open&a=login");
            fab.hide();
//            initWebView("file:///android_asset/金蝶云之家帐号在线登录-云之家官网.htm");
            page_type = "3";
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_bigevent) {
            startActivity(new Intent(this, LearnMaterialsQuery.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(this, ShareAppActivity.class));

        } else if (id == R.id.nav_relate) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
        MyToast.showToast(this, message);
    }

    /**
     * 登录入口http://elec.hoperun.com:8106/eoffice_pc/attendance/initPage
     */
    private void login(String pageFlag) {
        //第一次登陆，需要输入登陆账号，记住账号。
        switch (pageFlag) {
            case "0":
                //是主页面登录
                loginWay("考勤", "0");
                break;
            case "1":
                //是EHR系统的登录
                loginWay("人事系统", "1");
                break;
            case "2":
                //是邮箱页面登录
                loginWay("公司邮箱", "2");
                break;
            case "3":
                //是云之家网页页面登录
                loginWay("云之家", "3");
                break;
        }
    }

    private void loginWay(String whichpage, String pageFlag) {
        userLoginInfo = getUserLoginInfo(pageFlag);
        String account = (String) userLoginInfo.get("login_account");
        String password = (String) userLoginInfo.get("login_password");
        if (account == null && password == null) {
            //第一次登陆/重新登录，需要输入登陆账号，记住账号。
            dialogFirstLogin(whichpage, pageFlag);
        } else {
            //非第一次/非重新登陆，判断是登录的是哪个页面，根据页面提取账号密码，直接登录。
            login(pageFlag, account, password);
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
                        //保存密码并登陆
                        String account = accountEditText.getText().toString().trim();
                        String password = passwordEditText.getText().toString().trim();
                        login(pageFlag, account, password);
                        saveLoginInfo(pageFlag, account, password);
                    }
                });
        customizeDialog.show();
    }

    /**
     * 将对话框中的输入数据保存到数据库中
     */
    public void saveLoginInfo(final String pageid, final String account, final String password) {
        userInfoEntity = new UserInfoEntity();
        userInfoEntity.setPage_id(pageid);
        userInfoEntity.setLogin_account(account);
        userInfoEntity.setLogin_password(password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg.what = 1;
                msg.arg1 = Integer.parseInt(pageid);
                msg.obj = userInfoEntity;
                handler.sendMessage(msg);
            }
        }).start();

    }

    /**
     * 根据URL判断是否登录成功
     */
    private Boolean isURLChange(String url, String pageid) {
        Boolean flag = false;
        switch (pageid) {
            case "0":
                //是考勤主页面登录
                flag = isEqualStr(url, "http://elec.hoperun.com:8106/eoffice_pc/attendance/initPage");
                break;
            case "1":
                //是EHR系统的登录
                flag = isEqualStr(url, "http://ehr.hoperun.com:8018/HRIS/Portal/Home.do#nv_1");
                break;
            case "2":
                //是邮箱页面登录
                flag = isEqualStr(url, "http://mail.hoperun.com/wm2e/mail/login/show/loginShowAction_loginShow.do");
                break;
            case "3":
                //是云之家网页页面登录
                flag = isEqualStr(url, "https://www.yunzhijia.com/im/xiaoxi/");
                break;
        }
        return flag;
    }

    /**
     * 验证字符串是否相等
     */
    private Boolean isEqualStr(String url, String sample) {
        String subURL, subSample;
        StringBuilder stringBuilder1 = new StringBuilder(url);
        StringBuilder stringBuilder2 = new StringBuilder(sample);
        if (url.length() >= sample.length()) {
            subURL = stringBuilder1.substring(0, sample.length());
            subSample = stringBuilder2.substring(0, sample.length());
        } else {
            subURL = stringBuilder1.substring(0, url.length());
            subSample = stringBuilder2.substring(0, url.length());
        }
        boolean equals = subURL.equals(subSample);
        return equals;
    }

    /**
     * 联系人登录信息存在时，从数据库中查出数据直接登陆
     */
    private void login(final String pageFlag, String account, String password) {
        //把账号和密码回填入H5界面中
        webView01.loadUrl("javascript:androidLoginInterface('" + account + "','" + password + "')");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg.what = 2;
                msg.arg1 = Integer.parseInt(pageFlag);
                handler.sendMessage(msg);
            }
        }).start();
        //点击H5界面的登陆按钮
    }


    /**
     * 从数据库中获取登录的账号密码
     */
    public Map<String, Object> getUserLoginInfo(String pageid) {
        HashMap<String, Object> userLoginInfo = mySQLiteHelper.getUserLoginInfo(pageid);
        return userLoginInfo;
    }

}
