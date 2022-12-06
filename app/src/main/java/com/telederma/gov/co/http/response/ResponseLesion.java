package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.Lesion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponseLesion extends BaseResponse {

    @SerializedName("injury")
    @Expose
    public Lesion lesion;

    @SerializedName("images")
    @Expose
    public List<ImagenLesion> imagenes;

}
