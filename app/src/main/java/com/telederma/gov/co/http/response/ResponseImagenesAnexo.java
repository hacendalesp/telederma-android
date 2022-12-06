package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.ImagenAnexo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponseImagenesAnexo extends BaseResponse {

    @SerializedName("annex")
    @Expose
    public ImagenAnexo imagenAnexo;

}
