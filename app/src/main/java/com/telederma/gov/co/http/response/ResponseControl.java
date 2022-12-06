package com.telederma.gov.co.http.response;

import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sebastian noreña marquez
 */

public class ResponseControl extends BaseResponse {

    @SerializedName("consulta_control")
    @Expose
    public ControlConsulta controlConsulta;

    @SerializedName("control_medico")
    @Expose
    public ControlMedico controlMedico;

}
