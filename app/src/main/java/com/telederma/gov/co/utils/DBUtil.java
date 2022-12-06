package com.telederma.gov.co.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.Aseguradora;
import com.telederma.gov.co.modelo.BaseEntity;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.Credencial;
import com.telederma.gov.co.modelo.Departamento;
import com.telederma.gov.co.modelo.DescartarRequerimiento;
import com.telederma.gov.co.modelo.Diagnostico;
import com.telederma.gov.co.modelo.Especialista;
import com.telederma.gov.co.modelo.ExamenSolicitado;
import com.telederma.gov.co.modelo.Fallida;
import com.telederma.gov.co.modelo.Formula;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.modelo.ImagenAnexo;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.Mipres;
import com.telederma.gov.co.modelo.Municipio;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.modelo.RespuestaEspecialista;
import com.telederma.gov.co.modelo.Trazabilidad;
import com.telederma.gov.co.modelo.Usuario;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import static com.telederma.gov.co.utils.Constantes.TIPO_PROFESIONAL_ENFERMERA;
import static com.telederma.gov.co.utils.Constantes.TIPO_PROFESIONAL_MEDICO;
import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Created by Daniel Hernández on 9/08/2018.
 */

public final class DBUtil {

    private static DBUtil instance;
    private DBHelper dbHelper;

    public enum TipoConsulta { CONSULTA, CONTROL }

    //TODO cambiar por TeledermaException
    private BaseActivity contexto;

    private DBUtil() {
    }

    static public DBUtil getInstance(DBHelper dbHelper, Context contexto) {
        if (instance == null)
            instance = new DBUtil();

        instance.dbHelper = dbHelper;

        //TODO Machete!
        instance.contexto = (BaseActivity) contexto;

        return instance;
    }

    private DBHelper getDbHelper() {
        return dbHelper;
    }

