package com.telederma.gov.co;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.robohorse.gpversionchecker.GPVersionChecker;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseCredential;
import com.telederma.gov.co.http.response.ResponseParametros;
import com.telederma.gov.co.modelo.Aseguradora;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.Credencial;
import com.telederma.gov.co.modelo.Departamento;
import com.telederma.gov.co.modelo.Municipio;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.receivers.SincronizacionBroadcast;
import com.telederma.gov.co.services.SincronizacionService;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Actividad que ejecuta la carga de datos y validaciones iniciales al ingresar a la app
 */
public class FallidaActivity extends BaseActivity {

    private TextView tv_imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

        setContentView(R.layout.activity_fallida);

        tv_imei = (TextView) findViewById(R.id.tv_imei);
        tv_imei.setText(Utils.getInstance(TeledermaApplication.getInstance()).getIMEI());
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
