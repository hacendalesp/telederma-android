package com.telederma.gov.co.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.telederma.gov.co.R;

/**
 * Created by Juan Sebastian P.
 */

public class ConfirmDialog extends TeledermaDialog {

    protected Context contexto;
    private TextView tv_title, tv_desc;
    private int title;
    private String text;
    public Button btn_send, btn_cancel;

    public ConfirmDialog(@NonNull Context contexto, @StringRes int idTitulo, String texto) {
        super(contexto);
        this.contexto = contexto;
        this.title = idTitulo;
        this.text = texto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tv_title = findViewById(R.id.tv_title);
        this.tv_desc = findViewById(R.id.tv_desc);
        this.btn_send = findViewById(R.id.btn_send);
        this.btn_cancel = findViewById(R.id.btn_cancel);

        this.tv_title.setText(title);
        this.tv_desc.setText(text);


    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_confirm;
    }


}
