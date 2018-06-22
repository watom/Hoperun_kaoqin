package com.watom999.www.hoperun.broadcast_receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.watom999.www.hoperun.activity.TimingLaunchActivity;
import com.watom999.www.hoperun.utils.MyToast;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyToast.showToast(context, "收到定时广播");
        Intent i = new Intent(context, TimingLaunchActivity.class);
        context.startService(i);
    }
}
