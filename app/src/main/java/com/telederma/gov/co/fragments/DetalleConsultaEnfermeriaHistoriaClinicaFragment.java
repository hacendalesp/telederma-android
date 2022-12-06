package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.dialogs.VerPacienteDialog;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ImagenAnexo;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.views.ExtendableGridView;
import com.telederma.gov.co.views.ReproductorAudioView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Hernández on 16/07/2018.
 */

public class DetalleConsultaEnfermeriaHistoriaClinicaFragment extends DetalleConsultaContentFragment {

    private TextView lblNombrePaciente, lblEdadPaciente, lblIdentificacionPaciente, lblFechaPaciente,
            btnVerMasDatosPaciente, lblDiagnosticoEtiologico, lblIntensidadDolor, lblMotivoConsulta,
            lblTamanoUlceraLargo, lblTamanoUlceraAncho, lblSignosInfeccion, lblTejidoHerida,
            lblPedio, lblFemoral, lblTibial, lblPielAlrededorUlcera, lblManejoUlcera, lblAnexo,
            lblMejoriaSubjetivaControl, lblTamanoUlceraLargoControl, lblTamanoUlceraAnchoControl,
            lblIntensidadDolorControl, lblToleraTratamientoControl, lblMejoriaControl,
            lblComentariosControl, tv_name_medico, tv_number_professional_card, tv_fecha_consulta,
            tv_name_medico_control, tv_medico_control_number_professional_card, tv_fecha_control, lbl_weight;

    private FrameLayout datosPacienteContent;
    private LinearLayout layDatosControlEnfermeria, ll_content_control;

    private Spinner spnControles;
    private CustomSpinnerAdapter<ControlEnfermeria> controlesEnfermeriaAdapter;

    private ExtendableGridView grvImagenesAnexos;
    private ImageArrayAdapter imagenesAnexosAdapter;

    private ReproductorAudioView ravAnexoView, ravMotivoConsulta;
    Map<String, Integer> constantes = new HashMap<String, Integer>() {{
        put(Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO, R.id.id_paciente_tipo_documento);
        put(Parametro.TIPO_PARAMETRO_TIPO_CONDICION, R.id.id_paciente_condicion);
        put(Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA, R.id.id_paciente_unidad_medida);
        put(Parametro.TIPO_PARAMETRO_TIPO_USUARIO, R.id.id_paciente_tipo_usuario);
        put(Parametro.TIPO_PARAMETRO_PROPOSITO_DE_CONSULTA, R.id.id_paciente_finalidad);
        put(Parametro.TIPO_PARAMETRO_ESTADO_CIVIL, R.id.id_paciente_estado_civil);
        put(Parametro.TIPO_PARAMETRO_CAUSA_EXTERNA, R.id.id_paciente_causa_externa);
    }};
    List<Parametro> data_type_conditions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        llenarConstantes();
        inicializarViews(rootView);
        inicializarValores();

