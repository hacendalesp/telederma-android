package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Daniel Hernández on 30/07/2018.
 */

@DatabaseTable(tableName = "formula")
public class Formula extends BaseEntity {

    public static final String NOMBRE_CAMPO_ID_RESPUESTA = "specialist_response_id";
    public static final String NOMBRE_CAMPO_CODIGO_MEDICAMENTO = "medication_code";
    public static final String NOMBRE_CAMPO_TIPO_MEDICAMENTO = "type_medicament";
    public static final String NOMBRE_CAMPO_NOMBRE_GENERICO_MEDICAMENTO = "generic_name_medicament";
    public static final String NOMBRE_CAMPO_FORMULA_FARMACEUTICA = "pharmaceutical_form";
    public static final String NOMBRE_CAMPO_CONCENTRACION_DROGA = "drug_concentration";
    public static final String NOMBRE_CAMPO_UNIDAD_MEDIDA_MEDICAMENTO = "unit_measure_medication";
    public static final String NOMBRE_CAMPO_NUMERO_UNIDADES = "number_of_units";
    public static final String NOMBRE_CAMPO_VALOR_UNIDAD_MEDICAMENTO = "unit_value_medicament";
    public static final String NOMBRE_CAMPO_TOTAL_VALOR_MEDICAMENTO = "total_value_medicament";
    public static final String NOMBRE_CAMPO_COMENTARIO = "commentations";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_RESPUESTA)
    @SerializedName(NOMBRE_CAMPO_ID_RESPUESTA)
    @Expose
    private Integer idRespuesta;

    @DatabaseField(columnName = NOMBRE_CAMPO_CODIGO_MEDICAMENTO)
    @SerializedName(NOMBRE_CAMPO_CODIGO_MEDICAMENTO)
    @Expose
    private String codigoMedicamento;

    @DatabaseField(columnName = NOMBRE_CAMPO_TIPO_MEDICAMENTO)
    @SerializedName(NOMBRE_CAMPO_TIPO_MEDICAMENTO)
    @Expose
    private String tipoMedicamento;

    @DatabaseField(columnName = NOMBRE_CAMPO_NOMBRE_GENERICO_MEDICAMENTO)
    @SerializedName(NOMBRE_CAMPO_NOMBRE_GENERICO_MEDICAMENTO)
    @Expose
    private String nombreGenericoMedicamento;

    @DatabaseField(columnName = NOMBRE_CAMPO_FORMULA_FARMACEUTICA)
    @SerializedName(NOMBRE_CAMPO_FORMULA_FARMACEUTICA)
    @Expose
    private String formaFarmaceutica;

    @DatabaseField(columnName = NOMBRE_CAMPO_CONCENTRACION_DROGA)
    @SerializedName(NOMBRE_CAMPO_CONCENTRACION_DROGA)
    @Expose
    private String concentracionDroga;

    @DatabaseField(columnName = NOMBRE_CAMPO_UNIDAD_MEDIDA_MEDICAMENTO)
    @SerializedName(NOMBRE_CAMPO_UNIDAD_MEDIDA_MEDICAMENTO)
    @Expose
    private String unidadMedidaMedicamento;

    @DatabaseField(columnName = NOMBRE_CAMPO_NUMERO_UNIDADES)
    @SerializedName(NOMBRE_CAMPO_NUMERO_UNIDADES)
    @Expose
    private String numeroUnidades;

    @DatabaseField(columnName = NOMBRE_CAMPO_VALOR_UNIDAD_MEDICAMENTO)
    @SerializedName(NOMBRE_CAMPO_VALOR_UNIDAD_MEDICAMENTO)
    @Expose
    private String valorUnidadMedicamento;

    @DatabaseField(columnName = NOMBRE_CAMPO_TOTAL_VALOR_MEDICAMENTO)
    @SerializedName(NOMBRE_CAMPO_TOTAL_VALOR_MEDICAMENTO)
    @Expose
    private String totalValorMedicamento;

    @DatabaseField(columnName = NOMBRE_CAMPO_COMENTARIO)
    @SerializedName(NOMBRE_CAMPO_COMENTARIO)
    @Expose
    private String comentarios;

    public Formula() {
    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(String codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getTipoMedicamento() {
        return tipoMedicamento;
    }

    public void setTipoMedicamento(String tipoMedicamento) {
        this.tipoMedicamento = tipoMedicamento;
    }

    public String getNombreGenericoMedicamento() {
        return nombreGenericoMedicamento;
    }

    public void setNombreGenericoMedicamento(String nombreGenericoMedicamento) {
        this.nombreGenericoMedicamento = nombreGenericoMedicamento;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public String getConcentracionDroga() {
        return concentracionDroga;
    }

    public void setConcentracionDroga(String concentracionDroga) {
        this.concentracionDroga = concentracionDroga;
    }

    public String getUnidadMedidaMedicamento() {
        return unidadMedidaMedicamento;
    }

    public void setUnidadMedidaMedicamento(String unidadMedidaMedicamento) {
        this.unidadMedidaMedicamento = unidadMedidaMedicamento;
    }

    public String getNumeroUnidades() {
        return numeroUnidades;
    }

    public void setNumeroUnidades(String numeroUnidades) {
        this.numeroUnidades = numeroUnidades;
    }

    public String getValorUnidadMedicamento() {
        return valorUnidadMedicamento;
    }

    public void setValorUnidadMedicamento(String valorUnidadMedicamento) {
        this.valorUnidadMedicamento = valorUnidadMedicamento;
    }

    public String getTotalValorMedicamento() {
        return totalValorMedicamento;
    }

    public void setTotalValorMedicamento(String totalValorMedicamento) {
        this.totalValorMedicamento = totalValorMedicamento;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

}
