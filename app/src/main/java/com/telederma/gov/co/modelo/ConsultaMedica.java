package com.telederma.gov.co.modelo;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.ConsultaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseConsulta;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.ConsultaMedica.NOMBRE_TABLA;


@DatabaseTable(tableName = NOMBRE_TABLA)
public class ConsultaMedica extends Consulta implements ISincronizable {

    public static final String NOMBRE_TABLA = "consulta_medica";
    public static final String NOMBRE_CAMPO_WEIGHT = "weight";
    public static final String NOMBRE_CAMPO_TIEMPO_EVOLUCION = "evolution_time";
    public static final String NOMBRE_CAMPO_UNIDAD_TIEMPO_EVOLUCION = "unit_measurement";
    public static final String NOMBRE_CAMPO_NUMERO_LESIONES = "number_injuries";
    public static final String NOMBRE_CAMPO_EVOLUCION_LESIONES = "evolution_injuries";
    public static final String NOMBRE_CAMPO_SANGRAN = "blood";
    public static final String NOMBRE_CAMPO_EXUDAN = "exude";
    public static final String NOMBRE_CAMPO_SUPURAN = "suppurate";
    public static final String NOMBRE_CAMPO_SINTOMAS = "symptom";
    public static final String NOMBRE_CAMPO_LOS_SINTOMAS_CAMBIAN = "change_symptom";
    public static final String NOMBRE_CAMPO_OTROS_FACTORES_AGRAVEN_SINTOMAS = "other_factors_symptom";
    public static final String NOMBRE_CAMPO_FACTORES_AGRAVAN = "aggravating_factors";
    public static final String NOMBRE_CAMPO_ANTECEDENTES_FAMILIARES = "family_background";
    public static final String NOMBRE_CAMPO_ANTECEDENTES_PERSONALES = "personal_history";
    public static final String NOMBRE_CAMPO_TRATAMIENTO_RECIBIDO = "treatment_received";
    public static final String NOMBRE_CAMPO_SUSTANCIAS_APLICADAS = "applied_substances";
    public static final String NOMBRE_CAMPO_EFECTO_TRATAMIENTO = "treatment_effects";
    public static final String NOMBRE_CAMPO_DESCRIPTION_EXAM_FISICO = "description_physical_examination";
    public static final String NOMBRE_CAMPO_CAMPO_AUDIO_FISICO = "physical_audio";
    public static final String NOMBRE_CAMPO_ID_INFORMACION_PATIENT_LOCAL = "local_patient_information_id";
    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";

