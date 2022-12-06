package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Daniel Hernández on 1/08/2018.
 */

@DatabaseTable(tableName = "respuesta_especialista")
public class RespuestaEspecialista extends BaseEntity {

    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";
    public static final String NOMBRE_CAMPO_ID_CONTROL_CONSULTA = "consultation_control_id";
    public static final String NOMBRE_CAMPO_CONTROL_RECOMENDADO = "control_recommended";
    public static final String NOMBRE_CAMPO_CONTROL_OTHER_DIAGNOSTICS = "diagnostics";
    public static final String NOMBRE_CAMPO_FORMULAS = "formulas";
    public static final String NOMBRE_CAMPO_MIPRES = "mipres";
    public static final String NOMBRE_CAMPO_ESPECIALISTA = "specialist";
    public static final String NOMBRE_CAMPO_ID_ESPECIALISTA = "specialist_id";
    public static final String NOMBRE_CAMPO_DESCRIPCION_LESION = "case_analysis";
    public static final String NOMBRE_CAMPO_ANALISIS_CASO = "analysis_description";
    public static final String NOMBRE_CAMPO_EXAMENES_SOLICITADOS = "request_exams";
    public static final String NOMBRE_CAMPO_EXAMENES_HOUR = "hour";


    @DatabaseField(columnName = NOMBRE_CAMPO_ID_ESPECIALISTA)
    @SerializedName(NOMBRE_CAMPO_ID_ESPECIALISTA)
    @Expose
    private Integer idEspecialista;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private Integer idConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @Expose
    private Integer idControlConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_CONTROL_RECOMENDADO)
    @SerializedName(NOMBRE_CAMPO_CONTROL_RECOMENDADO)
    @Expose
    private String controlRecomendado;

    @DatabaseField(columnName = NOMBRE_CAMPO_DESCRIPCION_LESION)
    @SerializedName(NOMBRE_CAMPO_DESCRIPCION_LESION)
    @Expose
    private String descripcionLesion;

    @DatabaseField(columnName = NOMBRE_CAMPO_ANALISIS_CASO)
    @SerializedName(NOMBRE_CAMPO_ANALISIS_CASO)
    @Expose
    private String analisisCaso;


    @DatabaseField(columnName = NOMBRE_CAMPO_EXAMENES_HOUR)
    @SerializedName(NOMBRE_CAMPO_EXAMENES_HOUR)
    @Expose
    private String hour;

    @SerializedName(NOMBRE_CAMPO_EXAMENES_SOLICITADOS)
    @Expose
    private List<ExamenSolicitado> examenesSolicitados;

    @SerializedName(NOMBRE_CAMPO_FORMULAS)
    @Expose
    private List<Formula> formulas;

    @SerializedName(NOMBRE_CAMPO_MIPRES)
    @Expose
    private List<Mipres> mipres;

    @SerializedName(NOMBRE_CAMPO_ESPECIALISTA)
    @Expose
    private Especialista especialista;

    @SerializedName(NOMBRE_CAMPO_CONTROL_OTHER_DIAGNOSTICS)
    @Expose
    private List<Diagnostico> diagnosticos;

    public RespuestaEspecialista() {
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Integer getIdControlConsulta() {
        return idControlConsulta;
    }

    public void setIdControlConsulta(Integer idControlConsulta) {
        this.idControlConsulta = idControlConsulta;
    }

    public String getControlRecomendado() {
        return controlRecomendado;
    }

    public void setControlRecomendado(String controlRecomendado) {
        this.controlRecomendado = controlRecomendado;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }

    public List<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public List<Formula> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<Formula> formulas) {
        this.formulas = formulas;
    }

    public List<Mipres> getMipres() {
        return mipres;
    }

    public void setMipres(List<Mipres> mipres) {
        this.mipres = mipres;
    }

    public Integer getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Integer idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public String getDescripcionLesion() {
        return descripcionLesion;
    }

    public void setDescripcionLesion(String descripcionLesion) {
        this.descripcionLesion = descripcionLesion;
    }

    public String getAnalisisCaso() {
        return analisisCaso;
    }

    public void setAnalisisCaso(String analisisCaso) {
        this.analisisCaso = analisisCaso;
    }

    public List<ExamenSolicitado> getExamenesSolicitados() {
        return examenesSolicitados;
    }

    public void setExamenesSolicitados(List<ExamenSolicitado> examenesSolicitados) {
        this.examenesSolicitados = examenesSolicitados;
    }


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getFecha() {
        return this.created_at + " "+ hour;
    }
}
