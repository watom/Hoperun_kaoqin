package com.watom999.www.hoperun.broadcast_receiver;


import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.watom999.www.hoperun.activity.TimingLaunchActivity;
import com.watom999.www.hoperun.utils.Logout;
import com.watom999.www.hoperun.utils.MyToast;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public final static String ACTION_SEND = "pw.msdx.ACTION_SEND";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SEND.equals(action)) {
            // MyToast.showToast(context, "收到指定广播");
            Logout.e("收到指定广播");
            // 可以处理一些业务doSomething
            abortBroadcast();
        }
    }
}
