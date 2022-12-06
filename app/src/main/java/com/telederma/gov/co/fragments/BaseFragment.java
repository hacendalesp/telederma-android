package com.telederma.gov.co.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.services.LogoutService;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Fragment base con los comportamientos por defecto que deben tener los fragments
 * <p>
 * Created by Daniel Hernández on 6/8/2018.
 */
public abstract class BaseFragment extends Fragment {

    private static final String ERROR_BODY_TAG_ERROR = "error";

    protected Context contexto;
    protected DBUtil dbUtil;
    protected Utils utils;
    protected Session session;

    @LayoutRes
    abstract protected int getIdLayout();

    protected void setTitle(int idTitle) {
        getActivity().setTitle(idTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.contexto = context;
        this.dbUtil = DBUtil.getInstance(getDbHelper(), contexto);
        this.utils = Utils.getInstance(context);
        this.session = Session.getInstance(this.contexto);

    }

    public DBHelper getDbUtil()
    {
        return getDbHelper();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getIdLayout(), container, false);

        return rootView;
    }

    /**
     * Muestra un mensaje en pantalla
     *
     * @param idString
     * @param tipoMensaje
     */
    protected void mostrarMensaje(int idString, int tipoMensaje) {
        ((BaseActivity) getActivity()).mostrarMensaje(idString, tipoMensaje);
    }

    /**
     * Muestra un mensaje en pantalla
     *
     * @param mensaje
     * @param tipoMensaje
     */
    public void mostrarMensaje(String mensaje, int tipoMensaje) {
        ((BaseActivity) getActivity()).mostrarMensaje(mensaje, tipoMensaje);
    }

    /**
     * Muestra un mensaje de espera en pantalla
     */
    protected void mostrarMensajeEspera(Snackbar.Callback callback) {
        ((BaseActivity) getActivity()).mostrarMensajeEspera(callback);
    }

    /**
     * Oculta un mensaje de espera en pantalla
     */
    protected void ocultarMensajeEspera() {
        if(getActivity() != null) ((BaseActivity) getActivity()).ocultarMensajeEspera();
    }

    public double medirMemoria() {
        return ((BaseActivity) getActivity()).medirMemoria();
    }

    /**
     * Método utilitario que valida si la respuesta de un servicio de la api viene con error,
     * lo maneja y muestra un mensaje de error en pantalla
     * <p>
     * Autor: Daniel Hernández
     *
     * @param response
     * @return
     */
    protected boolean validarRespuestaServicio(Response response, Map<String, Map<String, View>> camposValidados) {
        if (response.code() == Constantes.RESPONSE_CODE_ERROR_411) {
            try {
                JSONObject errorBody = new JSONObject(response.errorBody().string());
                Map<View, String> camposConErrores = new HashMap<>();

                if (camposValidados != null && !camposValidados.isEmpty()) {
                    final Iterator<Map.Entry<String, Map<String, View>>> iObjetos = camposValidados.entrySet().iterator();
                    Map.Entry<String, Map<String, View>> jsonObjeto = null;

                    while (iObjetos.hasNext()) {
                        jsonObjeto = iObjetos.next();
                        if (errorBody.has(jsonObjeto.getKey())) {
                            final Iterator<Map.Entry<String, View>> iCampos = jsonObjeto.getValue().entrySet().iterator();
                            Map.Entry<String, View> jsonCampo = null;

                            while (iCampos.hasNext()) {
                                jsonCampo = iCampos.next();
                                if (errorBody.getJSONObject(jsonObjeto.getKey()).has(jsonCampo.getKey())) {
                                    final StringBuilder errores = new StringBuilder();
                                    JSONArray listaErrores = errorBody.getJSONObject(jsonObjeto.getKey()).getJSONArray(jsonCampo.getKey());

                                    for (int i = 0; i < listaErrores.length(); i++)
                                        errores.append(i == 0 ? listaErrores.get(i) : ", " + listaErrores.get(i));

                                    camposConErrores.put(
                                            jsonCampo.getValue(),
                                            errores.toString()
                                    );
                                }
                            }
                        }
                    }

                    if (!camposConErrores.isEmpty())
                        utils.mostrarCamposConErrores(camposConErrores);
                }

                mostrarMensaje(errorBody.getString(ERROR_BODY_TAG_ERROR), MSJ_ERROR);
            } catch (JSONException e) {
                Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error convirtiendo la respuesta [Error 411]", e);
                mostrarMensaje(R.string.msj_error, MSJ_ERROR);
            } catch (IOException e) {
                Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error convirtiendo la respuesta [Error 411]", e);
                mostrarMensaje(R.string.msj_error, MSJ_ERROR);
            }

            return false;
        } else if (response.code() == Constantes.RESPONSE_CODE_ERROR_401) {
            try {
                Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error convirtiendo la respuesta [Error 401]");
                final JSONObject errorBody = new JSONObject(response.errorBody().string());

                if (errorBody.has(ERROR_BODY_TAG_ERROR))
                    mostrarMensaje(errorBody.getString(ERROR_BODY_TAG_ERROR), MSJ_ERROR);
                else
                    mostrarMensaje(R.string.msj_error, MSJ_ERROR);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        } else if (response.code() == Constantes.RESPONSE_CODE_ERROR_500) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error convirtiendo la respuesta [Error 500]");
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);

            return false;
        }

        return true;
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
        ((BaseActivity) getActivity()).procesarExcepcionServicio(throwable);
    }

    protected void asignarEventoOcultarTeclado(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    ocultarTeclado();
                    return false;
                }
            });
        }
        //Para recorrer los hijos de las vistas
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                asignarEventoOcultarTeclado(innerView);
            }
        }
    }

    protected void ocultarTecladoAutomatic() {

        ocultarTeclado();
    }

    private void ocultarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if(focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            focusedView.clearFocus();
        }
    }

    private DBHelper dbHelper;

    protected DBHelper getDbHelper() {
        if (dbHelper == null)
            dbHelper = OpenHelperManager.getHelper(contexto, DBHelper.class);

        return dbHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }

        if(LogoutService.timer != null)
            LogoutService.timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        //FullScreencall();

        if (Session.getInstance(contexto).get(Session.SESSION_LOGUEADO) != null) {
            if(Boolean.valueOf(Session.getInstance(contexto).get(Session.SESSION_LOGUEADO).toString())) {
                if (LogoutService.timer != null)
                    LogoutService.timer.start();
                //((Activity)contexto).startService(new Intent(contexto, LogoutService.class));

            }
        }

        Log.i(this.getTag(), "inResume=====>");


    }




    @Override
    public void onStop() {
        super.onStop();

    }

    public void FullScreencall() {
//        if(contexto != null && ((Activity)contexto instanceof Activity)) {
//            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//                View v = ((Activity) contexto).getWindow().getDecorView();
//                v.setSystemUiVisibility(View.GONE);
//            } else if (Build.VERSION.SDK_INT >= 19) {
//                //for new api versions.
//                View decorView = ((Activity) contexto).getWindow().getDecorView();
//                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
//                decorView.setSystemUiVisibility(uiOptions);
//            }
//        }
    }

    





}
