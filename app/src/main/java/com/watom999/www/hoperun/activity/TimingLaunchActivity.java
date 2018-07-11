package com.watom999.www.hoperun.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.watom999.www.hoperun.R;
import com.watom999.www.hoperun.broadcast_receiver.AlarmReceiver;
import com.watom999.www.hoperun.utils.Logout;
import com.watom999.www.hoperun.utils.MyToast;


public class TimingLaunchActivity extends Activity{
    AlarmManager am;
    PendingIntent sendIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timing_launch_layout);
        setLaunchTime();

    }

    private void setLaunchTime() {
        Intent intent = new Intent("pw.msdx.ACTION_SEND");
        sendIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sendIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), sendIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), sendIntent);
        } else {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 3000, sendIntent);
        }

    }


   private void text(){

   }
}
