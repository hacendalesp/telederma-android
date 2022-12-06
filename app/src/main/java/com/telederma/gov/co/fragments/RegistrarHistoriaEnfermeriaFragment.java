package com.telederma.gov.co.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.telederma.gov.co.MainActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegistrarHistoriaEnfermeriaFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    View root_view;
    LinearLayout etimologico , nota ,linear_intensidad;
    public final int SIN_DIAGNOSTICO          = 7;
    public final int OTRA                     = 6;
    public  final String  ID_CONSULTA         = "id_consulta";
    public  final String  RESUMEN             = "resumen";
    public  final String  INFORMACION         = "informacion_paciente";

    public Map<CheckBox,EditText> data_otro_etimologico = new HashMap<>();
    Bundle arguments;
    Button btn_registrar;
    SeekBar seekBar_intensidad;
    TextView textView;
    EditText weight;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        btn_registrar = (Button) root_view.findViewById(R.id.btn_registrar_historia);
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        header.setText(getResources().getString(R.string.nueva_consulta_registro_paso2));
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
        toolbar.setTitle(getString(R.string.nueva_consulta_title_paso2));
        etimologico = (LinearLayout) root_view.findViewById(R.id.id_diagnosticos_etimologico);
        nota        = (LinearLayout) root_view.findViewById(R.id.id_nota);
        linear_intensidad = (LinearLayout) root_view.findViewById(R.id.linear_intensidad);
        arguments = getArguments();
        btn_registrar.setOnClickListener(this);

        seekBar_intensidad = (SeekBar) root_view.findViewById(R.id.id_intensidad);
        textView = (TextView) root_view.findViewById(R.id.count);
        weight = (EditText) root_view.findViewById(R.id.weight);

        seekBar_intensidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        return root_view;
    }

    public void saveConsult() {
        ocultarMensajeEspera();
        if (!validar_campos()) {
            return;
        }

        if(getSeleccionCheckBox(etimologico).contains(SIN_DIAGNOSTICO+""))
        {
            final Session session = Session.getInstance(getActivity());
            PendienteSincronizacion pendiente = new PendienteSincronizacion();
            ///////////////////////////INFORMACION PACIENTE /////////////////////////////
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(arguments.getInt(INFORMACION) + "");
            pendiente.setTable(InformacionPaciente.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        else
            {
                ConsultaEnfermeria consultaEnfermeria = null ;
                if(arguments.containsKey(ID_CONSULTA))
                    consultaEnfermeria = dbUtil.getConsultaEnfermeria(arguments.getInt(ID_CONSULTA));
                else
                    consultaEnfermeria = new ConsultaEnfermeria();
                consultaEnfermeria.setWeight(weight.getText().toString());
                consultaEnfermeria.setUlcer_etiology(getSeleccionCheckBox(etimologico));
                consultaEnfermeria.setUlcer_etiology_other(data_otro_etimologico.get((CheckBox)root_view.findViewWithTag("checkbox_"+OTRA)).getText().toString());
                consultaEnfermeria.setPain_intensity(seekBar_intensidad.getProgress()+"");

                dbUtil.crearConsultaEnfermeria(consultaEnfermeria);

                arguments.putInt(ID_CONSULTA, consultaEnfermeria.getId());
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment;
                fragment = new RegistrarHistoriaEnfermeria2Fragment();
                fragment.setArguments(arguments);
                ft.replace(R.id.paso1, fragment).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();

            }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean seleccionado) {

        verificarSintomas();
        verificarEditextOtro();
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_historia_enfermeria;
    }

    public boolean validar_campos() {
        boolean validacionCampos = true;

        validacionCampos = utils.validarCamposRequeridos(weight);

        if (getSeleccionCheckBox(etimologico).isEmpty()) {
            TextView txt_evolucion = (TextView) root_view.findViewById(R.id.txt_etimologico);
            txt_evolucion.setError(getResources().getString(R.string.nueva_consulta_enfermeria_validar_diagnostico_etimologico));
            txt_evolucion.requestFocus();
            validacionCampos = false;
        }
        return validacionCampos;
    }
    public void verificarEditextOtro()
    {
        for(Map.Entry<CheckBox,EditText> param :data_otro_etimologico.entrySet() )
        {
            if(param.getKey().isChecked())
                ((LinearLayout) param.getValue().getParent()).setVisibility(View.VISIBLE);
            else
                ((LinearLayout) param.getValue().getParent()).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        etimologico.removeAllViews();
        List<Integer> whitEditText = new ArrayList<Integer>();
        whitEditText.add(OTRA);

        if(etimologico.getChildCount()==0)
           data_otro_etimologico =utils.crearCheckBoxWithEditText(dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_ULCER_ETIOLOGY),whitEditText, etimologico);

        setListenerCheckBox();
        verificarSintomas();
        poblar_formulario(arguments.getInt(ID_CONSULTA));
    }





    public void verificarSintomas()
    {
        if(getSeleccionCheckBox(etimologico).contains(SIN_DIAGNOSTICO+""))
        {
            for (int i = 0; i < etimologico.getChildCount(); i++) {
                View v = etimologico.getChildAt(i);
                if (v instanceof CheckBox) {
                    Integer id_check = ((CheckBox) v).getId();
                    if (id_check == SIN_DIAGNOSTICO){
                        ((CheckBox) v).setChecked(true);
                        nota.setVisibility(View.VISIBLE);
                        linear_intensidad.setVisibility(View.GONE);
                    }
                    else
                        ((CheckBox) v).setChecked(false);
                }
            }
        }
        else
            {
                nota.setVisibility(View.GONE);
                linear_intensidad.setVisibility(View.VISIBLE);
            }

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
    public void setListenerCheckBox()
    {
        for (int i = 0; i < etimologico.getChildCount(); i++) {
            View v = etimologico.getChildAt(i);
            if (v instanceof CheckBox ) {
                ((CheckBox) v).setOnCheckedChangeListener(this);
            }
        }
    }

    public void poblar_formulario(int id_consulta_enfermeria) {
        ConsultaEnfermeria enfermeria = dbUtil.getConsultaEnfermeria(id_consulta_enfermeria) ;

        if(enfermeria!= null)
        {
            if (enfermeria.getUlcer_etiology().indexOf(",") > -1) {
                String[] arr_etimology = enfermeria.getUlcer_etiology().split(",");
                for (int i = 0; i < arr_etimology.length; i++) {
                    for (int j = 0; j < etimologico.getChildCount(); j++) {
                        View v = etimologico.getChildAt(j);
                        if (v instanceof CheckBox && ((CheckBox) v).getId() == Integer.parseInt(arr_etimology[i])) {
                            ((CheckBox) v).setChecked(true);
                        }
                    }
                }
            }
            data_otro_etimologico.get((CheckBox)root_view.findViewWithTag("checkbox_"+OTRA)).setText(enfermeria.getUlcer_etiology_other());
            seekBar_intensidad.setProgress(Integer.parseInt(enfermeria.getPain_intensity()));
            textView.setText(" " + seekBar_intensidad.getProgress() + " / " + seekBar_intensidad.getMax());
        }
    }

}








