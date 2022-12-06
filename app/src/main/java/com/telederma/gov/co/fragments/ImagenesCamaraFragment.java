package com.telederma.gov.co.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.Camara;
import com.telederma.gov.co.ImagenDermatoscopia;
import com.telederma.gov.co.MainActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CheckedImageArrayAdapter;
import com.telederma.gov.co.dialogs.VerFirmaUsuarioDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.view.Gravity.CENTER;

public class ImagenesCamaraFragment extends BaseFragment implements View.OnClickListener {
    View root_view;
    ArrayList<String> imageUrlsView;
    ArrayList<String> imageUrls;
    ExtendableGridView gv;
    Integer id_consulta, id_parte;
    Bundle arguments;
    Button btn_siguiente, btn_resumen;
    LinearLayout btn_registrar;
    Map<Integer, Integer> list_imagenes_lesion;
    HashMap<Integer, List<String>> imagenes_partes_lesiones = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<CheckedImage>> imagenes_check = new HashMap<Integer, List<CheckedImage>>();
    LinearLayout contenedor_lesiones;
    String name_parte;
    Boolean resumen = false;
    ScrollView scrollImagenes;
    final Integer maxImage = 10;
    JSONObject imagenes_dermatoscopia;
    JSONObject keys_imagen_nombre;
    int cantidadImagenesRegistradas = 0;
    int keyAgregarImagen = -1;

    public static final String ARG_ID_CONSULTA = "id_consulta";
    public static final String ARG_IMAGENES = "imagenes";
    public static final String ARG_ID_PARTE = "id_parte";
    public static final String ARG_NAME_PARTE = "name_parte";
    public static final String ARG_IMAGENES_LESION = "imagenes_lesion";
    public static final String ARG_ID_CONTENT = "id_content";
    public static final String ARG_ID_NAME_IMAGE = "id_name_image";

    public static final String ARG_ID_CONTROL = "modelo";
    public static final String ARG_ID_IMAGENES_CONTROL = "imagenes_control";
    public static final String ARG_VIEW = "tipo_view";

    public static final int REQUEST_CODE_IMAGEN = 200;
    public static final int RESULT_CODE_ELIMINAR_IMAGEN = 201;
    public static final int RESULT_CODE_EXITO_AGRGANDO_IMAGENES = 401;
    public static final String ARG_URL_IMAGEN = "url_imagen";
    public static final String ARG_POS_IMAGEN = "pos_imagen";
    private JSONObject imagenesNombreParte;
    public static final String ARG_IMAGEN_NOMBRE_PARTE = "imagen_nombre_parte";

    @Override
    protected int getIdLayout() {
        return R.layout.imagenes_camara;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);

        inicializarComponentes();

