package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Daniel Hernández on 1/08/2018.
 */

@DatabaseTable(tableName = "mipres")
public class Mipres extends BaseEntity {

    public static final String NOMBRE_CAMPO_ID_RESPUESTA = "specialist_response_id";
    public static final String NOMBRE_CAMPO_MIPRES = "mipres";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_RESPUESTA)
    @SerializedName(NOMBRE_CAMPO_ID_RESPUESTA)
    @Expose
    private Integer idRespuesta;

    @DatabaseField(columnName = NOMBRE_CAMPO_MIPRES)
    private String mipres;

//    @SerializedName(NOMBRE_CAMPO_MIPRES)
//    @Expose
//    private Archivo mipresServicio;

//    public Mipres() {
//        mipresServicio = new Archivo(NOMBRE_CAMPO_MIPRES);
//    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getMipres() {
        return mipres;
    }

    public void setMipres(String mipres) {
        this.mipres = mipres;
    }

//    public Archivo getMipresServicio() {
//        return mipresServicio;
//    }
//
//    public void setMipresServicio(Archivo mipresServicio) {
//        this.mipresServicio = mipresServicio;
//    }
}
