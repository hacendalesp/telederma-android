package com.telederma.gov.co.modelo;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;

public class Consulta extends BaseEntity {

//    public static final Integer ESTADO_CONSULTA_CREANDO = -1;
    public static final Integer ESTADO_CONSULTA_SIN_ENVIAR = 0;
    public static final Integer ESTADO_CONSULTA_RESUELTO = 1;
    public static final Integer ESTADO_CONSULTA_REQUERIMIENTO = 2;
    public static final Integer ESTADO_CONSULTA_PENDIENTE = 3;
    public static final Integer ESTADO_CONSULTA_ARCHIVADO = 4;
    public static final Integer ESTADO_CONSULTA_PROCESO = 5;
    public static final Integer ESTADO_CONSULTA_SIN_CREDITOS = 6;
    public static final Integer ESTADO_CONSULTA_REMISION = 7;
    public static final Integer ESTADO_CONSULTA_EVALUANDO = 8;

    public static final String NOMBRE_CAMPO_ID_PACIENTE = "patient_id";
    public static final String NOMBRE_CAMPO_ID_INFORMACION_PACIENTE = "patient_information_id";
    public static final String NOMBRE_CAMPO_ANEXO_DESCRIPTION = "annex_description";
    public static final String NOMBRE_CAMPO_IMAGENES_ANEXO = "annex_images";
    public static final String NOMBRE_CAMPO_ANEXO_AUDIO = "audio_annex";
    public static final String NOMBRE_CAMPO_CONSULTA_MEDICA = "medical_consultation";
    public static final String NOMBRE_CAMPO_CONSULTA_ENFERMERIA = "nurse_consultation";
    public static final String NOMBRE_CAMPO_CONTROLES_MEDICOS = "consultation_controls";
    public static final String NOMBRE_CAMPO_ID_DOCTOR = "doctor_id";
    public static final String NOMBRE_CAMPO_ID_ENFERMERA = "nurse_id";
    public static final String NOMBRE_CAMPO_INFORMACION_PACIENTE = "patient_information";
    public static final String NOMBRE_CAMPO_LESIONES = "injuries";
    public static final String NOMBRE_CAMPO_RESPUESTA_ESPECIALISTA = "specialist_response";
    public static final String NOMBRE_CAMPO_ESTADO = "status";
    public static final String NOMBRE_CAMPO_CONSULTA_LEIDA = "readed";
    public static final String NOMBRE_CAMPO_TIPO_PROFESIONAL = "type_profesional";
    public static final String NOMBRE_CAMPO_TRATAMIENTO = "treatment";
    public static final String NOMBRE_CAMPO_CANTIDAD_CONTROLES = "count_controls";
    public static final String NOMBRE_CAMPO_REQUERIMIENTOS = "request";
    public static final String NOMBRE_CAMPO_FECHA_ARCHIVADO = "date_archived";
    public static final String NOMBRE_CAMPO_ARCHIVADO = "archived";
    public static final String NOMBRE_CAMPO_IMPRESION_DIAGNOSTICO = "diagnostic_impression";
    public static final String NOMBRE_CAMPO_CIE10_CODE = "ciediezcode";
    public static final String NOMBRE_CAMPO_TYPE_REMISSION= "type_remission";
    public static final String NOMBRE_CAMPO_REMISSION_COMMENTS = "remission_comments";


    @DatabaseField(columnName = NOMBRE_CAMPO_TYPE_REMISSION)
    @SerializedName(NOMBRE_CAMPO_TYPE_REMISSION)
    @Expose
    private String type_remission;

    @DatabaseField(columnName = NOMBRE_CAMPO_REMISSION_COMMENTS)
    @SerializedName(NOMBRE_CAMPO_REMISSION_COMMENTS)
    @Expose
    private String remission_comments;

    @SerializedName(NOMBRE_CAMPO_REQUERIMIENTOS)
    @Expose
    private List<Requerimiento> requerimientos;

    @DatabaseField(columnName = NOMBRE_CAMPO_ARCHIVADO)
    @SerializedName(NOMBRE_CAMPO_ARCHIVADO)
    @Expose
    private Boolean archivado;

    @DatabaseField(columnName = NOMBRE_CAMPO_CANTIDAD_CONTROLES)
    @SerializedName(NOMBRE_CAMPO_CANTIDAD_CONTROLES)
    @Expose
    private Integer cantidadControles;

