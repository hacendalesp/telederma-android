package com.telederma.gov.co.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña
 */

public class ResponseCredito extends BaseResponse {

    @SerializedName("total")
    @Expose
    public Double total ;

    @SerializedName("consumidos")
    @Expose
    public Double consumidos;

}
