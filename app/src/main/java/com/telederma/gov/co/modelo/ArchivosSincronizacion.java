package com.telederma.gov.co.modelo;

import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import io.reactivex.Observable;
import retrofit2.Response;

@DatabaseTable(tableName = "archivos_sincronizacion")
public class ArchivosSincronizacion extends BaseEntity {

    public static final String NOMBRE_CAMPO_ID_ = "id_local";
    public static final String NOMBRE_CAMPO_TABLE = "table";
    public static final String NOMBRE_CAMPO_FIELD = "field";
    public static final String NOMBRE_CAMPO_EXTENSION = "extencion";
    public static final String NOMBRE_CAMPO_PATH = "path";
    public static final String NOMBRE_CAMPO_STATUS = "status";

    public static final Integer ESTADO_PENDIENTE = 0;
    public static final Integer ESTADO_SINCRONIZANDO = 1;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_)
    @SerializedName(NOMBRE_CAMPO_ID_)
    private String id_local;

    @DatabaseField(columnName = NOMBRE_CAMPO_TABLE)
    @SerializedName(NOMBRE_CAMPO_TABLE)
    private String table;

    @DatabaseField(columnName = NOMBRE_CAMPO_FIELD)
    @SerializedName(NOMBRE_CAMPO_FIELD)
    private String field;

    @DatabaseField(columnName = NOMBRE_CAMPO_EXTENSION)
    @SerializedName(NOMBRE_CAMPO_EXTENSION)
    private String extension;

    @DatabaseField(columnName = NOMBRE_CAMPO_PATH)
    @SerializedName(NOMBRE_CAMPO_PATH)
    private String path;

    @DatabaseField(columnName = NOMBRE_CAMPO_STATUS, defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_STATUS)
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId_local() {
        return id_local;
    }

    public void setId_local(String id_local) {
        this.id_local = id_local;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
