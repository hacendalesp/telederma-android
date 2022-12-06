package com.telederma.gov.co;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;
import com.telederma.gov.co.views.FirmaView;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Actividad que muestra un tablero de dibujo para ingresar una firma
 */
public class FirmaDigitalActivity extends BaseActivity implements View.OnClickListener {

    public static final String INTENT_EXTRA_FIRMA = "INTENT_EXTRA_FIRMA";
    public static final String ACTION_FIRMA_DIGITAL = "com.telederma.gov.co.FirmaDigitalActivity";

    public static final String NOMBRE_IMAGEN_FIRMA = "imagen_firma";

    private FirmaView firmaView;
    private Button btnConfirmar;
    private Button btnBorrar;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_digital);

        firmaView = (FirmaView) findViewById(R.id.firma_view);
        btnConfirmar = (Button) findViewById(R.id.btn_confirmar);
        btnBorrar = (Button) findViewById(R.id.btn_borrar);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar);

        btnConfirmar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        // Valida si se recibe la imagen de la firma y la muestra
        if (getIntent().hasExtra(INTENT_EXTRA_FIRMA)) {
            final Bitmap imagenFirma;
            try {
                imagenFirma = (Bitmap) FileUtils.obtenerImagenTemporal(
                        getIntent().getStringExtra(INTENT_EXTRA_FIRMA)
                );
                firmaView.setImage(imagenFirma);
            } catch (FileNotFoundException e) {
                Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error cargando imagen de firma", e);
                mostrarMensaje(R.string.msj_error, MSJ_ERROR);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirmar:
                try {
                    // Se guarda la imagen de la firma y se agrega el nombre al resultado de la actividad
                    FileUtils.guardarImagenTemporal(
                            firmaView.getImage(),
                            NOMBRE_IMAGEN_FIRMA
                    );
                    Intent data = getIntent();//Intent data = new Intent();
                    data.putExtra("data", NOMBRE_IMAGEN_FIRMA);
                    setResult(RESULT_OK, data);
                    finish();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } catch (IOException e) {
                    Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando imagen de firma", e);
                    mostrarMensaje(R.string.msj_error, MSJ_ERROR);
                }

                break;

            case R.id.btn_borrar:
                firmaView.clearSignature();

                break;

            case R.id.btn_cancelar:
                setResult(RESULT_CANCELED);
                finish();

                break;
        }
    }


}
