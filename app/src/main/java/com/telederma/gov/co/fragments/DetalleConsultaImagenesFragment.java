package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.telederma.gov.co.ImagenDermatoscopia;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CheckedImageArrayAdapter;
import com.telederma.gov.co.adapters.Custom2SpinnerAdapter;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.views.ExtendableGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.telederma.gov.co.fragments.DetalleConsultaFragment.tempControlMedico;


/**
 * Created by Daniel Hernández on 16/07/2018.
 */

public class DetalleConsultaImagenesFragment extends DetalleConsultaContentFragment {

    private Spinner spnControles;
    private CustomSpinnerAdapter controlesAdapter;
    private TextView lblFecha, lblNombreConsulta;
    private ExtendableGridView grvImagenes;
    private ImageArrayAdapter imagenesAdapter;
    private List<String> imagenes;
    private List<String> descriptions;
    private ControlConsulta control;
    private View rootView ;
    private JSONObject imagenesDermatoscopia;
    private JSONObject descripcion_images;
    private JSONObject imagenesNombreParte;

    public static final int REQUEST_CODE_IMAGEN = 200;
    public static final String ARG_URL_IMAGEN = "url_imagen";
    public static final String ARG_IMAGEN_NOMBRE_PARTE = "imagen_nombre_parte";
    public static final String ARG_POS_IMAGEN = "pos_imagen";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView = super.onCreateView(inflater, container, savedInstanceState);

