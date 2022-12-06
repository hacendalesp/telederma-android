package com.telederma.gov.co.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_PERMANENTE;
import static com.telederma.gov.co.utils.Constantes.FORMATO_DIRECTORIO_ARCHIVO;
import static com.telederma.gov.co.utils.Constantes.SEPARADOR_DIRECTORIOS;

/**
 * Created by Daniel Hernández on 10/08/2018.
 */

public class AprendeSemiologiaContentFragment extends BaseFragment {

    private static final String SEMIOLOGIA_DIR = String.format(FORMATO_DIRECTORIO_ARCHIVO, DIRECTORIO_PERMANENTE, "semiologia");
    private static final String ILUSTRACIONES_DIR = "ilustraciones";
    private static final String IMAGENES_DIR = "imagenes";
    private static final String TEXTOS_DIR = "textos";

    public static final String ARG_SEMIOLOGIA_SELECTED = "ARG_SEMIOLOGIA_SELECTED";

    private ImageView imgImagen, imgIlustracion;
    private FrameLayout layContent;
    private WebView webView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        imgImagen = rootView.findViewById(R.id.img_imagen_semiologia);
        imgIlustracion = rootView.findViewById(R.id.img_ilustracion_semiologia);
        layContent = rootView.findViewById(R.id.lay_texto_semiologia);

        cargarInformacion();

        return rootView;
    }

    private void cargarInformacion() {
        final String nombreSemiologia = getArguments().getString(ARG_SEMIOLOGIA_SELECTED);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(
                nombreSemiologia.substring(0, 1).toUpperCase() + nombreSemiologia.substring(1)
        );
        final String texto = FileUtils.leerArchivo(
                String.format(FORMATO_DIRECTORIO_ARCHIVO,
                        SEMIOLOGIA_DIR + SEPARADOR_DIRECTORIOS + TEXTOS_DIR,
                        nombreSemiologia + Constantes.EXTENSION_ARCHIVO_TEXTO)
        );
        imgImagen.setImageBitmap(BitmapFactory.decodeFile(
                String.format(FORMATO_DIRECTORIO_ARCHIVO,
                        SEMIOLOGIA_DIR + SEPARADOR_DIRECTORIOS + IMAGENES_DIR,
                        nombreSemiologia + Constantes.EXTENSION_ARCHIVO_IMAGEN_JPG)
        ));
        imgIlustracion.setImageBitmap(BitmapFactory.decodeFile(
                String.format(FORMATO_DIRECTORIO_ARCHIVO,
                        SEMIOLOGIA_DIR + SEPARADOR_DIRECTORIOS + ILUSTRACIONES_DIR,
                        nombreSemiologia + Constantes.EXTENSION_ARCHIVO_IMAGEN_PNG)
        ));

        webView = new WebView(contexto);
        layContent.addView(webView);
        webView.loadData("<html><head></head><body style='text-align:justify; color:gray;'>"
                        + texto + "</body></html>",
                "text/html; charset=utf-8", "utf-8");
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_aprende_semiologia_content;
    }

}
