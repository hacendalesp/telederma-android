package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.telederma.gov.co.R;
import com.telederma.gov.co.dialogs.VerPDFDialog;
import com.telederma.gov.co.interfaces.IOpcionMenu;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_PERMANENTE_PDF;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class AprendeFragment extends BaseFragment implements IOpcionMenu, View.OnClickListener {

    private Button btnQueEsTelederma, btnManejoApp, btnTomaImagenes, btnSemiologia, btnTomaBiopsia,
            btnConsejos, btnCertificacion;

    private static final String QUE_ES_TELEDERMA_PDF = "que_es_telederma.pdf";
    private static final String TOMA_DE_IMAGENES_PDF = "toma_de_imagenes.pdf";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        inicializarViews(rootView);

        return rootView;
    }

    private void inicializarViews(View rootView) {
        btnQueEsTelederma = rootView.findViewById(R.id.btn_que_es_telederma);
        btnManejoApp = rootView.findViewById(R.id.btn_manejo_app);
        btnTomaImagenes = rootView.findViewById(R.id.btn_toma_imagenes);
        btnSemiologia = rootView.findViewById(R.id.btn_semiologia);
        btnTomaBiopsia = rootView.findViewById(R.id.btn_toma_biopsia);
        btnConsejos = rootView.findViewById(R.id.btn_consejos);
        btnCertificacion = rootView.findViewById(R.id.btn_certificacion);

        btnQueEsTelederma.setOnClickListener(this);
        btnManejoApp.setOnClickListener(this);
        btnTomaImagenes.setOnClickListener(this);
        btnSemiologia.setOnClickListener(this);
        btnTomaBiopsia.setOnClickListener(this);
        btnConsejos.setOnClickListener(this);
        btnCertificacion.setOnClickListener(this);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_aprende;
    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_aprende;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (view.getId()) {
            case R.id.btn_que_es_telederma:
                new VerPDFDialog(contexto, new ArchivoDescarga(
                        null, DIRECTORIO_PERMANENTE_PDF, QUE_ES_TELEDERMA_PDF
                )).show();
                break;

            case R.id.btn_manejo_app:
                ft.replace(R.id.menu_content, new AprendeManejoAppFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();
                break;

            case R.id.btn_toma_imagenes:
                new VerPDFDialog(contexto, new ArchivoDescarga(
                        null, DIRECTORIO_PERMANENTE_PDF, TOMA_DE_IMAGENES_PDF
                )).show();
                break;

            case R.id.btn_semiologia:
                ft.replace(R.id.menu_content, new AprendeSemiologiaFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();
                break;

            case R.id.btn_toma_biopsia:
                ft.replace(R.id.menu_content, new AprendeTomaBiopsiaFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();
                break;

            case R.id.btn_consejos:
                break;

            case R.id.btn_certificacion:
                ft.replace(R.id.menu_content, new CertificacionFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();
                break;
        }
    }
}
