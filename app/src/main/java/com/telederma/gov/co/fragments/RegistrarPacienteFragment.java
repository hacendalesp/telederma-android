package com.telederma.gov.co.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import org.joda.time.DateMidnight;
//import org.joda.time.DateTime;
//import org.joda.time.Days;
//import org.joda.time.LocalDate;
//import org.joda.time.Months;
//import org.joda.time.ReadableInstant;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.AutocompleteAdapter;
import com.telederma.gov.co.adapters.Custom2SpinnerAdapter;
//import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.modelo.Aseguradora;
import com.telederma.gov.co.modelo.Departamento;
import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Municipio;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;
import com.github.barteksc.pdfviewer.util.ArrayUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;
public class RegistrarPacienteFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String ARG_ID_PACIENTE             ="paciente_id";
    public static final String ARG_CEDULA                  ="cedula_paciente";
    public static final String ARG_RESUMEN                 ="resumen";
    public static final String ARG_PACIENTE_ID_LOCAL       ="paciente_id_local";
    public static final String ARG_INFORMACION_PACIENTE    ="informacion_paciente";
    public static final String ARG_TIPO_PROFESIONAL        ="tipo_profesional";
    //RC(Registro civil),MS(Menor sin identificar),CN(Certificado nacido vivo)

    Calendar calendar;
    CheckBox terms_conditions;
    SwitchCompat companion, responsible;
    EditText number_inpec, phone, email, address, second_surname, number_document, name, second_name, last_name, birthdate, name_companion, phone_companion, name_responsible, phone_responsible, relationship, age, authorization_number, occupation;
    Spinner type_user, type_document, purpose_consultation, external_cause, unit_measure_age, department_id, municipality, civil_status, type_condition;
    RadioGroup genre, urban_zone;
    LinearLayout linear_responsable, linear_acompanante;
    Integer id_municipio;
    Integer id_insure;
    Integer id_server_paciente;
    Integer id_informacion_paciente = null;
    TextView txt_id_paciente_condicion, txt_paciente_numero_identificacion, txt_number_inpec;
    Map<String, Integer> constantes = new HashMap<String, Integer>() {{
        put(Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO, R.id.id_paciente_tipo_documento);
        put(Parametro.TIPO_PARAMETRO_TIPO_CONDICION, R.id.id_paciente_condicion);
        put(Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA, R.id.id_paciente_unidad_medida);
        put(Parametro.TIPO_PARAMETRO_TIPO_USUARIO, R.id.id_paciente_tipo_usuario);
        put(Parametro.TIPO_PARAMETRO_PROPOSITO_DE_CONSULTA, R.id.id_paciente_finalidad);
        put(Parametro.TIPO_PARAMETRO_ESTADO_CIVIL, R.id.id_paciente_estado_civil);
        put(Parametro.TIPO_PARAMETRO_CAUSA_EXTERNA, R.id.id_paciente_causa_externa);
    }};
    //CC= Cédula ciudadanía
    //CE= Cédula de extranjería
    //CD= Carné diplomático
    //PA= Pasaporte
    //SC= Salvoconducto
    //PE = Permiso Especial de Permanencia
    //RE= Residente Especial para la paz
    //RC=  Registro civil
    //TI= Tarjeta de identidad
    //CN= Certificado de nacido vivo
    //AS= Adulto sin identificar
    //MS= Menor sin identificar
    private int CC=-7, CE=-8, CD=-9, PA=-10, SC=-11, PE=-12, RE=-13, RC=-14, TI=-15, CN=-16, AS=-17, MS=-18;
    private int ANIOS=-36, MESES=-37, DIAS=-38;
    Map<String, RadioGroup> constantes_radios = new HashMap<String, RadioGroup>();
    AutocompleteAdapter adapter_autocomplete;
    AutoCompleteTextView aseguradora;
    Bundle arguments;
    Boolean precargado_sugerencia, resumen = false;
    Button btn_registrar, btn_resumen;
    View root_view;
    protected Utils utils;
    List<Parametro> data_type_documents = new ArrayList<>();
    List<Parametro> data_type_conditions = new ArrayList<>();
    boolean set_municipality = false;
    Integer temp_id_municipality=null;
    String tag = "RegistrarPacienteFragment";
    String fields_erors = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        utils = Utils.getInstance(getActivity());

        asignarEventoOcultarTeclado(root_view);
        id_municipio = null;
        id_insure = null;
        id_server_paciente = null;
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        if (header != null)
            header.setText(getResources().getString(R.string.nueva_consulta_registro_paso1));
        calendar = Calendar.getInstance();

        terms_conditions = (CheckBox) root_view.findViewById(R.id.id_paciente_consentimiento);
        companion = (SwitchCompat) root_view.findViewById(R.id.acompanante);
        responsible = (SwitchCompat) root_view.findViewById(R.id.responsable);
        phone = (EditText) root_view.findViewById(R.id.id_paciente_celular);
        email = (EditText) root_view.findViewById(R.id.id_paciente_email);
        number_inpec = (EditText) root_view.findViewById(R.id.id_paciente_number_inpec);
        address = (EditText) root_view.findViewById(R.id.id_paciente_direccion);
        second_surname = (EditText) root_view.findViewById(R.id.id_paciente_segundo_apellido);
        number_document = (EditText) root_view.findViewById(R.id.id_paciente_numero_identificacion);
        name = (EditText) root_view.findViewById(R.id.id_paciente_primer_nombre);
        second_name = (EditText) root_view.findViewById(R.id.id_paciente_segundo_nombre);
        last_name = (EditText) root_view.findViewById(R.id.id_paciente_primer_apellido);
        birthdate = (EditText) root_view.findViewById(R.id.id_paciente_fecha_nacimiento);
        name_companion = (EditText) root_view.findViewById(R.id.id_paciente_acompanante_nombre);
        phone_companion = (EditText) root_view.findViewById(R.id.id_paciente_acompanante_celular);
        name_responsible = (EditText) root_view.findViewById(R.id.id_paciente_responsable_nombre);
        phone_responsible = (EditText) root_view.findViewById(R.id.id_paciente_responsable_nombre_celular);
        relationship = (EditText) root_view.findViewById(R.id.id_paciente_responsable_parentesco);
        age = (EditText) root_view.findViewById(R.id.id_paciente_edad);
        authorization_number = (EditText) root_view.findViewById(R.id.id_paciente_numero_autorizacion);
        occupation = (EditText) root_view.findViewById(R.id.id_paciente_ocupacion);
        type_user = (Spinner) root_view.findViewById(R.id.id_paciente_tipo_usuario);
        type_document = (Spinner) root_view.findViewById(R.id.id_paciente_tipo_documento);
        purpose_consultation = (Spinner) root_view.findViewById(R.id.id_paciente_finalidad);
        external_cause = (Spinner) root_view.findViewById(R.id.id_paciente_causa_externa);
        unit_measure_age = (Spinner) root_view.findViewById(R.id.id_paciente_unidad_medida);
        department_id = (Spinner) root_view.findViewById(R.id.id_paciente_departamento);
        civil_status = (Spinner) root_view.findViewById(R.id.id_paciente_estado_civil);
        type_condition = (Spinner) root_view.findViewById(R.id.id_paciente_condicion);
        municipality = (Spinner) root_view.findViewById(R.id.id_paciente_municipio);
        genre = (RadioGroup) root_view.findViewById(R.id.id_paciente_opcion_sexo);
        urban_zone = (RadioGroup) root_view.findViewById(R.id.id_paciente_opcion_residencial);
        linear_acompanante = (LinearLayout) root_view.findViewById(R.id.id_acompanante);
        linear_responsable = (LinearLayout) root_view.findViewById(R.id.id_responsable);
        txt_id_paciente_condicion = (TextView) root_view.findViewById(R.id.txt_id_paciente_condicion);
        txt_paciente_numero_identificacion = (TextView) root_view.findViewById(R.id.txt_paciente_numero_identificacion);
        txt_number_inpec = (TextView) root_view.findViewById(R.id.txt_number_inpec);
        constantes_radios.put(Parametro.TIPO_PARAMETRO_GENERO, genre);
        //constantes_radios.put(Parametro.TIPO_PARAMETRO_ZONA_URBANA, urban_zone);
        crear_radios();
        crear_radio_zonas();
        companion.setOnCheckedChangeListener(this);
        responsible.setOnCheckedChangeListener(this);
        aseguradora = (AutoCompleteTextView) root_view.findViewById(R.id.id_paciente_asegurador);

        //age.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        //age.setTransformationMethod(null);


        LinearLayout indicador_pagina = (LinearLayout) root_view.findViewById(R.id.indice_pagina);
        arguments = getArguments();
        for (Map.Entry<String, Integer> constante : constantes.entrySet()) {
            Spinner spinner = root_view.findViewById(constante.getValue());
            poblar_spinner(spinner, dbUtil.obtenerParametros(constante.getKey()));
            if(constante.getKey().equals(Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO)){
                data_type_documents = dbUtil.obtenerParametros(constante.getKey());

            }if(constante.getKey().equals(Parametro.TIPO_PARAMETRO_TIPO_CONDICION)){
                data_type_conditions = dbUtil.obtenerParametros(constante.getKey());
            }

            if(constante.getValue().equals(R.id.id_paciente_finalidad))
                spinner.setSelection(1);

            if(constante.getValue().equals(R.id.id_paciente_causa_externa))
                spinner.setSelection(1);
        }

        poblar_spinner(department_id, cargar_departamentos());
        poblar_spinner(municipality, new ArrayList<Parametro>());
        /////////////////////////////autocomplete aseguradora///////////////////////////////////////////////////
        List<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
        adapter_autocomplete = new AutocompleteAdapter(new ListFilter(), getActivity(), R.layout.autocomplete_item
                , aseguradoras);
        aseguradora.setAdapter(adapter_autocomplete);
        aseguradora.setOnItemClickListener(onItemClickListener);
        aseguradora.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                precargado_sugerencia = false;
                return false;
            }
        });

        disableNumberDocument();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        type_document.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (type_document.getSelectedItem() != null) {

                    set_visibility_condition(((Parametro) type_document.getSelectedItem()).getValor());
                    Integer tipo_documento = ((Parametro) type_document.getSelectedItem()).getValor();
                    if(tipo_documento.equals(Paciente.CEDULA_CIUDADANIA) || tipo_documento.equals(Paciente.TARJETA_DE_IDENTIDAD) ) {
                        number_document.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
                        number_document.setTransformationMethod(null);
                    }else {
                        number_document.setInputType(InputType.TYPE_CLASS_TEXT);
                    }

                    // TODO: TD-11 mostrar los tipos de condicion 1,2,3,9,10
                    // TODO: TD-12 mostrar los tipos de condicion 4,5,6,7,8,10
                    // pregunta si es visible el campo para saber si esta editando el registro o si se esta creando un registro de paciente nuevo
                    if (type_document.getSelectedItem() != null && type_condition.getVisibility() == View.VISIBLE) {
                        poblar_spinner(type_condition, cargar_tipos_condicion( ((Parametro) type_document.getSelectedItem()).getValor()));
                        if (type_condition.getAdapter().getCount() > 1){
                            if (type_condition.getAdapter().getItem(1) != null) {
                                type_condition.setSelection(1);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        type_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (type_document.getSelectedItem() != null) {
                    set_visibility_condition(((Parametro) type_document.getSelectedItem()).getValor());
                    int tdValue = ((Parametro) type_document.getSelectedItem()).getValor();
                    // cuando es td=menor sin identificar y tc=menor recien nacido, se habilita el campo de numero documento
                    if( (tdValue == 12) && type_condition.getSelectedItem() != null && ((Parametro) type_condition.getSelectedItem()).getValor() == 7){
                        enableNumberDocument();
                    }else{
                        disableNumberDocument();
                    }
                }
                if (type_condition.getSelectedItem() != null) {
                    set_visibility_number_document(((Parametro) type_condition.getSelectedItem()).getIdServidor());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        department_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (department_id.getSelectedItem() != null) {

                    List<Parametro> temp_data = cargar_municipios(((Parametro) department_id.getSelectedItem()).getIdServidor());
                    poblar_spinner(municipality, temp_data);

                    Log.i(tag, "sp_departamento_seleccionado== temp_id : "+temp_id_municipality);
                    Log.i(tag, "sp_departamento_seleccionado== id_municipio : "+id_municipio);

//                    if (temp_id_municipality != null && id_municipio != temp_id_municipality) {
//                        id_municipio = null;
//                    }
                    if (id_municipio != null && id_municipio == temp_id_municipality) {
                        municipality.setSelection(id_municipio);
                        set_municipality = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        municipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (municipality.getSelectedItem() != null) {
                    temp_id_municipality = position;
                    id_municipio = null;
                    //temp_id_municipality = ((Parametro) municipality.getSelectedItem()).getIdServidor();
                    Log.i(tag, "sp_municipality_seleccionado temp_id :"+temp_id_municipality);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        unit_measure_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // pregunta si es visible el campo para saber si esta editando el registro o si se esta creando un registro de paciente nuevo
                if (unit_measure_age.getSelectedItem() != null && type_document.getVisibility() == View.VISIBLE) {
                    poblar_spinner(type_document, cargar_tipos_documento(((Parametro) unit_measure_age.getSelectedItem()).getIdServidor()));
                    if (type_document.getAdapter().getCount() > 1){
                        if (type_document.getAdapter().getItem(1) != null) {
                            type_document.setSelection(1);
                        }
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        btn_registrar = (Button) root_view.findViewById(R.id.btn_registrar_paciente);
        btn_resumen = (Button) root_view.findViewById(R.id.btn_resumen);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                enableNumberDocument();
                if (validar_campos()) {
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            savePatient();
                        }
                    });
                }else{
                    String errors = contexto.getResources().getString(R.string.mensaje_validacion_campos);
                    errors+="\n"+fields_erors;
                    mostrarMensaje(errors, MSJ_ERROR);
                }
            }
        });
        btn_resumen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (validar_campos()) {
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            savePatient();
                        }
                    });
                }
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                view.setMaxDate(new Date().getTime());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    actualizar_texto(year, monthOfYear, dayOfMonth);
                }
            }

        };



        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
        toolbar.setNavigationOnClickListener(view -> {
            ((Activity)contexto).onBackPressed();
        });
        if (toolbar != null)
            toolbar.setTitle(getString(R.string.nueva_consulta_title_paso1));
        arguments = getArguments();
        if (arguments.getInt(ARG_INFORMACION_PACIENTE) != 0 && arguments.getInt(ARG_PACIENTE_ID_LOCAL) != 0) {
            id_informacion_paciente = arguments.getInt(ARG_INFORMACION_PACIENTE);
            poblar_formulario_informacion(arguments.getInt(ARG_PACIENTE_ID_LOCAL), id_informacion_paciente);
        }else {

            if (arguments.getInt(ARG_ID_PACIENTE) != 0) {
                poblar_formulario(arguments.getInt(ARG_ID_PACIENTE), null);
            } else if (arguments.getInt(ARG_PACIENTE_ID_LOCAL) != 0) {
                poblar_formulario(null, arguments.getInt(ARG_PACIENTE_ID_LOCAL));
            } else {
                number_document.setText(arguments.getString(ARG_CEDULA));
            }
        }


