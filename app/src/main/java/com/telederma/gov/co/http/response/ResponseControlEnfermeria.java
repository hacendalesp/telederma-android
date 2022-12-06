package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponseControlEnfermeria extends BaseResponse {

    @SerializedName("consulta_control")
    @Expose
    public ControlConsulta controlConsulta;

    @SerializedName("control_enfermera")
    @Expose
    public ControlEnfermeria controlEnfermeria;

}
