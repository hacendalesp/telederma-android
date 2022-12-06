package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.Usuario;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUpdate extends BaseResponse {
    @SerializedName("user")
    @Expose
    public Usuario user;

}
