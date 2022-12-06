package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Paciente;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponsePaciente extends BaseResponse {

    @SerializedName("patient_information")
    @Expose
    public InformacionPaciente informacion_paciente;

    @SerializedName("patient")
    @Expose
    public Paciente paciente;


}
