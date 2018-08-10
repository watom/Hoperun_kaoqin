package com.watom999.www.hoperun.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.activity.MainActivity;
import com.watom999.www.hoperun.utils.Logout;
import com.watom999.www.hoperun.utils.MyToast;
import com.watom999.www.hoperun.utils.WebViewTool;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class Fragment02 extends Fragment {
    MainActivity activity;
    FrameLayout frameLayout;
    WebView webview;
    View view;

    /**
     * 在onAttach()方法中注册广播接收者
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment02_layout, container, false);
        activity = (MainActivity) getActivity();
        findView(view);
        new WebViewTool(activity).initWebView(webview, "file:///android_asset/kaoqin01.html");
        return view;
    }

    private void findView(View view) {
        frameLayout = view.findViewById(R.id.frame_layout);
        webview = view.findViewById(R.id.where_webview);
    }

    /**
     * 几秒后判断登录成功后，返回给Host登录成功的数据，并提示用户
     * 给对应的Fragment页面发送数据，并显示登录成功后的页面
     * 几秒后联系人登录信息存在时，从数据库中查出数据直接登陆
     */
    public void login( String account, String password) {
        //把账号和密码回填入H5界面中
        webview.loadUrl("javascript:androidLoginInterface('" + account + "','" + password + "')");
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
//                msg.arg1 = Integer.parseInt(pageFlag);
//                handler.sendMessage(msg);
            }
        }).start();
        //点击H5界面的登陆按钮
    }
}
