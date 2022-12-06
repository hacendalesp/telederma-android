package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlMedico;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 7/25/2018.
 */
public class ControlRequest extends BaseRequest {

    @SerializedName("consultation_control")
    @Expose
    public ControlConsulta control;

    @SerializedName("medical_control")
    @Expose
    public ControlMedico control_medico;

    public ControlRequest(ControlMedico control_medico,ControlConsulta control_consulta,  String user_token, String user_email) {
        super(user_token, user_email);
        this.control = control_consulta;
        this.control_medico = control_medico;
    }

}
