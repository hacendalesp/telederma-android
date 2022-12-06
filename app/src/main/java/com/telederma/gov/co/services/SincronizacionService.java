package com.telederma.gov.co.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.telederma.gov.co.modelo.DataSincronizacion;

public class SincronizacionService extends Service {

    private String tag = "SincronizacionService";
    private SincronizacionServiceTask asyncThread;

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

    private void init() {
        Log.e(tag, "init == ");
        asyncThread = new SincronizacionServiceTask();
        if(asyncThread != null) asyncThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(tag, "onStartCommand == ");
        asyncThread = new SincronizacionServiceTask();
        if(asyncThread != null) asyncThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e(tag, "onStartCommand == ACTION_SCREEN_OFF == true");

        }else {
            //Toast.makeText(this, "Sincronizando ...", Toast.LENGTH_SHORT).show();
            Log.e(tag, "onStartCommand == ACTION_SCREEN_OFF == false");
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
