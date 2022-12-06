package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Daniel Hernández on 6/19/2018.
 */

public class Municipio extends BaseEntity {

    public static final String NOMBRE_CAMPO_NOMBRE = "name";
    public static final String NOMBRE_CAMPO_ID_DEPARTAMENTO = "department_id";

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NOMBRE)
    @SerializedName(NOMBRE_CAMPO_NOMBRE)
    @Expose
    private String nombre;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_ID_DEPARTAMENTO)
    @SerializedName(NOMBRE_CAMPO_ID_DEPARTAMENTO)
    @Expose
    private Integer idDepartamento;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

}
