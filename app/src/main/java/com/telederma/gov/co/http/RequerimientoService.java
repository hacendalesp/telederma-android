package com.telederma.gov.co.http;

import com.telederma.gov.co.http.request.PacienteRequest;
import com.telederma.gov.co.http.request.RequerimientoRequest;
import com.telederma.gov.co.http.response.ResponsePaciente;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.http.response.ResponseRequerimiento;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sebastian noreña marquez on 6/14/2018.
 */

public interface RequerimientoService {

    // TODO: 5/11/19 cambiar metodo por put
    // responder requerimiento
    //ojo el id que se esta enviando en la ruta pertenece al registro del request
    @PUT("api/v1/consultations/{id}/response_request.json")
    Observable<Response<ResponseRequerimiento>> registrar(
            @Path("id") String id,
            @Body RequerimientoRequest requerimientoRequest
    );
}



