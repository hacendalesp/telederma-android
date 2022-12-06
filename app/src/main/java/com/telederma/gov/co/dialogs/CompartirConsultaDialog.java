package com.telederma.gov.co.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.R;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.CompartirConsultaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Created by Daniel Hernández on 13/07/2018.
 */

public final class CompartirConsultaDialog extends TeledermaDialog {

    private List<CompartirConsulta> consultas;
    private LinearLayout chkGroupConsultas;
    private Button btnCancelar, btnEnviar;


    public CompartirConsultaDialog(@NonNull Context context, List<CompartirConsulta> consultas) {
        super(context);

        this.consultas = consultas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chkGroupConsultas = findViewById(R.id.chk_group_consultas);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(v -> {
            dismiss();
        });

        for (CompartirConsulta c : consultas)
            Utils.getInstance(contexto).agregarCheckBox(
                    c.nombre, c.idServidor, c.checked, new CheckBox(contexto), chkGroupConsultas
            );

        btnEnviar = findViewById(R.id.btn_enviar);
        btnEnviar.setOnClickListener(v -> {
            onClickEnviar();
        });
    }

    private void onClickEnviar() {
        compartir();
    }

    private void compartir() {
        final List<String> consultasSeleccionadas = new ArrayList<>();

        for (int i = 0; i < chkGroupConsultas.getChildCount(); i++) {
            final CheckBox checkBox = ((CheckBox) chkGroupConsultas.getChildAt(i));
            if (checkBox.isChecked())
                consultasSeleccionadas.add(String.valueOf(checkBox.getId()));
        }

        final ConsultaService consultaService = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        Observable<Response<BaseResponse>> observable = consultaService.compartirConsultas(
                new CompartirConsultaRequest(
                        Session.getInstance(contexto).getCredentials().getToken(),
                        Session.getInstance(contexto).getCredentials().getEmail(),
                        consultasSeleccionadas
                )
        );
        HttpUtils.configurarObservable(
                this.contexto, observable,
                response -> {
                    Utils.showGeneralMessage(contexto, response.body().message);
                    //Toast.makeText(contexto, response.body().message, Toast.LENGTH_SHORT).show();
                    dismiss();
                },
                exception -> {
                    Toast.makeText(contexto, R.string.msj_error, Toast.LENGTH_SHORT).show();
                    Log.d("COMPARTIR_CONSULTA", "Ha ocurrido un error compartiendo la consultaMedicina", exception);
                }
        );
        dismiss();//cambiar si no se oculta
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_compartir_consulta;
    }

    static public class CompartirConsulta {

        public String nombre;
        public Integer idServidor;
        public boolean checked;

        public CompartirConsulta(String nombre, Integer idServidor) {
            this.nombre = nombre;
            this.idServidor = idServidor;
        }

        public Integer getIdServidor() {
            return idServidor;
        }
    }




}
