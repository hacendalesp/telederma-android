package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ExpandableListConsultasAdapter;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hernández on 16/07/2018.
 */

public class DetalleConsultaHistorialFragment extends DetalleConsultaContentFragment {

    private ExpandableListView elvConsultas;
    private ExpandableListConsultasAdapter elvConsultasAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        elvConsultas = rootView.findViewById(R.id.elv_consulta_historial);

        final List<Consulta> consultas = new ArrayList<>();
        if(consultaMedicina != null) {
            //todo: agregar respuesta del especialista en la lista de consultas para mostrar los datos
            final List<ConsultaMedica> consultaMedicas = dbUtil.obtenerConsultasMedicasPaciente(
                    paciente.getIdServidor()
            );
            for(ConsultaMedica consulta : consultaMedicas) {
                consulta.setControles(new ArrayList<>());
                for(ControlMedico control : consulta.getControlesMedicos())
                    consulta.getControles().add(control);
                consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                consultas.add(consulta);

            }
        } else if(consultaEnfermeria != null) {
            final List<ConsultaEnfermeria> consultaEnfermerias = dbUtil.obtenerConsultasEnfermeriaPaciente(
                    paciente.getInformacionPaciente().getIdServidor()
            );
            for(ConsultaEnfermeria consulta : consultaEnfermerias){
                consulta.setControles(new ArrayList<>());
                for(ControlEnfermeria control : consulta.getControlesEnfermeria())
                    consulta.getControles().add(control);
                consulta.setRespuestaEspecialista(dbUtil.obtenerRespuestaEspecialista(consulta.getIdConsulta()));
                consultas.add(consulta);
            }
        }

        elvConsultasAdapter = new ExpandableListConsultasAdapter((AppCompatActivity) getActivity(), consultas);
        elvConsultas.setGroupIndicator(null);
        elvConsultas.setAdapter(elvConsultasAdapter);

        return rootView;
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_detalle_consulta_historial;
    }
}
