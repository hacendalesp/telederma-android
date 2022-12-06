package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.InvocationTargetException;

import retrofit2.http.Field;

import static com.telederma.gov.co.utils.Constantes.API_REST_URL_BASE;

/**
 * Created by Daniel Hernández on 6/8/2018.
 */

@DatabaseTable(tableName = "usuario")
public class Usuario extends BaseEntity implements Cloneable {

    public static final String NOMBRE_CAMPO_DOCUMENTO = "number_document";
    public static final String NOMBRE_CAMPO_CONTRASENA = "password";
    public static final String NOMBRE_CAMPO_ACTUAL_CONTRASENA = "current_password";
    public static final String NOMBRE_CAMPO_NUEVA_CONTRASENA = "new_password";
    public static final String NOMBRE_CAMPO_CONTRASENA_CONFIRMACION = "password_confirmation";
    public static final String NOMBRE_CAMPO_NOMBRES = "name";
    public static final String NOMBRE_CAMPO_APELLIDOS = "surnames";
    public static final String NOMBRE_CAMPO_EMAIL = "email";
    public static final String NOMBRE_CAMPO_TIPO_PROFESIONAL = "type_professional";
    public static final String NOMBRE_CAMPO_TARJETA_PROFESIONAL = "professional_card";
    public static final String NOMBRE_CAMPO_TELEFONO = "phone";
    public static final String NOMBRE_CAMPO_TERMINOS_CONDICIONES = "terms_and_conditions";
    public static final String NOMBRE_CAMPO_STATUS = "status";
    public static final String NOMBRE_CAMPO_FIRMA_DIGITAL = "digital_signature";
    public static final String NOMBRE_CAMPO_FOTO = "photo";
    public static final String NOMBRE_CAMPO_TUTORIAL = "tutorial";
    public static final String NOMBRE_CAMPO_TOKEN_AUTENTICACION = "authentication_token";
    public static final String NOMBRE_CAMPO_IMAGEN_FIRMA = "image_digital";


    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_DOCUMENTO)
    @SerializedName(NOMBRE_CAMPO_DOCUMENTO)
    @Expose
    private String documento;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_NOMBRES)
    @SerializedName(NOMBRE_CAMPO_NOMBRES)
    @Expose
    private String nombres;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_APELLIDOS)
    @SerializedName(NOMBRE_CAMPO_APELLIDOS)
    @Expose
    private String apellidos;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TIPO_PROFESIONAL)
    @SerializedName(NOMBRE_CAMPO_TIPO_PROFESIONAL)
    @Expose
    private Integer tipoProfesional;

    @DatabaseField(columnName = NOMBRE_CAMPO_TARJETA_PROFESIONAL)
    @SerializedName(NOMBRE_CAMPO_TARJETA_PROFESIONAL)
    @Expose
    private String tarjetaProfesional;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TELEFONO)
    @SerializedName(NOMBRE_CAMPO_TELEFONO)
    @Expose
    private String telefono;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TERMINOS_CONDICIONES)
    @SerializedName(NOMBRE_CAMPO_TERMINOS_CONDICIONES)
    @Expose
    private boolean terminosCondiciones;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_FIRMA_DIGITAL)
    @SerializedName(NOMBRE_CAMPO_FIRMA_DIGITAL)
    @Expose
    private String firmaDigital;

    @DatabaseField(columnName = NOMBRE_CAMPO_FOTO)
    private String foto;

    @SerializedName(NOMBRE_CAMPO_FOTO)
    @Expose
    private Archivo fotoServicio;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TUTORIAL)
    @SerializedName(NOMBRE_CAMPO_TUTORIAL)
    @Expose
    private boolean tutorial;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_TOKEN_AUTENTICACION)
    @SerializedName(NOMBRE_CAMPO_TOKEN_AUTENTICACION)
    @Expose
    private String tokenAutenticacion;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_CONTRASENA)
    @SerializedName(NOMBRE_CAMPO_CONTRASENA)
    @Expose
    private String contrasena;

    // utilizado para actualizar el perfil del usuario
    @Expose
    private String current_password;

    @SerializedName(NOMBRE_CAMPO_CONTRASENA_CONFIRMACION)
    @Expose
    private String contrasena_confirmacion;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_EMAIL)
    @SerializedName(NOMBRE_CAMPO_EMAIL)
    @Expose
    private String email;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_STATUS)
    @SerializedName(NOMBRE_CAMPO_STATUS)
    @Expose
    private Integer status;

    @DatabaseField(canBeNull = false, columnName = NOMBRE_CAMPO_IMAGEN_FIRMA)
    private String imagenFirma;

    @SerializedName(NOMBRE_CAMPO_IMAGEN_FIRMA)
    @Expose
    private Archivo imagenFirmaServicio;

    public Usuario() {
        this.imagenFirmaServicio = new Archivo(API_REST_URL_BASE + NOMBRE_CAMPO_IMAGEN_FIRMA);
        this.fotoServicio = new Archivo(NOMBRE_CAMPO_FOTO);
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getTipoProfesional() {
        return tipoProfesional;
    }

    public void setTipoProfesional(Integer tipoProfesional) {
        this.tipoProfesional = tipoProfesional;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isTerminosCondiciones() {
        return terminosCondiciones;
    }

    public void setTerminosCondiciones(boolean terminosCondiciones) {
        this.terminosCondiciones = terminosCondiciones;
    }

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }

    public String getTokenAutenticacion() {
        return tokenAutenticacion;
    }

    public void setTokenAutenticacion(String tokenAutenticacion) {
        this.tokenAutenticacion = tokenAutenticacion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContrasena_confirmacion() {
        return contrasena_confirmacion;
    }

    public void setContrasena_confirmacion(String contrasena_confirmacion) {
        this.contrasena_confirmacion = contrasena_confirmacion;
    }

    public String getImagenFirma() {
        return imagenFirma;
    }

    public void setImagenFirma(String imagenFirma) {
        this.imagenFirma = imagenFirma;
    }

    public Archivo getFotoServicio() {
        return fotoServicio;
    }

    public void setFotoServicio(Archivo fotoServicio) {
        this.fotoServicio = fotoServicio;
    }

    public Archivo getImagenFirmaServicio() {
        return imagenFirmaServicio;
    }

    public void setImagenFirmaServicio(Archivo imagenFirmaServicio) {
        this.imagenFirmaServicio = imagenFirmaServicio;
    }

    public String getNombreCompleto() {
        return String.format("%s %s", this.nombres, this.apellidos);
    }

    public String getCurrent_password() {
        return current_password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    public static Usuario fillNewUser(Usuario user, int type){
        user.setDocumento("");
        //user.setIdServidor(user.getId());
        //user.setNombres("");
        //user.setApellidos("");
        user.setTipoProfesional(type);
        //user.setTarjetaProfesional("11111111");
        user.setTelefono("");
        user.setTerminosCondiciones(true);
        user.setFirmaDigital("");
        user.setFoto("");
        user.setFotoServicio(null);
        user.setTutorial(true);
        user.setTokenAutenticacion("");
        user.setContrasena("");
        user.setContrasena_confirmacion("");
        user.setEmail("");
        user.setStatus(0);
        user.setImagenFirma("");
        user.setImagenFirmaServicio(null);

        return user;
    }



}
