package com.telederma.gov.co.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.interfaces.IDownloadView;
import com.telederma.gov.co.tasks.DescargarArchivoTask;
import com.telederma.gov.co.tasks.ParamDownload;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public class VerPDFDialog extends Dialog implements IDownloadView {

    private static final String TAG_DIALOG_PDF = "TAG_DIALOG_PDF";

    private ArchivoDescarga archivoDescarga;
    private PDFView pdfView;
    private Button btnClose;
    private ViewGroup layDescarga;
    private TextView lblDescarga;

    public VerPDFDialog(@NonNull Context contexto, ArchivoDescarga archivo) {
        super(contexto);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_ver_pdf);

        this.archivoDescarga = archivo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pdfView = findViewById(R.id.pdf_view);
        btnClose = findViewById(R.id.btn_close_dialog);
        btnClose.setOnClickListener(v -> dismiss());
        layDescarga = findViewById(R.id.lay_descarga);
        lblDescarga = findViewById(R.id.lbl_descarga);

        layDescarga.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.GONE);

        new DescargarArchivoTask().execute(
                new ParamDownload(this, this.archivoDescarga)
        );
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

    @Override
    public void updateDownloadProgress(Integer key, Integer progress) {
        lblDescarga.setText(String.valueOf(progress));
    }

    @Override
    public void onFinishDownload(Integer key, Boolean fileDownloaded) {
        if (fileDownloaded) {
            layDescarga.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);

            try {
                pdfView.fromStream(new FileInputStream(archivoDescarga.getRutaAbsoluta())).load();
            } catch (FileNotFoundException e) {
                Log.d(TAG_DIALOG_PDF,
                        String.format("Ocurrió un error cargando el archivo \n%s", archivoDescarga.getRutaAbsoluta()), e
                );
            }
        } else {
            lblDescarga.setText(R.string.reproductor_audio_descarga_fallida);
            Log.d(TAG_DIALOG_PDF, String.format("No se terminó la desarga del archivo \n%s", archivoDescarga));
        }
    }

}
