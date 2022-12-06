package com.telederma.gov.co.modelo;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.telederma.gov.co.modelo.PendienteSincronizacion.NOMBRE_TABLA;

//@DatabaseTable(tableName = "pendientes_sincronizacion")
@DatabaseTable(tableName = NOMBRE_TABLA)
public class PendienteSincronizacion extends BaseEntity {

    public static final String NOMBRE_TABLA = "pendientes_sincronizacion";
    public static final String NOMBRE_CAMPO_ID_ = "id_local";
    public static final String NOMBRE_CAMPO_TABLE = "table";
    public static final String NOMBRE_CAMPO_EMAIL = "email";
    public static final String NOMBRE_CAMPO_TOKEN = "token";
    public static final String NOMBRE_CAMPO_DATE_TIME = "registration_date";
    public static final String NOMBRE_CAMPO_STATUS = "status";

    public static final Integer ESTADO_PENDIENTE = 0;
    public static final Integer ESTADO_SINCRONIZANDO = 1;
    public static final Integer ESTADO_OMITIDO = 2;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_)
    @SerializedName(NOMBRE_CAMPO_ID_)
    private String id_local;

    @DatabaseField(columnName = NOMBRE_CAMPO_TABLE)
    @SerializedName(NOMBRE_CAMPO_TABLE)
    private String table;

    @DatabaseField(columnName = NOMBRE_CAMPO_EMAIL)
    @SerializedName(NOMBRE_CAMPO_EMAIL)
    private String email;

    @DatabaseField(columnName = NOMBRE_CAMPO_TOKEN)
    @SerializedName(NOMBRE_CAMPO_TOKEN)
    private String token;

    @DatabaseField(columnName = NOMBRE_CAMPO_DATE_TIME, dataType = DataType.DATE_STRING,
            format = "yyyy-MM-dd HH:mm:ss")
    @SerializedName(NOMBRE_CAMPO_DATE_TIME)
    private java.util.Date registration_date;

    @DatabaseField(columnName = NOMBRE_CAMPO_STATUS, defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_STATUS)
    private Integer status;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        String status_name = "";
        switch(this.status) {
            case 0:
                status_name =  "PENDIENTE";
                break;
            case 1:
                status_name =  "SINCRONIZANDO";
                break;
            case 2:
                status_name =  "OMITIDO";
                break;
            default:
                status_name = String.valueOf(this.status);
        }
        return status_name;
    }

    public String getHumanDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dt = this.registration_date;//new Date();
        String S = sdf.format(dt); // formats to 09/23/2009 13:53:28.238


        return S;
    }


}
