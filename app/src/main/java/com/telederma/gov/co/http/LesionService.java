package com.telederma.gov.co.http;

import com.telederma.gov.co.http.response.ResponseLesion;
import com.telederma.gov.co.http.request.LesionRequest;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Sebastian noreña marquez on 6/28/2018.
 */

public interface LesionService {

    @POST("api/v1/injuries.json")
    Observable<Response<ResponseLesion>> registrar(
            @Body LesionRequest lesionRequest
    );

}



