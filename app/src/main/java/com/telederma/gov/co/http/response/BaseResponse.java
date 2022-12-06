package com.telederma.gov.co.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 28/07/2018.
 */

public class BaseResponse {

    @SerializedName("message")
    @Expose
    public String message;

}
