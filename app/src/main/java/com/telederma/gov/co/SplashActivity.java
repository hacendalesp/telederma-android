package com.telederma.gov.co;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseCredential;
import com.telederma.gov.co.http.response.ResponseParametros;
import com.telederma.gov.co.modelo.Aseguradora;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.Credencial;
import com.telederma.gov.co.modelo.DataSincronizacion;
import com.telederma.gov.co.modelo.Departamento;
import com.telederma.gov.co.modelo.Fallida;
import com.telederma.gov.co.modelo.Municipio;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.receivers.SincronizacionBroadcast;
import com.telederma.gov.co.services.LogoutService;
import com.telederma.gov.co.services.SincronizacionService;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.robohorse.gpversionchecker.GPVersionChecker;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ADVERTENCIA;
import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Actividad que ejecuta la carga de datos y validaciones iniciales al ingresar a la app
 */
public class SplashActivity extends BaseActivity {

    Timer timer = new Timer();
    final Handler handler = new Handler();

    private static final int PERMISSION_REQUEST = 200;

    private static final String[] permisos = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WAKE_LOCK

    };

    private static boolean permisosOtorgados = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

        setContentView(R.layout.activity_splash);


        solicitarPermisos();
        Log.e("IMEI_DISP", Utils.getInstance(TeledermaApplication.getInstance()).getIMEI());

