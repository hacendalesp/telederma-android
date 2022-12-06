package com.telederma.gov.co.http.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel Hernández on 28/07/2018.
 */

public class BaseRequest {

    @SerializedName("user_token")
    @Expose
    public String user_token;

    @SerializedName("user_email")
    @Expose
    public String user_email;

    public BaseRequest(String user_token, String user_email) {
        this.user_token = user_token;
        this.user_email = user_email;
    }

}