        return rootView;
    }

    private void llenarConstantes(){
        for (Map.Entry<String, Integer> constante : constantes.entrySet()) {
            if(constante.getKey().equals(Parametro.TIPO_PARAMETRO_TIPO_CONDICION)){
                data_type_conditions = dbUtil.obtenerParametros(constante.getKey());
            }
        }
    }

    private void inicializarViews(View rootView) {
        lblNombrePaciente = rootView.findViewById(R.id.lbl_nombre_paciente);
        lblEdadPaciente = rootView.findViewById(R.id.lbl_edad_paciente);
        lblIdentificacionPaciente = rootView.findViewById(R.id.lbl_identificacion_paciente);
        lblFechaPaciente = rootView.findViewById(R.id.lbl_fecha_paciente);

        tv_name_medico = rootView.findViewById(R.id.tv_name_medico);
        tv_number_professional_card = rootView.findViewById(R.id.tv_number_professional_card);
        tv_fecha_consulta = rootView.findViewById(R.id.tv_fecha_consulta);

        lbl_weight = rootView.findViewById(R.id.lbl_weight);
        lblDiagnosticoEtiologico = rootView.findViewById(R.id.lbl_diagnostico_etiologico);
        lblIntensidadDolor = rootView.findViewById(R.id.lbl_intensidad_dolor);
        lblTamanoUlceraLargo = rootView.findViewById(R.id.lbl_tamano_ulcera_largo);
        lblTamanoUlceraAncho = rootView.findViewById(R.id.lbl_tamano_ulcera_ancho);
        lblSignosInfeccion = rootView.findViewById(R.id.lbl_signos_infeccion);
        lblTejidoHerida = rootView.findViewById(R.id.lbl_tejido_herida);
        lblPedio = rootView.findViewById(R.id.lbl_pedio);
        lblFemoral = rootView.findViewById(R.id.lbl_femoral);
        lblTibial = rootView.findViewById(R.id.lbl_tibial);
        lblPielAlrededorUlcera = rootView.findViewById(R.id.lbl_piel_alrededor_ulcera);
        lblManejoUlcera = rootView.findViewById(R.id.lbl_manejo_ulcera);
        lblAnexo = rootView.findViewById(R.id.lbl_anexos_consulta);
        lblMotivoConsulta = rootView.findViewById(R.id.lbl_motivo_consulta);

        ll_content_control = rootView.findViewById(R.id.ll_content_control);
        spnControles = rootView.findViewById(R.id.spn_controles_consulta);
        grvImagenesAnexos = rootView.findViewById(R.id.grv_imagenes_anexos);
        ravAnexoView = rootView.findViewById(R.id.rav_anexo);
        ravMotivoConsulta = rootView.findViewById(R.id.rav_motivo_consulta);
        datosPacienteContent = rootView.findViewById(R.id.datos_paciente_content);
        btnVerMasDatosPaciente = rootView.findViewById(R.id.btn_ver_mas_datos_paciente);
        layDatosControlEnfermeria = rootView.findViewById(R.id.lay_info_control);

        tv_name_medico_control = rootView.findViewById(R.id.tv_name_medico_control);
        tv_medico_control_number_professional_card = rootView.findViewById(R.id.tv_medico_control_number_professional_card);
        tv_fecha_control = rootView.findViewById(R.id.tv_fecha_control);

        lblMejoriaSubjetivaControl = rootView.findViewById(R.id.lbl_mejoria_subjetiva);
        lblTamanoUlceraLargoControl = rootView.findViewById(R.id.lbl_control_tamano_ulcera_largo);
        lblTamanoUlceraAnchoControl = rootView.findViewById(R.id.lbl_control_tamano_ulcera_ancho);
        lblIntensidadDolorControl = rootView.findViewById(R.id.lbl_control_intensidad_dolor);
        lblToleraTratamientoControl = rootView.findViewById(R.id.lbl_control_tolera_tratamiento);
        lblMejoriaControl = rootView.findViewById(R.id.lbl_control_enfermeria_mejoria);
        lblComentariosControl = rootView.findViewById(R.id.lbl_control_enfermeria_comentarios);
    }

    private void inicializarValores() {
        controlEnfermeria = null;
        lblNombrePaciente.setText(paciente.getNombreCompleto());
        lblEdadPaciente.setText(String.format("%s %s", paciente.getInformacionPaciente().getAge(),
                dbUtil.obtenerNombreParametro(
                        Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA,
                        paciente.getInformacionPaciente().getUnit_measure_age())
        ));
        lblIdentificacionPaciente.setText(paciente.getNumber_document());
        // TODO: 4/12/19 created_at
        lblFechaPaciente.setText(consultaEnfermeria.getCreated_at().toString());
        lblDiagnosticoEtiologico.setText(String.format("%s%s",
                dbUtil.obtenerNombresParametro(
                        Parametro.TIPO_PARAMETRO_ULCER_ETIOLOGY,
                        consultaEnfermeria.getUlcer_etiology()
                ),
                TextUtils.isEmpty(consultaEnfermeria.getUlcer_etiology_other()) ?
                        "" : "\n" + consultaEnfermeria.getUlcer_etiology_other()
        ));

        Usuario enfermera = dbUtil.obtenerEnfermera(consultaEnfermeria.getIdEnfermera());
        if(enfermera != null) {
            tv_name_medico.setText(enfermera.getNombreCompleto());
            tv_number_professional_card.setText(enfermera.getTarjetaProfesional());
            // TODO: 4/12/19 created_at
            tv_fecha_consulta.setText(consultaEnfermeria.getCreated_at().toString());
        }

        lbl_weight.setText(consultaEnfermeria.getWeight());
        lblIntensidadDolor.setText(consultaEnfermeria.getPain_intensity());
        lblTamanoUlceraAncho.setText(consultaEnfermeria.getUlcer_width());
        lblTamanoUlceraLargo.setText(consultaEnfermeria.getUlcer_length());
        lblSignosInfeccion.setText(dbUtil.obtenerNombresParametro(
                Parametro.TIPO_PARAMETRO_SIGNOS_INFECCION,
                consultaEnfermeria.getInfection_signs()
        ));
        lblTejidoHerida.setText(dbUtil.obtenerNombresParametro(
                Parametro.TIPO_PARAMETRO_TEJIDO_HERIDA,
                consultaEnfermeria.getWound_tissue()
        ));
        lblPedio.setText(consultaEnfermeria.getPulses_pedio());
        lblFemoral.setText(consultaEnfermeria.getPulses_femoral());
        lblTibial.setText(consultaEnfermeria.getPulses_tibial());
        lblPielAlrededorUlcera.setText(dbUtil.obtenerNombresParametro(
                Parametro.TIPO_PARAMETRO_PIEL_ALREDEDOR_ULCERA,
                consultaEnfermeria.getSkin_among_ulcer()
        ));
        lblManejoUlcera.setText(String.format("%s%s%s",
                dbUtil.obtenerNombresParametro(
                        Parametro.TIPO_PARAMETRO_MANEJO_ULCERA,
                        consultaEnfermeria.getUlcer_handle()
                ),
                TextUtils.isEmpty(consultaEnfermeria.getUlcer_handle_aposito()) ?
                        "" : "\n" + consultaEnfermeria.getUlcer_handle_aposito(),
                TextUtils.isEmpty(consultaEnfermeria.getUlcer_handle_other()) ?
                        "" : "\n" + consultaEnfermeria.getUlcer_handle_other()
        ));
        if (TextUtils.isEmpty(consultaEnfermeria.getConsultation_reason_description()))
            lblMotivoConsulta.setVisibility(View.GONE);
        else
            lblMotivoConsulta.setText(consultaEnfermeria.getConsultation_reason_description());

        if (TextUtils.isEmpty(consultaEnfermeria.getAnexoDescripcion()))
            lblAnexo.setVisibility(View.GONE);
        else
            lblAnexo.setText(consultaEnfermeria.getAnexoDescripcion());

        if (consultaEnfermeria.getControlesEnfermeria().isEmpty()) {
            spnControles.setEnabled(false);
            ll_content_control.setBackgroundResource(0);
        }else
            ll_content_control.setBackground(contexto.getDrawable(R.drawable.spinner_states));

        controlesEnfermeriaAdapter = new CustomSpinnerAdapter<>(
                getActivity(), consultaEnfermeria.getControlesEnfermeria(), ControlEnfermeria.class
        );
        spnControles.setAdapter(controlesEnfermeriaAdapter);
        spnControles.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        cargarInformacionControlEnfermeria(controlEnfermeria = consultaEnfermeria.getControlesEnfermeria().get(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        imagenesAnexosAdapter = new ImageArrayAdapter(
                getActivity(),
                new ArrayList<>()
        );
        if(consultaEnfermeria.getImagenesAnexo() != null && !consultaEnfermeria.getImagenesAnexo().isEmpty())
            for(ImagenAnexo imagenAnexo : consultaEnfermeria.getImagenesAnexo())
                imagenesAnexosAdapter.add(imagenAnexo.getImagenAnexo());
        grvImagenesAnexos.setAdapter(imagenesAnexosAdapter);

        if (consultaEnfermeria.getAnexoAudio() == null)
            ravAnexoView.setVisibility(View.GONE);
        else
            ravAnexoView.load(new ArchivoDescarga(
                    consultaEnfermeria.getAnexoAudio(),
                    Constantes.DIRECTORIO_TEMPORAL,
                    String.format(
                            Constantes.NOMBRE_ARCHIVO_CONSULTA_ENFERMERIA_AUDIO_ANEXO,
                            consultaEnfermeria.getIdServidor()
                    )
            ));

        if (consultaEnfermeria.getConsultation_reason_audio() == null)
            ravMotivoConsulta.setVisibility(View.GONE);
        else
            ravMotivoConsulta.load(new ArchivoDescarga(
                    consultaEnfermeria.getConsultation_reason_audio(),
                    Constantes.DIRECTORIO_TEMPORAL,
                    String.format(
                            Constantes.NOMBRE_ARCHIVO_MOTIVO_CONSULTA_ENFERMERIA,
                            consultaEnfermeria.getIdServidor()
                    )
            ));

        btnVerMasDatosPaciente.setOnClickListener(v -> verMasDatosPaciente());
    }

    private void cargarInformacionControlEnfermeria(ControlEnfermeria controlEnfermeria) {
        if (controlEnfermeria != null) {
            layDatosControlEnfermeria.setVisibility(View.VISIBLE);

            Usuario enfermera = dbUtil.obtenerEnfermera(controlEnfermeria.getIdEnfermera());
            if(enfermera != null) {
                tv_name_medico_control.setText(enfermera.getNombreCompleto());
                tv_medico_control_number_professional_card.setText(enfermera.getTarjetaProfesional());
                // TODO: 4/12/19 created_at
                tv_fecha_control.setText(controlEnfermeria.getCreated_at().toString());
            }

            lblMejoriaSubjetivaControl.setText(controlEnfermeria.getMejoraSubjetiva());
            lblTamanoUlceraAnchoControl.setText(String.valueOf(controlEnfermeria.getTamanoUlceraAncho()));
            lblTamanoUlceraLargoControl.setText(String.valueOf(controlEnfermeria.getTamanoUlceraLargo()));
            lblIntensidadDolorControl.setText(String.valueOf(controlEnfermeria.getIntensidadDolor()));
            lblToleraTratamientoControl.setText(utils.obtenerValorBooleano(controlEnfermeria.getToleraTratamiento()));
            lblMejoriaControl.setText(utils.obtenerValorBooleano(controlEnfermeria.getConceptoEnfermeriaMejoria()));
            lblComentariosControl.setText(controlEnfermeria.getComentarios());
        } else {
            layDatosControlEnfermeria.setVisibility(View.GONE);
        }
    }

    private void verMasDatosPaciente() {
//        ScaleAnimation scale = new ScaleAnimation(1f, 1f, 1.0f, getView().findViewById(R.id.datos_paciente_content).getHeight());
//        scale.setFillAfter(true);
//        scale.setDuration(500);
//        datosPacienteContent.startAnimation(scale);

        new VerPacienteDialog(contexto, getDbHelper(), paciente).show();
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_detalle_consulta_enfermeria_historia_clinica;
    }
}
