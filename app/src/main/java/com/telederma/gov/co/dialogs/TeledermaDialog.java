package com.telederma.gov.co.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.telederma.gov.co.R;

/**
 * Created by Daniel Hernández on 13/07/2018.
 */

abstract class TeledermaDialog extends Dialog implements View.OnClickListener {

    protected Context contexto;

    public Button btnClose;
    private FrameLayout dialogContent;

    public TeledermaDialog(@NonNull Context contexto) {
        super(contexto);
        this.contexto = contexto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_telederma);

        btnClose = (Button) findViewById(R.id.btn_close_dialog);
        btnClose.setOnClickListener(this);
        dialogContent = findViewById(R.id.dialog_content);

        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        dialogContent.addView(inflater.inflate(getIdLayout(), dialogContent, false));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_close_dialog) {
            dismiss();
        }
    }

    @Override
    public void show() {
        super.show();

        getWindow().setBackgroundDrawableResource(R.color.white_transperent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    @LayoutRes
    abstract protected int getIdLayout();
}
