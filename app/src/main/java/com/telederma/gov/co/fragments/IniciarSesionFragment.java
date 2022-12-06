package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telederma.gov.co.IntroActivity;
import com.telederma.gov.co.MainActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.response.ResponseLogin;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;
import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Fragment que implementa la funcionalidad Iniciar sesión
 * <p>
 * Created by Daniel Hernández on 6/6/2018.
 */
public class IniciarSesionFragment extends BaseFragment implements View.OnClickListener {

    // Constantes para los argumentos de entrada
    public static final String ARG_DOCUMENTO = "ARG_DOCUMENTO";
    public static final String ARG_CONTRASENA = "ARG_CONTRASENA";

    private TextView txtDocumento;
    private TextView txtContrasena;
    private LinearLayout btnOlvideContrasena;
    private Button btnIngresar;
    private Button btnSoyNuevo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(rootView);
        //cambia el color del statusBar
//        Window window = getActivity().getWindow();
//        //clear flag_translucent_status_flag
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //add flag_draws_system_bar_backgrounds flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //finally change the color
//        window.setStatusBarColor(ContextCompat.getColor(contexto, R.color.green));

        txtDocumento = (TextView) rootView.findViewById(R.id.txt_documento);
        txtContrasena = (TextView) rootView.findViewById(R.id.txt_contrasena);
        btnOlvideContrasena = (LinearLayout) rootView.findViewById(R.id.btn_olvide_contrasena);
        btnIngresar = (Button) rootView.findViewById(R.id.btn_ingresar);
        btnSoyNuevo = (Button) rootView.findViewById(R.id.btn_soy_nuevo);

        btnOlvideContrasena.setOnClickListener(this);
        btnIngresar.setOnClickListener(this);
        btnSoyNuevo.setOnClickListener(this);

