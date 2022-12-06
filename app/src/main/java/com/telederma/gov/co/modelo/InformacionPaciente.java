package com.telederma.gov.co.modelo;

import android.util.Log;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.PacienteService;
import com.telederma.gov.co.http.request.PacienteRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponsePaciente;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.InformacionPaciente.NOMBRE_TABLA;

@DatabaseTable(tableName = NOMBRE_TABLA)
public class InformacionPaciente extends BaseEntity implements ISincronizable {


    ////////CAMPOS PACIENTE INFORMACION///////////////////////
    public static final String NOMBRE_TABLA = "informacion_paciente";
    public static final String NOMBRE_CAMPO_TERMS_CONDITIONS = "terms_conditions";
    public static final String NOMBRE_CAMPO_ESTADO_CIVIL = "civil_status";
    public static final String NOMBRE_CAMPO_OCCUPATION = "occupation";
    public static final String NOMBRE_CAMPO_PHONE = "phone";
    public static final String NOMBRE_CAMPO_EMAIL = "email";
    public static final String NOMBRE_CAMPO_ADDRESS = "address";
    public static final String NOMBRE_CAMPO_MUNICIPIO_ID = "municipality_id";
    public static final String NOMBRE_CAMPO_URBAN_ZONE = "urban_zone";
    public static final String NOMBRE_CAMPO_ACOMPANANTE = "companion";
    public static final String NOMBRE_CAMPO_NAME_ACOMP = "name_companion";
    public static final String NOMBRE_CAMPO_PHONE_ACOMP = "phone_companion";
    public static final String NOMBRE_CAMPO_RESPONSIBLE = "responsible";
    public static final String NOMBRE_CAMPO_NAME_RESPON = "name_responsible";
    public static final String NOMBRE_CAMPO_PHONE_RESPON = "phone_responsible";
    public static final String NOMBRE_CAMPO_PARENTESCO = "relationship";
    public static final String NOMBRE_CAMPO_TYPE_USER = "type_user";
    public static final String NOMBRE_CAMPO_AUTHORIZATION_NUMBER = "authorization_number";
    public static final String NOMBRE_CAMPO_PURPUSE_CONSULTATION = "purpose_consultation";
    public static final String NOMBRE_CAMPO_EXTERNAL_CAUSE = "external_cause";
    public static final String NOMBRE_CAMPO_UNIT_MEASURE_AGE = "unit_measure_age";
    public static final String NOMBRE_CAMPO_AGE = "age";
    public static final String NOMBRE_CAMPO_PATIENT_ID = "patient_id";
    public static final String NOMBRE_CAMPO_INSURE = "insurance_id";
    public static final String NOMBRE_CAMPO_STATUS = "status";
    public static final String NOMBRE_CAMPO_ID_PATIENT_LOCAL = "id_patient_local";

