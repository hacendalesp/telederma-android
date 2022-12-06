package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.telederma.gov.co.Body;
import com.telederma.gov.co.Camara;
import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.patologia.AnatomyItem;
import com.telederma.gov.co.patologia.ItemClickListener;
import com.telederma.gov.co.patologia.NoteImageAdapterImpl;
import com.telederma.gov.co.patologia.NoteImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParteCuerpoBackFragment extends BaseFragment implements ItemClickListener<AnatomyItem> {

    private List<AnatomyItem> pointsFront = new ArrayList<>();
    private NoteImageView imageMapViewFront;
    private static Map<Integer, Integer> map_parts_fondos;
    private RelativeLayout contenedor ;
    public static final String ARG_GENERO            ="genero";
    public static final String ARG_PARTE_ID          ="parte_id";
    public static final String ARG_ID_PARTE          ="id_parte";
    public static final String ARG_NAME_PARTE        ="name_parte";
    public static final String ARG_IMAGENES          ="imagenes";
    public static final String ARG_ID_CONTENT        ="id_content";

    private static Bundle arguments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        map_parts_fondos = new HashMap<>();
        arguments = getArguments();
        Integer parte_id = arguments.getInt(ARG_PARTE_ID);
        Integer genero = arguments.getInt(ARG_GENERO, 1);
        contenedor = root.findViewById(R.id.contenedor);
        map_parts_fondos.put(204, R.id.mano_izquierda);
        map_parts_fondos.put(210, R.id.mano_izquierda_atras);
        map_parts_fondos.put(209, R.id.mano_derecha_atras);
        map_parts_fondos.put(203, R.id.mano_derecha);
        map_parts_fondos.put(137, R.id.planta_derecha);
        map_parts_fondos.put(121, R.id.planta_izquierda);
        map_parts_fondos.put(206, R.id.pie_izquierdo);
        map_parts_fondos.put(205, R.id.pie_derecho);
        map_parts_fondos.put(208, R.id.gluteos);//GLUTEOS
        Canvas canvas = new Canvas();
        canvas.drawColor(Color.WHITE);
        root.draw(canvas);
        if(genero==1)
        {
            map_parts_fondos.put(200, R.id.hombre_cabeza_frente);//CARA
            map_parts_fondos.put(201, R.id.zona_torso_hombre);
            map_parts_fondos.put(202, R.id.zona_genital_hombre);
            map_parts_fondos.put(207, R.id.hombre_cabeza_atras);
        }
        else
        {
            map_parts_fondos.put(200, R.id.face);//CARA
            map_parts_fondos.put(201, R.id.torso);
            map_parts_fondos.put(202, R.id.zona_genital);
            map_parts_fondos.put(207, R.id.cabeza_atras);
        }
        pointsFront.clear();
        pointsFront.removeAll(pointsFront);
        pointsFront = Body.getPart(genero,parte_id,contexto);
        for(Map.Entry<Integer, Integer> part :map_parts_fondos.entrySet() ) {

            imageMapViewFront = (NoteImageView) root.findViewById(map_parts_fondos.get(part.getKey()));
            imageMapViewFront.setVisibility(View.GONE);
            imageMapViewFront.setAllowTransparent(true);
            imageMapViewFront.clearAnimation();
            imageMapViewFront.destroyDrawingCache();
        }

        imageMapViewFront = (NoteImageView) root.findViewById(map_parts_fondos.get(parte_id));
        imageMapViewFront.setVisibility(View.VISIBLE);
        imageMapViewFront.setAdapter(new NoteImageAdapterImpl(pointsFront, getActivity(), this));
        imageMapViewFront.setAllowTransparent(false);
        return root;
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_parte_cuerpo_back;
    }


    @Override
    public void onMapItemClick(AnatomyItem item) {
//        ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(item.getText());
//        Toast.makeText(contexto, parte.getNombre()+" == id --> "+item.getText(), Toast.LENGTH_SHORT).show();

        NoteImageAdapterImpl adapter = (NoteImageAdapterImpl) imageMapViewFront.getAdapter();
        if (adapter.get_selected().contains(item)) {
            adapter.get_selected().remove(item);
        } else {
            adapter.get_selected().add(item);
        }
        adapter.notifyDataSetHasChanged();
        final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
        intent.putExtra(ARG_ID_PARTE, item.getText());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 200);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.hasExtra(ARG_IMAGENES)) {
            ArrayList<String> imagenes = data.getExtras().getStringArrayList(ARG_IMAGENES);
            if (imagenes.size() > 0) {
                ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(data.getExtras().getString(ARG_ID_PARTE));
                if (parte != null) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment fragment = new ImagenesCamaraFragment();
                    arguments.putStringArrayList(ARG_IMAGENES, imagenes);
                    arguments.putString(ARG_ID_PARTE, data.getExtras().getString(ARG_ID_PARTE));
                    arguments.putString(ARG_NAME_PARTE, parte.getNombre());

                    fragment.setArguments(arguments);
                    ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).commit();
                }
            }
        }


    }
}









