package com.telederma.gov.co;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.telederma.gov.co.receivers.ConnectivityReceiver;
import com.telederma.gov.co.services.LogoutService;
import com.telederma.gov.co.utils.Constantes;

import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel Hernández on 6/30/2018.
 */

public class TeledermaApplication extends Application {

    private static TeledermaApplication mInstance;

    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        File outputFile = new File(String.format(Constantes.FORMATO_DIRECTORIO_ARCHIVO,
                Constantes.DIRECTORIO_PERMANENTE_LOGS, Constantes.ARCHIVO_LOG)
        );
        try {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("logcat -v time -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //appLockManager = AppLockManager.getInstance();
        mInstance = this;
    }

    public static synchronized TeledermaApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener connectivityListener) {
        ConnectivityReceiver.connectivityReceiverListener = connectivityListener;
    }

   /* public void touch() {
        appLockManager.updateTouch();
    }

    public void enableAppLock() {
        appLockManager.enableDefaultAppLockIfAvailable(TeledermaApplication.this);
    }

    public void disableAppLock() {
        appLockManager.disableDefaultAppLockIfAvailable(TeledermaApplication.this);
    }*/


    public TeledermaApplication() {
        super();
    }
}
