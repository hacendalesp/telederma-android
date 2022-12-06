package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.Lesion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 6/22/2018.
 */
public class ImagenesAnexoRequest extends BaseRequest {

    @SerializedName("consultation_id")
    @Expose
    public String consultation_id;

    @SerializedName("consultation_control_id")
    @Expose
    public String consultation_control_id;

    @SerializedName("images_annex")
    @Expose
    public String[] images_annex;


    public ImagenesAnexoRequest(String consultation_id, String consultation_control_id, String[] images, String user_token, String user_email) {
        super(user_token, user_email);
        this.consultation_id = consultation_id;
        this.consultation_control_id = consultation_control_id;
        this.images_annex = images;
    }

}


