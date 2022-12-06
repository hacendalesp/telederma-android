package com.telederma.gov.co.modelo;

import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.DescartarRequerimientoService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.RequerimientoService;
import com.telederma.gov.co.http.request.DescartarRequerimientoRequest;
import com.telederma.gov.co.http.request.RequerimientoRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.DescartarRequerimiento.NOMBRE_TABLA;

/**
 * Created by Daniel Hernández on 30/07/2018.
 */

@DatabaseTable(tableName = NOMBRE_TABLA)
public class DescartarRequerimiento extends BaseEntity implements ISincronizable {

    public static final String NOMBRE_TABLA = "descartar_requerimiento";
    public static final String NOMBRE_CAMPO_ID_REQUERIMIENTO = "requerimiento_id";
    public static final String NOMBRE_CAMPO_REASON = "reason";
    public static final String NOMBRE_CAMPO_OTHER_REASON = "other_reason";


    // transient hace que no se envie el parametro en el ws
    //private transient Integer idRequerimiento;
    @DatabaseField(columnName = NOMBRE_CAMPO_ID_REQUERIMIENTO)
    @SerializedName(NOMBRE_CAMPO_ID_REQUERIMIENTO)
    @Expose
    private transient Integer idRequerimiento;


    @DatabaseField(columnName = NOMBRE_CAMPO_REASON)
    @SerializedName(NOMBRE_CAMPO_REASON)
    @Expose
    private Integer reason;

    @DatabaseField(columnName = NOMBRE_CAMPO_OTHER_REASON)
    @SerializedName(NOMBRE_CAMPO_OTHER_REASON)
    @Expose
    private String otherReason;


    public DescartarRequerimiento() {
    }


    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public Integer getIdRequerimiento() {
        return idRequerimiento;
    }

    public void setIdRequerimiento(Integer idRequerimiento) {
        this.idRequerimiento = idRequerimiento;
    }


    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        DescartarRequerimientoService service = (DescartarRequerimientoService) HttpUtils.crearServicio(DescartarRequerimientoService.class);
        //sobreescribe el id para que envie al servidor el id perteneciente al requerimiento
        DescartarRequerimiento dr = this;
        dr.setId(this.getIdRequerimiento());
        Observable observable = service.registrar(String.valueOf(this.getIdServidor()), new DescartarRequerimientoRequest(dr, token, email));
        return observable;
    }






    @Override
    public <T> void nextAction(Response<T> response) {

        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.eliminarPendiente(this.getIdServidor(),NOMBRE_TABLA);
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable != null){
            throwable.toString();
        }
        new DataSincronizacion().actualizarArchivo(this.getIdServidor(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }
}
