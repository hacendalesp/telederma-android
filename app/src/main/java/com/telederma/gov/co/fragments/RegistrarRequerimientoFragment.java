package com.telederma.gov.co.fragments;

import android.media.MediaPlayer;
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
import android.widget.Toast;

import com.telederma.gov.co.IntroActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.utils.Utils;
import com.telederma.gov.co.views.ExtendableGridView;
import com.telederma.gov.co.views.ReproductorAudioView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_TEMPORAL_HISTORIA;


public class RegistrarRequerimientoFragment extends BaseFragment implements MediaPlayer.OnCompletionListener, MediaRecorder.OnInfoListener, View.OnClickListener {

    public static final String ARG_ID_REQUERIMIENTO = "id_requerimiento";
    public static final String ARG_VIEW             ="tipo_view";
    public static final String ARG_MODEL            ="modelo";

    Integer id_requerimiento;
    MediaRecorder recorder_fisico;
    MediaPlayer player_fisico;
    File archivo_fisico;
    Button grabar_fisico, detener_fisico, btn_imagenes, btn_crear_respuesta;
    EditText texto_examen_fisico;
    String path_audio_fisico = "";
    Bundle arguments;
    View root_view;
    ReproductorAudioView audio_examen_fisico;
    String name_audio_fisico;
    File path;
    ExtendableGridView gv;
    View f_examen_fisico;
    protected Utils utils;
    Speech speech=null;
    Boolean resumen = false;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_resolver_requerimiento;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        speech = Speech.init(contexto, contexto.getPackageName());
        utils = Utils.getInstance(contexto);
        recorder_fisico = null;
        f_examen_fisico = root_view.findViewById(R.id.f_examen_fisico);
        texto_examen_fisico = (EditText) f_examen_fisico.findViewById(R.id.ed_text);//root_view.findViewById(R.id.texto_examen_fisico);
        texto_examen_fisico.setHint(contexto.getString(R.string.consulta_requerimiento_texto));
        grabar_fisico = (Button) root_view.findViewById(R.id.grabar_audio_fisico);
        detener_fisico = (Button) root_view.findViewById(R.id.detener_audio_fisico);
        audio_examen_fisico = (ReproductorAudioView) root_view.findViewById(R.id.reproductor_audio_fisico);
        audio_examen_fisico.setOnRemoveClickListener(v -> eliminarAudioFisico());

        utils.speechText(f_examen_fisico);

        btn_crear_respuesta = (Button) root_view.findViewById(R.id.btn_crear_respuesta);

        btn_crear_respuesta.setOnClickListener(this);

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

        arguments = getArguments();
        id_requerimiento = arguments.getInt(ARG_ID_REQUERIMIENTO);

        path = new File(DIRECTORIO_TEMPORAL_HISTORIA);

        if (!path.exists()) {
            path.mkdirs();
        }

        resumen = arguments.getBoolean("resumen");
        if(resumen){
            inicializarValores();
            btn_crear_respuesta.setText(getResources().getString(R.string.nueva_consulta_registro_consulta_resumen));
        }

        return root_view;
    }

    public void inicializarValores(){
        try{
            Requerimiento requerimiento = dbUtil.getRequerimiento(String.valueOf(arguments.getInt(ARG_MODEL)));
            texto_examen_fisico.setText(requerimiento.getDescription_request());
            path_audio_fisico = requerimiento.getAudioClinica();

        }catch (Exception e){

        }
    }

    public void eliminarAudioFisico() {
        grabar_fisico.setVisibility(View.VISIBLE);
        path_audio_fisico = "";
        player_fisico = null;
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
            player_fisico = new MediaPlayer();
            player_fisico.setOnCompletionListener(this);
            player_fisico.setDataSource(archivo_fisico.getAbsolutePath());
            audio_examen_fisico.setVisibility(View.VISIBLE);
            audio_examen_fisico.load(new ArchivoDescarga("", path.getAbsolutePath(), name_audio_fisico));
        } catch (IOException e) {
        } catch (RuntimeException ex) {
            grabar_fisico.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer.equals(player_fisico))
            detenerAudioFisico();
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detener_audio_fisico:
                detenerAudioFisico();
                break;
            case R.id.btn_crear_respuesta:
                saveRespuestaRequerimiento();
                break;
        }
    }




    public void saveRespuestaRequerimiento() {
        if (TextUtils.isEmpty(path_audio_fisico) && TextUtils.isEmpty(texto_examen_fisico.getText().toString())) {
            texto_examen_fisico.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_validar_evolucion));
            texto_examen_fisico.requestFocus();
            return;
        }
        // TODO: 2/12/19 esta obteniendo un requriemiento que la bd devuelve null. el id_requerimiento = 8
        Requerimiento requerimiento = dbUtil.obtenerRequerimientoControl(id_requerimiento.toString());
        if (requerimiento != null) {
            Usuario doctor = dbUtil.obtenerUsuarioLogueado(
                    Session.getInstance(contexto).getCredentials().getIdUsuario()
            );
            if(doctor != null)
                requerimiento.setIdDoctor(doctor.getIdServidor());

            if (!TextUtils.isEmpty(path_audio_fisico))
                requerimiento.setAudioClinica(path_audio_fisico);
            requerimiento.setDescription_request(texto_examen_fisico.getText().toString());
            requerimiento.setIdServidor(id_requerimiento);
            dbUtil.crearRequerimiento(requerimiento);

            Fragment fragment;
            if(resumen){
                fragment = new ResumenFragment();
            } else {
                fragment = new PatologiaFragment();
            }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            arguments.putInt(ARG_VIEW, 2);
            arguments.putInt(ARG_MODEL, requerimiento.getId());
            arguments.putBoolean("cuerpo_frente", true);
            arguments.putInt("genero", arguments.getInt("genre", 1));
            fragment.setArguments(arguments);
            ft.replace(R.id.menu_content, fragment).addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK).commit();
        }else{
            Toast.makeText(contexto, "!Requerimiento no encontrado!", Toast.LENGTH_SHORT).show();
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
}


