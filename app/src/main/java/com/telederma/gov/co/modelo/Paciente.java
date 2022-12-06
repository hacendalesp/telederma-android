package com.telederma.gov.co.modelo;

import android.text.TextUtils;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import static com.telederma.gov.co.modelo.Paciente.NOMBRE_TABLA;

@DatabaseTable(tableName = NOMBRE_TABLA)
public class Paciente extends BaseEntity {
    public static final String NOMBRE_TABLA = "paciente";

    ///////////CAMPOS FIJOS USUARIOS ///////////////////////
    public static final String NOMBRE_CAMPO_TYPE_DOCUMENT = "type_document";
    public static final String NOMBRE_CAMPO_NUMBER_DOCUMENT = "number_document";
    public static final String NOMBRE_CAMPO_TYPE_CONDITION = "type_condition";
    public static final String NOMBRE_CAMPO_NAME = "name";
    public static final String NOMBRE_CAMPO_SECOND_NAME = "second_name";
    public static final String NOMBRE_CAMPO_LAST_NAME = "last_name";
    public static final String NOMBRE_CAMPO_BIRTHDATE = "birthdate";
    public static final String NOMBRE_CAMPO_GENRE = "genre";
    public static final String NOMBRE_CAMPO_SECOND_SUERNAME = "second_surname";
    public static final String NOMBRE_CAMPO_STATUS = "status";
    public static final String NOMBRE_CAMPO_NUMBER_INPEC = "number_inpec";
    public static final String NOMBRE_CAMPO_CONSULTAS = "consultants";


    ///////////////////////////CONSTANTES PACIENTE INFORMACION PACIENTE SERVIDOR/////////////////////////////////////////////
    ////////////////////////////TIPO DOCUMENTO////////////////////////////////
    public static final int CEDULA_CIUDADANIA  = 1;
    public static final int CEDULA_EXTRANJERIA = 2;
    public static final int CARNE_DIPLOMATICO  = 3;
    public static final int PASAPORTE          = 4;
    public static final int SALVOCONDUCTO      = 5;
    public static final int PERMISO_ESPECIAL_PERMANENCIA   = 6;
    public static final int RESIDENTE_ESPECIAL_PARA_LA_PAZ = 7;
    public static final int REGISTRO_CIVIL                 = 8;
    public static final int TARJETA_DE_IDENTIDAD           = 9;
    public static final int CERTIFICADO_DE_NACIDO_VIVO     = 10;
    public static final int ADULTO_SIN_IDENTIFICAR         = 11;
    public static final int MENOR_SIN_IDENTIFICAR          = 12;
  /////////////////////CONDICIÓN//////////////////////////////
    public static final int TERCERA_EDAD = 1;  //#Personas de la tercera edad en protección de ancianatos.
    public static final int INDIGENAS_MAYOR_EDAD = 2 ;//#Indígenas mayores de edad
    public static final int HABITANTES_CALLE_MAYOR_EDAD = 3;// #Habitantes de la calle mayores de edad.
    public static final int HABITANTES_CALLE_MENOR_EDAD = 4 ;//#Habitantes de la calle menores de edad.
    public static final int MENOR_EDAD_ICBF = 5 ;          //#Menores de edad desvinculados del conflicto armado. Población infantil vulnerable bajo protección en instituciones diferentes al ICBF. Menores de edad bajo protección del ICBF.
    public static final int INDIGENAS_MENOR_EDAD = 6;     // #Indígenas menores de edad
    public static final int  MENOR_EDAD_NACIDO = 7 ;      //#Menor de edad recién nacido con edad menor o igual a un (1) mes.
    public static final int VICTIMAS_MENOR_EDAD = 8 ; //#Víctimas menores de edad
    public static final int VICTIMAS_MAYOR_EDAD = 9 ; //#Víctimas mayores de edad
    public static final int POBLACION_RECLUSA = 10 ; //#Población Reclusa con identificación interna asignada por el Instituto Nacional Penitenciario y Carcelario – INPEC

