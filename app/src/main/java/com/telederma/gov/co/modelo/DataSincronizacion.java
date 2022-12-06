package com.telederma.gov.co.modelo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.telederma.gov.co.receivers.SincronizacionBroadcast;
import com.telederma.gov.co.tasks.UploadFileAmazon;
import com.telederma.gov.co.utils.DBUtil;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataSincronizacion {

    private static ISincronizable pendientes = null;
    private static DBHelper dbHelper;
    private static DBUtil dbUtil;
    private static String token;
    private static String email;
    private static Credencial credencial;

    /**
     * The shared preferences object for the application.
     */
    private SharedPreferences appSharedPrefs;

    /**
     * The shared preferences editor.
     */
    private Editor prefsEditor;

    private static final String APP_SHARED_PREFS = "pref_telederma";

    // User name (make variable public to access from outside)
    public static final String KEY_PREF = "proceso_usando_conexion";

    public DataSincronizacion() {
        dbHelper = OpenHelperManager.getHelper(
                TeledermaApplication.getInstance().getApplicationContext(), DBHelper.class);
        dbUtil = DBUtil.getInstance(dbHelper, SincronizacionBroadcast.context);
        credencial = dbUtil.getCredencial();

    }

    //
    synchronized public void sincronizar(Context... param) {

        Boolean hacerEnvio = false;
        appSharedPrefs = TeledermaApplication.getInstance().getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, TeledermaApplication.getInstance().getApplicationContext().MODE_PRIVATE);

        // Si existe una fecha de envio, es por que se esta enviando información
        String fechaEnvio = appSharedPrefs.getString(KEY_PREF, "");

        if (fechaEnvio.equals("")) {
            hacerEnvio = true;
        } else {
            hacerEnvio = puedoEnviarValidarFecha(fechaEnvio);
        }

        if (hacerEnvio) {

            //Toast.makeText(TeledermaApplication.getInstance().getApplicationContext(), com.telederma.gov.co.R.string.msj_error_timeout_reintentando, Toast.LENGTH_SHORT).show();

            Log.e("INICIANDO_SYNC", "INICIANDO_SINCRONIZACION");
            //if(HttpUtils.validarConexionApi())
            ArchivosSincronizacion sincro_archivo = get_sincronizable_archivo();
            if (sincro_archivo != null) {
                UploadFileAmazon.contexto = param[0];
                UploadFileAmazon.credencial = credencial;
                new UploadFileAmazon().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sincro_archivo);
            } else {
                ISincronizable sincronizable = get_sincronizable();
                if (sincronizable != null)
                    HttpUtils.configurarObservable(param[0], sincronizable.getObservable(token, email),
                            r -> sincronizable.nextAction(r),
                            r -> sincronizable.procesarExcepcionServicio(r)
                    );
            }
        } else {
            Log.e("PROCESO_OCUPADO", "PROCESO_OCUPADO");
        }


    }

    // metodo que se encarga de validar la fecha del ultimo bloqueo de envio, si es mayor a 2 minutos se quita el bloqueo
    public boolean puedoEnviarValidarFecha(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateBloqueo;
        try {
            dateBloqueo = format.parse(value);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateBloqueo); //tuFechaBase es un Date;
            calendar.add(Calendar.MINUTE, 20); //minutosASumar es int.

            dateBloqueo = calendar.getTime();
            System.out.println(dateBloqueo);

            Date currentTime = Calendar.getInstance().getTime();

            if (currentTime.getTime() > dateBloqueo.getTime()) {
                setFechaPreferences("");
                return true;
            } else {
                return false;
            }


        } catch (Exception e) {
            return true;
        }
    }

    public void setFechaPreferences(String value) {
        try {
            appSharedPrefs = TeledermaApplication.getInstance().getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, TeledermaApplication.getInstance().getApplicationContext().MODE_PRIVATE);
            prefsEditor = appSharedPrefs.edit();
            prefsEditor.putString(KEY_PREF, value);
            prefsEditor.commit();
        } catch (Exception e) {

        }
    }


    //Metodo llamado desde el LogAdapter
    synchronized public void sincronizar_registro(PendienteSincronizacion pendiente, Context... param) {
//        try {
//            HttpUtils.hayConexionApi();
//        } catch (IOException e) {
//            return;
//        }


        if (pendiente != null) {
            ISincronizable sincronizable = null;
            token = pendiente.getToken();
            email = pendiente.getEmail();
            switch (pendiente.getTable()) {
                case InformacionPaciente.NOMBRE_TABLA:
                    InformacionPaciente info = dbUtil.getInformacionPaciente(Integer.parseInt(pendiente.getId_local()));
                    info.setPaciente(dbUtil.getPaciente(info.getId_patient_local()));
                    sincronizable = info;
                    break;
                case ConsultaMedica.NOMBRE_TABLA:
                    sincronizable = dbUtil.getConsultaMedica(Integer.parseInt(pendiente.getId_local()));
                    break;
                case ConsultaEnfermeria.NOMBRE_TABLA:
                    sincronizable = dbUtil.getConsultaEnfermeria(Integer.parseInt(pendiente.getId_local()));
                    break;
                case ImagenLesion.NOMBRE_TABLA:
                    sincronizable = dbUtil.getImagenLesion(Integer.parseInt(pendiente.getId_local()));
                    break;
                case ImagenAnexo.NOMBRE_TABLA:
                    sincronizable = dbUtil.getImagenAnexo(Integer.parseInt(pendiente.getId_local()));
                    break;
                case Lesion.NOMBRE_TABLA:
                    sincronizable = dbUtil.getLesion(pendiente.getId_local());
                    break;
                case ControlMedico.NOMBRE_TABLA:
                    sincronizable = dbUtil.getControlMedico(pendiente.getId_local());
                    break;
                case ControlEnfermeria.NOMBRE_TABLA:
                    sincronizable = dbUtil.getControlEnfermeria(pendiente.getId_local());
                    break;
                case Requerimiento.NOMBRE_TABLA:
                    sincronizable = dbUtil.obtenerRequerimientoServidor(pendiente.getId_local()); // El idLocal es seteado desde la creacion con el id del servidor.
                    //return dbUtil.obtenerRequerimiento(pendiente.getId_local());
                    break;
                case DescartarRequerimiento.NOMBRE_TABLA:
                    // TODO: 3/28/19 Sebas P. Revisar metodo para devolver el registro correspondiente 
                    sincronizable = dbUtil.obtenerDescartarRequerimientoServidor(pendiente.getId_local());
                    break;
                case HelpDesk.NOMBRE_TABLA:
                    sincronizable = dbUtil.getMesaAyuda(pendiente.getId_local());
                    break;
                default:
                    sincronizable = null;
            }

            if (sincronizable != null) {
                ISincronizable finalSincronizable = sincronizable;
                ISincronizable finalSincronizable1 = sincronizable;
                HttpUtils.configurarObservable(param[0], sincronizable.getObservable(token, email),
                        r -> finalSincronizable.nextAction(r),
                        r -> finalSincronizable1.procesarExcepcionServicio(r)
                );
            }
        }
    }


    public ArchivosSincronizacion get_sincronizable_archivo() {
        ArchivosSincronizacion pendiente = dbUtil.obtenerSiguienteArchivo();
        return pendiente;
    }

    public void eliminarImagenLesion(List<ImagenLesion> imagenesLesiones) {
        dbUtil.eliminarImagenLesion(imagenesLesiones);
    }

    public void eliminarImagenAnexo(List<ImagenAnexo> imagenAnexos) {
        dbUtil.eliminarImagenAnexo(imagenAnexos);
    }

    public ISincronizable get_sincronizable() {
        PendienteSincronizacion pendiente = dbUtil.obtenerSiguienteSincronizable();
        if (pendiente != null) {
            token = pendiente.getToken();
            email = pendiente.getEmail();
            switch (pendiente.getTable()) {
                case InformacionPaciente.NOMBRE_TABLA:
                    InformacionPaciente info = dbUtil.getInformacionPaciente(Integer.parseInt(pendiente.getId_local()));
                    info.setPaciente(dbUtil.getPaciente(info.getId_patient_local()));
                    return info;
                case ConsultaMedica.NOMBRE_TABLA:
                    return dbUtil.getConsultaMedica(Integer.parseInt(pendiente.getId_local()));
                case ConsultaEnfermeria.NOMBRE_TABLA:
                    return dbUtil.getConsultaEnfermeria(Integer.parseInt(pendiente.getId_local()));
                case ImagenLesion.NOMBRE_TABLA:
                    return dbUtil.getImagenLesion(Integer.parseInt(pendiente.getId_local()));
                case ImagenAnexo.NOMBRE_TABLA:
                    return dbUtil.getImagenAnexo(Integer.parseInt(pendiente.getId_local()));
                case Lesion.NOMBRE_TABLA:
                    Lesion lesion = dbUtil.getLesion(pendiente.getId_local());
                    lesion.setIdBodyArea(182);
                    return lesion;
                case ControlMedico.NOMBRE_TABLA:
                    return dbUtil.getControlMedico(pendiente.getId_local());
                case ControlEnfermeria.NOMBRE_TABLA:
                    return dbUtil.getControlEnfermeria(pendiente.getId_local());
                case Requerimiento.NOMBRE_TABLA:
                    return dbUtil.obtenerRequerimientoServidor(pendiente.getId_local()); // El idLocal es seteado desde la creacion con el id del servidor.
                //return dbUtil.obtenerRequerimiento(pendiente.getId_local());
                case DescartarRequerimiento.NOMBRE_TABLA:
                    // TODO: 3/28/19 Sebas P. Revisar metodo para devolver el registro correspondiente
                    return dbUtil.obtenerDescartarRequerimientoServidor(pendiente.getId_local());
                case HelpDesk.NOMBRE_TABLA:
                    return dbUtil.getMesaAyuda(pendiente.getId_local());
                case Trazabilidad.NOMBRE_TABLA:
                    return dbUtil.getTrazabilidad(pendiente.getId_local());
            }
        }
        return null;
    }


    public void set_campo(ArchivosSincronizacion archivo, String value) {
        dbUtil.eliminarArchivoSincronizacion(archivo.getId());
        switch (archivo.getTable()) {
            case ImagenLesion.NOMBRE_TABLA:
                dbUtil.actualizarImagenLesion(archivo.getId_local(), archivo.getField(), value, "id");
                break;
            case ImagenAnexo.NOMBRE_TABLA:
                dbUtil.actualizarImagenAnexo(archivo.getId_local(), archivo.getField(), value, "id");
                break;
            case ConsultaMedica.NOMBRE_TABLA:
                dbUtil.actualizarConsulta(archivo.getId_local(), archivo.getField(), value, "id");
                break;
            case ConsultaEnfermeria.NOMBRE_TABLA:
                dbUtil.actualizarConsultaEnfermeria(archivo.getId_local(), archivo.getField(), value, "id");
                break;
            case ControlMedico.NOMBRE_TABLA:
                dbUtil.actualizarControl(archivo.getId_local(), archivo.getField(), value, "id");
                break;
            case Requerimiento.NOMBRE_TABLA:
                dbUtil.actualizarRequerimiento(archivo.getId_local(), archivo.getField(), value, Requerimiento.NOMBRE_CAMPO_ID_SERVIDOR);
                break;
            case ControlEnfermeria.NOMBRE_TABLA:
                dbUtil.actualizarControlEnfermeria(archivo.getId_local(), archivo.getField(), value, "id");
                break;
            case HelpDesk.NOMBRE_TABLA:
                dbUtil.actualizarMesaAyuda(archivo.getId_local(), archivo.getField(), value, "id");
                break;
        }

    }

    public void eliminarArchivoSincronizacion(Integer id) {
        dbUtil.eliminarArchivoSincronizacion(id);
    }

    public void actualizarArchivo(Integer id, String field, String value) {
        dbUtil.actualizarArchivoSincronizacion(id, field, value);
    }

    public void eliminarPendiente(Integer id, String name) {
        dbUtil.eliminarPendiente(id, name);
    }

    public void actualizarPendiente(Integer id, String field, String value) {
        dbUtil.actualizarPendienteSincronizacion(id, field, value);
    }


    public List<ConsultaMedica> getConsultaInformacion(int informacion_paciente) {
        List<ConsultaMedica> consultas = new ArrayList<>();
        try {
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDao = dbHelper.getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    ConsultaMedica.NOMBRE_CAMPO_ID_INFORMACION_PATIENT_LOCAL, informacion_paciente
            );
            return consultaDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public DBUtil getDbUtil() {
        return this.dbUtil;
    }


    public List<ConsultaEnfermeria> getConsultaEnfermeriaInformacion(int informacion_paciente) {
        List<ConsultaEnfermeria> consultas = new ArrayList<>();
        try {
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDao = dbHelper.getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    ConsultaEnfermeria.NOMBRE_CAMPO_ID_INFORMACION_PATIENT_LOCAL, informacion_paciente
            );
            return consultaDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public String[] getImagenesAnexo(String id_consulta, String id_control) {
        return dbUtil.getImagenesAnexo(id_consulta, id_control);
    }

    public List<ImagenAnexo> getImagenesAnexoObj(String id_consulta, String id_control) {
        return dbUtil.getImagenesAnexoObj(id_consulta, id_control);

    }


    public void actualizarCampo(String tabla, String campo, String valor, String id, String filter) {
        switch (tabla) {
            case ImagenLesion.NOMBRE_TABLA:
                dbUtil.actualizarImagenLesion(id, campo, valor, filter);
                break;
            case ImagenAnexo.NOMBRE_TABLA:
                dbUtil.actualizarImagenAnexo(id, campo, valor, filter);
                break;
            case ConsultaMedica.NOMBRE_TABLA:
                dbUtil.actualizarConsulta(id, campo, valor, filter);
                break;
            case ConsultaEnfermeria.NOMBRE_TABLA:
                dbUtil.actualizarConsultaEnfermeria(id, campo, valor, filter);
                break;
            case Lesion.NOMBRE_TABLA:
                dbUtil.actualizarLesion(id, campo, valor, filter);
                break;
            case ControlMedico.NOMBRE_TABLA:
                dbUtil.actualizarControl(id, campo, valor, filter);
                break;
            case ControlEnfermeria.NOMBRE_TABLA:
                dbUtil.actualizarControlEnfermeria(id, campo, valor, filter);
                break;
            case InformacionPaciente.NOMBRE_TABLA:
                dbUtil.actualizarInformacionPaciente(id, campo, valor, filter);
                break;
            case HelpDesk.NOMBRE_TABLA:
                dbUtil.actualizarMesaAyuda(id, campo, valor, filter);
                break;
        }
    }

    public void actualizarCampoFecha(String tabla, String campo, Date valor, String id, String filter) {
        switch (tabla) {
            case ImagenLesion.NOMBRE_TABLA:
                dbUtil.actualizarFechaImagenLesion(id, campo, valor, filter);
                break;
            case ImagenAnexo.NOMBRE_TABLA:
                dbUtil.actualizarFechaImagenAnexo(id, campo, valor, filter);
                break;
            case ConsultaMedica.NOMBRE_TABLA:
                dbUtil.actualizarFechaConsulta(id, campo, valor, filter);
                break;
            case ConsultaEnfermeria.NOMBRE_TABLA:
                dbUtil.actualizarFechaConsultaEnfermeria(id, campo, valor, filter);
                break;
            case Lesion.NOMBRE_TABLA:
                dbUtil.actualizarFechaLesion(id, campo, valor, filter);
                break;
            case ControlMedico.NOMBRE_TABLA:
                dbUtil.actualizarFechaControl(id, campo, valor, filter);
                break;
            case ControlEnfermeria.NOMBRE_TABLA:
                dbUtil.actualizarFechaControlEnfermeria(id, campo, valor, filter);
                break;
            case InformacionPaciente.NOMBRE_TABLA:
                dbUtil.actualizarFechaInformacionPaciente(id, campo, valor, filter);
                break;
            case HelpDesk.NOMBRE_TABLA:
                dbUtil.actualizarMesaAyudaFecha(id, campo, valor, filter);
                break;
        }
    }

    public List<ImagenLesion> getImagenesLesion(int lesion_id) {
        return dbUtil.getImagenesLesion(lesion_id);
    }


}
