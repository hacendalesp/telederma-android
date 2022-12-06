package com.telederma.gov.co.utils;

import android.os.Environment;

/**
 * Clase que contiene las constantes generales
 * <p>
 * To activate Huawei debug just call to *#*#2846579#*#*
 * <p>
 * Created by Daniel Hernández on 6/6/2018.
 */
public class Constantes {

    //public static final String API_REST_IP_SERVER = "telederma-web.herokuapp.com";
    //public static final String API_REST_IP_SERVER = "209.97.149.155";
    //public static final String API_REST_IP_SERVER = "209.97.151.1";
    public static final String API_REST_IP_SERVER = "telederma.gov.co" ;//"telederma.gov.co";//"telederma.gov.co";//"cb3ad32c.ngrok.io";//"104.248.1.97";
    //public static final String API_REST_IP_SERVER = "192.168.1.68";//"cb3ad32c.ngrok.io";//"104.248.1.97";
    ///public static final String API_REST_IP_SERVER = "192.168.1.11";//"cb3ad32c.ngrok.io";//"104.248.1.97";
    public static final Integer API_REST_PORT_SERVER = 443;
    ///public static final Integer API_REST_PORT_SERVER = 3000;
    //public static final Integer API_REST_PORT_SERVER = 80;
    public static final String API_REST_URL_BASE = String.format("https://%s:%s/", API_REST_IP_SERVER, API_REST_PORT_SERVER);
    ///public static final String API_REST_URL_BASE = String.format("http://%s:%s/", API_REST_IP_SERVER, API_REST_PORT_SERVER);
    //public static final String API_REST_URL_BASE = String.format("http://%s:%s/", API_REST_IP_SERVER, API_REST_PORT_SERVER);

    //public static final String API_REST_URL_BASE = ("http://"+ API_REST_IP_SERVER);

    public static final String DATABASE_NAME = "telederma.db";
    public static final int DATABASE_VERSION = 1;


    public static final String TAG_ERROR_BASE_ACTIVITY = "TAG_ERROR_BASE_ACTIVITY";
    public static final String TAG_ERROR_MENU_ACTIVITY = "TAG_ERROR_BASE_ACTIVITY";
    public static final String TAG_ERROR_ORMLITE_BASE_ACTIVITY = "TAG_ERROR_ORMLITE_BASE_ACTIVITY";
    public static final String TAG_LOGIN_ACTIVITY_BACK_STACK = "TAG_LOGIN_ACTIVITY_BACK_STACK";
    public static final String TAG_MENU_ACTIVITY_BACK_STACK = "TAG_MENU_ACTIVITY_BACK_STACK";
    public static final String TAG_DESCARGAR_ARCHIVO = "DESCARGAR_ARCHIVO_AUDIO_TASK";

    public static final int RESPONSE_CODE_SUCCESSFUL = 200;
    public static final int RESPONSE_CODE_ERROR_411 = 411;
    public static final int RESPONSE_CODE_ERROR_500 = 500;
    public static final int RESPONSE_CODE_ERROR_401 = 401;

    public static final String NOMBRE_ARCHIVO_CONSULTA_MEDICINA_AUDIO_ANEXO = "consulta_medicina_%s_audio_anexo";
    public static final String NOMBRE_ARCHIVO_CONSULTA_ENFERMERIA_AUDIO_ANEXO = "consulta_enfermeria_%s_audio_anexo";
    public static final String NOMBRE_ARCHIVO_CONSULTA_AUDIO_EXAMEN_FISICO = "consulta_%s_audio_examen_fisico";
    public static final String NOMBRE_ARCHIVO_CONTROL_AUDIO_CLINICA = "control_medico_%s_audio_clinica";
    public static final String NOMBRE_ARCHIVO_CONTROL_AUDIO_ANEXO = "control_medico_%s_audio_anexo";
    public static final String NOMBRE_ARCHIVO_MOTIVO_CONSULTA_ENFERMERIA = "consulta_enfermeria_%s_motivo_consulta";

    public static final String SEPARADOR_DIRECTORIOS = "/";
    public static final String FORMATO_DIRECTORIO_ARCHIVO = "%s/%s";
    public static final String DIRECTORIO_TEMPORAL = "/data/user/0/com.telederma.cdflla/files/temp";
    public static final String DIRECTORIO_TEMPORAL_HISTORIA = "/data/user/0/com.telederma.cdflla/files/patologia";
    public static final String DIRECTORIO_PERMANENTE = Environment.getExternalStorageDirectory().getPath() + "/telederma";
    public static final String DIRECTORIO_PERMANENTE_VIDEOS = String.format("%s/%s", DIRECTORIO_PERMANENTE, "videos");
    public static final String DIRECTORIO_PERMANENTE_PDF = String.format("%s/%s", DIRECTORIO_PERMANENTE, "pdf");
    public static final String DIRECTORIO_PERMANENTE_LOGS = String.format("%s/%s", DIRECTORIO_PERMANENTE, "logs");
    public static final String EXTENSION_ARCHIVO_TEXTO = ".txt";
    public static final String ARCHIVO_LOG = "debug.log";
    public static final String EXTENSION_ARCHIVO_IMAGEN_JPG = ".jpg";
    public static final String EXTENSION_ARCHIVO_IMAGEN_PNG = ".png";

    public static final String RAW_FILE_TERMINOS_Y_CONDICIONES = "terminos_y_condiciones";
    public static final String RAW_FILE_PREGUNTAS_FRECUENTES = "preguntas_frecuentes";

    public static final long INTERVALO_TIEMPO_SINCRONIZACION = (65000);//65000 milisegundos : 1 minuto

    public static final Integer TIPO_PROFESIONAL_MEDICO = 1;
    public static final Integer TIPO_PROFESIONAL_ENFERMERA = 2;

    //visor imagenes
    public static int EXACT_SCREEN_HEIGHT;
    public static int EXACT_SCREEN_WIDTH;

}
