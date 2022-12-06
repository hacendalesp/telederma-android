package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel Hernández on 6/22/2018.
 */

public class EditarUsuarioRequest extends BaseRequest {

    @SerializedName("user")
    @Expose
    public Usuario usuario;

//    @SerializedName("id")
//    @Expose
//    public String id;

    public EditarUsuarioRequest(String user_token, String user_email, Usuario usuario) {
        super(user_token, user_email);
       /* this.id = id;
        Object obj_user = new Object();
        Gson gson = new Gson();

        JSONObject json_user = new JSONObject();

        try {
            JSONObject json_user = new JSONObject(gson.toJson(usuario));
            json_user.remove("contrasena");
            obj_user = gson.fromJson(String.valueOf(json_user), Usuario.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        this.usuario = usuario;
    }


}
