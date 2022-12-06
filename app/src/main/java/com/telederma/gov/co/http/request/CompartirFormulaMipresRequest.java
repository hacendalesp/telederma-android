package com.telederma.gov.co.http.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public class CompartirFormulaMipresRequest extends BaseRequest {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("consultation_id")
    @Expose
    public Integer idConsulta;

    @SerializedName("tipo")
    @Expose
    public Integer tipo;

    public CompartirFormulaMipresRequest(String user_token, String user_email, Integer id, Integer idConsulta, Integer tipo) {
        super(user_token, user_email);

        this.id = id;
        this.idConsulta = idConsulta;
        this.tipo = tipo;
    }
}
