package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Requerimiento;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponseRequerimiento extends BaseResponse {

    @SerializedName("request")
    @Expose
    public Requerimiento requerimiento;


}
