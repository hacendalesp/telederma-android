package com.telederma.gov.co.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.modelo.CheckedImage;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.modelo.Municipio;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Utils;
import com.telederma.gov.co.views.ExtendableGridView;

import java.util.ArrayList;


public class VerTicketDialog extends TeledermaDialog {

    private HelpDesk helpDesk;
    private TextView asunto,descripcion,respuesta;
    ExtendableGridView gvImagen , gvImagenRespuesta;


    private View vAsunto,vDescripcion,vRespuesta,vImagen,vImagenRespuesta;

    public VerTicketDialog(@NonNull Context contexto,HelpDesk helpDesk) {
        super(contexto);
        this.helpDesk = helpDesk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inicializarViews();
    }


    private void inicializarViews() {
        asunto      = findViewById(R.id.id_acerca_de_asunto);
        descripcion = findViewById(R.id.id_acerca_de_descripcion);
        respuesta   = findViewById(R.id.id_acerca_de_respuesta);
        gvImagen = (ExtendableGridView) findViewById(R.id.id_acerca_de_imagen_respuesta);
        gvImagenRespuesta = (ExtendableGridView) findViewById(R.id.id_acerca_de_imagen);




        if (helpDesk.getSubject() != null) {
            vAsunto = findViewById(R.id.contenedor_acerca_de_asunto);
            vAsunto.setVisibility(View.VISIBLE);
            asunto.setText(helpDesk.getSubject());
        }
        if (helpDesk.getDescription() != null) {
            vDescripcion = findViewById(R.id.contenedor_acerca_de_descripcion);
            vDescripcion.setVisibility(View.VISIBLE);
            descripcion.setText(helpDesk.getDescription());
        }
        if (helpDesk.getResponse_ticket() != null) {
            vRespuesta = findViewById(R.id.contenedor_acerca_de_respuesta);
            vRespuesta.setVisibility(View.VISIBLE);
            respuesta.setText(helpDesk.getResponse_ticket());
        }
        if(helpDesk.getImage_user() != null && !helpDesk.getImage_user().isEmpty())
        {
            vImagen = findViewById(R.id.contenedor_acerca_de_imagen);
            vImagen.setVisibility(View.VISIBLE);
            ArrayList lstItemRespuesta = new ArrayList<CheckedImage>();
            lstItemRespuesta.add(helpDesk.getImage_user());
            ImageArrayAdapter adapterRespuesta = new ImageArrayAdapter(this.contexto, lstItemRespuesta);
            gvImagenRespuesta.setAdapter(adapterRespuesta);
        }
        if(helpDesk.getImage_admin() != null && !helpDesk.getImage_admin().isEmpty())
        {
            vImagenRespuesta = findViewById(R.id.contenedor_acerca_de_imagen_respuesta);
            vImagenRespuesta.setVisibility(View.VISIBLE);
            ArrayList lstItem = new ArrayList<CheckedImage>();
            lstItem.add(helpDesk.getImage_admin());
            ImageArrayAdapter adapter = new ImageArrayAdapter(this.contexto, lstItem);
            gvImagen.setAdapter(adapter);
        }
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_ver_ticket;
    }

}