    @DatabaseField(columnName = NOMBRE_CAMPO_TRATAMIENTO)
    @SerializedName(NOMBRE_CAMPO_TRATAMIENTO)
    @Expose
    private String tratamiento;

    @DatabaseField(columnName = NOMBRE_CAMPO_FECHA_ARCHIVADO)
    @SerializedName(NOMBRE_CAMPO_FECHA_ARCHIVADO)
    @Expose
    private String fechaArchivado;

    @DatabaseField(columnName = NOMBRE_CAMPO_TIPO_PROFESIONAL)
    @SerializedName(NOMBRE_CAMPO_TIPO_PROFESIONAL)
    @Expose
    private Integer tipoProfesional;

    @DatabaseField(columnName = NOMBRE_CAMPO_CONSULTA_LEIDA)
    private boolean consultaLeida;

    @DatabaseField(columnName = NOMBRE_CAMPO_ANEXO_DESCRIPTION)
    @SerializedName(NOMBRE_CAMPO_ANEXO_DESCRIPTION)
    @Expose
    private String anexoDescripcion;

    @SerializedName(NOMBRE_CAMPO_IMAGENES_ANEXO)
    @Expose(serialize = false)
    private List<ImagenAnexo> imagenesAnexo;

    @DatabaseField(columnName = NOMBRE_CAMPO_ANEXO_AUDIO)
    @SerializedName(NOMBRE_CAMPO_ANEXO_AUDIO)
    @Expose
    private String anexoAudio;

    @SerializedName(NOMBRE_CAMPO_CONSULTA_MEDICA)
    @Expose(serialize = false)
    private ConsultaMedica consultaMedica;