//        Thread asyncThread = new Thread() {
//            @Override
//            public void run() {
//                new DataSincronizacion().sincronizar(getApplicationContext());
//            }
//        };
//        asyncThread.start();


    }



    private void solicitarPermisos() {

        for (String permiso : permisos)
            if (checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permisos, PERMISSION_REQUEST);

                return;
            }


        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST)
            for (int grantResult : grantResults)
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    solicitarPermisos();

                    return;
                }

        init();
    }

    private void init() {
        Thread hilo = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);actualizar();  sleep(1000);
                    inicializarServicio();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        hilo.start();
    }

    /**
     * Método que ejecuta la actualización de la base de datos local
     * <p>
     * Autor: Daniel Hernández
     */
    private void actualizar() {
        actualizarParametros();
        actualizarDepartamentos();
        actualizarAseguradoras();
        actualizarPartesCuerpo();
        actualizarCie10();
        actualizarCredenciales();
        verificarDispositivo();
    }

    /**
     * Método que actualiza la información de CIE10 en la base de datos local
     * <p>
     * Autor: Juan Sebastian Perez
     */
    private void actualizarCie10() {
        try {
            RuntimeExceptionDao<Cie10, Integer> cie10DAO = getDbHelper().getCie10RuntimeDAO();
            QueryBuilder<Cie10, Integer> queryBuilder = cie10DAO.queryBuilder();
            queryBuilder.setCountOf(true);
            long cie10BD = cie10DAO.countOf(queryBuilder.prepare());

            if (cie10BD == 0) {
                LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
                Observable<Response<List<Cie10>>> cie10Observable = loginService.consultarCie10();
                HttpUtils.configurarObservable(
                        SplashActivity.this, cie10Observable,
                        this::procesarRespuestaConsultaCie10, this::procesarExcepcionServicio
                );
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando registros cie10", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }


    /**
     * Método que trae credenciales amazon
     * <p>
     * Autor : Sebastian Noreña
     */
    private void actualizarCredenciales() {
        try
        {
            LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
            Observable<Response<ResponseCredential>> credencialesObservable = loginService.consultarCredenciales();
            HttpUtils.configurarObservable(
                    SplashActivity.this, credencialesObservable,
                    this::procesarRespuestaConsultarCredential, this::procesarExcepcionServicio
            );
        } catch (Exception e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando registros cie10", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }



    /**
     * Método que verificar dispositivo
     * <p>
     * Autor : Sebastian Noreña
     */
    private void verificarDispositivo() {
        try
        {
            LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
            Observable<Response<BaseResponse>> deviceObservable = loginService.consultarDevice();
            if(HttpUtils.validarConexionApi())
            {
                RuntimeExceptionDao<Fallida, Integer> fallidaDAO = getDbHelper().getFallidaRuntimeDAO();
                Fallida fallida  = new Fallida();
                fallida.setFallida(true);
                fallida.setId(1);
                fallida.setIdServidor(1);
                fallidaDAO.createOrUpdate(fallida);
            }
            else
                {
                    inicializar();
                }
            HttpUtils.configurarObservable(
                    SplashActivity.this, deviceObservable,
                    this::procesarRespuestaConsultarDevice, this::procesarExcepcionServicio
            );
        } catch (Exception e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando registros cie10", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    private void inicializar()
    {
        try
        {
            RuntimeExceptionDao<Fallida, Integer> fallidaDAO = getDbHelper().getFallidaRuntimeDAO();
            QueryBuilder<Fallida, Integer> queryBuilder = fallidaDAO.queryBuilder();
            List<Fallida> list = fallidaDAO.query(queryBuilder.prepare());
            Fallida f = null ;
            if(list.size()>0)
                f = list.get(list.size()-1);
            if(f != null && !f.getFallida())
            {
                Boolean logueado = (Boolean) Session.getInstance(SplashActivity.this).get(Session.SESSION_LOGUEADO);
                new GPVersionChecker.Builder(SplashActivity.this).create();
                if (logueado != null && (Boolean) logueado) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
            else
                startActivity(new Intent(SplashActivity.this, FallidaActivity.class));
        }
        catch (SQLException e)
        {

        }
    }

    /**
     * Método que maneja la respuesta del servicio web para consultar cie10
     *
     * @param response
     */
    private void procesarRespuestaConsultaCie10(Response<List<Cie10>> response) {
        RuntimeExceptionDao<Cie10, Integer> cie10DAO = getDbHelper().getCie10RuntimeDAO();
        cie10DAO.create(response.body());
    }


    /**
     * Método que maneja la respuesta del servicio web para consultar las credenciales
     *
     * @param response
     */
    private void procesarRespuestaConsultarCredential(Response<ResponseCredential> response) {

        if (response.code() == 200) {
            RuntimeExceptionDao<Credencial, Integer> credencials = getDbHelper().getCredencialRuntimeDAO();
            Credencial credencial = response.body().credencial;
            credencial.setId(1);
            credencial.setIdServidor(1);
            credencials.createOrUpdate(credencial);
        }


    }


    /**
     * Método que maneja la respuesta del servicio web para verificar device
     *
     * @param response
     */
    private void procesarRespuestaConsultarDevice(Response<BaseResponse> response) {
        if (response.code() == 200 && response.body().message.equals("Correcto")) {
            RuntimeExceptionDao<Fallida, Integer> fallidaDAO = getDbHelper().getFallidaRuntimeDAO();
            Fallida fallida  = new Fallida();
            fallida.setFallida(false);
            fallida.setId(1);
            fallida.setIdServidor(1);
            fallidaDAO.createOrUpdate(fallida);
        }
        inicializar();

    }

    /**
     * Método que actualiza los parámetros del sistema en la base de datos local con las constantes de la api
     * <p>
     * Autor: Daniel Hernández
     */
    private void actualizarParametros() {
        try {
            RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
            QueryBuilder<Parametro, Integer> queryBuilder = parametroDAO.queryBuilder();
            queryBuilder.setCountOf(true);
            long parametrosBD = parametroDAO.countOf(queryBuilder.prepare());

            if (parametrosBD == 0) {
                LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
                Observable<Response<ResponseParametros>> parametrosObservable = loginService.consultarConstantes();
                HttpUtils.configurarObservableForSubscribe(SplashActivity.this, parametrosObservable);
                parametrosObservable.map(result -> {
                    List<Parametro> parametros = new ArrayList<>();
                    Parametro parametro;
                    int idServidor = 0;
                    for (ResponseParametros.Parametro p : result.body().listaParametros) {
                        for (Map.Entry<String, Integer> value : p.values.entrySet()) {
                            parametro = new Parametro();
                            parametro.setIdServidor(--idServidor);
                            parametro.setTipo(p.type);
                            parametro.setNombre(value.getKey());
                            parametro.setValor(value.getValue());
                            parametros.add(parametro);
                        }
                    }

                    return parametros;
                })
                        .subscribe(this::procesarRespuestaConsultaConstantes, this::procesarExcepcionServicio);
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando usuario", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    /**
     * Método que maneja la respuesta obtenida por el servicio web para consultar las constantes
     * <p>
     * Autor: Daniel Hernández
     *
     * @param parametros
     */
    private void procesarRespuestaConsultaConstantes(List<Parametro> parametros) {
        RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
        parametroDAO.create(parametros);
    }

    /**
     * Método que actualiza la información de departamentos y municipios en la base de datos local
     * <p>
     * Autor: Daniel Hernández
     */
    private void actualizarDepartamentos() {
        try {
            RuntimeExceptionDao<Departamento, Integer> departamentoDAO = getDbHelper().getDepartamentoRuntimeDAO();
            QueryBuilder<Departamento, Integer> queryBuilder = departamentoDAO.queryBuilder();
            queryBuilder.setCountOf(true);
            long departamentosBD = departamentoDAO.countOf(queryBuilder.prepare());

            if (departamentosBD == 0) {
                LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
                Observable<Response<List<Departamento>>> departamentosOservable = loginService.consultarDepartamentos();
                HttpUtils.configurarObservable(
                        SplashActivity.this, departamentosOservable,
                        this::procesarRespuestaConsultaDepartamentos, this::procesarExcepcionServicio
                );
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando usuario", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    /**
     * Método que maneja la respuesta del servicio web para consultar departamentos
     *
     * @param response
     */
    private void procesarRespuestaConsultaDepartamentos(Response<List<Departamento>> response) {
        RuntimeExceptionDao<Departamento, Integer> departamentoDAO = getDbHelper().getDepartamentoRuntimeDAO();
        RuntimeExceptionDao<Municipio, Integer> municipioDAO = getDbHelper().getMunicipioRuntimeDAO();
        departamentoDAO.create(response.body());

        List<Municipio> municipios = new ArrayList<>();
        for (Departamento departamento : response.body())
            municipios.addAll(departamento.getMunicipios());

        municipioDAO.create(municipios);
    }

    /**
     * Método que actualiza la información de aseguradoras en la base de datos local
     * <p>
     * Autor: Sebastian Noreña Marquez
     */
    private void actualizarAseguradoras() {
        try {
            RuntimeExceptionDao<Aseguradora, Integer> aseguradoraDAO = getDbHelper().getAseguradoraRuntimeDAO();
            QueryBuilder<Aseguradora, Integer> queryBuilder = aseguradoraDAO.queryBuilder();
            queryBuilder.setCountOf(true);
            long aseguradoraBD = aseguradoraDAO.countOf(queryBuilder.prepare());

            if (aseguradoraBD == 0) {
                LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
                Observable<Response<List<Aseguradora>>> aseguradoraObservable = loginService.consultarAseguradoras();
                HttpUtils.configurarObservable(
                        SplashActivity.this, aseguradoraObservable,
                        this::procesarRespuestaConsultaAseguradora, this::procesarExcepcionServicio
                );
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error consultando aseguradoras", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    /**
     * Método que maneja la respuesta del servicio web para consultar aseguradora
     *
     * @param response
     */


    private void procesarRespuestaConsultaAseguradora(Response<List<Aseguradora>> response) {
        RuntimeExceptionDao<Aseguradora, Integer> aseguradoraDAO = getDbHelper().getAseguradoraRuntimeDAO();
        aseguradoraDAO.create(response.body());
    }


    /**
     * Método que actualiza la información de partes del cuerpo en la base de datos local
     * <p>
     * Autor: Sebastian Noreña Marquez
     */
    private void actualizarPartesCuerpo() {
        try {
            RuntimeExceptionDao<ParteCuerpo, Integer> parteDao = getDbHelper().getParteCuerpoRuntimeDAO();
            QueryBuilder<ParteCuerpo, Integer> queryBuilder = parteDao.queryBuilder();
            queryBuilder.setCountOf(true);
            long parteBD = parteDao.countOf(queryBuilder.prepare());

            if (parteBD == 0) {
                LoginService loginService = (LoginService) HttpUtils.crearServicio(LoginService.class);
                Observable<Response<List<ParteCuerpo>>> parteObservable = loginService.consultarPartesCuerpo();
                HttpUtils.configurarObservable(
                        SplashActivity.this, parteObservable,
                        this::procesarRespuestaConsultaPartesCuerpo, this::procesarExcepcionServicio
                );
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error consultando partes del cuerpo", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    private void procesarRespuestaConsultaPartesCuerpo(Response<List<ParteCuerpo>> response) {
        RuntimeExceptionDao<ParteCuerpo, Integer> parteCuerpoDAO = getDbHelper().getParteCuerpoRuntimeDAO();
        parteCuerpoDAO.create(response.body());
    }

    public void programarEnvio(){

        Log.e("programarEnvio", "programarEnvio");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                AsyncTask mytask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        new Handler (Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                new DataSincronizacion().sincronizar(getApplicationContext());
                            }
                        });

                        return null;
                    }
                };
                mytask.execute();
            }
        };
        timer.schedule(task,0,20000);
    }

    public void inicializarServicio() {
        programarEnvio();
        return;

        /*************************************Para iniciar servicio de sincronizacón**********************************/
//        if (!Utils.verificarEjecucionDelServicio(SincronizacionService.class, getApplicationContext())) { //método que determina si el servicio ya está corriendo o no
//            Intent serv = new Intent(this, SincronizacionService.class); //serv de tipo Intent
//            startService(serv); //ctx de tipo Context
//        }
//        SincronizacionBroadcast.context = this;
//        Intent intent = new Intent(getApplicationContext(), SincronizacionBroadcast.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), Constantes.INTERVALO_TIEMPO_SINCRONIZACION, pendingIntent);

        /**************************************************************************************/

    }

    public static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }


    public static String bytesToHuman (long size)
    {
        long Kb = 1  * 1000;
        long Mb = Kb * 1000;
        long Gb = Mb * 1000;
        long Tb = Gb * 1000;
        long Pb = Tb * 1000;
        long Eb = Pb * 1000;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "???";
    }

    private Boolean validateSpace(long size){
        Boolean is_valid = false;
        long Kb = 1  * 1000;
        long Mb = Kb * 1000;
        long Gb = Mb * 1000;


        if (size >= Mb) {// + " Mb";
            if( ((double) size / Mb) < 1000 && ((double) size / Mb) > 100){
                is_valid = true;
                Toast.makeText(this, "Te queda poco espacio en el dispositivo " +
                        bytesToHuman(size)+ ", se necesita un mínio de 100 Mb para funcionar" +
                        " correctamente", Toast.LENGTH_LONG).show();
            }else if( ((double) size / Mb) < 100 ){
                Toast.makeText(this, "Telederma necesita 100 Mb disponibles y tienes " +
                        bytesToHuman(size)+" libera espacio para funcionar correctamente", Toast.LENGTH_LONG).show();
            }else{
                is_valid = true;
            }
        }

        return is_valid;
    }

    private void changeScreenBrightness(Context context, int screenBrightnessValue)
    {
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue);

        /*
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = screenBrightnessValue / 255f;
        window.setAttributes(layoutParams);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

    }
}
