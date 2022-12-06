package com.telederma.gov.co.http.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 31/07/2018.
 */

public class ArchivarConsultaRequest extends BaseRequest {

    @SerializedName("id")
    @Expose
    public Integer idConsulta;

    @SerializedName("tipo")
    @Expose
    public Integer tipo;

    public ArchivarConsultaRequest(String user_token, String user_email, Integer idConsulta, Integer tipo) {
        super(user_token, user_email);
        this.idConsulta = idConsulta;
        this.tipo = tipo;
    }

}
