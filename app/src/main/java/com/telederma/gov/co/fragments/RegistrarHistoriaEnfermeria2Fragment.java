package com.telederma.gov.co.fragments;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.views.ReproductorAudioView;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_TEMPORAL_HISTORIA;

public class RegistrarHistoriaEnfermeria2Fragment extends BaseFragment implements MediaPlayer.OnCompletionListener, MediaRecorder.OnInfoListener, View.OnClickListener,CompoundButton.OnCheckedChangeListener {


    View root_view;
    LinearLayout ulcer;
    public final int OTRA                     = 6;
    public final int APOSITO                  = 5;
    public  final String  RESPUESTA_POSITIVA  = "Si" ;
    public  final String  ID_CONSULTA         = "id_consulta";
    public  final String  RESUMEN             = "resumen";


    Button  grabar_fisico, detener_fisico, btn_registrar,btn_resumen;
    EditText texto_examen_fisico,etLargo,etAncho;
    String  path_audio_fisico;
    Bundle arguments;
    ReproductorAudioView  audio_examen_fisico;
    String name_audio_fisico;
    File path,archivo_fisico;
    MediaRecorder  recorder_fisico;
    Boolean resumen = false;
    RadioGroup group_inflamacion,group_olor,group_exudan,group_aumento,group_granulacion,group_fibrina,group_necrotico,group_hipergranulacion ,group_cicatrizacion,group_costra,group_dermatitis,group_fibrosis;
    Spinner spinner_pedio , spinner_femoral , spinner_tibial;
    public Map<CheckBox,EditText> data_otro_ulcer = new HashMap<>();
    Map<Integer,String> constantes = new HashMap< Integer,String>() {{
        put( R.id.id_pedio,Parametro.TIPO_PARAMETRO_PULSES);
        put( R.id.id_femoral,Parametro.TIPO_PARAMETRO_PULSES);
        put( R.id.id_tibial,Parametro.TIPO_PARAMETRO_PULSES);
    }};
    View f_examen_fisico;
    Speech speech=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        speech = Speech.init(contexto, contexto.getPackageName());
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        header.setText(getResources().getString(R.string.nueva_consulta_registro_paso3));
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
        toolbar.setTitle(getString(R.string.nueva_consulta_title_paso3));
        ulcer = (LinearLayout) root_view.findViewById(R.id.id_manejo);
        recorder_fisico = null;
        btn_registrar = (Button) root_view.findViewById(R.id.btn_registrar_historia);
        btn_resumen = (Button) root_view.findViewById(R.id.btn_resumen);
        btn_registrar.setOnClickListener(this);
        btn_resumen.setOnClickListener(this);

        group_inflamacion       = (RadioGroup) root_view.findViewById(R.id.id_inflamacion);
        group_olor              = (RadioGroup) root_view.findViewById(R.id.id_olor);
        group_exudan            = (RadioGroup) root_view.findViewById(R.id.id_exudan);
        group_aumento           = (RadioGroup) root_view.findViewById(R.id.id_aumento);
        group_granulacion       = (RadioGroup) root_view.findViewById(R.id.id_granulacion);
        group_fibrina           = (RadioGroup) root_view.findViewById(R.id.id_fibrina);
        group_necrotico         = (RadioGroup) root_view.findViewById(R.id.id_necrotico);
        group_hipergranulacion  = (RadioGroup) root_view.findViewById(R.id.id_hipergranulacion);
        group_cicatrizacion     = (RadioGroup) root_view.findViewById(R.id.id_cicatrizacion);
        group_costra            = (RadioGroup) root_view.findViewById(R.id.id_costra);
        group_dermatitis        = (RadioGroup) root_view.findViewById(R.id.id_dermatitis);
        group_fibrosis          = (RadioGroup) root_view.findViewById(R.id.id_fibrosis);

