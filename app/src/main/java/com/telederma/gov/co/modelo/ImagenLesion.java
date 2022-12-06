package com.telederma.gov.co.modelo;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.modelo.ImagenLesion.NOMBRE_TABLA;

@DatabaseTable(tableName = NOMBRE_TABLA)
public class ImagenLesion extends BaseEntity implements ISincronizable {

    public static final String NOMBRE_TABLA = "imagen_lesion";
    public static final String NOMBRE_CAMPO_PHOTO = "photo";
    public static final String NOMBRE_CAMPO_EDITED_PHOTO = "edited_photo";
    public static final String NOMBRE_CAMPO_DESCRIPTION = "description";
    public static final String NOMBRE_CAMPO_INJURY_ID = "injury_id";
    public static final String NOMBRE_CAMPO_ID_INJURY_LOCAL = "id_injury_local";
    public static final String NOMBRE_CAMPO_CONTROL_MEDICO_ID = "medical_control_id";
    public static final String NOMBRE_CAMPO_IMAGE_INJURY_ID = "image_injury_id";


    @DatabaseField(columnName = NOMBRE_CAMPO_ID_INJURY_LOCAL)
    @Expose
    private transient Integer id_injury_local;

    @DatabaseField(columnName = NOMBRE_CAMPO_PHOTO)
    @SerializedName(NOMBRE_CAMPO_PHOTO)
    @Expose
    private String photo;

    @DatabaseField(columnName = NOMBRE_CAMPO_EDITED_PHOTO)
    @SerializedName(NOMBRE_CAMPO_EDITED_PHOTO)
    @Expose
    private String editedPhoto;

    @DatabaseField(columnName = NOMBRE_CAMPO_DESCRIPTION)
    @SerializedName(NOMBRE_CAMPO_DESCRIPTION)
    @Expose
    private String description;

    @DatabaseField(columnName = NOMBRE_CAMPO_INJURY_ID)
    @SerializedName(NOMBRE_CAMPO_INJURY_ID)
    @Expose
    private Integer injury_id;

    @DatabaseField(columnName = NOMBRE_CAMPO_CONTROL_MEDICO_ID)
    @SerializedName(NOMBRE_CAMPO_CONTROL_MEDICO_ID)
    @Expose
    private Integer medical_contol_id;

    @DatabaseField(columnName = NOMBRE_CAMPO_IMAGE_INJURY_ID)
    @SerializedName(NOMBRE_CAMPO_IMAGE_INJURY_ID)
    @Expose
    private Integer image_injury_id;


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getDescription() {
        return (StringUtils.isEmpty(description) ? "" : description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInjury_id() {
        return injury_id;
    }

    public void setInjury_id(Integer injury_id) {
        this.injury_id = injury_id;
    }

    public Integer getId_injury_local() {
        return id_injury_local;
    }

    public void setId_injury_local(Integer id_injury_local) {
        this.id_injury_local = id_injury_local;
    }

    public Integer getMedical_contol_id() {
        return medical_contol_id;
    }

    public void setMedical_contol_id(Integer medical_contol_id) {
        this.medical_contol_id = medical_contol_id;
    }

    public String getEditedPhoto() {
        return editedPhoto;
    }

    public void setEditedPhoto(String editedPhoto) {
        this.editedPhoto = editedPhoto;
    }

    @Override
    public Observable<Response<? extends BaseResponse>> getObservable(String token, String email) {
        return null;
    }

    public Integer getImage_injury_id() {
        return image_injury_id;
    }

    public void setImage_injury_id(Integer image_injury_id) {
        this.image_injury_id = image_injury_id;
    }


    @Override
    public <T> void nextAction(Response<T> response) {
        if (response.code() == 200) {
            DataSincronizacion data = new DataSincronizacion();
            data.eliminarPendiente(this.getId(),NOMBRE_TABLA);
            //data.sincronizar(TeledermaApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
       // new DataSincronizacion().actualizarPendiente(this.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "0");
    }
}
