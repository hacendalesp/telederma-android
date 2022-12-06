package com.telederma.gov.co.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ExpandableListPacientesAdapter;
import com.telederma.gov.co.fragments.BaseFragment;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.ArchivarConsultaRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.DataSincronizacion;
import com.telederma.gov.co.utils.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import retrofit2.Response;


/**
 * Created by Sebastián Noreña Máquez on 25/11/2018.
 */

public class VerOpcionesDialog extends TeledermaDialog {

    private Button btnCompartir, btnArchivar;
    private View.OnClickListener onEnviarClickListener;

    private ExpandableListPacientesAdapter adapter;
    private List<Consulta> consultas = new ArrayList<>();
    private String texto;
    private Drawable icon;

    public VerOpcionesDialog(@NonNull Context contexto, String texto) {
        super(contexto);
        this.texto = texto;
        this.icon = contexto.getResources().getDrawable(R.drawable.archivar_p);
    }

    public VerOpcionesDialog(@NonNull Context contexto, String texto, Drawable drawable) {
        super(contexto);
        this.texto = texto;
        this.icon = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnCompartir = findViewById(R.id.btn_compartir);
        btnArchivar = findViewById(R.id.btn_archivar);
        btnArchivar.setText(texto);
        btnArchivar.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);

        if (!this.texto.isEmpty())
            btnArchivar.setText(this.texto);
        btnArchivar.setOnClickListener(v -> {
            final ConsultaService consultaService = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
            if (consultas.size() > 0) {
                Observable<Response<BaseResponse>> observable = consultaService.archivarConsulta(
                        new ArchivarConsultaRequest(
                                Session.getInstance(contexto).getCredentials().getToken(),
                                Session.getInstance(contexto).getCredentials().getEmail(),
                                consultas.get(0).getIdServidor(),
                                ((consultas.get(0).getArchivado() == null) ? ConsultaService.ARCHIVAR : ConsultaService.DESARCHIVAR)
                        )
                );

                HttpUtils.configurarObservable(
                        this.contexto, observable,
                        response -> {
                            List<Consulta> consultas = adapter.getMapa_consultas().get(this.consultas.get(0).getIdPaciente());
                            Iterator<Consulta> i = consultas.iterator();

                            int countNotArchived = 0;

                            while (i.hasNext()) {
                                Consulta c = i.next();

                                if (c.getEstado().equals(1) || c.getEstado().equals(7) || c.getEstado().equals(4)) {
                                    DataSincronizacion data = new DataSincronizacion();
                                    boolean es_archivar = (c.getArchivado() == null || c.getArchivado() == false) ? false : true;
                                    if (data.getDbUtil().archivarConsulta(String.valueOf(c.getId()), (es_archivar ? null : true), "id")) {

                                        i.remove();
                                    } else {
                                        Log.e("VerOpcionesDialog", "Consulta no actualizada: " + c.getId());
                                    }

                                }

                            }

                            //if (countNotArchived == 0) {
                            //    showToast(response.body().message);
                            //} else {
                            //    if(countNotArchived == consultas.size()){
                            //        showToast(contexto.getString(R.string.lista_consultas_tab_resueltas_archivar_men1));
                            //    } else {
                            //        showToast(contexto.getString(R.string.lista_consultas_tab_resueltas_archivar_men2));
                            //    }
//
                            //}

                            showToast(response.body().message);
                            adapter.notifyDataSetChanged();


                        },
                        exception -> {
                            Toast.makeText(contexto, R.string.msj_error, Toast.LENGTH_SHORT).show();
                            Log.d("COMPARTIR_CONSULTA", "Ha ocurrido un error archivando la consulta", exception);
                        }
                );
            }
            dismiss();
        });

        btnCompartir.setOnClickListener(v -> {

            Dialog compartir = new CompartirConsultaDialog(contexto, new ArrayList<CompartirConsultaDialog.CompartirConsulta>() {
                {
                    for (int i = 0; i < consultas.size(); i++) {
                        add(new CompartirConsultaDialog.CompartirConsulta(
                                ((!consultas.get(i).getImpresionDiagnostica().equals("")) ? consultas.get(i).getImpresionDiagnostica() : contexto.getString(R.string.consultas_pendiente_diagnostico)),
                                consultas.get(i).getIdServidor()
                        ));
                    }
                }
            });
            compartir.show();
            dismiss();
        });

    }

    public void setConsulta(Consulta consulta) {
        this.consultas.clear();
        consultas.add(consulta);
    }


    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public void setOnEnviarClickListener(View.OnClickListener onEnviarClickListener) {
        this.onEnviarClickListener = onEnviarClickListener;

    }

    public void setAdapter(ExpandableListPacientesAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_ver_opciones_usuario;
    }

    public void showToast(String message) {
        if (message == null)
            return;

        Toast.makeText(contexto, message, Toast.LENGTH_LONG).show();

    }

}
