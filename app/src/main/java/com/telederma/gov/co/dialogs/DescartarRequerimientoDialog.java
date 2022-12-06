package com.telederma.gov.co.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.telederma.gov.co.MainActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.fragments.BaseFragment;
import com.telederma.gov.co.fragments.ConsultasFragment;
import com.telederma.gov.co.fragments.PatologiaFragment;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.DescartarRequerimiento;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.validation.Validator;

/**
 * Created by Daniel Hernández on 2/08/2018.
 * Comentario: Contiene las opcines de descartar requerimiento de forma quedamadas en los tags del radioGroup
 */

public class DescartarRequerimientoDialog extends TeledermaDialog {

    private Integer idRequerimiento;

    private ViewGroup rgMotivos;
    private EditText txtOtro;
    private Button btnDescartar, btnCancelar;
    private RadioGroup rg_motivo;
    private DescartarRequerimiento descartarRequerimiento;
    protected DBUtil dbUtil;
    private Context context;
    //private VerFirmaUsuarioDialog dialogFirma;

    public DescartarRequerimientoDialog(@NonNull Context contexto, DBUtil dbUtil, Integer idRequerimiento) {
        //public DescartarRequerimientoDialog(@NonNull Context contexto, Integer idConsulta) {
        super(contexto);
        //this.dialogFirma = new VerFirmaUsuarioDialog(contexto);
        this.context = contexto;
        this.dbUtil = dbUtil;
        this.idRequerimiento = idRequerimiento;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rgMotivos = findViewById(R.id.rg_motivo);
        rg_motivo = findViewById(R.id.rg_motivo);
        txtOtro = findViewById(R.id.txt_otro);
        btnDescartar = findViewById(R.id.btn_descartar);
        //this.dialogFirma = new VerFirmaUsuarioDialog(context);


        btnDescartar.setOnClickListener(v -> {
            //Toast.makeText(contexto, "Requerimiento descartado ...", Toast.LENGTH_LONG).show();
            VerFirmaUsuarioDialog dialogFirma = new VerFirmaUsuarioDialog(context);
            dialogFirma.show();
            Button btnEnviar = dialogFirma.findViewById(R.id.btn_enviar);
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnEnviar.setEnabled(false);
                    if (saveRegistro())
                        dialogFirma.dismiss();
                    dismiss();

                    try {
                        Class fragment = ConsultasFragment.class;
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.menu_content, ((BaseFragment) fragment.newInstance())).addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK).commit();
                    } catch (Exception e) {
                    }

                }
            });


        });
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(v -> dismiss());
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_descartar_requerimiento;
    }


    public boolean saveRegistro() {
        boolean is_boolean = false;
        descartarRequerimiento = new DescartarRequerimiento();
        descartarRequerimiento.setIdRequerimiento(idRequerimiento);
        descartarRequerimiento.setIdServidor(idRequerimiento);
        int motivo = 0;

        if (rg_motivo.getCheckedRadioButtonId() != -1) {
            motivo = Integer.parseInt((String) ((RadioButton) findViewById(rg_motivo.getCheckedRadioButtonId())).getTag());
        }

        if (motivo > 0)
            descartarRequerimiento.setReason(motivo);
        if (txtOtro.getText().toString() != null)
            descartarRequerimiento.setOtherReason(txtOtro.getText().toString());
        if (descartarRequerimiento.getReason() == 0 && descartarRequerimiento.getOtherReason() == null) {
            Utils.showGeneralMessage(contexto, "Revisa los campos del formulario");
            return is_boolean;
        }
        dbUtil.crearDescartarRequerimiento(descartarRequerimiento);
        if (descartarRequerimiento.getId() != null) {
            final Session session = Session.getInstance(contexto);

            PendienteSincronizacion pendiente = new PendienteSincronizacion();
            // Nota: Sebastian Perez. Cambio este metodo por que al sincroniza se busca por el id del servidor
            pendiente.setId_local(descartarRequerimiento.getIdServidor().toString());
            pendiente.setIdServidor(descartarRequerimiento.getIdServidor()); // por revisar
            pendiente.setTable(DescartarRequerimiento.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
            // revisar almacenamiento: dbUtil.obtenerRequerimientoServidor("9")
            // revisar almacenamiento: dbUtil.obtenerSiguienteSincronizable()
            // revisar almacenamiento: dbUtil.obtenerSincronizables()
            // lista los controles de enfermería: dbUtil.getDbHelper().getControlEnfermeriaRuntimeDAO().queryForAll()
            is_boolean = true;
        }

        return is_boolean;
    }
}
