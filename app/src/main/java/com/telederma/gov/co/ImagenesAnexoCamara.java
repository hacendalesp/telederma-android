package com.telederma.gov.co;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.telederma.gov.co.adapters.CheckedImageArrayAdapter;
import com.telederma.gov.co.dialogs.ConfirmDialog;
import com.telederma.gov.co.modelo.CheckedImage;
import com.telederma.gov.co.views.ExtendableGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagenesAnexoCamara extends BaseActivity implements View.OnClickListener {
    ArrayList<String> imageUrls;
    Integer id_consulta;
    Button btn_siguiente;
    Map<Integer, Integer> list_imagenes_anexo;
    LinearLayout contenedor_imagenes_anexo;
    private static final int REQUEST_IMAGENES_ANEXO = 201;
    public static final String ACTION_ANEXO = "com.telederma.gov.co.ImagenesAnexoCamara";
    final Integer maxImage = 10 ;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagenes_anexo_camara);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.imagenes_anexos);

        contenedor_imagenes_anexo = findViewById(R.id.contenedor_anexo_imagenes);
        list_imagenes_anexo = new HashMap<>();
        btn_siguiente = (Button) findViewById(R.id.btn_siguiente);

        imageUrls =  getIntent().getStringArrayListExtra("imagenes_anexo");
        id_consulta =  getIntent().getIntExtra("id_consulta",0);
        if (imageUrls.size() > 0) {
            ExtendableGridView gv = new ExtendableGridView(getApplicationContext());
            crearListaImagenesNuevas(gv, imageUrls);
            gv.setNumColumns(2);
            gv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            gv.setId(list_imagenes_anexo.size());
            contenedor_imagenes_anexo.addView(gv);
            list_imagenes_anexo.put(0, gv.getId());
        }
        btn_siguiente.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_siguiente:
                Intent data = new Intent();
                ArrayList<String> imagenes = new ArrayList<String>();

                for (Map.Entry<Integer, Integer> datoLesion : list_imagenes_anexo.entrySet()) {
                    CheckedImageArrayAdapter adapter = ((CheckedImageArrayAdapter) ((ExtendableGridView) findViewById(datoLesion.getValue())).getAdapter());
                    List<CheckedImage> lista_imagenes = adapter.getCheckedItem();
                    if(lista_imagenes.size() <=maxImage)
                    {
                        for (CheckedImage imagen : lista_imagenes)
                            imagenes.add(imagen.getUrl());
                       /* List<CheckedImage> lista_no_selected_imagenes = adapter.getNotCheckedItem();
                        for (CheckedImage imagen : lista_no_selected_imagenes) {
                            File file = new File(imagen.getUrl());
                            if (file.exists())
                                file.delete();
                        }*/
                    }
                    else
                        {
                            Toast.makeText(this, String.format("%s:%s/", getResources().getString(R.string.nueva_consulta_cam_mensaje_maximo), maxImage.toString()) , Toast.LENGTH_SHORT).show();
                            return ;
                        }
                }

                if(imagenes.size() == 0){
                    String text = context.getString(R.string.imagenes_anexos_quieres_dejar_seleccion_imagenes_vacia);
                    ConfirmDialog confirm_dialog = new ConfirmDialog(context, R.string.dialog_confirm_cofirm, text);
                    confirm_dialog.show();
                    Button btn_send = confirm_dialog.btn_send;
                    Button btn_cancel = confirm_dialog.btn_cancel;
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            confirm_dialog.dismiss();
                        }
                    });
                    btn_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            confirm_dialog.dismiss();
                            data.putExtra("imagenes_anexo", imagenes);
                            setResult(REQUEST_IMAGENES_ANEXO, data);
                            finish();
                        }
                    });
                }else {
                    data.putExtra("imagenes_anexo", imagenes);
                    setResult(REQUEST_IMAGENES_ANEXO, data);
                    finish();
                    break;
                }
        }
    }

    private void crearListaImagenesNuevas(ExtendableGridView gv, ArrayList<String> imageUrls) {
        ArrayList<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < imageUrls.size(); i++)
            lstItem.add(new CheckedImage(false, imageUrls.get(i)));
        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(this, lstItem);
        gv.setAdapter(adapter);
    }


}




