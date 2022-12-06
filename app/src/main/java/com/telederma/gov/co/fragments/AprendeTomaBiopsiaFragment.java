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

public class AprendeTomaBiopsiaFragment extends BaseFragment implements View.OnClickListener {

    private static final String VIDEO_ELIPSE = String.format(FORMATO_DIRECTORIO_ARCHIVO, DIRECTORIO_PERMANENTE_VIDEOS, "biopsia_elipse.mp4");
    private static final String VIDEO_SACABOCADO = String.format(FORMATO_DIRECTORIO_ARCHIVO, DIRECTORIO_PERMANENTE_VIDEOS, "biopsia_sacabocado.mp4");
    private static final String INSTRUCTIVO_BIOPSIA_PDF = "instructivo_biopsia.pdf";

    private static final int REQUEST_REPRODUCIR_VIDEO = 100;

    private View btnElipse, btnSacabocado, btnInstructivo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.aprende_toma_biopsia);
        inicializarViews(rootView);

        return rootView;
    }

    private void inicializarViews(View rootView) {
        btnElipse = rootView.findViewById(R.id.btn_elipse);
        btnSacabocado = rootView.findViewById(R.id.btn_sacabocado);
        btnInstructivo = rootView.findViewById(R.id.btn_instructivo);

        btnElipse.setOnClickListener(this);
        btnSacabocado.setOnClickListener(this);
        btnInstructivo.setOnClickListener(this);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_aprende_toma_biopsia;
    }

    @Override
    public void onClick(View view) {
        final Intent intent;
        switch (view.getId()) {
            case R.id.btn_elipse:
                intent = new Intent(ReproducirVideoActivity.ACTION_REPRODUCIR_VIDEO);
                intent.putExtra(ReproducirVideoActivity.EXTRA_RUTA_VIDEO, VIDEO_ELIPSE);
                startActivityForResult(intent, REQUEST_REPRODUCIR_VIDEO);

                break;

            case R.id.btn_sacabocado:
                intent = new Intent(ReproducirVideoActivity.ACTION_REPRODUCIR_VIDEO);
                intent.putExtra(ReproducirVideoActivity.EXTRA_RUTA_VIDEO, VIDEO_SACABOCADO);
                startActivityForResult(intent, REQUEST_REPRODUCIR_VIDEO);

                break;

            case R.id.btn_instructivo:
                new VerPDFDialog(contexto, new ArchivoDescarga(
                        null, Constantes.DIRECTORIO_PERMANENTE_PDF, INSTRUCTIVO_BIOPSIA_PDF)
                ).show();

                break;
        }
    }

}
