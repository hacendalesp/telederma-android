package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.telederma.gov.co.Camara;
import com.telederma.gov.co.ImagenesAnexoCamara;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CheckedImageArrayAdapter;
import com.telederma.gov.co.modelo.CheckedImage;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.utils.Utils;
import com.telederma.gov.co.utils.VoiceText;
import com.telederma.gov.co.views.ExtendableGridView;
import com.telederma.gov.co.views.ReproductorAudioView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.SpeechRecognitionNotAvailable;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_TEMPORAL_HISTORIA;


public class RegistrarControlFragment extends BaseFragment implements MediaRecorder.OnInfoListener, View.OnClickListener {

    private static final int REQUEST_IMAGENES_CAMARA_ANEXO    = 201;
    private static final int REQUEST_IMAGENES_ANEXO           = 202;
    public static final String ARG_MODEL                      ="modelo";
    public static final String ARG_VIEW                       ="tipo_view";
    public static final String ARG_ID_IMAGENES_CONTROL        ="imagenes_control";

    Integer id_consulta;
    MediaRecorder recorder_anexo, recorder_fisico;
    File archivo_anexo, archivo_fisico;
    Button grabar_anexo, detener_anexo, grabar_fisico, detener_fisico, btn_control, btn_camara_anexo;
    EditText texto_examen_fisico, texto_anexo;
    String path_audio_anexo, path_audio_fisico;
    Bundle arguments;
    View root_view;
    ReproductorAudioView audio_anexos, audio_examen_fisico;
    String name_audio_anexo, name_audio_fisico, imagen_anexo;
    File path;
    SeekBar mejoria_subjetiva;
    TextView textView;
    ExtendableGridView gv;
    RadioGroup indicaciones, tolerancia_medicamentos;
    Integer cantidad = 0;
    List<CheckedImage> lstCheckedItem;
    List<String> imagenes = new ArrayList<>();
    ArrayList<String> imagenes_anexo;
    View f_examen_fisico, f_texto_anexo;
    Speech speech=null;
    Boolean resumen = false;


    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_control;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        speech = Speech.init(contexto, contexto.getPackageName());
        utils = Utils.getInstance(contexto);
        recorder_anexo = null;
        recorder_fisico = null;
        indicaciones = (RadioGroup) root_view.findViewById(R.id.id_indicaciones);
        tolerancia_medicamentos = (RadioGroup) root_view.findViewById(R.id.id_tolerancia_medicamentos);

        utils.crearRadios(dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_TOLERO_MEDICAMENTOS), tolerancia_medicamentos);
        gv = (ExtendableGridView) root_view.findViewById(R.id.grid_imagenes);
        grabar_anexo = (Button) root_view.findViewById(R.id.grabar_audio_anexo);
        detener_anexo = (Button) root_view.findViewById(R.id.detener_audio_anexo);
        btn_camara_anexo = (Button) root_view.findViewById(R.id.camara);
        f_texto_anexo = root_view.findViewById(R.id.f_texto_anexo);
        f_examen_fisico = root_view.findViewById(R.id.f_examen_fisico);
        texto_anexo = (EditText) f_texto_anexo.findViewById(R.id.ed_text);//root_view.findViewById(R.id.texto_anexo);
        texto_examen_fisico = (EditText) f_examen_fisico.findViewById(R.id.ed_text);//root_view.findViewById(R.id.texto_examen_fisico);
        texto_anexo.setHint(contexto.getString(R.string.anexos));
        texto_examen_fisico.setHint(contexto.getString(R.string.examen_fisico));



        audio_anexos = (ReproductorAudioView) root_view.findViewById(R.id.reproductor_audio_anexo);
        audio_anexos.setOnRemoveClickListener(v -> eliminarAudioAnexo());


        utils.speechText(f_texto_anexo);
        utils.speechText(f_examen_fisico);

        grabar_anexo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        grabarAudioAnexo();
                        break;
                    case MotionEvent.ACTION_UP:
                        detenerAudioAnexo();
                        break;
                }
                return true;
            }
        });

        detener_anexo.setOnClickListener(this);
        btn_camara_anexo.setOnClickListener(this);

        grabar_fisico = (Button) root_view.findViewById(R.id.grabar_audio_fisico);
        detener_fisico = (Button) root_view.findViewById(R.id.detener_audio_fisico);
        audio_examen_fisico = (ReproductorAudioView) root_view.findViewById(R.id.reproductor_audio_fisico);
        audio_examen_fisico.setOnRemoveClickListener(v -> eliminarAudioFisico());

