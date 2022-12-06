package com.telederma.gov.co.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.MainActivity;
import com.telederma.gov.co.MenuActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.SplashActivity;
import com.telederma.gov.co.dialogs.LoadingDialog;
import com.telederma.gov.co.dialogs.VerFirmaUsuarioDialog;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.DataSincronizacion;
import com.telederma.gov.co.modelo.ImagenAnexo;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.InformacionPaciente;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.ParteCuerpo;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_TEMPORAL_HISTORIA;
import static com.telederma.gov.co.utils.Utils.MSJ_ADVERTENCIA;
import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Created by Sebastián Noreña Márquez on 19/7/2018.
 */

public class ResumenFragment extends BaseFragment implements View.OnClickListener {

    View root_view;
    LinearLayout datos_paciente, historia_clinica, examen_fisico, imagenes, anexos, resumen_datos_control, resumen_imagenes, resumen_historia_clinica, resumen_imagenes_requerimiento;
    Bundle arguments;
    Button btn_registrar_consulta;
    List<Lesion> lesiones = new ArrayList<>();
    HashMap<Integer, List<String>> imagenes_partes_lesiones = new HashMap<Integer, List<String>>();
    public static final String ARG_IMAGENES_LESION = "imagenes_lesion";
    public static final String ARG_TIPO_PROFESIONAL = "tipo_profesional";
    Integer tipo = -1;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_resumen;
    }

    Session session;
    Boolean grabado = false, start_sync = false;
    protected Utils utils;
    private LoadingDialog loadingDialog;
    private JSONObject imagenes_dermatoscopia;
    private int tipo_view;
    public static final String ARG_VIEW = "tipo_view";
    public static final String ARG_ID_CONTENT = "id_content";
    public static final String ARG_ID_CONTROL = "modelo";
    public static final String ARG_ID_IMAGENES_CONTROL = "imagenes_control";
    public static final String ARG_ID_NAME_IMAGE = "id_name_image";
    JSONObject keys_imagen_nombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        super.ocultarTecladoAutomatic();
        loadingDialog = new LoadingDialog(contexto);
        utils = Utils.getInstance(getActivity());

        btn_registrar_consulta = (Button) root_view.findViewById(R.id.btn_registrar_consulta);
        btn_registrar_consulta.setOnClickListener(this);

        arguments = getArguments();
        this.keys_imagen_nombre = new JSONObject();

        if (arguments.containsKey(ARG_VIEW)) {
            tipo_view = arguments.getInt(ARG_VIEW);
        }

        if (arguments.containsKey(ARG_ID_NAME_IMAGE)) {
            try {
                this.keys_imagen_nombre = new JSONObject(arguments.getString(ARG_ID_NAME_IMAGE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (tipo_view == 1) {
            ((RelativeLayout)root_view.findViewById(R.id.relative_header)).setVisibility(View.VISIBLE);
            //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(contexto.getString(R.string.nuevao_requerimiento_registro_resumen_titulo));
            resumen_datos_control = (LinearLayout) root_view.findViewById(R.id.resumen_datos_control);
            resumen_datos_control.setVisibility(View.VISIBLE);
            resumen_imagenes = (LinearLayout) root_view.findViewById(R.id.resumen_imagenes);

            if (!this.keys_imagen_nombre.toString().equals("{}")) {
                resumen_imagenes.setVisibility(View.VISIBLE);
                resumen_imagenes.setOnClickListener(this);
            }


            resumen_datos_control.setOnClickListener(this);

        } else if (tipo_view == 2) {
            ((RelativeLayout)root_view.findViewById(R.id.relative_header)).setVisibility(View.VISIBLE);
            //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(contexto.getString(R.string.nuevao_requerimiento_registro_resumen_titulo));
            resumen_historia_clinica = (LinearLayout) root_view.findViewById(R.id.resumen_historia_clinica);
            resumen_historia_clinica.setVisibility(View.VISIBLE);
            resumen_imagenes_requerimiento = (LinearLayout) root_view.findViewById(R.id.resumen_imagenes_requerimiento);
            if (!this.keys_imagen_nombre.toString().equals("{}")) {
                resumen_imagenes_requerimiento.setVisibility(View.VISIBLE);
                resumen_imagenes_requerimiento.setOnClickListener(this);
            }
            resumen_historia_clinica.setOnClickListener(this);

        } else {

            TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
            header.setText(getResources().getString(R.string.nueva_consulta_registro_resumen));
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
            toolbar.setTitle(getString(R.string.nueva_consulta_registro_resumen_titulo));
            datos_paciente = (LinearLayout) root_view.findViewById(R.id.resumen_paso1);
            datos_paciente.setVisibility(View.VISIBLE);
            historia_clinica = (LinearLayout) root_view.findViewById(R.id.resumen_paso2);
            historia_clinica.setVisibility(View.VISIBLE);
            examen_fisico = (LinearLayout) root_view.findViewById(R.id.resumen_paso3);
            examen_fisico.setVisibility(View.VISIBLE);
            imagenes = (LinearLayout) root_view.findViewById(R.id.resumen_paso4);
            imagenes.setVisibility(View.VISIBLE);
            anexos = (LinearLayout) root_view.findViewById(R.id.resumen_paso5);
            anexos.setVisibility(View.VISIBLE);

            datos_paciente.setOnClickListener(this);
            historia_clinica.setOnClickListener(this);
            examen_fisico.setOnClickListener(this);
            imagenes.setOnClickListener(this);
            anexos.setOnClickListener(this);
        }


        if (arguments.getSerializable(ARG_IMAGENES_LESION) != null)
            imagenes_partes_lesiones = (HashMap<Integer, List<String>>) arguments.getSerializable(ARG_IMAGENES_LESION);

        if (arguments.containsKey(ARG_TIPO_PROFESIONAL))
            tipo = arguments.getInt(ARG_TIPO_PROFESIONAL);

        if (arguments.containsKey("imagenes_dermatoscopia")) {
            try {
                imagenes_dermatoscopia = new JSONObject(arguments.getString("imagenes_dermatoscopia"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (tipo == 2)
            examen_fisico.setVisibility(View.GONE);

        return root_view;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.resumen_paso1:
                fragment = new RegistrarPacienteFragment();
                nextView(fragment);
                break;
            case R.id.resumen_paso2:
                if (tipo == 2)
                    fragment = new RegistrarHistoriaEnfermeriaFragment();
                else
                    fragment = new RegistrarHistoriaFragment();
                nextView(fragment);
                break;
            case R.id.resumen_paso3:
                fragment = new RegistrarExamenFisicoFragment();
                nextView(fragment);
                break;
            case R.id.resumen_paso4:
                fragment = new ImagenesCamaraFragment();
                nextView(fragment);
                break;
            case R.id.resumen_paso5:
                fragment = new RegistrarAnexoFragment();
                nextView(fragment);
                break;
            case R.id.resumen_datos_control:
                fragment = new RegistrarControlFragment();
                nextView(fragment);
                break;
            case R.id.resumen_imagenes:
                fragment = new ImagenesCamaraFragment();
                nextView(fragment);
                break;
            case R.id.resumen_historia_clinica:
                fragment = new RegistrarRequerimientoFragment();
                nextView(fragment);
                break;
            case R.id.resumen_imagenes_requerimiento:
                fragment = new ImagenesCamaraFragment();
                nextView(fragment);
                break;
            case R.id.btn_registrar_consulta:
                VerFirmaUsuarioDialog dialog = new VerFirmaUsuarioDialog(contexto);
                dialog.setOnEnviarClickListener(v -> {
                    dialog.dismiss();
                    loadingDialog.show();
                    if(tipo_view == 1) {
                        saveControl();
                    } else if(tipo_view == 2) {
                        saveRequerimiento();
                    } else {
                        saveLesion();
                    }


                });
                dialog.show();
                break;

        }
    }

    public void saveControl() {
        ControlMedico control_medico = dbUtil.getControlMedico(String.valueOf(arguments.getSerializable(ARG_ID_CONTROL)));
        final Session session = Session.getInstance(getActivity());
        PendienteSincronizacion pendiente = new PendienteSincronizacion();


        ArchivosSincronizacion archivo = new ArchivosSincronizacion();
        if (control_medico.getAudioAnexo() != null) {
            archivo.setId_local(control_medico.getId() + "");
            archivo.setTable(control_medico.NOMBRE_TABLA);
            archivo.setField(control_medico.NOMBRE_CAMPO_AUDIO_ANEXO);
            archivo.setPath(control_medico.getAudioAnexo());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }

        if (control_medico.getAudioClinica() != null) {
            //////////////
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(control_medico.getId() + "");
            archivo.setTable(control_medico.NOMBRE_TABLA);
            archivo.setField(control_medico.NOMBRE_CAMPO_AUDIO_CLINICA);
            archivo.setPath(control_medico.getAudioClinica());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }

        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(String.valueOf(control_medico.getId()));
        pendiente.setTable(ControlMedico.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);

        ArrayList<String> list_im_anexo = arguments.getStringArrayList(ARG_ID_IMAGENES_CONTROL);
        Integer id_anexo = null;

        for (String ruta : list_im_anexo) {
            ImagenAnexo ia = new ImagenAnexo();
            ia.setIdControlLocal(String.valueOf(control_medico.getId()));
            ia.setImagenAnexo(ruta);
            id_anexo = dbUtil.crearImagenAnexo(ia);
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(id_anexo + "");
            archivo.setTable(ImagenAnexo.NOMBRE_TABLA);
            archivo.setField(ImagenAnexo.NOMBRE_CAMPO_IMAGEN_ANNEXO);
            archivo.setPath(ruta);
            archivo.setExtension("jpg");
            dbUtil.crearArchivoSincronizacion(archivo);

            if (id_anexo > 0) {
                pendiente = new PendienteSincronizacion();
                pendiente.setId_local(id_anexo.toString());
                pendiente.setTable(ImagenAnexo.NOMBRE_TABLA);
                pendiente.setEmail(session.getCredentials().getEmail());
                pendiente.setToken(session.getCredentials().getToken());
                pendiente.setRegistration_date(new Date());
                dbUtil.crearPendienteSincronizacion(pendiente);
            }
        }
//        if (list_im_anexo.size() > 0) {
//            pendiente = new PendienteSincronizacion();
//            pendiente.setId_local(id_anexo.toString());
//            pendiente.setTable(ImagenAnexo.NOMBRE_TABLA);
//            pendiente.setEmail(session.getCredentials().getEmail());
//            pendiente.setToken(session.getCredentials().getToken());
//            pendiente.setRegistration_date(new Date());
//            dbUtil.crearPendienteSincronizacion(pendiente);
//        }
        saveLesion(Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL, control_medico.getId());


        List<Lesion> lesiones = new ArrayList<>();
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesiones(control_medico.getId(), Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL);
        for (int i = 0; i < lesiones.size(); i++) {
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }
        startActivity(new Intent(getActivity(), MainActivity.class));

    }

    public void saveRequerimiento() {
        //Note: El requerimiento siempre tiene IdServidor por que llega desde el. Es creado por el especialista
        Requerimiento requerimiento = dbUtil.getRequerimiento(String.valueOf(arguments.getInt(ARG_ID_CONTROL)));
        final Session session = Session.getInstance(getActivity());
        ArchivosSincronizacion archivo = new ArchivosSincronizacion();
        if (requerimiento.getAudioClinica() != null) {
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(requerimiento.getIdServidor() + "");
            archivo.setTable(requerimiento.NOMBRE_TABLA);
            archivo.setField(requerimiento.NOMBRE_CAMPO_AUDIO_CLINICA);
            archivo.setPath(requerimiento.getAudioClinica());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }


        PendienteSincronizacion pendiente = new PendienteSincronizacion();
        // Nota: Sebastian Perez. Cambio este metodo por que al sincroniza se busca por el id del servidor
        pendiente.setId_local(requerimiento.getIdServidor().toString());
        //pendiente.setIdServidor(requerimiento.getIdServidor());
        pendiente.setTable(Requerimiento.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);
        // revisar almacenamiento: dbUtil.obtenerRequerimientoServidor("9")
        // revisar almacenamiento: dbUtil.obtenerSiguienteSincronizable()
        // revisar almacenamiento: dbUtil.obtenerSincronizables()
        // lista los controles de enfermería: dbUtil.getDbHelper().getControlEnfermeriaRuntimeDAO().queryForAll()
        saveLesion(Lesion.NOMBRE_CAMPO_ID_REQUERIMIENTO, requerimiento.getIdServidor(), requerimiento.getIdConsulta(), requerimiento.getIdControlMedico());

        List<Lesion> lesiones = new ArrayList<>();
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesiones(requerimiento.getIdServidor(), Lesion.NOMBRE_CAMPO_ID_REQUERIMIENTO);
        for (int i = 0; i < lesiones.size(); i++) {
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }
        startActivity(new Intent(getActivity(), MainActivity.class));

    }

    public void nextView(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        arguments.putBoolean("resumen", true);
        fragment.setArguments(arguments);

        if(tipo_view == 1){
            ft.replace(arguments.getInt(ARG_ID_CONTENT), fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        } else if(tipo_view == 2){
            ft.replace(R.id.menu_content, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        } else{
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }


        ocultarMensajeEspera();
    }

    public void savePaciente() {
        if (!grabado) {
            grabado = true;
            if (tipo == 1) {
                saveMedico();
            } else
                saveEnfermeria();
        }
    }

    void goToMain() {
        loadingDialog.dismiss();
        startActivity(new Intent(getActivity(), MainActivity.class));
        ((Activity) contexto).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        ((Activity) contexto).finish();
    }

    public void saveMedico() {
        DataSincronizacion ds = new DataSincronizacion();
        if (ds != null && (ds.get_sincronizable_archivo() == null && ds.get_sincronizable() == null))
            start_sync = true;

        final Session session = Session.getInstance(getActivity());
        ///////////////////AUDIO ANEXOS///////////////////////
        ConsultaMedica consulta = dbUtil.getConsultaMedica(arguments.getInt("id_consulta"));
        ArchivosSincronizacion archivo = new ArchivosSincronizacion();

        if (consulta.getAnexoAudio() != null) {
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(consulta.getId() + "");
            archivo.setTable(ConsultaMedica.NOMBRE_TABLA);
            archivo.setField(ConsultaMedica.NOMBRE_CAMPO_ANEXO_AUDIO);
            archivo.setPath(consulta.getAnexoAudio());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }


        if (consulta.getAudioFisico() != null) {
            //////////////
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(consulta.getId() + "");
            archivo.setTable(ConsultaMedica.NOMBRE_TABLA);
            archivo.setField(ConsultaMedica.NOMBRE_CAMPO_CAMPO_AUDIO_FISICO);
            archivo.setPath(consulta.getAudioFisico());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }


        PendienteSincronizacion pendiente = new PendienteSincronizacion();

        ///////////////////////////INFORMACION PACIENTE /////////////////////////////
        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(arguments.getInt("informacion_paciente") + "");
        pendiente.setTable(InformacionPaciente.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);


        //////////////////////////////////CONSULTA/////////////////////////////////////
        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(arguments.getInt("id_consulta") + "");
        pendiente.setTable(ConsultaMedica.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);


        ////////////IMAGEN ANEXO//////////////

        if (arguments.containsKey("imagenes_anexo")) {
            ArrayList<String> list_im_anexo = arguments.getStringArrayList("imagenes_anexo");
            Integer id_anexo = null;
            for (String ruta : list_im_anexo) {
                ImagenAnexo ia = new ImagenAnexo();
                ia.setIdConsultaLocal(arguments.getInt("id_consulta") + "");
                ia.setImagenAnexo(ruta);
                ia.setIdConsultaLocal(consulta.getId().toString());
                id_anexo = dbUtil.crearImagenAnexo(ia);
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(id_anexo + "");
                archivo.setTable(ImagenAnexo.NOMBRE_TABLA);
                archivo.setField(ImagenAnexo.NOMBRE_CAMPO_IMAGEN_ANNEXO);
                archivo.setPath(ruta);
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            if (list_im_anexo.size() > 0) {
                pendiente = new PendienteSincronizacion();
                pendiente.setId_local(id_anexo.toString());
                pendiente.setTable(ImagenAnexo.NOMBRE_TABLA);
                pendiente.setEmail(session.getCredentials().getEmail());
                pendiente.setToken(session.getCredentials().getToken());
                pendiente.setRegistration_date(new Date());
                dbUtil.crearPendienteSincronizacion(pendiente);
            }

        }
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesionesConsulta(arguments.getInt("id_consulta"));
        for (int i = 0; i < lesiones.size(); i++) {
            // TODO: 4/5/19 descomentar por pruebas
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (HttpUtils.validarConexionApi()) {
                    // si no tiene registros pendientes por sincronizar, se inicia el proceso de
                    // sincronización con la consulta actual
                    if (start_sync)
                        ds.sincronizar(contexto);

                    loadingDialog.dismiss();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    ((Activity) contexto).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    ((Activity) contexto).finish();
                } else {
                    mostrarMensajeEspera(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            mostrarMensaje(contexto.getString(R.string.msj_la_consulta_se_enviara_tan_pronto_exista_conexion), MSJ_INFORMACION);
                        }
                    });

                    new CountDownTimer(4000, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            loadingDialog.dismiss();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            ((Activity) contexto).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            ((Activity) contexto).finish();
                        }
                    }.start();
                }
            }
        }.start();


    }


    public void saveEnfermeria() {
        final Session session = Session.getInstance(getActivity());

        ///////////////////AUDIO ANEXOS///////////////////////
        ConsultaEnfermeria consulta = dbUtil.getConsultaEnfermeria(arguments.getInt("id_consulta"));
        ArchivosSincronizacion archivo = new ArchivosSincronizacion();


        if (consulta.getAnexoAudio() != null) {
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(consulta.getId() + "");
            archivo.setTable(ConsultaEnfermeria.NOMBRE_TABLA);
            archivo.setField(ConsultaEnfermeria.NOMBRE_CAMPO_ANEXO_AUDIO);
            archivo.setPath(consulta.getAnexoAudio());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }


        if (consulta.getConsultation_reason_audio() != null) {
            //////////////
            archivo = new ArchivosSincronizacion();
            archivo.setId_local(consulta.getId() + "");
            archivo.setTable(ConsultaEnfermeria.NOMBRE_TABLA);
            archivo.setField(ConsultaEnfermeria.NOMBRE_CAMPO_CONSULTATION_REASON_AUDIO);
            archivo.setPath(consulta.getConsultation_reason_audio());
            archivo.setExtension("acc");
            dbUtil.crearArchivoSincronizacion(archivo);
        }


        PendienteSincronizacion pendiente = new PendienteSincronizacion();

        ///////////////////////////INFORMACION PACIENTE /////////////////////////////
        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(arguments.getInt("informacion_paciente") + "");
        pendiente.setTable(InformacionPaciente.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);


        pendiente = new PendienteSincronizacion();
        pendiente.setId_local(arguments.getInt("id_consulta") + "");
        pendiente.setTable(ConsultaEnfermeria.NOMBRE_TABLA);
        pendiente.setEmail(session.getCredentials().getEmail());
        pendiente.setToken(session.getCredentials().getToken());
        pendiente.setRegistration_date(new Date());
        dbUtil.crearPendienteSincronizacion(pendiente);


        ////////////IMAGEN ANEXO//////////////

        if (arguments.containsKey("imagenes_anexo")) {
            ArrayList<String> list_im_anexo = arguments.getStringArrayList("imagenes_anexo");
            Integer id_anexo = null;
            for (String ruta : list_im_anexo) {
                ImagenAnexo ia = new ImagenAnexo();
                ia.setIdConsultaLocal(arguments.getInt("id_consulta") + "");
                ia.setImagenAnexo(ruta);
                ia.setIdConsultaLocal(consulta.getId().toString());
                id_anexo = dbUtil.crearImagenAnexo(ia);
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(id_anexo + "");
                archivo.setTable(ImagenAnexo.NOMBRE_TABLA);
                archivo.setField(ImagenAnexo.NOMBRE_CAMPO_IMAGEN_ANNEXO);
                archivo.setPath(ruta);
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            if (list_im_anexo.size() > 0) {
                pendiente = new PendienteSincronizacion();
                pendiente.setId_local(id_anexo.toString());
                pendiente.setTable(ImagenAnexo.NOMBRE_TABLA);
                pendiente.setEmail(session.getCredentials().getEmail());
                pendiente.setToken(session.getCredentials().getToken());
                pendiente.setRegistration_date(new Date());
                dbUtil.crearPendienteSincronizacion(pendiente);
            }

        }
        //////////////////IMAGENES LESION////////////////
        lesiones = dbUtil.getLesionesConsulta(arguments.getInt("id_consulta"));
        for (int i = 0; i < lesiones.size(); i++) {
            List<ImagenLesion> imagenes = dbUtil.getImagenesLesion(lesiones.get(i).getId());
            for (ImagenLesion imagen_lesion : imagenes) {
                archivo = new ArchivosSincronizacion();
                archivo.setId_local(imagen_lesion.getId() + "");
                archivo.setTable(ImagenLesion.NOMBRE_TABLA);
                archivo.setField(ImagenLesion.NOMBRE_CAMPO_PHOTO);
                archivo.setPath(imagen_lesion.getPhoto());
                archivo.setExtension("jpg");
                dbUtil.crearArchivoSincronizacion(archivo);
            }
            pendiente = new PendienteSincronizacion();
            pendiente.setId_local(lesiones.get(i).getId().toString());
            pendiente.setTable(Lesion.NOMBRE_TABLA);
            pendiente.setEmail(session.getCredentials().getEmail());
            pendiente.setToken(session.getCredentials().getToken());
            pendiente.setRegistration_date(new Date());
            dbUtil.crearPendienteSincronizacion(pendiente);
        }
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


    public Boolean saveLesion() {
        Integer lesion_id = 0;
        Lesion lesion = null;
        //dbUtil.obtenerConsultaFromLocal(4);
        for (Map.Entry<Integer, List<String>> datoLesion : imagenes_partes_lesiones.entrySet()) {

            String bodyName = "";
            try{
                bodyName = keys_imagen_nombre.getString("" + datoLesion.getKey());
            }catch (Exception e){

            }
            lesion = new Lesion();
            lesion.setIdBodyArea(datoLesion.getKey());
            lesion.setBodyName(bodyName);
            lesion.setIdConsultaLocal(arguments.getInt("id_consulta"));
            ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(datoLesion.getKey().toString());
            if (parte != null)
                lesion.setNameArea(parte.getNombre());
            dbUtil.crearLesion(lesion);
            crearImagenesLesion(datoLesion.getValue(), lesion.getId());
            crearImagenesDermastoscopiaLesion(imagenes_dermatoscopia, lesion.getId());
        }

        savePaciente();
        return true;
    }

    public Boolean saveLesion(String field, Integer... params) {
        Integer lesion_id = 0;
        Lesion lesion = null;
        for (Map.Entry<Integer, List<String>> datoLesion : imagenes_partes_lesiones.entrySet()) {

            String bodyName = "";
            try{
                bodyName = keys_imagen_nombre.getString("" + datoLesion.getKey());
            }catch (Exception e){

            }
            lesion = new Lesion();
            lesion.setIdBodyArea(datoLesion.getKey());
            lesion.setBodyName(bodyName);
            switch (field) {
                case Lesion.NOMBRE_CAMPO_ID_CONTROL_LOCAL:
                    lesion.setIdControlLocal(params[0]);
                    break;
                case Lesion.NOMBRE_CAMPO_ID_REQUERIMIENTO:
                    lesion.setIdRequerimiento(params[0]);
                    lesion.setIdConsulta(params[1]);
                    lesion.setIdControlConsulta(params[2]);
                    break;
            }
            ParteCuerpo parte = dbUtil.obtenerParteDelCuerpo(datoLesion.getKey().toString());
            if (parte != null)
                lesion.setNameArea(parte.getNombre());
            dbUtil.crearLesion(lesion);
            //crearImagenesLesion(datoLesion.getValue(), lesion.getId());
            // TODO: 2/13/19 revisar este cambio con Orli
            crearImagenesLesion(datoLesion.getValue(), lesion.getId());
            crearImagenesDermastoscopiaLesion(imagenes_dermatoscopia, lesion.getId());
        }
        return true;
    }

    public void crearImagenesLesion(List<String> lista_imagenes, Integer lesion_id) {
        for (int i = 0; i < lista_imagenes.size(); i++) {
            ImagenLesion imagenLesion = new ImagenLesion();
            imagenLesion.setPhoto(lista_imagenes.get(i));
            imagenLesion.setDescription("");
            imagenLesion.setId_injury_local(lesion_id);
            dbUtil.crearImagenLesion(imagenLesion);
        }
    }

    public void crearImagenesDermastoscopiaLesion(JSONObject lista_imagenes, Integer lesion_id) {
        try {

            Iterator<?> keys = lista_imagenes.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                List<ImagenLesion> imagenes = dbUtil.getImagenesDermatoscopiaLesion(lesion_id, key);
                if (imagenes.size() > 0) {
                    JSONArray imagenesDermatoscopia = lista_imagenes.getJSONArray(key);

                    for (int i = 0; i < imagenesDermatoscopia.length(); i++) {
                        String path = imagenesDermatoscopia.getString(i);

                        ImagenLesion imagenLesion = new ImagenLesion();
                        imagenLesion.setPhoto(path);
                        imagenLesion.setDescription("");
                        imagenLesion.setId_injury_local(lesion_id);
                        imagenLesion.setImage_injury_id(imagenes.get(0).getId());
                        dbUtil.crearImagenLesion(imagenLesion);
                    }
                }

            }


        } catch (Exception e) {

        }


    }


    public void eliminarConsulta(int consulta_id) {
        try {

            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDao = getDbHelper().getConsultaMedicaRuntimeDAO();
            DeleteBuilder<ConsultaMedica, Integer> queryBuilder = consultaDao.deleteBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            consultaDao.delete(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //  eliminarArchivos();
    }


    public void eliminarArchivos() {
        try {
            File f = new File(DIRECTORIO_TEMPORAL_HISTORIA);
            ArrayList<File> files = archivosCompatibles(f.listFiles(), "temporal");
            for (File file : files) {
                if (file.exists()) {
                    boolean deleted = file.delete();
                }
            }
        } catch (Exception e) {

        }
    }

    public ArrayList<File> archivosCompatibles(File[] archivos, String name) {
        ArrayList<File> archivosC = new ArrayList<File>();
        for (File f : archivos)
            if (f.getName().toLowerCase().startsWith(name)) {
                archivosC.add(f);
            }

        return archivosC;
    }
}
