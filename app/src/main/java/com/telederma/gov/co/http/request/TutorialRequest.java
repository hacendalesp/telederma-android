package com.telederma.gov.co.http.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TutorialRequest extends BaseRequest {

    public TutorialRequest(String user_token, String user_email) {
        super(user_token, user_email);
    }

}
