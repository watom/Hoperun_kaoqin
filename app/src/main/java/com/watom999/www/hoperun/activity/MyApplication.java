package com.watom999.www.hoperun.activity;

import android.app.Application;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class MyApplication extends Application{
    public static Application mApplication;
    @Override
    public void onCreate()
    {
        super.onCreate();
        mApplication = this;
    }

    public static Application getApplication()
    {
        return mApplication;
    }
}
