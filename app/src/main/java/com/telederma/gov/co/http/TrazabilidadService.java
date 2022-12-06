package com.telederma.gov.co.http;

import com.telederma.gov.co.http.request.TrazabilidadRequest;
import com.telederma.gov.co.http.request.UsuarioRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseLogin;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Daniel Hernández on 11/08/2018.
 */

public interface TrazabilidadService {

    @POST("api/v1/consultations/traceability.json")
    Observable<Response<BaseResponse>> enviarTrazabilidad(
            @Body TrazabilidadRequest trazabilidadRequest
    );

}
