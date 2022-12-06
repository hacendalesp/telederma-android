package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Created by Daniel Hernández on 4/10/2018.
 */

public class ControlConsulta extends BaseEntity {

    public static final String NOMBRE_CAMPO_TYPE_PROFESSIONAL = "type_professional";
    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";
    public static final String NOMBRE_CAMPO_ID_DOCTOR = "doctor_id";
    public static final String NOMBRE_CAMPO_ID_ENFERMERA = "nurse_id";
    public static final String NOMBRE_CAMPO_ESTADO = "status";
    public static final String NOMBRE_CAMPO_RESPUESTA_ESPECIALISTA = "specialist_response";
    public static final String NOMBRE_CAMPO_REQUERIMIENTOS = "request";
    public static final String NOMBRE_CAMPO_LESIONES = "injuries";
    public static final String NOMBRE_CAMPO_IMAGENES_ANEXO = "annex_images";
    public static final String NOMBRE_CAMPO_CONTROL_MEDICO = "medical_control";
    public static final String NOMBRE_CAMPO_CONTROL_ENFERMERIA = "nurse_control";
    public static final String NOMBRE_CAMPO_TRATAMIENTO = "treatment";

    public static final String NOMBRE_CAMPO_TIPO_REMISSION = "type_remission";
    public static final String NOMBRE_CAMPO_COMENTARIO_REMISSION = "remission_comments";

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_TRATAMIENTO)
    @SerializedName(NOMBRE_CAMPO_TRATAMIENTO)
    @Expose
    private String tratamiento;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_TIPO_REMISSION)
    @SerializedName(NOMBRE_CAMPO_TIPO_REMISSION)
    @Expose
    private String tipo_remision;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_COMENTARIO_REMISSION)
    @SerializedName(NOMBRE_CAMPO_COMENTARIO_REMISSION)
    @Expose
    private String comentario_remision;


    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_ID_DOCTOR)
    @SerializedName(NOMBRE_CAMPO_ID_DOCTOR)
    @Expose
    private int idDoctor;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_ID_ENFERMERA)
    @SerializedName(NOMBRE_CAMPO_ID_ENFERMERA)
    @Expose
    private int idEnfermera;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private int idConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_ESTADO)
    @SerializedName(NOMBRE_CAMPO_ESTADO)
    @Expose
    private Integer estado;

    @DatabaseField(columnName = NOMBRE_CAMPO_TYPE_PROFESSIONAL)
    @SerializedName(NOMBRE_CAMPO_TYPE_PROFESSIONAL)
    @Expose
    private Integer tipoProfesional;

    @SerializedName(NOMBRE_CAMPO_LESIONES)
    @Expose
    private List<Lesion> lesiones;

    @SerializedName(NOMBRE_CAMPO_IMAGENES_ANEXO)
    @Expose(serialize = false)
    private List<ImagenAnexo> imagenesAnexo;

    @SerializedName(NOMBRE_CAMPO_RESPUESTA_ESPECIALISTA)
    @Expose
    private List<RespuestaEspecialista> respuestaEspecialista;

    @SerializedName(NOMBRE_CAMPO_REQUERIMIENTOS)
    @Expose
    private List<Requerimiento> requerimientos;

    @SerializedName(NOMBRE_CAMPO_CONTROL_MEDICO)
    @Expose
    private ControlMedico controlMedico;

    @SerializedName(NOMBRE_CAMPO_CONTROL_ENFERMERIA)
    @Expose
    private ControlEnfermeria controlEnfermeria;

    @Expose
    private Usuario nurse;

    @Expose
    private Usuario doctor;




    public ControlConsulta() {
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<RespuestaEspecialista> getRespuestaEspecialista() {
        return respuestaEspecialista;
    }

    public void setRespuestaEspecialista(List<RespuestaEspecialista> respuestaEspecialista) {
        this.respuestaEspecialista = respuestaEspecialista;
    }


    public List<Requerimiento> getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(List<Requerimiento> requerimientos) {
        this.requerimientos = requerimientos;
    }

    public Integer getTipoProfesional() {
        return tipoProfesional;
    }

    public void setTipoProfesional(Integer tipoProfesional) {
        this.tipoProfesional = tipoProfesional;
    }

    public int getIdEnfermera() {
        return idEnfermera;
    }

    public void setIdEnfermera(int idEnfermera) {
        this.idEnfermera = idEnfermera;
    }

    public List<Lesion> getLesiones() {
        return lesiones;
    }

    public void setLesiones(List<Lesion> lesiones) {
        this.lesiones = lesiones;
    }

    public List<ImagenAnexo> getImagenesAnexo() {
        return imagenesAnexo;
    }

    public void setImagenesAnexo(List<ImagenAnexo> imagenesAnexo) {
        this.imagenesAnexo = imagenesAnexo;
    }

    public ControlMedico getControlMedico() {
        return controlMedico;
    }

    public void setControlMedico(ControlMedico controlMedico) {
        this.controlMedico = controlMedico;
    }

    public ControlEnfermeria getControlEnfermeria() {
        return controlEnfermeria;
    }

    public void setControlEnfermeria(ControlEnfermeria controlEnfermeria) {
        this.controlEnfermeria = controlEnfermeria;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getTipo_remision() {
        return tipo_remision;
    }

    public void setTipo_remision(String tipo_remision) {
        this.tipo_remision = tipo_remision;
    }

    public String getComentario_remision() {
        return comentario_remision;
    }
    public void setComentario_remision(String comentario_remision) {
        this.comentario_remision = comentario_remision;
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
}
