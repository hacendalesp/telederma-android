package com.telederma.gov.co.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.telederma.gov.co.Camara;
import com.telederma.gov.co.ImagenesAnexoCamara;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Speech;
import com.telederma.gov.co.utils.Utils;
import com.telederma.gov.co.views.ExtendableGridView;
import com.telederma.gov.co.views.ReproductorAudioView;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RegistrarAnexoFragment extends BaseFragment implements MediaPlayer.OnCompletionListener, MediaRecorder.OnInfoListener, View.OnClickListener {

    String tag = "RegistrarAnexoFragment";
    Integer id_consulta ,tipo;
    MediaRecorder recorder;
    File archivo = null, path;
    Button grabar, detener, camara, btn_anexo, btn_resumen;
    EditText texto_anexo;
    Bundle arguments;
    Boolean resumen = false;
    View root_view;
    ReproductorAudioView audio;
    ArrayList<String> imagenes_anexo;
    private static final int REQUEST_IMAGENES_ANEXO = 201;
    private static final int REQUEST_IMAGENES_CAMARA = 202;
    View f_texto_anexo;
    Speech speech=null;
    @Override
    protected int getIdLayout() {
        return R.layout.fragment_registrar_anexos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = super.onCreateView(inflater, container, savedInstanceState);
        asignarEventoOcultarTeclado(root_view);
        speech = Speech.init(contexto, contexto.getPackageName());
        utils = Utils.getInstance(contexto);
        audio = (ReproductorAudioView) root_view.findViewById(R.id.reproductor);
        audio.setOnRemoveClickListener(v -> eliminar());
        final Session session = Session.getInstance(getActivity());
        tipo = session.getCredentials().getTipoProfesional();

        f_texto_anexo = root_view.findViewById(R.id.f_texto_anexo);
        texto_anexo = (EditText) f_texto_anexo.findViewById(R.id.ed_text);//root_view.findViewById(R.id.texto_anexo);
        texto_anexo.setHint(contexto.getString(R.string.anexos));
        arguments = getArguments();
        id_consulta = arguments.getInt("id_consulta");

        grabar = (Button) root_view.findViewById(R.id.grabar);
        detener = (Button) root_view.findViewById(R.id.detener);
        camara = (Button) root_view.findViewById(R.id.camara);
        btn_anexo = (Button) root_view.findViewById(R.id.btn_anexo);

        utils.speechText(f_texto_anexo);

        grabar.setOnClickListener(this);
        grabar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        grabar();
                        break;
                    case MotionEvent.ACTION_UP:
                        detener();
                        break;
                }
                return true;
            }
        });
        detener.setOnClickListener(this);
        camara.setOnClickListener(this);
        btn_anexo.setOnClickListener(this);
        recorder = null;
        btn_resumen = (Button) root_view.findViewById(R.id.btn_resumen);
        resumen = arguments.getBoolean("resumen");
        btn_anexo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mostrarMensajeEspera(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        saveAnexos();
                    }
                });
            }
        });

        btn_resumen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mostrarMensajeEspera(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        saveAnexos();
                    }
                });
            }
        });
        if (resumen) {
            arguments.putBoolean("resumen", false);
            btn_anexo.setVisibility(View.GONE);
            btn_resumen.setVisibility(View.VISIBLE);
        } else {
            btn_anexo.setVisibility(View.VISIBLE);
            btn_resumen.setVisibility(View.GONE);
        }
        path = new File(Constantes.DIRECTORIO_TEMPORAL_HISTORIA);
        if (!path.exists()) {
            path.mkdirs();
        }
        TextView header = (TextView) getActivity().findViewById(R.id.text_header_register);
        header.setText(getResources().getString(R.string.nueva_consulta_registro_paso5));
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_historia);
        toolbar.setTitle(getString(R.string.nueva_consulta_title_paso5));
        poblar_formulario(id_consulta);

        if (arguments.containsKey("imagenes_anexo")) {
            imagenes_anexo = arguments.getStringArrayList("imagenes_anexo");
            if (imagenes_anexo.size() > 0)
                crearListaImagenes(imagenes_anexo);
        } else
            imagenes_anexo = new ArrayList<String>();



        return root_view;
    }

    public void eliminar() {
        grabar.setVisibility(View.VISIBLE);
        audio.setVisibility(View.GONE);
    }

    public void grabar() {
        try {
            grabar.setVisibility(View.INVISIBLE);
            detener.setVisibility(View.VISIBLE);
            recorder = new MediaRecorder();
            recorder.setMaxDuration(120000);
            recorder.setOnInfoListener(this);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            archivo = new File(path, "temporal" + System.currentTimeMillis() + ".acc");
            FileOutputStream fOut = new FileOutputStream(archivo);
            fOut.flush();
            fOut.close();
            recorder.setOutputFile(archivo.getAbsolutePath());
            recorder.prepare();
            recorder.start();

        } catch (Exception e) {

        }
    }

    public void detener() {
        grabar.setVisibility(View.INVISIBLE);
        detener.setVisibility(View.INVISIBLE);
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            audio.setVisibility(View.VISIBLE);
            audio.load(new ArchivoDescarga("", path.getAbsolutePath(), archivo.getName()));
        } catch (RuntimeException ex) {
            grabar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        detener();
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camara:
                final Intent intent = new Intent(Camara.ACTION_CAMARA_SLIDER);
                intent.putExtra("cantidad_imagenes", 0);
                startActivityForResult(intent, REQUEST_IMAGENES_CAMARA);
                break;
            case R.id.detener:
                detener();
                break;
            case R.id.btn_anexo:
                mostrarMensajeEspera(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        saveAnexos();
                    }
                });
                break;
        }
    }


    public void poblar_formulario(int consulta_id) {

        Consulta consulta = null ;
        if(tipo==1)
             consulta = getConsulta(consulta_id);
        else
             consulta = getConsultaEnfermeria(consulta_id);

        if (consulta != null) {
            texto_anexo.setText(consulta.getAnexoDescripcion());
            if (consulta.getAnexoAudio() != null && !consulta.getAnexoAudio().isEmpty()) {
                String path_audio = consulta.getAnexoAudio();
                if (!path_audio.isEmpty()) {
                    archivo = new File(path_audio);
                    detener();
                }
            }
        }
    }

    public ConsultaMedica getConsulta(int consulta_id) {
        ConsultaMedica consulta = null;
        try {

            RuntimeExceptionDao<ConsultaMedica, Integer> consultaDAO = getDbHelper().getConsultaMedicaRuntimeDAO();
            QueryBuilder<ConsultaMedica, Integer> queryBuilder = consultaDAO.queryBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            List<ConsultaMedica> list_consultas = consultaDAO.query(queryBuilder.prepare());
            if (!list_consultas.isEmpty()) {
                consulta = list_consultas.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consulta;
    }


    public ConsultaEnfermeria getConsultaEnfermeria(int consulta_id) {
        ConsultaEnfermeria consulta = null;
        try {

            RuntimeExceptionDao<ConsultaEnfermeria, Integer> consultaDAO = getDbHelper().getConsultaEnfermeriaRuntimeDAO();
            QueryBuilder<ConsultaEnfermeria, Integer> queryBuilder = consultaDAO.queryBuilder();
            queryBuilder.where().eq(
                    "id", consulta_id
            );
            List<ConsultaEnfermeria> list_consultas = consultaDAO.query(queryBuilder.prepare());
            if (!list_consultas.isEmpty()) {
                consulta = list_consultas.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consulta;
    }

    public void saveAnexos() {
        Consulta consulta = null;
        if(tipo == 1 )
            consulta = getConsulta(id_consulta);
        else
            consulta = getConsultaEnfermeria(id_consulta);

        consulta.setAnexoDescripcion(texto_anexo.getText().toString());
        if (archivo != null) {
            consulta.setAnexoAudio(archivo.getAbsolutePath());
            audio.pause();
        }
        if(tipo == 1 )
            dbUtil.crearConsultaMedica((ConsultaMedica)consulta);
        else
           dbUtil.crearConsultaEnfermeria((ConsultaEnfermeria) consulta);

        ocultarMensajeEspera();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        arguments.putBoolean("resumen", false);
        if (resumen) {
            fragment = new ResumenFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).commit();
        } else {
            fragment = new ResumenFragment();
            fragment.setArguments(arguments);
            ft.replace(R.id.paso1, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGENES_CAMARA){
            if (data.hasExtra("imagenes")) {
                ArrayList<String> imagenes = data.getExtras().getStringArrayList("imagenes");
                if (imagenes.size() > 0) {
                    final Intent intent = new Intent(ImagenesAnexoCamara.ACTION_ANEXO);
                    imagenes_anexo.addAll(imagenes);
                    intent.putExtra("imagenes_anexo", imagenes_anexo);
                    startActivityForResult(intent, REQUEST_IMAGENES_ANEXO);
                }
            }
        }
        else if(requestCode == REQUEST_IMAGENES_ANEXO)
             if(data.hasExtra("imagenes_anexo")) {
                 ArrayList<String> imagenes =  data.getStringArrayListExtra("imagenes_anexo");
                 if (imagenes.size() > 0) {
                     imagenes_anexo.clear();
                     imagenes_anexo.addAll(imagenes);
                     arguments.putStringArrayList("imagenes_anexo", data.getStringArrayListExtra("imagenes_anexo"));
                     crearListaImagenes(imagenes_anexo);
                 }
             }
    }

    private void crearListaImagenes(ArrayList<String> imageUrls) {
        ExtendableGridView gv = (ExtendableGridView) root_view.findViewById(R.id.grid_imagenes);
        List<String> lstItem = new ArrayList<String>();
        for (int i = 0; i < imageUrls.size(); i++)
            lstItem.add(imageUrls.get(i));
        ImageArrayAdapter adapter = new ImageArrayAdapter(getActivity(), lstItem);
        gv.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "controller status ondestroy ");
        if(speech != null)
            speech.shutdown();
    }
}