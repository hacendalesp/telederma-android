package com.telederma.gov.co.fragments;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.utils.Utils;
import com.telederma.gov.co.views.ReproductorAudioView;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_TEMPORAL_HISTORIA;


public class RegistrarExamenFisicoFragment extends BaseFragment
        implements MediaRecorder.OnInfoListener, View.OnClickListener {

    Integer id_consulta;
    MediaRecorder recorder;
    File archivo = null;
    Button grabar, detener, btn_examen_fisico, btn_resumen;
    EditText texto_examen_fisico, weight;
    Bundle arguments;
    View root_view;
    Boolean resumen = false;
    ReproductorAudioView audio;
    File path;
    View f_examen_fisico;
    Speech speech=null;
    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_examen_fisico;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        asignarEventoOcultarTeclado(root_view);
        speech = Speech.init(contexto, contexto.getPackageName());
        utils = Utils.getInstance(contexto);
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        header.setText(getResources().getString(R.string.nueva_consulta_registro_paso3));
        f_examen_fisico = root_view.findViewById(R.id.f_examen_fisico);
        weight = (EditText) root_view.findViewById(R.id.weight);
        texto_examen_fisico = (EditText) f_examen_fisico.findViewById(R.id.ed_text);//root_view.findViewById(R.id.texto_examen_fisico);
        texto_examen_fisico.setHint(contexto.getString(R.string.examen_fisico));

        grabar = (Button) root_view.findViewById(R.id.grabar);
        detener = (Button) root_view.findViewById(R.id.detener);
        btn_examen_fisico = (Button) root_view.findViewById(R.id.btn_examen_fisico);
        audio = (ReproductorAudioView) root_view.findViewById(R.id.reproductor);
        audio.setOnRemoveClickListener(v -> eliminar());

        utils.speechText(f_examen_fisico);

        path = new File(DIRECTORIO_TEMPORAL_HISTORIA);

        if (!path.exists()) {
            path.mkdirs();
        }
        recorder = null;
        grabar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        grabar();
                        break;
                    case MotionEvent.ACTION_UP:
                        detener();
                        break;
                }
                return true;
            }
        });

        detener.setOnClickListener(this);
        btn_examen_fisico.setOnClickListener(this);
        arguments = getArguments();
        id_consulta = arguments.getInt("id_consulta");
        if (arguments.getInt("id_examen") != 0) {
            poblar_formulario(id_consulta);
        }
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
        toolbar.setTitle(getString(R.string.nueva_consulta_title_paso3));


        btn_resumen = (Button) root_view.findViewById(R.id.btn_resumen);
        resumen = arguments.getBoolean("resumen");
        btn_resumen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mostrarMensajeEspera(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        saveExamenFisico();
                    }
                });
            }
        });
        if (resumen) {
            btn_examen_fisico.setVisibility(View.GONE);
            btn_resumen.setVisibility(View.VISIBLE);
        } else {
            btn_examen_fisico.setVisibility(View.VISIBLE);
            btn_resumen.setVisibility(View.GONE);
        }
        return root_view;
    }

    public void eliminar() {
        grabar.setVisibility(View.VISIBLE);
        audio.setVisibility(View.GONE);
    }

    public void grabar() {

        try {
            grabar.setVisibility(View.INVISIBLE);
            detener.setVisibility(View.VISIBLE);
            recorder = new MediaRecorder();
            recorder.setMaxDuration(120000);
            recorder.setOnInfoListener(this);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            archivo = new File(path, "temporal" + System.currentTimeMillis() + ".acc"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            FileOutputStream fOut = new FileOutputStream(archivo);
            fOut.flush(); // Not really required
            fOut.close();

            recorder.setOutputFile(archivo.getAbsolutePath());
            recorder.prepare();
            recorder.start();

        } catch (Exception e) {
            String p = "";
        }


    }

    public void detener() {
        grabar.setVisibility(View.INVISIBLE);
        detener.setVisibility(View.INVISIBLE);
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            audio.setVisibility(View.VISIBLE);
            audio.load(new ArchivoDescarga("", path.getAbsolutePath(), archivo.getName()));
        } catch (RuntimeException ex) {
            grabar.setVisibility(View.VISIBLE);
            Log.d("EXAMEN FISICO", "AUDIO EXAMEN");
        }

    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detener:
                detener();
                break;
            case R.id.btn_examen_fisico:
                mostrarMensajeEspera(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        saveExamenFisico();
                    }
                });
                break;
        }
    }

    public void saveExamenFisico() {
        if (!validar_campos()) {
            ocultarMensajeEspera();
            return;
        }
        ConsultaMedica consulta = getConsulta(id_consulta);
        consulta.setDescripcionExamenFisico(texto_examen_fisico.getText().toString());
        if (archivo != null) {
            consulta.setAudioFisico(archivo.getAbsolutePath());
            audio.pause();
        }
        consulta.setWeight(weight.getText().toString());
        dbUtil.crearConsultaMedica(consulta);
        ocultarMensajeEspera();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        arguments.putBoolean("resumen", false);
        arguments.putInt("id_examen", id_consulta);

        if (resumen) {
            fragment = new ResumenFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).commit();
        } else if (arguments.containsKey("lesiones_registradas")) {
            fragment = new ImagenesCamaraFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        } else {
            fragment = new PatologiaFragment();
            arguments.putInt("id_content",R.id.paso1);
            arguments.putBoolean("cuerpo_frente", true);
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }
    }

    public boolean validar_campos() {
        boolean validacionCampos = true;

        if (archivo == null && texto_examen_fisico.getText().toString().isEmpty()) {
            EditText texto_examen_fisico = (EditText) root_view.findViewById(R.id.texto_examen_fisico);
            texto_examen_fisico.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_validar_evolucion));
            texto_examen_fisico.requestFocus();
            validacionCampos = false;
        }

        if (weight.getText().toString().isEmpty()) {
            weight.setError(getResources().getString(R.string.campo_requerido));
            weight.requestFocus();
            validacionCampos = false;
        }

        return validacionCampos;
    }

    public void poblar_formulario(int consulta_id) {
        ConsultaMedica consulta = getConsulta(consulta_id);
        if (consulta != null) {
            String path_audio = consulta.getAudioFisico();
            texto_examen_fisico.setText(consulta.getDescripcionExamenFisico());
            weight.setText(consulta.getWeight());
            if (path_audio != null && !path_audio.isEmpty()) {
                archivo = new File(path_audio);
                detener();
            }
        }
    }

    public ConsultaMedica getConsulta(int consulta_id) {
        ConsultaMedica consulta = null;
        try {

            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDAO.queryBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            List<ConsultaMedica> list_consultas = consultaDAO.query(queryBuilder.prepare());
            if (!list_consultas.isEmpty()) {
                consulta = list_consultas.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consulta;
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

