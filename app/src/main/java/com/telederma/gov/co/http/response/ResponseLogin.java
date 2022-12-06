package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.Usuario;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 6/14/2018.
 */
public class ResponseLogin extends BaseResponse {

    @SerializedName("authentication_token")
    @Expose
    public String authentication_token;

    @SerializedName("user")
    @Expose
    public Usuario user;

}
