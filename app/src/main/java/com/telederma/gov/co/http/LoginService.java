package com.telederma.gov.co.http;

import com.telederma.gov.co.http.request.EditarUsuarioRequest;
import com.telederma.gov.co.http.request.TutorialRequest;
import com.telederma.gov.co.http.request.UsuarioRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseCredential;
import com.telederma.gov.co.http.response.ResponseLogin;
import com.telederma.gov.co.http.response.ResponseParametros;
import com.telederma.gov.co.http.response.ResponseUpdate;
import com.telederma.gov.co.modelo.Aseguradora;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.Departamento;
import com.telederma.gov.co.modelo.ParteCuerpo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Servicio web para las funcionalidades del login
 * <p>
 * Created by Daniel Hernández on 6/14/2018.
 */
public interface LoginService {

    @FormUrlEncoded
    @POST("api/v1/sessions/login.json")
    Observable<Response<ResponseLogin>> iniciarSesion(
            @Field("number_document") String document,
            @Field("password") String password
    );

    @POST("api/v1/sessions/register.json")
    Observable<Response<ResponseLogin>> registrarUsuario(
            @Body UsuarioRequest usuarioRequestalert
    );

    @PUT("api/v1/sessions/tutorial.json")
    Observable<Response<BaseResponse>> marcarTutorial(@Body TutorialRequest tutorial);

    //@PUT("api/v1/sessions/{id}.json")
    @PUT("api/v1/sessions/update.json")
    Observable<Response<ResponseUpdate>> editarUsuario(@Body EditarUsuarioRequest usuarioRequest );

    @FormUrlEncoded
    @POST("api/v1/sessions/forget_password.json")
    Observable<Response<BaseResponse>> recuperarContrasena(
            @Field("number_document") String document
    );

    @GET("api/v1/static_resources/system_constants.json")
    Observable<Response<ResponseParametros>> consultarConstantes();

    @GET("api/v1/static_resources/departments.json")
    Observable<Response<List<Departamento>>> consultarDepartamentos();

    @GET("api/v1/static_resources/diseases.json")
    Observable<Response<List<Cie10>>> consultarCie10();

    @GET("api/v1/static_resources/all_insurances.json")
    Observable<Response<List<Aseguradora>>> consultarAseguradoras();

    @GET("api/v1/static_resources/body_areas.json")
    Observable<Response<List<ParteCuerpo>>> consultarPartesCuerpo();

    @GET("api/v1/static_resources/access.json")
    Observable<Response<ResponseCredential>> consultarCredenciales();

    @GET("api/v1/static_resources/device.json")
    Observable<Response<BaseResponse>> consultarDevice();

}
