package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telederma.gov.co.R;
import com.telederma.gov.co.ReproducirVideoActivity;
import com.telederma.gov.co.dialogs.VerPDFDialog;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_PERMANENTE_VIDEOS;
import static com.telederma.gov.co.utils.Constantes.FORMATO_DIRECTORIO_ARCHIVO;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class AprendeManejoAppFragment extends BaseFragment implements View.OnClickListener {

    private static final String VIDEO_MANEJO_APP = String.format(FORMATO_DIRECTORIO_ARCHIVO, DIRECTORIO_PERMANENTE_VIDEOS, "tutorial.mp4");
    private static final String MANUAL_DE_USUARIO_PDF = "manual_de_usuario.pdf";

    private static final int REQUEST_REPRODUCIR_VIDEO = 100;

    private View btnVideoTutorial, btnManualUsuario;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.aprende_manejo_app);
        inicializarViews(rootView);

        return rootView;
    }

    private void inicializarViews(View rootView) {
        btnVideoTutorial = rootView.findViewById(R.id.btn_video_tutorial);
        btnManualUsuario = rootView.findViewById(R.id.btn_manual_usuario);

        btnVideoTutorial.setOnClickListener(this);
        btnManualUsuario.setOnClickListener(this);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_aprende_manejo_app;
    }

    @Override
    public void onClick(View view) {
        final Intent intent;
        switch (view.getId()) {
            case R.id.btn_video_tutorial:
                intent = new Intent(ReproducirVideoActivity.ACTION_REPRODUCIR_VIDEO);
                intent.putExtra(ReproducirVideoActivity.EXTRA_RUTA_VIDEO, VIDEO_MANEJO_APP);
                intent.putExtra("orientation", "portrait");
                startActivityForResult(intent, REQUEST_REPRODUCIR_VIDEO);

                break;

            case R.id.btn_manual_usuario:
                new VerPDFDialog(contexto, new ArchivoDescarga(
                        null, Constantes.DIRECTORIO_PERMANENTE_PDF, MANUAL_DE_USUARIO_PDF)
                ).show();

                break;
        }
    }

}
