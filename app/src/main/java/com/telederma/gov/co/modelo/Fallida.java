package com.telederma.gov.co.modelo;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Sebastián Noreña .
 */

@DatabaseTable(tableName = "fallida")
public class Fallida extends BaseEntity {

    public static final String API_ACCESS_KEY= "fallida";

    @DatabaseField(columnName = API_ACCESS_KEY)
    private Boolean fallida;

    public Boolean getFallida() {
        return fallida;
    }

    public void setFallida(Boolean fallida) {
        this.fallida = fallida;
    }
}
