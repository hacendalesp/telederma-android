package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Daniel Hernández on 6/19/2018.
 */

@DatabaseTable(tableName = "departamento")
public class Departamento extends BaseEntity {

    public static final String NOMBRE_CAMPO_NOMBRE = "name";

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NOMBRE)
    @SerializedName(NOMBRE_CAMPO_NOMBRE)
    @Expose
    private String nombre;

    @SerializedName("municipalities")
    @Expose
    private List<Municipio> municipios;


    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
