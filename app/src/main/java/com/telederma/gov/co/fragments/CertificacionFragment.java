package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.telederma.gov.co.R;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.AprendeService;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class CertificacionFragment extends BaseFragment {

    private static final Map<Integer, Integer> mapaRespuestas;

    static {
        mapaRespuestas = new HashMap<Integer, Integer>() {{
            put(R.id.rdg_pregunta_1, 1);
            put(R.id.rdg_pregunta_2, 4);
            put(R.id.rdg_pregunta_3, 4);
            put(R.id.rdg_pregunta_4, 2);
            put(R.id.rdg_pregunta_5, 1);
            put(R.id.rdg_pregunta_6, 2);
            put(R.id.rdg_pregunta_7, 1);
            put(R.id.rdg_pregunta_8, 1);
            put(R.id.rdg_pregunta_9, 2);
            put(R.id.rdg_pregunta_10, 3);
        }};
    }

    private RadioGroup pregunta1, pregunta2, pregunta3, pregunta4, pregunta5, pregunta6, pregunta7,
            pregunta8, pregunta9, pregunta10;
    private Button btnEnviarRespuestas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.aprende_certificacion);
        inicializarViews(rootView);

        return rootView;
    }

    private void inicializarViews(View rootView) {
        pregunta1 = rootView.findViewById(R.id.rdg_pregunta_1);
        pregunta2 = rootView.findViewById(R.id.rdg_pregunta_2);
        pregunta3 = rootView.findViewById(R.id.rdg_pregunta_3);
        pregunta4 = rootView.findViewById(R.id.rdg_pregunta_4);
        pregunta5 = rootView.findViewById(R.id.rdg_pregunta_5);
        pregunta6 = rootView.findViewById(R.id.rdg_pregunta_6);
        pregunta7 = rootView.findViewById(R.id.rdg_pregunta_7);
        pregunta8 = rootView.findViewById(R.id.rdg_pregunta_8);
        pregunta9 = rootView.findViewById(R.id.rdg_pregunta_9);
        pregunta10 = rootView.findViewById(R.id.rdg_pregunta_10);
        btnEnviarRespuestas = rootView.findViewById(R.id.btn_enviar_respuestas);
        btnEnviarRespuestas.setOnClickListener(v -> {
            mostrarMensajeEspera(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar sb) {
                    validarCertificacion();
                }
            });
        });

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_1_respuesta_1, 1, null, new RadioButton(contexto), pregunta1);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_1_respuesta_2, 2, null, new RadioButton(contexto), pregunta1);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_1_respuesta_3, 3, null, new RadioButton(contexto), pregunta1);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_1_respuesta_4, 4, null, new RadioButton(contexto), pregunta1);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_2_respuesta_1, 1, null, new RadioButton(contexto), pregunta2);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_2_respuesta_2, 2, null, new RadioButton(contexto), pregunta2);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_2_respuesta_3, 3, null, new RadioButton(contexto), pregunta2);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_2_respuesta_4, 4, null, new RadioButton(contexto), pregunta2);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_3_respuesta_1, 1, null, new RadioButton(contexto), pregunta3);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_3_respuesta_2, 2, null, new RadioButton(contexto), pregunta3);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_3_respuesta_3, 3, null, new RadioButton(contexto), pregunta3);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_3_respuesta_4, 4, null, new RadioButton(contexto), pregunta3);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_4_respuesta_1, 1, null, new RadioButton(contexto), pregunta4);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_4_respuesta_2, 2, null, new RadioButton(contexto), pregunta4);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_4_respuesta_3, 3, null, new RadioButton(contexto), pregunta4);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_4_respuesta_4, 4, null, new RadioButton(contexto), pregunta4);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_5_respuesta_1, 1, null, new RadioButton(contexto), pregunta5);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_5_respuesta_2, 2, null, new RadioButton(contexto), pregunta5);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_5_respuesta_3, 3, null, new RadioButton(contexto), pregunta5);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_5_respuesta_4, 4, null, new RadioButton(contexto), pregunta5);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_6_respuesta_1, 1, null, new RadioButton(contexto), pregunta6);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_6_respuesta_2, 2, null, new RadioButton(contexto), pregunta6);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_6_respuesta_3, 3, null, new RadioButton(contexto), pregunta6);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_6_respuesta_4, 4, null, new RadioButton(contexto), pregunta6);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_7_respuesta_1, 1, null, new RadioButton(contexto), pregunta7);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_7_respuesta_2, 2, null, new RadioButton(contexto), pregunta7);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_8_respuesta_1, 1, null, new RadioButton(contexto), pregunta8);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_8_respuesta_2, 2, null, new RadioButton(contexto), pregunta8);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_9_respuesta_1, 1, null, new RadioButton(contexto), pregunta9);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_9_respuesta_2, 2, null, new RadioButton(contexto), pregunta9);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_9_respuesta_3, 3, null, new RadioButton(contexto), pregunta9);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_9_respuesta_4, 4, null, new RadioButton(contexto), pregunta9);

        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_10_respuesta_1, 1, null, new RadioButton(contexto), pregunta10);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_10_respuesta_2, 2, null, new RadioButton(contexto), pregunta10);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_10_respuesta_3, 3, null, new RadioButton(contexto), pregunta10);
        utils.agregarRadioButton(R.string.aprende_certificacion_pregunta_10_respuesta_4, 4, null, new RadioButton(contexto), pregunta10);
    }

    private void validarCertificacion() {
        byte puntaje = 0;

        if (pregunta1.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_1))
            puntaje += 10;
        if (pregunta2.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_2))
            puntaje += 10;
        if (pregunta3.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_3))
            puntaje += 10;
        if (pregunta4.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_4))
            puntaje += 10;
        if (pregunta5.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_5))
            puntaje += 10;
        if (pregunta6.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_6))
            puntaje += 10;
        if (pregunta7.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_7))
            puntaje += 10;
        if (pregunta8.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_8))
            puntaje += 10;
        if (pregunta9.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_9))
            puntaje += 10;
        if (pregunta10.getCheckedRadioButtonId() == mapaRespuestas.get(R.id.rdg_pregunta_10))
            puntaje += 10;

        if (puntaje >= 70)
            enviar();
        else {
            mostrarMensaje(R.string.aprende_certificacion_no_certificado, Utils.MSJ_ERROR);
            regresar();
        }
    }

    private void enviar() {
        AprendeService service = (AprendeService) HttpUtils.crearServicio(AprendeService.class);
        final Session session = Session.getInstance(contexto);

        Observable<Response<BaseResponse>> observable = service.enviarCertificacion(
                session.getCredentials().getIdUsuario(),
                true,
                session.getCredentials().getEmail(),
                session.getCredentials().getToken()
        );

        HttpUtils.configurarObservable(
                contexto, observable,
                this::procesarRespuestaEnviar, this::procesarExcepcionServicio
        );
    }

    private void procesarRespuestaEnviar(Response<BaseResponse> response) {
        if (validarRespuestaServicio(response, null)) {
            ocultarMensajeEspera();
            mostrarMensaje(response.body().message, Utils.MSJ_INFORMACION);
            regresar();
        }
    }

    private void regresar() {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.menu_content, new AprendeFragment()).commit();
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_certificacion;
    }

}
