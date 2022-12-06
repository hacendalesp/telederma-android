package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Daniel Hernández on 30/07/2018.
 */

@DatabaseTable(tableName = "especialista")
public class Especialista extends BaseEntity {

    public static final String NOMBRE_CAMPO_NOMBRES = "name";
    public static final String NOMBRE_CAMPO_APELLIDOS = "surnames";
    public static final String NOMBRE_CAMPO_FECHA = "date";
    public static final String NOMBRE_CAMPO_HORA = "hour";
    public static final String NOMBRE_CAMPO_DOCUMENTO = "professional_card";

    @DatabaseField(columnName = NOMBRE_CAMPO_NOMBRES)
    @SerializedName(NOMBRE_CAMPO_NOMBRES)
    @Expose
    private String nombre;

    @DatabaseField(columnName = NOMBRE_CAMPO_APELLIDOS)
    @SerializedName(NOMBRE_CAMPO_APELLIDOS)
    @Expose
    private String apellidos;

    @DatabaseField(columnName = NOMBRE_CAMPO_FECHA)
    @SerializedName(NOMBRE_CAMPO_FECHA)
    @Expose
    private String fecha;

    @DatabaseField(columnName = NOMBRE_CAMPO_HORA)
    @SerializedName(NOMBRE_CAMPO_HORA)
    @Expose
    private String hora;

    @DatabaseField(columnName = NOMBRE_CAMPO_DOCUMENTO)
    @SerializedName(NOMBRE_CAMPO_DOCUMENTO)
    @Expose
    private String documento;

    public Especialista() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreCompleto() {
        return String.format("%s %s", this.nombre, (this.apellidos == null ? "" : this.apellidos));
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
