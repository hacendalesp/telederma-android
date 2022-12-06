package com.telederma.gov.co.modelo;

import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.RequerimientoService;
import com.telederma.gov.co.http.request.RequerimientoRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseRequerimiento;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.Requerimiento.NOMBRE_TABLA;

/**
 * Created by Daniel Hernández on 30/07/2018.
 */

@DatabaseTable(tableName = NOMBRE_TABLA)
public class Requerimiento extends BaseEntity implements ISincronizable {

    public static final String NOMBRE_TABLA = "requerimiento";
    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";
    public static final String NOMBRE_CAMPO_ID_CONTROL_CONSULTA = "medical_control_id";
    public static final String NOMBRE_CAMPO_COMENTARIO = "comment";
    public static final String NOMBRE_CAMPO_TIPO_REQUERIMIENTO = "type_request";
    public static final String NOMBRE_CAMPO_ID_DOCTOR = "doctor_id";
    public static final String NOMBRE_CAMPO_STATUS = "status";
    public static final String NOMBRE_CAMPO_AUDIO_CLINICA = "audio_request";
    public static final String NOMBRE_CAMPO_TEXTO_CLINICA = "description_request";
    public static final String NOMBRE_CAMPO_ESPECIALISTA = "specialist";
    public static final String NOMBRE_CAMPO_ID_ESPECIALISTA = "specialist_id";
    public static final String NOMBRE_CAMPO_REASON = "reason";
    public static final String NOMBRE_CAMPO_OTHER_REASON = "other_reason";
//    public static final String NOMBRE_CAMPO_CREATED_AT = "created_at";
    public static final String NOMBRE_CAMPO_HOUR = "hour";


    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private Integer idConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @Expose
    private Integer idControlMedico;

    @DatabaseField(columnName = NOMBRE_CAMPO_TEXTO_CLINICA)
    @SerializedName(NOMBRE_CAMPO_TEXTO_CLINICA)
    @Expose
    private String description_request;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_ESPECIALISTA)
    @SerializedName(NOMBRE_CAMPO_ID_ESPECIALISTA)
    @Expose
    private Integer specialist_id;

    @DatabaseField(columnName = NOMBRE_CAMPO_AUDIO_CLINICA)
    @SerializedName(NOMBRE_CAMPO_AUDIO_CLINICA)
    @Expose
    private String audioClinica;

    @DatabaseField(columnName = NOMBRE_CAMPO_COMENTARIO)
    @SerializedName(NOMBRE_CAMPO_COMENTARIO)
    @Expose
    private String comentario;

    @DatabaseField(columnName = NOMBRE_CAMPO_TIPO_REQUERIMIENTO)
    @SerializedName(NOMBRE_CAMPO_TIPO_REQUERIMIENTO)
    @Expose
    private String tipoRequerimiento;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_DOCTOR)
    @SerializedName(NOMBRE_CAMPO_ID_DOCTOR)
    @Expose
    private Integer idDoctor;


    @Expose
    private transient Usuario nurse;

    @Expose
    private transient Usuario doctor;

    @DatabaseField(columnName = NOMBRE_CAMPO_STATUS)
    @SerializedName(NOMBRE_CAMPO_STATUS)
    @Expose
    private Integer status;


//    @DatabaseField(columnName = NOMBRE_CAMPO_CREATED_AT)
//    @SerializedName(NOMBRE_CAMPO_CREATED_AT)
//    @Expose
//    private String created_at;

    @DatabaseField(columnName = NOMBRE_CAMPO_HOUR)
    @SerializedName(NOMBRE_CAMPO_HOUR)
    @Expose
    private String hour;


    @DatabaseField(columnName = NOMBRE_CAMPO_REASON)
    @SerializedName(NOMBRE_CAMPO_REASON)
    @Expose
    private Integer reason;

    @DatabaseField(columnName = NOMBRE_CAMPO_OTHER_REASON)
    @SerializedName(NOMBRE_CAMPO_OTHER_REASON)
    @Expose
    private String otherReason;

    @SerializedName(NOMBRE_CAMPO_ESPECIALISTA)
    @Expose
    private transient Especialista especialista;


    public Requerimiento() {
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(String tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }

    public Integer getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAudioClinica() {
        return audioClinica;
    }

    public void setAudioClinica(String audioClinica) {
        this.audioClinica = audioClinica;
    }

    public String getDescription_request() {
        return description_request;
    }

    public void setDescription_request(String description_request) {
        this.description_request = description_request;
    }

    public Integer getSpecialist_id() {
        return specialist_id;
    }

    public void setSpecialist_id(Integer specialist_id) {
        this.specialist_id = specialist_id;
    }

    public Integer getIdControlMedico() {
        return idControlMedico;
    }

    public void setIdControlMedico(Integer idControlMedico) {
        this.idControlMedico = idControlMedico;
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

    public Usuario getNurse() {
        return nurse;
    }

    public void setNurse(Usuario nurse) {
        this.nurse = nurse;
    }

    public Usuario getDoctor() {
        return doctor;
    }

    public void setDoctor(Usuario doctor) {
        this.doctor = doctor;
    }

    //    public String getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(String created_at) {
//        this.created_at = created_at;
//    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }


    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        RequerimientoService service = (RequerimientoService) HttpUtils.crearServicio(RequerimientoService.class);
        Observable observable = service.registrar(String.valueOf(this.getIdServidor()), new RequerimientoRequest(this, token, email));

        return observable;
    }






    @Override
    public <T> void nextAction(Response<T> response) {

        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.eliminarPendiente(this.getIdServidor(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        } else
            new DataSincronizacion().actualizarPendiente(this.getIdServidor(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable != null){
            throwable.toString();
        }
        //new DataSincronizacion().actualizarArchivo(this.getIdServidor(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }
}
