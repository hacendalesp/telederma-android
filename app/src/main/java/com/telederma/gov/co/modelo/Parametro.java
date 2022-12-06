package com.telederma.gov.co.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Daniel Hernández on 6/18/2018.
 */

@DatabaseTable(tableName = "parametro")
public class Parametro extends BaseEntity {

    public static final String UNIQUE_INDEX_NAME_PARAMETRO = "UNIQUE_INDEX_NAME_PARAMETRO";

    public static final String NOMBRE_CAMPO_TIPO = "type";
    public static final String NOMBRE_CAMPO_NOMBRE = "name";
    public static final String NOMBRE_CAMPO_VALOR = "value";

    public static final String TIPO_PARAMETRO_TIPO_PROFESIONAL = "type_professional";
    public static final String TIPO_PARAMETRO_STATUS = "status";
    public static final String TIPO_PARAMETRO_TIPO_DOCUMENTO = "type_document";
    public static final String TIPO_PARAMETRO_TIPO_CONDICION = "type_condition";
    public static final String TIPO_PARAMETRO_GENERO = "genre";
    public static final String TIPO_PARAMETRO_ESTADO_CIVIL = "civil_status";
    public static final String TIPO_PARAMETRO_UNIDAD_DE_MEDIDA = "unit_measurement";
    public static final String TIPO_PARAMETRO_TIPO_USUARIO = "type_user";
    public static final String TIPO_PARAMETRO_PROPOSITO_DE_CONSULTA = "consultation_purpose";
    public static final String TIPO_PARAMETRO_CAUSA_EXTERNA = "external_cause";
    public static final String TIPO_PARAMETRO_ZONA_URBANA = "urban_zone";
    public static final String TIPO_PARAMETRO_NUMERO_LESIONES = "number_injuries";
    public static final String TIPO_PARAMETRO_EVOLUCION_LESIONES = "evolution_injuries";
    public static final String TIPO_PARAMETRO_ESTADO_DE_CONSULTA = "status_consultant";
    public static final String TIPO_PARAMETRO_SINTOMA = "symptom";
    public static final String TIPO_PARAMETRO_CAMBIO_DE_SINTOMAS = "change_symptom";
    public static final String TIPO_PARAMETRO_TOLERO_MEDICAMENTOS = "tolerate_medications";
    public static final String TIPO_PARAMETRO_REASON = "reason";

    //Enfermeria
    public static final String TIPO_PARAMETRO_ULCER_ETIOLOGY = "ulcer_etiology";
    public static final String TIPO_PARAMETRO_PULSES = "pulses";
    public static final String TIPO_PARAMETRO_ULCER = "ulcer_handle";
    public static final String TIPO_PARAMETRO_SIGNOS_INFECCION = "infection_signs";
    public static final String TIPO_PARAMETRO_TEJIDO_HERIDA = "wound_tissue";
    public static final String TIPO_PARAMETRO_PIEL_ALREDEDOR_ULCERA = "skin_among_ulcer";
    public static final String TIPO_PARAMETRO_MANEJO_ULCERA = "ulcer_handle";


    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TIPO, uniqueIndexName = UNIQUE_INDEX_NAME_PARAMETRO)
    private String tipo;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NOMBRE)
    private String nombre;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_VALOR, uniqueIndexName = UNIQUE_INDEX_NAME_PARAMETRO)
    private Integer valor;


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

}
