package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Sebastian Noreña on 1/08/2018.
 */

@DatabaseTable(tableName = "partes_cuerpo")
public class ParteCuerpo extends BaseEntity {

    public static final String UNIQUE_INDEX_NAME_PARAMETRO = "UNIQUE_INDEX_NAME_PARAMETRO";
    public static final String NOMBRE_CAMPO_NOMBRE = "name";

    @DatabaseField(columnName = NOMBRE_CAMPO_NOMBRE)
    @SerializedName(NOMBRE_CAMPO_NOMBRE)
    @Expose
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
