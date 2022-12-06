package com.telederma.gov.co.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.AutocompleteAdapterCie10;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.dialogs.ConfirmDialog;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.Constantes;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;


public class RegistrarHistoriaFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public  final int  SINTOMAS_PRURITO  = 1 ;
    public  final int  SINTOMAS_ARDOR    = 2 ;
    public  final int  SINTOMAS_DOLOR    = 3 ;
    public  final int  SINTOMAS_NINGUNO  = 4 ;
    public  final String  RESPUESTA_POSITIVA  = "Si" ;


    EditText id_consulta_tiempo, id_consulta_personales, id_consulta_recibidos, id_consulta_familiares, id_consulta_efectos, id_consulta_diagnostico, id_consulta_sustancias, id_consulta_otros_factores, motivo_de_consulta, enfermedad_actual;
    Spinner id_consulta_unidad, id_consulta_lesiones;
    SwitchCompat id_recibido, id_agravan_los_sintomas, id_antecedentes, id_efectos, id_factores;
    TextView id_txt_antecedentes, id_txt_familiares;
    Integer id_information;
    Integer id_consulta;
    Bundle arguments;
    View root_view;
    Button btn_resumen, btn_registrar;
    Boolean precargado_sugerencia = false, resumen = false;

    RadioGroup cambios, id_consulta_supuran, id_consulta_exudan, id_consulta_sangran;
    LinearLayout lesiones,sintomas;
    Map<String, Integer> constantes = new HashMap<String, Integer>() {{
        put(Parametro.TIPO_PARAMETRO_NUMERO_LESIONES, R.id.id_consulta_lesiones);
        put(Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA, R.id.id_consulta_unidad);
    }};
    Integer id_cie10;
    AutocompleteAdapterCie10 adapter_autocomplete;
    AutoCompleteTextView ac_cie10;
    Cie10 cie10_default = null;
    String tag = "RegistrarHistoriaFragment";
    Cie10 cie10=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        asignarEventoOcultarTeclado(root_view);
        id_cie10 = null;
        //L989
        cie10_default = obtenerCie10sPorCodigo("L989");
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        header.setText(getResources().getString(R.string.nueva_consulta_registro_paso2));
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
        toolbar.setTitle(getString(R.string.nueva_consulta_title_paso2));
        id_consulta_tiempo = (EditText) root_view.findViewById(R.id.id_consulta_tiempo);
        id_consulta_personales = (EditText) root_view.findViewById(R.id.id_consulta_personales);
        id_consulta_familiares = (EditText) root_view.findViewById(R.id.id_consulta_familiares);
        id_consulta_efectos = (EditText) root_view.findViewById(R.id.id_consulta_efectos);
        id_consulta_sustancias = (EditText) root_view.findViewById(R.id.id_consulta_sustancias);
        id_consulta_recibidos = (EditText) root_view.findViewById(R.id.id_consulta_recibidos);
        id_consulta_otros_factores = (EditText) root_view.findViewById(R.id.id_consulta_otros_factores);

        //////CAMPOS NUEVOS
        motivo_de_consulta = (EditText) root_view.findViewById(R.id.motivo_de_consulta);
        enfermedad_actual = (EditText) root_view.findViewById(R.id.enfermedad_actual);
        /////////////

        id_consulta_diagnostico = (EditText) root_view.findViewById(R.id.id_consulta_diagnostico);
        id_consulta_unidad = (Spinner) root_view.findViewById(R.id.id_consulta_unidad);
        id_consulta_lesiones = (Spinner) root_view.findViewById(R.id.id_consulta_lesiones);
        id_recibido = (SwitchCompat) root_view.findViewById(R.id.id_recibido);
        id_agravan_los_sintomas = (SwitchCompat) root_view.findViewById(R.id.id_agravan_los_sintomas);
        id_antecedentes = (SwitchCompat) root_view.findViewById(R.id.id_antecedenes);
        id_efectos = (SwitchCompat) root_view.findViewById(R.id.id_efectos);
        id_factores = (SwitchCompat) root_view.findViewById(R.id.id_factores);
        id_txt_antecedentes = (TextView) root_view.findViewById(R.id.id_txt_antecedentes);
        id_txt_familiares = (TextView) root_view.findViewById(R.id.id_txt_familiares);
        cambios = (RadioGroup) root_view.findViewById(R.id.id_consulta_sintomas_empeoran);
        sintomas = (LinearLayout) root_view.findViewById(R.id.id_consulta_sintomas_prurito);
        id_consulta_supuran = (RadioGroup) root_view.findViewById(R.id.id_consulta_supuran);
        id_consulta_exudan = (RadioGroup) root_view.findViewById(R.id.id_consulta_exudan);
        id_consulta_sangran = (RadioGroup) root_view.findViewById(R.id.id_consulta_sangran);
        lesiones = (LinearLayout) root_view.findViewById(R.id.id_lesiones);
        btn_registrar = (Button) root_view.findViewById(R.id.btn_registrar_historia);
        btn_resumen = (Button) root_view.findViewById(R.id.btn_resumen);
        ac_cie10 = (AutoCompleteTextView) root_view.findViewById(R.id.id_consulta_cie10);

        arguments = getArguments();
        id_information = arguments.getInt("informacion_paciente");
        resumen = arguments.getBoolean("resumen");

        id_recibido.setOnCheckedChangeListener(this);
        id_agravan_los_sintomas.setOnCheckedChangeListener(this);
        id_antecedentes.setOnCheckedChangeListener(this);
        id_efectos.setOnCheckedChangeListener(this);
        id_factores.setOnCheckedChangeListener(this);
        btn_registrar.setOnClickListener(this);
        btn_resumen.setOnClickListener(this);

        for (Map.Entry<String, Integer> constante : constantes.entrySet())
            poblar_spinner(root_view.findViewById(constante.getValue()),
                    dbUtil.obtenerParametros(constante.getKey()));

        if (resumen) {
            btn_registrar.setVisibility(View.GONE);
            btn_resumen.setVisibility(View.VISIBLE);
        } else {
            btn_registrar.setVisibility(View.VISIBLE);
            btn_resumen.setVisibility(View.GONE);
        }


        /////////////////////////////autocomplete aseguradora///////////////////////////////////////////////////
        List<Cie10> cie10s = new ArrayList<Cie10>();
        adapter_autocomplete = new AutocompleteAdapterCie10(new ListFilter(), getActivity(), R.layout.autocomplete_item
                , cie10s);
        ac_cie10.setAdapter(adapter_autocomplete);
        ac_cie10.setOnItemClickListener(onItemClickListener);