    @SerializedName(NOMBRE_CAMPO_CONSULTA_ENFERMERIA)
    @Expose(serialize = false)
    private ConsultaEnfermeria consultaEnfermeria;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_PACIENTE)
    @SerializedName(NOMBRE_CAMPO_ID_PACIENTE)
    @Expose
    private Integer idPaciente;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_INFORMACION_PACIENTE)
    @SerializedName(NOMBRE_CAMPO_ID_INFORMACION_PACIENTE)
    @Expose
    private Integer idInformacionPaciente;

    @SerializedName(NOMBRE_CAMPO_INFORMACION_PACIENTE)
    @Expose
    private InformacionPaciente informacionPaciente;

    @DatabaseField(columnName = NOMBRE_CAMPO_ESTADO)
    @SerializedName(NOMBRE_CAMPO_ESTADO)
    @Expose
    private Integer estado;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_DOCTOR)
    @SerializedName(NOMBRE_CAMPO_ID_DOCTOR)
    @Expose
    private Integer idDoctor;

    @Expose
    private Usuario nurse;

    @Expose
    private Usuario doctor;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_ENFERMERA)
    @SerializedName(NOMBRE_CAMPO_ID_ENFERMERA)
    @Expose
    private Integer idEnfermera;

    @DatabaseField(columnName = NOMBRE_CAMPO_IMPRESION_DIAGNOSTICO)
    @SerializedName(NOMBRE_CAMPO_IMPRESION_DIAGNOSTICO)
    @Expose
    private String impresionDiagnostica;

    // transient hace que no se envie el parametro en el ws ni se almacene en la bd
    //@Expose // hace que no se envie el dato en el ws
    // este campo se agrega en el archivo ormlite_config, en la tabla consulta_medica y debe ser agregado en caso de ser necesario para la tabla de enfermeria.
    @DatabaseField(columnName = NOMBRE_CAMPO_CIE10_CODE)
    @SerializedName(NOMBRE_CAMPO_CIE10_CODE)
    @Expose
    private transient String ciediezcode;

    @SerializedName(NOMBRE_CAMPO_RESPUESTA_ESPECIALISTA)
    @Expose
    private List<RespuestaEspecialista> respuestaEspecialista;

    @SerializedName(NOMBRE_CAMPO_LESIONES)
    @Expose(serialize = false)
    private List<Lesion> lesiones;

    @SerializedName(NOMBRE_CAMPO_CONTROLES_MEDICOS)
    @Expose//(serialize = false)
    private List<ControlConsulta> controles;


    public String getAnexoDescripcion() {
        return anexoDescripcion;
    }

    public void setAnexoDescripcion(String anexoDescripcion) {
        this.anexoDescripcion = anexoDescripcion;
    }

    public String getAnexoAudio() {
        return anexoAudio;
    }

    public void setAnexoAudio(String anexoAudio) {
        this.anexoAudio = anexoAudio;
    }

    public Integer getIdInformacionPaciente() {
        return idInformacionPaciente;
    }

    public void setIdInformacionPaciente(Integer idInformacionPaciente) {
        this.idInformacionPaciente = idInformacionPaciente;
    }

    public InformacionPaciente getInformacionPaciente() {
        return informacionPaciente;
    }

    public void setInformacionPaciente(InformacionPaciente informacionPaciente) {
        this.informacionPaciente = informacionPaciente;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public List<ControlConsulta> getControles() {
        return controles;
    }

    public void setControles(List<ControlConsulta> controles) {
        this.controles = controles;
    }

    public boolean isConsultaLeida() {
        return consultaLeida;
    }

    public void setConsultaLeida(boolean consultaLeida) {
        this.consultaLeida = consultaLeida;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Integer getTipoProfesional() {
        return tipoProfesional;
    }

    public void setTipoProfesional(Integer tipoProfesional) {
        this.tipoProfesional = tipoProfesional;
    }

    public List<Lesion> getLesiones() {
        return lesiones;
    }

    public void setLesiones(List<Lesion> lesiones) {
        this.lesiones = lesiones;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public List<RespuestaEspecialista> getRespuestaEspecialista() {
        return respuestaEspecialista;
    }

    public void setRespuestaEspecialista(List<RespuestaEspecialista> respuestaEspecialista) {
        this.respuestaEspecialista = respuestaEspecialista;
    }

    public Integer getCantidadControles() {
        return cantidadControles;
    }

    public void setCantidadControles(Integer cantidadControles) {
        this.cantidadControles = cantidadControles;
    }

    public ConsultaMedica getConsultaMedica() {
        return consultaMedica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consultaMedica = consultaMedica;
    }

    public String getFechaArchivado() {
        return fechaArchivado;
    }

    public void setFechaArchivado(String fechaArchivado) {
        this.fechaArchivado = fechaArchivado;
    }

    public Boolean getArchivado() {
        return archivado;
    }

    public void setArchivado(Boolean archivado) {
        this.archivado = archivado;
    }

    public ConsultaEnfermeria getConsultaEnfermeria() {
        return consultaEnfermeria;
    }

    public void setConsultaEnfermeria(ConsultaEnfermeria consultaEnfermeria) {
        this.consultaEnfermeria = consultaEnfermeria;
    }

    public Integer getIdEnfermera() {
        return idEnfermera;
    }

    public void setIdEnfermera(Integer idEnfermera) {
        this.idEnfermera = idEnfermera;
    }

    public String getImpresionDiagnostica() {
        //return ((impresionDiagnostica == null) ? "Ninguna" : impresionDiagnostica);
        return (StringUtils.isEmpty(impresionDiagnostica) ? "No reporta" : impresionDiagnostica);
    }

    public void setImpresionDiagnostica(String impresionDiagnostica) {
        this.impresionDiagnostica = impresionDiagnostica;
    }

    public List<ImagenAnexo> getImagenesAnexo() {
        return imagenesAnexo;
    }

    public void setImagenesAnexo(List<ImagenAnexo> imagenesAnexo) {
        this.imagenesAnexo = imagenesAnexo;
    }

    public List<Requerimiento> getRequerimientos() {
        return (requerimientos == null ? ( new ArrayList<Requerimiento>()) : requerimientos );
    }

    public void setRequerimientos(List<Requerimiento> requerimientos) {
        this.requerimientos = requerimientos;
    }

    public String getType_remission() {
        return type_remission;
    }

    public void setType_remission(String type_remission) {
        this.type_remission = type_remission;
    }

    public String getRemission_comments() {
        return remission_comments;
    }

    public void setRemission_comments(String remission_comments) {
        this.remission_comments = remission_comments;
    }

    public String getCiediezcode() {
        return ciediezcode;
    }

    public void setCiediezcode(String ciediezcode) {
        this.ciediezcode = ciediezcode;
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
//
//    public void setDoctor(Usuario doctor) {
//        this.doctor = doctor;
//    }
}
