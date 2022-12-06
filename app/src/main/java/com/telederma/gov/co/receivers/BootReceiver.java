package com.telederma.gov.co.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.telederma.gov.co.services.ServiceScheduler;
import com.telederma.gov.co.services.SincronizacionServiceJob;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
            SincronizacionServiceJob.enqueueWork(context, new Intent());
            ServiceScheduler serviceScheduler = ServiceScheduler.newInstance(context);
//            if (serviceScheduler.isEnabled()) {
                //serviceScheduler.scheduleService();
//            }
        }
    }
}
