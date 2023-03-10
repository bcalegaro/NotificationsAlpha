package com.bcalegaro.notificationsalpha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if ((Intent.ACTION_BOOT_COMPLETED).equals(intent.getAction())){
            // reset all alarms (system boot)
        }
        else{
            // perform your scheduled task here (eg. send alarm notification)
            Log.d("NOTI-ALPHA","o louco, recebeu");
            NotificationUtils _notificationUtils = new NotificationUtils(context);
            NotificationCompat.Builder _builder = _notificationUtils.setNotification("Test + 10", "Testing notification system");
            _notificationUtils.getManager().notify(101, _builder.build());
        }
  }
}