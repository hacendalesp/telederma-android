package com.telederma.gov.co.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.FirmaDigitalActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.Custom2SpinnerAdapter;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.dialogs.VerTextoDialog;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.request.UsuarioRequest;
import com.telederma.gov.co.http.response.ResponseLogin;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class NuevoUsuarioFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_FOTO_PERFIL = 100;
    private static final int REQUEST_FIRMA_DIGITAL = 2001;

    private String firma;

    private Custom2SpinnerAdapter<Parametro> parametrosAdapter;

    private ImageView imgProfile;
    private FloatingActionButton fabCapturar;
    private Spinner spnTipoProfesional;
    private TextView txtDocumento, txtDocumentoConfirmar, txtNroTarjProf, txtNombres, txtApellidos,
            txtNroCelular, txtCorreo, txtContrasena, txtContrasenaConfirmar;
    private CheckBox chkAceptarTerminos;
    private LinearLayout btnVerTerminos;
    private Button btnRegistrarme, btnFirmar;

    private Usuario usuario;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(rootView);
        usuario = new Usuario();

        parametrosAdapter = new Custom2SpinnerAdapter<>(
                getActivity(),
                dbUtil.obtenerParametros(Parametro.TIPO_PARAMETRO_TIPO_PROFESIONAL).stream().filter(
                        // Se cargan sólo las opciones Medicina y Enfermería
                        //parametro -> Integer.valueOf(1).equals(parametro.getValor()) || Integer.valueOf(2).equals(parametro.getValor())
                        parametro -> (parametro.getValor() == 1)
                ).collect(Collectors.toList()),
                Parametro.class
        );
        spnTipoProfesional = (Spinner) rootView.findViewById(R.id.spn_tipo_profesional);
        spnTipoProfesional.setAdapter(parametrosAdapter);

        // Se pone por defecto en tipo de profesional Medicina
        spnTipoProfesional.setSelection(1);

        imgProfile = (ImageView) rootView.findViewById(R.id.img_profile);
        fabCapturar = (FloatingActionButton) rootView.findViewById(R.id.fab_capturar);
        txtDocumento = (TextView) rootView.findViewById(R.id.txt_documento);
        txtDocumentoConfirmar = (TextView) rootView.findViewById(R.id.txt_documento_confirmar);
        txtNroTarjProf = (TextView) rootView.findViewById(R.id.txt_nro_tarj_prof);
        txtNombres = (TextView) rootView.findViewById(R.id.txt_nombres);
        txtApellidos = (TextView) rootView.findViewById(R.id.txt_apellidos);
        txtNroCelular = (TextView) rootView.findViewById(R.id.txt_nro_celular);
        txtCorreo = (TextView) rootView.findViewById(R.id.txt_correo);
        txtContrasena = (TextView) rootView.findViewById(R.id.txt_contrasena);
        txtContrasenaConfirmar = (TextView) rootView.findViewById(R.id.txt_contrasena_confirmar);
        chkAceptarTerminos = (CheckBox) rootView.findViewById(R.id.chk_aceptar_terminos);
        btnVerTerminos = (LinearLayout) rootView.findViewById(R.id.btn_ver_terminos);
        btnRegistrarme = (Button) rootView.findViewById(R.id.btn_registrarme);
        btnFirmar = (Button) rootView.findViewById(R.id.btn_firmar);

        fabCapturar.setOnClickListener(this);
        btnVerTerminos.setOnClickListener(this);
        btnRegistrarme.setOnClickListener(this);
        btnFirmar.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_capturar:
                final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(cameraIntent, REQUEST_FOTO_PERFIL);

                break;

            case R.id.btn_ver_terminos:
                final String tcRaw = FileUtils.leerArchivoRaw(contexto, Constantes.RAW_FILE_TERMINOS_Y_CONDICIONES);
                new VerTextoDialog(contexto, R.string.dialog_terminos_condiciones_title, tcRaw).show();

                break;

            case R.id.btn_firmar:
                 final Intent intent = new Intent(FirmaDigitalActivity.ACTION_FIRMA_DIGITAL);
                //final Intent intent = new Intent(TeledermaApplication.getInstance().getApplicationContext(), FirmaDigitalActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // TeledermaApplication.getInstance().getApplicationContext().startActivity(intent);

                if (!TextUtils.isEmpty(firma))
                    intent.putExtra(FirmaDigitalActivity.INTENT_EXTRA_FIRMA, firma);

                startActivityForResult(intent, REQUEST_FIRMA_DIGITAL);

                break;
            case R.id.btn_registrarme:
                if (validarCampos())
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            registrarUsuario();
                        }
                    });

                break;
        }
    }

    private void registrarUsuario() {
        LoginService service = (LoginService) HttpUtils.crearServicio(LoginService.class);

        llenarUsuario();

        Observable<Response<ResponseLogin>> usuarioObservable = service.registrarUsuario(
                new UsuarioRequest(usuario)
        );

        HttpUtils.configurarObservable(
                contexto, usuarioObservable,
                this::procesarRespuestaRegistrarUsuario, this::procesarExcepcionServicio
        );
    }

    private void llenarUsuario() {
        usuario.setDocumento(txtDocumento.getText().toString());
        usuario.setNombres(txtNombres.getText().toString());
        usuario.setApellidos(txtApellidos.getText().toString());
        usuario.setTipoProfesional(((Parametro) spnTipoProfesional.getSelectedItem()).getValor());
        usuario.setTarjetaProfesional(txtNroTarjProf.getText().toString());
        usuario.setTelefono(txtNroCelular.getText().toString());
        usuario.setTerminosCondiciones(chkAceptarTerminos.isChecked());
        usuario.setFirmaDigital("00000");
        usuario.setTutorial(false);
        usuario.setContrasena(txtContrasena.getText().toString());
        usuario.setContrasena_confirmacion(txtContrasenaConfirmar.getText().toString());
        usuario.setEmail(txtCorreo.getText().toString());

        try {
            final Bitmap imagenFirma = FileUtils.obtenerImagenTemporal(firma);
            usuario.getImagenFirmaServicio().url = FileUtils.codificarImagenABase64(imagenFirma);
        } catch (FileNotFoundException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error obteniendo la imagen de la firma", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    private void procesarRespuestaRegistrarUsuario(Response<ResponseLogin> response) {
        final Map<String, Map<String, View>> camposValidar = new HashMap<>();
        camposValidar.put("user", new HashMap<>());
        camposValidar.get("user").put(Usuario.NOMBRE_CAMPO_DOCUMENTO, txtDocumento);
        camposValidar.get("user").put(Usuario.NOMBRE_CAMPO_EMAIL, txtCorreo);

        if (validarRespuestaServicio(response, camposValidar)) {
            ocultarMensajeEspera();
            final Fragment fragment = new IniciarSesionFragment();
            final FragmentManager fm = getActivity().getSupportFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            final Bundle args = new Bundle();
            args.putString(IniciarSesionFragment.ARG_DOCUMENTO, usuario.getDocumento());
            args.putString(IniciarSesionFragment.ARG_CONTRASENA, usuario.getContrasena());
            fragment.setArguments(args);
            ft.replace(R.id.login_content, fragment).addToBackStack(
                    Constantes.TAG_LOGIN_ACTIVITY_BACK_STACK
            ).commit();
        }
    }

    private boolean validarCampos() {
        boolean validacionCampos = utils.validarCamposRequeridos(
                txtDocumento, txtDocumentoConfirmar, spnTipoProfesional, txtNroTarjProf, txtNombres, txtApellidos,
                txtNroCelular, txtCorreo, txtContrasena, txtContrasenaConfirmar, chkAceptarTerminos
        );

        if (TextUtils.isEmpty(firma)) {
            mostrarMensaje(R.string.soy_nuevo_firma_msj_requerido, MSJ_ERROR);
            btnFirmar.setError(getResources().getString(R.string.soy_nuevo_firma_msj_requerido));
            validacionCampos = false;
        }

        if (!TextUtils.equals(txtDocumento.getText(), txtDocumentoConfirmar.getText())) {
            txtDocumentoConfirmar.setError(getResources().getString(R.string.soy_nuevo_msj_confirmar_documento));
            txtDocumentoConfirmar.requestFocus();
            validacionCampos = false;
        }

        if (!utils.validarEmail(txtCorreo.getText().toString())) {
            txtCorreo.setError(getResources().getString(R.string.soy_nuevo_msj_email_invalido));
            txtCorreo.requestFocus();
            validacionCampos = false;
        }

        if (!utils.validarContrasena(txtContrasena.getText().toString())) {
            txtContrasena.setError(getResources().getString(R.string.soy_nuevo_msj_contrasena_invalido));
            txtContrasena.requestFocus();
            validacionCampos = false;
        }

        if (!TextUtils.equals(txtContrasena.getText(), txtContrasenaConfirmar.getText())) {
            txtContrasenaConfirmar.setError(getResources().getString(R.string.soy_nuevo_msj_confirmar_contrasena));
            txtContrasenaConfirmar.requestFocus();
            validacionCampos = false;
        }

        return validacionCampos;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FOTO_PERFIL) {
                final Bitmap foto = (Bitmap) data.getExtras().get("data");
                imgProfile.setImageBitmap(foto);
                usuario.getFotoServicio().url = FileUtils.codificarImagenABase64(foto);
            } else if (requestCode == REQUEST_FIRMA_DIGITAL) {
                firma = data.getExtras().getString("data");
            }
        }





    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_nuevo_usuario;
    }

}
