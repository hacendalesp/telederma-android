package com.telederma.gov.co.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ExpandableListHelpDeskAdapter;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.MesaAyudaService;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseHelpDesk;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.modelo.HelpDeskRes;
import com.telederma.gov.co.utils.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.Response;

public class MesaAyudaVerTickets extends  BaseFragment {

    private TextView lblActualizar, lbl_bandeja_vacia;


    public static final Map<Integer, Integer[]> estadosPorTab = new HashMap<>();
    private SwipeRefreshLayout srlElvConsultas;
    private ExpandableListHelpDeskAdapter elvConsultasAdapter;
    private ExpandableListView elvConsultas;
    private TextView lblBandejaVacia;
    private MesaAyudaVerTickets.DatosExpandableListViewConsultas datosExpandableList;
    MesaAyudaService consultaService ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        datosExpandableList = new MesaAyudaVerTickets.DatosExpandableListViewConsultas();
        consultaService = (MesaAyudaService) HttpUtils.crearServicio(MesaAyudaService.class);
        srlElvConsultas = rootView.findViewById(R.id.srl_elv_consultas);
        elvConsultas = rootView.findViewById(R.id.elv_consultas_resueltas);
        lblBandejaVacia = rootView.findViewById(R.id.lbl_bandeja_vacia);
        srlElvConsultas.setOnRefreshListener(onSwipeToRefresh());
        elvConsultas.setEmptyView(lblBandejaVacia);
        elvConsultas.setGroupIndicator(null);
        lblActualizar = rootView.findViewById(R.id.lbl_actualizar);
        lbl_bandeja_vacia = rootView.findViewById(R.id.lbl_bandeja_vacia);
        obtenerConsultarDB();
        return rootView;
    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh() {
        return () -> mostrarMensajeEspera(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                obtenerConsultasServicio();
                lblActualizar.startAnimation(
                        AnimationUtils.loadAnimation(contexto, R.anim.hide_slide_up)
                );
                lblActualizar.setVisibility(View.GONE);
            }
        });
    }

    private void obtenerConsultarDB()
    {
        ocultarMensajeEspera();
        List<HelpDesk> listHelpDesk =  dbUtil.getAllMesaAyuda(Session.getInstance(contexto).getCredentials().getIdUsuario());
        for (HelpDesk resHelp : listHelpDesk) {
            datosExpandableList.listaHelpDesk.add(resHelp);
            elvConsultasAdapter = new ExpandableListHelpDeskAdapter((AppCompatActivity) getActivity(), datosExpandableList.listaHelpDesk);
            elvConsultas.setGroupIndicator(null);
            elvConsultas.setAdapter(elvConsultasAdapter);
        }
        lblActualizar.setVisibility(View.INVISIBLE);
    }

    private void obtenerConsultasServicio() {

        ocultarMensajeEspera();
        Observable<Response<List<HelpDesk>>> consultasObservable = consultaService.getTickets(
                Session.getInstance(contexto).getCredentials().getEmail(),
                Session.getInstance(contexto).getCredentials().getToken()
        );
        HttpUtils.configurarObservable(
                getContext(), consultasObservable,
                this::procesarRespuestaConsultar, this::procesarExcepcionServicio
        );
    }


    private void procesarRespuestaConsultar(Response<List<HelpDesk>> response) {

        if (validarRespuestaServicio(response, null)) {
            dbUtil.eliminarMesaAyuda();
            datosExpandableList.listaHelpDesk = new ArrayList<>();
            for (HelpDesk resHelp : response.body())
            {

                dbUtil.crearMesaAyuda(resHelp);
            }
        }
        lblActualizar.setVisibility(View.INVISIBLE);
        srlElvConsultas.setRefreshing(false);
        obtenerConsultarDB();
    }
    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        super.procesarExcepcionServicio(throwable);

        if (srlElvConsultas.isRefreshing())
            srlElvConsultas.setRefreshing(false);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_ver_list_mesa_ayuda;
    }


    private class DatosExpandableListViewConsultas {

        List<HelpDesk> listaHelpDesk;
        Map<Integer, List<HelpDesk>> mapaHelpDesk;

        public DatosExpandableListViewConsultas() {
            listaHelpDesk = new ArrayList<>();
            mapaHelpDesk = new HashMap<>();
        }

    }




}