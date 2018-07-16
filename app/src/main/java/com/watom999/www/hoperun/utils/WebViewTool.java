package com.watom999.www.hoperun.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.watom999.www.hoperun.activity.UserInfoQuery;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class WebViewTool {
    private Context context;
    private PackageManager packageManager;
    
    public WebViewTool(Context context) {
        this.context=context;
    }

    public void initWebView(WebView webView, String url) {
        // 加载网页 H5,html,自定义浏览器，或者网页播放器
        // webView = new WebView(this);
        // 设置WebSettings支持javascript
        WebSettings webSettings = webView.getSettings();
        //在本地浏览器的页面里面有页面时，也会调用webview，不会调用手机浏览器
        webView.setWebViewClient(new WebViewClient());
        //设置为可调用js方法
        webSettings.setJavaScriptEnabled(true);
        //不调用浏览器 而调用自己的内部的浏览器
        webView.setWebViewClient(new WebViewClient() {
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
        webView.loadUrl(url);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setUseWideViewPort(true);//扩大比例的缩放
        //将提供的Java对象注入到此WebView中。可以让JS通过android的这个字段调用这个java对象中的方法
        webView.addJavascriptInterface(new androidLoginInterface(), "Android");

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         *  1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
         *  2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }

    class androidLoginInterface {
        @JavascriptInterface  //这里的JavascriptInterface注解很重要
        public void showToast() {
            Toast.makeText(context, "js调用安卓代码", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, UserInfoQuery.class);
            context.startActivity(intent);
        }
    }
    public void openLocalApp(String packageName, String appName) {
        packageManager = context.getPackageManager();
        if (checkPackInfo(packageName)) {//检查是否有要打开的app
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            if (packageName.equals("com.alibaba.android.rimet")) {
                //打开钉钉考勤界面
            }
//            Intent intent = new Intent();
//            intent.setClassName("com.alibaba.android.rimet", "com.alibaba.android.rimet.lightapp.runtime.activity.CommonWebViewActivity");
            context.startActivity(intent);
        } else {
            MyToast.showToast(context, "手机未安装" + appName + "软件，正前往手机市场...");
            launchAppMarket(packageName, appName);//跳转到应用市场
        }
    }

    public void launchAppMarket(String packageName, String appName) {
        String model = Build.BRAND;
        String appmarket = null;
        if (model.contains("huawei") ||model.equalsIgnoreCase("huawei")) {
            appmarket = "com.huawei.appmarket";
        } else if (model.contains("xiaomi")) {
            appmarket = "com.xiaomi.market";
        } else {
            MyToast.showToast(context, "请在应用市场上下载" + appName);
            return;
        }
        try {
            if (TextUtils.isEmpty(packageName)) return;

            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(appmarket)) {
                intent.setPackage(appmarket);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查包是否存在。
     */
    public boolean checkPackInfo(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
}
