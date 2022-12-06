package com.telederma.gov.co.database;

import android.content.Context;
import android.util.Log;

import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.Aseguradora;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
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
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Utils;
import com.j256.ormlite.cipher.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;



/**
 * Helper para realizar operaciones sobre la base de datos local
 * <p>
 * Created by Daniel Hernández on 6/8/2018.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private Dao<Usuario, Integer> usuarioDAO = null;
    private RuntimeExceptionDao<Usuario, Integer> usuarioRuntimeDAO = null;

    private Dao<ConsultaMedica, Integer> consultaMedicaDAO = null;
    private RuntimeExceptionDao<ConsultaMedica, Integer> consultaMedicaRuntimeDAO = null;

    private Dao<Credencial, Integer> credencialDAO = null;
    private RuntimeExceptionDao<Credencial, Integer> credencialRuntimeDAO = null;

    private Dao<Paciente, Integer> pacienteDAO = null;
    private RuntimeExceptionDao<Paciente, Integer> pacienteRuntimeDAO = null;

    private Dao<Parametro, Integer> parametroDAO = null;
    private RuntimeExceptionDao<Parametro, Integer> parametroRuntimeDAO = null;

    private Dao<InformacionPaciente, Integer> informacionPacienteDAO = null;
    private RuntimeExceptionDao<InformacionPaciente, Integer> informacionPacienteRuntimeDAO = null;

    private Dao<Departamento, Integer> departamentoDAO = null;
    private RuntimeExceptionDao<Departamento, Integer> departamentoRuntimeDAO = null;

    private Dao<Cie10, Integer> cie10DAO = null;
    private RuntimeExceptionDao<Cie10, Integer> cie10RuntimeDAO = null;

    private Dao<Municipio, Integer> municipioDAO = null;
    private RuntimeExceptionDao<Municipio, Integer> municipioRuntimeDAO = null;

    private Dao<ImagenLesion, Integer> imagenLesionDAO = null;
    private RuntimeExceptionDao<ImagenLesion, Integer> imagenLesionRuntimeDAO = null;

    private Dao<Lesion, Integer> lesionDAO = null;
    private RuntimeExceptionDao<Lesion, Integer> lesionRuntimeDAO = null;

    private Dao<Aseguradora, Integer> aseguradoraDAO = null;
    private RuntimeExceptionDao<Aseguradora, Integer> aseguradoraRuntimeDAO = null;

    private Dao<ControlMedico, Integer> controlMedicoDAO = null;
    private RuntimeExceptionDao<ControlMedico, Integer> controlMedicoRuntimeDAO = null;

    private Dao<ControlEnfermeria, Integer> controlEnfermeriaDAO = null;
    private RuntimeExceptionDao<ControlEnfermeria, Integer> controlEnfermeriaRuntimeDAO = null;

    private Dao<ParteCuerpo, Integer> parteCuerpoDao = null;
    private RuntimeExceptionDao<ParteCuerpo, Integer> parteCuerpoRuntimeDAO = null;

    private Dao<Diagnostico, Integer> diagnosticoDAO = null;
    private RuntimeExceptionDao<Diagnostico, Integer> diagnosticoRuntimeDAO = null;

    private Dao<Especialista, Integer> especialistaDAO = null;
    private RuntimeExceptionDao<Especialista, Integer> especialistaRuntimeDAO = null;

    private Dao<ExamenSolicitado, Integer> examenSolicitadoDAO = null;
    private RuntimeExceptionDao<ExamenSolicitado, Integer> examenSolicitadoRuntimeDAO = null;

    private Dao<Formula, Integer> formulaDAO = null;
    private RuntimeExceptionDao<Formula, Integer> formulaRuntimeDAO = null;

    private Dao<Requerimiento, Integer> requerimientoDAO = null;
    private RuntimeExceptionDao<Requerimiento, Integer> requerimientoRuntimeDAO = null;

    private Dao<Mipres, Integer> mipresDAO = null;
    private RuntimeExceptionDao<Mipres, Integer> mipresRuntimeDAO = null;

    private Dao<RespuestaEspecialista, Integer> respuestaEspecialistaDAO = null;
    private RuntimeExceptionDao<RespuestaEspecialista, Integer> respuestaEspecialistaRuntimeDAO = null;

    private Dao<ImagenAnexo, Integer> imagenAnexoDAO = null;
    private RuntimeExceptionDao<ImagenAnexo, Integer> imagenAnexoRuntimeDAO = null;

    private Dao<PendienteSincronizacion, Integer> pendienteDAO = null;
    private RuntimeExceptionDao<PendienteSincronizacion, Integer> pendienteRuntimeDAO = null;

    private Dao<ArchivosSincronizacion, Integer> archivoSincronizacionDAO = null;
    private RuntimeExceptionDao<ArchivosSincronizacion, Integer> archivoSincronizacionDAORuntimeDAO = null;

    private Dao<ConsultaEnfermeria, Integer> consultaEnfermeriaDAO = null;
    private RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaEnfermeriaRuntimeDAO = null;

    private Dao<DescartarRequerimiento, Integer> descartarRequerimientoDao = null;
    private RuntimeExceptionDao<DescartarRequerimiento, Integer> descartarRequerimientoRuntimeDao = null;

    private Dao<HelpDesk, Integer> helpDeskDAO = null;
    private RuntimeExceptionDao<HelpDesk, Integer> helpDeskRuntimeDAO = null;


    private Dao<Fallida, Integer> fallidaDAO = null;
    private RuntimeExceptionDao<Fallida, Integer> fallidaRuntimeDAO = null;

    private Dao<Trazabilidad, Integer> trazabilidadDAO = null;
    private RuntimeExceptionDao<Trazabilidad, Integer> trazabilidadRuntimeDAO = null;


    private Context context ;
    public DBHelper(Context context) {
        super(context, Constantes.DATABASE_NAME, null, Constantes.DATABASE_VERSION, R.raw.ormlite_config);
        this.context = context;
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource source) {
        try {
            Log.i(DBHelper.class.getSimpleName(), "onCreate");
            crearTablas(source);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getSimpleName(), "No fue posible crear la base de datos", e);
            throw new RuntimeException(e);
        }
    }

    public void crearTablas(ConnectionSource source) throws SQLException {
        TableUtils.createTable(source, Usuario.class);
        TableUtils.createTable(source, ConsultaMedica.class);
        TableUtils.createTable(source, Parametro.class);
        TableUtils.createTable(source, Paciente.class);
        TableUtils.createTable(source, InformacionPaciente.class);
        TableUtils.createTable(source, Cie10.class);
        TableUtils.createTable(source, Departamento.class);
        TableUtils.createTable(source, Municipio.class);
        TableUtils.createTable(source, Lesion.class);
        TableUtils.createTable(source, ImagenLesion.class);
        TableUtils.createTable(source, Aseguradora.class);
        TableUtils.createTable(source, ControlMedico.class);
        TableUtils.createTable(source, Diagnostico.class);
        TableUtils.createTable(source, Especialista.class);
        TableUtils.createTable(source, ExamenSolicitado.class);
        TableUtils.createTable(source, Formula.class);
        TableUtils.createTable(source, Requerimiento.class);
        TableUtils.createTable(source, Mipres.class);
        TableUtils.createTable(source, RespuestaEspecialista.class);
        TableUtils.createTable(source, ParteCuerpo.class);
        TableUtils.createTable(source, ImagenAnexo.class);
        TableUtils.createTable(source, PendienteSincronizacion.class);
        TableUtils.createTable(source, ArchivosSincronizacion.class);
        TableUtils.createTable(source, ConsultaEnfermeria.class);
        TableUtils.createTable(source, ControlEnfermeria.class);
        TableUtils.createTable(source, DescartarRequerimiento.class);
        TableUtils.createTable(source, HelpDesk.class);
        TableUtils.createTable(source, Credencial.class);
        TableUtils.createTable(source, Fallida.class);
        TableUtils.createTable(source, Trazabilidad.class);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int i, int i1) {
        try {
            Log.i(DBHelper.class.getSimpleName(), "onUupgrade");
            eliminarTablas(source);
            onCreate(db, source);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getSimpleName(), "No fue posible actualizar la base de datos", e);
            throw new RuntimeException(e);
        }
    }

    public void eliminarTablas(ConnectionSource source) throws SQLException {
        TableUtils.dropTable(source, Usuario.class, true);
        TableUtils.dropTable(source, ConsultaMedica.class, true);
        TableUtils.dropTable(source, Parametro.class, true);
        TableUtils.dropTable(source, Paciente.class, true);
        TableUtils.dropTable(source, InformacionPaciente.class, true);
        TableUtils.dropTable(source, Cie10.class, true);
        TableUtils.dropTable(source, Departamento.class, true);
        TableUtils.dropTable(source, Municipio.class, true);
        TableUtils.dropTable(source, Lesion.class, true);
        TableUtils.dropTable(source, ImagenLesion.class, true);
        TableUtils.dropTable(source, Aseguradora.class, true);
        TableUtils.dropTable(source, ControlMedico.class, true);
        TableUtils.dropTable(source, Diagnostico.class, true);
        TableUtils.dropTable(source, Especialista.class, true);
        TableUtils.dropTable(source, ExamenSolicitado.class, true);
        TableUtils.dropTable(source, Formula.class, true);
        TableUtils.dropTable(source, Requerimiento.class, true);
        TableUtils.dropTable(source, Mipres.class, true);
        TableUtils.dropTable(source, RespuestaEspecialista.class, true);
        TableUtils.dropTable(source, ParteCuerpo.class, true);
        TableUtils.dropTable(source, ImagenAnexo.class, true);
        TableUtils.dropTable(source, PendienteSincronizacion.class, true);
        TableUtils.dropTable(source, ArchivosSincronizacion.class, true);
        TableUtils.dropTable(source, ConsultaEnfermeria.class, true);
        TableUtils.dropTable(source, ControlEnfermeria.class, true);
        TableUtils.dropTable(source, DescartarRequerimiento.class, true);
        TableUtils.dropTable(source, HelpDesk.class, true);
        TableUtils.dropTable(source, Credencial.class, true);
        TableUtils.dropTable(source, Fallida.class, true);
        TableUtils.dropTable(source, Trazabilidad.class, true);
    }

    public Dao<Usuario, Integer> getUsuarioDAO() throws SQLException {
        if (usuarioDAO == null) usuarioDAO = getDao(Usuario.class);

        return usuarioDAO;
    }

    public RuntimeExceptionDao<Usuario, Integer> getUsuarioRuntimeDAO() {
        if (usuarioRuntimeDAO == null) usuarioRuntimeDAO = getRuntimeExceptionDao(Usuario.class);

        return usuarioRuntimeDAO;
    }

    public Dao<ConsultaMedica, Integer> getConsultaMedicaDAO() throws SQLException {
        if (consultaMedicaDAO == null) consultaMedicaDAO = getDao(ConsultaMedica.class);
        return consultaMedicaDAO;
    }

    public RuntimeExceptionDao<ConsultaMedica, Integer> getConsultaMedicaRuntimeDAO() {
        if (consultaMedicaRuntimeDAO == null)
            consultaMedicaRuntimeDAO = getRuntimeExceptionDao(ConsultaMedica.class);
        return consultaMedicaRuntimeDAO;
    }

    public Dao<Credencial, Integer> getCredencialDAO() throws SQLException {
        if (credencialDAO == null) credencialDAO = getDao(Credencial.class);
        return credencialDAO;
    }

    public RuntimeExceptionDao<Credencial, Integer> getCredencialRuntimeDAO() {
        if (credencialRuntimeDAO == null)
            credencialRuntimeDAO = getRuntimeExceptionDao(Credencial.class);
        return credencialRuntimeDAO;
    }


    public Dao<Fallida, Integer> getFallidaDAO() throws SQLException {
        if (fallidaDAO == null) fallidaDAO = getDao(Fallida.class);
        return fallidaDAO;
    }

    public RuntimeExceptionDao<Fallida, Integer> getFallidaRuntimeDAO() {
        if (fallidaRuntimeDAO == null)
            fallidaRuntimeDAO = getRuntimeExceptionDao(Fallida.class);
        return fallidaRuntimeDAO;
    }

    public Dao<Paciente, Integer> getPacienteDAO() throws SQLException {
        if (pacienteDAO == null) pacienteDAO = getDao(Paciente.class);
        return pacienteDAO;
    }

    public RuntimeExceptionDao<Paciente, Integer> getPacienteRuntimeDAO() {
        if (pacienteRuntimeDAO == null) pacienteRuntimeDAO = getRuntimeExceptionDao(Paciente.class);
        return pacienteRuntimeDAO;
    }

    public Dao<Parametro, Integer> getParametroDAO() throws SQLException {
        if (parametroDAO == null) pacienteDAO = getDao(Paciente.class);
        return parametroDAO;
    }

    public RuntimeExceptionDao<Parametro, Integer> getParametroRuntimeDAO() {
        if (parametroRuntimeDAO == null)
            parametroRuntimeDAO = getRuntimeExceptionDao(Parametro.class);
        return parametroRuntimeDAO;
    }

    public Dao<InformacionPaciente, Integer> getInformacionPacienteDAO() throws SQLException {
        if (informacionPacienteRuntimeDAO == null)
            informacionPacienteDAO = getDao(InformacionPaciente.class);
        return informacionPacienteDAO;
    }

    public RuntimeExceptionDao<InformacionPaciente, Integer> getInformacionPacienteRuntimeDAO() {
        if (informacionPacienteRuntimeDAO == null)
            informacionPacienteRuntimeDAO = getRuntimeExceptionDao(InformacionPaciente.class);
        return informacionPacienteRuntimeDAO;
    }


    public Dao<Cie10, Integer> getCie10DAO() throws SQLException {
        if (cie10DAO == null) cie10DAO = getDao(Cie10.class);
        return cie10DAO;
    }

    public RuntimeExceptionDao<Cie10, Integer> getCie10RuntimeDAO() {
        if (cie10RuntimeDAO == null)
            cie10RuntimeDAO = getRuntimeExceptionDao(Cie10.class);
        return cie10RuntimeDAO;
    }


    public Dao<Departamento, Integer> getDepartamentoDAO() throws SQLException {
        if (departamentoDAO == null) departamentoDAO = getDao(Departamento.class);
        return departamentoDAO;
    }

    public RuntimeExceptionDao<Departamento, Integer> getDepartamentoRuntimeDAO() {
        if (departamentoRuntimeDAO == null)
            departamentoRuntimeDAO = getRuntimeExceptionDao(Departamento.class);
        return departamentoRuntimeDAO;
    }

    public Dao<Municipio, Integer> getMunicipioDAO() throws SQLException {
        if (municipioDAO == null) municipioDAO = getDao(Municipio.class);
        return municipioDAO;
    }

    public RuntimeExceptionDao<Municipio, Integer> getMunicipioRuntimeDAO() {
        if (municipioRuntimeDAO == null)
            municipioRuntimeDAO = getRuntimeExceptionDao(Municipio.class);
        return municipioRuntimeDAO;
    }

    public Dao<ImagenLesion, Integer> getImagenLesionDAO() throws SQLException {
        if (imagenLesionDAO == null) imagenLesionDAO = getDao(ImagenLesion.class);
        return imagenLesionDAO;
    }

    public RuntimeExceptionDao<ImagenLesion, Integer> getImagenLesionRuntimeDAO() {
        if (imagenLesionRuntimeDAO == null)
            imagenLesionRuntimeDAO = getRuntimeExceptionDao(ImagenLesion.class);
        return imagenLesionRuntimeDAO;
    }

    public Dao<Lesion, Integer> getLesionDAO() throws SQLException {
        if (lesionDAO == null) lesionDAO = getDao(Lesion.class);
        return lesionDAO;
    }

    public RuntimeExceptionDao<Lesion, Integer> getLesionRuntimeDAO() {
        if (lesionRuntimeDAO == null) lesionRuntimeDAO = getRuntimeExceptionDao(Lesion.class);
        return lesionRuntimeDAO;
    }

    public Dao<Aseguradora, Integer> getAseguradoraDAO() throws SQLException {
        if (aseguradoraDAO == null) aseguradoraDAO = getDao(Aseguradora.class);
        return aseguradoraDAO;
    }

    public RuntimeExceptionDao<Aseguradora, Integer> getAseguradoraRuntimeDAO() {
        if (aseguradoraRuntimeDAO == null)
            aseguradoraRuntimeDAO = getRuntimeExceptionDao(Aseguradora.class);
        return aseguradoraRuntimeDAO;
    }

    public Dao<ControlMedico, Integer> getControlMedicoDAO() throws SQLException {
        if (controlMedicoDAO == null) controlMedicoDAO = getDao(ControlMedico.class);
        return controlMedicoDAO;
    }

    public RuntimeExceptionDao<ControlMedico, Integer> getControlMedicoRuntimeDAO() {
        if (controlMedicoRuntimeDAO == null)
            controlMedicoRuntimeDAO = getRuntimeExceptionDao(ControlMedico.class);
        return controlMedicoRuntimeDAO;
    }

    public Dao<ControlEnfermeria, Integer> getControlEnfermeriaDAO() throws SQLException {
        if (controlEnfermeriaDAO == null) controlEnfermeriaDAO = getDao(ControlEnfermeria.class);
        return controlEnfermeriaDAO;
    }

    public RuntimeExceptionDao<ControlEnfermeria, Integer> getControlEnfermeriaRuntimeDAO() {
        if (controlEnfermeriaRuntimeDAO == null)
            controlEnfermeriaRuntimeDAO = getRuntimeExceptionDao(ControlEnfermeria.class);
        return controlEnfermeriaRuntimeDAO;
    }

    public Dao<Diagnostico, Integer> getDiagnosticoDAO() throws SQLException {
        if (diagnosticoDAO == null) diagnosticoDAO = getDao(Diagnostico.class);
        return diagnosticoDAO;
    }

    public RuntimeExceptionDao<Diagnostico, Integer> getDiagnosticoRuntimeDAO() {
        if (diagnosticoRuntimeDAO == null)
            diagnosticoRuntimeDAO = getRuntimeExceptionDao(Diagnostico.class);
        return diagnosticoRuntimeDAO;
    }

    public Dao<Especialista, Integer> getEspecialistaDAO() throws SQLException {
        if (especialistaDAO == null) especialistaDAO = getDao(Especialista.class);
        return especialistaDAO;
    }

    public RuntimeExceptionDao<Especialista, Integer> getEspecialistaRuntimeDAO() {
        if (especialistaRuntimeDAO == null)
            especialistaRuntimeDAO = getRuntimeExceptionDao(Especialista.class);
        return especialistaRuntimeDAO;
    }

    public Dao<ExamenSolicitado, Integer> getExamenSolicitadoDAO() throws SQLException {
        if (examenSolicitadoDAO == null) examenSolicitadoDAO = getDao(ExamenSolicitado.class);
        return examenSolicitadoDAO;
    }

    public RuntimeExceptionDao<ExamenSolicitado, Integer> getExamenSolicitadoRuntimeDAO() {
        if (examenSolicitadoRuntimeDAO == null)
            examenSolicitadoRuntimeDAO = getRuntimeExceptionDao(ExamenSolicitado.class);
        return examenSolicitadoRuntimeDAO;
    }

    public Dao<Formula, Integer> getFormulaDAO() throws SQLException {
        if (formulaDAO == null) formulaDAO = getDao(Formula.class);
        return formulaDAO;
    }

    public RuntimeExceptionDao<Formula, Integer> getFormulaRuntimeDAO() {
        if (formulaRuntimeDAO == null) formulaRuntimeDAO = getRuntimeExceptionDao(Formula.class);
        return formulaRuntimeDAO;
    }

    public Dao<Requerimiento, Integer> getRequerimientoDAO() throws SQLException {
        if (requerimientoDAO == null) requerimientoDAO = getDao(Requerimiento.class);
        return requerimientoDAO;
    }

    public RuntimeExceptionDao<Requerimiento, Integer> getRequerimientoRuntimeDAO() {
        if (requerimientoRuntimeDAO == null)
            requerimientoRuntimeDAO = getRuntimeExceptionDao(Requerimiento.class);
        return requerimientoRuntimeDAO;
    }

    public Dao<Mipres, Integer> getMipresDAO() throws SQLException {
        if (mipresDAO == null) mipresDAO = getDao(Mipres.class);
        return mipresDAO;
    }

    public RuntimeExceptionDao<Mipres, Integer> getMipresRuntimeDAO() {
        if (mipresRuntimeDAO == null) mipresRuntimeDAO = getRuntimeExceptionDao(Mipres.class);
        return mipresRuntimeDAO;
    }

    public Dao<RespuestaEspecialista, Integer> getRespuestaEspecialistaDAO() throws SQLException {
        if (respuestaEspecialistaDAO == null)
            respuestaEspecialistaDAO = getDao(RespuestaEspecialista.class);
        return respuestaEspecialistaDAO;
    }

    public RuntimeExceptionDao<RespuestaEspecialista, Integer> getRespuestaEspecialistaRuntimeDAO() {
        if (respuestaEspecialistaRuntimeDAO == null)
            respuestaEspecialistaRuntimeDAO = getRuntimeExceptionDao(RespuestaEspecialista.class);
        return respuestaEspecialistaRuntimeDAO;
    }

    public Dao<ParteCuerpo, Integer> getParteCuepoDao() throws SQLException {
        if (parteCuerpoDao == null) parteCuerpoDao = getDao(ParteCuerpo.class);
        return parteCuerpoDao;
    }

    public RuntimeExceptionDao<ParteCuerpo, Integer> getParteCuerpoRuntimeDAO() {
        if (parteCuerpoRuntimeDAO == null)
            parteCuerpoRuntimeDAO = getRuntimeExceptionDao(ParteCuerpo.class);
        return parteCuerpoRuntimeDAO;
    }

    public Dao<ImagenAnexo, Integer> getImagenAnexoDao() throws SQLException {
        if (imagenAnexoDAO == null) imagenAnexoDAO = getDao(ImagenAnexo.class);
        return imagenAnexoDAO;
    }

    public RuntimeExceptionDao<ImagenAnexo, Integer> getImagenAnexoRuntimeDAO() {
        if (imagenAnexoRuntimeDAO == null)
            imagenAnexoRuntimeDAO = getRuntimeExceptionDao(ImagenAnexo.class);
        return imagenAnexoRuntimeDAO;
    }


    public Dao<PendienteSincronizacion, Integer> getPendienteSincronizacionDao() throws SQLException {
        if (pendienteDAO == null) pendienteDAO = getDao(PendienteSincronizacion.class);
        return pendienteDAO;
    }

    public RuntimeExceptionDao<PendienteSincronizacion, Integer> getPendienteSincronizacionRuntimeDAO() {
        if (pendienteRuntimeDAO == null)
            pendienteRuntimeDAO = getRuntimeExceptionDao(PendienteSincronizacion.class);
        return pendienteRuntimeDAO;
    }

    public Dao<ArchivosSincronizacion, Integer> getArchivosSincronizacionDao() throws SQLException {
        if (archivoSincronizacionDAO == null)
            archivoSincronizacionDAO = getDao(ArchivosSincronizacion.class);
        return archivoSincronizacionDAO;
    }

    public RuntimeExceptionDao<ArchivosSincronizacion, Integer> getArchivosSincronizacionRuntimeDAO() {
        if (archivoSincronizacionDAORuntimeDAO == null)
            archivoSincronizacionDAORuntimeDAO = getRuntimeExceptionDao(ArchivosSincronizacion.class);
        return archivoSincronizacionDAORuntimeDAO;
    }

    public Dao<ConsultaEnfermeria, Integer> getConsultaEnfermeriaDao() throws SQLException {
        if (consultaEnfermeriaDAO == null)
            consultaEnfermeriaDAO = getDao(ConsultaEnfermeria.class);
        return consultaEnfermeriaDAO;
    }

    public RuntimeExceptionDao<ConsultaEnfermeria, Integer> getConsultaEnfermeriaRuntimeDAO() {
        if (consultaEnfermeriaRuntimeDAO == null)
            consultaEnfermeriaRuntimeDAO = getRuntimeExceptionDao(ConsultaEnfermeria.class);
        return consultaEnfermeriaRuntimeDAO;
    }

    public Dao<DescartarRequerimiento, Integer> getDescartarRequerimientoDAO() throws SQLException {
        if (descartarRequerimientoDao == null) descartarRequerimientoDao = getDao(DescartarRequerimiento.class);
        return descartarRequerimientoDao;
    }

    public RuntimeExceptionDao<DescartarRequerimiento, Integer> getDescartarRequerimientoRuntimeDAO() {
        if (descartarRequerimientoRuntimeDao == null)
            descartarRequerimientoRuntimeDao = getRuntimeExceptionDao(DescartarRequerimiento.class);
        return descartarRequerimientoRuntimeDao;
    }

    public Dao<HelpDesk, Integer> getHelpDeskDAO() throws SQLException {
        if (helpDeskDAO == null) helpDeskDAO = getDao(HelpDesk.class);
        return helpDeskDAO;
    }

    public RuntimeExceptionDao<HelpDesk, Integer> getHelpDeskRuntimeDAO() {
        if (helpDeskRuntimeDAO == null)
            helpDeskRuntimeDAO = getRuntimeExceptionDao(HelpDesk.class);
        return helpDeskRuntimeDAO;
    }


    public Dao<Trazabilidad, Integer> getTrazabilidadDAO() throws SQLException {
        if (trazabilidadDAO == null) trazabilidadDAO = getDao(Trazabilidad.class);
        return trazabilidadDAO;
    }

    public RuntimeExceptionDao<Trazabilidad, Integer> getTrazabilidadRuntimeDAO() {
        if (trazabilidadRuntimeDAO == null)
            trazabilidadRuntimeDAO = getRuntimeExceptionDao(Trazabilidad.class);
        return trazabilidadRuntimeDAO;
    }

    @Override
    public void close() {
        super.close();

        usuarioDAO = null;
        usuarioRuntimeDAO = null;

        consultaMedicaDAO = null;
        consultaMedicaRuntimeDAO = null;

        credencialDAO = null;
        credencialRuntimeDAO = null;

        pacienteDAO = null;
        pacienteRuntimeDAO = null;

        parametroDAO = null;
        parametroRuntimeDAO = null;

        informacionPacienteDAO = null;
        informacionPacienteRuntimeDAO = null;

        cie10DAO = null;
        cie10RuntimeDAO = null;

        departamentoDAO = null;
        departamentoRuntimeDAO = null;

        municipioDAO = null;
        municipioRuntimeDAO = null;

        imagenLesionDAO = null;
        imagenLesionRuntimeDAO = null;

        lesionDAO = null;
        lesionRuntimeDAO = null;

        aseguradoraDAO = null;
        aseguradoraRuntimeDAO = null;

        controlMedicoDAO = null;
        parteCuerpoDao = null;
        controlMedicoRuntimeDAO = null;

        diagnosticoDAO = null;
        diagnosticoRuntimeDAO = null;

        especialistaDAO = null;
        especialistaRuntimeDAO = null;

        examenSolicitadoDAO = null;
        examenSolicitadoRuntimeDAO = null;

        formulaDAO = null;
        formulaRuntimeDAO = null;

        requerimientoDAO = null;
        requerimientoRuntimeDAO = null;

        mipresDAO = null;
        mipresRuntimeDAO = null;

        respuestaEspecialistaDAO = null;
        respuestaEspecialistaRuntimeDAO = null;

        imagenAnexoDAO = null;
        imagenAnexoRuntimeDAO = null;

        pendienteDAO = null;
        pendienteRuntimeDAO = null;

        archivoSincronizacionDAO = null;
        archivoSincronizacionDAORuntimeDAO = null;

        consultaEnfermeriaDAO = null;
        consultaEnfermeriaRuntimeDAO = null;

        controlEnfermeriaDAO = null;
        controlEnfermeriaRuntimeDAO = null;

        descartarRequerimientoDao = null;
        descartarRequerimientoRuntimeDao = null;

        helpDeskDAO = null;
        helpDeskRuntimeDAO = null;

        fallidaDAO = null;
        fallidaRuntimeDAO = null;

        trazabilidadDAO = null;
        trazabilidadRuntimeDAO = null;
    }

    @Override
    protected String getPassword() {
        return Utils.getInstance(context).getIMEI().substring(2,6);//las claves de todos los dispositivos son del caracter 2 al 6 del imei no se debe aumentar
    }

}
