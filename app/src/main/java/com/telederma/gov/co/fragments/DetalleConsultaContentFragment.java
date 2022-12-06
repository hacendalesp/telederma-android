package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;

import static com.telederma.gov.co.fragments.DetalleConsultaFragment.ARG_ID_CONSULTA;
import static com.telederma.gov.co.fragments.DetalleConsultaFragment.ARG_ID_CONTROL;
import static com.telederma.gov.co.utils.Constantes.TIPO_PROFESIONAL_ENFERMERA;
import static com.telederma.gov.co.utils.Constantes.TIPO_PROFESIONAL_MEDICO;

/**
 * Created by Daniel Hernández on 16/07/2018.
 */

abstract class DetalleConsultaContentFragment extends BaseFragment {

    protected Paciente paciente;
    protected Integer tipoProfesional; // 1 -> Médico, 2 -> Enfermera
    protected ConsultaMedica consultaMedicina;
    protected ConsultaEnfermeria consultaEnfermeria;
    protected static ControlMedico controlMedico;
    protected static ControlEnfermeria controlEnfermeria;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        tipoProfesional = Session.getInstance(contexto).getCredentials().getTipoProfesional();

        if (getArguments() != null && !getArguments().isEmpty() && getArguments().containsKey(ARG_ID_CONSULTA)) {
            final Integer idPaciente;
            final Integer idInformacionPaciente;

            if (tipoProfesional == TIPO_PROFESIONAL_MEDICO) {
                consultaMedicina = dbUtil.obtenerConsultaMedicina(getArguments().getInt(ARG_ID_CONSULTA));
                if(getArguments().getInt(ARG_ID_CONTROL) > 0) {
                    controlMedico = dbUtil.getControlMedicoServidor(String.valueOf(getArguments().getInt(ARG_ID_CONTROL)));
                }
                consultaMedicina.setControlesMedicos(dbUtil.obtenerControlesMedicosConsultaBD(consultaMedicina.getIdConsulta()));
                consultaMedicina.setRequerimientos(dbUtil.obtenerRequerimientos(DBUtil.TipoConsulta.CONSULTA, consultaMedicina.getIdConsulta()));
                idPaciente = consultaMedicina.getIdPaciente();
                idInformacionPaciente = consultaMedicina.getIdInformacionPaciente();
            } else if (tipoProfesional == TIPO_PROFESIONAL_ENFERMERA) {
                consultaEnfermeria = dbUtil.obtenerConsultaEnfermeria(getArguments().getInt(ARG_ID_CONSULTA));
                consultaEnfermeria.setControlesEnfermeria(dbUtil.obtenerControlesEnfermeriaConsultaBD(consultaEnfermeria.getIdConsulta()));
                consultaEnfermeria.setRequerimientos(dbUtil.obtenerRequerimientos(DBUtil.TipoConsulta.CONSULTA, consultaEnfermeria.getIdConsulta()));
                idPaciente = consultaEnfermeria.getIdPaciente();
                idInformacionPaciente = consultaEnfermeria.getIdInformacionPaciente();
            } else {
                idPaciente = -1;
                idInformacionPaciente = -1;
            }

            paciente = dbUtil.obtenerPaciente(idPaciente);
            paciente.setInformacionPaciente(dbUtil.obtenerInformacionPacienteBD(idInformacionPaciente));
        }

        return rootView;
    }

}
