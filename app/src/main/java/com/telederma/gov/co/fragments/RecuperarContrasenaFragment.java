package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Fragment que implementa la funcionalidad Recuperar contraseña
 * <p>
 * Created by Daniel Hernández on 6/6/2018.
 */
public class RecuperarContrasenaFragment extends BaseFragment {

    private TextView txtDocumento;
    private Button btnEnviar;


    @Override
    protected int getIdLayout() {
        return R.layout.fragment_recuperar_contrasena;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        txtDocumento = (TextView) rootView.findViewById(R.id.txt_documento);
        btnEnviar = (Button) rootView.findViewById(R.id.btn_enviar);
        btnEnviar.setOnClickListener(this::enviarClickListener);
        txtDocumento.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND)
                enviarClickListener(getView());

            return false;
        });

        return rootView;
    }

    /**
     * Método que implementa la lógica para enviar una solicitud de restaurar contraseña
     * <p>
     * Autor: Daniel Hernández
     *
     * @param view
     */
    private void enviarClickListener(View view) {
        mostrarMensajeEspera(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                enviar();
            }
        });
    }

    private void enviar() {
        if (!utils.validarCamposRequeridos(txtDocumento))
            return;

        final LoginService service = (LoginService) HttpUtils.crearServicio(LoginService.class);

        Observable<Response<BaseResponse>> usuarioObservable = service.recuperarContrasena(
                txtDocumento.getText().toString()
        );

        HttpUtils.configurarObservable(
                contexto, usuarioObservable,
                this::procesarRespuestaEnviar, this::procesarExcepcionServicio
        );
    }

    /**
     * Método que maneja la respuesta obtenida por el servicio web para recuperar contraseña
     * <p>
     * Autor: Daniel Hernández
     *
     * @param response
     */
    private void procesarRespuestaEnviar(Response<BaseResponse> response) {
        ocultarMensajeEspera();

        if (validarRespuestaServicio(response, null) && response.body() != null) {
            mostrarMensaje(response.body().message, MSJ_INFORMACION);
            final Fragment fragment = new IniciarSesionFragment();
            final FragmentManager fm = getActivity().getSupportFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.login_content, fragment).commit();
        }
    }

}
