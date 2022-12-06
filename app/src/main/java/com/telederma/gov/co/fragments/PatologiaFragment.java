package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.Body;
import com.telederma.gov.co.Camara;
import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.patologia.AnatomyItem;
import com.telederma.gov.co.patologia.ItemClickListener;
import com.telederma.gov.co.patologia.NoteImageAdapterImpl;
import com.telederma.gov.co.patologia.NoteImageView;
import com.telederma.gov.co.utils.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


public class PatologiaFragment extends BaseFragment implements ItemClickListener<AnatomyItem> {

    public static final String ARG_GENERO            ="genero";
    public static final String ARG_PARTE_ID          ="parte_id";
    public static final String ARG_ID_PARTE          ="id_parte";
    public static final String ARG_NAME_PARTE        ="name_parte";
    public static final String ARG_CUERPO_FRENTE     ="cuerpo_frente";
    public static final String ARG_IMAGENES          ="imagenes";
    public static final String ARG_ID_CONTENT        ="id_content";
    public static final String ARG_ID_NAME_IMAGE     ="id_name_image";

    private List<AnatomyItem> pointsFront;
    private NoteImageView imagen_cuerpo;
    private Map<Integer, Integer> map_fondos;

    private static Bundle arguments;

    private EditText txt_nombre_parte;
    private Button btn_continuar;
    private Button btn_continuar_sin_imagenes;