        spinner_femoral         = (Spinner) root_view.findViewById(R.id.id_femoral);
        spinner_pedio           = (Spinner) root_view.findViewById(R.id.id_pedio);
        spinner_tibial          = (Spinner) root_view.findViewById(R.id.id_tibial);

        etLargo                 = (EditText) root_view.findViewById(R.id.id_largo);
        etAncho                 = (EditText) root_view.findViewById(R.id.id_ancho);

        // remplazado por el campo de speech
        f_examen_fisico         = root_view.findViewById(R.id.f_examen_fisico);
        texto_examen_fisico     = f_examen_fisico.findViewById(R.id.ed_text);//(EditText) root_view.findViewById(R.id.texto_examen_fisico);
        grabar_fisico           = (Button) root_view.findViewById(R.id.grabar_audio_fisico);
        detener_fisico          = (Button) root_view.findViewById(R.id.detener_audio_fisico);
        audio_examen_fisico     = (ReproductorAudioView) root_view.findViewById(R.id.reproductor_audio_fisico);

        texto_examen_fisico.setHint(contexto.getString(R.string.historia_enfermeria_diagnostico_motivo_));

        utils.speechText(f_examen_fisico);
        audio_examen_fisico.setOnRemoveClickListener(v -> eliminarAudioFisico());
        arguments = getArguments();

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
        path = new File(DIRECTORIO_TEMPORAL_HISTORIA);

        if (!path.exists()) {
            path.mkdirs();
        }

        for (Map.Entry<Integer,String> constante : constantes.entrySet())
            poblar_spinner(root_view.findViewById(constante.getKey()), dbUtil.obtenerParametros( constante.getValue() ));

