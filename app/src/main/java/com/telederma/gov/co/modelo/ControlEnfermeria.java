package com.telederma.gov.co.modelo;

import com.telederma.gov.co.http.ControlService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.ControlEnfermeriaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseControlEnfermeria;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.ControlEnfermeria.NOMBRE_TABLA;

/**
 * Created by Daniel Hernández on 7/6/2018.
 */

@DatabaseTable(tableName = NOMBRE_TABLA)
public class ControlEnfermeria extends ControlConsulta implements ISincronizable {

    public static final String NOMBRE_TABLA = "control_enfermeria";
    public static final String NOMBRE_CAMPO_MEJORA_SUBJETIVA = "subjetive_improvement";
    public static final String NOMBRE_CAMPO_TAMANO_ULCERA_LARGO = "ulcer_length";
    public static final String NOMBRE_CAMPO_TAMANO_ULCERA_ANCHO = "ulcer_width";
    public static final String NOMBRE_CAMPO_INTENSIDAD_DOLOR = "pain_intensity";
    public static final String NOMBRE_CAMPO_TOLERA_TRATAMIENTO = "tolerated_treatment";
    public static final String NOMBRE_CAMPO_CONCEPTO_ENFERMERIA_MEJORIA = "improvement";
    public static final String NOMBRE_CAMPO_ID_PATIENT_LOCAL = "id_patient_local";
    public static final String NOMBRE_CAMPO_ID_CONTROL_CONSULTA = "consultation_control_id";
    public static final String NOMBRE_CAMPO_COMENTARIOS = "commentation";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_PATIENT_LOCAL)
    private Integer id_patient_local;

    @DatabaseField( columnName = NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @Expose
    private Integer idControlConsulta;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_MEJORA_SUBJETIVA)
    @SerializedName(NOMBRE_CAMPO_MEJORA_SUBJETIVA)
    @Expose
    private String mejoraSubjetiva;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TAMANO_ULCERA_LARGO)
    @SerializedName(NOMBRE_CAMPO_TAMANO_ULCERA_LARGO)
    @Expose
    private Float tamanoUlceraLargo;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TAMANO_ULCERA_ANCHO)
    @SerializedName(NOMBRE_CAMPO_TAMANO_ULCERA_ANCHO)
    @Expose
    private Float tamanoUlceraAncho;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_INTENSIDAD_DOLOR)
    @SerializedName(NOMBRE_CAMPO_INTENSIDAD_DOLOR)
    @Expose
    private Integer intensidadDolor;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TOLERA_TRATAMIENTO)
    @SerializedName(NOMBRE_CAMPO_TOLERA_TRATAMIENTO)
    @Expose
    private Boolean toleraTratamiento;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_CONCEPTO_ENFERMERIA_MEJORIA)
    @SerializedName(NOMBRE_CAMPO_CONCEPTO_ENFERMERIA_MEJORIA)
    @Expose
    private Boolean conceptoEnfermeriaMejoria;

    @DatabaseField(columnName = NOMBRE_CAMPO_COMENTARIOS)
    @SerializedName(NOMBRE_CAMPO_COMENTARIOS)
    @Expose
    private String comentarios;

    public ControlEnfermeria() {
    }

    public Integer getId_patient_local() {
        return id_patient_local;
    }

    public void setId_patient_local(Integer id_patient_local) {
        this.id_patient_local = id_patient_local;
    }

    public String getMejoraSubjetiva() {
        return mejoraSubjetiva;
    }

    public void setMejoraSubjetiva(String mejoraSubjetiva) {
        this.mejoraSubjetiva = mejoraSubjetiva;
    }

    public Float getTamanoUlceraLargo() {
        return tamanoUlceraLargo;
    }

    public void setTamanoUlceraLargo(Float tamanoUlceraLargo) {
        this.tamanoUlceraLargo = tamanoUlceraLargo;
    }

    public Float getTamanoUlceraAncho() {
        return tamanoUlceraAncho;
    }

    public void setTamanoUlceraAncho(Float tamanoUlceraAncho) {
        this.tamanoUlceraAncho = tamanoUlceraAncho;
    }

    public Integer getIntensidadDolor() {
        return intensidadDolor;
    }

    public void setIntensidadDolor(Integer intensidadDolor) {
        this.intensidadDolor = intensidadDolor;
    }

    public Boolean getToleraTratamiento() {
        return toleraTratamiento;
    }

    public void setToleraTratamiento(Boolean toleraTratamiento) {
        this.toleraTratamiento = toleraTratamiento;
    }

    public Boolean getConceptoEnfermeriaMejoria() {
        return conceptoEnfermeriaMejoria;
    }

    public void setConceptoEnfermeriaMejoria(Boolean conceptoEnfermeriaMejoria) {
        this.conceptoEnfermeriaMejoria = conceptoEnfermeriaMejoria;
    }

    public Integer getIdControlConsulta() {
        return idControlConsulta;
    }

    public void setIdControlConsulta(Integer idControlConsulta) {
        this.idControlConsulta = idControlConsulta;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }


    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        ControlService service = (ControlService) HttpUtils.crearServicio(ControlService.class);
        ControlConsulta control_consulta = new ControlConsulta();
        control_consulta.setIdConsulta(this.getIdConsulta());
        Observable lesionObservable = service.registrarControlEnfermeria(new ControlEnfermeriaRequest(this,control_consulta, token, email));
        return lesionObservable;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.actualizarCampo(NOMBRE_TABLA, NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseControlEnfermeria) response.body()).controlEnfermeria.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampo(NOMBRE_TABLA, NOMBRE_CAMPO_ID_CONTROL_CONSULTA, ((ResponseControlEnfermeria) response.body()).controlEnfermeria.getIdControlConsulta().toString(), this.getId().toString(), "id");
            data.actualizarCampo(Lesion.NOMBRE_TABLA, Lesion.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, ((ResponseControlEnfermeria) response.body()).controlConsulta.getIdServidor().toString(), this.getId().toString(), Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
            data.actualizarCampo(ImagenAnexo.NOMBRE_TABLA, ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, ((ResponseControlEnfermeria) response.body()).controlConsulta.getIdServidor().toString(), this.getId().toString(), ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        //new DataSincronizacion().actualizarArchivo(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }




}
