package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Paciente;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 6/22/2018.
 */
public class PacienteRequest extends BaseRequest {

    @SerializedName("patient")
    @Expose
    public Paciente paciente;

    @SerializedName("patient_information")
    @Expose
    public InformacionPaciente informacionPaciente;

    public PacienteRequest(Paciente paciente, InformacionPaciente informacionPaciente,
                           String user_token, String user_email) {
        super(user_token, user_email);
        this.paciente = paciente;
        this.informacionPaciente = informacionPaciente;
        this.user_token = user_token;
        this.user_email = user_email;
    }

}