//        ac_cie10.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                //if(ac_cie10.getText().toString().trim().length() == 0 || id_cie10 == null) {
//                    //precargado_sugerencia = false;
//                    //id_cie10 = null;
//                //}
//                return false;
//            }
//        });

        //Consultamos los antescedentes personales y famliares
        //int paciente_id = arguments.getInt("paciente_id");
        //obtenerAntescedentes(paciente_id);


        fillFieldsDefault();

        return root_view;
    }


    public void saveConsult() {
        if (!validar_campos()) {
            ocultarMensajeEspera();
            mostrarMensaje(R.string.mensaje_validacion_campos, MSJ_ERROR);
            return;
        }

        ConsultaMedica consulta_medica = getConsulta(id_consulta);
        consulta_medica.setUnidadMedidaTiempoEvolucion(((Parametro) id_consulta_unidad.getSelectedItem()).getValor());
        consulta_medica.setNumeroLesiones(((Parametro) id_consulta_lesiones.getSelectedItem()).getValor());
        consulta_medica.setEvolucionLesiones(getSeleccionCheckBox(lesiones));
        consulta_medica.setSangran(((RadioButton)root_view.findViewById(id_consulta_sangran.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA));
        consulta_medica.setExudan(((RadioButton)root_view.findViewById(id_consulta_exudan.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA));
        consulta_medica.setSupuran(((RadioButton)root_view.findViewById(id_consulta_supuran.getCheckedRadioButtonId())).getText().equals(RESPUESTA_POSITIVA));
        consulta_medica.setSintomas(getSeleccionCheckBox(sintomas));
        consulta_medica.setSintomasCambian(((Integer) cambios.getCheckedRadioButtonId() == -1) ? null : cambios.getCheckedRadioButtonId());
        consulta_medica.setOtrosFactoresAgravenSintomas(id_consulta_otros_factores.getText().toString());


        consulta_medica.setFactoresAgravan(id_consulta_sustancias.getText().toString());
        consulta_medica.setAntecedentesFamiliares(id_consulta_familiares.getText().toString());
        consulta_medica.setAntecedentesPersonales(id_consulta_personales.getText().toString());
        consulta_medica.setTratamientoRecibido(id_consulta_recibidos.getText().toString());
        consulta_medica.setSustanciasAplicadas(id_consulta_sustancias.getText().toString());
        consulta_medica.setEfectoTratamiento(id_consulta_efectos.getText().toString());
        consulta_medica.setIdInformacionPacienteLocal(id_information);

        ///CAMPOS NUEVOS
        consulta_medica.setMotivoConsulta(motivo_de_consulta.getText().toString());
        consulta_medica.setEnfermedadActual(enfermedad_actual.getText().toString());
        //////////////

        //consulta_medica.setTiempoEvolucion(Float.parseFloat(id_consulta_tiempo.getText().toString()));
        consulta_medica.setTiempoEvolucion(Integer.parseInt(id_consulta_tiempo.getText().toString()));
        //consulta_medica.setImpresionDiagnostica(id_consulta_diagnostico.getText().toString());

        if(id_cie10 != null) {
            Cie10 cie10 = getCie10(id_cie10);
            if (cie10 != null)
                consulta_medica.setImpresionDiagnostica(cie10.getName()+ " - "+ cie10.getCode());
                consulta_medica.setCiediezcode(cie10.getCode());

        }
//        consulta_medica.setImpresionDiagnostica("Sindrome del miembro fantasma sin dolor");
//        consulta_medica.setCiediezcode("G547");

        dbUtil.crearConsultaMedica(consulta_medica);
        ocultarMensajeEspera();


        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        arguments.putInt("id_consulta", consulta_medica.getId());
        arguments.putBoolean("resumen", false);

        if (resumen) {
            fragment = new ResumenFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).commit();
        } else {
            fragment = new RegistrarExamenFisicoFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }

    }

    public ConsultaMedica getConsulta(Integer consulta_id) {
        ConsultaMedica consulta = new ConsultaMedica();
        try {
            if (consulta_id == null)
                consulta_id = 0;
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

    private void poblar_spinner(Spinner spinnerCustom, List<Parametro> data) {
        CustomSpinnerAdapter<Parametro> customSpinnerAdapter = new CustomSpinnerAdapter<>(
                getActivity().getApplicationContext(), data, Parametro.class
        );
        spinnerCustom.setAdapter((SpinnerAdapter) customSpinnerAdapter);
    }

    public void poblar_formulario(int consulta_id) {
        ConsultaMedica consulta = getConsulta(consulta_id);
        id_consulta_unidad.setSelection((((CustomSpinnerAdapter<Parametro>) id_consulta_unidad.getAdapter()).getPosition(consulta.getUnidadMedidaTiempoEvolucion())));
        id_consulta_lesiones.setSelection((((CustomSpinnerAdapter<Parametro>) id_consulta_lesiones.getAdapter()).getPosition(consulta.getNumeroLesiones())));

        if (!consulta.getEfectoTratamiento().toString().matches(""))
            id_efectos.setChecked(true);
        if (!consulta.getSustanciasAplicadas().toString().matches(""))
            id_agravan_los_sintomas.setChecked(true);

        if (!consulta.getAntecedentesPersonales().toString().matches("") || !consulta.getAntecedentesFamiliares().toString().matches(""))
            id_antecedentes.setChecked(true);

        if (!consulta.getTratamientoRecibido().toString().matches(""))
            id_recibido.setChecked(true);

        if (!consulta.getOtrosFactoresAgravenSintomas().toString().matches(""))
            id_factores.setChecked(true);


        ((RadioButton) root_view.findViewById ((consulta.getSupuran()) ? R.id.id_consulta_cambian_supuran_si : R.id.id_consulta_cambian_supuran_no)).setChecked(true);
        ((RadioButton) root_view.findViewById((consulta.getExudan()) ? R.id.id_consulta_cambian_exudan_si : R.id.id_consulta_cambian_exudan_no)).setChecked(true);
        ((RadioButton) root_view.findViewById((consulta.getSangran()) ? R.id.id_consulta_cambian_sangran_si : R.id.id_consulta_cambian_sangran_no)).setChecked(true);
        if (consulta.getEvolucionLesiones().indexOf(",") > -1) {
            String[] arr_lesiones = consulta.getEvolucionLesiones().split(",");
            for (int i = 0; i < arr_lesiones.length; i++) {
                for (int j = 0; j < lesiones.getChildCount(); j++) {
                    View v = lesiones.getChildAt(j);
                    if (v instanceof CheckBox && ((CheckBox) v).getId() == Integer.parseInt(arr_lesiones[i])) {
                        ((CheckBox) v).setChecked(true);
                        break;
                    }
                }
            }
        }

//        for (int j = 0; j < lesiones.getChildCount(); j++) {
//            View v = lesiones.getChildAt(j);
//            if (v instanceof CheckBox) {
//                ((CheckBox) v).setChecked(true);
//            }
//        }

        if (consulta.getSintomas().indexOf(",") > -1) {
            String[] arr_sintomas = consulta.getSintomas().split(",");
            for (int i = 0; i < arr_sintomas.length; i++) {
                for (int j = 0; j < sintomas.getChildCount(); j++) {
                    View v = sintomas.getChildAt(j);
                    if (v instanceof CheckBox && ((CheckBox) v).getId() == Integer.parseInt(arr_sintomas[i])) {
                        ((CheckBox) v).setChecked(true);
                    if(Integer.parseInt(arr_sintomas[i]) == SINTOMAS_NINGUNO)
                        cambios.setVisibility(View.GONE);
                    }
                }
            }
        }

        id_consulta_otros_factores.setText(consulta.getOtrosFactoresAgravenSintomas());
        id_consulta_personales.setText(consulta.getAntecedentesPersonales());
        id_consulta_familiares.setText(consulta.getAntecedentesFamiliares());
        id_consulta_recibidos.setText(consulta.getTratamientoRecibido());
        id_consulta_sustancias.setText(consulta.getSustanciasAplicadas());

        /////CAMPOS NUEVOS
        motivo_de_consulta.setText(consulta.getMotivoConsulta());
        enfermedad_actual.setText(consulta.getEnfermedadActual());
        //////////

        id_consulta_efectos.setText(consulta.getEfectoTratamiento());
        id_consulta_tiempo.setText(consulta.getTiempoEvolucion() + "");
        id_consulta_diagnostico.setText(consulta.getCiediezcode());
        cambios.check(consulta.getSintomasCambian());

        if(consulta.getImpresionDiagnostica() != null && consulta.getCiediezcode() != null) {
            cie10 = dbUtil.getCie10ByCode(consulta.getCiediezcode());
            if (cie10 != null) {
                ac_cie10.setText(cie10.getName()+ " - "+cie10.getCode()  );
                ac_cie10.setSelection(0);
                ac_cie10.setSelected(true);
                id_cie10 = cie10.getIdServidor();
                Log.i(tag, "cie10_selected from poblar formulario =>"+id_cie10);
                //precargado_sugerencia = true;
            }
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean seleccionado) {
        switch (compoundButton.getId()) {
            case R.id.id_factores:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), id_consulta_otros_factores);
                break;
            case R.id.id_recibido:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), id_consulta_recibidos);
                break;
            case R.id.id_agravan_los_sintomas:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), id_consulta_sustancias);
                break;
            case R.id.id_antecedenes:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), id_txt_antecedentes, id_consulta_personales, id_txt_familiares, id_consulta_familiares);
                break;
            case R.id.id_efectos:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), id_consulta_efectos);
                break;
            case SINTOMAS_PRURITO:
            case SINTOMAS_ARDOR:
            case SINTOMAS_DOLOR:
            case SINTOMAS_NINGUNO:
                 controlarSintomas(compoundButton, seleccionado);//verificarSintomas();
                break;
        }
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_historia;
    }

    public boolean validar_campos() {
        boolean validacionCampos = utils.validarCamposRequeridos(id_consulta_tiempo, id_consulta_lesiones, id_consulta_unidad, ac_cie10, motivo_de_consulta, enfermedad_actual);
        final ScrollView sv_content = (ScrollView)root_view.findViewById(R.id.sv_content);
        if (getSeleccionCheckBox(lesiones).isEmpty()) {
            TextView txt_evolucion = (TextView) root_view.findViewById(R.id.txt_evolucion);
            txt_evolucion.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_validar_evolucion));
            txt_evolucion.requestFocus();
            validacionCampos = false;

            int[] location = new int[2];
            txt_evolucion.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];

            sv_content.post(new Runnable() {
                @Override
                public void run() {
                    sv_content.scrollTo(0, y);
                }
            });