    public static final int ANIOS = 1 ;
    public static final int MESES = 2 ;
    public static final int DIAS  = 3 ;

    public InformacionPaciente infoPaciente;
    public String medico_token;
    public String medico_email;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TYPE_DOCUMENT,defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_TYPE_DOCUMENT)
    @Expose
    private Integer type_document;

    @DatabaseField(columnName = NOMBRE_CAMPO_TYPE_CONDITION)
    @SerializedName(NOMBRE_CAMPO_TYPE_CONDITION)
    @Expose
    private Integer type_condition;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NUMBER_DOCUMENT,defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_NUMBER_DOCUMENT)
    @Expose
    private String number_document;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_LAST_NAME,defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_LAST_NAME)
    @Expose
    private String last_name;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_SECOND_SUERNAME)
    @SerializedName(NOMBRE_CAMPO_SECOND_SUERNAME)
    @Expose
    private String second_surname;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NAME,defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_NAME)
    @Expose
    private String name;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_SECOND_NAME)
    @SerializedName(NOMBRE_CAMPO_SECOND_NAME)
    @Expose
    private String second_name;

    @DatabaseField(canBeNull = true, columnName = NOMBRE_CAMPO_BIRTHDATE)
    @SerializedName(NOMBRE_CAMPO_BIRTHDATE)
    @Expose
    private String birthdate;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_GENRE,defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_GENRE)
    @Expose
    private Integer genre;

    @DatabaseField(columnName = NOMBRE_CAMPO_NUMBER_INPEC)
    @SerializedName(NOMBRE_CAMPO_NUMBER_INPEC)
    @Expose
    private String number_inpec;

    @SerializedName(NOMBRE_CAMPO_CONSULTAS)
    @Expose
    private List<Consulta> consultas;

    @SerializedName("patient_information")
    @Expose
    private InformacionPaciente informacionPaciente;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_STATUS,defaultValue = "0")
    @SerializedName(NOMBRE_CAMPO_STATUS)
    @Expose
    private Integer status;

    public Integer getType_condition() {
        return type_condition;
    }

    public void setType_condition(Integer type_condition) {
        this.type_condition = type_condition;
    }

    public Integer getType_document() {
        return type_document;
    }

    public void setType_document(Integer type_document) {
        this.type_document = type_document;
    }

    public String getNumber_document() {
        //return number_document;
        return (StringUtils.isEmpty(number_document) ? "No reporta" : number_document);
    }

    public void setNumber_document(String number_document) {
        this.number_document = number_document;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSecond_surname() {
        return second_surname;
    }

    public void setSecond_surname(String second_surname) {
        this.second_surname = second_surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public InformacionPaciente getInformacionPaciente() {
        return informacionPaciente;
    }

    public void setInformacionPaciente(InformacionPaciente informacionPaciente) {
        this.informacionPaciente = informacionPaciente;
    }

    public String getNombreCompleto() {
        final StringBuilder sb = new StringBuilder();

        sb.append(this.name).append(" ");
        if (!TextUtils.isEmpty(this.second_name)) sb.append(this.second_name).append(" ");
        sb.append(this.last_name);
        if (!TextUtils.isEmpty(this.second_surname)) sb.append(" ").append(this.second_surname);

        return sb.toString();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNumber_inpec() {
        return number_inpec;
    }

    public void setNumber_inpec(String number_inpec) {
        this.number_inpec = number_inpec;
    }

    public InformacionPaciente getInfoPaciente() {
        return infoPaciente;
    }

    public void setInfoPaciente(InformacionPaciente infoPaciente) {
        this.infoPaciente = infoPaciente;
    }


    public String getMedico_token() {
        return medico_token;
    }

    public void setMedico_token(String medico_token) {
        this.medico_token = medico_token;
    }

    public String getMedico_email() {
        return medico_email;
    }

    public void setMedico_email(String medico_email) {
        this.medico_email = medico_email;
    }


}


