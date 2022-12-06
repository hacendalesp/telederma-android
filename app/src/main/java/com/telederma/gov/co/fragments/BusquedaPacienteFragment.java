package com.telederma.gov.co.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ExpandableListPacientesAdapter;
import com.telederma.gov.co.dialogs.ConfirmDialog;
import com.telederma.gov.co.dialogs.VerOpcionesDialog;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.PacienteService;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.interfaces.IOpcionMenu;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import static com.telederma.gov.co.utils.Constantes.TIPO_PROFESIONAL_ENFERMERA;
import static com.telederma.gov.co.utils.Constantes.TIPO_PROFESIONAL_MEDICO;
import static com.telederma.gov.co.utils.Utils.MSJ_ADVERTENCIA;

/**
 * @description Clase para el menejo de la vista buscar
 */
public class BusquedaPacienteFragment extends BaseFragment implements IOpcionMenu {

    ExpandableListPacientesAdapter list_adapter;
    ExpandableListView exp_list_view;
    List<Paciente> list_datos_paciente;
    Map<Integer, List<Consulta>> list_datos_consulta;
    public EditText txt_search;
    LinearLayout txt_not_found;
    Button btn_registrar_paciente, btn_buscar;
    Bundle arguments;
    public static final String ARG_SHOWTUTORIAL = "show_tutorial";
    boolean show_tutorial = false;
    View v_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        v_fragment = view;

//        arguments = getArguments();
//        if(arguments != null && arguments.get(ARG_SHOWTUTORIAL) != null) {
//            if (arguments.getBoolean(ARG_SHOWTUTORIAL))
//                show_tutorial = arguments.getBoolean(ARG_SHOWTUTORIAL);
//        }
//        show_tutorial = true;
//        showTutorials();

        exp_list_view = (ExpandableListView) view.findViewById(R.id.parentList);
        exp_list_view.setGroupIndicator(null);
        txt_search = (EditText) view.findViewById(R.id.txt_search);
        txt_not_found = (LinearLayout) view.findViewById(R.id.id_text_not_found);
        btn_registrar_paciente = (Button) view.findViewById(R.id.btn_registrar_paciente);
        txt_search.setText("");

