package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.HelpDesk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class SolicitudAyudaRequest extends BaseRequest {

    @SerializedName("help_desk")
    @Expose
    HelpDesk helpDesk;

    public SolicitudAyudaRequest(String user_token, String user_email, HelpDesk helpDesk) {
        super(user_token, user_email);
        this.helpDesk = helpDesk;
    }



}
