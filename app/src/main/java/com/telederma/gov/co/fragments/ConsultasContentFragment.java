package com.telederma.gov.co.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ExpandableListPacientesAdapter;
import com.telederma.gov.co.dialogs.VerOpcionesDialog;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ADVERTENCIA;
import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Created by Daniel Hernández on 6/26/2018.
 */

public class ConsultasContentFragment extends BaseFragment {

    public static final String ARG_TAB_SELECTED = "ARG_TAB_SELECTED";
    public static final int TAB_RESUELTAS = 0;
    public static final int TAB_PENDIENTES = 1; // pendientes de respuesta o están evaluando en ese momento.
    public static final int TAB_ARCHIVADAS = 2;
    private TextView lblActualizar;

    private Integer tipoProfesional;

    public static final Map<Integer, Integer[]> estadosPorTab = new HashMap<>();

    static {
        estadosPorTab.put(TAB_RESUELTAS, new Integer[]{
                Consulta.ESTADO_CONSULTA_RESUELTO, Consulta.ESTADO_CONSULTA_REQUERIMIENTO, Consulta.ESTADO_CONSULTA_REMISION
        });
        estadosPorTab.put(TAB_PENDIENTES, new Integer[]{
                Consulta.ESTADO_CONSULTA_PENDIENTE, Consulta.ESTADO_CONSULTA_PROCESO,
                Consulta.ESTADO_CONSULTA_SIN_CREDITOS, Consulta.ESTADO_CONSULTA_EVALUANDO
        });
        estadosPorTab.put(TAB_ARCHIVADAS, new Integer[]{
                Consulta.ESTADO_CONSULTA_ARCHIVADO
        });
    }

    private SwipeRefreshLayout srlElvConsultas;
    private ExpandableListPacientesAdapter elvConsultasAdapter;
    private ExpandableListView elvConsultas;
    private TextView lblBandejaVacia;
    private ImageView ivBuscarPaciente;

    private DatosExpandableListViewConsultas datosExpandableList;

    public static int tabSelected = TAB_RESUELTAS;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        datosExpandableList = new DatosExpandableListViewConsultas();
        tipoProfesional = Session.getInstance(contexto).getCredentials().getTipoProfesional();

        if (getArguments() != null && !getArguments().isEmpty()
                && getArguments().containsKey(ARG_TAB_SELECTED)) {
            tabSelected = getArguments().getInt(ARG_TAB_SELECTED);
        }

