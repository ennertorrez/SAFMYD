package com.suplidora.sistemas.sisago.Principal;

/**
 * Created by Sistemas on 17/7/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoArranque extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context,  GPSTracker.class);
        context.startService(service);
    }
}
