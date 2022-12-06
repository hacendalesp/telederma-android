package com.telederma.gov.co.modelo;

import android.util.Log;

import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.ImagenesAnexoRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseImagenesAnexo;
import com.telederma.gov.co.http.response.ResponseLesion;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.ImagenAnexo.NOMBRE_TABLA;

@DatabaseTable(tableName = NOMBRE_TABLA)

public class ImagenAnexo extends BaseEntity implements ISincronizable {

    public static final String NOMBRE_TABLA = "imagen_anexo";
    public static final String NOMBRE_CAMPO_IMAGEN_ANNEXO = "annex_url";
    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";
    public static final String NOMBRE_CAMPO_ID_CONTROL_CONSULTA = "consultation_control_id";
    public static final String NOMBRE_CAMPO_ID_CONTROL_LOCAL = "consultation_control_local";
    public static final String NOMBRE_CAMPO_ID_CONSULTA_LOCAL = "consultation_id_local";

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_LOCAL)
    private String idControlLocal;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA_LOCAL)
    private String idConsultaLocal;

    @DatabaseField(columnName = NOMBRE_CAMPO_IMAGEN_ANNEXO)
    @SerializedName(NOMBRE_CAMPO_IMAGEN_ANNEXO)
    @Expose
    private String imagenAnexo;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private String idConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @Expose
    private String idControlConsulta;

    public String getImagenAnexo() {
        return imagenAnexo;
    }

    public void setImagenAnexo(String imagenAnexo) {
        this.imagenAnexo = imagenAnexo;
    }

    public String getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getIdControlConsulta() {
        return idControlConsulta;
    }

    public void setIdControlConsulta(String idControlConsulta) {
        this.idControlConsulta = idControlConsulta;
    }

    public String getIdControlLocal() {
        return idControlLocal;
    }

    public void setIdControlLocal(String idControlLocal) {
        this.idControlLocal = idControlLocal;
    }

    public String getIdConsultaLocal() {
        return idConsultaLocal;
    }

    public void setIdConsultaLocal(String idConsultaLocal) {
        this.idConsultaLocal = idConsultaLocal;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        String[] im = new DataSincronizacion().getImagenesAnexo(this.getIdConsulta(), this.getIdControlConsulta());
        Observable lesionObservable = service.registrar_imagenes_anexo(new ImagenesAnexoRequest(this.getIdConsulta(), this.getIdControlConsulta(), im, token, email));
        return lesionObservable;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            String a = ImagenAnexo.NOMBRE_CAMPO_ID_SERVIDOR;
            //data.actualizarCampo(NOMBRE_TABLA, ImagenAnexo.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseImagenesAnexo) response.body()).imagenAnexo.getIdServidor().toString(), this.getId().toString(), "id");
            data.eliminarImagenAnexo(new DataSincronizacion().getImagenesAnexoObj(this.getIdConsulta(), this.getIdControlConsulta()));
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        if(throwable.toString() != null){
            Log.e(NOMBRE_TABLA, "exeption====>"+throwable.toString());
        }
       // new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }


}
