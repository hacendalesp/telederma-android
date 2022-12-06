package com.telederma.gov.co.interfaces;


import com.telederma.gov.co.http.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.Response;

public interface ISincronizable {
    <T> Observable<Response<T>> getObservable(String token, String email);

    <T> void nextAction(Response<T> response);

     void  procesarExcepcionServicio(Throwable throwable);
}
