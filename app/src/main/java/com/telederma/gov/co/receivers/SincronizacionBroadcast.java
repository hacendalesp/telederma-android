package com.telederma.gov.co.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.telederma.gov.co.services.SincronizacionService;
import com.telederma.gov.co.services.SincronizacionServiceJob;

public class SincronizacionBroadcast extends BroadcastReceiver {
    public static Context context;
    private String tag = "SincronizacionBroadcast";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.e(tag, "onReceive==");
        SincronizacionServiceJob.enqueueWork(ctx, new Intent());
//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            ctx.startService(new Intent(ctx, SincronizacionServiceJob.class));
//        }

//        //ctx.startService(new Intent(ctx, SincronizacionService.class));
//        // TODO: 2/20/19 Sebas - LLamar servicios en background sin estar abierta la App
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            Intent serviceIntent = new Intent(ctx, SincronizacionService.class);
////            ContextCompat.startForegroundService(ctx, serviceIntent);
//            ctx.startService(new Intent(ctx, SincronizacionService.class));
//        } else {
//            ctx.startService(new Intent(ctx, SincronizacionService.class));
//        }
    }



}