    public static final String NOMBRE_CAMPO_MOTIVO_CONSULTA = "reason_consultation";
    public static final String NOMBRE_CAMPO_ENFERMEDAD_ACTUAL = "current_illness";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_INFORMACION_PATIENT_LOCAL)
    private Integer idInformacionPacienteLocal;

    @DatabaseField(columnName = NOMBRE_CAMPO_TIEMPO_EVOLUCION)
    @SerializedName(NOMBRE_CAMPO_TIEMPO_EVOLUCION)
    @Expose
    private float tiempoEvolucion;

    @DatabaseField(columnName = NOMBRE_CAMPO_UNIDAD_TIEMPO_EVOLUCION)
    @SerializedName(NOMBRE_CAMPO_UNIDAD_TIEMPO_EVOLUCION)
    @Expose
    private Integer unidadMedidaTiempoEvolucion;

    @DatabaseField(columnName = NOMBRE_CAMPO_WEIGHT)
    @SerializedName(NOMBRE_CAMPO_WEIGHT)
    @Expose
    private String weight;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private Integer idConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_NUMERO_LESIONES)
    @SerializedName(NOMBRE_CAMPO_NUMERO_LESIONES)
    @Expose
    private Integer numeroLesiones;

    @DatabaseField(columnName = NOMBRE_CAMPO_EVOLUCION_LESIONES)
    @SerializedName(NOMBRE_CAMPO_EVOLUCION_LESIONES)
    @Expose
    private String evolucionLesiones;

    @DatabaseField(columnName = NOMBRE_CAMPO_SANGRAN)
    @SerializedName(NOMBRE_CAMPO_SANGRAN)
    @Expose
    private Boolean sangran;

    @DatabaseField(columnName = NOMBRE_CAMPO_EXUDAN)
    @SerializedName(NOMBRE_CAMPO_EXUDAN)
    @Expose
    private Boolean exudan;

    @DatabaseField(columnName = NOMBRE_CAMPO_SUPURAN)
    @SerializedName(NOMBRE_CAMPO_SUPURAN)
    @Expose
    private Boolean supuran;

    @DatabaseField(columnName = NOMBRE_CAMPO_SINTOMAS)
    @SerializedName(NOMBRE_CAMPO_SINTOMAS)
    @Expose
    private String sintomas;

    @DatabaseField(columnName = NOMBRE_CAMPO_LOS_SINTOMAS_CAMBIAN)
    @SerializedName(NOMBRE_CAMPO_LOS_SINTOMAS_CAMBIAN)
    @Expose
    private Integer sintomasCambian;

    @DatabaseField(columnName = NOMBRE_CAMPO_OTROS_FACTORES_AGRAVEN_SINTOMAS)
    @SerializedName(NOMBRE_CAMPO_OTROS_FACTORES_AGRAVEN_SINTOMAS)
    @Expose
    private String otrosFactoresAgravenSintomas;

    @DatabaseField(columnName = NOMBRE_CAMPO_FACTORES_AGRAVAN)
    @SerializedName(NOMBRE_CAMPO_FACTORES_AGRAVAN)
    @Expose
    private String factoresAgravan;

    @DatabaseField(columnName = NOMBRE_CAMPO_ANTECEDENTES_FAMILIARES)
    @SerializedName(NOMBRE_CAMPO_ANTECEDENTES_FAMILIARES)
    @Expose
    private String antecedentesFamiliares;

    @DatabaseField(columnName = NOMBRE_CAMPO_ANTECEDENTES_PERSONALES)
    @SerializedName(NOMBRE_CAMPO_ANTECEDENTES_PERSONALES)
    @Expose
    private String antecedentesPersonales;

    @DatabaseField(columnName = NOMBRE_CAMPO_TRATAMIENTO_RECIBIDO)
    @SerializedName(NOMBRE_CAMPO_TRATAMIENTO_RECIBIDO)
    @Expose
    private String tratamientoRecibido;

    @DatabaseField(columnName = NOMBRE_CAMPO_SUSTANCIAS_APLICADAS)
    @SerializedName(NOMBRE_CAMPO_SUSTANCIAS_APLICADAS)
    @Expose
    private String sustanciasAplicadas;

    @DatabaseField(columnName = NOMBRE_CAMPO_EFECTO_TRATAMIENTO)
    @SerializedName(NOMBRE_CAMPO_EFECTO_TRATAMIENTO)
    @Expose
    private String efectoTratamiento;

    @DatabaseField(columnName = NOMBRE_CAMPO_DESCRIPTION_EXAM_FISICO)
    @SerializedName(NOMBRE_CAMPO_DESCRIPTION_EXAM_FISICO)
    @Expose
    private String descripcionExamenFisico;

    @DatabaseField(columnName = NOMBRE_CAMPO_CAMPO_AUDIO_FISICO)
    @SerializedName(NOMBRE_CAMPO_CAMPO_AUDIO_FISICO)
    @Expose
    private String audioFisico;

    @DatabaseField(columnName = NOMBRE_CAMPO_MOTIVO_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_MOTIVO_CONSULTA)
    @Expose
    private String motivoConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_ENFERMEDAD_ACTUAL)
    @SerializedName(NOMBRE_CAMPO_ENFERMEDAD_ACTUAL)
    @Expose
    private String enfermedadActual;

    private List<ControlMedico> controlesMedicos;

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getEnfermedadActual() {
        return enfermedadActual;
    }

    public void setEnfermedadActual(String enfermedadActual) {
        this.enfermedadActual = enfermedadActual;
    }

    public int getTiempoEvolucion() {
        return (int)tiempoEvolucion;
    }

    public void setTiempoEvolucion(float tiempoEvolucion) {
        this.tiempoEvolucion = tiempoEvolucion;
    }

    public Integer getUnidadMedidaTiempoEvolucion() {
        return unidadMedidaTiempoEvolucion;
    }

    public void setUnidadMedidaTiempoEvolucion(Integer unidadMedidaTiempoEvolucion) {
        this.unidadMedidaTiempoEvolucion = unidadMedidaTiempoEvolucion;
    }

    public Integer getNumeroLesiones() {
        return numeroLesiones;
    }

    public void setNumeroLesiones(Integer numeroLesiones) {
        this.numeroLesiones = numeroLesiones;
    }

    public String getEvolucionLesiones() {
        return evolucionLesiones;
    }

    public void setEvolucionLesiones(String evolucionLesiones) {
        this.evolucionLesiones = evolucionLesiones;
    }

    public Boolean getSangran() {
        return sangran;
    }

    public void setSangran(Boolean sangran) {
        this.sangran = sangran;
    }

    public Boolean getExudan() {
        return exudan;
    }

    public void setExudan(Boolean exudan) {
        this.exudan = exudan;
    }

    public Boolean getSupuran() {
        return supuran;
    }

    public void setSupuran(Boolean supuran) {
        this.supuran = supuran;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public Integer getSintomasCambian() {
        return sintomasCambian;
    }

    public void setSintomasCambian(Integer sintomasCambian) {
        this.sintomasCambian = sintomasCambian;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOtrosFactoresAgravenSintomas() {
        return (StringUtils.isEmpty(otrosFactoresAgravenSintomas) ? "Ningúno" : otrosFactoresAgravenSintomas);
    }

    public void setOtrosFactoresAgravenSintomas(String otrosFactoresAgravenSintomas) {
        this.otrosFactoresAgravenSintomas = otrosFactoresAgravenSintomas;
    }

    public String getFactoresAgravan() {
        return factoresAgravan;
    }

    public void setFactoresAgravan(String factoresAgravan) {
        this.factoresAgravan = factoresAgravan;
    }

    public String getAntecedentesFamiliares() {
        return antecedentesFamiliares;
    }

    public void setAntecedentesFamiliares(String antecedentesFamiliares) {
        this.antecedentesFamiliares = antecedentesFamiliares;
    }

    public String getAntecedentesPersonales() {
        return antecedentesPersonales;
    }

    public void setAntecedentesPersonales(String antecedentesPersonales) {
        this.antecedentesPersonales = antecedentesPersonales;
    }

    public String getTratamientoRecibido() {
        return tratamientoRecibido;
    }

    public void setTratamientoRecibido(String tratamientoRecibido) {
        this.tratamientoRecibido = tratamientoRecibido;
    }

    public String getSustanciasAplicadas() {
        return (StringUtils.isEmpty(sustanciasAplicadas) ? "Ninguna" : sustanciasAplicadas);
    }

    public void setSustanciasAplicadas(String sustanciasAplicadas) {
        this.sustanciasAplicadas = sustanciasAplicadas;
    }

    public String getEfectoTratamiento() {
        return efectoTratamiento;
    }

    public void setEfectoTratamiento(String efectoTratamiento) {
        this.efectoTratamiento = efectoTratamiento;
    }

    public String getDescripcionExamenFisico() {
        return descripcionExamenFisico;
    }

    public void setDescripcionExamenFisico(String descripcionExamenFisico) {
        this.descripcionExamenFisico = descripcionExamenFisico;
    }

    public String getAudioFisico() {
        return audioFisico;
    }

    public void setAudioFisico(String audioFisico) {
        this.audioFisico = audioFisico;
    }

    public Integer getIdInformacionPacienteLocal() {
        return idInformacionPacienteLocal;
    }

    public void setIdInformacionPacienteLocal(Integer idInformacionPacienteLocal) {
        this.idInformacionPacienteLocal = idInformacionPacienteLocal;
    }

    public String obtenerAntecedentesRelevantes() {
        final StringBuilder antecedentes = new StringBuilder();
        String temp_personales = getAntecedentesPersonales();
        temp_personales = StringUtils.isEmpty(getAntecedentesPersonales()) ? "No reporta" : temp_personales;

        antecedentes.append("Personales: "+temp_personales);

        if (antecedentes.length() == 0) {
            antecedentes.append("Personales: " + temp_personales);
            antecedentes.append(("\n\nFamiliares: No reporta"));
        }
        else {
            antecedentes.append(("\n\nFamiliares: " + getAntecedentesFamiliares()));
            //antecedentes.append(String.format("\n %s", "Personales: "+getAntecedentesPersonales()));
        }
        return antecedentes.toString();
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public List<ControlMedico> getControlesMedicos() {
        return controlesMedicos;
    }

    public void setControlesMedicos(List<ControlMedico> controlesMedicos) {
        this.controlesMedicos = controlesMedicos;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        if(this.getIdInformacionPaciente() == null){
            DataSincronizacion data = new DataSincronizacion();
            InformacionPaciente informacionPaciente =  data.getDbUtil().obtenerInformacionPacienteFomIdLocal(this.getIdInformacionPacienteLocal());
            if(informacionPaciente != null){
                if(informacionPaciente.getIdServidor() != null) {
                    this.setIdInformacionPacienteLocal(informacionPaciente.getIdServidor());
                    data.actualizarCampo(ConsultaMedica.NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_ID_INFORMACION_PACIENTE, informacionPaciente.getIdServidor().toString(), this.getId().toString(), "id");
                }else{
                    // TODO: Sebas - 2/26/19 enviar al servidor el cliente cuando aún falta
//                    HttpUtils.configurarObservable(data.getDbUtil().context, informacionPaciente.getObservable(token, email),
//                            r -> informacionPaciente.nextAction(r),
//                            r -> informacionPaciente.procesarExcepcionServicio(r)
//                    );
                }
            }
        }
        this.setImpresionDiagnostica(this.getCiediezcode());
        Observable pacienteObservable = service.registrar(new ConsultaRequest(this, this, token, email));
        return pacienteObservable;
    }


    @Override
    public  <T> void nextAction(Response<T> response) {
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.actualizarCampo(NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseConsulta) response.body()).consultaMedica.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampo(NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_ID_CONSULTA, ((ResponseConsulta) response.body()).consulta.getIdServidor().toString(), this.getId().toString(), "id");

            data.actualizarCampoFecha(NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_CREATED_AT, ((ResponseConsulta) response.body()).consulta.created_at, this.getId().toString(), "id");
            data.actualizarCampoFecha(NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_UPDATED_AT, ((ResponseConsulta) response.body()).consulta.updated_at, this.getId().toString(), "id");
            //data.actualizarCampo(NOMBRE_TABLA, ConsultaMedica.NOMBRE_CAMPO_ID_SERVIDOR, Consulta.ESTADO_CONSULTA_PENDIENTE.toString(), this.getId().toString(), "id");
            data.actualizarCampo(Lesion.NOMBRE_TABLA, Lesion.NOMBRE_CAMPO_ID_CONSULTA, ((ResponseConsulta) response.body()).consulta.getIdServidor().toString(), this.getId().toString(), Lesion.NOMBRE_CAMPO_ID_CONSULT_LOCAL);
            data.actualizarCampo(ImagenAnexo.NOMBRE_TABLA, ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA, ((ResponseConsulta) response.body()).consulta.getIdServidor().toString(), this.getId().toString(), ImagenAnexo.NOMBRE_CAMPO_ID_CONSULTA_LOCAL);
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
        }

    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable != null){
            throwable.toString();
        }
       // new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }





}
