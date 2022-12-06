package com.telederma.gov.co.http;

import com.telederma.gov.co.http.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Daniel Hernández on 11/08/2018.
 */

public interface AprendeService {

    @GET("api/v1/sessions/certificated.json")
    Observable<Response<BaseResponse>> enviarCertificacion(
            @Query("doctor_id") Integer idDoctor,
            @Query("certified") Boolean certified,
            @Query("user_email") String userEmail,
            @Query("user_token") String userToken
    );

}
