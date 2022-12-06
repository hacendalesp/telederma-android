package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 6/22/2018.
 */
public class ConsultaRequest extends BaseRequest {

    @SerializedName("consultation")
    @Expose
    public Consulta consulta;

    @SerializedName("medical_consultation")
    @Expose
    public ConsultaMedica consulta_medica;


    public ConsultaRequest(ConsultaMedica consulta_medica, Consulta consulta, String user_token, String user_email) {
        super(user_token, user_email);
        this.consulta_medica = consulta_medica;
        this.consulta = consulta;
    }

}