        srlElvConsultas = rootView.findViewById(R.id.srl_elv_consultas);
        elvConsultas = rootView.findViewById(R.id.elv_consultas_resueltas);
        lblBandejaVacia = rootView.findViewById(R.id.lbl_bandeja_vacia);
        lblActualizar = rootView.findViewById(R.id.lbl_actualizar);
        ivBuscarPaciente = rootView.findViewById(R.id.iv_buscar_paciente);
        ivBuscarPaciente.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_content, new BusquedaPacienteFragment())
                    .addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK)
                    .commit();
        });

        srlElvConsultas.setOnRefreshListener(onSwipeToRefresh());
        elvConsultas.setEmptyView(lblBandejaVacia);
        elvConsultas.setGroupIndicator(null);

        if (tabSelected == TAB_ARCHIVADAS) {
            lblBandejaVacia.setVisibility(View.GONE);
            lblActualizar.setVisibility(View.GONE);
            ivBuscarPaciente.setVisibility(View.VISIBLE);
            srlElvConsultas.setVisibility(View.GONE);

        } else {

            lblBandejaVacia.setVisibility(View.VISIBLE);
            lblActualizar.setVisibility(View.VISIBLE);
            ivBuscarPaciente.setVisibility(View.GONE);
            srlElvConsultas.setVisibility(View.VISIBLE);

            mostrarMensajeEspera(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar sb) {
                    actualizarConsultasView();
                }
            });

            lblActualizar = rootView.findViewById(R.id.lbl_actualizar);
            if (tabSelected == 2)
                lblBandejaVacia.setText(getResources().getString(R.string.consultas_actualizar));
        }


        return rootView;
    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh() {
        return () -> mostrarMensajeEspera(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                obtenerConsultasServicio(estadosPorTab.get(tabSelected));
                lblActualizar.startAnimation(
                        AnimationUtils.loadAnimation(contexto, R.anim.hide_slide_up)
                );
                lblActualizar.setVisibility(View.GONE);
            }
        });
    }


    private void obtenerConsultasServicio(Integer... estadosConsultas) {
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        final Session session = Session.getInstance(contexto);
        Observable<Response<List<ResponsePacienteBusqueda>>> consultasObservable = service.consultarConsultas(
                estadosConsultas,
                session.getCredentials().getUsername(),
                session.getCredentials().getEmail(),
                session.getCredentials().getToken());
        HttpUtils.configurarObservable(
                contexto, consultasObservable,
                this::procesarRespuestaConsultar, this::procesarExcepcionServicio
        );
    }

    private void procesarRespuestaConsultar(Response<List<ResponsePacienteBusqueda>> response) {
        if (validarRespuestaServicio(response, null)) {
            if (tabSelected == TAB_RESUELTAS || tabSelected == TAB_PENDIENTES || tabSelected == TAB_ARCHIVADAS) {
                actualizarConsultasBD(response.body());
            }
            // validacion para no duplicar el proceso
            //if (!srlElvConsultas.isRefreshing())
            //actualizarConsultasView();
        } else if (srlElvConsultas.isRefreshing())
            srlElvConsultas.setRefreshing(false);
    }

    private void actualizarConsultasBD(List<ResponsePacienteBusqueda> consultasResponse) {
        //TODO Mejorar estrategia de actualización
        // obtenerConsultasBD(estadosPorTab.get(tabSelected));
        for (Map.Entry<Integer, List<Consulta>> entry : datosExpandableList.mapaConsultas.entrySet())
            dbUtil.eliminarConsulta(
                    Session.getInstance(contexto).getCredentials().getTipoProfesional(),
                    entry.getValue()
            );

        if (tabSelected == TAB_ARCHIVADAS) {
            datosExpandableList = new DatosExpandableListViewConsultas();
        }

        // TODO: Sebas - revisar respuestas
        //obtenerConsultasMedicinaBD(Session.getInstance(contexto).getCredentials().getIdUsuario(), (estadosPorTab.get(tabSelected)))
        for (ResponsePacienteBusqueda response : consultasResponse) {
            dbUtil.crearPaciente(response.paciente);
            if (response.paciente.getConsultas() == null)
                continue; // salta el ciclo de iteración
            Log.i("tab", "tabSelected=>" + tabSelected);
            for (Consulta consulta : response.paciente.getConsultas()) {
                dbUtil.crearConsulta(consulta, tabSelected);
                dbUtil.crearInformacionPaciente(consulta.getInformacionPaciente());
            }

            if (tabSelected == TAB_ARCHIVADAS) {

                try{
                    //datosExpandableList = new DatosExpandableListViewConsultas();
                    datosExpandableList.listaPacientes.add(response.paciente);
                    for (Consulta c : response.paciente.getConsultas())
                        c.setArchivado(true);
                    datosExpandableList.mapaConsultas.put(
                            response.paciente.getIdServidor(),
                            response.paciente.getConsultas()
                    );
                    //dbUtil.getConsultaMedicaByServer(response.paciente.getConsultas().get(0).getConsultaMedica().getIdServidor())

                }catch (Exception e){
                    Log.e("s", "s00");
                    int a = 10;

                }

            }
            response.paciente.setConsultas(null);
        }
        if (tabSelected != TAB_ARCHIVADAS) {
            // obtiene consultas creadas despues de respuesta en el servidor
            obtenerConsultasBD(estadosPorTab.get(tabSelected));
        } else {
            actualizarAdaptador();
        }
    }


    private void actualizarConsultasView() {

        if (tabSelected == TAB_RESUELTAS) {
            obtenerConsultasBD(estadosPorTab.get(tabSelected));
        } else if (tabSelected == TAB_PENDIENTES) {
            obtenerConsultasBD(estadosPorTab.get(tabSelected));
        }


        actualizarAdaptador();

    }


    private void actualizarAdaptador() {
        boolean mostrarOpcionCompartir = false, mostrarBotonNuevaConsulta = false;
        if (tabSelected == TAB_RESUELTAS) {
            mostrarOpcionCompartir = true;
            mostrarBotonNuevaConsulta = true;
        } else if (tabSelected == TAB_PENDIENTES) {

        }

        elvConsultasAdapter = new ExpandableListPacientesAdapter(
                (AppCompatActivity) getActivity(),
                datosExpandableList.listaPacientes,
                datosExpandableList.mapaConsultas,
                mostrarOpcionCompartir,
                mostrarBotonNuevaConsulta,
                true,
                tabSelected
        );

        elvConsultasAdapter.tabWorking = tabSelected;

        elvConsultas.setAdapter(elvConsultasAdapter);
        elvConsultasAdapter.notifyDataSetChanged();

        elvConsultas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String l = "";
                if (tabSelected == TAB_PENDIENTES) {
                    mostrarMensaje("Esta consulta no se puede archivar por que " +
                            "esta en estado de pendiente", MSJ_ADVERTENCIA);
                } else {
                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                        final Paciente paciente = (Paciente) elvConsultas.getAdapter().getItem(position);
                        List<Consulta> consultas = elvConsultasAdapter.getMapa_consultas().get(paciente.getIdServidor());
                        if (consultas != null) {
                            if (consultas.size() > 0) {
                                Consulta consulta = consultas.get(0);
                                VerOpcionesDialog opciones_dialog;
                                if (consulta.getArchivado() == null) {
                                    opciones_dialog = new VerOpcionesDialog(contexto, contexto.getString(R.string.archivar), contexto.getResources().getDrawable(R.drawable.archivar_p));
                                } else {
                                    opciones_dialog = new VerOpcionesDialog(contexto, contexto.getString(R.string.desarchivar), contexto.getResources().getDrawable(R.drawable.desarchivar_p));
                                }
                                opciones_dialog.setConsultas(consultas);
                                opciones_dialog.setAdapter(elvConsultasAdapter);
                                opciones_dialog.show();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }


        });

        ocultarMensajeEspera();

        if (srlElvConsultas.isRefreshing())
            srlElvConsultas.setRefreshing(false);
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        super.procesarExcepcionServicio(throwable);

        if (srlElvConsultas.isRefreshing())
            srlElvConsultas.setRefreshing(false);
    }

    private void obtenerConsultasBD(Integer... estadosConsultas) {
        final int idUsuario = Session.getInstance(contexto).getCredentials().getIdUsuario();

        if (Integer.valueOf(1).equals(tipoProfesional))
            obtenerConsultasMedicinaBD(idUsuario, estadosConsultas);
        else if (Integer.valueOf(2).equals(tipoProfesional))
            obtenerConsultasEnfermeriaBD(idUsuario, estadosConsultas);
    }


    private final void obtenerConsultasMedicinaBD(int idUsuario, Integer... estadosConsultas) {
        try {
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaMedicaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> consultaMedicaQueryBuilder = consultaMedicaDAO.queryBuilder();
            List<ConsultaMedica> consultas = new ArrayList<>();
            if (tabSelected == TAB_RESUELTAS || tabSelected == TAB_PENDIENTES) {
                consultaMedicaQueryBuilder.where().eq(
                        ConsultaMedica.NOMBRE_CAMPO_ID_DOCTOR, idUsuario
                ).and().isNull(Consulta.NOMBRE_CAMPO_ARCHIVADO)
                        .and().isNotNull(ConsultaMedica.NOMBRE_CAMPO_UPDATED_AT).and().in(
                        ConsultaMedica.NOMBRE_CAMPO_ESTADO, (Object[]) estadosConsultas
                );

                consultaMedicaQueryBuilder.orderBy(ConsultaMedica.NOMBRE_CAMPO_UPDATED_AT, false);
                consultas = consultaMedicaDAO.query(consultaMedicaQueryBuilder.prepare());

            } else {
                //.in( ConsultaMedica.NOMBRE_CAMPO_ESTADO, (Object[]) estadosConsultas).and()
                consultaMedicaQueryBuilder.where()

                        .eq(ConsultaMedica.NOMBRE_CAMPO_ID_DOCTOR, idUsuario).and()
                        .eq(ConsultaMedica.NOMBRE_CAMPO_ARCHIVADO, true)
                        .and().isNotNull(ConsultaMedica.NOMBRE_CAMPO_UPDATED_AT);

                consultaMedicaQueryBuilder.orderBy(ConsultaMedica.NOMBRE_CAMPO_UPDATED_AT, false);
                consultas = consultaMedicaDAO.query(consultaMedicaQueryBuilder.prepare());
//
            }


            if (!consultas.isEmpty()) {
                //List<Integer> a = consultas.stream().map(ConsultaMedica::getIdPaciente).collect(Collectors.toList());
                RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
                QueryBuilder<Paciente, Integer> pacienteQueryBuilder = pacienteDAO.queryBuilder();
//                Map<Integer, List<Consulta>> cmListGrouped =
//                        consultas.stream().collect(Collectors.groupingBy(w -> w.getIdPaciente()));
//                datosExpandableList.mapaConsultas = cmListGrouped;

                datosExpandableList.listaPacientes = new ArrayList<>();
                datosExpandableList.mapaConsultas = new HashMap<>();
                for (ConsultaMedica consulta : consultas) {

                    pacienteQueryBuilder.where().eq(Paciente.NOMBRE_CAMPO_ID_SERVIDOR, consulta.getIdPaciente());
                    Paciente p = pacienteDAO.queryForFirst(pacienteQueryBuilder.prepare());

                    consulta.setIdServidor(consulta.getIdConsulta());
                    consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                    if (!datosExpandableList.listaPacientes.contains(p)) {
                        datosExpandableList.listaPacientes.add(p);
                    }
                    if (datosExpandableList.mapaConsultas.containsKey(consulta.getIdPaciente())) {
                        int index = datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).indexOf(consulta);
                        if (index >= 0)
                            datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).set(index, consulta);
                        else
                            datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
                    } else {

                        datosExpandableList.mapaConsultas.put(consulta.getIdPaciente(), new ArrayList<>());
                        datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
                    }
                }

                //Ordenar pacientes por la ultima fecha
                try {
                    Collections.sort(datosExpandableList.listaPacientes, new Comparator<Paciente>() {
                        @Override
                        public int compare(Paciente p1, Paciente p2) {
                            return obtenerFechaRecienteConsulta(p2.getIdServidor()).compareTo(obtenerFechaRecienteConsulta(p1.getIdServidor()));
                        }
                    });
                } catch (Exception e) {

                }

                actualizarAdaptador();

            } else {
                // Oculta el mensaje de refrescando en la pantalla
                ocultarMensajeEspera();

                if (srlElvConsultas.isRefreshing())
                    srlElvConsultas.setRefreshing(false);
            }

