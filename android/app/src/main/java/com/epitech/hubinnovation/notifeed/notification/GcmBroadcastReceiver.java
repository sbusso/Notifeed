package com.epitech.hubinnovation.notifeed.notification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.epitech.hubinnovation.notifeed.activity.MainActivity;

/**
 * Created by Roro on 16/07/2015.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), MainActivity.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        // http://developer.android.com/reference/android/content/BroadcastReceiver.html#setResultCode(int)
        setResultCode(Activity.RESULT_OK);
    }

}
