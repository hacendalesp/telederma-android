package com.telederma.gov.co.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.ReproducirVideoActivity;
import com.telederma.gov.co.dialogs.VerPDFDialog;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;

import java.util.HashMap;
import java.util.Map;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_PERMANENTE_VIDEOS;
import static com.telederma.gov.co.utils.Constantes.FORMATO_DIRECTORIO_ARCHIVO;

/**
 * Created by Daniel Hernández on 10/08/2018.
 */

public class AprendeSemiologiaFragment extends BaseFragment {

    private static final String ANEXOS_PDF = "semiologia_anexos.pdf";
    private static final String VIDEO_SEMIOLOGIA = String.format(FORMATO_DIRECTORIO_ARCHIVO, DIRECTORIO_PERMANENTE_VIDEOS, "video_semiologia.mp4");

    private ViewGroup aprendeContenidoBotones;
    private ScrollView scrollView;
    private static final int REQUEST_REPRODUCIR_VIDEO = 100;

    private static final Map<Integer, String> mapaNombresSemiologia;

    static {
        mapaNombresSemiologia = new HashMap<Integer, String>() {{
            put(0, "Video semiología cutánea");
            put(1, "ampolla");
            put(2, "atrofia");
            put(3, "cicatriz");
            put(4, "costra");
            put(5, "erosión");
            put(6, "escama");
            put(7, "esclerosis");
            put(8, "excoriación");
            put(9, "fístula");
            put(10, "fisura");
            put(11, "roncha");
            put(12, "liquenificación");
            put(13, "mácula");
            put(14, "nódulo");
            put(15, "pápula");
            put(16, "parche");
            put(17, "placa");
            put(18, "poiquilodermia");
            put(19, "pústula");
            put(20, "quiste");
            put(21, "telangiectasia");
            put(22, "tumor");
            put(23, "úlcera");
            put(24, "vegetaciones");
            put(25, "vesícula");
        }};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.aprende_semiologia);

        inicializarViews(rootView);

        return rootView;
    }

    private void inicializarViews(View rootView) {
        aprendeContenidoBotones = rootView.findViewById(R.id.aprende_contenido_botones);
        scrollView = rootView.findViewById(R.id.scv_aprende_semiologia);

        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //TODO Cambiar por ListView con adapter
        for (int i = 0; i <= 25; i++) {
            View boton = inflater.inflate(R.layout.aprende_semiologia_boton_item, null);
            String nombreSemiologia = mapaNombresSemiologia.get(i);
            int finalI = i;
            ((TextView) boton.findViewById(R.id.texto_button)).setText(
                    nombreSemiologia.substring(0, 1).toUpperCase() + nombreSemiologia.substring(1)
            );
            boton.setOnClickListener(v -> onClickItem(nombreSemiologia));
            aprendeContenidoBotones.addView(boton);
        }

        final View boton = inflater.inflate(R.layout.aprende_semiologia_boton_item, null);
        ((TextView) boton.findViewById(R.id.texto_button)).setText(
                getResources().getString(R.string.aprende_semiologia_anexos)
        );
        boton.setOnClickListener(v ->
                new VerPDFDialog(contexto, new ArchivoDescarga(null, Constantes.DIRECTORIO_PERMANENTE_PDF, ANEXOS_PDF)).show()
        );
        aprendeContenidoBotones.addView(boton);
    }

    private void onClickItem(String nombreSemiologia) {

        if(nombreSemiologia.equals("Video semiología cutánea")){
            Intent intent = new Intent(ReproducirVideoActivity.ACTION_REPRODUCIR_VIDEO);
               intent.putExtra(ReproducirVideoActivity.EXTRA_RUTA_VIDEO, VIDEO_SEMIOLOGIA);
               startActivityForResult(intent, REQUEST_REPRODUCIR_VIDEO);
        } else {
            Fragment fragment = new AprendeSemiologiaContentFragment();
            Bundle args = new Bundle();
            args.putString(AprendeSemiologiaContentFragment.ARG_SEMIOLOGIA_SELECTED, nombreSemiologia);
            fragment.setArguments(args);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.menu_content, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }

    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_aprende_semiologia;
    }
}
