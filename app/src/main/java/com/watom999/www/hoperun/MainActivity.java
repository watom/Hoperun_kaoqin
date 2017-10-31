package com.watom999.www.hoperun;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView webView01;
    MyDialog myDialog;
    WebSettings webSettings;
    Map<String, String> inputData;

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
                Snackbar.make(view, "请选择需要登陆的账号", Snackbar.LENGTH_LONG)
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
//        initWebView("file:///android_asset/js.html");//注意路径不同
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
//        webView01 = new WebView(this);
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
     * 登陆入口
     */
    private void isFirstCome() {
        if (null != PrefUtils.getString("id", null, this)) {
            //配置文件中有数据，说明是不是第一次登陆，可以选择账号,正常登陆
            dialogSelectLogin();
        } else {
            //配置文件中没有数据，说明第一次登陆，需要输入登陆账号
            dialogFirstLogin();
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

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
    private void dialogFirstLogin() {
        myDialog = new MyDialog(this, 0,new MyDialog.MyDialogCommonListener() {
            @Override
            public void onCommonClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.yes:
                        //保存密码
                        saveAndGetPassWord();
                        MainActivity.this.myDialog.dismiss();
                        break;
                    case R.id.no:
                        MainActivity.this.myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    /**
     * 有联系人存在时，从对话框列表中选择联系人登陆
     */
    private void dialogSelectLogin() {
        myDialog = new MyDialog(this,1, new MyDialog.MyDialogListViewItemListener() {
            @Override
            public void onListViewItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取存储的用户名称、账号、密码

                //登陆
                login("","");
            }
        });
        myDialog.show();
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
}
