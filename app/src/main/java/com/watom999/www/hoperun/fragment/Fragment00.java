package com.watom999.www.hoperun.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.utils.WebViewTool;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class Fragment00 extends Fragment {
    Activity activity;
    FrameLayout frameLayout;
    WebView webview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment00_layout, container, false);
        activity = getActivity();
        findView(view);
        new WebViewTool(activity).openLocalApp("com.kdweibo.client", "云之家");
        return view;
    }

    private void findView(View view) {
        frameLayout = view.findViewById(R.id.frame_layout);
        webview = view.findViewById(R.id.where_webview);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