        // Se valida si tiene argumentos de entrada para iniciar sesión automáticamente
        if (getArguments() != null && !getArguments().isEmpty()
                && !TextUtils.isEmpty(getArguments().getString(ARG_DOCUMENTO))
                && !TextUtils.isEmpty(getArguments().getString(ARG_CONTRASENA))) {
            txtDocumento.setText(getArguments().getString(ARG_DOCUMENTO));
            txtContrasena.setText(getArguments().getString(ARG_CONTRASENA));
            btnIngresar.performClick();
            getArguments().remove(ARG_DOCUMENTO);
            getArguments().remove(ARG_CONTRASENA);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.btn_olvide_contrasena:
                fragment = new RecuperarContrasenaFragment();
                ft.replace(R.id.login_content, fragment).addToBackStack(
                        Constantes.TAG_LOGIN_ACTIVITY_BACK_STACK
                ).commit();

                break;

            case R.id.btn_ingresar:
                if (validarCampos())
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            iniciarSesion();
                        }
                    });
                break;

            case R.id.btn_soy_nuevo:
                fragment = new NuevoUsuarioFragment();
                ft.replace(R.id.login_content, fragment).addToBackStack(
                        Constantes.TAG_LOGIN_ACTIVITY_BACK_STACK
                ).commit();

                break;
        }
    }

    /**
     * Método que implementa la lógica para iniciar sesión en la aplicación
     * <p>
     * Autor: Daniel Hernández
     */
    private void iniciarSesion() {
        LoginService service = (LoginService) HttpUtils.crearServicio(LoginService.class);

        Observable<Response<ResponseLogin>> usuarioObservable = service.iniciarSesion(
                txtDocumento.getText().toString(),
                txtContrasena.getText().toString()
        );

        HttpUtils.configurarObservable(
                contexto, usuarioObservable,
                this::procesarRespuestaIniciarSesion, this::procesarExcepcionServicio
        );
    }

    @Override
    public void procesarExcepcionServicio(Throwable throwable) {
        super.procesarExcepcionServicio(throwable);

        if(throwable instanceof ConnectException)
            actualizarCredencialesUsuarioSesion();
    }

    /**
     * Método que maneja la respuesta obtenida por el servicio web para iniciar sesión
     * <p>
     * Autor: Daniel Hernández
     *
     * @param response
     */
    private void procesarRespuestaIniciarSesion(Response<ResponseLogin> response) {
        if (validarRespuestaServicio(response, null)) {
            ocultarMensajeEspera();
            actualizarCredencialesUsuarioBD(response.body().user);
            actualizarCredencialesUsuarioSesion();
        }
    }

    private boolean validarCampos() {
        final boolean validacionRequeridos = utils.validarCamposRequeridos(
                txtDocumento, txtContrasena
        );

        if (!validacionRequeridos) return false;

        if (!utils.validarContrasena(txtContrasena.getText().toString())) {
            txtContrasena.setError(getResources().getString(R.string.soy_nuevo_msj_contrasena_invalido));
            txtContrasena.requestFocus();

            return false;
        }

        return true;
    }

    /**
     * Método que inserta o actualiza los datos de un usuario en la base de datos local
     * <p>
     * Autor: Daniel Hernández
     *
     * @param usuario
     */
    private void actualizarCredencialesUsuarioBD(Usuario usuario) {
        usuario.setFoto(usuario.getFotoServicio().url);
        usuario.setImagenFirma(usuario.getImagenFirmaServicio().url);
        usuario.setContrasena(txtContrasena.getText().toString());
        dbUtil.crearUsuario(usuario);
    }

    /**
     * Método que actualiza las credenciales del usuario según los datos del usuario
     * de la base de datos local
     * <p>
     * Autor: Daniel Hernández
     */
    private void actualizarCredencialesUsuarioSesion() {
        try {
            RuntimeExceptionDao<Usuario, Integer> usuarioDAO = getDbHelper().getUsuarioRuntimeDAO();
            QueryBuilder<Usuario, Integer> queryBuilder = usuarioDAO.queryBuilder();
            queryBuilder.where().eq(
                    Usuario.NOMBRE_CAMPO_DOCUMENTO, txtDocumento.getText()
            ).and().eq(
                    Usuario.NOMBRE_CAMPO_CONTRASENA, txtContrasena.getText()
            );
            final List<Usuario> usuarios = usuarioDAO.query(queryBuilder.prepare());

            if (!usuarios.isEmpty() && usuarios.size() == 1) {
                final Usuario usuario = usuarios.get(0);
                final Session session = Session.getInstance(getActivity());

                Session.Credentials credentials = new Session.Credentials();
                credentials.setIdUsuario(usuario.getIdServidor());
                credentials.setUsername(usuario.getDocumento());
                credentials.setToken(usuario.getTokenAutenticacion());
                credentials.setEmail(usuario.getEmail());
                credentials.setNombreUsuario(usuario.getNombreCompleto());
                credentials.setImagenFirma(usuario.getImagenFirma());
                credentials.setTipoProfesional(usuario.getTipoProfesional());
                session.setCredentials(credentials);
                session.put(Session.SESSION_LOGUEADO, true);
                session.put(Session.SESSION_IMAGEN_URL_USUARIO, usuario.getFoto());

                mostrarMensaje(R.string.msj_inicio_sesion_bienvenido, MSJ_INFORMACION);

                Intent intent;
                if (!usuario.isTutorial())
                    intent = new Intent(getActivity(), IntroActivity.class).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                    );
                else
                    intent = new Intent(getActivity(), MainActivity.class).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                    );

                final Bundle animation = ActivityOptionsCompat.makeCustomAnimation(
                        contexto, android.R.anim.fade_in, android.R.anim.fade_out
                ).toBundle();

                startActivity(intent, animation);
                getActivity().finish();
            } else {
                mostrarMensaje(R.string.msj_inicio_sesion_no_encuentra_usuario, MSJ_ERROR);
            }
        } catch (SQLException e) {
            Log.e(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error consultando el usuario en inicio de sesión", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_iniciar_sesion;
    }

}