    private Paciente paciente;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_PATIENT_LOCAL)
    private Integer id_patient_local;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TERMS_CONDITIONS)
    @SerializedName(NOMBRE_CAMPO_TERMS_CONDITIONS)
    @Expose
    private Boolean terms_conditions;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_INSURE)
    @SerializedName(NOMBRE_CAMPO_INSURE)
    @Expose
    private Integer insurance_id;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_UNIT_MEASURE_AGE)
    @SerializedName(NOMBRE_CAMPO_UNIT_MEASURE_AGE)
    @Expose
    private Integer unit_measure_age;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_AGE)
    @SerializedName(NOMBRE_CAMPO_AGE)
    @Expose
    private Integer age;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_OCCUPATION)
    @SerializedName(NOMBRE_CAMPO_OCCUPATION)
    @Expose
    private String occupation;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_PHONE)
    @SerializedName(NOMBRE_CAMPO_PHONE)
    @Expose
    private String phone;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_EMAIL)
    @SerializedName(NOMBRE_CAMPO_EMAIL)
    @Expose
    private String email;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_ADDRESS)
    @SerializedName(NOMBRE_CAMPO_ADDRESS)
    @Expose
    private String address;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_MUNICIPIO_ID)
    @SerializedName(NOMBRE_CAMPO_MUNICIPIO_ID)
    @Expose
    private Integer municipality_id;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_URBAN_ZONE)
    @SerializedName(NOMBRE_CAMPO_URBAN_ZONE)
    @Expose
    private Integer urban_zone;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_ACOMPANANTE)
    @SerializedName(NOMBRE_CAMPO_ACOMPANANTE)
    @Expose
    private Boolean companion;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_NAME_ACOMP)
    @SerializedName(NOMBRE_CAMPO_NAME_ACOMP)
    @Expose
    private String name_companion;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_PHONE_ACOMP)
    @SerializedName(NOMBRE_CAMPO_PHONE_ACOMP)
    @Expose
    private String phone_companion;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_RESPONSIBLE)
    @SerializedName(NOMBRE_CAMPO_RESPONSIBLE)
    @Expose
    private Boolean responsible;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_NAME_RESPON)
    @SerializedName(NOMBRE_CAMPO_NAME_RESPON)
    @Expose
    private String name_responsible;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_PHONE_RESPON)
    @SerializedName(NOMBRE_CAMPO_PHONE_RESPON)
    @Expose
    private String phone_responsible;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_PARENTESCO)
    @SerializedName(NOMBRE_CAMPO_PARENTESCO)
    @Expose
    private String relationship;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TYPE_USER)
    @SerializedName(NOMBRE_CAMPO_TYPE_USER)
    @Expose
    private Integer type_user;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_AUTHORIZATION_NUMBER)
    @SerializedName(NOMBRE_CAMPO_AUTHORIZATION_NUMBER)
    @Expose
    private String authozation_number;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_PURPUSE_CONSULTATION)
    @SerializedName(NOMBRE_CAMPO_PURPUSE_CONSULTATION)
    @Expose
    private Integer purpose_consultation;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_EXTERNAL_CAUSE)
    @SerializedName(NOMBRE_CAMPO_EXTERNAL_CAUSE)
    @Expose
    private Integer external_cause;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_ESTADO_CIVIL)
    @SerializedName(NOMBRE_CAMPO_ESTADO_CIVIL)
    @Expose
    private Integer civil_status;

    @DatabaseField(columnName = NOMBRE_CAMPO_PATIENT_ID)
    @SerializedName(NOMBRE_CAMPO_PATIENT_ID)
    @Expose
    private Integer patient_id;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_STATUS)
    @SerializedName(NOMBRE_CAMPO_STATUS)
    @Expose
    private Integer status;

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Boolean getTerms_conditions() {
        return terms_conditions;
    }

    public void setTerms_conditions(Boolean terms_conditions) {
        this.terms_conditions = terms_conditions;
    }

    public Integer getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(Integer insurance_id) {
        this.insurance_id = insurance_id;
    }

    public Integer getUnit_measure_age() {
        return unit_measure_age;
    }

    public void setUnit_measure_age(Integer unit_measure_age) {
        this.unit_measure_age = unit_measure_age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOccupation() {
        return (StringUtils.isEmpty(occupation) ? "No reporta" : occupation);
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhone() {
        return (StringUtils.isEmpty(phone) ? "No reporta" : phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return (StringUtils.isEmpty(email) ? "No reporta" : email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return (StringUtils.isEmpty(address) ? "No reporta" : address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMunicipality_id() {
        return municipality_id;
    }

    public void setMunicipality_id(Integer municipality_id) {
        this.municipality_id = municipality_id;
    }

    public Integer getUrban_zone() {
        return urban_zone;
    }

    public void setUrban_zone(Integer urban_zone) {
        this.urban_zone = urban_zone;
    }

    public Boolean getCompanion() {
        return companion;
    }

    public void setCompanion(Boolean companion) {
        this.companion = companion;
    }

    public String getName_companion() {
        return name_companion;
    }

    public void setName_companion(String name_companion) {
        this.name_companion = name_companion;
    }

    public String getPhone_companion() {
        return phone_companion;
    }

    public void setPhone_companion(String phone_companion) {
        this.phone_companion = phone_companion;
    }

    public Boolean getResponsible() {
        return responsible;
    }

    public void setResponsible(Boolean responsible) {
        this.responsible = responsible;
    }

    public String getName_responsible() {
        return name_responsible;
    }

    public void setName_responsible(String name_responsible) {
        this.name_responsible = name_responsible;
    }

    public String getPhone_responsible() {
        return phone_responsible;
    }

    public void setPhone_responsible(String phone_responsible) {
        this.phone_responsible = phone_responsible;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Integer getType_user() {
        return type_user;
    }

    public void setType_user(Integer type_user) {
        this.type_user = type_user;
    }

    public String getAuthozation_number() {
        return authozation_number;
    }

    public void setAuthozation_number(String authozation_number) {
        this.authozation_number = authozation_number;
    }

    public Integer getPurpose_consultation() {
        return purpose_consultation;
    }

    public void setPurpose_consultation(Integer purpose_consultation) {
        this.purpose_consultation = purpose_consultation;
    }

    public Integer getExternal_cause() {
        return external_cause;
    }

    public void setExternal_cause(Integer external_cause) {
        this.external_cause = external_cause;
    }

    public Integer getCivil_status() {
        return civil_status;
    }

    public void setCivil_status(Integer civil_status) {
        this.civil_status = civil_status;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getId_patient_local() {
        return id_patient_local;
    }

    public void setId_patient_local(Integer id_patient_local) {
        this.id_patient_local = id_patient_local;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        PacienteService service = (PacienteService) HttpUtils.crearServicio(PacienteService.class);
        Observable pacienteObservable = service.registrar(new PacienteRequest(this.getPaciente(), this, token, email));
        return pacienteObservable;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            Log.e("PATIEND_ID", ((ResponsePaciente) response.body()).informacion_paciente.getPatient_id().toString());
            Log.e("SERVER_ID", ((ResponsePaciente) response.body()).informacion_paciente.getIdServidor().toString());
            if(((ResponsePaciente) response.body()).informacion_paciente.getPatient_id() != null && ((ResponsePaciente) response.body()).informacion_paciente.getIdServidor() != null) {
                Log.e("UPDATE PATIENT", "UPDATE PATIENT");
                data.actualizarCampo(Paciente.NOMBRE_TABLA, Paciente.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponsePaciente) response.body()).informacion_paciente.getPatient_id().toString(), this.getPaciente().getId().toString(), "id");
                data.actualizarCampo(NOMBRE_TABLA, InformacionPaciente.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponsePaciente) response.body()).informacion_paciente.getIdServidor().toString(), this.getId().toString(), "id");
                data.actualizarCampoFecha(NOMBRE_TABLA, InformacionPaciente.NOMBRE_CAMPO_CREATED_AT, ((ResponsePaciente) response.body()).informacion_paciente.created_at, this.getId().toString(), "id");
                data.actualizarCampoFecha(NOMBRE_TABLA, InformacionPaciente.NOMBRE_CAMPO_UPDATED_AT, ((ResponsePaciente) response.body()).informacion_paciente.updated_at, this.getId().toString(), "id");

                try {
                    List<ConsultaMedica> consultas = data.getConsultaInformacion(this.getId());
                    for (Consulta consulta : consultas)
                        data.actualizarCampo(ConsultaMedica.NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_ID_INFORMACION_PACIENTE, ((ResponsePaciente) response.body()).informacion_paciente.getIdServidor().toString(), consulta.getId().toString(), "id");
                } catch (Exception e) {
                    if (e != null) {
                        e.toString();
                    }
                }
                try {
                    List<ConsultaEnfermeria> consultaEnfermeria = data.getConsultaEnfermeriaInformacion(this.getId());
                    for (Consulta consulta : consultaEnfermeria)
                        data.actualizarCampo(ConsultaEnfermeria.NOMBRE_TABLA, ConsultaEnfermeria.NOMBRE_CAMPO_ID_INFORMACION_PACIENTE, ((ResponsePaciente) response.body()).informacion_paciente.getIdServidor().toString(), consulta.getId().toString(), "id");
                } catch (Exception e) {
                    if (e != null) {
                        e.toString();
                    }
                }

                data.eliminarPendiente(this.getId(), NOMBRE_TABLA);
            }


        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable != null){
            throwable.toString();
        }
      //  new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }
}
