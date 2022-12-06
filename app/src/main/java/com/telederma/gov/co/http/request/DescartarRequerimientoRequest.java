package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.DescartarRequerimiento;
import com.telederma.gov.co.modelo.Requerimiento;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Perez 27/03/2019.
 */
public class DescartarRequerimientoRequest extends BaseRequest {

//    @SerializedName("request")
//    @Expose
//    public Requerimiento requerimiento;

    // nombre del nodo principal para entregar los datos en el ws
    @SerializedName("request")
    @Expose
    public DescartarRequerimiento descartarRequerimiento;


    public DescartarRequerimientoRequest(DescartarRequerimiento descartarRequerimiento, String user_token, String user_email) {
        super(user_token, user_email);
        this.descartarRequerimiento = descartarRequerimiento;
    }

}


