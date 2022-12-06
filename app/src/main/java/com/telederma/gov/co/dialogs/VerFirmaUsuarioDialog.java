package com.telederma.gov.co.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.utils.Session;
import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public class VerFirmaUsuarioDialog extends TeledermaDialog {

    private Button btnEnviar, btnCancelar;
    private ImageView imgFirma;
    private View.OnClickListener onEnviarClickListener;

    public VerFirmaUsuarioDialog(@NonNull Context contexto) {
        super(contexto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnEnviar = findViewById(R.id.btn_enviar);
        btnEnviar.setOnClickListener(v -> {
            if (onEnviarClickListener != null) {
                btnEnviar.setEnabled(false);
                onEnviarClickListener.onClick(v);
            }

            dismiss();
        });

        String firma = Session.getInstance(contexto).getCredentials().getImagenFirma();

        if(!firma.contains("http")) {
            firma = "http://190.25.231.102" + firma;
        }


        btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(v -> dismiss());
        imgFirma = findViewById(R.id.img_firma);
        Glide.with(contexto)
                .load(firma)
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.cargando_firma)
                        .error(R.drawable.error_cargando)
                ).into(imgFirma);
    }

    public void setOnEnviarClickListener(View.OnClickListener onEnviarClickListener) {
        this.onEnviarClickListener = onEnviarClickListener;

    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_ver_firma_usuario;
    }
}
