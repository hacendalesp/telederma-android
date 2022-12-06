package com.telederma.gov.co.http.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telederma.gov.co.modelo.Lesion;

/**
 * Created by Sebastian Noreña Marquez 6/22/2018.
 */
public class TrazabilidadRequest extends BaseRequest {
    @SerializedName("consultation_id")
    @Expose
    public Integer consultation_id;

    @SerializedName("consultation_control_id")
    @Expose
    public Integer consultation_control_id;

    @SerializedName("tipo")
    @Expose
    public Boolean tipo;


    public TrazabilidadRequest(Integer consultation_id,Integer consultation_control_id,Boolean tipo, String user_token, String user_email) {
        super(user_token, user_email);
        this.consultation_id = consultation_id;
        this.consultation_control_id = consultation_control_id;
        this.tipo = tipo;
    }

}


