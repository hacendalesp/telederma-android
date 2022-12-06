package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.telederma.gov.co.Camara;
import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.Constantes;

public class RegistrarControlEnfermeriaFragment extends BaseFragment implements MediaRecorder.OnInfoListener, View.OnClickListener {

    public static final String ARG_MODEL                      ="modelo";
    public static final String ARG_VIEW                       ="tipo_view";

    Integer id_consulta;
    Bundle arguments;
    View root_view;
    SeekBar mejoria_subjetiva,intensidad_dolor;
    TextView textContMejoraSub ,textContIntensidadDolor ;
    RadioGroup concepto_enfermeria, tolerancia_medicamentos;
    EditText edit_ancho,edit_largo,edit_comment ;
    Button btn_crear_control;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_control_enfermeria;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        concepto_enfermeria = (RadioGroup) root_view.findViewById(R.id.id_concepto_enfermeria);
        tolerancia_medicamentos = (RadioGroup) root_view.findViewById(R.id.id_tolera_tratamiento);

        arguments = getArguments();
        id_consulta = arguments.getInt("id_consulta");


        edit_ancho = (EditText) root_view.findViewById(R.id.id_ancho);
        edit_largo = (EditText) root_view.findViewById(R.id.id_largo);
        edit_comment = (EditText) root_view.findViewById(R.id.id_control_enfermeria_comentarios);

        mejoria_subjetiva = (SeekBar) root_view.findViewById(R.id.seekBar_mejoria_subjetiva);
        textContMejoraSub = (TextView) root_view.findViewById(R.id.count);

        intensidad_dolor = (SeekBar) root_view.findViewById(R.id.seekBar_intensidad);
        textContIntensidadDolor = (TextView) root_view.findViewById(R.id.count2);

        btn_crear_control = (Button) root_view.findViewById(R.id.btn_crear_control);

        intensidad_dolor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                textContIntensidadDolor.setText(" " + seekBarProgress + " / " + seekBar.getMax());
            }

        });

        mejoria_subjetiva.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                textContMejoraSub.setText(" " + seekBarProgress + " / " + seekBar.getMax());
            }

        });
        btn_crear_control.setOnClickListener(this);

        return root_view;
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
        switch (view.getId()) {
            case R.id.btn_crear_control:
                save_control();
                break;
        }
    }

    public void save_control() {

        if (!utils.validarCamposRequeridos(edit_comment, edit_ancho,edit_largo))
            return;

        ControlEnfermeria controlEnfermeria = new ControlEnfermeria();
        controlEnfermeria.setMejoraSubjetiva(mejoria_subjetiva.getProgress()+"");
        controlEnfermeria.setTamanoUlceraAncho(Float.parseFloat(edit_ancho.getText().toString()));
        controlEnfermeria.setTamanoUlceraLargo(Float.parseFloat(edit_largo.getText().toString()));
        controlEnfermeria.setIntensidadDolor(intensidad_dolor.getProgress());
        controlEnfermeria.setToleraTratamiento(tolerancia_medicamentos.getCheckedRadioButtonId()==R.id.id_tolera_tratamiento_si);
        controlEnfermeria.setConceptoEnfermeriaMejoria(concepto_enfermeria.getCheckedRadioButtonId()==R.id.id_concepto_si);
        controlEnfermeria.setComentarios(edit_comment.getText().toString());
        controlEnfermeria.setIdConsulta(id_consulta);
        dbUtil.crearControlEnfermeria(controlEnfermeria);


        ocultarMensajeEspera();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        fragment = new PatologiaFragment();
        arguments.putInt(ARG_MODEL,controlEnfermeria.getId());
        arguments.putInt(ARG_VIEW,3);
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
    }
}


