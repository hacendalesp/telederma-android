package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Daniel Hernández on 30/07/2018.
 */

@DatabaseTable(tableName = "diagnostico")
public class Diagnostico extends BaseEntity {

    public static final String NOMBRE_CAMPO_ID_RESPUESTA = "specialist_response_id";
    public static final String NOMBRE_CAMPO_DIAGNOSTICO_PRINCIPAL = "disease";
    public static final String NOMBRE_CAMPO_TIPO_DIAGNOSTICO = "type_diagnostic";
    public static final String NOMBRE_CAMPO_STATUS = "status";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_RESPUESTA)
    @SerializedName(NOMBRE_CAMPO_ID_RESPUESTA)
    @Expose
    private Integer idRespuesta;

    @DatabaseField(columnName = NOMBRE_CAMPO_DIAGNOSTICO_PRINCIPAL)
    @SerializedName(NOMBRE_CAMPO_DIAGNOSTICO_PRINCIPAL)
    @Expose
    private String diagnosticoPrincipal;

    @DatabaseField(columnName = NOMBRE_CAMPO_TIPO_DIAGNOSTICO)
    @SerializedName(NOMBRE_CAMPO_TIPO_DIAGNOSTICO)
    @Expose
    private String tipoDiagnostico;

    @DatabaseField(columnName = NOMBRE_CAMPO_STATUS)
    @SerializedName(NOMBRE_CAMPO_STATUS)
    @Expose
    private String status;


    public Diagnostico() {
    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getDiagnosticoPrincipal() {
        return diagnosticoPrincipal;
    }

    public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
        this.diagnosticoPrincipal = diagnosticoPrincipal;
    }

    public String getTipoDiagnostico() {
        return tipoDiagnostico;
    }

    public void setTipoDiagnostico(String tipoDiagnostico) {
        this.tipoDiagnostico = tipoDiagnostico;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
