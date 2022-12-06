package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 6/14/2018.
 */

public class ResponseConsulta extends BaseResponse {

    @SerializedName("authentication_token")
    @Expose
    public String authentication_token;

    @SerializedName("consultant")
    @Expose
    public Consulta consulta;

    @SerializedName("medical_consultant")
    @Expose
    public ConsultaMedica consultaMedica;

}
