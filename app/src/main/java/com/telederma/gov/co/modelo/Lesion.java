package com.telederma.gov.co.modelo;

import android.graphics.Bitmap;

import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LesionService;
import com.telederma.gov.co.http.request.LesionRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseLesion;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.telederma.gov.co.utils.FileUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.Lesion.NOMBRE_TABLA;

@DatabaseTable(tableName = NOMBRE_TABLA)
public class Lesion extends BaseEntity implements ISincronizable {

    public static final String NOMBRE_TABLA = "lesion";
    public static final String NOMBRE_CAMPO_ID_CONSULTA = "consultation_id";
    public static final String NOMBRE_CAMPO_ID_CONTROL_CONSULTA = "consultation_control_id";
    public static final String NOMBRE_CAMPO_IMAGENES_LESION = "images_injuries";
    public static final String NOMBRE_CAMPO_NAME_AREA = "name_area";
    public static final String NOMBRE_CAMPO_BODY_AREA_ID = "body_area_id";
    public static final String NOMBRE_CAMPO_ID_CONSULT_LOCAL = "id_consult_local";
    public static final String NOMBRE_CAMPO_ID_CONTROL_LOCAL = "id_control_local";
    public static final String NOMBRE_CAMPO_ID_REQUERIMIENTO= "id_requerimiento";
    public static final String NOMBRE_CAMPO_ID_ENVIADO = "enviado";
    public static final String NOMBRE_CAMPO_BODY_NAME = "body_name";


    @DatabaseField(columnName = NOMBRE_CAMPO_NAME_AREA)
    @SerializedName(NOMBRE_CAMPO_NAME_AREA)
    @Expose
    private String nameArea;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_ENVIADO)
    private Boolean enviado;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULT_LOCAL)
    private Integer idConsultaLocal;


    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_LOCAL)
    private Integer idControlLocal;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_REQUERIMIENTO)
    private Integer idRequerimiento;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONSULTA)
    @Expose
    private Integer idConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @SerializedName(NOMBRE_CAMPO_ID_CONTROL_CONSULTA)
    @Expose
    private Integer idControlConsulta;

    @DatabaseField(columnName = NOMBRE_CAMPO_BODY_AREA_ID)
    @SerializedName(NOMBRE_CAMPO_BODY_AREA_ID)
    @Expose
    private Integer idBodyArea;

    @SerializedName(NOMBRE_CAMPO_IMAGENES_LESION)
    @Expose
    private List<ImagenLesion> imagenesLesion;

    @DatabaseField(columnName = NOMBRE_CAMPO_BODY_NAME)
    @SerializedName(NOMBRE_CAMPO_BODY_NAME)
    @Expose
    private String bodyName;


    public String getNameArea() {
        return nameArea;
    }

    public void setNameArea(String nameArea) {
        this.nameArea = nameArea;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Integer getIdConsultaLocal() {
        return idConsultaLocal;
    }

    public void setIdConsultaLocal(Integer idConsultaLocal) {
        this.idConsultaLocal = idConsultaLocal;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Integer getIdBodyArea() {
        return idBodyArea;
    }

    public void setIdBodyArea(Integer idBodyArea) {
        this.idBodyArea = idBodyArea;
    }

    public List<ImagenLesion> getImagenesLesion() {
        return imagenesLesion;
    }

    public void setImagenesLesion(List<ImagenLesion> imagenesLesion) {
        this.imagenesLesion = imagenesLesion;
    }

    public Integer getIdControlConsulta() {
        return idControlConsulta;
    }

    public String getBodyName() {
        return bodyName;
    }

    public void setBodyName(String bodyName) {
        this.bodyName = bodyName;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        LesionService service = (LesionService) HttpUtils.crearServicio(LesionService.class);
        List<ImagenLesion> listImg = new DataSincronizacion().getImagenesLesion(this.getId());
        //String[] imagenes = new String[listImg.size()];
        //for (int i = 0; i < imagenes.length; i++) {
        //    imagenes[i] = listImg.get(i).getPhoto();
        //}

        ImagenDermatoscopia[] imagenes =  new ImagenDermatoscopia[listImg.size()];
        try{
            for (int i = 0; i < listImg.size(); i++){
                String path = obtenerPathImage_injury_id(listImg, listImg.get(i).getImage_injury_id());
                imagenes[i] = new ImagenDermatoscopia(listImg.get(i).getPhoto(), path);
            }
        }catch (Exception e){

        }



        Observable lesionObservable = service.registrar(new LesionRequest(this, imagenes, 1, token, email));
        return lesionObservable;
    }

    @Override
    public <T> void nextAction(Response<T> response) {
        DataSincronizacion data = new DataSincronizacion();
        if (response.code() == 200) {
            data.actualizarCampo(NOMBRE_TABLA, Lesion.NOMBRE_CAMPO_ID_SERVIDOR, ((ResponseLesion) response.body()).lesion.getIdServidor().toString(), this.getId().toString(), "id");
            data.actualizarCampo(ImagenLesion.NOMBRE_TABLA, ImagenLesion.NOMBRE_CAMPO_INJURY_ID, ((ResponseLesion) response.body()).lesion.getIdServidor().toString(), this.getId().toString(), ImagenLesion.NOMBRE_CAMPO_ID_INJURY_LOCAL);
            data.eliminarImagenLesion(new DataSincronizacion().getImagenesLesion(this.getId()));
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


    public Integer getIdControlLocal() {
        return idControlLocal;
    }

    public void setIdControlLocal(Integer idControlLocal) {
        this.idControlLocal = idControlLocal;
    }

    public void setIdControlConsulta(Integer idControlConsulta) {
        this.idControlConsulta = idControlConsulta;
    }

    public Integer getIdRequerimiento() {
        return idRequerimiento;
    }

    public void setIdRequerimiento(Integer idRquerimiento) {
        this.idRequerimiento = idRquerimiento;
    }

    public String obtenerPathImage_injury_id(List<ImagenLesion> listImg, Integer id){
        try{
            for(int i = 0; i < listImg.size(); i++){
                if(listImg.get(i).getId().equals(id)){

                    return listImg.get(i).getPhoto();
                }
            }
        }catch (Exception e){

        }
        return null;
    }

}