//            if (!consultas.isEmpty()) {
//                RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
//                QueryBuilder<Paciente, Integer> pacienteQueryBuilder = pacienteDAO.queryBuilder();
//                pacienteQueryBuilder.distinct().where().in(
//                        Paciente.NOMBRE_CAMPO_ID_SERVIDOR,
//                        consultas.stream().map(ConsultaMedica::getIdPaciente).collect(Collectors.toList())
//                );
//                pacienteQueryBuilder.orderBy(Paciente.NOMBRE_CAMPO_CREATED_AT ,false);
//                datosExpandableList.listaPacientes = pacienteDAO.query(pacienteQueryBuilder.prepare());
//
//                for (Paciente paciente : datosExpandableList.listaPacientes)
//                    datosExpandableList.mapaConsultas.put(paciente.getIdServidor(), new ArrayList<>());
//
//                for (ConsultaMedica consulta : consultas) {
//                    consulta.setIdServidor(consulta.getIdConsulta());
//                    consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
//                    datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
//                }
//            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_MENU_ACTIVITY, "Ha ocurrido un error obteniendo las consultas", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    private Date obtenerFechaRecienteConsulta(int idPaciente) {
        try {
            List<Consulta> consultas = datosExpandableList.mapaConsultas.get(idPaciente);
            Date date = null;

            for (Consulta consulta : consultas) {
                Date created_atFormatDate = consulta.getCreated_atFormatDate();

                if (date == null) {
                    date = created_atFormatDate;
                } else {
                    if (date.compareTo(created_atFormatDate) < 0) {
                        date = created_atFormatDate;
                    }
                }
            }

            return date;


        } catch (Exception e) {

        }

        return null;
    }

    private final void obtenerConsultasEnfermeriaBD(int idUsuario, Integer... estadosConsultas) {
        try {
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> consultaQueryBuilder = consultaDAO.queryBuilder();
            consultaQueryBuilder.where().in(
                    ConsultaEnfermeria.NOMBRE_CAMPO_ESTADO, (Object[]) estadosConsultas
            ).and().eq(
                    ConsultaEnfermeria.NOMBRE_CAMPO_ID_ENFERMERA, idUsuario
            ).and().isNull(Consulta.NOMBRE_CAMPO_ARCHIVADO);
            consultaQueryBuilder.orderBy(Consulta.NOMBRE_CAMPO_CREATED_AT, false);
            List<ConsultaEnfermeria> consultas = consultaDAO.query(consultaQueryBuilder.prepare());

            if (!consultas.isEmpty()) {
                RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
                QueryBuilder<Paciente, Integer> pacienteQueryBuilder = pacienteDAO.queryBuilder();
                pacienteQueryBuilder.distinct().where().in(
                        Paciente.NOMBRE_CAMPO_ID_SERVIDOR,
                        consultas.stream().map(ConsultaEnfermeria::getIdPaciente).collect(Collectors.toList())
                );
                pacienteQueryBuilder.orderBy(Paciente.NOMBRE_CAMPO_CREATED_AT, false);

                datosExpandableList.listaPacientes = pacienteDAO.query(pacienteQueryBuilder.prepare());

                for (Paciente paciente : datosExpandableList.listaPacientes)
                    datosExpandableList.mapaConsultas.put(paciente.getIdServidor(), new ArrayList<>());

                for (ConsultaEnfermeria consulta : consultas) {
                    consulta.setIdServidor(consulta.getIdConsulta());
                    consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                    datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
                }
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_MENU_ACTIVITY, "Ha ocurrido un error obteniendo las consultas", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
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
                //List<Integer> a = consultas.stream().map(ConsultaMedica::getIdPaciente).collect(Collectors.toList());
                RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
                QueryBuilder<Paciente, Integer> pacienteQueryBuilder = pacienteDAO.queryBuilder();
//                Map<Integer, List<Consulta>> cmListGrouped =
//                        consultas.stream().collect(Collectors.groupingBy(w -> w.getIdPaciente()));
//                datosExpandableList.mapaConsultas = cmListGrouped;

                datosExpandableList.listaPacientes = new ArrayList<>();
                datosExpandableList.mapaConsultas = new HashMap<>();
                for (ConsultaMedica consulta : consultas) {

                    pacienteQueryBuilder.where().eq(Paciente.NOMBRE_CAMPO_ID_SERVIDOR, consulta.getIdPaciente());
                    Paciente p = pacienteDAO.queryForFirst(pacienteQueryBuilder.prepare());

                    consulta.setIdServidor(consulta.getIdConsulta());
                    consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                    if (!datosExpandableList.listaPacientes.contains(p)) {
                        datosExpandableList.listaPacientes.add(p);
                    }
                    if (datosExpandableList.mapaConsultas.containsKey(consulta.getIdPaciente())) {
                        int index = datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).indexOf(consulta);
                        if (index >= 0)
                            datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).set(index, consulta);
                        else
                            datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
                    } else {

                        datosExpandableList.mapaConsultas.put(consulta.getIdPaciente(), new ArrayList<>());
                        datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
                    }
                }

                actualizarAdaptador();

            } else {
                // Oculta el mensaje de refrescando en la pantalla
                ocultarMensajeEspera();

                if (srlElvConsultas.isRefreshing())
                    srlElvConsultas.setRefreshing(false);
            }

