package com.telederma.gov.co.modelo;

import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.ConsultaEnfermeriaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseConsultaEnfermeria;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.ConsultaEnfermeria.NOMBRE_TABLA;


@DatabaseTable(tableName = NOMBRE_TABLA)
public class ConsultaEnfermeria extends Consulta implements ISincronizable {

    public static final String NOMBRE_TABLA = "consulta_enfermeria";
    public static final String NOMBRE_CAMPO_WEIGHT = "weight";
    public static final String NOMBRE_CAMPO_ULCER_ETIOLOGY = "ulcer_etiology";
    public static final String NOMBRE_CAMPO_ULCER_ETIOLOGY_OTHER = "ulcer_etiology_other";
    public static final String NOMBRE_CAMPO_PAIN_INTENSITY = "pain_intensity";
    public static final String NOMBRE_CAMPO_ULCER_LENGTH= "ulcer_length";
    public static final String NOMBRE_CAMPO_ULCER_WIDTH = "ulcer_width";
    public static final String NOMBRE_CAMPO_INFECTION_SIGNS = "infection_signs";
    public static final String NOMBRE_CAMPO_WOUND_TISSUE = "wound_tissue";
    public static final String NOMBRE_CAMPO_SKIN_AMONG_ULCER = "skin_among_ulcer";
    public static final String NOMBRE_CAMPO_ULCER_HANDLE = "ulcer_handle";
    public static final String NOMBRE_CAMPO_ULCER_HANDLE_APOSITO = "ulcer_handle_aposito";
    public static final String NOMBRE_CAMPO_ULCER_HANDLE_OTHER = "ulcer_handle_other";
    public static final String NOMBRE_CAMPO_CONSULTATION_REASON_DESCRIPTION = "consultation_reason_description";
    public static final String NOMBRE_CAMPO_CONSULTATION_REASON_AUDIO = "consultation_reason_audio";
    public static final String NOMBRE_CAMPO_PULSES_PEDIO = "pulses_pedio";
    public static final String NOMBRE_CAMPO_PULSES_FEMORAL = "pulses_femoral";
    public static final String NOMBRE_CAMPO_PULSES_TIBIAL = "pulses_tibial";
    public static final String NOMBRE_CAMPO_ID_INFORMACION_PATIENT_LOCAL = "local_patient_information_id";
    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";

    public static final String INFLAMACION = "1";
    public static final String OLOR_FETIDO = "2";
    public static final String EXUDADO_PURULENTO = "3";
    public static final String AUMENTO_RECIENTE_DOLOR = "4";

    public static final String TEJIDO_GRANULACION = "1";
    public static final String FIBRINA = "2";
    public static final String TEJIDO_NECROTICO = "3";
    public static final String HIPERGRANULACION = "4";
    public static final String CICATRIZACION_COMPLETA = "5";
    public static final String COSTRA = "6";

