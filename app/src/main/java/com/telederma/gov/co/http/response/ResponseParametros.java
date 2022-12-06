package com.telederma.gov.co.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Hernández on 6/25/2018.
 */

public class ResponseParametros extends BaseResponse {

    @SerializedName("constants")
    @Expose
    public List<Parametro> listaParametros;

    public class Parametro {

        @SerializedName("type")
        @Expose
        public String type;

        @SerializedName("values")
        @Expose
        public Map<String, Integer> values;

    }

}
