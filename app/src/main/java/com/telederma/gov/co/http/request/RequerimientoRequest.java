package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.Requerimiento;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 6/22/2018.
 */
public class RequerimientoRequest extends BaseRequest {

    @SerializedName("request")
    @Expose
    public Requerimiento requerimiento;

    public RequerimientoRequest(Requerimiento _requerimiento, String user_token, String user_email) {
        super(user_token, user_email);
        this.requerimiento = _requerimiento;
    }

}