        return root_view;
    }

    private void inicializarComponentes(){
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        if (header != null)
            header.setText(getResources().getString(R.string.nueva_consulta_registro_paso4));
        contenedor_lesiones = root_view.findViewById(R.id.contenedor_lesiones);
        contenedor_lesiones.removeAllViews();
        list_imagenes_lesion = new HashMap<>();
        btn_registrar = (LinearLayout) root_view.findViewById(R.id.btn_registrar_nueva_lesion);
        btn_siguiente = (Button) root_view.findViewById(R.id.btn_siguiente);
        btn_resumen = (Button) root_view.findViewById(R.id.btn_resumen);
        arguments = getArguments();
        imageUrls = arguments.getStringArrayList(ARG_IMAGENES);
        imageUrlsView = new ArrayList<>();
        id_consulta = arguments.getInt(ARG_ID_CONSULTA);
        id_parte = Integer.parseInt(arguments.getString(ARG_ID_PARTE));
        name_parte = arguments.getString(ARG_NAME_PARTE);
        this.imagenes_dermatoscopia = new JSONObject();
        this.keys_imagen_nombre = new JSONObject();

        if (arguments.containsKey("imagenes_dermatoscopia")) {
            try {
                this.imagenes_dermatoscopia = new JSONObject(arguments.getString("imagenes_dermatoscopia"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e("DERMAOSCOPIA", this.imagenes_dermatoscopia.toString());

        if (arguments.containsKey(ARG_ID_NAME_IMAGE)) {
            try {
                this.keys_imagen_nombre = new JSONObject(arguments.getString(ARG_ID_NAME_IMAGE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imagenesNombreParte = new JSONObject();
        if (arguments.getSerializable(ARG_IMAGENES_LESION) != null) {
            imagenes_partes_lesiones = (HashMap<Integer, List<String>>) arguments.getSerializable(ARG_IMAGENES_LESION);
            for (Map.Entry<Integer, List<String>> datoLesion : imagenes_partes_lesiones.entrySet()) {
                List<CheckedImage> imageChecked = new ArrayList<>();
                for (String ruta : datoLesion.getValue()) {
                    imageChecked.add(new CheckedImage(true, ruta));
                    imageUrlsView.add(ruta);
                    cantidadImagenesRegistradas++;
                }

                imageChecked.add(new CheckedImage(false, "captura_imagen___" + datoLesion.getKey()));
                imagenes_check.put(datoLesion.getKey(), imageChecked);
            }
            cargar_lesiones(imagenes_check);
        }

        if (imageUrls.size() > 0) {

            //ExtendableGridView gv = new ExtendableGridView(contexto);
            gv = new ExtendableGridView(contexto);
            crearListaImagenesNuevas(gv, imageUrls);
            gv.setNumColumns(2);
            gv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            gv.setId(id_parte);
            contenedor_lesiones.addView(crearEncabezado(name_parte));
            contenedor_lesiones.addView(gv);
            list_imagenes_lesion.put(0, gv.getId());
        }

        btn_registrar.setOnClickListener(this);
        btn_siguiente.setOnClickListener(this);
        btn_resumen.setOnClickListener(this);


        resumen = arguments.getBoolean("resumen");

        if (resumen) {
            btn_siguiente.setVisibility(View.GONE);
            btn_resumen.setVisibility(View.VISIBLE);
        } else {
            btn_siguiente.setVisibility(View.VISIBLE);
            btn_resumen.setVisibility(View.GONE);
        }

        scrollImagenes = root_view.findViewById(R.id.scroll_imagenes);
        scrollImagenes.post(() -> scrollImagenes.fullScroll(View.FOCUS_DOWN));
    }


    private void initializarGrid(ExtendableGridView grid, List<CheckedImage> images, String name) {
        //CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(getActivity(), images);
        //grid.setAdapter(adapter);


        ArrayList<String> imagenes = new ArrayList<>();

        for (CheckedImage imagen : images) {
            imagenes.add(imagen.getUrl());
            try {
                imagenesNombreParte.put(imagen.getUrl(), name);
            } catch (Exception e) {

            }
        }

        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(getActivity(), images, this.imagenes_dermatoscopia, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent();
                CheckedImageArrayAdapter.ViewHolder holder = (CheckedImageArrayAdapter.ViewHolder) parent.getTag();
                keyAgregarImagen = -1;

                List<String> imagenes = imagenesSeccion(holder.url);
                int cantidadImagenes = (imagenes == null ) ? -1 : imagenes.size();

                if (holder.position == cantidadImagenes) {
                    final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
                    intent.putExtra(ARG_ID_PARTE, id_parte);
                    ArrayList<String> imagenesCamara = new ArrayList<>(imagenes);

                    try{
                        intent.putExtra(ARG_NAME_PARTE, imagenesNombreParte.getString(imagenesCamara.get(0)));
                    }catch (Exception e){

                    }

                    intent.putExtra("verBotonesDermatoscopia", true);
                    intent.putExtra("anadirImagen", true);
                    intent.putExtra("imageUrls", imagenesCamara);
                    intent.putExtra("imagenes_dermatoscopia", imagenes_dermatoscopia.toString());
                    keyAgregarImagen = Integer.parseInt(holder.url.split("___")[1]);

                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 300);
                } else {

                    openShowImageDermatoscopia(imageUrlsView, posicionImagen(holder.url), true);
                }
            }
        });
        grid.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btn_registrar_nueva_lesion:
                if (saveLesion(false)) {
                    fragment = new PatologiaFragment();
                    nextView(fragment, false);
                }
                break;
            case R.id.btn_siguiente:
                if (saveLesion(true)) {
                    if (resumen) {
                        fragment = new ResumenFragment();
                        nextView(fragment, true);
                    } else {
                        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
                        if (header != null) {
                            fragment = new RegistrarAnexoFragment();
                            nextView(fragment, true);
                        } else {

                            if(arguments.getInt(ARG_VIEW, 0) == 1 || arguments.getInt(ARG_VIEW, 0) == 2){
                                fragment = new ResumenFragment();
                                nextView(fragment, true);
                            } else {
                                VerFirmaUsuarioDialog dialog = new VerFirmaUsuarioDialog(contexto);
                                //if (arguments.getInt(ARG_VIEW, 0) == 1)
                                //    dialog.setOnEnviarClickListener(v -> saveControl());
                                //if (arguments.getInt(ARG_VIEW, 0) == 2)
                                //    dialog.setOnEnviarClickListener(v -> saveRequerimiento());
                                if (arguments.getInt(ARG_VIEW, 0) == 3)
                                    dialog.setOnEnviarClickListener(v -> saveControlEnfermeria());
                                dialog.show();
                            }
                        }
                    }

                }
                break;
            case R.id.btn_resumen:
                if (saveLesion(true)) {
                    fragment = new ResumenFragment();
                    nextView(fragment, true);
                }
                break;
        }
    }

    public void nextView(Fragment fragment, Boolean encolar) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        arguments.putBoolean("lesiones_registradas", true);
        arguments.putSerializable("imagenes_lesion", imagenes_partes_lesiones);
        arguments.putString("imagenes_dermatoscopia", imagenes_dermatoscopia.toString());
        arguments.putStringArrayList(ARG_IMAGENES, new ArrayList<String>());
        fragment.setArguments(arguments);
        if (encolar)
            ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK).commit();
        else
            ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
        ocultarMensajeEspera();
    }

    public void cargar_lesiones(HashMap<Integer, List<CheckedImage>> imagenes_lesiones) {

        for (Map.Entry<Integer, List<CheckedImage>> datoLesion : imagenes_lesiones.entrySet()) {
            //ExtendableGridView gv = new ExtendableGridView(contexto);
            gv = new ExtendableGridView(contexto);
            gv.setNumColumns(2);
            gv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            //gv.setId(datoLesion.getKey() + contenedor_lesiones.getChildCount()); // SebasP: Comentada esta linea por que ocasionaba error aumentando el id de las partes del cuerpo
                gv.setId(datoLesion.getKey());
            list_imagenes_lesion.put(datoLesion.getKey(), gv.getId());

            String name = "";
            try{
                name = keys_imagen_nombre.getString("" + datoLesion.getKey());
            }catch (Exception e){

            }


            initializarGrid(gv, datoLesion.getValue(), name);

            try {
                //ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(datoLesion.getKey().toString());
                //if (parte != null)
                contenedor_lesiones.addView(crearEncabezado(name));




            } catch (Exception e) {

            }

            contenedor_lesiones.addView(gv);
        }
    }

    private void crearListaImagenesNuevas(ExtendableGridView gv, ArrayList<String> imageUrls) {
        ArrayList<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < imageUrls.size(); i++) {
            lstItem.add(new CheckedImage(false, imageUrls.get(i)));
            imageUrlsView.add(imageUrls.get(i));
            try {
                imagenesNombreParte.put(imageUrls.get(i), name_parte);
            } catch (Exception e) {

            }
        }
        lstItem.add(new CheckedImage(false, "captura_imagen"));
        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(getActivity(), lstItem, this.imagenes_dermatoscopia, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent();
                CheckedImageArrayAdapter.ViewHolder holder = (CheckedImageArrayAdapter.ViewHolder) parent.getTag();


                keyAgregarImagen = -1;

                if (holder.position == imageUrls.size()) {
                    final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
                    intent.putExtra(ARG_ID_PARTE, id_parte);
                    intent.putExtra("verBotonesDermatoscopia", true);
                    intent.putExtra("anadirImagen", true);
                    intent.putExtra("imageUrls", imageUrls);

                    try{
                        intent.putExtra(ARG_NAME_PARTE, imagenesNombreParte.getString(imageUrls.get(0)));
                    }catch (Exception e){

                    }

                    intent.putExtra("imagenes_dermatoscopia", imagenes_dermatoscopia.toString());

                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 300);
                } else {

                    openShowImageDermatoscopia(imageUrlsView, posicionImagen(holder.url), true);
                }
            }
        });
        gv.setAdapter(adapter);
    }

    public List<String> imagenesSeccion(String path){

        try{
            int key = Integer.parseInt(path.split("___")[1]);
            imagenes_partes_lesiones = (HashMap<Integer, List<String>>) arguments.getSerializable(ARG_IMAGENES_LESION);
            return imagenes_partes_lesiones.get(key);

        } catch (Exception e){
            return null;
        }


    }

    public int posicionImagen(String path){

        try{
            imagenes_partes_lesiones = (HashMap<Integer, List<String>>) arguments.getSerializable(ARG_IMAGENES_LESION);

            for (int i = 0; i < imageUrlsView.size(); i++) {
                if(imageUrlsView.get(i).equals(path)){
                    return i;
                }
            }
            return 0;
        } catch (Exception e){
            return 0;
        }


    }


    private LinearLayout crearEncabezado(String nombre_parte) {
        LinearLayout contenedor = new LinearLayout(contexto);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 10);
        contenedor.setLayoutParams(layoutParams);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        contenedor.setGravity(CENTER);
        contenedor.setVerticalGravity(CENTER);
        contenedor.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        TextView text_nombre_parte = new TextView(contexto);
        text_nombre_parte.setTextColor(Color.WHITE);
        text_nombre_parte.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text_nombre_parte.setText(nombre_parte);
        contenedor.setPadding(20, 20, 20, 20);
        contenedor.addView(text_nombre_parte);
        return contenedor;
    }

    public Boolean saveLesion(Boolean siguiente) {
        CheckedImageArrayAdapter adapter = null;
        imagenes_partes_lesiones = new HashMap<>();

        int numeroImagenesTotales = 0;
        int numeroDermatoscopicasTotales = 0;
        for (Map.Entry<Integer, Integer> datoLesion : list_imagenes_lesion.entrySet()) {
            adapter = ((CheckedImageArrayAdapter) ((ExtendableGridView) root_view.findViewById(datoLesion.getValue())).getAdapter());
            List<CheckedImage> lista_imagenes = adapter.getCheckedItem();

            int numeroImagenes = lista_imagenes.size();
            int numeroDermatoscopicas = obtenerNumeroDermatoscopicas(lista_imagenes);

            numeroImagenesTotales =  numeroImagenesTotales + numeroImagenes;
            numeroDermatoscopicasTotales = numeroDermatoscopicasTotales + numeroDermatoscopicas;

            if(numeroImagenes > 1 || numeroDermatoscopicas > 0){
                if((numeroImagenes + numeroDermatoscopicas) <= 10){
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < lista_imagenes.size(); i++)
                        list.add(lista_imagenes.get(i).getUrl().toString());
//
                    if (imagenes_partes_lesiones.get(datoLesion.getValue()) == null)
                        imagenes_partes_lesiones.put(datoLesion.getValue(), new ArrayList<>());
                    imagenes_partes_lesiones.get(datoLesion.getValue()).addAll(list);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.nueva_consulta_mensaje_maximo_total_dermatoscopia), Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(getActivity(), String.format(getResources().getString(R.string.nueva_consulta_registro_imagenes_validacion), "2"), Toast.LENGTH_SHORT).show();
                return false;
            }


        }

        if(siguiente) {
            if((numeroImagenesTotales + numeroDermatoscopicasTotales) > 10){
                Toast.makeText(getActivity(), getResources().getString(R.string.nueva_consulta_mensaje_maximo_total_dermatoscopia), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public int obtenerNumeroDermatoscopicas(List<CheckedImage> lista) {

        int cantidad = 0;
        for (int i = 0; i < lista.size(); i++) {
            try {
                if (imagenes_dermatoscopia.has(lista.get(i).getUrl())) {
                    cantidad += imagenes_dermatoscopia.getJSONArray(lista.get(i).getUrl()).length();
                }
            } catch (Exception e) {

            }
        }


        return cantidad;
    }

    public void saveControl() {
        ControlMedico control_medico = dbUtil.getControlMedico(String.valueOf(arguments.getSerializable(ARG_ID_CONTROL)));
        final Session session = Session.getInstance(getActivity());
        PendienteSincronizacion pendiente = new PendienteSincronizacion();


        ArchivosSincronizacion archivo = new ArchivosSincronizacion();
        if (control_medico.getAudioAnexo() != null) {
            archivo.setId_local(control_medico.getId() + "");
            archivo.setTable(control_medico.NOMBRE_TABLA);
            archivo.setField(control_medico.NOMBRE_CAMPO_AUDIO_ANEXO);
            archivo.setPath(control_medico.getAudioAnexo());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }

        if (control_medico.getAudioClinica() != null) {
            //////////////
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(control_medico.getId() + "");
            archivo.setTable(control_medico.NOMBRE_TABLA);
            archivo.setField(control_medico.NOMBRE_CAMPO_AUDIO_CLINICA);
            archivo.setPath(control_medico.getAudioClinica());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }

        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(String.valueOf(control_medico.getId()));
        pendiente.setTable(ControlMedico.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);

        ArrayList<String> list_im_anexo = arguments.getStringArrayList(ARG_ID_IMAGENES_CONTROL);
        Integer id_anexo = null;

        for (String ruta : list_im_anexo) {
            ImagenAnexo ia = new ImagenAnexo();
            ia.setIdControlLocal(String.valueOf(control_medico.getId()));
            ia.setImagenAnexo(ruta);
            id_anexo = dbUtil.crearImagenAnexo(ia);
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(id_anexo + "");
            archivo.setTable(ImagenAnexo.NOMBRE_TABLA);
            archivo.setField(ImagenAnexo.NOMBRE_CAMPO_IMAGEN_ANNEXO);
            archivo.setPath(ruta);
            archivo.setExtension("jpg");
            dbUtil.crearArchivoSincronizacion(archivo);

            if (id_anexo > 0) {
                pendiente = new PendienteSincronizacion();
                pendiente.setId_local(id_anexo.toString());
                pendiente.setTable(ImagenAnexo.NOMBRE_TABLA);
                pendiente.setEmail(session.getCredentials().getEmail());
                pendiente.setToken(session.getCredentials().getToken());
                pendiente.setRegistration_date(new Date());
                dbUtil.crearPendienteSincronizacion(pendiente);
            }
        }
//        if (list_im_anexo.size() > 0) {
//            pendiente = new PendienteSincronizacion();
//            pendiente.setId_local(id_anexo.toString());
//            pendiente.setTable(ImagenAnexo.NOMBRE_TABLA);
//            pendiente.setEmail(session.getCredentials().getEmail());
//            pendiente.setToken(session.getCredentials().getToken());
//            pendiente.setRegistration_date(new Date());
//            dbUtil.crearPendienteSincronizacion(pendiente);
//        }
        saveLesion(Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL, control_medico.getId());


        List<Lesion> lesiones = new ArrayList<>();
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesiones(control_medico.getId(), Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
        for (int i = 0; i < lesiones.size(); i++) {
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }
        startActivity(new Intent(getActivity(), MainActivity.class));

    }

    public Boolean saveLesion(String field, Integer... params) {
        Integer lesion_id = 0;
        Lesion lesion = null;
        for (Map.Entry<Integer, List<String>> datoLesion : imagenes_partes_lesiones.entrySet()) {

            lesion = new Lesion();
            lesion.setIdBodyArea(datoLesion.getKey());
            switch (field) {
                case Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL:
                    lesion.setIdControlLocal(params[0]);
                    break;
                case Lesion.NOMBRE_CAMPO_ID_REQUERIMIENTO:
                    lesion.setIdRequerimiento(params[0]);
                    lesion.setIdConsulta(params[1]);
                    lesion.setIdControlConsulta(params[2]);
                    break;
            }
            ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(datoLesion.getKey().toString());
            if (parte != null)
                lesion.setNameArea(parte.getNombre());
            dbUtil.crearLesion(lesion);
            //crearImagenesLesion(datoLesion.getValue(), lesion.getId());
            // TODO: 2/13/19 revisar este cambio con Orli 
            crearImagenesLesion(datoLesion.getValue(), lesion.getId());
            crearImagenesDermastoscopiaLesion(imagenes_dermatoscopia, lesion.getId());
        }
        return true;
    }

    public void crearImagenesLesion(List<String> lista_imagenes, Integer lesion_id) {
        for (int i = 0; i < lista_imagenes.size(); i++) {
            ImagenLesion imagenLesion = new ImagenLesion();
            imagenLesion.setPhoto(lista_imagenes.get(i));
            imagenLesion.setDescription("");
            imagenLesion.setId_injury_local(lesion_id);
            dbUtil.crearImagenLesion(imagenLesion);
        }
    }

    public void crearImagenesDermastoscopiaLesion(JSONObject lista_imagenes, Integer lesion_id) {
        try {

            Iterator<?> keys = lista_imagenes.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                List<ImagenLesion> imagenes = dbUtil.getImagenesDermatoscopiaLesion(lesion_id, key);
                if (imagenes.size() > 0) {
                    JSONArray imagenesDermatoscopia = lista_imagenes.getJSONArray(key);

                    for (int i = 0; i < imagenesDermatoscopia.length(); i++) {
                        String path = imagenesDermatoscopia.getString(i);

                        ImagenLesion imagenLesion = new ImagenLesion();
                        imagenLesion.setPhoto(path);
                        imagenLesion.setDescription("");
                        imagenLesion.setId_injury_local(lesion_id);
                        imagenLesion.setImage_injury_id(imagenes.get(0).getId());
                        dbUtil.crearImagenLesion(imagenLesion);
                    }
                }

            }


        } catch (Exception e) {

        }


    }


    public void saveRequerimiento() {
        //Note: El requerimiento siempre tiene IdServidor por que llega desde el. Es creado por el especialista
        Requerimiento requerimiento = dbUtil.getRequerimiento(String.valueOf(arguments.getInt(ARG_ID_CONTROL)));
        final Session session = Session.getInstance(getActivity());
        ArchivosSincronizacion archivo = new ArchivosSincronizacion();
        if (requerimiento.getAudioClinica() != null) {
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(requerimiento.getIdServidor() + "");
            archivo.setTable(requerimiento.NOMBRE_TABLA);
            archivo.setField(requerimiento.NOMBRE_CAMPO_AUDIO_CLINICA);
            archivo.setPath(requerimiento.getAudioClinica());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }


        PendienteSincronizacion pendiente = new PendienteSincronizacion();
        // Nota: Sebastian Perez. Cambio este metodo por que al sincroniza se busca por el id del servidor
        pendiente.setId_local(requerimiento.getIdServidor().toString());
        //pendiente.setIdServidor(requerimiento.getIdServidor());
        pendiente.setTable(Requerimiento.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);
        // revisar almacenamiento: dbUtil.obtenerRequerimientoServidor("9")
        // revisar almacenamiento: dbUtil.obtenerSiguienteSincronizable()
        // revisar almacenamiento: dbUtil.obtenerSincronizables()
        // lista los controles de enfermería: dbUtil.getDbHelper().getControlEnfermeriaRuntimeDAO().queryForAll()
        saveLesion(Lesion.NOMBRE_CAMPO_ID_REQUERIMIENTO, requerimiento.getIdServidor(), requerimiento.getIdConsulta(), requerimiento.getIdControlMedico());

        List<Lesion> lesiones = new ArrayList<>();
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesiones(requerimiento.getIdServidor(), Lesion.NOMBRE_CAMPO_ID_REQUERIMIENTO);
        for (int i = 0; i < lesiones.size(); i++) {
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }
        startActivity(new Intent(getActivity(), MainActivity.class));

    }


    public void saveControlEnfermeria() {
        ControlEnfermeria control_medico = dbUtil.getControlEnfermeria(String.valueOf(arguments.getInt(ARG_ID_CONTROL)));
        ArchivosSincronizacion archivo = new ArchivosSincronizacion();
        Integer id_control = dbUtil.crearControlEnfermeria(control_medico);
        final Session session = Session.getInstance(getActivity());
        PendienteSincronizacion pendiente = new PendienteSincronizacion();

        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(id_control.toString());
        pendiente.setTable(ControlEnfermeria.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);
        saveLesion(Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL, id_control);


        List<Lesion> lesiones = new ArrayList<>();
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesiones(id_control, Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
        for (int i = 0; i < lesiones.size(); i++) {
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }
        startActivity(new Intent(getActivity(), MainActivity.class));

    }

    public void openShowImageDermatoscopia(ArrayList<String> imagenes, int posicion, Boolean showButtonsDermoscopy) {

        try {
            Intent intent = new Intent(ImagenDermatoscopia.ACTION_IMAGEN_DERMATOSCOPIA);
            intent.putExtra(ARG_URL_IMAGEN, imagenes);
            intent.putExtra(ARG_POS_IMAGEN, posicion);
            intent.putExtra(ARG_IMAGEN_NOMBRE_PARTE, imagenesNombreParte.toString());
            intent.putExtra("showButtonsDermoscopy", showButtonsDermoscopy);
            intent.putExtra("imagenes_dermatoscopia", this.imagenes_dermatoscopia.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, REQUEST_CODE_IMAGEN);


        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGEN) {
            if (resultCode == RESULT_CODE_ELIMINAR_IMAGEN) {
                String urlImagen = data.getStringExtra("url_imagen");

                eliminarImagen(urlImagen);
                inicializarComponentes();
            }
            if (resultCode == RESULT_CODE_EXITO_AGRGANDO_IMAGENES) {

                try {
                    String urlImagen = data.getStringExtra("url_imagen");
                    ArrayList<String> imagenes_dermatoscopia = data.getStringArrayListExtra("imagenes_dermatoscopia");
//
                    JSONArray imagenes = new JSONArray();
                    for (String imagen : imagenes_dermatoscopia) {
                        imagenes.put(imagen);
                    }
                    this.imagenes_dermatoscopia.put(urlImagen, imagenes);
                    Log.e("DERMATOSCOPICAS_1", this.imagenes_dermatoscopia.toString());
                    //crearListaImagenesNuevas(gv, imageUrls);
                    arguments.putString("imagenes_dermatoscopia",this.imagenes_dermatoscopia.toString());
                    this.setArguments(arguments);
                    inicializarComponentes();
                } catch (Exception e) {
//
                }
            }
        } else if (requestCode == 300) {
            if (resultCode == 200) {
                try {

                    if(data.hasExtra("imagenes_dermatoscopia")){
                        //this.imagenes_dermatoscopia = new JSONObject(data.getStringExtra("imagenes_dermatoscopia"));
                        arguments.putString("imagenes_dermatoscopia", data.getStringExtra("imagenes_dermatoscopia"));
                    }

                    this.imageUrls = data.getStringArrayListExtra("imagenes");
                    if(this.keyAgregarImagen == -1){
                        arguments.putStringArrayList(ARG_IMAGENES, this.imageUrls);
                    } else {
                        imagenes_partes_lesiones.get(this.keyAgregarImagen).clear();
                        imagenes_partes_lesiones.get(this.keyAgregarImagen).addAll(this.imageUrls);
                        arguments.putSerializable("imagenes_lesion", imagenes_partes_lesiones);
                    }
                    //crearListaImagenesNuevas(gv, imageUrls);
                    this.setArguments(arguments);
                    inicializarComponentes();
                } catch (Exception e) {

                }

            }
        }
    }

    public void eliminarImagen(String urlImagen) {

        if(imageUrls.contains(urlImagen)){
            imageUrls.remove(urlImagen);
            arguments.putStringArrayList(ARG_IMAGENES, this.imageUrls);
        } else {
            for (int key : imagenes_partes_lesiones.keySet()) {
                imagenes_partes_lesiones.get(key).remove(urlImagen);
            }
            arguments.putSerializable("imagenes_lesion", imagenes_partes_lesiones);
        }

        this.setArguments(arguments);

        //crearListaImagenesNuevas(gv, imageUrls);



    }
}





