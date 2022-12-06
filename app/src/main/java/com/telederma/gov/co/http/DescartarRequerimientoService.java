package com.telederma.gov.co.http;

import com.telederma.gov.co.http.request.DescartarRequerimientoRequest;
import com.telederma.gov.co.http.request.RequerimientoRequest;
import com.telederma.gov.co.http.response.ResponseRequerimiento;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Sebastian perez  27/03/2019.
 */

public interface DescartarRequerimientoService {

    //Todo: Sebas - pasar id para put
    // descartar requerimiento
    //ojo el id que se esta enviando en la ruta pertenece al registro del request
    @PUT("api/v1/consultations/{id}/reject_request.json")
    Observable<Response<ResponseRequerimiento>> registrar(
            @Path("id") String id,
            @Body DescartarRequerimientoRequest descartarRequerimientoRequest
    );
}



