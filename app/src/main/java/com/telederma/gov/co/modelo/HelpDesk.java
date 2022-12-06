package com.telederma.gov.co.modelo;

import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LesionService;
import com.telederma.gov.co.http.MesaAyudaService;
import com.telederma.gov.co.http.request.LesionRequest;
import com.telederma.gov.co.http.request.SolicitudAyudaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseHelpDesk;
import com.telederma.gov.co.http.response.ResponseLesion;
import com.telederma.gov.co.http.response.ResponsePaciente;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.HelpDesk.NOMBRE_TABLA;

@DatabaseTable(tableName = NOMBRE_TABLA)
public class HelpDesk extends BaseEntity implements ISincronizable {

    public static final Integer ESTADO_MESA_AYUDA_SIN_ENVIAR = 0;
    public static final Integer ESTADO_MESA_AYUDA_PENDIENTE  = 1;
    public static final Integer ESTADO_MESA_AYUDA_EVALUANDO  = 2;
    public static final Integer ESTADO_MESA_AYUDA_RESUELTO   = 3;

    public static final String NOMBRE_TABLA                   = "helpDesk";
    public static final String NOMBRE_CAMPO_DESCRIPTION       = "description";
    public static final String NOMBRE_CAMPO_SUBJET            = "subject";
    public static final String NOMBRE_CAMPO_IMAGE_USER        = "image_user";
    public static final String NOMBRE_CAMPO_USER_ID           = "user_id";
    public static final String NOMBRE_CAMPO_STATUS            = "status";
    public static final String NOMBRE_CAMPO_DEVICE_ID         = "device_id";
    public static final String NOMBRE_CAMPO_TICKET            = "ticket";
    public static final String NOMBRE_CAMPO_RESPONSE_TICKET   = "response_ticket";
    public static final String NOMBRE_CAMPO_IMAGE_ADMIN       = "image_admin";
    public static final String NOMBRE_CAMPO_ADMIN_USER_ID     = "admin_user_id";


    @DatabaseField(columnName = NOMBRE_CAMPO_TICKET)
    @SerializedName(NOMBRE_CAMPO_TICKET)
    @Expose
    private String ticket;

    @DatabaseField(columnName = NOMBRE_CAMPO_RESPONSE_TICKET)
    @SerializedName(NOMBRE_CAMPO_RESPONSE_TICKET)
    @Expose
    private String response_ticket;

    @DatabaseField(columnName = NOMBRE_CAMPO_IMAGE_ADMIN)
    @SerializedName(NOMBRE_CAMPO_IMAGE_ADMIN)
    @Expose
    private String image_admin;


    @DatabaseField(columnName = NOMBRE_CAMPO_STATUS)
    @SerializedName(NOMBRE_CAMPO_STATUS)
    @Expose
    private int status;

    @DatabaseField(columnName = NOMBRE_CAMPO_DEVICE_ID)
    @SerializedName(NOMBRE_CAMPO_DEVICE_ID)
    @Expose
    private String device_id;


    @DatabaseField(columnName = NOMBRE_CAMPO_DESCRIPTION)
    @SerializedName(NOMBRE_CAMPO_DESCRIPTION)
    @Expose
    private String description;

    @DatabaseField(columnName = NOMBRE_CAMPO_SUBJET)
    @SerializedName(NOMBRE_CAMPO_SUBJET)
    @Expose
    private String subject;

    @DatabaseField(columnName = NOMBRE_CAMPO_IMAGE_USER)
    @SerializedName(NOMBRE_CAMPO_IMAGE_USER)
    @Expose
    private String image_user;


    @DatabaseField(columnName = NOMBRE_CAMPO_USER_ID)
    @SerializedName(NOMBRE_CAMPO_USER_ID)
    @Expose
    private int user_id;

    public int getAdmin_user_id() {
        return admin_user_id;
    }

    public void setAdmin_user_id(int admin_user_id) {
        this.admin_user_id = admin_user_id;
    }

    @DatabaseField(columnName = NOMBRE_CAMPO_ADMIN_USER_ID)
    @SerializedName(NOMBRE_CAMPO_ADMIN_USER_ID)
    @Expose
    private int admin_user_id;

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        MesaAyudaService service = (MesaAyudaService) HttpUtils.crearServicio(MesaAyudaService.class);
        DataSincronizacion data = new DataSincronizacion();
        HelpDesk mesa =  data.getDbUtil().getMesaAyuda(this.getId().toString());
        if(mesa != null) {
            Observable lesionObservable = service.enviarSolicitudAyuda(new SolicitudAyudaRequest(token, email,mesa));
            return lesionObservable;
        }
      return null;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        DataSincronizacion data = new DataSincronizacion();
        if (response.code() == 200) {
            data.actualizarCampo(NOMBRE_TABLA, HelpDesk.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseHelpDesk) response.body()).help_desk.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampoFecha(NOMBRE_TABLA, HelpDesk.NOMBRE_CAMPO_UPDATED_AT, ((ResponseHelpDesk) response.body()).help_desk.updated_at, this.getId().toString(), "id");

            //data.actualizarCampo(ImagenLesion.NOMBRE_TABLA, ImagenLesion.NOMBRE_CAMPO_INJURY_ID, ((ResponseLesion) response.body()).lesion.getIdServidor().toString(), this.getId().toString(), ImagenLesion.NOMBRE_CAMPO_ID_INJURY_LOCAL);
            //data.eliminarImagenLesion(new DataSincronizacion().getImagenesLesion(this.getId()));
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable != null){
            throwable.toString();
        }
        //new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }



    public HelpDesk(String description, String subject,String image_user, int userId) {
        this.description = description;
        this.image_user = image_user;
        this.user_id = userId;
        this.subject = subject;
    }

    public HelpDesk() {

    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getResponse_ticket() {
        return response_ticket;
    }

    public void setResponse_ticket(String response_ticket) {
        this.response_ticket = response_ticket;
    }

    public String getImage_admin() {
        return image_admin;
    }

    public void setImage_admin(String image_admin) {
        this.image_admin = image_admin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
