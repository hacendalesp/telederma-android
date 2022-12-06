package com.telederma.gov.co.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.telederma.gov.co.R;

/**
 * Created by Daniel Hernández on 8/08/2018.
 */

public class VerTextoDialog extends TeledermaDialog {

    private FrameLayout layContent;
    private WebView webView;
    private TextView lblTitulo;

    private int idTitulo;
    private String texto;

    public VerTextoDialog(@NonNull Context contexto, @StringRes int idTitulo, String texto) {
        super(contexto);

        this.idTitulo = idTitulo;
        this.texto = texto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lblTitulo = findViewById(R.id.ver_texto_titulo);
        lblTitulo.setText(idTitulo);
        layContent = findViewById(R.id.ver_texto_texto);
        webView = new WebView(contexto);
        webView.setVerticalScrollBarEnabled(false);
        layContent.addView(webView);
        webView.loadDataWithBaseURL("file:///android_asset/", "<html><head></head><body style='text-align:justify; color:gray;'>"
                        + texto + "</body></html>",
                "text/html; charset=utf-8", "utf-8", null);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_ver_texto;
    }

}
