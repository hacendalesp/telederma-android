package com.telederma.gov.co.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagenDermatoscopia {

    public static final String NOMBRE_CAMPO_PHOTO = "photo";
    public static final String NOMBRE_CAMPO_IMAGE_INJURY_ID = "image_injury_id";

    @SerializedName(NOMBRE_CAMPO_PHOTO)
    @Expose
    String photo;

    @SerializedName(NOMBRE_CAMPO_IMAGE_INJURY_ID)
    @Expose
    String image_injury_id;

    public ImagenDermatoscopia(String photo, String image_injury_id) {
        this.photo = photo;
        this.image_injury_id = image_injury_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImage_injury_id() {
        return image_injury_id;
    }

    public void setImage_injury_id(String image_injury_id) {
        this.image_injury_id = image_injury_id;
    }
}
