package com.telederma.gov.co.http;

import com.telederma.gov.co.BuildConfig;
import com.telederma.gov.co.http.response.ResponseImage;
import com.telederma.gov.co.utils.Constantes;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Daniel Hernández on 15/08/2018.
 */

public interface ImagenesService {
    @Multipart
    @Headers({
            "Authorization: Client-ID " + BuildConfig.MY_IMGUR_CLIENT_ID
    })
    @POST("image")
    Observable<Response<ResponseImage>> postImagen(
            @Query("title") String title,
            @Query("description") String description,
            @Query("album") String albumId,
            @Query("account_url") String username,
            @Part MultipartBody.Part file
    );

    @Multipart
    @Headers({
            "Authorization: Client-ID " + BuildConfig.MY_IMGUR_CLIENT_ID
    })
    @POST("image")
    Observable<Response<ResponseImage>> postAudio(
            @Query("title") String title,
            @Query("description") String description,
            @Query("album") String albumId,
            @Query("account_url") String username,
            @Part MultipartBody.Part file
    );

}