//        if (arguments.getInt(ARG_INFORMACION_PACIENTE) != 0) {
//            id_informacion_paciente = arguments.getInt(ARG_INFORMACION_PACIENTE);
//            // TODO: 5/23/19 crear metodo para que traiga la informacion del paciente de acuerdo a lo almacenado localmente
//            //poblar_formulario_infopaciente(null, id_informacion_paciente);
//        }

        if(arguments.containsKey(ARG_RESUMEN))
            resumen = arguments.getBoolean(ARG_RESUMEN);
        if (resumen) {
            btn_registrar.setVisibility(View.GONE);
            btn_resumen.setVisibility(View.VISIBLE);
        } else {
            btn_registrar.setVisibility(View.VISIBLE);
            btn_resumen.setVisibility(View.GONE);
        }
        return root_view;
    }

    private void disableNumberDocument() {
        number_document.clearFocus();
        number_document.setFocusable(false);
        number_document.setClickable(false);
    }

    private void enableNumberDocument(){
        number_document.requestFocus();
        number_document.setEnabled(true);
        number_document.setFocusableInTouchMode(true);
        //number_document.setFocusable(true);
        number_document.setClickable(true);



    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    id_insure = ((Aseguradora) adapterView.getItemAtPosition(i)).getId();
                }
            };

    private List<Parametro> cargar_tipos_condicion(Integer value){

        List<Integer> list_tc = new ArrayList<>();
        List<Parametro> data = new ArrayList<>();
        if (value == 11) {
            //int[] array = {1,2,3,9,10};
            int[] array = {-19,-20,-21,-27,-28};
            list_tc = IntStream.of(array).boxed().collect(Collectors.toList());
//            for (int i = 0; i < temp_adapter.getCount(); i++) {
//                if(!Arrays.asList(array).contains(((Parametro)type_condition.getAdapter().getItem(i)).getIdServidor())){
//                }
//            }
        }else if (value == 12) {
            //int[] array = {4,5,6,7,8,10};
            int[] array = {-22,-23,-24,-25,-26,-28};
            list_tc = IntStream.of(array).boxed().collect(Collectors.toList());
        }else{
            data = data_type_conditions;
        }


        for (int i = 0; i < list_tc.size(); i++) {
            Parametro parametro = getTypeConditionByIdServidor(list_tc.get(i));
            if(parametro != null)
                data.add(parametro);
        }

        return data;

    }

    private void set_visibility_number_document(Integer value) {
        Log.i(tag, "type_condition_value ==>"+value);
        boolean is_visible = (value != null && value != -28);
        number_document.setVisibility(is_visible ? View.VISIBLE : View.GONE);
        txt_paciente_numero_identificacion.setVisibility(is_visible ? View.VISIBLE : View.GONE);
    }

    private void set_visibility_condition(Integer value) {
        if (value == 11) {
            type_condition.setVisibility(View.VISIBLE);
            txt_id_paciente_condicion.setVisibility(View.VISIBLE);

            number_document.setVisibility(View.VISIBLE);
            txt_paciente_numero_identificacion.setVisibility(View.VISIBLE);


        } else if (value == 12) {
            type_condition.setVisibility(View.VISIBLE);
            txt_id_paciente_condicion.setVisibility(View.VISIBLE);
            number_document.setVisibility(View.VISIBLE);
            txt_paciente_numero_identificacion.setVisibility(View.VISIBLE);


        } else {

            number_document.setVisibility(View.VISIBLE);
            txt_paciente_numero_identificacion.setVisibility(View.VISIBLE);
            type_condition.setVisibility(View.GONE);
            txt_id_paciente_condicion.setVisibility(View.GONE);
            number_inpec.setVisibility(View.GONE);
            txt_number_inpec.setVisibility(View.GONE);
            //type_condition.setSelection(0);


            poblar_spinner(type_condition, cargar_tipos_condicion( 0));
            type_condition.setSelection(0);
//            if (type_condition.getAdapter().getCount() > 1){
//                if (type_condition.getAdapter().getItem(1) != null) {
//                    type_condition.setSelection(1);
//                }
//            }

        }

        if (type_condition.getSelectedItem() != null && ((Parametro) type_condition.getSelectedItem()).getValor() == 10) {
            number_inpec.setVisibility(View.VISIBLE);
            txt_number_inpec.setVisibility(View.VISIBLE);
            number_document.setVisibility(View.GONE);
            txt_paciente_numero_identificacion.setVisibility(View.GONE);
        } else {
            number_inpec.setVisibility(View.GONE);
            txt_number_inpec.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void actualizar_texto(int year, int month, int day) {
        String myFormat = "dd-MM-Y";//"Y-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        int[] data_diff = differenceDates(year, month, day);
        age.setText(String.valueOf(data_diff[0]));
        unit_measure_age.setSelection(data_diff[1]);
        if(unit_measure_age.getSelectedItem() != null)
            poblar_spinner(type_document, cargar_tipos_documento(((Parametro) unit_measure_age.getSelectedItem()).getIdServidor()));
        birthdate.setText(sdf.format(calendar.getTime()));


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private int[] differenceDates(int year, int month, int day){
        int amount=0, unit = 0;

        LocalDate now = LocalDate.now();
        LocalDate birth = now.minusDays(now.getDayOfMonth() - day).minusMonths(now.getMonthValue() - (month+1)).minusYears(now.getYear() - year);

        Period period = Period.between(birth, now);
        if(period.getYears() > 0){
            unit = 1; amount = period.getYears();
        }else if(period.getMonths() > 0) {
            unit = 2; amount = period.getMonths();
        }else if(period.getDays() > 0 || unit == 0) {
            unit = 3; amount = period.getDays();
        }


        return new int[]{amount, unit};
    }



    private void poblar_spinner(Spinner spinnerCustom, List<Parametro> data) {
        Custom2SpinnerAdapter<Parametro> customSpinnerAdapter = new Custom2SpinnerAdapter<>(
                getActivity().getApplicationContext(), data, Parametro.class
        );
        spinnerCustom.setAdapter((SpinnerAdapter) customSpinnerAdapter);
    }


    public void savePatient() {
        ocultarMensajeEspera();
        Paciente p = new Paciente();
        p.setBirthdate(birthdate.getText().toString());
        p.setGenre(Integer.valueOf(genre.getCheckedRadioButtonId() + ""));
        p.setName(name.getText().toString());
        p.setSecond_name(second_name.getText().toString());
        p.setLast_name(last_name.getText().toString());
        p.setSecond_surname(second_surname.getText().toString());
        p.setNumber_inpec(number_inpec.getText().toString());
        if ((Parametro) type_condition.getSelectedItem() != null)
            p.setType_condition(((Parametro) type_condition.getSelectedItem()).getValor());
        p.setType_document(((Parametro) type_document.getSelectedItem()).getValor());
        p.setNumber_document(number_document.getText().toString());

        InformacionPaciente infoPaciente = new InformacionPaciente();
        infoPaciente.setTerms_conditions(terms_conditions.isChecked());
        infoPaciente.setCivil_status(((Parametro) civil_status.getSelectedItem()).getValor());
        infoPaciente.setOccupation(occupation.getText().toString());
        infoPaciente.setPhone(phone.getText().toString());
        infoPaciente.setEmail(email.getText().toString());
        infoPaciente.setAddress(address.getText().toString());
        infoPaciente.setMunicipality_id(((Parametro) municipality.getSelectedItem()).getValor());
        infoPaciente.setUrban_zone(urban_zone.getCheckedRadioButtonId());
        infoPaciente.setCompanion(companion.isChecked());
        infoPaciente.setResponsible(companion.isChecked());
        infoPaciente.setName_responsible(name_responsible.getText().toString());
        infoPaciente.setPhone_responsible(phone_responsible.getText().toString());
        infoPaciente.setRelationship(relationship.getText().toString());
        infoPaciente.setType_user(((Parametro) type_user.getSelectedItem()).getValor());
        infoPaciente.setAuthozation_number(authorization_number.getText().toString());
        infoPaciente.setPurpose_consultation(((Parametro) purpose_consultation.getSelectedItem()).getValor());
        infoPaciente.setUnit_measure_age(((Parametro) unit_measure_age.getSelectedItem()).getValor());
        infoPaciente.setAge(Integer.valueOf(age.getText().toString().isEmpty() ? "0" : age.getText().toString()));
        infoPaciente.setPatient_id(null);
        infoPaciente.setExternal_cause(((Parametro) external_cause.getSelectedItem()).getValor());
        infoPaciente.setName_companion(name_companion.getText().toString());
        infoPaciente.setPhone_companion(phone_companion.getText().toString());
        infoPaciente.setStatus(0);
        if (id_insure != null)
            infoPaciente.setInsurance_id(id_insure);
        if (id_server_paciente != null) {
            p.setIdServidor(id_server_paciente);
            infoPaciente.setPatient_id(id_server_paciente);
        }
        p.setStatus(0);
        dbUtil.crearPaciente(p);
        infoPaciente.setId_patient_local(p.getId());
        if (id_informacion_paciente != null)
            infoPaciente.setId(id_informacion_paciente);
        dbUtil.crearInformacionPaciente(infoPaciente);
        ocultarMensajeEspera();
        arguments.putBoolean("resumen", false);
        arguments.putInt("paciente_id_local", p.getId());
        arguments.putInt("informacion_paciente", infoPaciente.getId());
        arguments.putInt("genero", p.getGenre());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;

        if (resumen) {
            fragment = new ResumenFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).commit();
        } else {
            final Session session = Session.getInstance(getActivity());
            Integer tipo = session.getCredentials().getTipoProfesional();
            arguments.putInt(ARG_TIPO_PROFESIONAL,tipo);
            if(tipo==1)
                fragment = new RegistrarHistoriaFragment();
            else
                fragment = new RegistrarHistoriaEnfermeriaFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }


    }


    public boolean validar_campos() {
        Integer tipo_documento = 0;
        fields_erors = "";
        boolean validacionCampos = utils.validarCamposRequeridos(name, last_name, age, unit_measure_age, type_document, civil_status, department_id, municipality, type_user, aseguradora, purpose_consultation, external_cause, terms_conditions);


        if (validacionCampos == true && TextUtils.isEmpty(age.getText().toString())) {
            age.setError(getResources().getString(R.string.msj_campos_requeridos));
            age.requestFocus();
            validacionCampos = false;
        }else if (type_document.getSelectedItem() != null) {
            tipo_documento = ((Parametro) type_document.getSelectedItem()).getValor();
            ////////////////////////validaciones de RIPS////////////////////////////////
            if(unit_measure_age.getSelectedItem() != null)
            {
                Integer unit_measure = 0;
                unit_measure = ((Parametro) unit_measure_age.getSelectedItem()).getValor();

//                    if(tipo_documento.equals(Paciente.MENOR_SIN_IDENTIFICAR)|| tipo_documento.equals(Paciente.CERTIFICADO_DE_NACIDO_VIVO))
//                    //if(tipo_documento.equals(Paciente.MENOR_SIN_IDENTIFICAR)||tipo_documento.equals(Paciente.REGISTRO_CIVIL) || tipo_documento.equals(Paciente.CERTIFICADO_DE_NACIDO_VIVO))
//                    {
//                        if(unit_measure.equals(Paciente.ANIOS))
//                        {
//                            age.setError(getResources().getString(R.string.validacion_edad_anios_menor));
//                            age.requestFocus();
//                            validacionCampos = false;
//                        }
//                    }else {
//                        if(!unit_measure.equals(Paciente.ANIOS))
//                        {
//                            age.setError(getResources().getString(R.string.validacion_tipo_documento));
//                            age.requestFocus();
//                            validacionCampos = false;
//                        }
//                    }

                if(unit_measure.equals(Paciente.DIAS)&& age.getText().toString().matches("[0-9]+") && (Integer.parseInt(age.getText().toString()) < 1 || Integer.parseInt(age.getText().toString()) > 29  ))
                {
                    age.setError(getResources().getString(R.string.validacion_edad_dias));
                    age.requestFocus();
                    validacionCampos = false;
                    fields_erors += " "+getResources().getString(R.string.validacion_edad_dias);
                }
                else if(unit_measure.equals(Paciente.MESES) && age.getText().toString().matches("[0-9]+") && (Integer.parseInt(age.getText().toString()) < 1 || Integer.parseInt(age.getText().toString()) > 11  ))
                {
                    age.setError(getResources().getString(R.string.validacion_edad_meses));
                    age.requestFocus();
                    validacionCampos = false;
                    fields_erors += " "+getResources().getString(R.string.validacion_edad_meses);
                }
                else if(unit_measure.equals(Paciente.ANIOS))
                {
                    if(age.getText().toString().matches("[0-9]+") && Integer.parseInt(age.getText().toString()) < 1 || Integer.parseInt(age.getText().toString()) > 120  )
                    {
                        age.setError(getResources().getString(R.string.validacion_edad_anios));
                        age.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_edad_anios);

                    }
                    if (age.getText().toString().matches("[0-9]+") && Integer.parseInt(age.getText().toString())>18 && tipo_documento.equals(Paciente.TARJETA_DE_IDENTIDAD) )
                    {
                        age.setError(getResources().getString(R.string.validacion_edad_menor_edad_));
                        age.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_edad_menor_edad_);
                    }
                    if ( age.getText().toString().matches("[0-9]+")&&Integer.parseInt(age.getText().toString())<18 && tipo_documento.equals(Paciente.ADULTO_SIN_IDENTIFICAR) )
                    {
                        age.setError(getResources().getString(R.string.validacion_tipo_documento_as));
                        age.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_documento_as);
                    }
                }



            }

            switch (tipo_documento)
            {
                case Paciente.CEDULA_CIUDADANIA:
                    if( number_document.getText().toString().length()>10)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tipo_cedula));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_cedula);
                    }
                    break;
                case Paciente.CEDULA_EXTRANJERIA:
                    if( number_document.getText().toString().length()>6)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tipo_cedula_extrangeria));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_cedula_extrangeria);
                    }
                    break;
                case Paciente.CARNE_DIPLOMATICO:
                    if( number_document.getText().toString().length()>16)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tipo_carnet_diplomatico));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_carnet_diplomatico);
                    }
                    break;
                case Paciente.SALVOCONDUCTO:
                    if( number_document.getText().toString().length()>16)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tipo_salvo_conducto));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_salvo_conducto);
                    }
                    break;
                case Paciente.PERMISO_ESPECIAL_PERMANENCIA:
                    if( number_document.getText().toString().length()>15)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tipo_permiso));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_permiso);
                    }
                    break;
                case Paciente.RESIDENTE_ESPECIAL_PARA_LA_PAZ:
                    if( number_document.getText().toString().length()>15)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tipo_residente));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tipo_residente);
                    }
                    break;
                case Paciente.REGISTRO_CIVIL:
                    if( number_document.getText().toString().length()>11)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_registro_civil));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_registro_civil);
                    }
                    break;
                case Paciente.TARJETA_DE_IDENTIDAD:
                    if( number_document.getText().toString().length()>11)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_tarjeta_identidad));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_tarjeta_identidad);
                    }
                    break;
                case Paciente.CERTIFICADO_DE_NACIDO_VIVO:
                    if( number_document.getText().toString().length()>9)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_certificado_nacido_vivo));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_certificado_nacido_vivo);
                    }
                    break;
                case Paciente.ADULTO_SIN_IDENTIFICAR:
                    if( number_document.getText().toString().length()>10)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_adulto_sin_identificar));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_adulto_sin_identificar);
                    }
                    break;
                case Paciente.MENOR_SIN_IDENTIFICAR:
                    if( number_document.getText().toString().length()>12)
                    {
                        number_document.setError(getResources().getString(R.string.validacion_menor_sin_identificar));
                        number_document.requestFocus();
                        validacionCampos = false;
                        fields_erors += " "+getResources().getString(R.string.validacion_menor_sin_identificar);
                    }
                    break;

            }
            if (tipo_documento.equals(Paciente.ADULTO_SIN_IDENTIFICAR) || tipo_documento.equals(Paciente.MENOR_SIN_IDENTIFICAR)) {
                if (!utils.validarCamposRequeridos(type_condition))
                    validacionCampos = false;
                fields_erors += " Tipo de codición no válido para el tipo de documento";

            }
            else if (tipo_documento.equals(Paciente.CEDULA_CIUDADANIA)||tipo_documento.equals(Paciente.TARJETA_DE_IDENTIDAD))
            {
                if(!number_document.getText().toString().matches("\\d+(?:\\.\\d+)?"))
                {
                    number_document.setError(getResources().getString(R.string.nueva_consulta_registro_validacion_numero_identificacion));
                    number_document.requestFocus();
                    validacionCampos = false;
                    fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_validacion_numero_identificacion);

                }
            }
        }else{
            type_document.requestFocus();
            validacionCampos = false;
            fields_erors += " Tipo de documento no valido";

        }

       /* if (type_condition.getSelectedItem() != null && tipo_documento != Paciente.ADULTO_SIN_IDENTIFICAR && ((Parametro) type_condition.getSelectedItem()).getValor() != Paciente.POBLACION_RECLUSA && (number_document.getText().toString().length() < 5 || number_document.getText().toString().length() > 15)) {
            number_document.setError(getResources().getString(R.string.nueva_consulta_registro_validacion_numero_identificacion));
            number_document.requestFocus();
            validacionCampos = false;
        }*/

        if (type_condition.getSelectedItem() == null && tipo_documento != Paciente.ADULTO_SIN_IDENTIFICAR && tipo_documento != Paciente.MENOR_SIN_IDENTIFICAR && (number_document.getText().toString().length() < 5 || number_document.getText().toString().length() > 15)) {
            number_document.setError(getResources().getString(R.string.nueva_consulta_registro_validacion_numero_identificacion));
            number_document.requestFocus();
            validacionCampos = false;
            fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_validacion_numero_identificacion);

        }

        if (type_condition.getSelectedItem() != null && ((Parametro) type_condition.getSelectedItem()).getValor() == Paciente.POBLACION_RECLUSA) {
            if (!utils.validarCamposRequeridos(number_inpec))
                validacionCampos = false;
            if (number_inpec.getText().toString().length() != 6) {
                number_inpec.setError(getResources().getString(R.string.nueva_consulta_registro_validacion_numero_inpec));
                number_inpec.requestFocus();
                validacionCampos = false;
                fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_validacion_numero_inpec);

            }
        }

        if (phone.length() > 0 && phone.getText().toString().length() < 7) {
            phone.setError(getResources().getString(R.string.nueva_consulta_registro_validacion_numero_telefono));
            phone.requestFocus();
            validacionCampos = false;
            fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_validacion_numero_telefono);

        }
        if (age.getText().toString().length() > 3) {
            age.setError(getResources().getString(R.string.nueva_consulta_registro_validacion_edad));
            age.requestFocus();
            validacionCampos = false;
            fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_validacion_edad);

        }


        if (!StringUtils.isEmpty(email.getText().toString())) {
            if (!utils.validarEmail(email.getText().toString())) {
                email.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_ingrese_email));
                email.requestFocus();
                validacionCampos = false;
                fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_consulta_ingrese_email);
            }
        }
        if (!aseguradora.getText().toString().isEmpty() && id_insure == null) {
            aseguradora.setError(getResources().getString(R.string.nueva_consulta_registro_consulta_validar_aseguradora));
            aseguradora.requestFocus();
            validacionCampos = false;
            fields_erors += " "+getResources().getString(R.string.nueva_consulta_registro_consulta_validar_aseguradora);

        }
        /*if(last_name.getText().toString().length() >20)
        {
            last_name.setError(getResources().getString(R.string.nueva_consulta_registro_validar));
            last_name.requestFocus();
            validacionCampos = false;
        }

        if(name.getText().toString().length() >20)
        {
            name.setError(getResources().getString(R.string.nueva_consulta_registro_validar));
            name.requestFocus();
            validacionCampos = false;
        }
        if(second_surname.getText().toString().length() >20)
        {
            second_surname.setError(getResources().getString(R.string.nueva_consulta_registro_validar));
            second_surname.requestFocus();
            validacionCampos = false;
        }
        if(second_name.getText().toString().length() >20)
        {
            second_name.setError(getResources().getString(R.string.nueva_consulta_registro_validar));
            second_name.requestFocus();
            validacionCampos = false;
        }

        if(second_name.getText().toString().length() >20)
        {
            second_name.setError(getResources().getString(R.string.nueva_consulta_registro_validar));
            second_name.requestFocus();
            validacionCampos = false;
        }*/



        return validacionCampos;
    }

    private List<Parametro> cargar_departamentos() {
        List<Parametro> departementos = new ArrayList<>();
        try {
            RuntimeExceptionDao<Departamento, Integer> departamentoDAO = getDbHelper().getDepartamentoRuntimeDAO();
            QueryBuilder<Departamento, Integer> queryBuilder = departamentoDAO.queryBuilder();
            queryBuilder.orderBy(Departamento.NOMBRE_CAMPO_NOMBRE, true);
            List<Departamento> departamentosBD = departamentoDAO.query(queryBuilder.prepare());
            for (Departamento departamento : departamentosBD) {
                Parametro p = new Parametro();
                p.setIdServidor(departamento.getIdServidor());
                p.setNombre(departamento.getNombre());
                p.setValor(departamento.getIdServidor());
                departementos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departementos;
    }

    private List<Parametro> cargar_municipios(Integer id_departamento) {
        List<Parametro> municipios = new ArrayList<>();
        try {
            RuntimeExceptionDao<Municipio, Integer> municipioDAO = getDbHelper().getMunicipioRuntimeDAO();
            QueryBuilder<Municipio, Integer> queryBuilder = municipioDAO.queryBuilder();
            queryBuilder.where().eq(
                    Municipio.NOMBRE_CAMPO_ID_DEPARTAMENTO, id_departamento
            );
            queryBuilder.orderBy(Municipio.NOMBRE_CAMPO_NOMBRE, true);
            final List<Municipio> municipiosBD = municipioDAO.query(queryBuilder.prepare());
            for (Municipio municipo : municipiosBD) {
                Parametro p = new Parametro();
                p.setIdServidor(municipo.getIdServidor());
                p.setNombre(municipo.getNombre());
                p.setValor(municipo.getIdServidor());
                municipios.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipios;
    }


    private List<Parametro> cargar_tipos_documento(Integer id_mesure_age) {
        List<Parametro> data = new ArrayList<>();
        List<Integer> list_td = new ArrayList<>();
        int temp_age = Integer.valueOf(age.getText().toString().isEmpty() ? "0" : age.getText().toString());


        if(id_mesure_age == ANIOS){ // años
            if(temp_age < 7){
                int[] docs = {RC, PA, PE, RE, MS};
                list_td = IntStream.of(docs).boxed().collect(Collectors.toList());

            }else if(temp_age >= 7 && temp_age <= 17){
                int[] docs = {TI, RC, CE, PE, RE, MS};
                list_td = IntStream.of(docs).boxed().collect(Collectors.toList());
            }else if(temp_age >= 18){
                if(temp_age == 18){
                    int[] docs = {CC, TI, RC, CN, CE, PE, RE, AS};
                    list_td = IntStream.of(docs).boxed().collect(Collectors.toList());

                }else{
                    int[] docs = {CC, CE, CD, PA, SC, PE, RE, AS};
                    list_td = IntStream.of(docs).boxed().collect(Collectors.toList());

                }
            }
        }else if(id_mesure_age == MESES){ // meses
            if(temp_age < 3 || temp_age <= 11){
                int[] docs = {RC, CE, CD, PA, SC, PE, RE, CN, MS};
                list_td = IntStream.of(docs).boxed().collect(Collectors.toList());

            }

        }else if(id_mesure_age == DIAS){ // dias
            int[] docs = {RC, MS, CN, CD};
            list_td = IntStream.of(docs).boxed().collect(Collectors.toList());

        }

        if(list_td.size() > 0)

            for (int i = 0; i < list_td.size(); i++) {
                Parametro parametro = getTypeDocumentByIdServidor(list_td.get(i));
                if(parametro != null)
                    data.add(parametro);
            }


        return data;
    }
    private Parametro getTypeDocumentByIdServidor(int id){
        Parametro parametro = null;
        for (int i = 0; i < data_type_documents.size(); i++) {
            Parametro p = data_type_documents.get(i);
            if(p.getIdServidor() == id) {
                parametro = p;
                break;
            }

        }
        return parametro;
    }


    private Parametro getTypeConditionByIdServidor(int id){
        Parametro parametro = null;
        for(Parametro p : data_type_conditions){
//        for (int i = 0; i < data_type_conditions.size(); i++) {
//            Parametro p = data_type_conditions.get(i);
            if(p != null && p.getIdServidor() == id) {
                parametro = p;
                break;
            }

        }
        return parametro;
    }

    public void crear_radio_zonas() {
        utils.crearRadios(dbUtil.obtenerParametrosZona(), urban_zone);
        // Se deja seleccionado por defecto Urbana
        urban_zone.check(1);
    }

    public void crear_radios() {
        for (Map.Entry<String, RadioGroup> constante : constantes_radios.entrySet()) {
            utils.crearRadios(dbUtil.obtenerParametros(constante.getKey()), constante.getValue());
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean seleccionado) {
        switch (compoundButton.getId()) {
            case R.id.responsable:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), linear_responsable);
                break;
            case R.id.acompanante:
                utils.cambiarVisibilidad((((seleccionado) ? View.VISIBLE : View.GONE)), linear_acompanante);
                break;
        }
    }


    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_paciente;
    }

    public void poblar_formulario(Integer paciente_id_server, Integer paciente_id_local) {
        id_server_paciente = paciente_id_server;
        precargado_sugerencia = true;
        try {
            Paciente paciente;
            RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> queryBuilder = pacienteDAO.queryBuilder();
            if (paciente_id_server != null)
                queryBuilder.where().eq(
                        Paciente.NOMBRE_CAMPO_ID_SERVIDOR, paciente_id_server
                );
            else if (paciente_id_local != null)
                queryBuilder.where().eq(
                        "id", paciente_id_local
                );
            paciente = pacienteDAO.queryForFirst(queryBuilder.prepare());
            if (paciente!= null) {
                RuntimeExceptionDao<InformacionPaciente, Integer> informacionPacientesDAO = getDbHelper().getInformacionPacienteRuntimeDAO();
                QueryBuilder<InformacionPaciente, Integer> queryBuilderInformacion = informacionPacientesDAO.queryBuilder();
                if (paciente_id_server != null)
                    queryBuilderInformacion.where().eq(
                            InformacionPaciente.NOMBRE_CAMPO_PATIENT_ID, paciente.getIdServidor()
                    );
                else if (paciente_id_local != null)
                    queryBuilderInformacion.where().eq(
                            InformacionPaciente.NOMBRE_CAMPO_ID_PATIENT_LOCAL, paciente_id_local
                    );

                List<InformacionPaciente> list_informacion = informacionPacientesDAO.query(queryBuilderInformacion.prepare());

                if (!list_informacion.isEmpty()) {
                    birthdate.setText(paciente.getBirthdate());
                    second_surname.setText(paciente.getSecond_surname());
                    number_document.setText(paciente.getNumber_document());
                    name.setText(paciente.getName());
                    second_name.setText(paciente.getSecond_name());
                    last_name.setText(paciente.getLast_name());
                    type_document.setSelection(((Custom2SpinnerAdapter<Parametro>) type_document.getAdapter()).getPosition(paciente.getType_document()));

                    InformacionPaciente informacion = list_informacion.get(0);
                    terms_conditions.setChecked(informacion.getTerms_conditions());
                    companion.setChecked(informacion.getCompanion());
                    responsible.setChecked(informacion.getResponsible());
                    phone.setText(informacion.getPhone());
                    email.setText(  informacion.getEmail().equals("No reporta") ? "" : informacion.getEmail() );
                    address.setText(informacion.getAddress());
                    name_companion.setText(informacion.getName_companion());
                    phone_companion.setText(informacion.getPhone_companion());
                    name_responsible.setText(informacion.getName_responsible());
                    phone_responsible.setText(informacion.getPhone_responsible());
                    relationship.setText(informacion.getRelationship());
                    age.setText(informacion.getAge().toString());
                    authorization_number.setText(informacion.getAuthozation_number());
                    occupation.setText(informacion.getOccupation());

                    type_user.setSelection((((Custom2SpinnerAdapter<Parametro>) type_user.getAdapter()).getPosition(informacion.getType_user())));
                    purpose_consultation.setSelection((((Custom2SpinnerAdapter<Parametro>) purpose_consultation.getAdapter()).getPosition(informacion.getPurpose_consultation())));
                    external_cause.setSelection((((Custom2SpinnerAdapter<Parametro>) external_cause.getAdapter()).getPosition(informacion.getExternal_cause())));
                    unit_measure_age.setSelection((((Custom2SpinnerAdapter<Parametro>) unit_measure_age.getAdapter()).getPosition(informacion.getUnit_measure_age())));
                    department_id.setSelection((((Custom2SpinnerAdapter<Parametro>) department_id.getAdapter()).getPosition(getDepartamento(informacion.getMunicipality_id()))));
                    civil_status.setSelection((((Custom2SpinnerAdapter<Parametro>) civil_status.getAdapter()).getPosition(informacion.getCivil_status())));

                    set_visibility_condition(paciente.getType_document());
                    if (paciente.getType_condition() != null)
                        type_condition.setSelection((((Custom2SpinnerAdapter<Parametro>) type_condition.getAdapter()).getPosition(paciente.getType_condition())));
                    //municipality
                    poblar_spinner(municipality, cargar_municipios(((Parametro) department_id.getSelectedItem()).getIdServidor()));
                    id_municipio = (((Custom2SpinnerAdapter<Parametro>) municipality.getAdapter()).getPosition(informacion.getMunicipality_id()));
                    if(id_municipio != null) {
                        set_municipality = true;
                        temp_id_municipality = id_municipio;
                        Log.i(tag, "sp_setvalies== id_municipio : "+id_municipio);

                    }
                    genre.check(paciente.getGenre());
                    urban_zone.check(informacion.getUrban_zone());
                    number_inpec.setText(paciente.getNumber_inpec());
                    if (informacion.getInsurance_id() != null) {
                        Object obj = getAseguradora(informacion.getInsurance_id());
                        if (obj != null) {
                            Aseguradora insurance = (Aseguradora) obj;
                            aseguradora.setText(insurance.getName());
                            id_insure = insurance.getId();
                        }
                    }
                }
                type_document.setVisibility(View.GONE);
                number_document.setVisibility(View.GONE);
                type_condition.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                second_surname.setVisibility(View.GONE);
                second_name.setVisibility(View.GONE);
                last_name.setVisibility(View.GONE);
                birthdate.setVisibility(View.GONE);
                number_inpec.setVisibility(View.GONE);

                for (int i = 0; i < genre.getChildCount(); i++) {
                    genre.getChildAt(i).setVisibility(View.GONE);
                }

                TextView txt_number_inpec = (TextView) root_view.findViewById(R.id.txt_number_inpec);
                TextView txt_id_paciente_condicion = (TextView) root_view.findViewById(R.id.txt_id_paciente_condicion);
                TextView txt_tipo_identificacion = (TextView) root_view.findViewById(R.id.txt_tipo_identificacion);
                TextView txt_paciente_primer_nombre = (TextView) root_view.findViewById(R.id.txt_paciente_primer_nombre);
                TextView txt_paciente_segundo_nombre = (TextView) root_view.findViewById(R.id.txt_paciente_segundo_nombre);
                TextView txt_paciente_segundo_apellido = (TextView) root_view.findViewById(R.id.txt_paciente_segundo_apellido);
                TextView txt_paciente_primer_apellido = (TextView) root_view.findViewById(R.id.txt_paciente_primer_apellido);
                TextView txt_paciente_fecha_nacimiento = (TextView) root_view.findViewById(R.id.txt_paciente_fecha_nacimiento);
                TextView txt_paciente_opcion_sexo_ = (TextView) root_view.findViewById(R.id.txt_paciente_opcion_sexo_);
                LinearLayout linear_paciente_opcion_sexo = (LinearLayout) root_view.findViewById(R.id.txt_paciente_opcion_sexo);


                txt_paciente_numero_identificacion.setVisibility(View.GONE);
                txt_number_inpec.setVisibility(View.GONE);
                txt_id_paciente_condicion.setVisibility(View.GONE);
                txt_tipo_identificacion.setVisibility(View.GONE);
                txt_paciente_primer_nombre.setVisibility(View.GONE);
                txt_paciente_segundo_apellido.setVisibility(View.GONE);
                txt_paciente_segundo_nombre.setVisibility(View.GONE);
                txt_paciente_primer_apellido.setVisibility(View.GONE);
                txt_paciente_fecha_nacimiento.setVisibility(View.GONE);
                linear_paciente_opcion_sexo.setVisibility(View.GONE);
                txt_paciente_opcion_sexo_.setVisibility(View.GONE);

                LinearLayout info_paciente = (LinearLayout) root_view.findViewById(R.id.info_paciente);
                info_paciente.setVisibility(View.VISIBLE);

                LinearLayout terminos = (LinearLayout) root_view.findViewById(R.id.id_terminos_condiciones);
                terminos.setVisibility(View.GONE);

                TextView txt_nombre = (TextView) root_view.findViewById(R.id.id_paciente_nombre);
                txt_nombre.setText(paciente.getName() + " " + paciente.getSecond_name() + " " + paciente.getLast_name() + " " + paciente.getSecond_surname());

                TextView txt_fecha = (TextView) root_view.findViewById(R.id.id_paciente_fecha_nacimiento_info);
                txt_fecha.setText(paciente.getBirthdate());

                TextView txt_tipo_identificacion_ = (TextView) root_view.findViewById(R.id.id_paciente_tipo_documento_);
                txt_tipo_identificacion_.setText(dbUtil.obtenerNombreParametro(
                        Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO, paciente.getType_document()
                ));

                TextView txt_numero_identificacion_ = (TextView) root_view.findViewById(R.id.id_paciente_numero_identificacion_);
                txt_numero_identificacion_.setText(paciente.getNumber_document());

                TextView txt_consentimiento = (TextView) root_view.findViewById(R.id.id_paciente_consentimiento_);
                txt_consentimiento.setText("Si");

                TextView id_paciente_sexo = (TextView) root_view.findViewById(R.id.id_paciente_sexo);
                id_paciente_sexo.setText(dbUtil.obtenerNombreParametro(
                        Parametro.TIPO_PARAMETRO_GENERO, paciente.getGenre()
                ));

                if (paciente.getType_condition() != null) {
                    type_condition.setSelection((((Custom2SpinnerAdapter<Parametro>) type_condition.getAdapter()).getPosition(paciente.getType_condition())));
                    TextView txt_condicion = (TextView) root_view.findViewById(R.id.txt_id_paciente_condicion__);
                    txt_condicion.setText(((Parametro) type_condition.getSelectedItem()).getNombre());
                    LinearLayout linear_condition = (LinearLayout) root_view.findViewById(R.id.id_paciente_condicion_);
                    linear_condition.setVisibility(View.VISIBLE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void poblar_formulario_informacion(Integer id_paciente_local, Integer id_informacion_local) {
        precargado_sugerencia = true;
        try {
            Paciente paciente = dbUtil.obtenerPacienteFromIdLocal(id_paciente_local);

            InformacionPaciente informacion = dbUtil.obtenerInformacionPacienteFomIdLocal(id_informacion_local);

            if (informacion != null) {
                birthdate.setText(paciente.getBirthdate());
                second_surname.setText(paciente.getSecond_surname());
                number_document.setText(paciente.getNumber_document());
                name.setText(paciente.getName());
                second_name.setText(paciente.getSecond_name());
                last_name.setText(paciente.getLast_name());
                type_document.setSelection(((Custom2SpinnerAdapter<Parametro>) type_document.getAdapter()).getPosition(paciente.getType_document()));


                terms_conditions.setChecked(informacion.getTerms_conditions());
                companion.setChecked(informacion.getCompanion());
                responsible.setChecked(informacion.getResponsible());
                phone.setText(informacion.getPhone());
                email.setText(  informacion.getEmail().equals("No reporta") ? "" : informacion.getEmail() );
                address.setText(informacion.getAddress());
                name_companion.setText(informacion.getName_companion());
                phone_companion.setText(informacion.getPhone_companion());
                name_responsible.setText(informacion.getName_responsible());
                phone_responsible.setText(informacion.getPhone_responsible());
                relationship.setText(informacion.getRelationship());
                age.setText(informacion.getAge().toString());
                authorization_number.setText(informacion.getAuthozation_number());
                occupation.setText(informacion.getOccupation());

                type_user.setSelection((((Custom2SpinnerAdapter<Parametro>) type_user.getAdapter()).getPosition(informacion.getType_user())));
                purpose_consultation.setSelection((((Custom2SpinnerAdapter<Parametro>) purpose_consultation.getAdapter()).getPosition(informacion.getPurpose_consultation())));
                external_cause.setSelection((((Custom2SpinnerAdapter<Parametro>) external_cause.getAdapter()).getPosition(informacion.getExternal_cause())));
                unit_measure_age.setSelection((((Custom2SpinnerAdapter<Parametro>) unit_measure_age.getAdapter()).getPosition(informacion.getUnit_measure_age())));
                department_id.setSelection((((Custom2SpinnerAdapter<Parametro>) department_id.getAdapter()).getPosition(getDepartamento(informacion.getMunicipality_id()))));
                civil_status.setSelection((((Custom2SpinnerAdapter<Parametro>) civil_status.getAdapter()).getPosition(informacion.getCivil_status())));

                set_visibility_condition(paciente.getType_document());
                if (paciente.getType_condition() != null)
                    type_condition.setSelection((((Custom2SpinnerAdapter<Parametro>) type_condition.getAdapter()).getPosition(paciente.getType_condition())));
                //municipality
                poblar_spinner(municipality, cargar_municipios(((Parametro) department_id.getSelectedItem()).getIdServidor()));
                id_municipio = (((Custom2SpinnerAdapter<Parametro>) municipality.getAdapter()).getPosition(informacion.getMunicipality_id()));
                if(id_municipio != null) {
                    set_municipality = true;
                    temp_id_municipality = id_municipio;
                    Log.i(tag, "sp_setvalies== id_municipio : "+id_municipio);

                }
                genre.check(paciente.getGenre());
                urban_zone.check(informacion.getUrban_zone());
                number_inpec.setText(paciente.getNumber_inpec());
                if (informacion.getInsurance_id() != null) {
                    Object obj = getAseguradora(informacion.getInsurance_id());
                    if (obj != null) {
                        Aseguradora insurance = (Aseguradora) obj;
                        aseguradora.setText(insurance.getName());
                        id_insure = insurance.getId();
                    }
                }
            }
            type_document.setVisibility(View.GONE);
            number_document.setVisibility(View.GONE);
            type_condition.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            second_surname.setVisibility(View.GONE);
            second_name.setVisibility(View.GONE);
            last_name.setVisibility(View.GONE);
            birthdate.setVisibility(View.GONE);
            number_inpec.setVisibility(View.GONE);

            for (int i = 0; i < genre.getChildCount(); i++) {
                genre.getChildAt(i).setVisibility(View.GONE);
            }

            TextView txt_number_inpec = (TextView) root_view.findViewById(R.id.txt_number_inpec);
            TextView txt_id_paciente_condicion = (TextView) root_view.findViewById(R.id.txt_id_paciente_condicion);
            TextView txt_tipo_identificacion = (TextView) root_view.findViewById(R.id.txt_tipo_identificacion);
            TextView txt_paciente_primer_nombre = (TextView) root_view.findViewById(R.id.txt_paciente_primer_nombre);
            TextView txt_paciente_segundo_nombre = (TextView) root_view.findViewById(R.id.txt_paciente_segundo_nombre);
            TextView txt_paciente_segundo_apellido = (TextView) root_view.findViewById(R.id.txt_paciente_segundo_apellido);
            TextView txt_paciente_primer_apellido = (TextView) root_view.findViewById(R.id.txt_paciente_primer_apellido);
            TextView txt_paciente_fecha_nacimiento = (TextView) root_view.findViewById(R.id.txt_paciente_fecha_nacimiento);
            TextView txt_paciente_opcion_sexo_ = (TextView) root_view.findViewById(R.id.txt_paciente_opcion_sexo_);
            LinearLayout linear_paciente_opcion_sexo = (LinearLayout) root_view.findViewById(R.id.txt_paciente_opcion_sexo);


            txt_paciente_numero_identificacion.setVisibility(View.GONE);
            txt_number_inpec.setVisibility(View.GONE);
            txt_id_paciente_condicion.setVisibility(View.GONE);
            txt_tipo_identificacion.setVisibility(View.GONE);
            txt_paciente_primer_nombre.setVisibility(View.GONE);
            txt_paciente_segundo_apellido.setVisibility(View.GONE);
            txt_paciente_segundo_nombre.setVisibility(View.GONE);
            txt_paciente_primer_apellido.setVisibility(View.GONE);
            txt_paciente_fecha_nacimiento.setVisibility(View.GONE);
            linear_paciente_opcion_sexo.setVisibility(View.GONE);
            txt_paciente_opcion_sexo_.setVisibility(View.GONE);

            LinearLayout info_paciente = (LinearLayout) root_view.findViewById(R.id.info_paciente);
            info_paciente.setVisibility(View.VISIBLE);

            LinearLayout terminos = (LinearLayout) root_view.findViewById(R.id.id_terminos_condiciones);
            terminos.setVisibility(View.GONE);

            TextView txt_nombre = (TextView) root_view.findViewById(R.id.id_paciente_nombre);
            txt_nombre.setText(paciente.getName() + " " + paciente.getSecond_name() + " " + paciente.getLast_name() + " " + paciente.getSecond_surname());

            TextView txt_fecha = (TextView) root_view.findViewById(R.id.id_paciente_fecha_nacimiento_info);
            txt_fecha.setText(paciente.getBirthdate());

            TextView txt_tipo_identificacion_ = (TextView) root_view.findViewById(R.id.id_paciente_tipo_documento_);
            txt_tipo_identificacion_.setText(dbUtil.obtenerNombreParametro(
                    Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO, paciente.getType_document()
            ));

            TextView txt_numero_identificacion_ = (TextView) root_view.findViewById(R.id.id_paciente_numero_identificacion_);
            txt_numero_identificacion_.setText(paciente.getNumber_document());

            TextView txt_consentimiento = (TextView) root_view.findViewById(R.id.id_paciente_consentimiento_);
            txt_consentimiento.setText("Si");

            TextView id_paciente_sexo = (TextView) root_view.findViewById(R.id.id_paciente_sexo);
            id_paciente_sexo.setText(dbUtil.obtenerNombreParametro(
                    Parametro.TIPO_PARAMETRO_GENERO, paciente.getGenre()
            ));

            if (paciente.getType_condition() != null) {
                type_condition.setSelection((((Custom2SpinnerAdapter<Parametro>) type_condition.getAdapter()).getPosition(paciente.getType_condition())));
                TextView txt_condicion = (TextView) root_view.findViewById(R.id.txt_id_paciente_condicion__);
                txt_condicion.setText(((Parametro) type_condition.getSelectedItem()).getNombre());
                LinearLayout linear_condition = (LinearLayout) root_view.findViewById(R.id.id_paciente_condicion_);
                linear_condition.setVisibility(View.VISIBLE);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getDepartamento(int municipio) {
        RuntimeExceptionDao<Municipio, Integer> municipioDAO = getDbHelper().getMunicipioRuntimeDAO();
        QueryBuilder<Municipio, Integer> queryBuildermunicipio = municipioDAO.queryBuilder();
        try {
            queryBuildermunicipio.where().eq(
                    Municipio.NOMBRE_CAMPO_ID_SERVIDOR, municipio
            );
            List<Municipio> list_municipio = municipioDAO.query(queryBuildermunicipio.prepare());

            if (!list_municipio.isEmpty())
                return list_municipio.get(0).getIdDepartamento();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Aseguradora getAseguradora(int aseguradora) {
        RuntimeExceptionDao<Aseguradora, Integer> aseguradoraDAO = getDbHelper().getAseguradoraRuntimeDAO();
        QueryBuilder<Aseguradora, Integer> queryBuilderaseguradora = aseguradoraDAO.queryBuilder();
        try {
            queryBuilderaseguradora.where().eq(
                    Aseguradora.NOMBRE_CAMPO_ID_SERVIDOR, aseguradora
            );
            List<Aseguradora> list_aseguradora = aseguradoraDAO.query(queryBuilderaseguradora.prepare());

            if (!list_aseguradora.isEmpty())
                return list_aseguradora.get(0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Aseguradora> obtenerAseguradoras(String nombre) {
        List<Aseguradora> aseguradoras = new ArrayList<>();

        try {
            RuntimeExceptionDao<Aseguradora, Integer> aseguradoraDAO = getDbHelper().getAseguradoraRuntimeDAO();
            QueryBuilder<Aseguradora, Integer> queryBuilder = aseguradoraDAO.queryBuilder();
            queryBuilder.where().like(
                    Aseguradora.NOMBRE_CAMPO_NAME, "%" + nombre + "%"
            );
            aseguradoras = aseguradoraDAO.query(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando aseguradoras " + nombre, e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return aseguradoras;
    }


    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (!precargado_sugerencia) {
                id_insure = null;
                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        results.values = new ArrayList<String>();
                        results.count = 0;
                    }
                } else {
                    final String searchStrLowerCase = prefix.toString().toLowerCase();
                    List<Aseguradora> matchValues = obtenerAseguradoras(searchStrLowerCase);
                    results.values = matchValues;
                    results.count = matchValues.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                adapter_autocomplete.setDataList((ArrayList<Aseguradora>) results.values);
            } else {
                adapter_autocomplete.setDataList(null);
            }
            if (results.count > 0) {
                adapter_autocomplete.notifyDataSetChanged();
            } else {
                adapter_autocomplete.notifyDataSetInvalidated();
            }
        }
    }
}
