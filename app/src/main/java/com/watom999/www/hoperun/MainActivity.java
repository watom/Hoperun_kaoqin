package com.watom999.www.hoperun;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.watom999.www.hoperun.data.MySQLiteHelper;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView webView01;
    MyDialog myDialog;
    WebSettings webSettings;
    Map<String, String> inputData;
    int page_type=0;//0:主页面 1：ERP系统 2：邮箱 3：云之家

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView01 = (WebView) findViewById(R.id.where_webview);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialogFirstLogin();
                isFirstCome();//判断是否是第一次登陆，如果是第一次登陆，弹出Dialog，如果不是弹出登陆账号列表
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
//        initWebView("http://elec.hoperun.com:8106/eoffice_pc/");
        initWebView("file:///android_asset/js01.html");//注意路径不同
        //------------------------------------

        TextView timeView = (TextView) findViewById(R.id.time);
//        Date date = DateUtil.getDate(new Date());
//        String myDate = "";
//        if (!(date == null)) {
//            myDate= DateUtil.format(date);
//        }
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
        webView01.setWebViewClient(new WebViewClient());
        //加载网络上的HTML文件
        webView01.loadUrl(url);

        webView01.addJavascriptInterface(new AndroidAndJSInterface(), "Android");

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setUseWideViewPort(true);//扩大比例的缩放

        //webView01.evaluateJavascript("",);

        webSettings.setLoadWithOverviewMode(true);

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         *  1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
         *  2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

    }

    /**
     * 登录入口
     */
    private void isFirstCome() {
        if (null != PrefUtils.getString("id", null, this)) {
            //非第一次/非重新登陆，判断是登录的是哪个页面，根据页面提取账号密码，直接登录。
            switch (page_type){
                case 0:
                    //是主页面登录
                    dialogSelectLogin();
                    break;
                case 1:
                    //是EHR系统的登录
                    break;
                case 2:
                    //是邮箱页面登录
                    break;
                case 3:
                    //是云之家页面登录
                    break;
            }
        } else {
            //第一次登陆，需要输入登陆账号，记住账号。
            switch (page_type) {
                case 0:
                    //是主页面登录
                    dialogFirstLogin("考勤");
                    break;
                case 1:
                    //是EHR系统的登录
                    dialogFirstLogin("人事系统");
                    break;
                case 2:
                    //是邮箱页面登录
                    dialogFirstLogin("公司邮箱");
                    break;
                case 3:
                    //是云之家页面登录
                    dialogFirstLogin("云之家");
                    break;
            }
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
            initWebView("http://ehr.hoperun.com:8018/HRIS/");
            page_type=1;
        } else if (id == R.id.nav_gallery) {
            initWebView("http://mail.hoperun.com/");
            page_type=2;
        } else if (id == R.id.nav_slideshow) {
            initWebView("https://www.yunzhijia.com/home/?m=open&a=login");
            page_type=3;
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_bigevent) {
            startActivity(new Intent(this, LearnMaterialsQuery.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_relate) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void dailogBack() {
        finish();
    }

    /**
     * 首次登陆和添加新的联系人时弹出该对话框
     */
    private void dialogFirstLogin(String whichPage) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.alert_dialog_2editview, null);
        customizeDialog.setTitle("提 示");
        customizeDialog.setIcon(R.mipmap.ic_launcher);
        customizeDialog.setMessage("      首次或重新登陆后，此应用会记住"+whichPage+"的登录信息！");
        customizeDialog.setView(dialogView);
        //获取EditView中的输入内容
        final EditText accountEditText = dialogView.findViewById(R.id.input_id);
        final EditText passwordEditText = dialogView.findViewById(R.id.input_password);
        customizeDialog.setPositiveButton("登陆",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (true) {
                            //保存密码并登陆
                            String account = accountEditText.getText().toString().trim();
                            String password = passwordEditText.getText().toString().trim();
                            login(account, password);
                            saveUserLoginInfo(account, password,MainActivity.this);
                        } else {
                            //提示密码或账号错误
                            Toast.makeText(MainActivity.this, "密码或账号有误，请重试", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        customizeDialog.show();
    }

    /**
     * 联系人登录信息存在时，从对话框列表中选择联系人登陆
     */
    private int yourChoice = -1;

    private void dialogSelectLogin() {
        //直接登录
        PrefUtils.getString("账号","密码",this);
        login("账号","密码");
//        yourChoice = 0;
//        final String[] items = {"列表1", "列表2", "列表3", "列表4"};
//        AlertDialog.Builder singleChoiceDialog =
//                new AlertDialog.Builder(MainActivity.this);
//        singleChoiceDialog.setIcon(R.mipmap.ic_launcher);
//        singleChoiceDialog.setTitle("单选+确认Dialog");
//        // 第二个参数是默认选项，此处设置为0
//        singleChoiceDialog.setSingleChoiceItems(items, 0,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        yourChoice = which;
//                    }
//                });
//        singleChoiceDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (yourChoice != -1) {
//                            Toast.makeText(MainActivity.this,
//                                    "确认: " + items[yourChoice], Toast.LENGTH_LONG);
////                            登陆
////                            login();
//                        }
//                    }
//                });
//        singleChoiceDialog.show();
    }

    private void login(String id, String password) {
        //把账号和密码回填入H5界面中
        webView01.loadUrl("javascript:androidLoginInterface('" + id + "','" + password + "')");
        //点击H5界面的登陆按钮

    }

    private void saveAndGetPassWord() {
        inputData = MyDialog.getInputData(this);//先把数据保存到SP中，再拿出数据一个Map
        String id = inputData.get("id");
        String password = inputData.get("password");
        login(id, password);//java回填JS账号和密码
        //跳转到主页
    }


    class AndroidAndJSInterface {
        public void showToast() {
            Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dailogBack();
        }
        return false;
    }

    /**
     * 获取dialog中的数据
     *
     * @param context
     * @return
     */
    public Map<String, String> getUserLoginInfo(Context context, String id, String password) {
        HashMap<String, String> map = new HashMap<>();
        if (null != PrefUtils.getString("id", null, context)) {
            //从SP文件中拿出数据。并装在Map集合中返回调用处。
            map.put("id", PrefUtils.getString("LOGIN_ID", null, context));
            map.put("password", PrefUtils.getString("LOGIN_PASSWARD", null, context));
        }
        return map;
    }

    /**
     * 将对话框中的输入数据保存到SP中
     * @param context
     * @return
     */
    public void saveUserLoginInfo( String id, String password,Context context) {
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(context);
        HashMap<String, String> map = new HashMap<>();
        if (null == PrefUtils.getString("id", null, context)) {
            PrefUtils.putString("LOGIN_ACCOUNT", id, context);
            PrefUtils.putString("LOGIN_PASSWARD", password, context);
        }
    }
}
