package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Sebastián Noreña .
 */

@DatabaseTable(tableName = "credenciales")
public class Credencial extends BaseEntity {

    public static final String API_ACCESS_KEY= "ACCESS_KEY";
    public static final String API_SECRET_KEY = "SECRET_KEY";
    public static final String API_BUCKET_NAME= "BUCKET_NAME";
    public static final String API_BUCKET_DIRECTORY = "BUCKET_DIRECTORY";

    @DatabaseField(columnName = API_ACCESS_KEY)
    @SerializedName(API_ACCESS_KEY)
    @Expose
    private String api_access_key;

    @DatabaseField(columnName = API_SECRET_KEY)
    @SerializedName(API_SECRET_KEY)
    @Expose
    private String api_secret_key;

    @DatabaseField(columnName = API_BUCKET_NAME)
    @SerializedName(API_BUCKET_NAME)
    @Expose
    private String api_bucket_name;

    @DatabaseField(columnName = API_BUCKET_DIRECTORY)
    @SerializedName(API_BUCKET_DIRECTORY)
    @Expose
    private String api_bucket_directory;

    public String getApi_bucket_name() {
        return api_bucket_name;
    }

    public void setApi_bucket_name(String api_bucket_name) {
        this.api_bucket_name = api_bucket_name;
    }
    
    public String getApi_access_key() {
        return api_access_key;
    }

    public void setApi_access_key(String api_access_key) {
        this.api_access_key = api_access_key;
    }

    public String getApi_secret_key() {
        return api_secret_key;
    }

    public void setApi_secret_key(String api_secret_key) {
        this.api_secret_key = api_secret_key;
    }

    public String getApi_bucket_directory() {
        return api_bucket_directory;
    }

    public void setApi_bucket_directory(String api_bucket_directory) {
        this.api_bucket_directory = api_bucket_directory;
    }

}
