package com.telederma.gov.co.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.bumptech.glide.Glide;

/**
 * Created by Juan Sebastian P.
 */

public class LoadingDialog extends TeledermaDialog {

    protected Context contexto;
    public LoadingDialog(@NonNull Context contexto) {
        super(contexto);
        this.contexto = contexto;
        this.setCancelable(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
        btnClose.setVisibility(View.GONE);

        ImageView iv_icon = findViewById(R.id.iv_icon);
        Glide.with(contexto)
                .load(R.drawable.loading)
                .into(iv_icon);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_loading;
    }


}
