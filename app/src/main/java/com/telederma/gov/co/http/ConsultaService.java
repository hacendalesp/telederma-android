package com.telederma.gov.co.http;

import com.telederma.gov.co.http.request.ArchivarConsultaRequest;
import com.telederma.gov.co.http.request.CompartirConsultaRequest;
import com.telederma.gov.co.http.request.CompartirFormulaMipresRequest;
import com.telederma.gov.co.http.request.ConsultaEnfermeriaRequest;
import com.telederma.gov.co.http.request.ConsultaRequest;
import com.telederma.gov.co.http.request.ImagenesAnexoRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseConsulta;
import com.telederma.gov.co.http.response.ResponseConsultaEnfermeria;
import com.telederma.gov.co.http.response.ResponseCredito;
import com.telederma.gov.co.http.response.ResponseImagenesAnexo;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.http.response.ResponseRespuestaEspecialista;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Sebastian noreña marquez on 6/14/2018.
 */

public interface ConsultaService {

    Integer ARCHIVAR = 1, DESARCHIVAR = 2, FORMULA = 1, MIPRES = 2;

    @POST("api/v1/consultations.json")
    Observable<Response<ResponseConsulta>> registrar(
            @Body ConsultaRequest consultaRequest
    );

    @PUT("api/v1/consultations/update.json")
    Observable<Response<ResponseImagenesAnexo>> registrar_imagenes_anexo(
            @Body ImagenesAnexoRequest imagenesAnexoRequest
    );

    @GET("api/v1/consultations/all_information.json")//@GET("api/v1/consultations.json")
    //@GET("api/v1/consultations.json")//@GET("api/v1/consultations.json")
    Observable<Response<List<ResponsePacienteBusqueda>>> consultarConsultas(
            @Query("status[]") Integer[] status,
            @Query("number_document") String numberDocument,
            @Query("user_email") String userEmail,
            @Query("user_token") String userToken
    );

    @POST("api/v1/consultations/shared_consultations.json")
    Observable<Response<BaseResponse>> compartirConsultas(
            @Body CompartirConsultaRequest compartirConsultaRequest
    );

    @GET("api/v1/especialista/consult/get_info_specialist.json")
    Observable<Response<List<ResponseRespuestaEspecialista>>> consultarRespuestaEspecialista(
            @Query("consultation_id") String numberDocument,
            @Query("user_email") String userEmail,
            @Query("user_token") String userToken
    );

    @POST("api/v1/consultations/archived_consultation.json")
    Observable<Response<BaseResponse>> archivarConsulta(
            @Body ArchivarConsultaRequest compartirConsultaRequest
    );


    @GET("api/v1/especialista/consult/client_credits.json")
    Observable<Response<ResponseCredito>> consultarCreditos(
            @Query("user_email") String userEmail,
            @Query("user_token") String userToken
    );


    @POST("api/v1/consultations/shared_formula_mipres.json")
    Observable<Response<BaseResponse>> compartirFormlaMipres(
            @Body CompartirFormulaMipresRequest compartirFormulaMipresRequest
    );

    @POST("api/v1/nurse_consultations.json")
    Observable<Response<ResponseConsultaEnfermeria>> registrarEnfermeria(
            @Body ConsultaEnfermeriaRequest consultaRequest
    );

    @GET("api/v1/consultations/show_information.json")
    Observable<Response<ResponsePacienteBusqueda>> getConsulta(
            @Query("consultation_id") String consultation_id,
            @Query("consultation_control_id") String consultation_control_id,
            @Query("user_email") String userEmail,
            @Query("user_token") String userToken
    );



}