//            sv_content.post(new Runnable() {
//                @Override
//                public void run() {
//                    View id_lesiones = root_view.findViewById(R.id.id_lesiones);
//                    sv_content.scrollTo(0, id_lesiones.getTop()-20);
//                }
//            });
        }else{
            TextView txt_evolucion = (TextView) root_view.findViewById(R.id.txt_evolucion);
            txt_evolucion.setError(null);
        }

        if (!utils.validarCamposRequeridos(id_consulta_lesiones)) {
            int[] location = new int[2];
            id_consulta_lesiones.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            //Toast.makeText(contexto, "posicion-lesiones==>"+y, Toast.LENGTH_LONG).show();

            sv_content.post(new Runnable() {
                @Override
                public void run() {
                    sv_content.scrollTo(0, y);
                }
            });

        }
        if (getSeleccionCheckBox(sintomas).isEmpty()) {
            TextView txt_sistomas = (TextView) root_view.findViewById(R.id.txt_id_consulta_sintomas);
            txt_sistomas.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_sintomas));
            txt_sistomas.requestFocus();
            validacionCampos = false;

            int[] location = new int[2];
            txt_sistomas.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            //Toast.makeText(contexto, "posicion-sintomas==>"+y, Toast.LENGTH_LONG).show();

            sv_content.post(new Runnable() {
                @Override
                public void run() {
                    sv_content.scrollTo(0, y);
                }
            });
        }else{
            TextView txt_evolucion = (TextView) root_view.findViewById(R.id.txt_id_consulta_sintomas);
            txt_evolucion.setError(null);
        }
        if(id_cie10 == null){
            validacionCampos = false;
            ac_cie10.setError("Debes seleccionar una opción");
            ac_cie10.requestFocus();

            if(cie10_default != null) {
                String text = "El sistema agregará por defecto el CIE10 " + cie10_default.getName();
                ConfirmDialog confirm_dialog = new ConfirmDialog(contexto, R.string.dialog_confirm_estas_seguro_no_impresion_diagnostica, text);
                confirm_dialog.show();
                Button btn_send = confirm_dialog.btn_send;
                Button btn_cancel = confirm_dialog.btn_cancel;
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirm_dialog.dismiss();
                    }
                });
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cie10_default != null) {
                            cie10 = cie10_default;
                            ac_cie10.setText(cie10_default.getName() + " - "+cie10_default.getCode());
                            ac_cie10.setError(null);
                            ac_cie10.setSelection(0);
                            ac_cie10.setSelected(true);
                            id_cie10 = cie10_default.getIdServidor();
                            Log.i(tag, "cie10_selected from poblar modal =>"+id_cie10);

                            //precargado_sugerencia = true;
                            //ac_cie10.performCompletion();
//                            if(ac_cie10 != null){
//
//                            }

                        }
                        confirm_dialog.dismiss();
                    }
                });


            }
        }


        return validacionCampos;
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

    public int checkBoxSeleccionados( LinearLayout s) {
        int datos = 0;
        for (int i = 0; i < s.getChildCount(); i++) {
            View v = s.getChildAt(i);
            if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
                datos += 1;
            }
        }
        return datos;
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


    public void controlarSintomas(CompoundButton compoundButton, boolean seleccionado){
        Log.i(tag, "sintomas id ==> "+compoundButton.getId()+" seleccionado ==> "+seleccionado+" == sintomas ==>"+getSeleccionCheckBox(sintomas));


        // acá deja seleccionado la opción ninguno
        if(getSeleccionCheckBox(sintomas).contains(SINTOMAS_NINGUNO+"")){
            boolean remove_ninguno = false;
            if(getSeleccionCheckBox(sintomas).split(",").length > 1)
                remove_ninguno = true;
            cambios.setVisibility(View.GONE);
            int temp_i = (int)SINTOMAS_NINGUNO - 1;
            View temp_v = sintomas.getChildAt(temp_i);

            if(compoundButton.getId() == SINTOMAS_NINGUNO && ((CheckBox) temp_v).isChecked()){
                for (int i = 0; i < sintomas.getChildCount(); i++) {
                    View v = sintomas.getChildAt(i);
                    if (v instanceof CheckBox ) {
                        Integer id_check = ((CheckBox) v).getId();
                        if (id_check == SINTOMAS_NINGUNO)
                            ((CheckBox) v).setChecked(true);
                        else
                            ((CheckBox) v).setChecked(false);
                    }
                }
            }else {
            // acá remueve la opción ninguno
                for (int i = 0; i < sintomas.getChildCount(); i++) {
                    View v = sintomas.getChildAt(i);
                    if (v instanceof CheckBox) {
                        Integer id_check = ((CheckBox) v).getId();

                        if (id_check == SINTOMAS_NINGUNO && remove_ninguno)
                            ((CheckBox) v).setChecked(false);
                        else {
                            //((CheckBox) v).setChecked(false);
                        }
                    }
                }
            }
        }
        else
            cambios.setVisibility(View.VISIBLE);

    }

    public void verificarSintomas(){
        if(getSeleccionCheckBox(sintomas).contains(SINTOMAS_NINGUNO+""))
        {
            cambios.setVisibility(View.GONE);
            for (int i = 0; i < sintomas.getChildCount(); i++) {
                View v = sintomas.getChildAt(i);
                if (v instanceof CheckBox ) {
                   Integer id_check = ((CheckBox) v).getId();
                    if (id_check == SINTOMAS_NINGUNO)
                       ((CheckBox) v).setChecked(true);
                    else
                        ((CheckBox) v).setChecked(false);
                }
            }
        }
        else
            cambios.setVisibility(View.VISIBLE);
    }

    public void sintomasDefault(){

        boolean is_selected=false;
        int index_ninguno = 0;
        for (int i = 0; i < sintomas.getChildCount(); i++) {
            View v = sintomas.getChildAt(i);
            if (v instanceof CheckBox ) {
                Integer id_check = ((CheckBox) v).getId();
                Log.i(tag, "id_consulta sintomas=="+id_check);
                if(((CheckBox) v).isChecked())
                    is_selected = true;
                if (id_check == SINTOMAS_NINGUNO) {
                    index_ninguno = i;
                }
            }
        }
        if (is_selected==false && index_ninguno != 0) {
            View v = sintomas.getChildAt(index_ninguno);
            ((CheckBox) v).setChecked(true);
        }
        /*if (!is_selected && index_ninguno != 0) {
            View v = sintomas.getChildAt(index_ninguno);
            ((CheckBox) v).setChecked(true);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    // Este es el listener del checkbox de sintomas
    public void setListenerCheckBox(){
        for (int i = 0; i < sintomas.getChildCount(); i++) {
            View v = sintomas.getChildAt(i);
            if (v instanceof CheckBox ) {
                ((CheckBox) v).setOnCheckedChangeListener(this);
            }
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    cie10 = ((Cie10) adapterView.getItemAtPosition(i));
                    id_cie10 = cie10.getIdServidor();
                    Log.i(tag, "cie10_selected from click =>"+id_cie10);

//                    InputMethodManager mgr = (InputMethodManager)contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    mgr.hideSoftInputFromWindow(ac_cie10.getWindowToken(), 0);
                }
            };


    private void fillFieldsDefault(){
        lesiones.removeAllViews();
        sintomas.removeAllViews();
        cambios.removeAllViews();

        if(cambios.getChildCount()==0)
            utils.crearRadios(dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_CAMBIO_DE_SINTOMAS), cambios);
        if(sintomas.getChildCount()==0) {
            //asigna lo valores de checkbox a los sintomas
            utils.crearCheckbox(dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_SINTOMA), sintomas);
        }
        if(lesiones.getChildCount()==0)
            utils.crearCheckbox(dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_EVOLUCION_LESIONES), lesiones);

        if (arguments.getInt("id_consulta") != 0) {
            id_consulta = arguments.getInt("id_consulta");
            poblar_formulario(id_consulta);
        }
        setListenerCheckBox();
        if (arguments.getInt("id_consulta") == 0) {
            Log.i(tag, "id_consulta == 0");
            sintomasDefault();
        }
    }


    public Cie10 getCie10(int idCie10Servidor) {
        RuntimeExceptionDao<Cie10, Integer> cie10DAO = getDbHelper().getCie10RuntimeDAO();
        QueryBuilder<Cie10, Integer> qb = cie10DAO.queryBuilder();
        try {
            qb.where().eq(
                    Cie10.NOMBRE_CAMPO_ID_SERVIDOR, idCie10Servidor
            );
            List<Cie10> list_cie10 = cie10DAO.query(qb.prepare());

            if (!list_cie10.isEmpty())
                return list_cie10.get(0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cie10> obtenerCie10s(String nombre) {
        List<Cie10> cie10s = new ArrayList<>();

        try {
            RuntimeExceptionDao<Cie10, Integer> cie10DAO = getDbHelper().getCie10RuntimeDAO();
            QueryBuilder<Cie10, Integer> queryBuilder = cie10DAO.queryBuilder();
            queryBuilder.where().like(
                    Cie10.NOMBRE_CAMPO_NAME, "%" + nombre + "%"
            ).or().like(
                    Cie10.NOMBRE_CAMPO_CODE, "%" + nombre + "%"
            );
            cie10s = cie10DAO.query(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando cie10s " + nombre, e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return cie10s;
    }

    public Cie10 obtenerCie10sPorCodigo(String code) {
        Cie10 cie10 = null;

        try {
            RuntimeExceptionDao<Cie10, Integer> cie10DAO = getDbHelper().getCie10RuntimeDAO();
            QueryBuilder<Cie10, Integer> queryBuilder = cie10DAO.queryBuilder();
            queryBuilder.where().eq(
                    Cie10.NOMBRE_CAMPO_CODE, code
            );
            cie10 = cie10DAO.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando cie10 " + code, e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return cie10;
    }


    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (!precargado_sugerencia) {
                //Toast.makeText(contexto, "reinicio de id_cie10", Toast.LENGTH_LONG).show();
                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        results.values = new ArrayList<String>();
                        results.count = 0;
                    }
                } else {
                    final String searchStrLowerCase = prefix.toString().toLowerCase();
                    List<Cie10> matchValues = obtenerCie10s(searchStrLowerCase);
                    results.values = matchValues;
                    results.count = matchValues.size();
                    if(results.count > 1 || cie10 == null) {
                        id_cie10 = null;
                        Log.i(tag, "cie10 reiniciado en busqueda ===>prefix : " + prefix);
                    }
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                //Log.i(tag, "filter_status values != null");
                adapter_autocomplete.setDataList((ArrayList<Cie10>) results.values);
            } else {
                //Log.i(tag, "filter_status values == null");
                //Toast.makeText(contexto, "results  null", Toast.LENGTH_LONG).show();
                if(!ac_cie10.isSelected()) {
                    id_cie10 = null;
                    precargado_sugerencia = false;
                    Log.i(tag, "cie10 filter_status values == null");

                }
                adapter_autocomplete.setDataList(null);
            }
            if (results.count > 0) {
                Log.i(tag, "cie10 filter_status count > 0");
                adapter_autocomplete.notifyDataSetChanged();
            } else {
                //Log.i(tag, "filter_status count == 0");
                //Toast.makeText(contexto, "count 0", Toast.LENGTH_LONG).show();
                if(!ac_cie10.isSelected()) {
                    id_cie10 = null;
                    precargado_sugerencia = false;
                    Log.i(tag, "cie10 filter_status count > 0 false");

                }
                adapter_autocomplete.notifyDataSetInvalidated();
            }
        }
    }

    public JSONObject obtenerAntescedentes(int paciente_id){
        try{
            obtenerConsultasMedicinaPacienteBD(paciente_id);

        }catch (Exception e){

        }

        return null;
    }

    private final void obtenerConsultasMedicinaPacienteBD(int idUsuario) {
        try {
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaMedicaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> consultaMedicaQueryBuilder = consultaMedicaDAO.queryBuilder();
            List<ConsultaMedica> consultas = new ArrayList<>();

            consultaMedicaQueryBuilder.where().eq(
                    ConsultaMedica.NOMBRE_CAMPO_ID_PACIENTE, idUsuario
            );

            consultaMedicaQueryBuilder.orderBy(ConsultaMedica.NOMBRE_CAMPO_UPDATED_AT, false);
            consultas = consultaMedicaDAO.query(consultaMedicaQueryBuilder.prepare());


            if (!consultas.isEmpty()) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}








