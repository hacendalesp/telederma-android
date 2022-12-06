package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.TrazabilidadService;
import com.telederma.gov.co.http.request.TrazabilidadRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.telederma.gov.co.utils.Session;

import io.reactivex.Observable;
import retrofit2.Response;

@DatabaseTable(tableName = "trazabilidad")
public class Trazabilidad extends BaseEntity implements ISincronizable {


    public static final String CAMPO_CONSULTATION = "consultation_id";
    public static final String CAMPO_CONTROL = "consultation_control_id";
    public static final String CAMPO_TIPO = "tipo";
    public static final String NOMBRE_TABLA = "trazabilidad";


    @DatabaseField(columnName = CAMPO_CONSULTATION)
    @SerializedName(CAMPO_CONSULTATION)
    @Expose
    private Integer consultation_id;

    @DatabaseField(columnName = CAMPO_CONTROL)
    @SerializedName(CAMPO_CONTROL)
    @Expose
    private Integer control_id;


    @DatabaseField(columnName = CAMPO_TIPO)
    @SerializedName(CAMPO_TIPO)
    @Expose
    private Boolean tipo;

    public Integer getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(Integer consultation_id) {
        this.consultation_id = consultation_id;
    }

    public Integer getControl_id() {
        return control_id;
    }

    public void setControl_id(Integer control_id) {
        this.control_id = control_id;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }



    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {

        TrazabilidadService trazabilidadServiceService = (TrazabilidadService) HttpUtils.crearServicio(TrazabilidadService.class);
        Observable deviceObservable = trazabilidadServiceService.enviarTrazabilidad(new TrazabilidadRequest(this.getConsultation_id(),this.getControl_id(),false, token, email));
        return deviceObservable;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        DataSincronizacion data = new DataSincronizacion();
        if (response.code() == 200) {
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable != null){
            throwable.toString();
        }
        //new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }
}