//        texto_examen_fisico.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b) {
//                    texto_examen_fisico.setGravity(Gravity.LEFT);
//                    texto_examen_fisico.setHint("");
//                }else {
//                    texto_examen_fisico.setGravity(Gravity.CENTER);
//                    texto_examen_fisico.setHint(contexto.getString(R.string.examen_fisico));
//                }
//            }
//        });

        grabar_fisico.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        grabarAudioFisico();
                        break;
                    case MotionEvent.ACTION_UP:
                        detenerAudioFisico();
                        break;
                }
                return true;
            }
        });

        detener_fisico.setOnClickListener(this);

        btn_control = (Button) root_view.findViewById(R.id.btn_crear_control);
        btn_control.setOnClickListener(this);
        arguments = getArguments();
        id_consulta = arguments.getInt("id_consulta");

        path = new File(DIRECTORIO_TEMPORAL_HISTORIA);

        if (!path.exists()) {
            path.mkdirs();
        }
        mejoria_subjetiva = (SeekBar) root_view.findViewById(R.id.seekBar);
        textView = (TextView) root_view.findViewById(R.id.count);

        mejoria_subjetiva.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(" " + seekBarProgress + " / " + seekBar.getMax());
            }

        });

        /*Toolbar toolbar = (Toolbar) root_view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });*/
        if (arguments.containsKey("imagenes_anexo")) {
            imagenes_anexo = arguments.getStringArrayList("imagenes_anexo");
            if (imagenes_anexo.size() > 0)
                crearListaImagenesAnexo(imagenes_anexo, false);
        } else
            imagenes_anexo = new ArrayList<String>();

        resumen = arguments.getBoolean("resumen");
        if(resumen){
            inicializarValores();
            btn_control.setText(getResources().getString(R.string.nueva_consulta_registro_consulta_resumen));
        }

        return root_view;
    }

    public void inicializarValores(){
        try{
            ControlMedico control_medico = dbUtil.getControlMedico(String.valueOf(arguments.getSerializable(ARG_MODEL)));

            texto_anexo.setText(control_medico.getDescripcionAnexo());

            indicaciones.check(control_medico.isTuvoTratamiento() ? R.id.id_indicaciones_si : R.id.id_indicaciones_no);
            tolerancia_medicamentos.check(control_medico.getToleracionMedicacion());
            mejoria_subjetiva.setProgress(Integer.parseInt(control_medico.getMejoraSubjetiva()));
            texto_examen_fisico.setText(control_medico.getDescripcionClinica());
            path_audio_anexo = control_medico.getAudioAnexo();
            path_audio_fisico = control_medico.getAudioClinica();
            textView.setText(" " + Integer.parseInt(control_medico.getMejoraSubjetiva()) + " / " + mejoria_subjetiva.getMax());

            if (arguments.containsKey(ARG_ID_IMAGENES_CONTROL)) {
                imagenes_anexo = arguments.getStringArrayList(ARG_ID_IMAGENES_CONTROL);
                if (imagenes_anexo.size() > 0)
                    crearListaImagenesAnexo(imagenes_anexo, true);
            } else
                imagenes_anexo = new ArrayList<String>();
        }catch (Exception e){

        }
    }

    public void eliminarAudioFisico() {
        grabar_fisico.setVisibility(View.VISIBLE);
        path_audio_fisico = "";
        audio_examen_fisico.setVisibility(View.GONE);
    }

    public void grabarAudioFisico() {

        try {
            grabar_fisico.setVisibility(View.INVISIBLE);
            detener_fisico.setVisibility(View.VISIBLE);
            recorder_fisico = new MediaRecorder();
            recorder_fisico.setMaxDuration(120000);
            recorder_fisico.setOnInfoListener(this);
            recorder_fisico.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder_fisico.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder_fisico.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            name_audio_fisico = "temporal" + System.currentTimeMillis() + ".acc";
            archivo_fisico = new File(path, name_audio_fisico); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            FileOutputStream fOut = new FileOutputStream(archivo_fisico);
            fOut.flush(); // Not really required
            fOut.close();
            path_audio_fisico = path + "/" + name_audio_fisico;
            recorder_fisico.setOutputFile(archivo_fisico.getAbsolutePath());
            recorder_fisico.prepare();
            recorder_fisico.start();

        } catch (Exception e) {

        }


    }

    public void detenerAudioFisico() {
        grabar_fisico.setVisibility(View.INVISIBLE);
        detener_fisico.setVisibility(View.INVISIBLE);
        try {
            if (recorder_fisico != null) {
                recorder_fisico.stop();
                recorder_fisico.release();
            }
            audio_examen_fisico.setVisibility(View.VISIBLE);
            audio_examen_fisico.load(new ArchivoDescarga("", path.getAbsolutePath(), name_audio_fisico));
        } catch (RuntimeException ex) {
            grabar_fisico.setVisibility(View.VISIBLE);
        }

    }


    public void eliminarAudioAnexo() {
        grabar_anexo.setVisibility(View.VISIBLE);
        path_audio_anexo = "";
        audio_anexos.setVisibility(View.GONE);
    }

    public void grabarAudioAnexo() {

        try {
            grabar_anexo.setVisibility(View.INVISIBLE);
            detener_anexo.setVisibility(View.VISIBLE);
            recorder_anexo = new MediaRecorder();
            recorder_anexo.setMaxDuration(120000);
            recorder_anexo.setOnInfoListener(this);
            recorder_anexo.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder_anexo.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder_anexo.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            name_audio_anexo = "temporal" + System.currentTimeMillis() + ".acc";
            archivo_anexo = new File(path, name_audio_anexo); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            FileOutputStream fOut = new FileOutputStream(archivo_anexo);
            fOut.flush(); // Not really required
            fOut.close();
            path_audio_anexo = path + "/" + name_audio_anexo;
            recorder_anexo.setOutputFile(archivo_anexo.getAbsolutePath());
            recorder_anexo.prepare();
            recorder_anexo.start();

        } catch (Exception e) {

        }


    }

    public void detenerAudioAnexo() {
        grabar_anexo.setVisibility(View.INVISIBLE);
        detener_anexo.setVisibility(View.INVISIBLE);
        try {
            if (recorder_anexo != null) {
                recorder_anexo.stop();
                recorder_anexo.release();
            }
            audio_anexos.setVisibility(View.VISIBLE);
            audio_anexos.load(new ArchivoDescarga("", path.getAbsolutePath(), name_audio_anexo));
        } catch (RuntimeException ex) {
            grabar_anexo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
        switch (view.getId()) {
            case R.id.detener_audio_fisico:
                detenerAudioFisico();
                break;
            case R.id.detener_audio_anexo:
                detenerAudioAnexo();
                break;
            case R.id.camara:
                intent.putExtra("cantidad_imagenes", 0);
                startActivityForResult(intent, REQUEST_IMAGENES_CAMARA_ANEXO);
                break;
            case R.id.btn_crear_control:
                save_control();
                break;
        }
    }


    private void crearListaImagenes(ArrayList<String> imageUrls) {
        //gv = (ExtendableGridView) root_view.findViewById(R.id.grid_imagenes);
        ArrayList<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < imageUrls.size(); i++)
            lstItem.add(new CheckedImage(true, imageUrls.get(i)));
        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(getActivity(), lstItem);
        gv.setAdapter(adapter);
    }

    private void crearListaImagenesAnexo(ArrayList<String> imageUrls, boolean estado) {
        //gv = (ExtendableGridView) root_view.findViewById(R.id.grid_imagenes_anexo);
        ArrayList<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < imageUrls.size(); i++)
            lstItem.add(new CheckedImage(estado, imageUrls.get(i)));
        CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(getActivity(), lstItem);
        gv.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.hasExtra("imagenes")) {
            ArrayList<String> imagenes = data.getExtras().getStringArrayList("imagenes");
            if (requestCode == REQUEST_IMAGENES_CAMARA_ANEXO) {
                if (data.hasExtra("imagenes")) {
                    if (imagenes.size() > 0) {
                        final Intent intent = new Intent(ImagenesAnexoCamara.ACTION_ANEXO);
                        imagenes_anexo.addAll(imagenes);
                        intent.putExtra("imagenes_anexo", imagenes_anexo);
                        crearListaImagenes(imagenes_anexo);
                    }
                }
            }
        }

    }


    public void save_control() {
        if (TextUtils.isEmpty(path_audio_anexo) && TextUtils.isEmpty(path_audio_fisico) && TextUtils.isEmpty(imagen_anexo) && texto_examen_fisico.getText().toString().isEmpty() && texto_anexo.getText().toString().isEmpty()) {
            texto_examen_fisico.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_validar_evolucion));
            texto_anexo.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_validar_evolucion));
            texto_examen_fisico.requestFocus();
            return;
        }

        ArrayList<String> imagenes = new ArrayList<String>();
        if (gv != null)
        {
            try{
                CheckedImageArrayAdapter adapter = (CheckedImageArrayAdapter) gv.getAdapter();
                lstCheckedItem = adapter.getCheckedItem();
                if (lstCheckedItem != null && lstCheckedItem.size() > 0) {

                    for (int i = 0; i < lstCheckedItem.size(); i++) {
                        imagenes.add(lstCheckedItem.get(i).getUrl());
                    }
                }
            /*List<CheckedImage> lista_no_selected_imagenes = adapter.getNotCheckedItem();
            for (CheckedImage imagen : lista_no_selected_imagenes) {
                File file = new File(imagen.getUrl());
                if(file.exists())
                    file.delete();
            }*/
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        ControlMedico control = new ControlMedico();
        control.setDescripcionAnexo(texto_anexo.getText().toString());
        control.setIdConsulta(id_consulta);

        control.setTuvoTratamiento(indicaciones.getCheckedRadioButtonId()==R.id.id_indicaciones_si);
        control.setToleracionMedicacion(tolerancia_medicamentos.getCheckedRadioButtonId());
        control.setMejoraSubjetiva(mejoria_subjetiva.getProgress() + "");
        control.setDescripcionClinica(texto_examen_fisico.getText().toString());
        control.setAudioAnexo(path_audio_anexo);
        control.setAudioClinica(path_audio_fisico);
        dbUtil.crearControlMedico(control);

        if (!TextUtils.isEmpty(path_audio_anexo))
            audio_anexos.pause();

        if (!TextUtils.isEmpty(path_audio_fisico))
            audio_examen_fisico.pause();
        ocultarMensajeEspera();


        Fragment fragment;
        if(resumen){
            fragment = new ResumenFragment();
        } else {
            fragment = new PatologiaFragment();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        arguments.putInt(ARG_MODEL,control.getId());
        arguments.putStringArrayList(ARG_ID_IMAGENES_CONTROL,imagenes);
        arguments.putInt(ARG_VIEW,1);
        fragment.setArguments(arguments);
        ft.replace(R.id.menu_content, fragment).addToBackStack(
                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        ).commit();


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

}


