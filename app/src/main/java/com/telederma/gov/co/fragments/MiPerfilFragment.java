package com.telederma.gov.co.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.telederma.gov.co.FirmaDigitalActivity;
import com.telederma.gov.co.LoginActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.request.EditarUsuarioRequest;
import com.telederma.gov.co.http.response.ResponseLogin;
import com.telederma.gov.co.http.response.ResponseUpdate;
import com.telederma.gov.co.interfaces.IDownloadView;
import com.telederma.gov.co.interfaces.IOpcionMenu;
import com.telederma.gov.co.modelo.Cie10;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.tasks.DescargarArchivoTask;
import com.telederma.gov.co.tasks.ParamDownload;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;
import com.telederma.gov.co.utils.Session;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;
import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class MiPerfilFragment extends BaseFragment implements IOpcionMenu, View.OnClickListener, IDownloadView {

    private static final int REQUEST_FOTO_PERFIL = 100;
    private static final int REQUEST_FIRMA_DIGITAL = 200;
    public static final String IMAGEN_FIRMA_USUARIO = FirmaDigitalActivity.NOMBRE_IMAGEN_FIRMA;
    public static final String IMAGEN_FOTO_PERFIL = "imagen_foto_perfil";

    private EditText txtDocumento, txtNombre, txtApellido, txtCelular, txtCorreo, ed_current_password, ed_confirm_new_password, ed_new_password;
    private Button btnFirma, btnGuardar;
    private ImageView imgPerfil;
    private FloatingActionButton fabCapturar;
    private Usuario usuario;
    private CheckBox cb_change_password;
    private LinearLayout ll_content_change_password;

    private ArchivoDescarga archivoDescargaFotoPerfil;
    private ArchivoDescarga archivoDescargaFirma;
    private String tag = "MiPerfil";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        usuario = dbUtil.obtenerUsuarioLogueado(Session.getInstance(contexto).getCredentials().getIdUsuario());

        txtDocumento = rootView.findViewById(R.id.txt_documento);
        txtNombre = rootView.findViewById(R.id.txt_nombres);
        txtApellido = rootView.findViewById(R.id.txt_apellidos);
        txtCelular = rootView.findViewById(R.id.txt_nro_celular);
        txtCorreo = rootView.findViewById(R.id.txt_correo);
        btnFirma = rootView.findViewById(R.id.btn_firmar);
        btnGuardar = rootView.findViewById(R.id.btn_guardar);
        imgPerfil = rootView.findViewById(R.id.img_profile);
        fabCapturar = rootView.findViewById(R.id.fab_capturar);
        ed_current_password = rootView.findViewById(R.id.ed_current_password);
        ed_new_password = rootView.findViewById(R.id.ed_new_password);
        ed_confirm_new_password = rootView.findViewById(R.id.ed_confirm_new_password);
        cb_change_password = rootView.findViewById(R.id.cb_change_password);
        ll_content_change_password = rootView.findViewById(R.id.ll_content_change_password);

        inicializar();

        return rootView;
    }

    private void inicializar() {
        ll_content_change_password.setVisibility(View.GONE);
        txtDocumento.setText(usuario.getDocumento());
        txtNombre.setText(usuario.getNombres());
        txtApellido.setText(usuario.getApellidos());
        txtCelular.setText(usuario.getTelefono());
        txtCorreo.setText(usuario.getEmail());

        btnGuardar.setOnClickListener(this);
        btnFirma.setOnClickListener(this);
        fabCapturar.setOnClickListener(this);

        btnFirma.setEnabled(false);
        btnFirma.setBackgroundTintList(contexto.getColorStateList(R.color.grayDark));
        btnFirma.setText(R.string.mi_perfil_progreso_descargar);

        if (!TextUtils.isEmpty(usuario.getFoto())) {
            archivoDescargaFotoPerfil = new ArchivoDescarga(
                    usuario.getFoto(),
                    Constantes.DIRECTORIO_TEMPORAL,
                    IMAGEN_FOTO_PERFIL
            );
            archivoDescargaFotoPerfil.setKey(0);
            new DescargarArchivoTask().execute(
                    new ParamDownload(this, this.archivoDescargaFotoPerfil)
            );
        }

        archivoDescargaFirma = new ArchivoDescarga(
                usuario.getImagenFirma(),
                Constantes.DIRECTORIO_TEMPORAL,
                IMAGEN_FIRMA_USUARIO
        );
        archivoDescargaFirma.setKey(1);
        new DescargarArchivoTask().execute(
                new ParamDownload(this, this.archivoDescargaFirma)
        );


        cb_change_password.setOnCheckedChangeListener(changeCheckListener);
    }


    private CompoundButton.OnCheckedChangeListener changeCheckListener =
        new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ll_content_change_password.setVisibility(View.VISIBLE);
                else {
                    ll_content_change_password.setVisibility(View.GONE);
                    //ed_new_password.setText("");
                    //ed_confirm_new_password.setText("");
                }
            }
    };

    private void llenarUsuario() {
        usuario.setDocumento(txtDocumento.getText().toString());
        usuario.setNombres(txtNombre.getText().toString());
        usuario.setApellidos(txtApellido.getText().toString());
        usuario.setTelefono(txtCelular.getText().toString());
        usuario.setEmail(txtCorreo.getText().toString());
        String current_password = ed_current_password.getText().toString();
        usuario.setCurrent_password(current_password);

        if(!cb_change_password.isChecked()) {
            usuario.setContrasena(current_password);
            usuario.setContrasena_confirmacion(current_password);
        }else{
            usuario.setContrasena(ed_new_password.getText().toString());
            usuario.setContrasena_confirmacion(ed_confirm_new_password.getText().toString());
        }

        try {
            final Bitmap imagenFirma = FileUtils.obtenerImagenTemporal(IMAGEN_FIRMA_USUARIO);
            usuario.getImagenFirmaServicio().url = FileUtils.codificarImagenABase64(imagenFirma);
        } catch (FileNotFoundException e) {
            Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error obteniendo la imagen de la firma", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }
    }

    private void guardarCambios() {
        llenarUsuario();
        LoginService service = (LoginService) HttpUtils.crearServicio(LoginService.class);
        Observable<Response<ResponseUpdate>> usuarioObservable = service.editarUsuario(
                new EditarUsuarioRequest(
                        Session.getInstance(contexto).getCredentials().getToken(),
                        Session.getInstance(contexto).getCredentials().getEmail(),
                        usuario
                )
        );
        HttpUtils.configurarObservable(
                contexto, usuarioObservable,
                this::procesarRespuestaServicio,
                this::procesarExcepcionServicio
        );
    }

    private void procesarRespuestaServicio(Response<ResponseUpdate> response) {
        final Map<String, Map<String, View>> camposValidar = new HashMap<>();
        camposValidar.put("user", new HashMap<>());
        camposValidar.get("user").put(Usuario.NOMBRE_CAMPO_EMAIL, txtCorreo);

        if (validarRespuestaServicio(response, null)) {
            mostrarMensaje(response.body().message, MSJ_INFORMACION);
            Session.getInstance(contexto).invalidate();

            final Intent intent = new Intent(contexto, LoginActivity.class);
            intent.putExtra(IniciarSesionFragment.ARG_DOCUMENTO, usuario.getDocumento());
            intent.putExtra(IniciarSesionFragment.ARG_CONTRASENA, usuario.getContrasena());
            startActivity(intent);
        }
    }

    private void editarFirma() {
        final Intent intent = new Intent(FirmaDigitalActivity.ACTION_FIRMA_DIGITAL);
        intent.putExtra(FirmaDigitalActivity.INTENT_EXTRA_FIRMA, IMAGEN_FIRMA_USUARIO);
        startActivityForResult(intent, REQUEST_FIRMA_DIGITAL);
    }

    private void editarFoto() {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(cameraIntent, REQUEST_FOTO_PERFIL);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_mi_perfil;
    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_mi_perfil;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_guardar:
                if (validarCampos())
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            guardarCambios();
                        }
                    });

                break;
            case R.id.btn_firmar:
                editarFirma();

                break;
            case R.id.fab_capturar:
                editarFoto();

                break;
        }
    }

    private boolean validarCampos() {
        boolean validacionCampos = utils.validarCamposRequeridos(
                txtDocumento, txtNombre, txtApellido, txtCelular, txtCorreo, ed_current_password
        );

        if (TextUtils.isEmpty(usuario.getImagenFirma())) {
            mostrarMensaje(R.string.soy_nuevo_firma_msj_requerido, MSJ_ERROR);
            btnFirma.setError(getResources().getString(R.string.soy_nuevo_firma_msj_requerido));
            validacionCampos = false;
        }

        if (!utils.validarEmail(txtCorreo.getText().toString())) {
            txtCorreo.setError(getResources().getString(R.string.soy_nuevo_msj_email_invalido));
            txtCorreo.requestFocus();
            validacionCampos = false;
        }

        if (cb_change_password.isChecked() && !TextUtils.equals(ed_new_password.getText(), ed_confirm_new_password.getText())) {
            ed_confirm_new_password.setError(getResources().getString(R.string.soy_nuevo_msj_confirmar_contrasena));
            ed_confirm_new_password.requestFocus();
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
                imgPerfil.setImageBitmap(foto);
                usuario.getFotoServicio().url = FileUtils.codificarImagenABase64(foto);
            } else if (requestCode == REQUEST_FIRMA_DIGITAL) {
                usuario.setImagenFirma(data.getExtras().getString("data"));
            }
        }
    }

    @Override
    public void updateDownloadProgress(Integer key, Integer progress) {
    }

    @Override
    public synchronized void onFinishDownload(Integer key, Boolean fileDownloaded) {
        if (key == archivoDescargaFirma.getKey()) {
            btnFirma.setEnabled(true);
            btnFirma.setBackgroundTintList(contexto.getColorStateList(R.color.colorPrimaryDark));

            if (fileDownloaded)
                btnFirma.setText(R.string.mi_perfil_firma);
            else
                btnFirma.setText(R.string.mi_perfil_firma_error);
        } else if (key == archivoDescargaFotoPerfil.getKey()) {
            if (fileDownloaded)
                try {
                    final Bitmap foto = FileUtils.obtenerImagenTemporal(IMAGEN_FOTO_PERFIL);
                    imgPerfil.setImageBitmap(foto);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

}
