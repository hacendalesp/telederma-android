package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 7/25/2018.
 */
public class ControlEnfermeriaRequest extends BaseRequest {


    @SerializedName("consultation_control")
    @Expose
    public ControlConsulta control;

    @SerializedName("nurse_control")
    @Expose
    public ControlEnfermeria control_enfermeria;

    public ControlEnfermeriaRequest(ControlEnfermeria control_enfermeria, ControlConsulta control_consulta, String user_token, String user_email) {
        super(user_token, user_email);
        this.control = control_consulta;
        this.control_enfermeria = control_enfermeria;
    }

}
