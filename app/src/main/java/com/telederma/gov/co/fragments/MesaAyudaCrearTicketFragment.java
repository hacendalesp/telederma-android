package com.telederma.gov.co.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.telederma.gov.co.Camara;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.CheckedImage;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.utils.Utils;
import com.telederma.gov.co.views.ExtendableGridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Created by Sebastián Noreña.
 */

public class MesaAyudaCrearTicketFragment extends BaseFragment implements  View.OnClickListener{

    private EditText txtAsunto,txtDescripcion;
    private Button btnEnviar,btn_tomar_foto,btn_adjuntar;
    private static final int REQUEST_IMAGEN_MESA_AYUDA    = 201;
    private static final int REQUEST_ADJUNTAR_IMAGEN      = 202;
    private String id_image = "";
    View f_descripcion_ticket;
    Speech speech=null;
    View rootView ;
    ExtendableGridView gv;
    ArrayList<String> listImage = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        inicializarViews(rootView);
        return rootView;
    }

    private void inicializarViews(View rootView) {
        speech = Speech.init(contexto, contexto.getPackageName());
        utils = Utils.getInstance(contexto);
        txtAsunto = rootView.findViewById(R.id.txt_asunto);
        f_descripcion_ticket = rootView.findViewById(R.id.f_descripcion_ticket);
        txtDescripcion = (EditText) f_descripcion_ticket.findViewById(R.id.ed_text);//root_view.findViewById(R.id.texto_examen_fisico);
        txtDescripcion.setHint(contexto.getString(R.string.mesa_ayuda_ingrese_descripcion));
        btn_tomar_foto = rootView.findViewById(R.id.btn_tomar_foto);
        btn_tomar_foto.setOnClickListener(this);
        btnEnviar = rootView.findViewById(R.id.btn_enviar);
        btn_adjuntar = rootView.findViewById(R.id.btn_adjuntar);
        btn_adjuntar.setOnClickListener(this);
        utils.speechText(f_descripcion_ticket);
        btnEnviar.setOnClickListener(view -> {
            mostrarMensajeEspera(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar sb) {
                    if(validar_campos())
                       enviar();
                }
            });
        });
    }

    private void enviar() {
        final Session session = Session.getInstance(contexto);

        String url_imagen = "";
        if(listImage.size()>0)
            url_imagen = listImage.get(0);
        HelpDesk helpDesk = new HelpDesk(
                txtAsunto.getText().toString(),
                txtDescripcion.getText().toString(),
                url_imagen,
                session.getCredentials().getIdUsuario()
        );
        dbUtil.crearMesaAyuda(helpDesk);
        PendienteSincronizacion pendiente = new PendienteSincronizacion();
        pendiente.setId_local(helpDesk.getId().toString());
        pendiente.setIdServidor(helpDesk.getIdServidor()); // por revisar
        pendiente.setTable(HelpDesk.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);

        if(listImage.size()>0)
        {
            ArchivosSincronizacion archivo = new ArchivosSincronizacion();
            archivo.setId_local(helpDesk.getId() + "");
            archivo.setTable(HelpDesk.NOMBRE_TABLA);
            archivo.setField(HelpDesk.NOMBRE_CAMPO_IMAGE_USER);
            archivo.setPath(new File(url_imagen).getAbsolutePath());
            archivo.setExtension("png");
            dbUtil.crearArchivoSincronizacion(archivo);
        }
        ocultarMensajeEspera();
        mostrarMensaje(R.string.acerca_de_mensaje, MSJ_INFORMACION);
        getActivity().onBackPressed();
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_mesa_ayuda_crear_ticket;
    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
        switch (view.getId()) {
            case R.id.btn_tomar_foto:
                intent.putExtra("cantidad_imagenes", 0);
                intent.putExtra("unica", true);
                startActivityForResult(intent, REQUEST_IMAGEN_MESA_AYUDA);
                break;
            case R.id.btn_adjuntar:
                Intent intent_adjuntar = new Intent();
                intent_adjuntar.setType("image/*");
                intent_adjuntar.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent_adjuntar, ""), REQUEST_ADJUNTAR_IMAGEN);
                break;



        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(speech != null)
            speech.shutdown();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGEN_MESA_AYUDA) {
            if (data.hasExtra("imagenes")) {
                ArrayList<String> imagenes = data.getExtras().getStringArrayList("imagenes");
                if (data.hasExtra("imagenes")) {
                    if (imagenes.size() > 0) {
                        listImage = new ArrayList<String>();
                        listImage.add(imagenes.get(0));
                        crearListaImagenes(listImage);
                    }
                }
            }
        }
        if (requestCode == REQUEST_ADJUNTAR_IMAGEN) {
            try
            {
                if (data != null) {
                     Uri selectedImageUri = data.getData();
                     String url = getRealPathFromURI(getContext(),selectedImageUri);
                     File source = new File(url);
                    id_image = String.format(Constantes.FORMATO_DIRECTORIO_ARCHIVO, Constantes.DIRECTORIO_TEMPORAL, Math.random()+".png");
                    File destination = new File(id_image);

                     FileUtils.copy(source,destination);
                     listImage = new ArrayList<String>();
                     listImage.add(id_image);
                     crearListaImagenes(listImage);
                }
            }
           catch (Exception e)
           {
           }
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        String wholeID = DocumentsContract.getDocumentId(contentUri);
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);
        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst())
            return cursor.getString(columnIndex);
            //return cursor.getString(columnIndex);

        cursor.close();
        return null ;
    }
    private void crearListaImagenes(ArrayList<String> imageUrls) {
        gv = (ExtendableGridView) rootView.findViewById(R.id.grid_imagenes);
        ArrayList lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < imageUrls.size(); i++)
            lstItem.add(imageUrls.get(i));
        ImageArrayAdapter adapter = new ImageArrayAdapter(getActivity(), lstItem);
        gv.setAdapter(adapter);
    }

    public boolean validar_campos() {
        return utils.validarCamposRequeridos(txtAsunto,txtDescripcion);
    }
}
