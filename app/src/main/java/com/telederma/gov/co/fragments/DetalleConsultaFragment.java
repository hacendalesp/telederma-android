package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.TrazabilidadService;
import com.telederma.gov.co.http.request.TrazabilidadRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.modelo.Trazabilidad;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.http.HttpUtils;

import java.util.Date;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Daniel Hernández on 16/07/2018.
 */

public class DetalleConsultaFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    public static final String ARG_ID_CONSULTA = "ARG_ID_CONSULTA";
    public static final String ARG_ID_CONTROL = "ARG_ID_CONTROL";

    private static final int TAB_HISTORIA_CLINICA = 0;
    private static final int TAB_IMAGENES = 1;
    private static final int TAB_RESPUESTA = 2;
    private static final int TAB_HISTORIAL = 3;

    private TabLayout tabsDetalleConsulta;
    private Button btnNuevoControl;
    private Integer id_consulta = -1, id_control = -1, tipoProfesional;
    public static ControlMedico tempControlMedico = null;
    private ProgressBar srlElvConsultas;
    private  Boolean registrarTrazabilidad = true ;
    private  Integer control_actual = null ;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_detalle_consulta;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        tipoProfesional = Session.getInstance(contexto).getCredentials().getTipoProfesional();
        if (getArguments() != null && !getArguments().isEmpty() && getArguments().containsKey(ARG_ID_CONSULTA)) {
            id_consulta = getArguments().getInt(ARG_ID_CONSULTA);
            tempControlMedico = null;
        }
        getNavegationArguments();


        tabsDetalleConsulta = (TabLayout) view.findViewById(R.id.tabs_detalle_consulta);
        tabsDetalleConsulta.addTab(tabsDetalleConsulta.newTab().setText(R.string.detalle_consulta_historia_clinica_title));
        tabsDetalleConsulta.addTab(tabsDetalleConsulta.newTab().setText(R.string.detalle_consulta_imagenes_title));
        tabsDetalleConsulta.addTab(tabsDetalleConsulta.newTab().setText(R.string.detalle_consulta_respuesta_title));
        tabsDetalleConsulta.addTab(tabsDetalleConsulta.newTab().setText(R.string.detalle_consulta_historial_title));
        tabsDetalleConsulta.addOnTabSelectedListener(this);

        btnNuevoControl = (Button) view.findViewById(R.id.btn_nuevo_control);
        btnNuevoControl.setOnClickListener(this::nuevoControl);

        srlElvConsultas = (ProgressBar)view.findViewById(R.id.srl_elv_consultas_detalle);
        srlElvConsultas.setVisibility(View.VISIBLE);

        if(HttpUtils.validarConexionApi())
            obtenerConsultaServicio();
        else {
            srlElvConsultas.setVisibility(View.GONE);
            replaceFragment(TAB_HISTORIA_CLINICA);
        }

        return view;
    }



    private void registrarTrazabilidad(Integer id_consulta, Integer id_control) {
        try
        {
            if(HttpUtils.validarConexionApi())
            {
                TrazabilidadService trazabilidadServiceService = (TrazabilidadService) HttpUtils.crearServicio(TrazabilidadService.class);
                Observable<Response<BaseResponse>> deviceObservable = trazabilidadServiceService.enviarTrazabilidad(new TrazabilidadRequest((id_consulta==-1)?null:id_consulta,(id_control==-1)?null:id_control,true,Session.getInstance(contexto).getCredentials().getToken(), Session.getInstance(contexto).getCredentials().getEmail()));
                HttpUtils.configurarObservable(
                        getContext(), deviceObservable,
                        this::procesarRespuestaConsultar, this::procesarExcepcionServicio
                );
            }
            else
                {
                    Trazabilidad t = new Trazabilidad();
                    t.setConsultation_id((id_consulta==-1)?null:id_consulta);
                    t.setControl_id((id_control==-1)?null:id_control);
                    t.setTipo(false);
                    Integer id_trazabilidad = dbUtil.crearTrazabilidad(t);
                    if(id_trazabilidad != null)
                    {
                        PendienteSincronizacion pendiente = new PendienteSincronizacion();

                        ///////////////////////////INFORMACION PACIENTE /////////////////////////////
                        pendiente = new PendienteSincronizacion();
                        pendiente.setId_local(id_trazabilidad + "");
                        pendiente.setTable(Trazabilidad.NOMBRE_TABLA);
                        pendiente.setEmail(session.getCredentials().getEmail());
                        pendiente.setToken(session.getCredentials().getToken());
                        pendiente.setRegistration_date(new Date());
                        dbUtil.crearPendienteSincronizacion(pendiente);
                    }
                }

        } catch (Exception e) {

        }
    }

    private void procesarRespuestaConsultar(Response<BaseResponse> response) {
        String l = "";
    }


    public void getNavegationArguments(){
        if (getArguments() != null && !getArguments().isEmpty()) {
            if (getArguments().containsKey(ARG_ID_CONTROL)) {
                id_control = getArguments().getInt(ARG_ID_CONTROL);
                tempControlMedico = dbUtil.getControlMedicoServidor(String.valueOf(getArguments().getInt(ARG_ID_CONTROL)));
            } else
                loadDefaultControl();
        } else
            loadDefaultControl();
    }
    private void loadDefaultControl(){
        if (tempControlMedico != null)
        {
            id_control = tempControlMedico.getIdServidor();
            if(control_actual !=id_control)
            {
                control_actual = id_control;
                registrarTrazabilidad = true ;
            }

        }

    }

    private void nuevoControl(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;

        if(Constantes.TIPO_PROFESIONAL_MEDICO.equals(tipoProfesional))
            fragment = new RegistrarControlFragment();
        else
            fragment = new RegistrarControlEnfermeriaFragment();

        if (id_consulta != -1) {
            final Paciente paciente = dbUtil.obtenerPacienteConsulta(tipoProfesional, id_consulta);
            final Bundle args = new Bundle();
            args.putInt("id_consulta", id_consulta);
            args.putBoolean("cuerpo_frente",true);
            args.putInt("genero",paciente.getGenre());
            args.putInt("id_content",R.id.menu_content);
            fragment.setArguments(args);
        }

        ft.replace(R.id.menu_content, fragment).addToBackStack(
                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        ).commit();
    }

    private void replaceFragment(int position) {
        srlElvConsultas.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Fragment fragment;
        getNavegationArguments();
        switch (position) {
            case TAB_HISTORIA_CLINICA:
                fragment = tipoProfesional == 1 ? new DetalleConsultaMedicaHistoriaClinicaFragment()
                    : new DetalleConsultaEnfermeriaHistoriaClinicaFragment();
                break;

            case TAB_IMAGENES:
                fragment = new DetalleConsultaImagenesFragment();
                break;

            case TAB_RESPUESTA:
                fragment = new DetalleConsultaRespuestaFragment();
                break;

            case TAB_HISTORIAL:
                fragment = new DetalleConsultaHistorialFragment();
                break;

            default:
                fragment = new DetalleConsultaMedicaHistoriaClinicaFragment();
                break;
        }
        if(registrarTrazabilidad)
        {
            this.registrarTrazabilidad(id_consulta,id_control);
            registrarTrazabilidad = false ;
        }

        final Bundle args = new Bundle();
        args.putInt(ARG_ID_CONSULTA, id_consulta);
        args.putInt(ARG_ID_CONTROL, id_control);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(
                R.id.tabs_detalle_consulta_content, fragment
        ).commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        replaceFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void obtenerConsultaServicio() {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        final Session session = Session.getInstance(contexto);
        String consultaId = (id_consulta==-1)?null:String.valueOf(id_consulta);
        String controlId  = (id_control==-1)?null:String.valueOf(id_control);
        Observable<Response<ResponsePacienteBusqueda>> consultasObservable = service.getConsulta(
                consultaId,
                controlId,
                session.getCredentials().getEmail(),
                session.getCredentials().getToken());
        HttpUtils.configurarObservable(
                contexto, consultasObservable,
                this::procesarRespuestaConsulta, this::procesarExcepcionServicio
        );
    }

    private void procesarRespuestaConsulta(Response<ResponsePacienteBusqueda> response) {
        if (validarRespuestaServicio(response, null))
            actualizarConsultasBD(response.body());
    }

    private void actualizarConsultasBD(ResponsePacienteBusqueda response)
    {
        dbUtil.eliminarConsulta(
                Session.getInstance(contexto).getCredentials().getTipoProfesional(),
                response.paciente.getConsultas()
        );
        dbUtil.crearPaciente(response.paciente);

        for (Consulta consulta : response.paciente.getConsultas()) {
            dbUtil.crearConsulta(consulta);
            dbUtil.crearInformacionPaciente(consulta.getInformacionPaciente());
        }


        replaceFragment(TAB_HISTORIA_CLINICA);
    }

}


