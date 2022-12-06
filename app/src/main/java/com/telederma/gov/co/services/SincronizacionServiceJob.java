package com.telederma.gov.co.services;

import android.app.Service;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.telederma.gov.co.modelo.DataSincronizacion;

public class SincronizacionServiceJob extends JobIntentService{

    private String tag = "SincronizacionService";
    private SincronizacionServiceTask asyncThread;

    public static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent work) {
        Log.e("SincronizacionService", "enqueueWork==");
        enqueueWork(context, SincronizacionServiceJob.class, JOB_ID, work);
    }

    private class SincronizacionServiceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            new DataSincronizacion().sincronizar(getApplicationContext());

            return null;
        }
    }

    @Override
    public void onCreate() {
        init();
    }





    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e(tag, "onHandleWork == ");

        asyncThread = new SincronizacionServiceTask();
        if(asyncThread != null) asyncThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void init() {
        Log.e(tag, "init == ");
        asyncThread = new SincronizacionServiceTask();
        if(asyncThread != null) asyncThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
