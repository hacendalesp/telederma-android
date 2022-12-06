package com.telederma.gov.co.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telederma.gov.co.modelo.Credencial;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponseCredential extends BaseResponse {

    @SerializedName("constant")
    @Expose
    public Credencial credencial;


}
