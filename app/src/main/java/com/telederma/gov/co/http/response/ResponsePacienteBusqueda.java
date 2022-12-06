package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Paciente;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponsePacienteBusqueda extends BaseResponse {

    @SerializedName("consultants")
    @Expose
    public List<Consulta> consultas;

    @SerializedName("patient_information")
    @Expose
    public InformacionPaciente informacion_paciente;

    @SerializedName("patient")
    @Expose
    public Paciente paciente;

}
