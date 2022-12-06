package com.telederma.gov.co.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.telederma.gov.co.R;
//import com.telederma.gov.co.modules.GlideApp;
import com.bumptech.glide.Glide;
import com.telederma.gov.co.views.ZoomImageView;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Daniel Hernández on 18/07/2018.
 */

public class VerImagenDialog extends Dialog {

    private Button btnClose;
    private ZoomImageView imageView;
    private String urlImagen;

    public VerImagenDialog(@NonNull Context context, String urlImagen) {
        super(context);

        this.urlImagen = urlImagen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_ver_imagen);

        imageView = findViewById(R.id.img_imagen);
        Glide.with(getContext())
                .load(urlImagen)
                .apply(new RequestOptions().fitCenter()
                        .placeholder(R.drawable.cargando)
                        .error(R.drawable.error_cargando)
                ).into(imageView);

        btnClose = findViewById(R.id.btn_close_dialog);
        btnClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void show() {
        super.show();

        getWindow().setBackgroundDrawableResource(android.R.color.black);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }
}
