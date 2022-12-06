package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Entidad base con los campos por defecto
 * <p>
 * Created by Daniel Hernández on 6/8/2018.
 */
public abstract class BaseEntity implements Serializable {

    public static final String NOMBRE_CAMPO_ID_SERVIDOR = "id_server";
    public static final String NOMBRE_CAMPO_CREATED_AT = "created_at";
    public static final String NOMBRE_CAMPO_UPDATED_AT = "updated_at";


    public static class Archivo {

        public static final String URL_KEY = "url";

        public String nombre;

        @SerializedName(URL_KEY)
        @Expose
        public String url;

        public Archivo(String nombre) {
            this.nombre = nombre;
        }

    }


    @DatabaseField(generatedId = true)
    protected Integer id;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_SERVIDOR, unique = true)
    @SerializedName("id")
    @Expose(serialize = false)
    protected Integer idServidor;


    //@DatabaseField(columnName = NOMBRE_CAMPO_CREATED_AT)
    //@DatabaseField(columnName = NOMBRE_CAMPO_CREATED_AT, dataType = DataType.DATE_STRING, format = "dd/MM/yyyy hh:mm a")
    @DatabaseField(columnName = NOMBRE_CAMPO_CREATED_AT, dataType = DataType.DATE_STRING, format = "dd/MM/yyyy h:mm a")
    @SerializedName(NOMBRE_CAMPO_CREATED_AT)
    @Expose//(serialize = false)
    protected java.util.Date created_at;

    //@DatabaseField(columnName = NOMBRE_CAMPO_UPDATED_AT, dataType = DataType.DATE_STRING, format = "dd-MM-yyyy")
    @DatabaseField(columnName = NOMBRE_CAMPO_UPDATED_AT, dataType = DataType.DATE_STRING, format = "dd/MM/yyyy h:mm a")
    @SerializedName(NOMBRE_CAMPO_UPDATED_AT)
    @Expose//(serialize = false)
    protected java.util.Date  updated_at;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Integer idServidor) {
        this.idServidor = idServidor;
    }

    public String getCreated_at() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH);
        if(created_at != null)
            return f.format(created_at);
        else
            return "";
    }

    public java.util.Date getCreated_atFormatDate() {
        return  created_at;
    }

    public String getCreated_at_no_time() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        if(created_at != null)
            return f.format(created_at);
        else
            return "";
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }


    public String getUpdated_at() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH);
        if(updated_at != null)
            return f.format(updated_at);
        else
            return "";
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity entity = (BaseEntity) o;

        return idServidor.equals(entity.idServidor);
    }

}