        String texto_1= getString(R.string.historia_enfermeria_diagnostico_signos_inflamacion)+" "+getString(R.string.historia_enfermeria_diagnostico_signos_inflamacion_);
        SpannableString ss1=  new SpannableString(texto_1);
        ss1.setSpan(new RelativeSizeSpan(0.7f), getString(R.string.historia_enfermeria_diagnostico_signos_inflamacion).length(),getString(R.string.historia_enfermeria_diagnostico_signos_inflamacion).length()+getString(R.string.historia_enfermeria_diagnostico_signos_inflamacion_).length()+1, 0); // set size
        //ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, getString(R.string.historia_enfermeria_diagnostico_signos_inflamacion).length(), 0);// set color
        TextView tv= (TextView) root_view.findViewById(R.id.id_inflamacion_text);
        tv.setWidth(420);
        tv.setText(ss1);
        resumen = arguments.getBoolean(RESUMEN, false);
        return root_view;
    }

    public void saveConsult() {
        String infeccion = "" ;
        String heridas   = "" ;
        String alrededor_ulcera   = "" ;

        if (!validar_campos()) {
            ocultarMensajeEspera();
            return;
        }
        ConsultaEnfermeria consultaEnfermeria = dbUtil.getConsultaEnfermeria(arguments.getInt(ID_CONSULTA));
        consultaEnfermeria.setPulses_pedio(((Parametro) spinner_pedio.getSelectedItem()).getValor().toString());
        consultaEnfermeria.setPulses_tibial(((Parametro) spinner_tibial.getSelectedItem()).getValor().toString());
        consultaEnfermeria.setPulses_femoral(((Parametro) spinner_femoral.getSelectedItem()).getValor().toString());
        consultaEnfermeria.setUlcer_length(etLargo.getText().toString());
        consultaEnfermeria.setUlcer_width(etAncho .getText().toString());
        consultaEnfermeria.setUlcer_handle(getSeleccionCheckBox(ulcer));
        consultaEnfermeria.setUlcer_handle_other(((CheckBox)root_view.findViewWithTag("checkbox_"+OTRA)).getText().toString());
        consultaEnfermeria.setUlcer_handle_aposito(((CheckBox)root_view.findViewWithTag("checkbox_"+APOSITO)).getText().toString());

        infeccion += (((RadioButton)root_view.findViewById(group_inflamacion.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.INFLAMACION+"," :"";
        infeccion += (((RadioButton)root_view.findViewById(group_olor.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.OLOR_FETIDO+"," :"";
        infeccion += (((RadioButton)root_view.findViewById(group_exudan.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.EXUDADO_PURULENTO+"," :"";
        infeccion += (((RadioButton)root_view.findViewById(group_aumento.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.AUMENTO_RECIENTE_DOLOR+"," :"";

        heridas   += (((RadioButton)root_view.findViewById(group_granulacion.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.TEJIDO_GRANULACION+"," :"";
        heridas   += (((RadioButton)root_view.findViewById(group_fibrina.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.FIBRINA+"," :"";
        heridas   += (((RadioButton)root_view.findViewById(group_necrotico.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.TEJIDO_NECROTICO+"," :"";
        heridas   += (((RadioButton)root_view.findViewById(group_hipergranulacion.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.HIPERGRANULACION+"," :"";
        heridas   += (((RadioButton)root_view.findViewById(group_cicatrizacion.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.CICATRIZACION_COMPLETA+"," :"";
        heridas   += (((RadioButton)root_view.findViewById(group_costra.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.COSTRA+"," :"";

        alrededor_ulcera   += (((RadioButton)root_view.findViewById(group_dermatitis.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.DERMATITIS+"," :"";
        alrededor_ulcera   += (((RadioButton)root_view.findViewById(group_fibrosis.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA))?ConsultaEnfermeria.FIBROSIS+"," :"";

        consultaEnfermeria.setInfection_signs(infeccion);
        consultaEnfermeria.setWound_tissue(heridas);
        consultaEnfermeria.setSkin_among_ulcer(alrededor_ulcera);
        consultaEnfermeria.setIdInformacionPacienteLocal( arguments.getInt("informacion_paciente")+"");

        consultaEnfermeria.setConsultation_reason_description(texto_examen_fisico.getText().toString());
        if (archivo_fisico != null) {
            consultaEnfermeria.setConsultation_reason_audio(archivo_fisico.getAbsolutePath());
            audio_examen_fisico.pause();
        }
        dbUtil.crearConsultaEnfermeria(consultaEnfermeria);
        ocultarMensajeEspera();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        arguments.putInt(ID_CONSULTA, consultaEnfermeria.getId());

        arguments.putBoolean(RESUMEN, false);

        if (resumen) {
            fragment = new RegistrarAnexoFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).commit();
        }  else if (arguments.containsKey("lesiones_registradas"))
        {
            fragment = new ImagenesCamaraFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        } else {
            fragment = new PatologiaFragment();
            arguments.putInt("id_content",R.id.paso1);
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }
    }

    public ConsultaEnfermeria getConsulta(Integer consulta_id) {
        ConsultaEnfermeria consulta = new ConsultaEnfermeria();
        try {
            if (consulta_id == null)
                consulta_id = 0;
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaDAO.queryBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            List<ConsultaEnfermeria> list_consultas = consultaDAO.query(queryBuilder.prepare());
            if (!list_consultas.isEmpty()) {
                consulta = list_consultas.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consulta;
    }

    private void poblar_spinner(Spinner spinnerCustom, List<Parametro> data) {
        CustomSpinnerAdapter<Parametro> customSpinnerAdapter = new CustomSpinnerAdapter<>(
                getActivity().getApplicationContext(), data, Parametro.class
        );
        spinnerCustom.setAdapter((SpinnerAdapter) customSpinnerAdapter);
    }

    public void poblar_formulario(int id_consulta_enfermeria) {
        ConsultaEnfermeria enfermeria = dbUtil.getConsultaEnfermeria(id_consulta_enfermeria) ;
        if(enfermeria!= null)
        {
            if (enfermeria.getUlcer_handle()!= null && enfermeria.getUlcer_handle().indexOf(",") > -1) {
                String[] arr_handle= enfermeria.getUlcer_handle().split(",");
                for (int i = 0; i < arr_handle.length; i++) {
                    for (int j = 0; j < ulcer.getChildCount(); j++) {
                        View v = ulcer.getChildAt(j);
                        if (v instanceof CheckBox && ((CheckBox) v).getId() == Integer.parseInt(arr_handle[i])) {
                            ((CheckBox) v).setChecked(true);
                        }
                    }
                }
            }
            data_otro_ulcer.get((CheckBox)root_view.findViewWithTag("checkbox_"+OTRA)).setText(enfermeria.getUlcer_handle_other());
            data_otro_ulcer.get((CheckBox)root_view.findViewWithTag("checkbox_"+APOSITO)).setText(enfermeria.getUlcer_handle_aposito());

            if(enfermeria.getPulses_pedio()!=null)
              spinner_pedio.setSelection((((CustomSpinnerAdapter<Parametro>) spinner_pedio.getAdapter()).getPosition(Integer.parseInt(enfermeria.getPulses_pedio()))));
            if(enfermeria.getPulses_femoral()!=null)
              spinner_femoral.setSelection((((CustomSpinnerAdapter<Parametro>) spinner_femoral.getAdapter()).getPosition(Integer.parseInt(enfermeria.getPulses_femoral()))));
            if(enfermeria.getPulses_tibial()!=null)
              spinner_tibial.setSelection((((CustomSpinnerAdapter<Parametro>) spinner_tibial.getAdapter()).getPosition(Integer.parseInt(enfermeria.getPulses_tibial()))));

            etLargo.setText(enfermeria.getUlcer_length());
            etAncho.setText(enfermeria.getUlcer_width());

            if (enfermeria.getInfection_signs() != null &&enfermeria.getInfection_signs().indexOf(",") > -1) {
                if(enfermeria.getInfection_signs().contains(ConsultaEnfermeria.INFLAMACION))
                    group_inflamacion.check(((RadioButton)group_inflamacion.getChildAt(0)).getId());
                if(enfermeria.getInfection_signs().contains(ConsultaEnfermeria.OLOR_FETIDO))
                    group_olor.check(((RadioButton)group_olor.getChildAt(0)).getId());
                if(enfermeria.getInfection_signs().contains(ConsultaEnfermeria.EXUDADO_PURULENTO))
                    group_exudan.check(((RadioButton)group_exudan.getChildAt(0)).getId());
                if(enfermeria.getInfection_signs().contains(ConsultaEnfermeria.AUMENTO_RECIENTE_DOLOR))
                    group_aumento.check(((RadioButton)group_aumento.getChildAt(0)).getId());
            }
            if(enfermeria.getWound_tissue()!= null && enfermeria.getWound_tissue().indexOf(",")>-1)
            {
                if(enfermeria.getWound_tissue().contains(ConsultaEnfermeria.TEJIDO_GRANULACION))
                    group_granulacion.check(((RadioButton)group_granulacion.getChildAt(0)).getId());
                if(enfermeria.getWound_tissue().contains(ConsultaEnfermeria.FIBRINA))
                    group_fibrina.check(((RadioButton)group_fibrina.getChildAt(0)).getId());
                if(enfermeria.getWound_tissue().contains(ConsultaEnfermeria.TEJIDO_NECROTICO))
                    group_necrotico.check(((RadioButton)group_necrotico.getChildAt(0)).getId());
                if(enfermeria.getWound_tissue().contains(ConsultaEnfermeria.HIPERGRANULACION))
                    group_hipergranulacion.check(((RadioButton)group_hipergranulacion.getChildAt(0)).getId());
                if(enfermeria.getWound_tissue().contains(ConsultaEnfermeria.CICATRIZACION_COMPLETA))
                    group_cicatrizacion.check(((RadioButton)group_cicatrizacion.getChildAt(0)).getId());
                if(enfermeria.getWound_tissue().contains(ConsultaEnfermeria.COSTRA))
                    group_costra.check(((RadioButton)group_costra.getChildAt(0)).getId());
            }
            if(enfermeria.getSkin_among_ulcer()!= null && enfermeria.getSkin_among_ulcer().indexOf(",")>-1)
            {
                if(enfermeria.getSkin_among_ulcer().contains(ConsultaEnfermeria.DERMATITIS))
                    group_dermatitis.check(((RadioButton)group_dermatitis.getChildAt(0)).getId());

                if(enfermeria.getSkin_among_ulcer().contains(ConsultaEnfermeria.FIBROSIS))
                    group_fibrosis.check(((RadioButton)group_fibrosis.getChildAt(0)).getId());
            }
            texto_examen_fisico.setText(enfermeria.getConsultation_reason_description());

            texto_examen_fisico.setText(enfermeria.getConsultation_reason_description());
            if (enfermeria.getConsultation_reason_audio() != null && !enfermeria.getConsultation_reason_audio().isEmpty()) {
                String path_audio = enfermeria.getConsultation_reason_audio();
                if (!path_audio.isEmpty()) {
                    archivo_fisico = new File(path_audio);
                    name_audio_fisico = archivo_fisico.getName();
                    detenerAudioFisico();
                }
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean seleccionado) {
        verificarEditextOtro();
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_historia_enfermeria2;
    }

    public boolean validar_campos() {
        boolean validacionCampos = utils.validarCamposRequeridos(etLargo,etAncho,spinner_pedio , spinner_femoral , spinner_tibial);
        
        if (archivo_fisico == null && texto_examen_fisico.getText().toString().isEmpty()) {
            EditText texto_examen_fisico = f_examen_fisico.findViewById(R.id.ed_text);//(EditText) root_view.findViewById(R.id.texto_examen_fisico);
            texto_examen_fisico.setError(getResources().getString(R.string.nueva_consulta_enfermeria_validar_motivo));
            texto_examen_fisico.requestFocus();
            validacionCampos = false;
        }
        if (getSeleccionCheckBox(ulcer).isEmpty()) {
            TextView txt_evolucion = (TextView) root_view.findViewById(R.id.txt_manejo);
            txt_evolucion.setError(getResources().getString(R.string.nueva_consulta_enfermeria_validar_manejo));
            txt_evolucion.requestFocus();
            validacionCampos = false;
        }

        return validacionCampos;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detener_audio_fisico:
                detenerAudioFisico();
                break;
            case R.id.btn_registrar_historia:
            case R.id.btn_resumen:
                mostrarMensajeEspera(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        saveConsult();
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ulcer.removeAllViews();
        List<Integer> whitEditText = new ArrayList<Integer>();
        whitEditText.add(OTRA);
        whitEditText.add(APOSITO);
        if(ulcer.getChildCount()==0)
           data_otro_ulcer =utils.crearCheckBoxWithEditText(dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_ULCER),whitEditText, ulcer);
        setListenerCheckBox(ulcer);
        poblar_formulario(arguments.getInt(ID_CONSULTA));
    }


    public String getSeleccionCheckBox( LinearLayout s) {
        String datos = "";
        for (int i = 0; i < s.getChildCount(); i++) {
            View v = s.getChildAt(i);
            if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
                datos += ((CheckBox) v).getId() + ",";
            }
        }
        return datos;
    }

    public void setListenerCheckBox(LinearLayout layout)
    {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof CheckBox ) {
                ((CheckBox) v).setOnCheckedChangeListener(this);
            }
        }
    }


    public void verificarEditextOtro()
    {
        for(Map.Entry<CheckBox,EditText> param :data_otro_ulcer.entrySet() )
        {
            if(param.getKey().isChecked())
                ((LinearLayout) param.getValue().getParent()).setVisibility(View.VISIBLE);
            else
                ((LinearLayout) param.getValue().getParent()).setVisibility(View.GONE);
        }
    }





    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(speech != null)
            speech.shutdown();
    }



}








