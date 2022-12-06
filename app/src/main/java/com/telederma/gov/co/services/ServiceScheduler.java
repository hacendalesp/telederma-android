package com.telederma.gov.co.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.telederma.gov.co.modelo.DataSincronizacion;

public final class ServiceScheduler {
    static Context _context;

    public ServiceScheduler(Context context) {
        //_context = context;
        //scheduleService();
    }

    public static ServiceScheduler newInstance(Context context) {
        _context = context;
        Context appContext = context.getApplicationContext();
        Context safeContext = ContextCompat.createDeviceProtectedStorageContext(appContext);

        new DataSincronizacion().sincronizar(safeContext);

        if (safeContext == null) {
            safeContext = appContext;
        }

        return new ServiceScheduler(safeContext);
    }


    public void scheduleService() {
        SincronizacionServiceJob.enqueueWork(_context, new Intent());
    }
}