        imagenesDermatoscopia = new JSONObject();
        descripcion_images = new JSONObject();
        inicializarViews(rootView);
        inicializarValores();
        inicializarSpinnerControles();
        spnControles = rootView.findViewById(R.id.spn_controles_consulta);
        return rootView;
    }
    private void inicializarSpinnerControles() {
        // variable temporal con el argumento recibido como parametro
        /*int id_control = getArguments().getInt(DetalleConsultaFragment.ARG_ID_CONTROL);
        if(id_control <= 0 ) {
            controlMedico = null;
        }*/

        limpiarControlesErroneos();

        if(consultaMedicina != null) {
            controlesAdapter = new CustomSpinnerAdapter<>(
                    getActivity(), consultaMedicina.getControlesMedicos(), ControlMedico.class
            );

            // Hace que el campo select del control no sea seleccionable
            if(consultaMedicina.getControlesMedicos().size() == 1) {
                if(consultaMedicina.getControlesMedicos().get(0) == null) {
                    spnControles.setEnabled(false);
                }
            }
        }else{
            controlesAdapter = new CustomSpinnerAdapter<>(
                    getActivity(), consultaEnfermeria.getControlesEnfermeria(), ControlEnfermeria.class
            );

            if(consultaEnfermeria.getControlesEnfermeria().size() < 1) {
                spnControles.setEnabled(false);
            }else{
                if(consultaEnfermeria.getControlesEnfermeria().get(0) == null){
                    spnControles.setEnabled(false);
                }
            }
        }

        spnControles.setAdapter(controlesAdapter);
        spnControles.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try
                        {
                            if(consultaMedicina != null && i> 0) {
                                controlMedico = consultaMedicina.getControlesMedicos().get(i);
                                tempControlMedico = controlMedico;
                            }else if(consultaEnfermeria != null && i> 0)
                                controlEnfermeria = consultaEnfermeria.getControlesEnfermeria().get(i);
                            else
                                {
                                    controlMedico = null;
                                    controlEnfermeria=null;
                                }
                        }
                        catch (Exception e)
                        {

                        }
                        inicializarControl();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        if(tempControlMedico!= null && consultaMedicina.getControlesMedicos().size() > 1) {
            int temp_index = controlesAdapter.getIndex(tempControlMedico.getId());
            spnControles.setSelection(temp_index);
        }
    }

    public void limpiarControlesErroneos(){
        try{
            List<ControlMedico> controlesMedicos = consultaMedicina.getControlesMedicos();
            List<ControlMedico> lista = new ArrayList<>();

            for (ControlMedico control : controlesMedicos) {
                if (!(control.getCreated_at().equals(""))){
                    lista.add(control);
                }
            }

            if(lista.size() > 0){
                consultaMedicina.setControlesMedicos(lista);
            }

        } catch (Exception e){

        }

    }

    private void inicializarControl() {
        control = controlMedico != null ? controlMedico : controlEnfermeria;
        if(control != null ) {
            control.setIdServidor(
                    controlMedico != null ?
                            controlMedico.getIdControlConsulta() : controlEnfermeria.getIdControlConsulta()
            );
        }
        inicializarViews(rootView);
        inicializarValores();
    }

    private void inicializarViews(View rootView) {
        lblFecha = rootView.findViewById(R.id.lbl_fecha_consulta);
        lblNombreConsulta = rootView.findViewById(R.id.lbl_nombre_consulta);
        grvImagenes = rootView.findViewById(R.id.grv_imagenes_consulta);
        spnControles = rootView.findViewById(R.id.spn_controles_consulta);
    }

    private void inicializarValores() {
        inicializarListaLesiones();
        imagenesAdapter = new ImageArrayAdapter(
                getActivity(),
                imagenes, descriptions, imagenesDermatoscopia, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageArrayAdapter.ViewHolder holder = (ImageArrayAdapter.ViewHolder) view.getTag();


                ArrayList<String> imagenesArray = new ArrayList<>();

                for(int i = 0; i < imagenes.size(); i++){
                    imagenesArray.add(imagenes.get(i));
                }

                openShowImageDermatoscopia(imagenesArray, holder.position, false);
            }
        });
        grvImagenes.setAdapter(imagenesAdapter);

        if(consultaMedicina != null) {
            if(controlMedico != null) {
                // TODO: 4/12/19 created_at
                lblFecha.setText(controlMedico.getCreated_at().toString());
                lblNombreConsulta.setText(contexto.getString(R.string.control_medico));
            } else {
                // TODO: 4/12/19 created_at
                lblFecha.setText(consultaMedicina.getCreated_at().toString());
                lblNombreConsulta.setText(contexto.getString(R.string.consulta_medica));
            }
        } else if(consultaEnfermeria != null){
            if(controlEnfermeria != null) {
                // TODO: 4/12/19 created_at
                lblFecha.setText(controlEnfermeria.getCreated_at().toString());
                lblNombreConsulta.setText(contexto.getString(R.string.control_enfermeria));
            } else {
                // TODO: 4/12/19 created_at
                lblFecha.setText(consultaEnfermeria.getCreated_at().toString());
                lblNombreConsulta.setText(contexto.getString(R.string.consulta_enfermeria));
            }
        }
    }

    private void inicializarListaLesiones() {
        imagenes = new ArrayList<>();
        descriptions = new ArrayList<>();

        final List<Lesion> lesiones;
        if(controlMedico != null)
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONTROL, controlMedico.getIdControlConsulta()
            );
        else if(controlEnfermeria != null)
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONTROL, controlEnfermeria.getIdControlConsulta()
            );
        else if(consultaMedicina != null)
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONSULTA, consultaMedicina.getIdConsulta()
            );
        else
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONSULTA, consultaEnfermeria.getIdConsulta()
            );

        imagenesNombreParte = new JSONObject();
        Iterator<Lesion> iLesion = lesiones.iterator();
        while (iLesion.hasNext()) {
            Lesion lesion = iLesion.next();
            final List<ImagenLesion> imagenesLesion = lesion.getImagenesLesion();

            if(imagenesLesion != null) {
                inicializarImagenesDermatoscopicas(imagenesLesion);
                Iterator<ImagenLesion> iImagenes = imagenesLesion.iterator();
                ImagenLesion imagenLesion;
                while (iImagenes.hasNext()) {
                    imagenLesion = iImagenes.next();
                    if(imagenLesion.getImage_injury_id() == null) {

                        String nombreParte = lesion.getNameArea() != null ? lesion.getNameArea() : "";
                        imagenes.add(imagenLesion.getPhoto());

                        try{
                            imagenesNombreParte.put(imagenLesion.getPhoto(), nombreParte);
                        } catch (Exception e){

                        }



                        if (!TextUtils.isEmpty(imagenLesion.getEditedPhoto())) {
                            //valida que solo la imagen editada contenga el comentario.
                            descriptions.add("");
                            imagenes.add(imagenLesion.getEditedPhoto());

                            try{
                                imagenesNombreParte.put(imagenLesion.getEditedPhoto(), nombreParte);
                            } catch (Exception e){

                            }
                            if (imagenLesion.getDescription() != null) {
                                descriptions.add(imagenLesion.getDescription());
                            } else {
                                descriptions.add("");
                            }
                        } else {
                            //valida que solo la imagen editada contenga el comentario.
                            descriptions.add(imagenLesion.getDescription());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_detalle_consulta_imagenes;
    }

    public void inicializarImagenesDermatoscopicas(List<ImagenLesion> listImagenes){
        try{
            for(int i = 0; i < listImagenes.size(); i++){

                if(!listImagenes.get(i).getDescription().equals("")){
                    descripcion_images.put(listImagenes.get(i).getEditedPhoto(), listImagenes.get(i).getDescription());
                }

                if(listImagenes.get(i).getImage_injury_id() != null){
                    String urlPadre = obtenerUrlImagen(listImagenes.get(i).getImage_injury_id(), listImagenes);
                    if(!urlPadre.equals("")){
                        if(imagenesDermatoscopia.has(urlPadre)){
                            if(!imagenExistente(imagenesDermatoscopia.getJSONArray(urlPadre), listImagenes.get(i).getPhoto())){
                                imagenesDermatoscopia.getJSONArray(urlPadre).put(listImagenes.get(i).getPhoto());
                            }
                        } else{
                            JSONArray imagenes = new JSONArray();
                            imagenes.put(listImagenes.get(i).getPhoto());
                            imagenesDermatoscopia.put(urlPadre, imagenes);
                        }

                        if(listImagenes.get(i).getEditedPhoto() != null){
                            if(!imagenExistente(imagenesDermatoscopia.getJSONArray(urlPadre), listImagenes.get(i).getEditedPhoto())){
                                imagenesDermatoscopia.getJSONArray(urlPadre).put(listImagenes.get(i).getEditedPhoto());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
    }

    public String obtenerUrlImagen(int idServidor, List<ImagenLesion> listImagenes){
        for(int i = 0; i < listImagenes.size(); i++){
            if(listImagenes.get(i).getIdServidor().equals(idServidor)){
                return listImagenes.get(i).getPhoto();
            }
        }

        return "";


    }

    public Boolean imagenExistente(JSONArray imagenes, String valor){
        try{
            for(int i = 0; i < imagenes.length(); i++){
                if(imagenes.getString(i).equals(valor)){
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public void openShowImageDermatoscopia(ArrayList<String> imagenes, int posicion, Boolean showButtonsDermoscopy) {

        try {
            Intent intent = new Intent(ImagenDermatoscopia.ACTION_IMAGEN_DERMATOSCOPIA);
            intent.putExtra(ARG_URL_IMAGEN, imagenes);
            intent.putExtra(ARG_POS_IMAGEN, posicion);
            intent.putExtra(ARG_IMAGEN_NOMBRE_PARTE, imagenesNombreParte.toString());
            intent.putExtra("showButtonsDermoscopy", showButtonsDermoscopy);
            intent.putExtra("imagenes_dermatoscopia", this.imagenesDermatoscopia.toString());
            intent.putExtra("descripciones", this.descripcion_images.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, REQUEST_CODE_IMAGEN);


        } catch (Exception e) {

        }
    }
}
