package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez
 */

public class ResponseConsultaEnfermeria extends BaseResponse {

    @SerializedName("authentication_token")
    @Expose
    public String authentication_token;

    @SerializedName("consultant")
    @Expose
    public Consulta consulta;

    @SerializedName("nurse_consulta")
    @Expose
    public ConsultaEnfermeria consultaEnfermeria;

}
