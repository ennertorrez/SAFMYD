package com.suplidora.sistemas.sisago.Principal;

/**
 * Created by Sistemas on 17/7/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AutoArranque extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, GPSTracker.class));
        } else {
            context.startService(new Intent(context, GPSTracker.class));
        }

       /* Intent service = new Intent(context,  GPSTracker.class);
        context.startService (service);*/
    }
}
