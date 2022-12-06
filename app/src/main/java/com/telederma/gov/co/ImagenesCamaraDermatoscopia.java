package com.telederma.gov.co;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.telederma.gov.co.adapters.CheckedImageArrayAdapter;
import com.telederma.gov.co.dialogs.VerFirmaUsuarioDialog;
import com.telederma.gov.co.fragments.BaseFragment;
import com.telederma.gov.co.fragments.PatologiaFragment;
import com.telederma.gov.co.fragments.RegistrarAnexoFragment;
import com.telederma.gov.co.fragments.ResumenFragment;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.CheckedImage;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.ImagenAnexo;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.views.ExtendableGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.Gravity.CENTER;

public class ImagenesCamaraDermatoscopia extends BaseActivity implements View.OnClickListener {
    View root_view;
    ArrayList<String> imageUrls;
    ExtendableGridView gv;
    Bundle arguments;
    Button btn_guardar;
    Map<Integer, Integer> list_imagenes_lesion;
    HashMap<Integer,List<String>> imagenes_partes_lesiones = new HashMap<Integer,List<String>>();
    HashMap<Integer,List<CheckedImage>> imagenes_check = new HashMap<Integer,List<CheckedImage>>();
    LinearLayout contenedor_lesiones;
    ScrollView scrollImagenes;
    final Integer maxImage = 2 ;
    public static final String ACTION_IMAGENES_CAMARA_DERMATOSCOPIA = "com.telederma.gov.co.ImagenesCamaraDermatoscopia";
    public static final int REQUEST_CODE_AGREGAR_IMAGENES = 400;
    public static final int RESULT_CODE_EXITO_AGRGANDO_IMAGENES = 401;
    public static final String ARG_IMAGEN_NOMBRE_PARTE = "imagen_nombre_parte";
    private String nombreCuerpo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        setContentView(R.layout.imagenes_camara_dermatoscopia);

       // Glide.get(this).getBitmapPool().clearMemory();



        //Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        initView();

    }

    public void initView(){


        if(findViewById(R.id.toolbar)!=null)
        {
            Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
            setTitle(R.string.dermatoscopia_visualizar_imagenes);
            setSupportActionBar(toolbar);
        }

        TextView header = findViewById(R.id.text_header_register);
        if(header != null )
            header.setText(getResources().getString(R.string.nueva_consulta_registro_paso4));
        contenedor_lesiones = findViewById(R.id.contenedor_lesiones);
        list_imagenes_lesion = new HashMap<>();
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        imageUrls = getIntent().getStringArrayListExtra("imagenes");


        if (imageUrls.size() > 0) {

            gv = new ExtendableGridView(getApplicationContext());
            crearListaImagenesNuevas(gv, imageUrls);
            gv.setNumColumns(2);
            gv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            contenedor_lesiones.addView(gv);
            list_imagenes_lesion.put(0, gv.getId());
        }

        btn_guardar.setOnClickListener(this);


        if(getIntent().hasExtra(ARG_IMAGEN_NOMBRE_PARTE)){
            this.nombreCuerpo = getIntent().getStringExtra(ARG_IMAGEN_NOMBRE_PARTE);
        }

        scrollImagenes = findViewById(R.id.scroll_imagenes);
        scrollImagenes.post(() -> scrollImagenes.fullScroll(View.FOCUS_DOWN));

    }

    private void initializarGrid(ExtendableGridView grid, List<CheckedImage> images) {
        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(this, images);
        grid.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_guardar:
                CheckedImageArrayAdapter adapter = ((CheckedImageArrayAdapter) (gv).getAdapter());
                List<CheckedImage> lista_imagenes = adapter.getCheckedItem();
                if(lista_imagenes.size() > maxImage){
                    Toast.makeText(this, getResources().getString(R.string.nueva_consulta_mensaje_maximo_dermatoscopia) , Toast.LENGTH_SHORT).show();
                } else if(lista_imagenes.size() < 2){
                    Toast.makeText(this, String.format(getResources().getString(R.string.nueva_consulta_registro_imagenes_validacion), "2"), Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    for(int i = 0 ; i< lista_imagenes.size();i++)
                        list.add(lista_imagenes.get(i).getUrl());
                    Intent intent = new Intent();
                    intent.putExtra("imagenes_dermatoscopia", list);
                    setResult(RESULT_CODE_EXITO_AGRGANDO_IMAGENES, intent);
                    finish();
                }


                break;

        }
    }


    private void crearListaImagenesNuevas(ExtendableGridView gv, ArrayList<String> imageUrls) {
        ArrayList<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < imageUrls.size(); i++)
            lstItem.add(new CheckedImage(false, imageUrls.get(i)));
        lstItem.add(new CheckedImage(false, "captura_imagen_dermatoscopia"));
        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(this, lstItem);
        gv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_save_photos:
//                savePhotos();
//                return true;
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}