        txt_search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                btn_registrar_paciente.setVisibility(View.GONE);
                txt_not_found.setVisibility(View.VISIBLE);
                list_datos_paciente.clear();
                exp_list_view.clearTextFilter();
                exp_list_view.setAdapter((BaseExpandableListAdapter) null);
            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });

        Button btn_buscar = (Button) view.findViewById(R.id.btn_buscar_paceinte);
        list_datos_paciente = new ArrayList<Paciente>();

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(txt_search.getText().toString().trim().length() == 0){
                    showModalWithoutDocument();
                }else {
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            buscar_paciente();
                        }
                    });
                }
            }
        });
        btn_registrar_paciente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(txt_search.getText().toString().trim().length() == 0){
                    showModalWithoutDocument();
                }else{
                    goToNewHC();
                }

            }
        });

        exp_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String l = "";

                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                        final Paciente paciente = (Paciente) exp_list_view.getAdapter().getItem(position);
                        List<Consulta> consultas = list_adapter.getMapa_consultas().get(paciente.getIdServidor());
                        if (consultas != null) {
                            if (consultas.size() > 0) {
                                Consulta consulta = consultas.get(0);
                                VerOpcionesDialog opciones_dialog;
                                opciones_dialog = new VerOpcionesDialog(contexto, contexto.getString(R.string.desarchivar), contexto.getResources().getDrawable(R.drawable.desarchivar_p));
                                consultas.get(0).setArchivado(true);
                                opciones_dialog.setConsultas(consultas);
                                opciones_dialog.setAdapter(list_adapter);
                                opciones_dialog.show();
                            }
                        }
                        return true;
                    }

                return false;
            }


        });

        return view;

    }

    private void showModalWithoutDocument(){
        String text = contexto.getString(R.string.nueva_consulta_estas_seguro_documento_vacio_descripcion);
        ConfirmDialog confirm_dialog = new ConfirmDialog(contexto, R.string.nueva_consulta_estas_seguro_documento_vacio, text);
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
                confirm_dialog.dismiss();
                goToNewHC();
            }
        });
    }

    private void goToNewHC(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = new HistoriaFragment();
        Bundle arguments = new Bundle();
        if (list_datos_paciente.size() > 0 && list_datos_paciente.get(0).getIdServidor() != null)
            arguments.putInt("paciente_id", list_datos_paciente.get(0).getIdServidor());
        else if (list_datos_paciente.size() > 0)
            arguments.putInt("paciente_id_local", list_datos_paciente.get(0).getId());
        arguments.putBoolean("resumen", false);
        arguments.putBoolean("cuerpo_frente", true);
        arguments.putInt("genero", 1);
        arguments.putString("cedula_paciente",txt_search.getText().toString());
        fragment.setArguments(arguments);
        ft.replace(R.id.menu_content, fragment).addToBackStack(
                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        ).commit();
    }


    private void showTutorials(){
//        LinearLayout ll_step1 = v_fragment.findViewById(R.id.ll_step1);
//        new GuideView.Builder(contexto)
//                .setTitle("Buscar paciente")
//                .setContentText("Ingresa aquí el número de documento del paciente")
//                .setGravity(Gravity.auto) //optional
//                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//                .setTargetView(ll_step1)
//                .setContentTextSize(12)//optional
//                .setTitleTextSize(14)//optional
//                .setGuideListener(new GuideListener() {
//                    @Override
//                    public void onDismiss(View view) {
//
//                    }
//                })
//                .build()
//                .show();
    }

    /**
     * @param numero_documento : el documento del paciente a buscar  , la busqueda se hace a la base de datos local
     * @description :metdodo para poblar el listview
     */
    private void cargar_consultas(String numero_documento) {
        list_datos_consulta = new HashMap<>();
        try {
            RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> queryBuilder = pacienteDAO.queryBuilder();
            queryBuilder.where().eq(
                    Paciente.NOMBRE_CAMPO_NUMBER_DOCUMENT, numero_documento
            );
            Paciente paciente = pacienteDAO.queryForFirst(queryBuilder.orderBy("id",false).prepare());
            if (paciente != null) {
                list_datos_paciente.add(paciente);
                list_datos_consulta.put(paciente.getIdServidor(), new ArrayList<>());

                if(TIPO_PROFESIONAL_MEDICO.equals(Session.getInstance(contexto).getCredentials().getTipoProfesional())){
                    RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
                    QueryBuilder<ConsultaMedica, Integer> queryBuilderConsulta = consultaDAO.queryBuilder();
                    List<ConsultaMedica> consultas = new ArrayList<>();
                    if(paciente.getIdServidor() != null) {
                        queryBuilderConsulta.where().eq(
                                ConsultaMedica.NOMBRE_CAMPO_ID_PACIENTE, paciente.getIdServidor()
                        ).and().not().eq(ConsultaMedica.NOMBRE_CAMPO_ESTADO, "-1"); // Nota: Sebas - Se agrego el campo estado en el query
                        consultas = consultaDAO.query(queryBuilderConsulta.prepare());
                    }
                    for (ConsultaMedica consulta : consultas) {
                        consulta.setIdServidor(consulta.getIdConsulta());
                        consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                        list_datos_consulta.get(paciente.getIdServidor()).add(consulta);
                    }
                } else if(TIPO_PROFESIONAL_ENFERMERA.equals(Session.getInstance(contexto).getCredentials().getTipoProfesional())) {
                    RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
                    QueryBuilder<ConsultaEnfermeria, Integer> queryBuilderConsulta = consultaDAO.queryBuilder();
                    queryBuilderConsulta.where().eq(
                            ConsultaEnfermeria.NOMBRE_CAMPO_ID_PACIENTE, paciente.getIdServidor()
                    ).and().not().eq(ConsultaEnfermeria.NOMBRE_CAMPO_ESTADO, "-1"); // Nota: Sebas - Se agrego el campo estado en el query
                    List<ConsultaEnfermeria> consultas = consultaDAO.query(queryBuilderConsulta.prepare());
                    for (ConsultaEnfermeria consulta : consultas) {
                        consulta.setIdServidor(consulta.getIdConsulta());
                        consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                        list_datos_consulta.get(paciente.getIdServidor()).add(consulta);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btn_registrar_paciente.setVisibility(View.VISIBLE);
        list_adapter = new ExpandableListPacientesAdapter(
                (AppCompatActivity) getActivity(), list_datos_paciente, list_datos_consulta,
                false, false, false, -1
        );
        exp_list_view.setAdapter(list_adapter);
        list_adapter.notifyDataSetChanged();



        if (list_datos_paciente.size() > 0) {
            txt_not_found.setVisibility(View.GONE);
            btn_registrar_paciente.setText(getResources().getString(R.string.action_nueva_consulta));
            btn_registrar_paciente.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_menu_nueva_consulta, 0);
            exp_list_view.expandGroup(0);
        } else {
            txt_not_found.setVisibility(View.VISIBLE);
            btn_registrar_paciente.setText(getResources().getString(R.string.nueva_consulta_nuevo_paciente));
            btn_registrar_paciente.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_add, 0);
        }
        ocultarMensajeEspera();


//        if(show_tutorial){
//            if(list_datos_paciente.size() > 0 ) {
//                new GuideView.Builder(contexto)
//                    .setTitle("Resultado de búsqueda")
//                    .setContentText("Puedes seleccionar una consulta de la lista para ver toda su información")
//                    .setGravity(Gravity.auto) //optional
//                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//                    .setTargetView(exp_list_view)
//                    .setContentTextSize(12)//optional
//                    .setTitleTextSize(14)//optional
//                    .setGuideListener(new GuideListener() {
//                        @Override
//                        public void onDismiss(View view) {
//
//                        }
//                    })
//                    .build()
//                    .show();
//            }else{
//                new GuideView.Builder(contexto)
//                    .setTitle("Resultado de búsqueda")
//                    .setContentText("Como no encuentras el paciente registro en nuestro sistema, puedes crearlo en el siguiente boton")
//                    .setGravity(Gravity.auto) //optional
//                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//                    .setTargetView(exp_list_view)
//                    .setContentTextSize(12)//optional
//                    .setTitleTextSize(14)//optional
//                    .setGuideListener(new GuideListener() {
//                        @Override
//                        public void onDismiss(View view) {
//                            new GuideView.Builder(contexto)
//                                    .setTitle("Registrar paciente")
//                                    .setContentText("Con este boton podrás siempre crear una nueva consulta con el documento del paciente ingresado")
//                                    .setGravity(Gravity.auto) //optional
//                                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//                                    .setTargetView(btn_registrar_paciente)
//                                    .setContentTextSize(12)//optional
//                                    .setTitleTextSize(14)//optional
//                                    .setGuideListener(new GuideListener() {
//                                        @Override
//                                        public void onDismiss(View view) {
//
//                                        }
//                                    })
//                                    .build()
//                                    .show();
//                        }
//                    })
//                    .build()
//                    .show();
//            }
//        }

    }


    public void buscar_paciente() {
        txt_not_found.setVisibility(View.GONE);
        btn_registrar_paciente.setVisibility(View.VISIBLE);
        list_datos_paciente.clear();
        exp_list_view.clearTextFilter();
        exp_list_view.setAdapter((BaseExpandableListAdapter) null);

        if (!utils.validarCamposRequeridos(txt_search))
            return;

        PacienteService service = (PacienteService) HttpUtils.crearServicio(PacienteService.class);
        final Session session = Session.getInstance(getActivity());
        Observable<Response<ResponsePacienteBusqueda>> pacienteObservable = service.buscar(txt_search.getText().toString(), session.getCredentials().getEmail(), session.getCredentials().getToken());
        HttpUtils.configurarObservable(contexto, pacienteObservable, this::procesarRespuestaBuscar, this::procesarExcepcionServicio);
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable instanceof ConnectException)
            cargar_consultas(txt_search.getText().toString());

        super.procesarExcepcionServicio(throwable);
    }

    // TODO: Sebas - revisar respuesta
    private void procesarRespuestaBuscar(Response<ResponsePacienteBusqueda> response) {
        if (validarRespuestaServicio(response, null)) {
            if (response.body().paciente.getIdServidor() != null) {
                Paciente paciente = response.body().paciente;
                paciente.setStatus(response.body().informacion_paciente.getStatus());
                dbUtil.crearPaciente(paciente);
                if (response.body().informacion_paciente.getIdServidor() != null)
                    dbUtil.crearInformacionPaciente(response.body().informacion_paciente);
                if (response.body().consultas.size() > 0) {
                    for (int i = 0; i < response.body().consultas.size(); i++) {
                        dbUtil.crearConsulta(response.body().consultas.get(i));
                        dbUtil.crearInformacionPaciente(response.body().consultas.get(i).getInformacionPaciente());
                        if (dbUtil.obtenerUsuarioLogueado(response.body().consultas.get(i).getIdDoctor()) == null && dbUtil.obtenerUsuarioLogueado(response.body().consultas.get(i).getIdEnfermera()) == null) {
                            if (response.body().consultas.get(i).getIdDoctor() != null) {
                                Usuario doctor = Usuario.fillNewUser(response.body().consultas.get(i).getDoctor(), 1);
                                dbUtil.crearUsuario(doctor);
                            } else if (response.body().consultas.get(i).getIdEnfermera() != null) {
                                Usuario enfermera = Usuario.fillNewUser(response.body().consultas.get(i).getNurse(), 2);
                                dbUtil.crearUsuario(enfermera);
                            }
                        }
                    }
                }
            }
        }
        cargar_consultas(txt_search.getText().toString());
    }





    @Override
    protected int getIdLayout() {
        return R.layout.fragment_busqueda_paciente;
    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_nueva_consulta;
    }
}
