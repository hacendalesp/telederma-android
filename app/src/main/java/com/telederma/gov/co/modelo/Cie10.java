package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * Created by Juan Sebastian Perez on 3/04/2019.
 */

@DatabaseTable(tableName = "cie10")
public class Cie10 extends BaseEntity {

    public static final String NOMBRE_CAMPO_NAME = "name";
    public static final String NOMBRE_CAMPO_CODE = "code";

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NAME)
    @SerializedName(NOMBRE_CAMPO_NAME)
    @Expose
    private String name;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_CODE)
    @SerializedName(NOMBRE_CAMPO_CODE)
    @Expose
    private String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return (getName()+" - "+getCode());
    }

}
