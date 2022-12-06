package com.telederma.gov.co;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.receivers.ConnectivityReceiver;
import com.telederma.gov.co.services.LogoutService;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.utils.Utils;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Locale;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;
import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;


/**
 * Actividad base con los comportamientos por defecto que deben tener todas las actividades
 * <p>
 * Created by Daniel Hernández on 6/7/2018.
 */
public abstract class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String INTENT_EXTRA_HAY_INTERNET = "INTENT_EXTRA_HAY_INTERNET";

    private boolean hayInternet;
    private ConnectivityReceiver connectivityReceiver;

    protected Session session;
    protected boolean registrarConnectivityReceiver = true;

    private DBHelper dbHelper;
    protected Utils utils;
    private int currentApiVersion;
    private String tag = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent serviceIntent = new Intent(this, LogoutService.class);
//        startService(serviceIntent);

        //cambia el color del header
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //cambiar el colro del statusBar
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        //registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));

        hayInternet = getIntent().getBooleanExtra(INTENT_EXTRA_HAY_INTERNET, hayInternet);
        utils = Utils.getInstance(BaseActivity.this);


        setBrightness(50);
        setVolumen(100);
    }




    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //FullScreencall();

        if (Session.getInstance(getApplicationContext()).get(Session.SESSION_LOGUEADO) != null) {
            if(Boolean.valueOf(Session.getInstance(getApplicationContext()).get(Session.SESSION_LOGUEADO).toString())) {
                if (LogoutService.timer == null) {
                    startService(new Intent(this, LogoutService.class));
                }else {
                    LogoutService.timer.start();
                }
            }
        }

        if (registrarConnectivityReceiver) {
            final IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            connectivityReceiver = new ConnectivityReceiver();

            connectivityReceiver.register(this, intentFilter);
            //TeledermaApplication.getInstance().setConnectivityListener(this);
        }
    }

    //Todo: Revisar para el launcher

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(connectivityReceiver);

        Log.i(this.getLocalClassName(), "inPause=====>");
//        // Sobreescribe el comportamiento del botón Actividades recientes
//        // para evitar que se abran otras aplicaciones
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /**
     * Muestra un mensaje en pantalla
     * <p>
     * Autor: Daniel Hernández
     *
     * @param idString
     * @param tipoMensaje
     */
    public void mostrarMensaje(@StringRes int idString, int tipoMensaje) {
        mostrarMensaje(getResources().getString(idString), tipoMensaje);
    }

    /**
     * Muestra un mensaje en pantalla
     * <p>
     * Autor: Daniel Hernández
     *
     * @param mensaje
     * @param tipoMensaje
     */
    public void mostrarMensaje(@NonNull String mensaje, int tipoMensaje) {
        mostrarMensaje(mensaje, tipoMensaje, Snackbar.LENGTH_LONG, null);
    }

    /**
     * Configura y muestra el mensaje en pantalla
     * <p>
     * Autor: Daniel Hernández
     *
     * @param mensaje
     * @param tipoMensaje
     * @param duracion
     */
    private void mostrarMensaje(String mensaje, int tipoMensaje, int duracion, Snackbar.Callback callback) {
        utils.mostrarMensaje(BaseActivity.this, mensaje, tipoMensaje, duracion, callback);
    }

    private void mostrarMensajeConexion(boolean hayConexion) {
        if (hayConexion)
            mostrarMensaje(R.string.msj_con_conexion, MSJ_INFORMACION);
        else
            mostrarMensaje(R.string.msj_sin_conexion, MSJ_ERROR);
    }

    @Override
    public void onBackPressed() {
        // Se sobreeescribe el comportamiento del botón atrás para facilitar la navegación entre fragments
        // Se debe agregar el fragment a la pila con el método addToBackStack de FragmentTransaction
        final FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 1)
            fm.popBackStack();

        fm.executePendingTransactions();
    }

    /**
     * Muestra un mensaje de espera en pantalla
     */
    public void mostrarMensajeEspera(Snackbar.Callback callback) {
        mostrarMensaje(getResources().getString(R.string.msj_espera), MSJ_INFORMACION, Snackbar.LENGTH_INDEFINITE, callback);
    }

    /**
     * Oculta el mensaje de espera en pantalla
     */
    public void ocultarMensajeEspera() {
        if (Utils.snackbar != null) Utils.snackbar.dismiss();
    }

    /**
     * Método utilitario que maneja las excepciones lanzadas por un servicio de la api
     * Muestra un mensaje de error en pantalla
     * <p>
     * Autor: Daniel Hernández
     *
     * @param throwable
     */
    public void procesarExcepcionServicio(Throwable throwable) {
        ocultarMensajeEspera();


        if (throwable instanceof SocketTimeoutException) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error consumiendo el servicio", throwable);
            mostrarMensaje(R.string.msj_error_timeout, MSJ_ERROR);

            return;
        } else if (!hayInternet || throwable instanceof ConnectException || throwable instanceof NoRouteToHostException) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error validando la conexión a la api", throwable);
            mostrarMensaje(R.string.msj_sin_conexion, MSJ_ERROR);

            return;
        }

        Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error consumiendo el servicio", throwable);
        mostrarMensaje(R.string.msj_error, MSJ_ERROR);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (hayInternet != isConnected) {
            mostrarMensajeConexion(isConnected);
            hayInternet = isConnected;
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.i(this.getLocalClassName(), "inStop=====>");


//        Intent i = getIntent();
//        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(i);

        if (registrarConnectivityReceiver) {
            // TODO: 5/6/19 revisar validacion con esta variable publica
            if (!connectivityReceiver.isRegistered)
                connectivityReceiver.unregister(this);

        }
    }


    @Override
    public void startActivity(Intent intent) {
        if (intent != null) intent.putExtra(INTENT_EXTRA_HAY_INTERNET, hayInternet);
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        if (options != null) options.putBoolean(INTENT_EXTRA_HAY_INTERNET, hayInternet);
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        if (intent != null) intent.putExtra(INTENT_EXTRA_HAY_INTERNET, hayInternet);
        super.startActivityFromFragment(fragment, intent, requestCode, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent != null) intent.putExtra(INTENT_EXTRA_HAY_INTERNET, hayInternet);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Se invoca al método de la clase super para que lo pueda reconocer el fragment
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected DBHelper getDbHelper() {
        if (dbHelper == null)
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        return dbHelper;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }

        Log.i(tag, "controller status ondestroy ");

    }

    @Override
    public boolean isDestroyed() {
        if(LogoutService.timer != null)
            LogoutService.timer.cancel();
        return super.isDestroyed();
    }

    public TeledermaApplication getApp() {
        return (TeledermaApplication) this.getApplication();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (Session.getInstance(getApplicationContext()).get(Session.SESSION_LOGUEADO) != null
                && Boolean.valueOf(Session.getInstance(getApplicationContext()).get(Session.SESSION_LOGUEADO).toString()))
           // getApp().touch();

        Log.d(getClass().getName(), "User interaction to " + this.toString());
    }

    public double medirMemoria() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

        //Percentage can be calculated for API 16+
        return (mi.availMem / (double)mi.totalMem * 100.0);
    }

    // se pasa el porcentaje de brillo requerido
    protected void setBrightness(int brightness){
        WindowManager.LayoutParams layout = getWindow().getAttributes();

        layout.screenBrightness = (float)brightness/100;
        //layout.screenBrightness = (float)brightness*255/100;
        //layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);
    }

    protected void setVolumen(int volumen){
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float percent = (float)volumen/100;
        //float percent = 0.7f;
        int seventyVolume = (int) (maxVolume*percent);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, 0);
    }






}
