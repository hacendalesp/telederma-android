package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.HelpDesk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseHelpDesk extends BaseResponse {

    @SerializedName("desk")
    @Expose
    public HelpDesk help_desk;
}
