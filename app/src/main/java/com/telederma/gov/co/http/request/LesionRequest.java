package com.telederma.gov.co.http.request;

import com.telederma.gov.co.modelo.ImagenDermatoscopia;
import com.telederma.gov.co.modelo.Lesion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian Noreña Marquez 6/22/2018.
 */
public class LesionRequest extends BaseRequest {

    @SerializedName("injury")
    @Expose
    public Lesion lesion;

    @SerializedName("images")
    @Expose
    public ImagenDermatoscopia[] images;

    @SerializedName("tipo")
    @Expose
    public Integer tipo;


    public LesionRequest(Lesion lesion, ImagenDermatoscopia[] images, Integer tipo, String user_token, String user_email) {
        super(user_token, user_email);
        this.lesion = lesion;
        this.images = images;
        this.tipo = tipo;
    }

}