    public List<Parametro> obtenerParametrosZona() {
        List<Parametro> parametros = new ArrayList<>();

        try {
            RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
            QueryBuilder<Parametro, Integer> queryBuilder = parametroDAO.queryBuilder();
            queryBuilder.where().eq(
                    Parametro.NOMBRE_CAMPO_TIPO, Parametro.TIPO_PARAMETRO_ZONA_URBANA
            );
            queryBuilder.orderBy(Parametro.NOMBRE_CAMPO_NOMBRE, false);
            parametros.addAll(parametroDAO.query(queryBuilder.prepare()));
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando parámetros " + "ZONA", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return parametros;
    }

    public List<Parametro> obtenerParametros(String nombreParametro) {
        List<Parametro> parametros = new ArrayList<>();

        try {
            RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
            QueryBuilder<Parametro, Integer> queryBuilder = parametroDAO.queryBuilder();
            queryBuilder.where().eq(
                    Parametro.NOMBRE_CAMPO_TIPO, nombreParametro
            );
            parametros.addAll(parametroDAO.query(queryBuilder.prepare()));
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando parámetros " + nombreParametro, e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return parametros;
    }

    public Parametro obtenerParametro(int idValor, String nombreParametro) {
        Parametro parametro = null;

        try {
            RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
            QueryBuilder<Parametro, Integer> queryBuilder = parametroDAO.queryBuilder();
            queryBuilder.where().eq(
                    Parametro.NOMBRE_CAMPO_VALOR, idValor
            ).and().eq(Parametro.NOMBRE_CAMPO_TIPO, nombreParametro);
            parametro = parametroDAO.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando parámetros " + idValor, e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return parametro;
    }

    public String obtenerNombreParametro(String tipo, Integer valor) {
        String nombre = null;

        try {
            RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
            QueryBuilder<Parametro, Integer> queryBuilder = parametroDAO.queryBuilder();
            queryBuilder.where().eq(
                    Parametro.NOMBRE_CAMPO_TIPO, tipo
            ).and().eq(
                    Parametro.NOMBRE_CAMPO_VALOR, valor
            );
            final List<Parametro> parametros = parametroDAO.query(queryBuilder.prepare());
            if (!parametros.isEmpty())
                nombre = parametros.get(0).getNombre();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    String.format("Error consultando nombre del parámetro %s %s", tipo, valor), e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return nombre;
    }

    public String obtenerNombresParametro(@NonNull String tipo, @NonNull String valores) {
        StringBuilder nombres = new StringBuilder();

        try {
            RuntimeExceptionDao<Parametro, Integer> parametroDAO = getDbHelper().getParametroRuntimeDAO();
            QueryBuilder<Parametro, Integer> queryBuilder = parametroDAO.queryBuilder();
            queryBuilder.where().eq(
                    Parametro.NOMBRE_CAMPO_TIPO, tipo
            ).and().in(
                    Parametro.NOMBRE_CAMPO_VALOR, Arrays.asList(valores.split(","))
            );

            final List<Parametro> parametros = parametroDAO.query(queryBuilder.prepare());
            Iterator<Parametro> i = parametros.iterator();
            Parametro parametro;

            while (i.hasNext()) {
                parametro = i.next();
                if (nombres.length() == 0)
                    nombres.append(parametro.getNombre());
                else
                    nombres.append(String.format(", %s", parametro.getNombre()));
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    String.format("Error consultando nombre del parámetro %s %s", tipo, valores), e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return nombres.toString();
    }

    // TODO: 2/18/19 Sebas - validar el return si es almacenado o no el parametro recibido
    public Boolean crearPendienteSincronizacion(PendienteSincronizacion pendiente) {
        RuntimeExceptionDao<PendienteSincronizacion, Integer> pendienteDAO = getDbHelper().getPendienteSincronizacionRuntimeDAO();
        QueryBuilder<PendienteSincronizacion, Integer> queryBuilder = pendienteDAO.queryBuilder();
        pendienteDAO.createOrUpdate(pendiente);
        return true;
    }

    // TODO: 2/18/19 Sebas - validar el return si es almacenado o no el parametro recibido
    public Boolean crearArchivoSincronizacion(ArchivosSincronizacion archivo) {
        RuntimeExceptionDao<ArchivosSincronizacion, Integer> archivoDAO = getDbHelper().getArchivosSincronizacionRuntimeDAO();
        QueryBuilder<ArchivosSincronizacion, Integer> queryBuilder = archivoDAO.queryBuilder();
        archivoDAO.createOrUpdate(archivo);
        return true;
    }


    public Boolean crearPaciente(Paciente paciente) {
        try {
            RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> queryBuilder = pacienteDAO.queryBuilder();
            int id_busqueda = ((paciente.getIdServidor() == null) ? 0 : paciente.getIdServidor());
            queryBuilder.where().eq(
                    Paciente.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<Paciente> pacienteBD = pacienteDAO.query(queryBuilder.prepare());

            if (!pacienteBD.isEmpty()) {
                paciente.setId(pacienteBD.get(0).getId());
            }
            pacienteDAO.createOrUpdate(paciente);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando paciente", e);
            return false;
        }
        return true;
    }



    public Boolean crearInformacionPaciente(InformacionPaciente informacionPaciente) {
        try {
            RuntimeExceptionDao<InformacionPaciente, Integer> informacionDAO = getDbHelper().getInformacionPacienteRuntimeDAO();
            QueryBuilder<InformacionPaciente, Integer> queryBuilder = informacionDAO.queryBuilder();
            int id_busqueda = ((informacionPaciente.getIdServidor() == null) ? 0 : informacionPaciente.getIdServidor());

            queryBuilder.where().eq(
                    InformacionPaciente.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<InformacionPaciente> informacionPacienteBD = informacionDAO.query(queryBuilder.prepare());

            if (!informacionPacienteBD.isEmpty()) {
                informacionPaciente.setId(informacionPacienteBD.get(0).getId());
            }
            informacionPaciente.setCompanion((informacionPaciente.getName_companion() == null || informacionPaciente.getName_companion().isEmpty()) ? false : true);
            informacionPaciente.setResponsible((informacionPaciente.getName_responsible() == null || informacionPaciente.getName_responsible().isEmpty()) ? false : true);
            informacionDAO.createOrUpdate(informacionPaciente);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando información paciente", e);
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean crearConsulta(Consulta consulta,Integer tab) {
        Boolean resultado = Boolean.FALSE;

        if(TIPO_PROFESIONAL_MEDICO.equals(consulta.getTipoProfesional())) {
            ConsultaMedica consultaMedica = consulta.getConsultaMedica();
            if(consultaMedica != null) {
                copiarDatosConsulta(consulta, consultaMedica);
                if (tab != 2)
                    consultaMedica.setArchivado(null);
                else
                    consultaMedica.setArchivado(true);
                resultado = crearConsultaMedica(consultaMedica);
            }
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(consulta.getTipoProfesional())) {

            ConsultaEnfermeria consultaEnfermeria = consulta.getConsultaEnfermeria();
            if(consultaEnfermeria != null) {
                copiarDatosConsulta(consulta, consultaEnfermeria);
                if (tab != 2)
                    consultaEnfermeria.setArchivado(null);
                else
                    consultaEnfermeria.setArchivado(true);
                resultado = crearConsultaEnfermeria(consultaEnfermeria);
            }
        }

        if (consulta.getRespuestaEspecialista() != null && !consulta.getRespuestaEspecialista().isEmpty())
            crearRespuestaEspecialista(consulta.getRespuestaEspecialista());
        if (consulta.getImagenesAnexo() != null && !consulta.getImagenesAnexo().isEmpty())
            crearImagenAnexo(consulta.getImagenesAnexo());
        if (consulta.getControles() != null && !consulta.getControles().isEmpty())
            crearControl(consulta.getTipoProfesional(), consulta.getControles());
        if (consulta.getLesiones() != null && !consulta.getLesiones().isEmpty())
            crearLesion(consulta.getLesiones());
        if (consulta.getRequerimientos() != null && !consulta.getRequerimientos().isEmpty())
            crearRequerimiento(consulta.getRequerimientos());

        return resultado;
    }
    public Boolean crearConsulta(Consulta consulta) {
        Boolean resultado = Boolean.FALSE;

        if(TIPO_PROFESIONAL_MEDICO.equals(consulta.getTipoProfesional())) {
            ConsultaMedica consultaMedica = consulta.getConsultaMedica();
            copiarDatosConsulta(consulta, consultaMedica);
            resultado = crearConsultaMedica(consultaMedica);
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(consulta.getTipoProfesional())) {
            ConsultaEnfermeria consultaEnfermeria = consulta.getConsultaEnfermeria();
            copiarDatosConsulta(consulta, consultaEnfermeria);
            resultado = crearConsultaEnfermeria(consultaEnfermeria);
        }

        if (consulta.getRespuestaEspecialista() != null && !consulta.getRespuestaEspecialista().isEmpty())
            crearRespuestaEspecialista(consulta.getRespuestaEspecialista());
        if (consulta.getImagenesAnexo() != null && !consulta.getImagenesAnexo().isEmpty())
            crearImagenAnexo(consulta.getImagenesAnexo());
        if (consulta.getControles() != null && !consulta.getControles().isEmpty())
            crearControl(consulta.getTipoProfesional(), consulta.getControles());
        if (consulta.getLesiones() != null && !consulta.getLesiones().isEmpty())
            crearLesion(consulta.getLesiones());
        if (consulta.getRequerimientos() != null && !consulta.getRequerimientos().isEmpty())
            crearRequerimiento(consulta.getRequerimientos());

        return resultado;
    }

    /**
     * Metodo para hacer copia de los datos de la clase Consulta sobre las entidades que la heredan
     *
     * @param consultaOrigen
     * @param consultaDestino
     */
    public void copiarDatosConsulta(Consulta consultaOrigen, Consulta consultaDestino) {
        consultaDestino.setImpresionDiagnostica(consultaOrigen.getImpresionDiagnostica());
        consultaDestino.setCiediezcode(consultaOrigen.getCiediezcode());
        consultaDestino.setArchivado(consultaOrigen.getArchivado());
        if(Boolean.TRUE.equals(consultaOrigen.getArchivado()))
            consultaDestino.setEstado(Consulta.ESTADO_CONSULTA_ARCHIVADO);

        consultaDestino.setCantidadControles(consultaOrigen.getCantidadControles());
        consultaDestino.setTratamiento(consultaOrigen.getTratamiento());
        consultaDestino.setFechaArchivado(consultaOrigen.getFechaArchivado());
        consultaDestino.setTipoProfesional(consultaOrigen.getTipoProfesional());
        consultaDestino.setAnexoDescripcion(consultaOrigen.getAnexoDescripcion());
        consultaDestino.setAnexoAudio(consultaOrigen.getAnexoAudio());
        consultaDestino.setIdPaciente(consultaOrigen.getIdPaciente());
        consultaDestino.setIdInformacionPaciente(consultaOrigen.getIdInformacionPaciente());
        consultaDestino.setEstado(consultaOrigen.getEstado());
        consultaDestino.setIdDoctor(consultaOrigen.getIdDoctor());
        consultaDestino.setIdEnfermera(consultaOrigen.getIdEnfermera());
        consultaDestino.setRespuestaEspecialista(consultaOrigen.getRespuestaEspecialista());
        consultaDestino.setLesiones(consultaOrigen.getLesiones());
        consultaDestino.setControles(consultaOrigen.getControles());
        consultaDestino.setRequerimientos(consultaOrigen.getRequerimientos());
        consultaDestino.setImagenesAnexo(consultaOrigen.getImagenesAnexo());
        consultaDestino.setRemission_comments(consultaOrigen.getRemission_comments());
        consultaDestino.setType_remission(consultaOrigen.getType_remission());
    }

    public Boolean crearConsultaMedica(ConsultaMedica consulta) {
        try {
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDAO.queryBuilder();
            int id_busqueda = ((consulta.getIdConsulta() == null) ? 0 : consulta.getIdConsulta());
            queryBuilder.where().eq(
                    ConsultaMedica.NOMBRE_CAMPO_ID_CONSULTA, id_busqueda
            );
            List<ConsultaMedica> consultaBD = consultaDAO.query(queryBuilder.prepare());

            if (!consultaBD.isEmpty())
                consulta.setId(consultaBD.get(0).getId());

            consultaDAO.createOrUpdate(consulta);
        } catch (Exception e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando consulta médica", e);

            return false;
        }

        return true;
    }

    public Boolean crearConsultaEnfermeria(ConsultaEnfermeria consulta) {
        try {
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaDAO.queryBuilder();
            int id_busqueda = ((consulta.getIdServidor() == null) ? 0 : consulta.getIdServidor());
            queryBuilder.where().eq(
                    ConsultaEnfermeria.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<ConsultaEnfermeria> consultaBD = consultaDAO.query(queryBuilder.prepare());

            if (!consultaBD.isEmpty())
                consulta.setId(consultaBD.get(0).getId());

            consultaDAO.createOrUpdate(consulta);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando consulta enfermería", e);

            return false;
        }

        return true;
    }

    public void crearControlMedico(List<ControlMedico> controles) {
        try {
            RuntimeExceptionDao<ControlMedico, Integer> controlMedicoDAO = getDbHelper().getControlMedicoRuntimeDAO();
            QueryBuilder<ControlMedico, Integer> controlMedicoQueryBuilder = controlMedicoDAO.queryBuilder();
            for (ControlMedico control : controles) {
                controlMedicoQueryBuilder.reset();
                controlMedicoQueryBuilder.where().eq(
                        ControlMedico.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, control.getIdControlConsulta()
                );
                List<ControlMedico> controlesBD = controlMedicoDAO.query(controlMedicoQueryBuilder.prepare());


                if (!controlesBD.isEmpty() && controlesBD.size() == 1)
                    control.setId(controlesBD.get(0).getId());

                if (control.getRespuestaEspecialista() != null && !control.getRespuestaEspecialista().isEmpty())
                    crearRespuestaEspecialista(control.getRespuestaEspecialista());
                if (control.getLesiones() != null && !control.getLesiones().isEmpty())
                    crearLesion(control.getLesiones());
                if (control.getRequerimientos() != null)
                    crearRequerimiento(control.getRequerimientos());
                if (control.getImagenesAnexo() != null && !control.getImagenesAnexo().isEmpty())
                    crearImagenAnexo(control.getImagenesAnexo());

                // TODO: 2/22/19 Sebas - Organizar cuando aun no tiene almancenado el controlconsulta.
                controlMedicoDAO.createOrUpdate(control);

            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando control médico", e);
        }

    }


    public Integer crearControlMedico(ControlMedico control) {
        try {
            RuntimeExceptionDao<ControlMedico, Integer> controlMedicoDAO = getDbHelper().getControlMedicoRuntimeDAO();
            QueryBuilder<ControlMedico, Integer> controlMedicoQueryBuilder = controlMedicoDAO.queryBuilder();
            controlMedicoQueryBuilder.reset();
            int id_busqueda = ((control.getIdServidor() == null) ? 0 : control.getIdServidor());
            controlMedicoQueryBuilder.where().eq(
                    ControlMedico.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<ControlMedico> controlesBD = controlMedicoDAO.query(controlMedicoQueryBuilder.prepare());

            if (!controlesBD.isEmpty() && controlesBD.size() == 1)
                control.setId(controlesBD.get(0).getId());
            controlMedicoDAO.createOrUpdate(control);
            return  control.getId();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando control médico", e);
            return null;
        }
    }

    public Integer crearControlEnfermeria(ControlEnfermeria control) {
        try {
            RuntimeExceptionDao<ControlEnfermeria, Integer> controlEnfermeriaDAO = getDbHelper().getControlEnfermeriaRuntimeDAO();
            QueryBuilder<ControlEnfermeria, Integer> controlEnfermeriaQueryBuilder = controlEnfermeriaDAO.queryBuilder();
            controlEnfermeriaQueryBuilder.reset();
            int id_busqueda = ((control.getIdServidor() == null) ? 0 : control.getIdServidor());
            controlEnfermeriaQueryBuilder.where().eq(
                    ControlEnfermeria.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<ControlEnfermeria> controlesBD = controlEnfermeriaDAO.query(controlEnfermeriaQueryBuilder.prepare());

            if (!controlesBD.isEmpty() && controlesBD.size() == 1)
                control.setId(controlesBD.get(0).getId());
            Dao.CreateOrUpdateStatus ia = controlEnfermeriaDAO.createOrUpdate(control);

            //return ia.getNumLinesChanged(); //Nota: Sebas - se comenta esta linea por inconsistencia. retorna el numero de lineas actualizadas, no el control.
            return control.getId();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando control enfermería", e);
            return null;
        }
    }

    public void crearControlEnfermeria(List<ControlEnfermeria> controles) {
        try {
            RuntimeExceptionDao<ControlEnfermeria, Integer> controlEnfermeriaDAO = getDbHelper().getControlEnfermeriaRuntimeDAO();
            QueryBuilder<ControlEnfermeria, Integer> controlEnfermeriaQueryBuilder = controlEnfermeriaDAO.queryBuilder();

            for (ControlEnfermeria control : controles) {
                controlEnfermeriaQueryBuilder.reset();
                controlEnfermeriaQueryBuilder.where().eq(
                        ControlEnfermeria.NOMBRE_CAMPO_ID_SERVIDOR, control.getIdServidor()
                );
                List<ControlEnfermeria> controlesBD = controlEnfermeriaDAO.query(controlEnfermeriaQueryBuilder.prepare());

                if (!controlesBD.isEmpty() && controlesBD.size() == 1)
                    control.setId(controlesBD.get(0).getId());

                controlEnfermeriaDAO.createOrUpdate(control);

                if (control.getImagenesAnexo() != null && !control.getImagenesAnexo().isEmpty())
                    crearImagenAnexo(control.getImagenesAnexo());
                if (control.getRespuestaEspecialista() != null && !control.getRespuestaEspecialista().isEmpty())
                    crearRespuestaEspecialista(control.getRespuestaEspecialista());
                if (control.getLesiones() != null && !control.getLesiones().isEmpty())
                    crearLesion(control.getLesiones());
                if (control.getRequerimientos() != null )
                    crearRequerimiento(control.getRequerimientos());
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando control enfermería", e);
        }
    }

    public void crearLesion(Collection<Lesion> lesiones) {
        try {
            RuntimeExceptionDao<Lesion, Integer> lesionDAO = getDbHelper().getLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> lesionQueryBuilder = lesionDAO.queryBuilder();

            for (Lesion lesion : lesiones) {
                lesionQueryBuilder.reset();
                lesionQueryBuilder.where().eq(
                        Lesion.NOMBRE_CAMPO_ID_SERVIDOR, lesion.getIdServidor()
                );
                List<Lesion> lesionBD = lesionDAO.query(lesionQueryBuilder.prepare());

                if (!lesionBD.isEmpty() && lesionBD.size() == 1) {
                    lesion.setId(lesionBD.get(0).getId());
                }
                lesionDAO.createOrUpdate(lesion);

                if (lesion.getImagenesLesion() != null && !lesion.getImagenesLesion().isEmpty())
                    crearImagenLesion(lesion.getImagenesLesion());
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando lesión", e);
        }
    }

    public Integer crearControl(ControlConsulta controlConsulta) {
        Integer resultado = -1;

        if(TIPO_PROFESIONAL_MEDICO.equals(controlConsulta.getTipoProfesional())) {
            ControlMedico controlMedico = controlConsulta.getControlMedico();
            copiarDatosControl(controlConsulta, controlMedico);
            resultado = crearControlMedico(controlMedico);
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(controlConsulta.getTipoProfesional())) {
            ControlEnfermeria controlEnfermeria = controlConsulta.getControlEnfermeria();
            copiarDatosControl(controlConsulta, controlEnfermeria);
            resultado = crearControlEnfermeria(controlEnfermeria);
        }

        return resultado;
    }

    public void crearControl(Integer tipoProfesional, List<ControlConsulta> controlesConsulta) {
        //tipo de profesional: {enfermeria=2}
        final List<ControlMedico> controlesMedicos = new ArrayList<>();
        final List<ControlEnfermeria> controlesEnfermeria = new ArrayList<>();
        final Iterator<ControlConsulta> i = controlesConsulta.iterator();

        ControlConsulta controlConsulta;
        ControlMedico controlMedico;
        ControlEnfermeria controlEnfermeria;

        while(i.hasNext()){
            controlConsulta = i.next();
            if(controlConsulta.getControlMedico() != null) {
                controlMedico = controlConsulta.getControlMedico();
                copiarDatosControl(controlConsulta, controlMedico);
                controlesMedicos.add(controlMedico);
            } else if(controlConsulta.getControlEnfermeria() != null) {
                controlEnfermeria = controlConsulta.getControlEnfermeria();
                copiarDatosControl(controlConsulta, controlEnfermeria);
                controlesEnfermeria.add(controlEnfermeria);
            }

            // TODO: 4/10/19 vaerificar antes de crear doctor y crear enfermera
            if(obtenerUsuarioLogueado(controlConsulta.getIdDoctor()) == null && obtenerUsuarioLogueado(controlConsulta.getIdEnfermera()) == null) {
                if(controlConsulta.getIdDoctor() > 0) {
                    Usuario doctor = Usuario.fillNewUser(controlConsulta.getDoctor(), 1);
                    crearUsuario(doctor);
                }else if(controlConsulta.getIdEnfermera() > 0) {
                    Usuario enfermera = Usuario.fillNewUser(controlConsulta.getNurse(), 2);
                    crearUsuario(enfermera);
                }
            }
        }

        crearControlMedico(controlesMedicos);
        crearControlEnfermeria(controlesEnfermeria);
    }

    /**
     * Metodo para hacer copia de los datos de la clase Consulta sobre las entidades que la heredan
     *
     * @param controlOrigen
     * @param controlDestino
     */
    private void copiarDatosControl(ControlConsulta controlOrigen, ControlConsulta controlDestino) {
        controlDestino.setIdConsulta(controlOrigen.getIdConsulta());
        controlDestino.setTipoProfesional(controlOrigen.getTipoProfesional());
        controlDestino.setEstado(controlOrigen.getEstado());
        controlDestino.setRespuestaEspecialista(controlOrigen.getRespuestaEspecialista());
        controlDestino.setLesiones(controlOrigen.getLesiones());
        controlDestino.setRequerimientos(controlOrigen.getRequerimientos());
        controlDestino.setImagenesAnexo(controlOrigen.getImagenesAnexo());
        controlDestino.setTratamiento(controlOrigen.getTratamiento());
        controlDestino.setTipo_remision(controlOrigen.getTipo_remision());
        controlDestino.setComentario_remision(controlOrigen.getComentario_remision());
    }

    public void crearImagenLesion(List<ImagenLesion> imagenesLesion) {
        try {
            RuntimeExceptionDao<ImagenLesion, Integer> imagenLesionDAO = getDbHelper().getImagenLesionRuntimeDAO();
            QueryBuilder<ImagenLesion, Integer> imagenLesionQueryBuilder = imagenLesionDAO.queryBuilder();

            for (ImagenLesion imagenLesion : imagenesLesion) {
                imagenLesionQueryBuilder.reset();
                imagenLesionQueryBuilder.where().eq(
                        ImagenLesion.NOMBRE_CAMPO_ID_SERVIDOR, imagenLesion.getIdServidor()
                );
                List<ImagenLesion> imagenLesionBD = imagenLesionDAO.query(imagenLesionQueryBuilder.prepare());

                if (!imagenLesionBD.isEmpty() && imagenLesionBD.size() == 1) {
                    imagenLesion.setId(imagenLesionBD.get(0).getId());
                }
                imagenLesionDAO.createOrUpdate(imagenLesion);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando imagen lesión", e);
        }
    }

    public Boolean crearImagenLesion(ImagenLesion imagen) {
        try {
            RuntimeExceptionDao<ImagenLesion, Integer> imagenDAO = getDbHelper().getImagenLesionRuntimeDAO();
            QueryBuilder<ImagenLesion, Integer> queryBuilder = imagenDAO.queryBuilder();
            int id_busqueda = ((imagen.getIdServidor() == null) ? 0 : imagen.getIdServidor());
            queryBuilder.where().eq(
                    ImagenLesion.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<ImagenLesion> imageDB = imagenDAO.query(queryBuilder.prepare());

            if (!imageDB.isEmpty()) {
                imagen.setId(imageDB.get(0).getId());
            }
            imagenDAO.createOrUpdate(imagen);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando imagen lesión", e);
            return false;
        }
        return true;
    }

    public Integer crearImagenAnexo(ImagenAnexo imagen) {
        try {
            RuntimeExceptionDao<ImagenAnexo, Integer> imagenDAO = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> queryBuilder = imagenDAO.queryBuilder();
            int id_busqueda = ((imagen.getIdServidor()==null)?0:imagen.getIdServidor());
            queryBuilder.where().eq(
                    ImagenAnexo.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<ImagenAnexo> imageDB = imagenDAO.query(queryBuilder.prepare());

            if (!imageDB.isEmpty()) {
                imagen.setId(imageDB.get(0).getId());
            }
            imagenDAO.createOrUpdate(imagen);
            return imagen.getId();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando imagen anexo", e);
            return null;
        }
    }

    public void crearImagenAnexo(List<ImagenAnexo> imagenesAnexo) {
        try {
            RuntimeExceptionDao<ImagenAnexo, Integer> imagenAnexoDAO = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> imagenAnexoQueryBuilder = imagenAnexoDAO.queryBuilder();

            for (ImagenAnexo imagen : imagenesAnexo) {
                imagenAnexoQueryBuilder.reset();
                imagenAnexoQueryBuilder.where().eq(
                        ImagenAnexo.NOMBRE_CAMPO_ID_SERVIDOR, imagen.getIdServidor()
                );
                List<ImagenAnexo> imagenesAnexoBD = imagenAnexoDAO.query(imagenAnexoQueryBuilder.prepare());

                if (!imagenesAnexoBD.isEmpty() && imagenesAnexoBD.size() == 1) {
                    imagen.setId(imagenesAnexoBD.get(0).getId());
                }

                imagenAnexoDAO.createOrUpdate(imagen);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando imagen anexo", e);
        }
    }

    public Boolean crearLesion(Lesion lesion) {
        try {
            RuntimeExceptionDao<Lesion, Integer> lesionDAO = getDbHelper().getLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> queryBuilder = lesionDAO.queryBuilder();
            int id_busqueda = ((lesion.getIdServidor() == null) ? 0 : lesion.getIdServidor());
            queryBuilder.where().eq(
                    Lesion.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<Lesion> imageDB = lesionDAO.query(queryBuilder.prepare());

            if (!imageDB.isEmpty()) {
                lesion.setId(imageDB.get(0).getId());
            }

            lesionDAO.createOrUpdate(lesion);
        } catch (SQLException e) {
            Log.e(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando lesión", e);
            return false;
        }
        return true;
    }

    public Integer crearLesionId(Lesion lesion) {
        try {
            RuntimeExceptionDao<Lesion, Integer> lesionDAO = getDbHelper().getLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> queryBuilder = lesionDAO.queryBuilder();
            int id_busqueda = ((lesion.getIdServidor() == null) ? 0 : lesion.getIdServidor());
            queryBuilder.where().eq(
                    Lesion.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<Lesion> imageDB = lesionDAO.query(queryBuilder.prepare());

            if (!imageDB.isEmpty()) {
                lesion.setId(imageDB.get(0).getId());
            }

            lesionDAO.createOrUpdate(lesion);
            // TODO: 2/13/19 retornar el ID

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando lesión", e);
            return 0;
        }
        return lesion.getId();
    }

    private void crearRespuestaEspecialista(List<RespuestaEspecialista> respuestaEspecialista) {
        if (respuestaEspecialista != null && !respuestaEspecialista.isEmpty()) {
            Iterator<RespuestaEspecialista> i = respuestaEspecialista.iterator();
            RespuestaEspecialista respuesta;

            while (i.hasNext()) {
                respuesta = i.next();

                crearRespuestaEspecialista(respuesta);

                if (respuesta.getDiagnosticos() != null && !respuesta.getDiagnosticos().isEmpty())
                    for (Diagnostico entidad : respuesta.getDiagnosticos())
                        crearDiagnostico(entidad);

                if (respuesta.getEspecialista() != null)
                    crearEspecialista(respuesta.getEspecialista());

                if (respuesta.getExamenesSolicitados() != null && !respuesta.getExamenesSolicitados().isEmpty())
                    for (ExamenSolicitado entidad : respuesta.getExamenesSolicitados())
                        crearExamenSolicitado(entidad);

                if (respuesta.getFormulas() != null && !respuesta.getFormulas().isEmpty())
                    for (Formula entidad : respuesta.getFormulas())
                        crearFormula(entidad);

                if (respuesta.getMipres() != null && !respuesta.getMipres().isEmpty())
                    for (Mipres entidad : respuesta.getMipres())
                        crearMipres(entidad);
            }
        }
    }

    public void crearDiagnostico(Diagnostico entidad) {
        try {
            RuntimeExceptionDao<Diagnostico, Integer> dao = getDbHelper().getDiagnosticoRuntimeDAO();
            QueryBuilder<Diagnostico, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    BaseEntity.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor()
            );
            List<? extends BaseEntity> entidadDB = dao.query(queryBuilder.prepare());
            if (!entidadDB.isEmpty()) {
                entidad.setId(entidadDB.get(0).getId());
            }
            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando diagnóstico", e);
        }
    }

    public void crearEspecialista(Especialista entidad) {
        try {
            RuntimeExceptionDao<Especialista, Integer> dao = getDbHelper().getEspecialistaRuntimeDAO();
            QueryBuilder<Especialista, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    BaseEntity.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor()
            );
            List<? extends BaseEntity> entidadDB = dao.query(queryBuilder.prepare());
            if (!entidadDB.isEmpty()) {
                entidad.setId(entidadDB.get(0).getId());
            }
            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando especialista", e);
        }
    }

    public void crearExamenSolicitado(ExamenSolicitado entidad) {
        try {
            RuntimeExceptionDao<ExamenSolicitado, Integer> dao = getDbHelper().getExamenSolicitadoRuntimeDAO();
            QueryBuilder<ExamenSolicitado, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    BaseEntity.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor()
            );
            List<? extends BaseEntity> entidadDB = dao.query(queryBuilder.prepare());
            if (!entidadDB.isEmpty()) {
                entidad.setId(entidadDB.get(0).getId());
            }
            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando examen solicitado", e);
        }
    }

    public void crearFormula(Formula entidad) {
        try {
            RuntimeExceptionDao<Formula, Integer> dao = getDbHelper().getFormulaRuntimeDAO();
            QueryBuilder<Formula, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    BaseEntity.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor()
            );
            List<? extends BaseEntity> entidadDB = dao.query(queryBuilder.prepare());
            if (!entidadDB.isEmpty()) {
                entidad.setId(entidadDB.get(0).getId());
            }
            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando fórmula", e);
        }
    }


    public void crearRequerimiento(Requerimiento entidad) {
        try {
            RuntimeExceptionDao<Requerimiento, Integer> requerimientoDAO = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> requerimientoQueryBuilder = requerimientoDAO.queryBuilder();

            requerimientoQueryBuilder.reset();
            requerimientoQueryBuilder.where().eq(Requerimiento.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor());
            List<Requerimiento> requerimientosBD = requerimientoDAO.query(requerimientoQueryBuilder.prepare());

            if (!requerimientosBD.isEmpty())
                entidad.setId(requerimientosBD.get(0).getId());
            requerimientoDAO.createOrUpdate(entidad);

        } catch (Exception e) {
            String l = e.getMessage();
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando requerimiento", e);

        }
    }

    public void crearDescartarRequerimiento(DescartarRequerimiento entidad) {
        try {
            RuntimeExceptionDao<DescartarRequerimiento, Integer> drDAO = getDbHelper().getDescartarRequerimientoRuntimeDAO();
            QueryBuilder<DescartarRequerimiento, Integer> drQueryBuilder = drDAO.queryBuilder();

            drQueryBuilder.reset();
            drQueryBuilder.where().eq(DescartarRequerimiento.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor());
            List<DescartarRequerimiento> drBD = drDAO.query(drQueryBuilder.prepare());

            if (!drBD.isEmpty())
                entidad.setId(drBD.get(0).getId());
            drDAO.createOrUpdate(entidad);

        } catch (Exception e) {
            String l = e.getMessage();
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando descartar requerimiento", e);
        }
    }

    public void crearRequerimiento(Collection<Requerimiento> requerimientos) {
        try {
            RuntimeExceptionDao<Requerimiento, Integer> requerimientoDAO = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> requerimientoQueryBuilder = requerimientoDAO.queryBuilder();

            for (Requerimiento requerimiento : requerimientos) {
                requerimientoQueryBuilder.reset();
                requerimientoQueryBuilder.where().eq(
                        Requerimiento.NOMBRE_CAMPO_ID_SERVIDOR, requerimiento.getIdServidor()
                );
                List<Requerimiento> requerimientosBD = requerimientoDAO.query(requerimientoQueryBuilder.prepare());

                if (!requerimientosBD.isEmpty() && requerimientosBD.size() == 1)
                    requerimiento.setId(requerimientosBD.get(0).getId());
                if(requerimiento.getIdDoctor() != null) {
                    Usuario doctor = this.obtenerUsuarioLogueado(requerimiento.getIdDoctor());
                    if (doctor == null && requerimiento.getIdDoctor() != null) {
                        try{
                            doctor = Usuario.fillNewUser(requerimiento.getDoctor(), 1);
                            this.crearUsuario(doctor);
                        } catch (Exception e){

                        }

                    }
                }
                requerimientoDAO.createOrUpdate(requerimiento);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando requerimiento", e);
        }
    }

    public void crearMipres(Mipres entidad) {
        try {
            RuntimeExceptionDao<Mipres, Integer> dao = getDbHelper().getMipresRuntimeDAO();
            QueryBuilder<Mipres, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    BaseEntity.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor()
            );
            List<? extends BaseEntity> entidadDB = dao.query(queryBuilder.prepare());

            if (!entidadDB.isEmpty())
                entidad.setId(entidadDB.get(0).getId());

            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando mipres", e);
        }
    }

    public void crearRespuestaEspecialista(RespuestaEspecialista entidad) {
        try {
            RuntimeExceptionDao<RespuestaEspecialista, Integer> dao = getDbHelper().getRespuestaEspecialistaRuntimeDAO();
            QueryBuilder<RespuestaEspecialista, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    BaseEntity.NOMBRE_CAMPO_ID_SERVIDOR, entidad.getIdServidor()
            );
            List<? extends BaseEntity> entidadDB = dao.query(queryBuilder.prepare());
            if (!entidadDB.isEmpty()) {
                entidad.setId(entidadDB.get(0).getId());
            }
            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando respuesta especialista", e);
        }
    }

    public ConsultaMedica obtenerConsultaMedicina(Integer idServidorConsulta) {
        ConsultaMedica consulta = null;

        try {
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> consultaQueryBuilder = consultaDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    ConsultaMedica.NOMBRE_CAMPO_ID_CONSULTA, idServidorConsulta
            );
            List<ConsultaMedica> consultas = consultaDAO.query(consultaQueryBuilder.prepare());

            if (!consultas.isEmpty()) {
                consulta = consultas.get(0);
                consulta.setImagenesAnexo(obtenerImagenesAnexo(TipoConsulta.CONSULTA, consulta.getIdConsulta()));
                consulta.setRequerimientos(obtenerRequerimientos(TipoConsulta.CONSULTA, consulta.getIdConsulta()));
                consulta.setRespuestaEspecialista(obtenerRespuestaEspecialista(consulta.getIdConsulta()));
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo las consultas", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return consulta;
    }

    public ConsultaMedica obtenerConsultaFromLocal(Integer consulta_id) {
        ConsultaMedica consulta = new ConsultaMedica();
        try {
            if (consulta_id == null)
                consulta_id = 0;
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDAO.queryBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            List<ConsultaMedica> list_consultas = consultaDAO.query(queryBuilder.prepare());
            if (!list_consultas.isEmpty()) {
                consulta = list_consultas.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consulta;
    }

    public ConsultaEnfermeria obtenerConsultaEnfermeria(Integer idServidorConsulta) {
        ConsultaEnfermeria consulta = null;

        try {
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> consultaQueryBuilder = consultaDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    ConsultaEnfermeria.NOMBRE_CAMPO_ID_CONSULTA, idServidorConsulta
            );
            List<ConsultaEnfermeria> consultas = consultaDAO.query(consultaQueryBuilder.prepare());

            if (!consultas.isEmpty()) {
                consulta = consultas.get(0);
                consulta.setImagenesAnexo(obtenerImagenesAnexo(TipoConsulta.CONSULTA, consulta.getIdConsulta()));
                consulta.setRequerimientos(obtenerRequerimientos(TipoConsulta.CONSULTA, consulta.getIdConsulta()));
                consulta.setRespuestaEspecialista(obtenerRespuestaEspecialista(consulta.getIdConsulta()));
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo las consultas", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return consulta;
    }

    public List<ConsultaMedica> obtenerConsultasMedicasPaciente(int idServidorInfoPaciente) {
        List<ConsultaMedica> consultas = new ArrayList<>();

        try {
            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> consultaQueryBuilder = consultaDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    ConsultaMedica.NOMBRE_CAMPO_ID_PACIENTE, idServidorInfoPaciente
            );
            // ORDEN DE CONSULTAS POR FECHA DE CREACION DESCENDENTEMENTE
            consultaQueryBuilder.orderBy(ConsultaMedica.NOMBRE_CAMPO_CREATED_AT, false);
            Iterator<ConsultaMedica> i = consultaDAO.query(consultaQueryBuilder.prepare()).iterator();
            while (i.hasNext()) {
                ConsultaMedica consulta = i.next();
                consulta.setControlesMedicos(obtenerControlesMedicosConsultaBD(consulta.getIdConsulta()));
                //consulta.setControlesMedicos(obtenerControlesMedicosConsultaBD(consulta.getIdServidor()));
                consultas.add(consulta);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    "Ha ocurrido un error obteniendo las consultas por paciente", e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return consultas;
    }

    public List<ConsultaEnfermeria> obtenerConsultasEnfermeriaPaciente(int idServidorInfoPaciente) {
        List<ConsultaEnfermeria> consultas = new ArrayList<>();

        try {
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> consultaQueryBuilder = consultaDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    ConsultaEnfermeria.NOMBRE_CAMPO_ID_INFORMACION_PACIENTE, idServidorInfoPaciente
            );
            consultaQueryBuilder.orderBy(ConsultaEnfermeria.NOMBRE_CAMPO_CREATED_AT, false);
            Iterator<ConsultaEnfermeria> i = consultaDAO.query(consultaQueryBuilder.prepare()).iterator();
            while (i.hasNext()) {
                ConsultaEnfermeria consulta = i.next();
                consulta.setControlesEnfermeria(obtenerControlesEnfermeriaConsultaBD(consulta.getIdServidor()));
                consultas.add(consulta);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    "Ha ocurrido un error obteniendo las consultas por paciente", e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return consultas;
    }

    private void llenarControlesMedicos(List<ControlMedico> controles) {
        if (!controles.isEmpty()) {
            for(ControlMedico control : controles) {
                //Nota: Sebas - valida que control.getIdControlConsulta() no este null para mostrar solo info sincronizada
                if (control.getIdControlConsulta() != null) {
                    control.setLesiones(obtenerLesiones(TipoConsulta.CONTROL, control.getIdControlConsulta()));
                    control.setRespuestaEspecialista(obtenerRespuestaEspecialista(
                            TipoConsulta.CONTROL, control.getIdControlConsulta()
                    ));
                    List<Requerimiento> list_requerimientos = obtenerRequerimientos(
                            TipoConsulta.CONTROL, control.getIdControlConsulta()
                    );

                    if (list_requerimientos.size() > 0)
                        control.setRequerimientos(list_requerimientos);
                    control.setImagenesAnexo(obtenerImagenesAnexo(
                            TipoConsulta.CONTROL, control.getIdControlConsulta()
                    ));
                }
            }
        }
    }

    private void llenarControlesEnfermeria(List<ControlEnfermeria> controles) {
        if (!controles.isEmpty()) {
            ControlEnfermeria control = controles.get(0);
            if(control.getIdControlConsulta() != null) {
                control.setLesiones(obtenerLesiones(TipoConsulta.CONTROL, control.getIdServidor()));
                control.setRespuestaEspecialista(obtenerRespuestaEspecialista(
                        TipoConsulta.CONTROL, control.getIdControlConsulta()
                ));

                List<Requerimiento> list_requerimientos = obtenerRequerimientos(
                        TipoConsulta.CONTROL, control.getIdControlConsulta()
                );
                if (list_requerimientos.size() > 0)
                    control.setRequerimientos(list_requerimientos);
                control.setImagenesAnexo(obtenerImagenesAnexo(
                        TipoConsulta.CONTROL, control.getIdControlConsulta()
                ));
            }
        }
    }


    public List<Diagnostico> obtenerDiagnosticos(RespuestaEspecialista respuestaEspecialista) {
        List<Diagnostico> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<Diagnostico, Integer> dao = getDbHelper().getDiagnosticoRuntimeDAO();
            QueryBuilder<Diagnostico, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Diagnostico.NOMBRE_CAMPO_ID_RESPUESTA, respuestaEspecialista.getIdServidor()
            );
            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los diagnósticos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public Usuario obtenerDoctor(int idUser) {

        try {
            RuntimeExceptionDao<Usuario, Integer> dao = getDbHelper().getUsuarioRuntimeDAO();
            QueryBuilder<Usuario, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Usuario.NOMBRE_CAMPO_ID_SERVIDOR, idUser
            );

            return dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo el especialista", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return null;
    }



    public Usuario obtenerEnfermera(int idUser) {

        try {
            RuntimeExceptionDao<Usuario, Integer> dao = getDbHelper().getUsuarioRuntimeDAO();
            QueryBuilder<Usuario, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Usuario.NOMBRE_CAMPO_ID_SERVIDOR, idUser
            );

            return dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo el especialista", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return null;
    }

    public Especialista obtenerEspecialista(int idServidorEspecialista) {
        try {
            RuntimeExceptionDao<Especialista, Integer> dao = getDbHelper().getEspecialistaRuntimeDAO();
            QueryBuilder<Especialista, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Especialista.NOMBRE_CAMPO_ID_SERVIDOR, idServidorEspecialista
            );
            final List<Especialista> resultado = dao.query(qb.prepare());
            if (!resultado.isEmpty())
                return resultado.get(0);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo el especialista", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return null;
    }

    public List<ExamenSolicitado> obtenerExamenesSolicitados(RespuestaEspecialista respuestaEspecialista) {
        List<ExamenSolicitado> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<ExamenSolicitado, Integer> dao = getDbHelper().getExamenSolicitadoRuntimeDAO();
            QueryBuilder<ExamenSolicitado, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    ExamenSolicitado.NOMBRE_CAMPO_ID_RESPUESTA, respuestaEspecialista.getIdServidor()
            );
            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los exámenes solicitados", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public List<Formula> obtenerFormulas(RespuestaEspecialista respuestaEspecialista) {
        List<Formula> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<Formula, Integer> dao = getDbHelper().getFormulaRuntimeDAO();
            QueryBuilder<Formula, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Formula.NOMBRE_CAMPO_ID_RESPUESTA, respuestaEspecialista.getIdServidor()
            );
            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo las fórmulas", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public List<Mipres> obtenerMipres(RespuestaEspecialista respuestaEspecialista) {
        List<Mipres> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<Mipres, Integer> dao = getDbHelper().getMipresRuntimeDAO();
            QueryBuilder<Mipres, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Mipres.NOMBRE_CAMPO_ID_RESPUESTA, respuestaEspecialista.getIdServidor()
            );
            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los mipres", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public List<RespuestaEspecialista> obtenerRespuestaEspecialista(Integer idServidorConsulta) {
        List<RespuestaEspecialista> resultado = new ArrayList<>();
        RespuestaEspecialista respuesta;

        try {
            RuntimeExceptionDao<RespuestaEspecialista, Integer> dao = getDbHelper().getRespuestaEspecialistaRuntimeDAO();
            QueryBuilder<RespuestaEspecialista, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    RespuestaEspecialista.NOMBRE_CAMPO_ID_CONSULTA, idServidorConsulta
            ).and().isNull(RespuestaEspecialista.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            resultado = dao.query(qb.prepare());

            if (!resultado.isEmpty()) {
                respuesta = resultado.get(0);


                respuesta.setEspecialista(obtenerEspecialista(respuesta.getIdEspecialista()));
                respuesta.setDiagnosticos(obtenerDiagnosticos(respuesta));
                respuesta.setExamenesSolicitados(obtenerExamenesSolicitados(respuesta));
                respuesta.setFormulas(obtenerFormulas(respuesta));
                respuesta.setMipres(obtenerMipres(respuesta));
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo respuestas de especialista", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public List<ImagenAnexo> obtenerImagenesAnexo(TipoConsulta tipoConsulta, Integer idServidor) {
        List<ImagenAnexo> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<ImagenAnexo, Integer> dao = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> qb = dao.queryBuilder();

            if(TipoConsulta.CONSULTA.equals(tipoConsulta))
                qb.where().eq(ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA, idServidor)
                        .and().isNull(ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            else if(TipoConsulta.CONTROL.equals(tipoConsulta))
                qb.where().eq(ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, idServidor);

            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo las imágenes anexo", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public Requerimiento obtenerRequerimientoControl(String idServidorConsulta) {
        Requerimiento resultado = null;

        try {
            RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Requerimiento.NOMBRE_CAMPO_ID_SERVIDOR, idServidorConsulta
            );
            resultado = dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return resultado;
    }

    public Requerimiento obtenerRequerimiento(String idServidorConsulta) {
        Requerimiento resultado = null;

        try {
            RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    Requerimiento.NOMBRE_CAMPO_ID_SERVIDOR, idServidorConsulta
            ).and().isNull(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            resultado = dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return resultado;
    }

    /*
     * Author: Sebastian Pérez
     * Description: Obitiene el requerimiento de la BD local por id
     * */
    public Requerimiento obtenerRequerimientoServidor(String idServer) {
        Requerimiento resultado = null;

        try {
            RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> qb = dao.queryBuilder();
            //qb.where().idEq(Integer.parseInt(idLocalConsulta));
            qb.where().eq(
                    Requerimiento.NOMBRE_CAMPO_ID_SERVIDOR, idServer
            );//.and().isNull(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            resultado = dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return resultado;
    }

    /*
     * Author: Sebastian Pérez
     * Description: Obitiene el requerimiento de la BD local por id
     * */
    public DescartarRequerimiento obtenerDescartarRequerimientoServidor(String idServidor) {
        DescartarRequerimiento resultado = null;

        try {
            RuntimeExceptionDao<DescartarRequerimiento, Integer> dao = getDbHelper().getDescartarRequerimientoRuntimeDAO();
            QueryBuilder<DescartarRequerimiento, Integer> qb = dao.queryBuilder();
            //qb.where().idEq(Integer.parseInt(idLocalConsulta));
            qb.where().eq(
                    DescartarRequerimiento.NOMBRE_CAMPO_ID_SERVIDOR, idServidor
            );//.and().isNull(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            resultado = dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return resultado;
    }

    /*
     * Author: Sebastian Pérez
     * Description: Obitiene el requerimiento de la BD local por id
     * */
    public DescartarRequerimiento obtenerDescartarRequerimientoByRequerimientoIdServidor(String id) {
        DescartarRequerimiento resultado = null;

        try {
            RuntimeExceptionDao<DescartarRequerimiento, Integer> dao = getDbHelper().getDescartarRequerimientoRuntimeDAO();
            QueryBuilder<DescartarRequerimiento, Integer> qb = dao.queryBuilder();
            //qb.where().idEq(Integer.parseInt(idLocalConsulta));
            qb.where().eq(
                    DescartarRequerimiento.NOMBRE_CAMPO_ID_REQUERIMIENTO, id
            );//.and().isNull(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            resultado = dao.queryForFirst(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return resultado;
    }

    public List<RespuestaEspecialista> obtenerRespuestaEspecialista(TipoConsulta tipoConsulta, Integer idServidor) {
        List<RespuestaEspecialista> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<RespuestaEspecialista, Integer> dao = getDbHelper().getRespuestaEspecialistaRuntimeDAO();
            QueryBuilder<RespuestaEspecialista, Integer> qb = dao.queryBuilder();

            if(TipoConsulta.CONSULTA.equals(tipoConsulta))
                qb.where().eq(RespuestaEspecialista.NOMBRE_CAMPO_ID_CONSULTA, idServidor)
                        .and().isNull(RespuestaEspecialista.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            else if(TipoConsulta.CONTROL.equals(tipoConsulta))
                qb.where().eq(RespuestaEspecialista.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, idServidor);

            resultado = dao.query(qb.prepare());
            if(resultado!= null)
            {
                for(RespuestaEspecialista respuesta : resultado ) {
                    respuesta.setEspecialista(obtenerEspecialista(respuesta.getIdEspecialista()));
                    respuesta.setDiagnosticos(obtenerDiagnosticos(respuesta));
                    respuesta.setExamenesSolicitados(obtenerExamenesSolicitados(respuesta));
                    respuesta.setFormulas(obtenerFormulas(respuesta));
                    respuesta.setMipres(obtenerMipres(respuesta));
                }
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo respuestas de especialista", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public List<Requerimiento> obtenerRequerimientos(TipoConsulta tipoConsulta, Integer idServidor) {
        List<Requerimiento> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> qb = dao.queryBuilder();

            if(TipoConsulta.CONSULTA.equals(tipoConsulta))
                qb.where().eq(Requerimiento.NOMBRE_CAMPO_ID_CONSULTA, idServidor)
                        .and().isNull(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            else if(TipoConsulta.CONTROL.equals(tipoConsulta))
                qb.where().eq(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, idServidor);

            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public Paciente obtenerPaciente(Integer idServidorPaciente) {
        Paciente paciente = null;

        try {
            RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> consultaQueryBuilder = pacienteDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    Paciente.NOMBRE_CAMPO_ID_SERVIDOR, idServidorPaciente
            );
            List<Paciente> pacientes = pacienteDAO.query(consultaQueryBuilder.prepare());

            if (!pacientes.isEmpty()) {
                paciente = pacientes.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los pacientes", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return paciente;
    }

    public Paciente obtenerPacienteFromIdLocal(Integer idLocal) {
        Paciente paciente = null;

        try {
            RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> consultaQueryBuilder = pacienteDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    "id", idLocal
            );
            List<Paciente> pacientes = pacienteDAO.query(consultaQueryBuilder.prepare());

            if (!pacientes.isEmpty()) {
                paciente = pacientes.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo paciente", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return paciente;
    }

    public InformacionPaciente obtenerInformacionPacienteFomIdLocal(Integer idLocal) {
        InformacionPaciente informacionPaciente = null;

        try {
            RuntimeExceptionDao<InformacionPaciente, Integer> informacionPacienteDAO = getDbHelper().getInformacionPacienteRuntimeDAO();
            QueryBuilder<InformacionPaciente, Integer> consultaQueryBuilder = informacionPacienteDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    "id", idLocal
            );
            List<InformacionPaciente> informacionPacientes = informacionPacienteDAO.query(consultaQueryBuilder.prepare());

            if (!informacionPacientes.isEmpty()) {
                informacionPaciente = informacionPacientes.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo la información paciente", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return informacionPaciente;
    }



    public Paciente obtenerPacienteConsulta(Integer tipoProfesional, Integer idServidorConsulta) {
        Paciente paciente = null;

        try {
            RuntimeExceptionDao<Paciente, Integer> pacienteDAO = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> consultaQueryBuilder = pacienteDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    Paciente.NOMBRE_CAMPO_ID_SERVIDOR, obtenerIdPacienteConsulta(tipoProfesional, idServidorConsulta)
            );
            List<Paciente> pacientes = pacienteDAO.query(consultaQueryBuilder.prepare());

            if (!pacientes.isEmpty())
                paciente = pacientes.get(0);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los pacientes", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return paciente;
    }

    public Integer obtenerIdPacienteConsulta(Integer tipoProfesional, Integer idServidorConsulta) {
        Integer idPaciente = -1;
        RuntimeExceptionDao dao = null;
        String nombreTabla = "", campoIdConsulta = "";

        if(TIPO_PROFESIONAL_MEDICO.equals(tipoProfesional)) {
            dao = getDbHelper().getConsultaMedicaRuntimeDAO();
            nombreTabla = ConsultaMedica.NOMBRE_TABLA;
            campoIdConsulta = ConsultaMedica.NOMBRE_CAMPO_ID_CONSULTA;
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(tipoProfesional)) {
            dao = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            nombreTabla = ConsultaEnfermeria.NOMBRE_TABLA;
            campoIdConsulta = ConsultaEnfermeria.NOMBRE_CAMPO_ID_CONSULTA;
        }

        try {
            GenericRawResults<String[]> rawResults = dao.queryRaw(String.format(
                    "SELECT %s FROM %s WHERE %s = %d",
                    Consulta.NOMBRE_CAMPO_ID_PACIENTE,
                    nombreTabla,
                    campoIdConsulta,
                    idServidorConsulta));
            idPaciente = Integer.valueOf(rawResults.getFirstResult()[0]);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo id del paciente", e);
        }

        return idPaciente;
    }

    public InformacionPaciente obtenerInformacionPacienteBD(Integer idServidorInfoPaciente) {
        InformacionPaciente infoPaciente = null;

        try {
            RuntimeExceptionDao<InformacionPaciente, Integer> infoPacienteDAO = getDbHelper().getInformacionPacienteRuntimeDAO();
            QueryBuilder<InformacionPaciente, Integer> consultaQueryBuilder = infoPacienteDAO.queryBuilder();
            consultaQueryBuilder.where().eq(
                    InformacionPaciente.NOMBRE_CAMPO_ID_SERVIDOR, idServidorInfoPaciente
            );
            List<InformacionPaciente> infoPacientes = infoPacienteDAO.query(consultaQueryBuilder.prepare());

            if (!infoPacientes.isEmpty()) {
                infoPaciente = infoPacientes.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los pacientes", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return infoPaciente;
    }

    public List<ControlMedico> obtenerControlesMedicosConsultaBD(Integer idServidorConsulta) {
        List<ControlMedico> controlesMedicos = new ArrayList<>();

        try {
            RuntimeExceptionDao<ControlMedico, Integer> controlDAO = getDbHelper().getControlMedicoRuntimeDAO();
            QueryBuilder<ControlMedico, Integer> consultaQueryBuilder = controlDAO.queryBuilder();
            consultaQueryBuilder.orderBy(ControlMedico.NOMBRE_CAMPO_CREATED_AT, false);
            consultaQueryBuilder.where().eq(
                    ControlMedico.NOMBRE_CAMPO_ID_CONSULTA, idServidorConsulta
            );

            controlesMedicos = controlDAO.query(consultaQueryBuilder.prepare());
            llenarControlesMedicos(controlesMedicos);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los controles médicos por consultaMedicina", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return controlesMedicos;
    }

    public List<ControlEnfermeria> obtenerControlesEnfermeriaConsultaBD(Integer idServidorConsulta) {
        List<ControlEnfermeria> controlesEnfermeria = new ArrayList<>();

        try {
            RuntimeExceptionDao<ControlEnfermeria, Integer> controlDAO = getDbHelper().getControlEnfermeriaRuntimeDAO();
            QueryBuilder<ControlEnfermeria, Integer> consultaQueryBuilder = controlDAO.queryBuilder();
            consultaQueryBuilder.orderBy(ControlEnfermeria.NOMBRE_CAMPO_CREATED_AT, false);
//            Nota: Sebas - Se comentó este método por que genera una exepción error,
//                          en otras partes de la App cuando el campo IdControlConsulta esta vacío.
//            consultaQueryBuilder.where().eq(
//                    ControlEnfermeria.NOMBRE_CAMPO_ID_CONSULTA, idServidorConsulta
//            );
            consultaQueryBuilder.where().eq(
                    ControlEnfermeria.NOMBRE_CAMPO_ID_CONSULTA, idServidorConsulta
            );//.and().isNotNull(ControlEnfermeria.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);

            controlesEnfermeria = controlDAO.query(consultaQueryBuilder.prepare());
            llenarControlesEnfermeria(controlesEnfermeria);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los controles médicos por consultaMedicina", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return controlesEnfermeria;
    }

    public List<Lesion> obtenerLesiones(TipoConsulta tipoConsulta, Integer idServidor) {
        List<Lesion> lesiones = new ArrayList<>();

        try {
            RuntimeExceptionDao<Lesion, Integer> lesionDAO = getDbHelper().getLesionRuntimeDAO();
            RuntimeExceptionDao<ImagenLesion, Integer> imagenLesionDAO = getDbHelper().getImagenLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> lesionQueryBuilder = lesionDAO.queryBuilder();

            if(TipoConsulta.CONSULTA.equals(tipoConsulta))
                lesionQueryBuilder.where().eq(Lesion.NOMBRE_CAMPO_ID_CONSULTA, idServidor)
                        .and().isNull(Lesion.NOMBRE_CAMPO_ID_CONTROL_CONSULTA);
            else if(TipoConsulta.CONTROL.equals(tipoConsulta))
                lesionQueryBuilder.where().eq(Lesion.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, idServidor);

            lesiones.addAll(lesionDAO.query(lesionQueryBuilder.prepare()));

            final Iterator<Lesion> i = lesiones.iterator();
            while (i.hasNext()) {
                final Lesion lesion = i.next();
                // note: Sebas - validacion
                if(lesion.getIdServidor() != null) {
                    QueryBuilder<ImagenLesion, Integer> imagenLesionQueryBuilder = imagenLesionDAO.queryBuilder();
                    imagenLesionQueryBuilder.where().eq(
                            ImagenLesion.NOMBRE_CAMPO_INJURY_ID, lesion.getIdServidor()
                    ).and().isNotNull(ImagenLesion.NOMBRE_CAMPO_ID_SERVIDOR);
                    lesion.setImagenesLesion(new ArrayList<>());
                    lesion.getImagenesLesion().addAll(imagenLesionDAO.query(imagenLesionQueryBuilder.prepare()));
                }
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo las lesiones por consultaMedicina", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return lesiones;
    }

    public ParteCuerpo obtenerParteDelCuerpo(String idParte) {
        ParteCuerpo parteCuerpo = null;

        try {
            RuntimeExceptionDao<ParteCuerpo, Integer> parteCuerpoDAO = getDbHelper().getParteCuerpoRuntimeDAO();
            QueryBuilder<ParteCuerpo, Integer> queryBuilder = parteCuerpoDAO.queryBuilder();
            queryBuilder.where().eq(
                    ParteCuerpo.NOMBRE_CAMPO_ID_SERVIDOR, idParte
            );
            List<ParteCuerpo> listParte = parteCuerpoDAO.query(queryBuilder.prepare());
            if (listParte.size() > 0)
                parteCuerpo = listParte.get(0);

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Error consultando partes del cuerpo " + idParte, e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return parteCuerpo;
    }
    ///aca
    public void eliminarConsulta(Integer tipoProfesional, List<Consulta> consultas) {
        for (Consulta consulta : consultas) {
            eliminarControlesConsulta(consulta.getTipoProfesional(), consulta.getIdServidor());
            if (!consulta.getIdServidor().equals(null) && consulta.getLesiones() != null && !consulta.getLesiones().isEmpty())
                eliminarLesion(consulta.getLesiones());

            try {
                if(consulta.getRespuestaEspecialista() != null && !consulta.getRespuestaEspecialista().isEmpty())
                    eliminarRespuestaEspecialista(
                            consulta.getIdServidor(),
                            consulta.getRespuestaEspecialista().get(0).getIdServidor()
                    );
                eliminarImagenAnexoConsulta(consulta.getIdServidor());

            } catch (SQLException e) {
                Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando consulta", e);
            }
        }

        if(TIPO_PROFESIONAL_MEDICO.equals(tipoProfesional)) {
            RuntimeExceptionDao<ConsultaMedica, Integer> dao = getDbHelper().getConsultaMedicaRuntimeDAO();
            final Iterator<Consulta> i = consultas.iterator();
            while (i.hasNext())
                dao.deleteById(i.next().getId());
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(tipoProfesional)) {
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> dao = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            final Iterator<Consulta> i = consultas.iterator();
            while (i.hasNext())
                dao.deleteById(i.next().getId());
        }
    }

    public void eliminarControlesConsulta(Integer tipoProfesional, Integer idConsulta) {
        if(TIPO_PROFESIONAL_MEDICO.equals(tipoProfesional)) {
            RuntimeExceptionDao<ControlMedico, Integer> dao = getDbHelper().getControlMedicoRuntimeDAO();

            DeleteBuilder<ControlMedico, Integer> queryBuilder = dao.deleteBuilder();
            try {
                queryBuilder.where().eq(ControlConsulta.NOMBRE_CAMPO_ID_CONSULTA, idConsulta).and().isNotNull(ControlConsulta.NOMBRE_CAMPO_ID_SERVIDOR);
                queryBuilder.delete();
            } catch (SQLException e) {
                Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando controles consulta", e);
            }
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(tipoProfesional)) {
            RuntimeExceptionDao<ControlEnfermeria, Integer> dao = getDbHelper().getControlEnfermeriaRuntimeDAO();

            DeleteBuilder<ControlEnfermeria, Integer> queryBuilder = dao.deleteBuilder();
            try {
                queryBuilder.where().eq(ControlConsulta.NOMBRE_CAMPO_ID_CONSULTA, idConsulta).and().isNotNull(ControlConsulta.NOMBRE_CAMPO_ID_SERVIDOR);
                queryBuilder.delete();
            } catch (SQLException e) {
                Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando controles consulta", e);
            }
        }
    }

    public void eliminarLesion(Collection<Lesion> lesiones) {
        for (Lesion lesion : lesiones)
            if (lesion.getImagenesLesion() != null && !lesion.getImagenesLesion().isEmpty())
                eliminarImagenLesion(lesion.getImagenesLesion());

        RuntimeExceptionDao<Lesion, Integer> lesionDAO = getDbHelper().getLesionRuntimeDAO();
        lesionDAO.delete(lesiones);
    }


    public void eliminarRequerimientoControl(Integer idControl) {

        RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();

        DeleteBuilder<Requerimiento, Integer> queryBuilder = dao.deleteBuilder();
        try {
            queryBuilder.where().eq(Requerimiento.NOMBRE_CAMPO_ID_CONTROL_CONSULTA, idControl);
            queryBuilder.delete();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando controles consulta", e);
        }
    }

    public void eliminarRequerimientoConsulta(Integer consulta) {

        RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();

        DeleteBuilder<Requerimiento, Integer> queryBuilder = dao.deleteBuilder();
        try {
            queryBuilder.where().eq(Requerimiento.NOMBRE_CAMPO_ID_CONSULTA, consulta);
            queryBuilder.delete();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando controles consulta", e);
        }
    }

    public void eliminarControl(Integer tipoProfesional, List<ControlConsulta> controles) {
        for (ControlConsulta control : controles)
            if (control.getLesiones() != null && !control.getLesiones().isEmpty())
                eliminarLesion(control.getLesiones());

        if(TIPO_PROFESIONAL_MEDICO.equals(tipoProfesional)) {
            RuntimeExceptionDao<ControlMedico, Integer> dao = getDbHelper().getControlMedicoRuntimeDAO();
            final Iterator<ControlConsulta> i = controles.iterator();
            while(i.hasNext())
                dao.deleteById(i.next().getId());
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(tipoProfesional)) {
            RuntimeExceptionDao<ControlEnfermeria, Integer> dao = getDbHelper().getControlEnfermeriaRuntimeDAO();
            final Iterator<ControlConsulta> i = controles.iterator();
            while(i.hasNext())
                dao.deleteById(i.next().getId());
        }
    }

    private void eliminarRespuestaEspecialista(Integer idConsulta, Integer idRespuesta) throws SQLException {
        RuntimeExceptionDao<Diagnostico, Integer> diagnosticoDAO = getDbHelper().getDiagnosticoRuntimeDAO();
        RuntimeExceptionDao<ExamenSolicitado, Integer> examenSolicitadoDAO = getDbHelper().getExamenSolicitadoRuntimeDAO();
        RuntimeExceptionDao<Formula, Integer> formulaDAO = getDbHelper().getFormulaRuntimeDAO();
        RuntimeExceptionDao<Requerimiento, Integer> requerimientoDAO = getDbHelper().getRequerimientoRuntimeDAO();

        DeleteBuilder<Diagnostico, Integer> dbDiagnostico = diagnosticoDAO.deleteBuilder();
        DeleteBuilder<ExamenSolicitado, Integer> dbExamenSolicitado = examenSolicitadoDAO.deleteBuilder();
        DeleteBuilder<Formula, Integer> dbFormula = formulaDAO.deleteBuilder();
        DeleteBuilder<Requerimiento, Integer> dbRequerimiento = requerimientoDAO.deleteBuilder();

        dbDiagnostico.where().eq(Diagnostico.NOMBRE_CAMPO_ID_RESPUESTA, idRespuesta);
        dbDiagnostico.delete();
        dbExamenSolicitado.where().eq(ExamenSolicitado.NOMBRE_CAMPO_ID_RESPUESTA, idRespuesta);
        dbExamenSolicitado.delete();
        dbFormula.where().eq(Formula.NOMBRE_CAMPO_ID_RESPUESTA, idRespuesta);
        dbFormula.delete();
        dbRequerimiento.where().eq(Requerimiento.NOMBRE_CAMPO_ID_CONSULTA, idConsulta);
        dbRequerimiento.delete();
    }


    public void eliminarImagenLesion(List<ImagenLesion> imagenesLesiones) {
        List<ImagenLesion> il = imagenesLesiones;
        RuntimeExceptionDao<ImagenLesion, Integer> imagenLesionDAO = getDbHelper().getImagenLesionRuntimeDAO();
        for (int i = 0; i < il.size(); i++) {
            ImagenLesion imagen = il.get(i);
            if (imagen.getIdServidor() == (null)) {
                imagenesLesiones.remove(i);
            }
        }
        if(imagenesLesiones != null && imagenesLesiones.size() > 0)
            imagenLesionDAO.delete(imagenesLesiones);
    }

    public void eliminarImagenAnexo(List<ImagenAnexo> imagenesAnexo) {
        List<ImagenAnexo> list_ia = imagenesAnexo;
        RuntimeExceptionDao<ImagenAnexo, Integer> imagenAnexoDAO = getDbHelper().getImagenAnexoRuntimeDAO();
        for (int i = 0; i < imagenesAnexo.size(); i++) {
            ImagenAnexo imagen = imagenesAnexo.get(i);
            if (imagen.getIdServidor() == null)
                list_ia.remove(i);
        }
        if(list_ia.size() > 0)
            imagenAnexoDAO.delete(list_ia);
    }

    //    public void eliminarImagenAnexo(List<ImagenAnexo> imagenesAnexo) {
//        List<ImagenAnexo> list_ia = new ArrayList<>();
//        RuntimeExceptionDao<ImagenAnexo, Integer> imagenAnexoDAO = getDbHelper().getImagenAnexoRuntimeDAO();
//        for (int i = 0; i < imagenesAnexo.size(); i++) {
//            ImagenAnexo imagen = imagenesAnexo.get(i);
//            if (imagen.getIdServidor() == null)
//                list_ia.add(imagen);
//        }
//        if(list_ia.size() > 0)
//            imagenAnexoDAO.delete(list_ia);
//    }
    public void eliminarImagenAnexoConsulta(Integer idConsulta) {
        try
        {
            RuntimeExceptionDao<ImagenAnexo, Integer> dao_imagen = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> queryBuilder_imagen_anexo = dao_imagen.queryBuilder();
            queryBuilder_imagen_anexo.where().eq(
                    ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA, idConsulta
            );

            List<ImagenAnexo> dataDB_anexo = dao_imagen.query(queryBuilder_imagen_anexo.prepare());

            for(ImagenAnexo i : dataDB_anexo)
            {
                RuntimeExceptionDao<PendienteSincronizacion, Integer> dao_ = getDbHelper().getPendienteSincronizacionRuntimeDAO();
                QueryBuilder<PendienteSincronizacion, Integer> queryBuilder_pendiente = dao_.queryBuilder();
                queryBuilder_pendiente.where().eq(
                        PendienteSincronizacion.NOMBRE_CAMPO_ID_, PendienteSincronizacion.ESTADO_SINCRONIZANDO
                );
                final PendienteSincronizacion dataDB = dao_.queryForFirst(queryBuilder_pendiente.prepare());

                if(dataDB== null)
                {
                    RuntimeExceptionDao<ImagenAnexo, Integer> imagenDAO = getDbHelper().getImagenAnexoRuntimeDAO();
                    imagenDAO.delete(i);
                }
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando imagen anexo", e);
        }



    }


    public void crearUsuario(Usuario usuario) {
        try {
            RuntimeExceptionDao<Usuario, Integer> usuarioDAO = getDbHelper().getUsuarioRuntimeDAO();
            QueryBuilder<Usuario, Integer> queryBuilder = usuarioDAO.queryBuilder();
            queryBuilder.where().eq(
                    Usuario.NOMBRE_CAMPO_ID_SERVIDOR, usuario.getIdServidor()
            );
            List<Usuario> usuarioBD = usuarioDAO.query(queryBuilder.prepare());

            if (!usuarioBD.isEmpty())
                usuario.setId(usuarioBD.get(0).getId());

            usuarioDAO.createOrUpdate(usuario);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error guardando usuario", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    public Usuario obtenerUsuarioLogueado(Integer idUsuario) {
        try {
            RuntimeExceptionDao<Usuario, Integer> usuarioDAO = getDbHelper().getUsuarioRuntimeDAO();
            QueryBuilder<Usuario, Integer> queryBuilder = usuarioDAO.queryBuilder();
            queryBuilder.where().eq(
                    Usuario.NOMBRE_CAMPO_ID_SERVIDOR, idUsuario
            );
            List<Usuario> usuarioBD = usuarioDAO.query(queryBuilder.prepare());

            if (!usuarioBD.isEmpty())
                return usuarioBD.get(0);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error obteniendo usuario", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return null;
    }

    public String obtenerNombreAseguradora(Integer id) {
        String nombre = null;

        try {
            RuntimeExceptionDao<Aseguradora, Integer> dao = getDbHelper().getAseguradoraRuntimeDAO();
            QueryBuilder<Aseguradora, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    Aseguradora.NOMBRE_CAMPO_ID_SERVIDOR, id
            );
            final List<Aseguradora> dataDB = dao.query(queryBuilder.prepare());
            if (!dataDB.isEmpty())
                nombre = dataDB.get(0).getName();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    String.format("Error consultando nombre de la aseguradora %d", id), e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return nombre;
    }

    public String obtenerNombreDepartamento(Integer id) {
        String nombre = null;

        try {
            RuntimeExceptionDao<Departamento, Integer> dao = getDbHelper().getDepartamentoRuntimeDAO();
            QueryBuilder<Departamento, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    Departamento.NOMBRE_CAMPO_ID_SERVIDOR, id
            );
            final List<Departamento> dataDB = dao.query(queryBuilder.prepare());
            if (!dataDB.isEmpty())
                nombre = dataDB.get(0).getNombre();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    String.format("Error consultando nombre del departamento %d", id), e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return nombre;
    }

    public Municipio obtenerMunicipio(Integer id) {
        Municipio entity = null;

        try {
            RuntimeExceptionDao<Municipio, Integer> dao = getDbHelper().getMunicipioRuntimeDAO();
            QueryBuilder<Municipio, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    Municipio.NOMBRE_CAMPO_ID_SERVIDOR, id
            );
            final List<Municipio> dataDB = dao.query(queryBuilder.prepare());
            if (!dataDB.isEmpty())
                entity = dataDB.get(0);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY,
                    String.format("Error consultando el municipio %d", id), e
            );
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return entity;
    }


    public List<PendienteSincronizacion> obtenerSincronizables() {
        try {
            if(verificarArchivosPendientes())
                return  null;
            RuntimeExceptionDao<PendienteSincronizacion, Integer> dao = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> qb = dao.queryBuilder();
            return qb.query();

        } catch (SQLException e) {
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return null;
    }

    public PendienteSincronizacion obtenerSiguienteSincronizable() {
        try {
            if(verificarArchivosPendientes())
                return  null;
            RuntimeExceptionDao<PendienteSincronizacion, Integer> dao = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    PendienteSincronizacion.NOMBRE_CAMPO_STATUS, PendienteSincronizacion.ESTADO_SINCRONIZANDO
            );
            final PendienteSincronizacion dataDB = dao.queryForFirst(queryBuilder.prepare());

            if (dataDB != null) {
                if(dataDB.getIdServidor() != null)
                    return dataDB;
            }

            queryBuilder.where().eq(
                    PendienteSincronizacion.NOMBRE_CAMPO_STATUS, PendienteSincronizacion.ESTADO_PENDIENTE
            );
            final List<PendienteSincronizacion> dataDB2 = dao.query(queryBuilder.prepare());
            if (!dataDB2.isEmpty()) {
                dataDB2.get(0).setStatus(PendienteSincronizacion.ESTADO_SINCRONIZANDO);
                dao.createOrUpdate(dataDB2.get(0));
                return dataDB2.get(0);
            }

            // sino tiene registros en estado de pendiente y sincronizando sin eliminar
            if (dataDB != null) {
                if(dataDB.getIdServidor() == null)
                    return dataDB;
            }
        } catch (SQLException e) {
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return null;
    }

    public ArchivosSincronizacion obtenerSiguienteArchivo() {

        ArchivosSincronizacion dataDB = null;
        try {
            RuntimeExceptionDao<ArchivosSincronizacion, Integer> dao = getDbHelper().getArchivosSincronizacionRuntimeDAO();
            QueryBuilder<ArchivosSincronizacion, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    ArchivosSincronizacion.NOMBRE_CAMPO_STATUS, ArchivosSincronizacion.ESTADO_SINCRONIZANDO
            );
            ArchivosSincronizacion list = dao.queryForFirst(queryBuilder.prepare());
            if (list != null )
                return list;

            queryBuilder.where().eq(
                    ArchivosSincronizacion.NOMBRE_CAMPO_STATUS, ArchivosSincronizacion.ESTADO_PENDIENTE
            );
            dataDB = dao.queryForFirst(queryBuilder.prepare());
            if (dataDB != null)
                dataDB.setStatus(ArchivosSincronizacion.ESTADO_SINCRONIZANDO);
            dao.createOrUpdate(dataDB);
        } catch (SQLException e) {
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return dataDB;
    }

    public Boolean verificarArchivosPendientes() {
        ArchivosSincronizacion dataDB = null;
        try {
            RuntimeExceptionDao<ArchivosSincronizacion, Integer> dao = getDbHelper().getArchivosSincronizacionRuntimeDAO();
            QueryBuilder<ArchivosSincronizacion, Integer> queryBuilder = dao.queryBuilder();
            dataDB = dao.queryForFirst(queryBuilder.prepare());
            if (dataDB != null)
                return true ;
        } catch (SQLException e) {
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
        return false;
    }

    public Boolean actualizarArchivoSincronizacion(Integer id, String field, String value) {
        RuntimeExceptionDao<ArchivosSincronizacion, Integer> archivoSincronizacionDAO = getDbHelper().getArchivosSincronizacionRuntimeDAO();
        UpdateBuilder<ArchivosSincronizacion, Integer> queryBuilder = archivoSincronizacionDAO.updateBuilder();
        try {
            queryBuilder.where().eq("id", id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando archivo sincronización", e);
        }
        return true;
    }

    public Boolean actualizarPendienteSincronizacion(Integer id, String field, String value) {
        RuntimeExceptionDao<PendienteSincronizacion, Integer> pendienteDAO = getDbHelper().getPendienteSincronizacionRuntimeDAO();
        UpdateBuilder<PendienteSincronizacion, Integer> queryBuilder = pendienteDAO.updateBuilder();
        try {
            queryBuilder.where().eq("id", id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando pendiente sincronización", e);
        }
        return true;
    }

    public Boolean actualizarConsulta(String id, String field, String value, String filter) {
        RuntimeExceptionDao<ConsultaMedica, Integer> consultaMedicaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
        UpdateBuilder<ConsultaMedica, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
//            if(field.equals(ConsultaMedica.NOMBRE_CAMPO_CREATED_AT)) {
//                queryBuilder.updateColumnValue(field, value);
//            }else
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando consulta", e);
        }
        return true;
    }

    public Boolean archivarConsulta(String id, Boolean value, String filter) {
        RuntimeExceptionDao<ConsultaMedica, Integer> consultaMedicaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
        UpdateBuilder<ConsultaMedica, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(ConsultaMedica.NOMBRE_CAMPO_ARCHIVADO, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error archivando consulta", e);
        }
        return true;
    }

    public Boolean actualizarFechaConsulta(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<ConsultaMedica, Integer> consultaMedicaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
        UpdateBuilder<ConsultaMedica, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando consulta", e);
        }
        return true;
    }

    public Boolean actualizarConsultaEnfermeria(String id, String field, String value, String filter) {
        RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaMedicaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
        UpdateBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando consulta enfermería", e);
        }
        return true;
    }

    public Boolean actualizarFechaConsultaEnfermeria(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaMedicaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
        UpdateBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando consulta enfermería", e);
        }
        return true;
    }


    public Boolean actualizarControl(String id, String field, String value, String filter) {
        RuntimeExceptionDao<ControlMedico, Integer> consultaMedicaDAO = getDbHelper().getControlMedicoRuntimeDAO();
        UpdateBuilder<ControlMedico, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando control", e);
        }
        return true;
    }

    public Boolean actualizarFechaControl(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<ControlMedico, Integer> consultaMedicaDAO = getDbHelper().getControlMedicoRuntimeDAO();
        UpdateBuilder<ControlMedico, Integer> queryBuilder = consultaMedicaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando control", e);
        }
        return true;
    }

    public Boolean actualizarInformacionPaciente(String id, String field, String value, String filter) {
        RuntimeExceptionDao<InformacionPaciente, Integer> informacionPacienteDAO = getDbHelper().getInformacionPacienteRuntimeDAO();
        UpdateBuilder<InformacionPaciente, Integer> queryBuilder = informacionPacienteDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando información paciente", e);
        }
        return true;
    }

    public Boolean actualizarFechaInformacionPaciente(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<InformacionPaciente, Integer> informacionPacienteDAO = getDbHelper().getInformacionPacienteRuntimeDAO();
        UpdateBuilder<InformacionPaciente, Integer> queryBuilder = informacionPacienteDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando información paciente", e);
        }
        return true;
    }

    public Boolean actualizarMesaAyudaFecha(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<HelpDesk, Integer> informacionPacienteDAO = getDbHelper().getHelpDeskRuntimeDAO();
        UpdateBuilder<HelpDesk, Integer> queryBuilder = informacionPacienteDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando información paciente", e);
        }
        return true;
    }


    public Boolean actualizarRequerimiento(String id, String field, String value, String filter) {
        RuntimeExceptionDao<Requerimiento, Integer> requerimientoDAO = getDbHelper().getRequerimientoRuntimeDAO();
        UpdateBuilder<Requerimiento, Integer> queryBuilder = requerimientoDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando requerimiento", e);
        }
        return true;
    }

    public Boolean actualizarImagenLesion(String id, String field, String value, String filter) {
        RuntimeExceptionDao<ImagenLesion, Integer> imagenDAO = getDbHelper().getImagenLesionRuntimeDAO();
        UpdateBuilder<ImagenLesion, Integer> queryBuilder = imagenDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando imagen lesión", e);
        }
        return true;
    }

    public Boolean actualizarFechaImagenLesion(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<ImagenLesion, Integer> imagenDAO = getDbHelper().getImagenLesionRuntimeDAO();
        UpdateBuilder<ImagenLesion, Integer> queryBuilder = imagenDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando imagen lesión", e);
        }
        return true;
    }

    public Boolean actualizarLesion(String id, String field, String value, String filter) {
        RuntimeExceptionDao<Lesion, Integer> imagenDAO = getDbHelper().getLesionRuntimeDAO();
        UpdateBuilder<Lesion, Integer> queryBuilder = imagenDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando lesión", e);
        }
        return true;
    }

    public Boolean actualizarFechaLesion(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<Lesion, Integer> imagenDAO = getDbHelper().getLesionRuntimeDAO();
        UpdateBuilder<Lesion, Integer> queryBuilder = imagenDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando lesión", e);
        }
        return true;
    }

    public Boolean actualizarImagenAnexo(String id, String field, String value, String filter) {
        RuntimeExceptionDao<ImagenAnexo, Integer> imagenDAO = getDbHelper().getImagenAnexoRuntimeDAO();
        UpdateBuilder<ImagenAnexo, Integer> queryBuilder = imagenDAO.updateBuilder();
        //imagenDAO.queryBuilder().where().eq(filter, id).query()
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando imagen anexo", e);
        }
        return true;
    }
    public Boolean actualizarFechaImagenAnexo(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<ImagenAnexo, Integer> imagenDAO = getDbHelper().getImagenAnexoRuntimeDAO();
        UpdateBuilder<ImagenAnexo, Integer> queryBuilder = imagenDAO.updateBuilder();

        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando imagen anexo", e);
        }
        return true;
    }

    public Boolean eliminarArchivoSincronizacion(Integer id) {
        RuntimeExceptionDao<ArchivosSincronizacion, Integer> imagenDAO = getDbHelper().getArchivosSincronizacionRuntimeDAO();
        DeleteBuilder<ArchivosSincronizacion, Integer> queryBuilder = imagenDAO.deleteBuilder();

        try {
            queryBuilder.where().eq("id", id);
            queryBuilder.delete();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando archivo sincronización", e);
        }

        return true;
    }


    public Paciente getPaciente(int idPaciente) {
        Paciente paciente = null;
        try {

            RuntimeExceptionDao<Paciente, Integer> pacienteDao = getDbHelper().getPacienteRuntimeDAO();
            QueryBuilder<Paciente, Integer> queryBuilder = pacienteDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", idPaciente
            );
            List<Paciente> list_paciente = pacienteDao.query(queryBuilder.prepare());
            if (!list_paciente.isEmpty()) {
                paciente = list_paciente.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniento paciente", e);
        }
        return paciente;
    }

    public InformacionPaciente getInformacionPaciente(int idInfoPaciente) {
        InformacionPaciente informacion_paciente = null;
        try {

            RuntimeExceptionDao<InformacionPaciente, Integer> informacionDao = getDbHelper().getInformacionPacienteRuntimeDAO();
            QueryBuilder<InformacionPaciente, Integer> queryBuilder = informacionDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", idInfoPaciente
            );
            List<InformacionPaciente> list_informacion = informacionDao.query(queryBuilder.prepare());
            if (!list_informacion.isEmpty()) {
                informacion_paciente = list_informacion.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo información paciente", e);
        }
        return informacion_paciente;
    }

    public ConsultaMedica getConsultaMedica(int idConsulta) {
        ConsultaMedica consulta = null;
        try {

            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDao = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", idConsulta
            );
            List<ConsultaMedica> list_consulta = consultaDao.query(queryBuilder.prepare());
            if (!list_consulta.isEmpty()) {
                consulta = list_consulta.get(0);
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo consulta", e);
        }
        return consulta;
    }

    public ConsultaMedica getConsultaMedicaByServer(int idServidor) {
        ConsultaMedica consulta = null;
        try {

            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDao = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    ConsultaMedica.NOMBRE_CAMPO_ID_SERVIDOR, idServidor
            );
            List<ConsultaMedica> list_consulta = consultaDao.query(queryBuilder.prepare());
            if (!list_consulta.isEmpty()) {
                consulta = list_consulta.get(0);
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo consulta", e);
        }
        return consulta;
    }

    public ConsultaEnfermeria getConsultaEnfermeria(int idConsulta) {
        ConsultaEnfermeria consulta = null;
        try {

            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDao = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", idConsulta
            );
            List<ConsultaEnfermeria> list_consulta = consultaDao.query(queryBuilder.prepare());
            if (!list_consulta.isEmpty()) {
                consulta = list_consulta.get(0);
            }

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo consulta enfermerìa", e);
        }
        return consulta;
    }


    public List<Lesion> getLesionesConsulta(int consulta_id) {
        List<Lesion> list_consulta = null;
        try {
            RuntimeExceptionDao<Lesion, Integer> consultaDao = getDbHelper().getLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    Lesion.NOMBRE_CAMPO_ID_CONSULT_LOCAL, consulta_id
            );
            list_consulta = consultaDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo lesiones consulta", e);
        }
        return list_consulta;
    }


    public List<Lesion> getLesiones(int id,String filter) {
        List<Lesion> list_consulta = null;
        try {
            RuntimeExceptionDao<Lesion, Integer> consultaDao = getDbHelper().getLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    filter, id
            );
            list_consulta = consultaDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo lesiones", e);
        }
        return list_consulta;
    }


    public Lesion getLesion(String lesion_id) {
        List<Lesion> list_consulta = null;
        try {
            RuntimeExceptionDao<Lesion, Integer> consultaDao = getDbHelper().getLesionRuntimeDAO();
            QueryBuilder<Lesion, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", lesion_id
            );
            list_consulta = consultaDao.query(queryBuilder.prepare());
            return list_consulta.get(0);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo lesiòn", e);
        }
        return null;
    }

    public ControlMedico getControlMedico(String control_id) {
        ControlMedico list_consulta = null;
        try {
            RuntimeExceptionDao<ControlMedico, Integer> consultaDao = getDbHelper().getControlMedicoRuntimeDAO();
            QueryBuilder<ControlMedico, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", control_id
            );
            list_consulta = consultaDao.queryForFirst(queryBuilder.prepare());
            return list_consulta;
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo control médico", e);
        }
        return null;
    }
    public ControlMedico getControlMedicoServidor(String idServidor) {
        ControlMedico list_consulta = null;
        try {
            RuntimeExceptionDao<ControlMedico, Integer> consultaDao = getDbHelper().getControlMedicoRuntimeDAO();
            QueryBuilder<ControlMedico, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    ControlMedico.NOMBRE_CAMPO_ID_SERVIDOR, idServidor
            );
            list_consulta = consultaDao.queryForFirst(queryBuilder.prepare());
            return list_consulta;
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo control médico", e);
        }
        return null;
    }


    public ControlEnfermeria getControlEnfermeria(String control_id) {
        List<ControlEnfermeria> list_consulta = null;
        try {
            RuntimeExceptionDao<ControlEnfermeria, Integer> consultaDao = getDbHelper().getControlEnfermeriaRuntimeDAO();
            QueryBuilder<ControlEnfermeria, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", control_id
            );
            list_consulta = consultaDao.query(queryBuilder.prepare());
            if(!list_consulta.isEmpty())
                return list_consulta.get(0);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo control enfermeria", e);
        }
        return null;
    }

    public List<ImagenLesion> getImagenesDermatoscopiaLesion(int lesion_id, String path) {
        List<ImagenLesion> list_consulta = null;
        try {
            RuntimeExceptionDao<ImagenLesion, Integer> imagenDao = getDbHelper().getImagenLesionRuntimeDAO();
            QueryBuilder<ImagenLesion, Integer> queryBuilder = imagenDao.queryBuilder();
            queryBuilder.where().eq(
                    ImagenLesion.NOMBRE_CAMPO_ID_INJURY_LOCAL, lesion_id
            ).and().eq(ImagenLesion.NOMBRE_CAMPO_PHOTO, path);
            list_consulta = imagenDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo imagen lesión", e);
        }
        return list_consulta;
    }

    public List<ImagenLesion> getImagenesLesion(int lesion_id) {
        List<ImagenLesion> list_consulta = null;
        try {
            RuntimeExceptionDao<ImagenLesion, Integer> imagenDao = getDbHelper().getImagenLesionRuntimeDAO();
            QueryBuilder<ImagenLesion, Integer> queryBuilder = imagenDao.queryBuilder();
            queryBuilder.where().eq(
                    ImagenLesion.NOMBRE_CAMPO_ID_INJURY_LOCAL, lesion_id
            );
            list_consulta = imagenDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo imagen lesión", e);
        }
        return list_consulta;
    }

    public String[] getImagenesAnexo(String id_consulta, String id_control) {
        String[] list_consulta = null;
        try {
            RuntimeExceptionDao<ImagenAnexo, Integer> imagenDao = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> queryBuilder = imagenDao.queryBuilder();
            queryBuilder.where().eq(
                    (id_consulta == null) ? ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_CONSULTA : ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA, (id_consulta == null) ? id_control : id_consulta
            );
            List<ImagenAnexo> list_anexo = imagenDao.query(queryBuilder.prepare());
            list_consulta = new String[list_anexo.size()];
            for (int i = 0; i < list_anexo.size(); i++) {
                list_consulta[i] = list_anexo.get(i).getImagenAnexo();
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo imagen anexo", e);
        }
        return list_consulta;
    }


    public List<ImagenAnexo> getImagenesAnexoObj(String id_consulta, String id_control) {
        List<ImagenAnexo> list_consulta = null;
        try {
            RuntimeExceptionDao<ImagenAnexo, Integer> imagenDao = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> queryBuilder = imagenDao.queryBuilder();
            queryBuilder.where().eq(
                    (id_consulta == null) ? ImagenAnexo.NOMBRE_CAMPO_ID_CONTROL_CONSULTA : ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA, (id_consulta == null) ? id_control : id_consulta
            );
            list_consulta = imagenDao.query(queryBuilder.prepare());

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo imagen anexo", e);
        }
        return list_consulta;
    }


    public ImagenAnexo getImagenAnexo(int id_local) {
        ImagenAnexo imagenAnexo = null;
        try {
            RuntimeExceptionDao<ImagenAnexo, Integer> imagenDao = getDbHelper().getImagenAnexoRuntimeDAO();
            QueryBuilder<ImagenAnexo, Integer> queryBuilder = imagenDao.queryBuilder();
            queryBuilder.where().eq("id", id_local);
            imagenAnexo = imagenDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo imagen anexo", e);
        }
        return imagenAnexo;
    }

    public ImagenLesion getImagenLesion(int id_local) {
        ImagenLesion ImagenLesion = null;
        try {
            RuntimeExceptionDao<ImagenLesion, Integer> imagenDao = getDbHelper().getImagenLesionRuntimeDAO();
            QueryBuilder<ImagenLesion, Integer> queryBuilder = imagenDao.queryBuilder();
            queryBuilder.where().eq("id", id_local);
            ImagenLesion = imagenDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo imagen lesión", e);
        }
        return ImagenLesion;
    }


    public void eliminarPendiente(Integer id,String name_table) {
        try {
            RuntimeExceptionDao<PendienteSincronizacion, Integer> pendienteDAO = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> queryBuilder = pendienteDAO.queryBuilder();
            queryBuilder.where().eq(PendienteSincronizacion.NOMBRE_CAMPO_ID_, id).and().eq(PendienteSincronizacion.NOMBRE_CAMPO_TABLE,name_table);

            List<PendienteSincronizacion> list = pendienteDAO.query(queryBuilder.prepare());
            for (int i = 0; i < list.size(); i++) {
                pendienteDAO.delete(list.get(i));
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando pendiente sincronización", e);
        }

    }

    public void eliminarPendienteById(Integer id) {
        try {
            RuntimeExceptionDao<PendienteSincronizacion, Integer> pendienteDAO = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> queryBuilder = pendienteDAO.queryBuilder();
            queryBuilder.where().eq("id", id);
            pendienteDAO.delete(pendienteDAO.queryForFirst(queryBuilder.prepare()));

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error eliminando pendiente sincronización", e);
        }

    }


    public List<Requerimiento> obtenerRequerimientos(Integer idRequerimiento) {
        List<Requerimiento> resultado = new ArrayList<>();

        try {
            RuntimeExceptionDao<Requerimiento, Integer> dao = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    "id", idRequerimiento
            );
            resultado = dao.query(qb.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo los requerimientos", e);
            contexto.mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        return resultado;
    }

    public String obtenerTituloConsulta(Consulta consulta) {
        String titulo = "";

        if(TIPO_PROFESIONAL_MEDICO.equals(consulta.getTipoProfesional())) {
            try {
                RuntimeExceptionDao<ConsultaMedica, Integer> dao = getDbHelper().getConsultaMedicaRuntimeDAO();
                GenericRawResults<String[]> rawResults = dao.queryRaw(String.format(
                        "SELECT %s FROM %s WHERE %s = %d",
                        ConsultaMedica.NOMBRE_CAMPO_IMPRESION_DIAGNOSTICO,
                        ConsultaMedica.NOMBRE_TABLA,
                        ConsultaMedica.NOMBRE_CAMPO_ID_SERVIDOR,
                        consulta.getIdServidor()));
                titulo = rawResults.getFirstResult()[0];
            } catch (SQLException e) {
                Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo título consulta", e);
            }
        } else if(TIPO_PROFESIONAL_ENFERMERA.equals(consulta.getTipoProfesional())) {
            try {
                RuntimeExceptionDao<ConsultaEnfermeria, Integer> dao = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
                GenericRawResults<String[]> rawResults = dao.queryRaw(String.format(
                        "SELECT %s, %s FROM %s WHERE %s = %d",
                        ConsultaEnfermeria.NOMBRE_CAMPO_ULCER_ETIOLOGY,
                        ConsultaEnfermeria.NOMBRE_CAMPO_ULCER_ETIOLOGY_OTHER,
                        ConsultaEnfermeria.NOMBRE_TABLA,
                        ConsultaEnfermeria.NOMBRE_CAMPO_ID_SERVIDOR,
                        consulta.getIdServidor()));
                titulo = obtenerNombresParametro(Parametro.TIPO_PARAMETRO_ULCER_ETIOLOGY, rawResults.getFirstResult()[0])
                        + rawResults.getFirstResult()[1];
            } catch (SQLException e) {
                Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo título consulta", e);
            }
        }

        return titulo;
    }


    public ConsultaEnfermeria getConsultaEnfermeria(Integer consulta_id) {
        ConsultaEnfermeria consulta = new ConsultaEnfermeria();
        try {
            if (consulta_id == null)
                consulta_id = 0;
            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaDAO.queryBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            List<ConsultaEnfermeria> list_consultas = consultaDAO.query(queryBuilder.prepare());
            if (!list_consultas.isEmpty()) {
                consulta = list_consultas.get(0);
            }
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo consulta enfermería", e);
        }
        return consulta;
    }


    public Boolean actualizarControlEnfermeria(String id, String field, String value, String filter) {
        RuntimeExceptionDao<ControlEnfermeria, Integer> consultaEnfermeriaDAO = getDbHelper().getControlEnfermeriaRuntimeDAO();
        UpdateBuilder<ControlEnfermeria, Integer> queryBuilder = consultaEnfermeriaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando control", e);
        }
        return true;
    }

    public Boolean actualizarFechaControlEnfermeria(String id, String field, Date value, String filter) {
        RuntimeExceptionDao<ControlEnfermeria, Integer> consultaEnfermeriaDAO = getDbHelper().getControlEnfermeriaRuntimeDAO();
        UpdateBuilder<ControlEnfermeria, Integer> queryBuilder = consultaEnfermeriaDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando control", e);
        }
        return true;
    }


    public void crearCie10(Cie10 entidad) {
        try {
            RuntimeExceptionDao<Cie10, Integer> dao = getDbHelper().getCie10RuntimeDAO();
            QueryBuilder<Cie10, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    "id", entidad.getId()
            );
            dao.createOrUpdate(entidad);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando CIE10", e);
        }
    }


    public List<Cie10> obtenerTodosCie10() {
        try {
            RuntimeExceptionDao<Cie10, Integer> dao = getDbHelper().getCie10RuntimeDAO();
            QueryBuilder<Cie10, Integer> qb = dao.queryBuilder();
            return qb.query();
        } catch (SQLException e) {
            Log.e("obtenerSincronizables", "error de exepcion ==>"+e);
        }
        return null;
    }



    public Requerimiento getRequerimiento(String id) {
        try {
            RuntimeExceptionDao<Requerimiento, Integer> consultaDao = getDbHelper().getRequerimientoRuntimeDAO();
            QueryBuilder<Requerimiento, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", id
            );
            return  consultaDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo requerimiento", e);
        }
        return null;
    }

    public Cie10 getCie10ByCode(String code) {
        try {
            RuntimeExceptionDao<Cie10, Integer> dao = getDbHelper().getCie10RuntimeDAO();
            QueryBuilder<Cie10, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(
                    Cie10.NOMBRE_CAMPO_CODE, code
            );
            return  dao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo cie10", e);
        }
        return null;
    }


    public Boolean crearMesaAyuda(HelpDesk mesaAyuda) {
        try {
            RuntimeExceptionDao<HelpDesk, Integer> mesaAyudaDAO = getDbHelper().getHelpDeskRuntimeDAO();
            QueryBuilder<HelpDesk, Integer> queryBuilder = mesaAyudaDAO.queryBuilder();
            int id_busqueda = ((mesaAyuda.getIdServidor() == null) ? 0 : mesaAyuda.getIdServidor());
            queryBuilder.where().eq(
                    Paciente.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<HelpDesk> pacienteBD = mesaAyudaDAO.query(queryBuilder.prepare());

            if (!pacienteBD.isEmpty()) {
                mesaAyuda.setId(pacienteBD.get(0).getId());
            }
            mesaAyudaDAO.createOrUpdate(mesaAyuda);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando paciente", e);
            return false;
        }
        return true;
    }

    public HelpDesk getMesaAyuda(String mesa_id) {
        HelpDesk list_consulta = null;
        try {
            RuntimeExceptionDao<HelpDesk, Integer> consultaDao = getDbHelper().getHelpDeskRuntimeDAO();
            QueryBuilder<HelpDesk, Integer> queryBuilder = consultaDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", mesa_id
            );
            list_consulta = consultaDao.queryForFirst(queryBuilder.prepare());
            return list_consulta;
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo control médico", e);
        }
        return null;
    }

    public List<HelpDesk> getAllMesaAyuda(int user_id) {
        try {
            RuntimeExceptionDao<HelpDesk, Integer> dao = getDbHelper().getHelpDeskRuntimeDAO();
            QueryBuilder<HelpDesk, Integer> qb = dao.queryBuilder();
            qb.where().eq(
                    HelpDesk.NOMBRE_CAMPO_USER_ID, user_id
            );
            return qb.query();
        } catch (SQLException e) {
            Log.e("obtenerSincronizables", "error de exepcion ==>"+e);
        }
        return null;
    }



    public void eliminarMesaAyuda() {
        try {
            RuntimeExceptionDao<HelpDesk, Integer> dao = getDbHelper().getHelpDeskRuntimeDAO();
            QueryBuilder<HelpDesk, Integer> qb = dao.queryBuilder();
            for (HelpDesk help : qb.query()) {
                if (help.getIdServidor() != (null))
                    dao.delete(help);
            }
        } catch (SQLException e) {
            Log.e("obtenerSincronizables", "error de exepcion ==>"+e);
        }
    }


    public Boolean actualizarMesaAyuda(String id, String field, String value, String filter) {
        RuntimeExceptionDao<HelpDesk, Integer> helpDAO = getDbHelper().getHelpDeskRuntimeDAO();
        UpdateBuilder<HelpDesk, Integer> queryBuilder = helpDAO.updateBuilder();
        try {
            queryBuilder.where().eq(filter, id);
            queryBuilder.updateColumnValue(field, value);
            queryBuilder.update();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error actualizando control", e);
        }
        return true;
    }

    public Credencial getCredencial() {
        try {
            RuntimeExceptionDao<Credencial, Integer> dao = getDbHelper().getCredencialRuntimeDAO();
            QueryBuilder<Credencial, Integer> queryBuilder = dao.queryBuilder();
            Credencial c = dao.queryForFirst(queryBuilder.prepare());
            List<Credencial> list = dao.query(queryBuilder.prepare());
            if(list.size()==0)
            {
                Credencial credencial_ = new Credencial();
                credencial_.setApi_access_key("");
                credencial_.setApi_bucket_directory("");
                credencial_.setApi_bucket_name("");
                credencial_.setApi_secret_key("");
                return credencial_;
            }
            else
                return list.get(list.size()-1);

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo credenciales", e);
        }
        return null;
    }



    public Fallida getFallida() {
        try {
            RuntimeExceptionDao<Fallida, Integer> dao = getDbHelper().getFallidaRuntimeDAO();
            QueryBuilder<Fallida, Integer> queryBuilder = dao.queryBuilder();
            Fallida c = dao.queryForFirst(queryBuilder.prepare());
            List<Fallida> list = dao.query(queryBuilder.prepare());
            return list.get(list.size()-1);

        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo fallida", e);
        }
        return null;
    }
    public Boolean crearCredencial(Credencial credencial) {
        try {
            RuntimeExceptionDao<Credencial, Integer> consultaDAO = getDbHelper().getCredencialRuntimeDAO();
            QueryBuilder<Credencial, Integer> queryBuilder = consultaDAO.queryBuilder();
            int id_busqueda = ((credencial.getId() == null) ? 0 : credencial.getId());
            queryBuilder.where().eq(
                    Credencial.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
            List<Credencial> consultaBD = consultaDAO.query(queryBuilder.prepare());

            if (!consultaBD.isEmpty())
                credencial.setId(consultaBD.get(0).getId());

            consultaDAO.createOrUpdate(credencial);
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando credencial", e);

            return false;
        }

        return true;
    }


    public Integer crearTrazabilidad(Trazabilidad trazabilidad) {
        try {
            RuntimeExceptionDao<Trazabilidad, Integer> consultaDAO = getDbHelper().getTrazabilidadRuntimeDAO();
            QueryBuilder<Trazabilidad, Integer> queryBuilder = consultaDAO.queryBuilder();
            int id_busqueda = ((trazabilidad.getId() == null) ? 0 : trazabilidad.getId());
            queryBuilder.where().eq(
                    Credencial.NOMBRE_CAMPO_ID_SERVIDOR, id_busqueda
            );
                List<Trazabilidad> consultaBD = consultaDAO.query(queryBuilder.prepare());

            if (!consultaBD.isEmpty())
                trazabilidad.setId(consultaBD.get(0).getId());
            consultaDAO.createOrUpdate(trazabilidad);
            return trazabilidad.getId();
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error creando credencial", e);

            return null;
        }


    }

    public Trazabilidad getTrazabilidad(String trazabilidad_id) {
        Trazabilidad list_trazabilidad = null;
        try {
            RuntimeExceptionDao<Trazabilidad, Integer> trazabilidadDao = getDbHelper().getTrazabilidadRuntimeDAO();
            QueryBuilder<Trazabilidad, Integer> queryBuilder = trazabilidadDao.queryBuilder();
            queryBuilder.where().eq(
                    "id", trazabilidad_id
            );
            list_trazabilidad = trazabilidadDao.queryForFirst(queryBuilder.prepare());
            return list_trazabilidad;
        } catch (SQLException e) {
            Log.d(Constantes.TAG_ERROR_ORMLITE_BASE_ACTIVITY, "Ha ocurrido un error obteniendo trazabilidad", e);
        }
        return null;
    }





}
