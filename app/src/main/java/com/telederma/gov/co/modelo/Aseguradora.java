package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "aseguradoras")
public class Aseguradora extends BaseEntity {

    public static final String NOMBRE_CAMPO_NAME = "name";
    public static final String NOMBRE_CAMPO_CODE = "code";


    @DatabaseField(columnName = NOMBRE_CAMPO_CODE)
    @SerializedName(NOMBRE_CAMPO_CODE)
    @Expose
    private String code;

    @DatabaseField(columnName = NOMBRE_CAMPO_NAME)
    @SerializedName(NOMBRE_CAMPO_NAME)
    @Expose
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
