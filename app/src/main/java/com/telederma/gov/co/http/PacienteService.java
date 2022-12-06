package com.telederma.gov.co.http;

import com.telederma.gov.co.http.response.ResponsePaciente;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.http.request.PacienteRequest;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sebastian noreña marquez on 6/14/2018.
 */

public interface PacienteService {

    @POST("api/v1/patients.json")
    Observable<Response<ResponsePaciente>> registrar(
            @Body PacienteRequest pacienteRequest
    );


    @GET("api/v1/patients/{document}.json")
    Observable<Response<ResponsePacienteBusqueda>> buscar(
            @Path("document") String document,
            @Query("user_email") String user_email,
            @Query("user_token") String user_token
    );


}



