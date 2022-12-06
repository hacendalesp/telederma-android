package com.telederma.gov.co.http;
import com.telederma.gov.co.http.request.SolicitudAyudaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseHelpDesk;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.modelo.HelpDeskRes;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Daniel Hernández on 11/08/2018.
 */

public interface MesaAyudaService {

    @POST("api/v1/managements.json")
    Observable<Response<ResponseHelpDesk>> enviarSolicitudAyuda(
            @Body SolicitudAyudaRequest request
    );

    @GET("api/v1/managements.json")
    Observable<Response<List<HelpDesk>>> getTickets(
            @Query("user_email") String userEmail,
            @Query("user_token") String userToken
    );



}
