package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.Usuario;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 6/22/2018.
 */

public class UsuarioRequest {

    @SerializedName("user")
    @Expose
    public Usuario usuario;


    public UsuarioRequest(Usuario usuario) {
        this.usuario = usuario;
    }
}
