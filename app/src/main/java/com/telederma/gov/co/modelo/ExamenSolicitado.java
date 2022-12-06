package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Daniel Hernández on 30/07/2018.
 */

@DatabaseTable(tableName = "examen_solicitado")
public class ExamenSolicitado extends BaseEntity {

    public static final String NOMBRE_CAMPO_ID_RESPUESTA = "specialist_response_id";
    public static final String NOMBRE_CAMPO_NOMBRE_TIPO_EXAMEN = "name_type_exam";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_RESPUESTA)
    @SerializedName(NOMBRE_CAMPO_ID_RESPUESTA)
    @Expose
    private Integer idRespuesta;

    @DatabaseField(columnName = NOMBRE_CAMPO_NOMBRE_TIPO_EXAMEN)
    @SerializedName(NOMBRE_CAMPO_NOMBRE_TIPO_EXAMEN)
    @Expose
    private String nombreTipoExamen;

    public ExamenSolicitado() {
    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getNombreTipoExamen() {
        return nombreTipoExamen;
    }

    public void setNombreTipoExamen(String nombreTipoExamen) {
        this.nombreTipoExamen = nombreTipoExamen;
    }

}