//            if (!consultas.isEmpty()) {
//                RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
//                QueryBuilder<Paciente, Integer> pacienteQueryBuilder = pacienteDAO.queryBuilder();
//                pacienteQueryBuilder.distinct().where().in(
//                        Paciente.NOMBRE_CAMPO_ID_SERVIDOR,
//                        consultas.stream().map(ConsultaMedica::getIdPaciente).collect(Collectors.toList())
//                );
//                pacienteQueryBuilder.orderBy(Paciente.NOMBRE_CAMPO_CREATED_AT ,false);
//                datosExpandableList.listaPacientes = pacienteDAO.query(pacienteQueryBuilder.prepare());
//
//                for (Paciente paciente : datosExpandableList.listaPacientes)
//                    datosExpandableList.mapaConsultas.put(paciente.getIdServidor(), new ArrayList<>());
//
//                for (ConsultaMedica consulta : consultas) {
//                    consulta.setIdServidor(consulta.getIdConsulta());
//                    consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
//                    datosExpandableList.mapaConsultas.get(consulta.getIdPaciente()).add(consulta);
//                }
//            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_MENU_ACTIVITY, "Ha ocurrido un error obteniendo las consultas", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_consultas_content;
    }


    private class DatosExpandableListViewConsultas {

        List<Paciente> listaPacientes;
        Map<Integer, List<Consulta>> mapaConsultas;

        public DatosExpandableListViewConsultas() {
            listaPacientes = new ArrayList<>();
            mapaConsultas = new HashMap<>();
        }

    }


}
