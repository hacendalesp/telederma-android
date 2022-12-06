package com.telederma.gov.co.http.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Daniel Hernández on 28/07/2018.
 */

public class CompartirConsultaRequest extends BaseRequest {

    @SerializedName("consult_ids")
    @Expose
    public List<String> consult_ids;

    public CompartirConsultaRequest(String user_token, String user_email, List<String> consult_ids) {
        super(user_token, user_email);

        this.consult_ids = consult_ids;
    }

}
