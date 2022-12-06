package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.R;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.response.ResponseCredito;
import com.telederma.gov.co.utils.Session;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Daniel Hernández on 8/08/2018.
 */

public class GestionCreditosFragment extends BaseFragment {
    TextView total ;
    TextView  consumido ;
    @Override
    protected int getIdLayout() {
        return R.layout.fragment_gestion_creditos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        final ConsultaService consultaService = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);
        total = (TextView) rootView.findViewById(R.id.total);
        consumido = (TextView) rootView.findViewById(R.id.consumidos);

        Observable<Response<ResponseCredito>> observable = consultaService.consultarCreditos(
                Session.getInstance(contexto).getCredentials().getEmail(),
                Session.getInstance(contexto).getCredentials().getToken()

        );

        HttpUtils.configurarObservable(
                this.contexto, observable,
                response -> {
                    if(response.body().consumidos != null && response.body().total !=null)
                    {
                       total.setText(""+String.format("%,d", Math.round(response.body().total)));
                       consumido.setText(" "+String.format("%,d", Math.round(response.body().consumidos)));
                    }

                },
                exception -> {
                    Toast.makeText(contexto, R.string.msj_error, Toast.LENGTH_SHORT).show();
                    Log.d("COMPARTIR_CONSULTA", "Ha ocurrido un error archivando la consulta", exception);
                }
        );

        return rootView;
    }
}
