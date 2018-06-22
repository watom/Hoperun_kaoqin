package com.watom999.www.hoperun.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.broadcast_receiver.AlarmReceiver;


public class TimingLaunchActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timing_launch_layout);
        setLaunchTime();

    }

    private void setLaunchTime() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);//获取AlarmManager实例
        int anHour =  6 * 1000 ;  // 6秒
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent2 = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);//开启提醒
    }

}
