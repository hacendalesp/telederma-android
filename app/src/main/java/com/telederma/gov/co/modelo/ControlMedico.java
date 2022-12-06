package com.telederma.gov.co.modelo;

import android.util.Log;

import com.telederma.gov.co.http.ControlService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.ControlRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseControl;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Daniel Hernández on 7/6/2018.
 */

@DatabaseTable(tableName = "control_medico")
public class ControlMedico extends ControlConsulta implements ISincronizable {

    public static final String NOMBRE_TABLA = "control_medico";
    public static final String NOMBRE_CAMPO_MEJORA_SUBJETIVA = "subjetive_improvement";
    public static final String NOMBRE_CAMPO_TUVO_TRATAMIENTO = "did_treatment";
    public static final String NOMBRE_CAMPO_TOLERACION_MEDICAMENTOS = "tolerated_medications";
    public static final String NOMBRE_CAMPO_AUDIO_CLINICA = "audio_clinic";
    public static final String NOMBRE_CAMPO_DESCRIPCION_CLINICA = "clinic_description";
    public static final String NOMBRE_CAMPO_AUDIO_ANEXO = "audio_annex";
    public static final String NOMBRE_CAMPO_DESCRIPCION_ANEXO = "annex_description";
    public static final String NOMBRE_CAMPO_ID_PATIENT_LOCAL = "id_patient_local";
    public static final String NOMBRE_CAMPO_ID_CONTROL_CONSULTA = "consultation_control_id";


    @DatabaseField(columnName = NOMBRE_CAMPO_ID_PATIENT_LOCAL)
    private Integer idPatientLocal;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_MEJORA_SUBJETIVA)
    @SerializedName(NOMBRE_CAMPO_MEJORA_SUBJETIVA)
    @Expose
    private String mejoraSubjetiva;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TUVO_TRATAMIENTO)
    @SerializedName(NOMBRE_CAMPO_TUVO_TRATAMIENTO)
    @Expose
    private Boolean tuvoTratamiento;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TOLERACION_MEDICAMENTOS)
    @SerializedName(NOMBRE_CAMPO_TOLERACION_MEDICAMENTOS)
    @Expose
    private Integer toleracionMedicacion;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @Expose
    private Integer idControlConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_AUDIO_CLINICA)
    @SerializedName(NOMBRE_CAMPO_AUDIO_CLINICA)
    @Expose
    private String audioClinica;

    @DatabaseField(columnName = NOMBRE_CAMPO_DESCRIPCION_CLINICA)
    @SerializedName(NOMBRE_CAMPO_DESCRIPCION_CLINICA)
    @Expose
    private String descripcionClinica;

    @DatabaseField(columnName = NOMBRE_CAMPO_AUDIO_ANEXO)
    @SerializedName(NOMBRE_CAMPO_AUDIO_ANEXO)
    @Expose
    private String audioAnexo;

    @DatabaseField(columnName = NOMBRE_CAMPO_DESCRIPCION_ANEXO)
    @SerializedName(NOMBRE_CAMPO_DESCRIPCION_ANEXO)
    @Expose
    private String descripcionAnexo;

    public String getMejoraSubjetiva() {
        return mejoraSubjetiva;
    }

    public void setMejoraSubjetiva(String mejoraSubjetiva) {
        this.mejoraSubjetiva = mejoraSubjetiva;
    }

    public Boolean isTuvoTratamiento() {
        return tuvoTratamiento;
    }

    public void setTuvoTratamiento(Boolean tuvoTratamiento) {
        this.tuvoTratamiento = tuvoTratamiento;
    }

    public Integer getToleracionMedicacion() {
        return toleracionMedicacion;
    }

    public void setToleracionMedicacion(Integer toleracionMedicacion) {
        this.toleracionMedicacion = toleracionMedicacion;
    }

    public String getAudioClinica() {
        return audioClinica;
    }

    public void setAudioClinica(String audioClinica) {
        this.audioClinica = audioClinica;
    }

    public String getDescripcionClinica() {
        return descripcionClinica;
    }

    public void setDescripcionClinica(String descripcionClinica) {
        this.descripcionClinica = descripcionClinica;
    }

    public String getAudioAnexo() {
        return audioAnexo;
    }

    public void setAudioAnexo(String audioAnexo) {
        this.audioAnexo = audioAnexo;
    }

    public String getDescripcionAnexo() {
        return descripcionAnexo;
    }

    public void setDescripcionAnexo(String descripcionAnexo) {
        this.descripcionAnexo = descripcionAnexo;
    }

    public Integer getIdPatientLocal() {
        return idPatientLocal;
    }

    public void setIdPatientLocal(Integer idPatientLocal) {
        this.idPatientLocal = idPatientLocal;
    }

    public Integer getIdControlConsulta() {
        return idControlConsulta;
    }

    public void setIdControlConsulta(Integer idControlConsulta) {
        this.idControlConsulta = idControlConsulta;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        Log.i("ControlMedico", "getObservable");
        ControlService service = (ControlService) HttpUtils.crearServicio(ControlService.class);
        ControlConsulta control_consulta = new ControlConsulta();
        control_consulta.setIdConsulta(this.getIdConsulta());
        Observable lesionObservable = service.registrar(new ControlRequest(this,control_consulta, token, email));
        return lesionObservable;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        Log.i("ControlMedico", "nexAction");
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.actualizarCampo(NOMBRE_TABLA, ControlMedico.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseControl) response.body()).controlMedico.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampo(NOMBRE_TABLA, ControlMedico.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, ((ResponseControl) response.body()).controlMedico.getIdControlConsulta().toString(), this.getId().toString(), "id");
            data.actualizarCampo(Lesion.NOMBRE_TABLA, Lesion.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, ((ResponseControl) response.body()).controlConsulta.getIdServidor().toString(), this.getId().toString(), Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
            data.actualizarCampo(ImagenAnexo.NOMBRE_TABLA, ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, ((ResponseControl) response.body()).controlConsulta.getIdServidor().toString(), this.getId().toString(), ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable.toString() != null){
            Log.e(NOMBRE_TABLA, "exeption====>"+throwable.toString());
        }
       // new DataSincronizacion().actualizarArchivo(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }



}