    @Override
    protected int getIdLayout() {
        return R.layout.fragment_patologia;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if(getActivity().findViewById(R.id.toolbar_historia)!=null)
        {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
            toolbar.setTitle(getString(R.string.nueva_consulta_title_paso4));
            TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
            header.setText(getResources().getString(R.string.nueva_consulta_registro_paso4));
        }

        arguments = getArguments();

//        //todo: Remover este bloque que es de pruebas en el menú
//        if(arguments == null) {
//            arguments = new Bundle();
//            arguments.putInt(RegistrarRequerimientoFragment.ARG_VIEW, 2);
//            arguments.putBoolean("cuerpo_frente", true);
//            arguments.putBoolean("resumen", false);
//            arguments.putInt("genero", arguments.getInt("genre", 2));
//            arguments.putInt("id_examen", 305);
//            arguments.putInt("id_content", R.id.menu_content);
//            //arguments.putInt("id_content", 2131624359);
//            arguments.putInt("informacion_paciente", 25);
//            arguments.putString("cedula_paciente", "040319");
//            arguments.putInt("paciente_id_local", 31);
//            arguments.putInt("id_consulta", 305);
//            arguments.putInt("tipo_profesional", 1);
//        }

        Integer genero = arguments.getInt(ARG_GENERO, 1);
        Boolean cara = arguments.getBoolean(ARG_CUERPO_FRENTE);
        pointsFront = Body.getBody(genero ,cara,contexto);
        map_fondos = new HashMap<>();

        if(genero == 1 ) {
            map_fondos.put(1, R.id.imagen_de_frente_hombre);
            map_fondos.put(2, R.id.imagen_de_atras_hombre);
        } else {
            map_fondos.put(1, R.id.imagen_de_frente);
            map_fondos.put(2, R.id.imagen_de_atras);
        }

        imagen_cuerpo = rootView.findViewById(map_fondos.get(((cara)?1:2)));
        imagen_cuerpo.setVisibility(View.VISIBLE);
        imagen_cuerpo.setAdapter(new NoteImageAdapterImpl(pointsFront, getActivity(), this));
        imagen_cuerpo.setAllowTransparent(false);

        ImageView btn_back_body = rootView.findViewById(R.id.btn_back_body);
        btn_back_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft;
                Fragment fragment;
                ft = fm.beginTransaction();
                fragment = new PatologiaFragment();
                if (arguments.getBoolean(ARG_CUERPO_FRENTE)) {
                    arguments.putBoolean(ARG_CUERPO_FRENTE, false);
                    fragment.setArguments(arguments);
                    ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
                } else {
                    arguments.putBoolean(ARG_CUERPO_FRENTE, true);
                    fragment.setArguments(arguments);
                    ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
                }
            }
        });


        btn_continuar = rootView.findViewById(R.id.btn_continuar);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
                intent.putExtra(ARG_ID_PARTE, "182");
                intent.putExtra("verBotonesDermatoscopia", true);
                intent.putExtra(ARG_NAME_PARTE, txt_nombre_parte.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 200);

            }
        });

        btn_continuar_sin_imagenes = rootView.findViewById(R.id.btn_continuar_sin_imagenes);
        btn_continuar_sin_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new ResumenFragment();


                nextView(fragment, true);

            }
        });

        txt_nombre_parte = rootView.findViewById(R.id.txt_nombre_parte);
        txt_nombre_parte.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(txt_nombre_parte.getText().length() >= 3){
                    btn_continuar.setVisibility(View.VISIBLE);

                    if(getActivity().findViewById(R.id.toolbar_historia) == null)
                    {
                        btn_continuar_sin_imagenes.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_continuar.setVisibility(View.INVISIBLE);

                    if(getActivity().findViewById(R.id.toolbar_historia) == null)
                    {
                        btn_continuar_sin_imagenes.setVisibility(View.GONE);
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        return rootView;
    }

    public void nextView(Fragment fragment, Boolean encolar) {
        try {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(arguments);
            if (encolar)
                ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK).commit();
            else
                ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
            ocultarMensajeEspera();
        } catch (Exception e){
            Log.e("error", e.toString());
        }

    }

    /**
     * Define what happen when an item is clicked here we store taped item,
     * so we can display differently selected items
     */
    @Override
    public void onMapItemClick(AnatomyItem item) {
        //FragmentManager fm = getFragmentManager();
        //FragmentTransaction ft;
        //Fragment fragment;
//
        ////Dermatoscopia
        //item.setText("182");
//
        //if (Integer.parseInt(item.getText()) >= 200) {
        //    ft = fm.beginTransaction();
        //    int[] partsBack = {210,209,208, 207};
        //    boolean contains = IntStream.of(partsBack).anyMatch(x -> x == Integer.parseInt(item.getText()));
        //    if(contains)
        //        fragment = new ParteCuerpoBackFragment();
        //    else
        //        fragment = new ParteCuerpoFragment();
//
        //    ft.remove(fragment);
        //    arguments.putInt(ARG_PARTE_ID, Integer.parseInt(item.getText()));
        //    fragment.setArguments(arguments);
        //    ft.addToBackStack(PatologiaFragment.class.getSimpleName());
        //    ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).addToBackStack(
        //            Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        //    ).commit();
        //} else {
//      //      ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(item.getText());
//      //      Toast.makeText(contexto, parte.getNombre()+" == id --> "+item.getText(), Toast.LENGTH_SHORT).show();
//
        //    final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
        //    intent.putExtra(ARG_ID_PARTE, item.getText());
        //    intent.putExtra("verBotonesDermatoscopia", true);
        //    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //    startActivityForResult(intent, 200);
        //}
//
        //NoteImageAdapterImpl adapter = (NoteImageAdapterImpl) imagen_cuerpo.getAdapter();
        //if (adapter.get_selected().contains(item)) {
        //    adapter.get_selected().remove(item);
        //} else {
        //    adapter.get_selected().add(item);
        //}
        //adapter.notifyDataSetHasChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data.hasExtra(ARG_IMAGENES)) {
            ArrayList<String> imagenes = data.getExtras().getStringArrayList(ARG_IMAGENES);
            if (imagenes.size() > 0) {
                //ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(data.getExtras().getString(ARG_ID_PARTE));
                //if (parte != null) {
                //    FragmentManager fm = getFragmentManager();
                //    FragmentTransaction ft = fm.beginTransaction();
                //    Fragment fragment = new ImagenesCamaraFragment();
                //    arguments.putStringArrayList(ARG_IMAGENES, imagenes);
                //    arguments.putString(ARG_ID_PARTE, data.getExtras().getString(ARG_ID_PARTE));
                //    arguments.putString(ARG_NAME_PARTE, parte.getNombre());
//
                //    if(data.hasExtra("imagenes_dermatoscopia")){
                //        arguments.putString("imagenes_dermatoscopia", data.getExtras().getString("imagenes_dermatoscopia"));
                //    }
                //    fragment.setArguments(arguments);
                //    ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
                //}

                String date = "" + (int) (new Date().getTime());

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = new ImagenesCamaraFragment();
                arguments.putStringArrayList(ARG_IMAGENES, imagenes);
                arguments.putString(ARG_ID_PARTE, date);
                arguments.putString(ARG_NAME_PARTE, txt_nombre_parte.getText().toString());

                try{
                    JSONObject keys_imagen_nombre = new JSONObject();
                    if(arguments.containsKey(ARG_ID_NAME_IMAGE)){
                        keys_imagen_nombre = new JSONObject(arguments.getString(ARG_ID_NAME_IMAGE));
                    }
                    keys_imagen_nombre.put(date, txt_nombre_parte.getText().toString());
                    arguments.putString(ARG_ID_NAME_IMAGE, keys_imagen_nombre.toString());
                }catch (Exception e){

                }


                if(data.hasExtra("imagenes_dermatoscopia")){

                    try{
                        JSONObject images;
                        if(arguments.containsKey("imagenes_dermatoscopia")){
                            images = new JSONObject(arguments.getString("imagenes_dermatoscopia"));
                            JSONObject imagesDermtoscopiaCamara = new JSONObject(data.getExtras().getString("imagenes_dermatoscopia"));

                            Iterator<?> keys = imagesDermtoscopiaCamara.keys();
                            while(keys.hasNext() ){
                                String key = (String)keys.next();
                                images.put(key, imagesDermtoscopiaCamara.getJSONArray(key));
                            }
                            arguments.putString("imagenes_dermatoscopia", images.toString());

                        } else {
                            arguments.putString("imagenes_dermatoscopia", data.getExtras().getString("imagenes_dermatoscopia"));
                        }


                    }catch (Exception e){

                    }


                }
                fragment.setArguments(arguments);
                ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
            }
        }


    }
}
