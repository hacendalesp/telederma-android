package com.telederma.gov.co.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Daniel Hernández on 7/4/2018.
 */

abstract class BaseReceiver extends BroadcastReceiver {

    public boolean isRegistered;
    //protected boolean isRegistered;

    public Intent register(Context context, IntentFilter filter) {
        try {
            return !isRegistered ? context.registerReceiver(this, filter) : null;
        } finally {
            isRegistered = true;
        }
    }

    public boolean unregister(Context context) {
        //return isRegistered && unregisterInternal(context);
        return unregisterInternal(context);
    }

    private boolean unregisterInternal(Context context) {
        context.unregisterReceiver(this);
        isRegistered = false;

        return true;
    }



}