    public static final String DERMATITIS = "1";
    public static final String FIBROSIS = "2";



    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_ETIOLOGY)
    @SerializedName(NOMBRE_CAMPO_ULCER_ETIOLOGY)
    @Expose
    private String ulcer_etiology;


    @DatabaseField(columnName = NOMBRE_CAMPO_WEIGHT)
    @SerializedName(NOMBRE_CAMPO_WEIGHT)
    @Expose
    private String weight;

    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_ETIOLOGY_OTHER)
    @SerializedName(NOMBRE_CAMPO_ULCER_ETIOLOGY_OTHER)
    @Expose
    private String ulcer_etiology_other;

    @DatabaseField(columnName = NOMBRE_CAMPO_PAIN_INTENSITY)
    @SerializedName(NOMBRE_CAMPO_PAIN_INTENSITY)
    @Expose
    private String pain_intensity;

    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_LENGTH)
    @SerializedName(NOMBRE_CAMPO_ULCER_LENGTH)
    @Expose
    private String ulcer_length;

    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_WIDTH)
    @SerializedName(NOMBRE_CAMPO_ULCER_WIDTH)
    @Expose
    private String ulcer_width;

    @DatabaseField(columnName = NOMBRE_CAMPO_INFECTION_SIGNS)
    @SerializedName(NOMBRE_CAMPO_INFECTION_SIGNS)
    @Expose
    private String infection_signs;

    @DatabaseField(columnName = NOMBRE_CAMPO_WOUND_TISSUE)
    @SerializedName(NOMBRE_CAMPO_WOUND_TISSUE)
    @Expose
    private String wound_tissue;

    @DatabaseField(columnName = NOMBRE_CAMPO_SKIN_AMONG_ULCER)
    @SerializedName(NOMBRE_CAMPO_SKIN_AMONG_ULCER)
    @Expose
    private String skin_among_ulcer;

    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_HANDLE)
    @SerializedName(NOMBRE_CAMPO_ULCER_HANDLE)
    @Expose
    private String ulcer_handle;

    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_HANDLE_APOSITO)
    @SerializedName(NOMBRE_CAMPO_ULCER_HANDLE_APOSITO)
    @Expose
    private String ulcer_handle_aposito;

    @DatabaseField(columnName = NOMBRE_CAMPO_ULCER_HANDLE_OTHER)
    @SerializedName(NOMBRE_CAMPO_ULCER_HANDLE_OTHER)
    @Expose
    private String ulcer_handle_other;

    @DatabaseField(columnName = NOMBRE_CAMPO_CONSULTATION_REASON_DESCRIPTION)
    @SerializedName(NOMBRE_CAMPO_CONSULTATION_REASON_DESCRIPTION)
    @Expose
    private String consultation_reason_description;

    @DatabaseField(columnName = NOMBRE_CAMPO_CONSULTATION_REASON_AUDIO)
    @SerializedName(NOMBRE_CAMPO_CONSULTATION_REASON_AUDIO)
    @Expose
    private String consultation_reason_audio;

    @DatabaseField(columnName = NOMBRE_CAMPO_PULSES_PEDIO)
    @SerializedName(NOMBRE_CAMPO_PULSES_PEDIO)
    @Expose
    private String pulses_pedio;

    @DatabaseField(columnName = NOMBRE_CAMPO_PULSES_FEMORAL)
    @SerializedName(NOMBRE_CAMPO_PULSES_FEMORAL)
    @Expose
    private String pulses_femoral;

    @DatabaseField(columnName = NOMBRE_CAMPO_PULSES_TIBIAL)
    @SerializedName(NOMBRE_CAMPO_PULSES_TIBIAL)
    @Expose
    private String pulses_tibial;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_INFORMACION_PATIENT_LOCAL)
    private String idInformacionPacienteLocal;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private Integer idConsulta;

    private List<ControlEnfermeria> controlesEnfermeria;

    public String getUlcer_etiology() {
        return ulcer_etiology;
    }

    public void setUlcer_etiology(String ulcer_etiology) {
        this.ulcer_etiology = ulcer_etiology;
    }

    public String getUlcer_etiology_other() {
        return ulcer_etiology_other;
    }

    public void setUlcer_etiology_other(String ulcer_etiology_other) {
        this.ulcer_etiology_other = ulcer_etiology_other;
    }

    public String getPain_intensity() {
        return pain_intensity;
    }

    public void setPain_intensity(String pain_intensity) {
        this.pain_intensity = pain_intensity;
    }

    public String getUlcer_length() {
        return ulcer_length;
    }

    public void setUlcer_length(String ulcer_length) {
        this.ulcer_length = ulcer_length;
    }

    public String getUlcer_width() {
        return ulcer_width;
    }

    public void setUlcer_width(String ulcer_width) {
        this.ulcer_width = ulcer_width;
    }

    public String getInfection_signs() {
        return infection_signs;
    }

    public void setInfection_signs(String infection_signs) {
        this.infection_signs = infection_signs;
    }

    public String getWound_tissue() {
        return wound_tissue;
    }

    public void setWound_tissue(String wound_tissue) {
        this.wound_tissue = wound_tissue;
    }

    public String getSkin_among_ulcer() {
        return skin_among_ulcer;
    }

    public void setSkin_among_ulcer(String skin_among_ulcer) {
        this.skin_among_ulcer = skin_among_ulcer;
    }

    public String getUlcer_handle() {
        return ulcer_handle;
    }

    public void setUlcer_handle(String ulcer_handle) {
        this.ulcer_handle = ulcer_handle;
    }

    public String getUlcer_handle_aposito() {
        return ulcer_handle_aposito;
    }

    public void setUlcer_handle_aposito(String ulcer_handle_aposito) {
        this.ulcer_handle_aposito = ulcer_handle_aposito;
    }

    public String getUlcer_handle_other() {
        return ulcer_handle_other;
    }

    public void setUlcer_handle_other(String ulcer_handle_other) {
        this.ulcer_handle_other = ulcer_handle_other;
    }

    public String getConsultation_reason_description() {
        return consultation_reason_description;
    }

    public void setConsultation_reason_description(String consultation_reason_description) {
        this.consultation_reason_description = consultation_reason_description;
    }

    public String getConsultation_reason_audio() {
        return consultation_reason_audio;
    }

    public void setConsultation_reason_audio(String consultation_reason_audio) {
        this.consultation_reason_audio = consultation_reason_audio;
    }

    public String getPulses_pedio() {
        return pulses_pedio;
    }

    public void setPulses_pedio(String pulses_pedio) {
        this.pulses_pedio = pulses_pedio;
    }

    public String getPulses_femoral() {
        return pulses_femoral;
    }

    public void setPulses_femoral(String pulses_femoral) {
        this.pulses_femoral = pulses_femoral;
    }

    public String getPulses_tibial() {
        return pulses_tibial;
    }

    public void setPulses_tibial(String pulses_tibial) {
        this.pulses_tibial = pulses_tibial;
    }

    public String getIdInformacionPacienteLocal() {
        return idInformacionPacienteLocal;
    }

    public void setIdInformacionPacienteLocal(String idInformacionPacienteLocal) {
        this.idInformacionPacienteLocal = idInformacionPacienteLocal;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public List<ControlEnfermeria> getControlesEnfermeria() {
        return controlesEnfermeria;
    }

    public void setControlesEnfermeria(List<ControlEnfermeria> controlesEnfermeria) {
        this.controlesEnfermeria = controlesEnfermeria;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        Observable pacienteObservable = service.registrarEnfermeria(new ConsultaEnfermeriaRequest(this, this, token, email));
        return pacienteObservable;
    }


    @Override
    public <T> void nextAction(Response<T> response) {
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.actualizarCampo(NOMBRE_TABLA, ConsultaEnfermeria.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseConsultaEnfermeria) response.body()).consultaEnfermeria.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampo(NOMBRE_TABLA, ConsultaEnfermeria.NOMBRE_CAMPO_ID_CONSULTA, ((ResponseConsultaEnfermeria) response.body()).consulta.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampo(Lesion.NOMBRE_TABLA, Lesion.NOMBRE_CAMPO_ID_CONSULTA, ((ResponseConsultaEnfermeria) response.body()).consulta.getIdServidor().toString(), this.getId().toString(), Lesion.NOMBRE_CAMPO_ID_CONSULT_LOCAL);
            data.actualizarCampo(ImagenAnexo.NOMBRE_TABLA, ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA, ((ResponseConsultaEnfermeria) response.body()).consulta.getIdServidor().toString(), this.getId().toString(), ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA_LOCAL);
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
       // new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }
}
