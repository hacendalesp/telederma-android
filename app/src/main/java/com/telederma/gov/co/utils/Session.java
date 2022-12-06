package com.telederma.gov.co.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Singleton que mantiene datos en contexto de sesión
 * <p>
 * Created by Daniel Hernández on 6/8/2018.
 */
public final class Session {

    public static final String SESSION_LOGUEADO = "SHARED_PREFERENCES_LOGUEADO";
    public static final String SESSION_USERNAME = "USER";
    public static final String SESSION_EMAIL = "EMAIL";
    public static final String SESSION_TOKEN = "TOKEN";
    public static final String SESSION_ID_USUARIO = "ID_USUARIO";
    public static final String SESSION_TIPO_PROFESIONAL = "SESSION_TIPO_PROFESIONAL";
    public static final String SESSION_NOMBRE_USUARIO = "NOMBRE_USUARIO";
    public static final String SESSION_IMAGEN_URL_USUARIO = "IMAGEN_URL_USUARIO";
    public static final String SESSION_IMAGEN_FIRMA = "SESSION_IMAGEN_FIRMA";
    public static final String SESSION_IMEI = "SESSION_IMEI";

    private SharedPreferences sharedPreferences;
    private static Session instance;
    public static Context context_actual;

    private Session() {
        new File(Constantes.DIRECTORIO_TEMPORAL).mkdirs();
    }

    static public Session getInstance(Context context) {
        if (instance == null)
            instance = new Session();

        instance.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (context_actual == null)
            context_actual = context;
        return instance;
    }

    /**
     * Retorna las credenciales del usuario en sesión
     *
     * @return
     */
    public Credentials getCredentials() {
        final Credentials credentials = new Credentials();
        credentials.setIdUsuario(sharedPreferences.getInt(SESSION_ID_USUARIO, -1));
        credentials.setUsername(sharedPreferences.getString(SESSION_USERNAME, ""));
        credentials.setToken(sharedPreferences.getString(SESSION_TOKEN, ""));
        credentials.setEmail(sharedPreferences.getString(SESSION_EMAIL, ""));
        credentials.setNombreUsuario(sharedPreferences.getString(SESSION_NOMBRE_USUARIO, ""));
        credentials.setImagenFirma(sharedPreferences.getString(SESSION_IMAGEN_FIRMA, ""));
        credentials.setImei(sharedPreferences.getString(SESSION_IMEI, ""));
        credentials.setTipoProfesional(sharedPreferences.getInt(SESSION_TIPO_PROFESIONAL, -1));

        return credentials;
    }

    /**
     * Sobreescribe las credenciales del usuario en sesión
     *
     * @param credentials
     */
    public void setCredentials(Credentials credentials) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SESSION_ID_USUARIO, credentials.getIdUsuario());
        editor.putString(SESSION_USERNAME, credentials.getUsername());
        editor.putString(SESSION_TOKEN, credentials.getToken());
        editor.putString(SESSION_EMAIL, credentials.getEmail());
        editor.putString(SESSION_NOMBRE_USUARIO, credentials.getNombreUsuario());
        editor.putString(SESSION_IMAGEN_FIRMA, credentials.getImagenFirma());
        editor.putString(SESSION_IMEI, Utils.getInstance(context_actual).getIMEI());
        editor.putInt(SESSION_TIPO_PROFESIONAL, credentials.getTipoProfesional());
        editor.apply();
    }

    /**
     * Método que recibe datos para ponerlos en sesión
     * Sólo recibe datos primitivos
     *
     * @param name
     * @param value
     */
    public void put(String name, Object value) {
        if (value instanceof String)
            sharedPreferences.edit().putString(name, (String) value).apply();
        else if (value instanceof Integer)
            sharedPreferences.edit().putInt(name, (Integer) value).apply();
        else if (value instanceof Boolean)
            sharedPreferences.edit().putBoolean(name, (Boolean) value).apply();
        else if (value instanceof Float)
            sharedPreferences.edit().putFloat(name, (Float) value).apply();
        else if (value instanceof Long)
            sharedPreferences.edit().putLong(name, (Long) value).apply();
    }

    /**
     * Método que retorna un dato puesto en sesión
     *
     * @param name
     * @return
     */
    public Object get(String name) {

        return sharedPreferences.getAll().get(name);
    }

    public void invalidate() {
        clearStorage();
        sharedPreferences.edit().clear().apply();
        instance = null;
    }

    private void clearStorage() {
        FileUtils.eliminarArchivos(new File(Constantes.DIRECTORIO_TEMPORAL));
    }

    /**
     * Pojo con la información del usuario en sesión
     */
    public static class Credentials {

        private Integer idUsuario;
        private String username;
        private String email;
        private String token;
        private String nombreUsuario;
        private String imagenFirma;
        private String imei;
        private Integer tipoProfesional;


        public Integer getTipoProfesional() {
            return tipoProfesional;
        }

        public void setTipoProfesional(Integer tipo_profesional) {
            this.tipoProfesional = tipo_profesional;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(Integer idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public void setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
        }

        public String getImagenFirma() {
            return imagenFirma;
        }

        public void setImagenFirma(String imagenFirma) {
            this.imagenFirma = imagenFirma;
        }
    }

}
