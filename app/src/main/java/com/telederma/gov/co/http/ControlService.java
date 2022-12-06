package com.telederma.gov.co.http;

import com.telederma.gov.co.http.request.ControlEnfermeriaRequest;
import com.telederma.gov.co.http.request.ControlRequest;
import com.telederma.gov.co.http.response.ResponseControl;
import com.telederma.gov.co.http.response.ResponseControlEnfermeria;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sebastian noreña marquez on 6/28/2018.
 */

public interface ControlService {

    @POST("api/v1/medical_controls.json")
    Observable<Response<ResponseControl>> registrar(
            @Body ControlRequest controlRequest
    );


    @POST("api/v1/nurse_controls.json")
    Observable<Response<ResponseControlEnfermeria>> registrarControlEnfermeria(
            @Body ControlEnfermeriaRequest controlRequest
    );


}



