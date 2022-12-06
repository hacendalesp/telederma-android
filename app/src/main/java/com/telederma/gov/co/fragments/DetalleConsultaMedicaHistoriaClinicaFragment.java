package com.telederma.gov.co.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.dialogs.VerPacienteDialog;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.response.ResponsePacienteBusqueda;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.DescartarRequerimiento;
import com.telederma.gov.co.modelo.ImagenAnexo;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.views.ExtendableGridView;
import com.telederma.gov.co.views.ReproductorAudioView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.fragments.DetalleConsultaFragment.tempControlMedico;

/**
 * Created by Daniel Hernández on 16/07/2018.
 */

public class DetalleConsultaMedicaHistoriaClinicaFragment extends DetalleConsultaContentFragment {

    private TextView lblNombrePaciente, lblEdadPaciente, lblIdentificacionPaciente, lblFechaPaciente,
            lblTiempoEvolucion, lblNroLesionesIniciales, lblEvolucionLesiones, lblCambiosEvidentesSangran,
            lblCambiosEvidentesExudan, lblCambiosEvidentesSupuran, lblSintomas, lblSintomasCambianDia,
            lblAntecedentesRelevantes, lblTratamientoRecibido, lblEfectosTratamiento, lblExamenFisico,
            lblAnexos, lblImpresionDiagnostica, btnVerMasDatosPaciente, lblMejoriaSubjetiva, lblHizoTratamiento,
            lblToleroMedicamentos, lblDescripcionClinica, lblControlAnexos,
            lbl_otros_factores_agravan_sintomas, lblOtrasSustanciasAplicadas, tv_name_medico,
            tv_number_professional_card, tv_fecha_consulta, tv_name_medico_control,
            tv_medico_control_number_professional_card, tv_fecha_control, lbl_weight, lblMotivoConsulta, lblEnfermedadActual;

    private FrameLayout datosPacienteContent;
    private LinearLayout layDatosControlMedico, ll_content_requirements, ll_content_control;

    private Spinner spnControles;
    private CustomSpinnerAdapter<ControlMedico> controlesMedicosAdapter;

    private ExtendableGridView grvImagenesAnexos, grvControlImagenesAnexos;
    private ImageArrayAdapter imagenesAnexosAdapter, imagenesControlAnexosAdapter;

    private ReproductorAudioView ravAnexoView, ravExamenFisicoView, ravAudioClinica, ravControlAnexo;
    Map<String, Integer> constantes = new HashMap<String, Integer>() {{
        put(Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO, R.id.id_paciente_tipo_documento);
        put(Parametro.TIPO_PARAMETRO_TIPO_CONDICION, R.id.id_paciente_condicion);
        put(Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA, R.id.id_paciente_unidad_medida);
        put(Parametro.TIPO_PARAMETRO_TIPO_USUARIO, R.id.id_paciente_tipo_usuario);
        put(Parametro.TIPO_PARAMETRO_PROPOSITO_DE_CONSULTA, R.id.id_paciente_finalidad);
        put(Parametro.TIPO_PARAMETRO_ESTADO_CIVIL, R.id.id_paciente_estado_civil);
        put(Parametro.TIPO_PARAMETRO_CAUSA_EXTERNA, R.id.id_paciente_causa_externa);
    }};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        inicializarViews(rootView);
        inicializarValores();
        return rootView;
    }




    private void inicializarViews(View rootView) {
        ll_content_requirements = rootView.findViewById(R.id.ll_content_requirements);
        tv_name_medico = rootView.findViewById(R.id.tv_name_medico);
        tv_number_professional_card = rootView.findViewById(R.id.tv_number_professional_card);
        tv_fecha_consulta = rootView.findViewById(R.id.tv_fecha_consulta);
        lblNombrePaciente = rootView.findViewById(R.id.lbl_nombre_paciente);
        lblEdadPaciente = rootView.findViewById(R.id.lbl_edad_paciente);
        lblIdentificacionPaciente = rootView.findViewById(R.id.lbl_identificacion_paciente);
        lblFechaPaciente = rootView.findViewById(R.id.lbl_fecha_paciente);
        lbl_weight = rootView.findViewById(R.id.lbl_weight);

        //CAMPOS NUEVOS
        lblMotivoConsulta = rootView.findViewById(R.id.lbl_motivo_consulta);
        lblEnfermedadActual = rootView.findViewById(R.id.lbl_enfermedad_actual);


        lblTiempoEvolucion = rootView.findViewById(R.id.lbl_tiempo_evolucion);
        lblNroLesionesIniciales = rootView.findViewById(R.id.lbl_nro_lesiones_iniciales);
        lblEvolucionLesiones = rootView.findViewById(R.id.lbl_evolucion_lesiones);
        lblCambiosEvidentesSangran = rootView.findViewById(R.id.lbl_cambios_evidentes_sangran);
        lblCambiosEvidentesExudan = rootView.findViewById(R.id.lbl_cambios_evidentes_exudan);
        lblCambiosEvidentesSupuran = rootView.findViewById(R.id.lbl_cambios_evidentes_supuran);
        lblSintomas = rootView.findViewById(R.id.lbl_sintomas);
        lblSintomasCambianDia = rootView.findViewById(R.id.lbl_sintomas_cambian_dia);
        lbl_otros_factores_agravan_sintomas = rootView.findViewById(R.id.lbl_otros_factores_agravan_sintomas);
        lblAntecedentesRelevantes = rootView.findViewById(R.id.lbl_antecedentes_relevantes);
        lblTratamientoRecibido = rootView.findViewById(R.id.lbl_tratamiento_recibido);
        lblOtrasSustanciasAplicadas = rootView.findViewById(R.id.lbl_otras_sustancias_aplicadas);
        lblEfectosTratamiento = rootView.findViewById(R.id.lbl_efectos_tratamiento);
        lblExamenFisico = rootView.findViewById(R.id.lbl_examen_fisico);
        lblAnexos = rootView.findViewById(R.id.lbl_anexos);
        lblImpresionDiagnostica = rootView.findViewById(R.id.lbl_impresion_diagnostica);
        ll_content_control = rootView.findViewById(R.id.ll_content_control);
        spnControles = rootView.findViewById(R.id.spn_controles_consulta);
        grvImagenesAnexos = rootView.findViewById(R.id.grv_imagenes_anexos);
        ravAnexoView = rootView.findViewById(R.id.rav_anexo);
        ravExamenFisicoView = rootView.findViewById(R.id.rav_examen_fisico);
        datosPacienteContent = rootView.findViewById(R.id.datos_paciente_content);
        btnVerMasDatosPaciente = rootView.findViewById(R.id.btn_ver_mas_datos_paciente);
        layDatosControlMedico = rootView.findViewById(R.id.lay_info_control);

        tv_name_medico_control = rootView.findViewById(R.id.tv_name_medico_control);
        tv_medico_control_number_professional_card = rootView.findViewById(R.id.tv_medico_control_number_professional_card);
        tv_fecha_control = rootView.findViewById(R.id.tv_fecha_control);

        lblMejoriaSubjetiva = rootView.findViewById(R.id.lbl_mejoria_subjetiva);
        lblHizoTratamiento = rootView.findViewById(R.id.lbl_hizo_tratamiento);
        lblToleroMedicamentos = rootView.findViewById(R.id.lbl_tolero_medicamentos);
        lblDescripcionClinica = rootView.findViewById(R.id.lbl_descripcion_clinica);
        lblControlAnexos = rootView.findViewById(R.id.lbl_control_anexos);
        grvControlImagenesAnexos = rootView.findViewById(R.id.grv_control_imagenes_anexos);
        ravAudioClinica = rootView.findViewById(R.id.rav_audio_clinica);
        ravControlAnexo = rootView.findViewById(R.id.rav_control_anexo);
    }

    private void inicializarValores() {
        // variable temporal con el argumento recibido como parametro
        /*int id_control = getArguments().getInt(DetalleConsultaFragment.ARG_ID_CONTROL);
        if(id_control <= 0 ) {
            controlMedico = null;
        }*/
        controlMedico = tempControlMedico;

        LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );

        //datos de requerimientos
        //ConsultaMedica consultaMedica = dbUtil.obtenerConsultaMedicina(consultaMedicina.getIdServidor());
        if (consultaMedicina.getRequerimientos().size() == 0)
            ll_content_requirements.setVisibility(View.GONE);
        for (int i = 0; i < consultaMedicina.getRequerimientos().size(); i++) {
            View v_requirement = getLayoutInflater().inflate(R.layout.list_item_requirement, null);
            Requerimiento requerimiento = consultaMedicina.getRequerimientos().get(i);
            if(requerimiento.getIdDoctor() == null) {
                continue;
            }else{
                ll_content_requirements.setVisibility(View.VISIBLE);
            }
            Usuario doctor = dbUtil.obtenerUsuarioLogueado(consultaMedicina.getRequerimientos().get(i).getIdDoctor());
            if(doctor != null){
                if(doctor.getId() != null) {
                    ((TextView) v_requirement.findViewById(R.id.tv_name_medico)).setText(doctor.getNombreCompleto());
                    ((TextView) v_requirement.findViewById(R.id.tv_number_professional_card)).setText(doctor.getTarjetaProfesional());
                }
            }
            if(requerimiento.getComentario() != null) {
                ((TextView) v_requirement.findViewById(R.id.tv_desc_label)).setText(contexto.getString(R.string.item_requerimiento_historia_clinica));
                ((TextView) v_requirement.findViewById(R.id.tv_number_requirement)).setText((i+1 > 1 ? "\n" : "")+"# " + String.valueOf(i + 1) );
                ((TextView) v_requirement.findViewById(R.id.tv_date_value)).setText(requerimiento.getUpdated_at());
                ((TextView) v_requirement.findViewById(R.id.tv_desc_value)).setText(requerimiento.getDescription_request());
                ReproductorAudioView rav_audio_desc_value = ((ReproductorAudioView) v_requirement.findViewById(R.id.rav_audio_desc_value));

                if (requerimiento.getAudioClinica() == null)
                    rav_audio_desc_value.setVisibility(View.GONE);
                else
                    rav_audio_desc_value.load(new ArchivoDescarga(
                            requerimiento.getAudioClinica(),
                            Constantes.DIRECTORIO_TEMPORAL,
                            String.format(Constantes.NOMBRE_ARCHIVO_CONTROL_AUDIO_CLINICA, requerimiento.getIdServidor())
                    ));

                ll_content_requirements.addView(v_requirement);

            }

            DescartarRequerimiento descartarRequerimiento = dbUtil.obtenerDescartarRequerimientoByRequerimientoIdServidor(requerimiento.getIdServidor().toString());
            if(descartarRequerimiento != null ){

                View viewReason= inflater.inflate(R.layout.respuesta_consulta_item, null);
                ((TextView) viewReason.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_remision_requerimiento_descartado);
                String reason = null;
                if(descartarRequerimiento.getReason() != null) {
                    if(descartarRequerimiento.getReason() > 0) {
                        reason = dbUtil.obtenerNombreParametro(
                                Parametro.TIPO_PARAMETRO_REASON, descartarRequerimiento.getReason()
                        );
                    }
                }


                if(reason != null){
                    ((TextView) viewReason.findViewById(R.id.lbl_contenido)).setText(reason);
                    ll_content_requirements.addView(viewReason);
                }else if(!StringUtils.isEmpty(descartarRequerimiento.getOtherReason())){
                    ((TextView) viewReason.findViewById(R.id.lbl_contenido)).setText(descartarRequerimiento.getOtherReason());
                    ll_content_requirements.addView(viewReason);
                }

                if(reason != null && StringUtils.isEmpty(descartarRequerimiento.getOtherReason()) == false ){
                    View viewOtherReason= inflater.inflate(R.layout.respuesta_consulta_item, null);
                    ((TextView) viewOtherReason.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_remision_requerimiento_descartado_otra_rason);
                    ((TextView) viewOtherReason.findViewById(R.id.lbl_contenido)).setText(descartarRequerimiento.getOtherReason());
                    ll_content_requirements.addView(viewOtherReason);
                }
            }
        }




        lblNombrePaciente.setText(paciente.getNombreCompleto());
        lblEdadPaciente.setText(String.format("%s %s", paciente.getInformacionPaciente().getAge(),
                dbUtil.obtenerNombreParametro(
                        Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA,
                        paciente.getInformacionPaciente().getUnit_measure_age())
        ));
        lblIdentificacionPaciente.setText(paciente.getNumber_document());
        lblFechaPaciente.setText(consultaMedicina.getCreated_at().toString());

        Usuario doctor = dbUtil.obtenerDoctor(consultaMedicina.getIdDoctor());
        if(doctor != null) {
            tv_name_medico.setText(doctor.getNombreCompleto());
            tv_number_professional_card.setText(doctor.getTarjetaProfesional());
            tv_fecha_consulta.setText(consultaMedicina.getCreated_at().toString());
        }


        lblTiempoEvolucion.setText(String.format("%s %s", (int)consultaMedicina.getTiempoEvolucion(),
                dbUtil.obtenerNombreParametro(
                        Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA,
                        consultaMedicina.getUnidadMedidaTiempoEvolucion())
        ));
        lblNroLesionesIniciales.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_NUMERO_LESIONES,
                consultaMedicina.getNumeroLesiones()
        ));
        lblEvolucionLesiones.setText(dbUtil.obtenerNombresParametro(
                Parametro.TIPO_PARAMETRO_EVOLUCION_LESIONES,
                consultaMedicina.getEvolucionLesiones()
        ));
        lblCambiosEvidentesSangran.setText(utils.obtenerValorBooleano(consultaMedicina.getSangran()));
        lblCambiosEvidentesExudan.setText(utils.obtenerValorBooleano(consultaMedicina.getExudan()));
        lblCambiosEvidentesSupuran.setText(utils.obtenerValorBooleano(consultaMedicina.getSupuran()));

        lblSintomas.setText(dbUtil.obtenerNombresParametro(
                Parametro.TIPO_PARAMETRO_SINTOMA,
                consultaMedicina.getSintomas()
        ));

        lblSintomasCambianDia.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_CAMBIO_DE_SINTOMAS,
                consultaMedicina.getSintomasCambian()
        ));

        lbl_weight.setText(consultaMedicina.getWeight());

        ///CAMPOS NUEVOS
        lblMotivoConsulta.setText(consultaMedicina.getMotivoConsulta());
        lblEnfermedadActual.setText(consultaMedicina.getEnfermedadActual());
        lbl_otros_factores_agravan_sintomas.setText(consultaMedicina.getOtrosFactoresAgravenSintomas());
        lblAntecedentesRelevantes.setText(consultaMedicina.obtenerAntecedentesRelevantes());
        lblTratamientoRecibido.setText(consultaMedicina.getTratamientoRecibido());
        lblOtrasSustanciasAplicadas.setText(consultaMedicina.getSustanciasAplicadas());
        lblEfectosTratamiento.setText(consultaMedicina.getEfectoTratamiento());

        if (TextUtils.isEmpty(consultaMedicina.getDescripcionExamenFisico()))
            lblExamenFisico.setVisibility(View.GONE);
        else
            lblExamenFisico.setText(consultaMedicina.getDescripcionExamenFisico());

        if (TextUtils.isEmpty(consultaMedicina.getAnexoDescripcion()))
            lblAnexos.setText("No reporta");
        else
            lblAnexos.setText(consultaMedicina.getAnexoDescripcion());

        lblImpresionDiagnostica.setText(consultaMedicina.getImpresionDiagnostica());

        limpiarControlesErroneos();

        if (consultaMedicina.getControlesMedicos().isEmpty()) {
            spnControles.setEnabled(false);
            ll_content_control.setBackgroundResource(0);
        }
        else
            ll_content_control.setBackground(contexto.getDrawable(R.drawable.spinner_states));

        controlesMedicosAdapter = new CustomSpinnerAdapter<>(getActivity(), consultaMedicina.getControlesMedicos(), ControlMedico.class);
        spnControles.setAdapter(controlesMedicosAdapter);
        spnControles.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(spnControles.getSelectedItem() != null){
                            controlMedico = consultaMedicina.getControlesMedicos().get(i);
                            tempControlMedico = controlMedico;
                            spnControles.setSelection(controlesMedicosAdapter.getIndex(controlMedico.getId()), false);
                            cargarInformacionControlMedico(controlMedico);
                            controlMedico = null;
                        }else if(controlMedico != null){
                            tempControlMedico = controlMedico;
                            spnControles.setSelection(controlesMedicosAdapter.getIndex(controlMedico.getId()), false);
                            cargarInformacionControlMedico(controlMedico);
                            controlMedico = null;
                        }else{
                            controlMedico = null;
                            tempControlMedico = controlMedico;
                            spnControles.setSelection(0);
                            cargarInformacionControlMedico(controlMedico);
                        }
                        /*if(view != null) {

                            if (i == 0 && controlMedico == null) {
                                cargarInformacionControlMedico(controlMedico);
                                controlMedico = null;
                            } else if (i > 0) {
                                cargarInformacionControlMedico(controlMedico = consultaMedicina.getControlesMedicos().get(i));
                                controlMedico = null;
                            } else if (controlMedico != null) {
                                spnControles.setSelection(controlesMedicosAdapter.getIndex(controlMedico.getId()), false);
                                cargarInformacionControlMedico(controlMedico);
                                controlMedico = null;
                            }
                        }*/
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
        if(consultaMedicina.getImagenesAnexo() != null && !consultaMedicina.getImagenesAnexo().isEmpty())
            for(ImagenAnexo imagenAnexo : consultaMedicina.getImagenesAnexo())
                imagenesAnexosAdapter.add(imagenAnexo.getImagenAnexo());
        grvImagenesAnexos.setAdapter(imagenesAnexosAdapter);

        if (consultaMedicina.getAnexoAudio() == null)
            ravAnexoView.setVisibility(View.GONE);
        else
            ravAnexoView.load(new ArchivoDescarga(
                    consultaMedicina.getAnexoAudio(),
                    Constantes.DIRECTORIO_TEMPORAL,
                    String.format(Constantes.NOMBRE_ARCHIVO_CONSULTA_MEDICINA_AUDIO_ANEXO, consultaMedicina.getIdServidor())
            ));

        if (consultaMedicina.getAudioFisico() == null)
            ravExamenFisicoView.setVisibility(View.GONE);
        else
            ravExamenFisicoView.load(new ArchivoDescarga(
                    consultaMedicina.getAudioFisico(),
                    Constantes.DIRECTORIO_TEMPORAL,
                    String.format(Constantes.NOMBRE_ARCHIVO_CONSULTA_AUDIO_EXAMEN_FISICO, consultaMedicina.getIdServidor())
            ));

        btnVerMasDatosPaciente.setOnClickListener(v -> verMasDatosPaciente());
    }

    public void limpiarControlesErroneos(){
        try{
            List<ControlMedico> controlesMedicos = consultaMedicina.getControlesMedicos();
            List<ControlMedico> lista = new ArrayList<>();

            for (ControlMedico control : controlesMedicos) {
                if (!(control.getCreated_at().equals(""))){
                    lista.add(control);
                }
            }

            consultaMedicina.setControlesMedicos(lista);


        } catch (Exception e){

        }

    }

    private void cargarInformacionControlMedico(ControlMedico controlMedico) {
        LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        if (controlMedico != null) {
            layDatosControlMedico.setVisibility(View.VISIBLE);
            Usuario doctor = dbUtil.obtenerDoctor(controlMedico.getIdDoctor());
            controlMedico.setRequerimientos(dbUtil.obtenerRequerimientos(DBUtil.TipoConsulta.CONTROL, controlMedico.getIdControlConsulta()));
            if(doctor != null) {
                tv_name_medico_control.setText(doctor.getNombreCompleto());
                tv_medico_control_number_professional_card.setText(doctor.getTarjetaProfesional());
                tv_fecha_control.setText(controlMedico.getCreated_at().toString());
            }

            lblMejoriaSubjetiva.setText(controlMedico.getMejoraSubjetiva());
            lblHizoTratamiento.setText(utils.obtenerValorBooleano(controlMedico.isTuvoTratamiento()));
            lblToleroMedicamentos.setText(dbUtil.obtenerNombreParametro(
                    Parametro.TIPO_PARAMETRO_TOLERO_MEDICAMENTOS, controlMedico.getToleracionMedicacion())
            );

            if (TextUtils.isEmpty(controlMedico.getDescripcionAnexo()))
                lblDescripcionClinica.setVisibility(View.GONE);
            else
                lblDescripcionClinica.setText(controlMedico.getDescripcionClinica());

            if (TextUtils.isEmpty(controlMedico.getDescripcionAnexo()))
                lblControlAnexos.setVisibility(View.GONE);
            else
                lblControlAnexos.setText(controlMedico.getDescripcionAnexo());

            imagenesControlAnexosAdapter = new ImageArrayAdapter(
                    getActivity(),
                    new ArrayList<>()
            );

            if(controlMedico.getImagenesAnexo() != null && !controlMedico.getImagenesAnexo().isEmpty()) {
                ArrayList<String> imgs = new ArrayList<>();

                for (ImagenAnexo imagenAnexo : controlMedico.getImagenesAnexo()) {
                    if (!imgs.contains(imagenAnexo.getImagenAnexo())){
                        imgs.add(imagenAnexo.getImagenAnexo());
                        imagenesControlAnexosAdapter.add(imagenAnexo.getImagenAnexo());
                    }
                }
            }
            grvControlImagenesAnexos.setAdapter(imagenesControlAnexosAdapter);

            if (controlMedico.getAudioClinica() == null)
                ravAudioClinica.setVisibility(View.GONE);
            else {
                ravAudioClinica.setVisibility(View.VISIBLE);
                ravAudioClinica.load(new ArchivoDescarga(
                        controlMedico.getAudioClinica(),
                        Constantes.DIRECTORIO_TEMPORAL,
                        String.format(Constantes.NOMBRE_ARCHIVO_CONTROL_AUDIO_CLINICA, controlMedico.getIdServidor())
                ));


            }
            if (controlMedico.getAudioAnexo() == null)
                ravControlAnexo.setVisibility(View.GONE);
            else {
                ravControlAnexo.setVisibility(View.VISIBLE);
                ravControlAnexo = this.getView().findViewById(R.id.rav_control_anexo);
                ArchivoDescarga archivoDescarga = new ArchivoDescarga(
                        controlMedico.getAudioAnexo(),
                        Constantes.DIRECTORIO_TEMPORAL,
                        String.format(Constantes.NOMBRE_ARCHIVO_CONTROL_AUDIO_ANEXO, controlMedico.getIdServidor())
                );
                if (archivoDescarga != null) {
                    ravControlAnexo.load(archivoDescarga);
                }



            }

            if (controlMedico.getRequerimientos() == null)
                ll_content_requirements.setVisibility(View.GONE);
            else{
                List<Requerimiento> reqList =
                        controlMedico.getRequerimientos()
                                .stream()
                                .filter(p-> p.getIdDoctor() != null)
                                .collect(Collectors.toList());

                // TODO: 4/23/19 agregar titulo de requerimientos
                if(reqList.size() > 0){
                    View viewTitleRequermiento = inflater.inflate(R.layout.respuesta_consulta_item, null);
                    ((TextView) viewTitleRequermiento.findViewById(R.id.lbl_titulo)).setTypeface(null, Typeface.BOLD);
                    ((TextView) viewTitleRequermiento.findViewById(R.id.lbl_titulo)).setText("\n Requerimientos \n" );
                    ((TextView) viewTitleRequermiento.findViewById(R.id.lbl_contenido)).setVisibility(View.GONE);
                    layDatosControlMedico.addView(viewTitleRequermiento);
                }

                for (int i = 0; i < reqList.size(); i++) {
                    View v_requirement = inflater.inflate(R.layout.list_item_requirement, null);
                    Requerimiento requerimiento = reqList.get(i);
                    if(requerimiento.getIdDoctor() == null)
                        continue;
                    Usuario doctor_requrimiento = dbUtil.obtenerUsuarioLogueado(requerimiento.getIdDoctor());
                    if (doctor_requrimiento != null) {
                        if (doctor_requrimiento.getId() != null) {
                            ((TextView) v_requirement.findViewById(R.id.tv_name_medico)).setText(doctor_requrimiento.getNombreCompleto());
                            ((TextView) v_requirement.findViewById(R.id.tv_number_professional_card)).setText(doctor_requrimiento.getTarjetaProfesional());
                        }
                    }
                    if (requerimiento.getComentario() != null) {
                        ((TextView) v_requirement.findViewById(R.id.tv_desc_label)).setText(contexto.getString(R.string.item_requerimiento_historia_clinica));
                        ((TextView) v_requirement.findViewById(R.id.tv_number_requirement)).setText( (i+1 > 1 ? "\n" : "")+ "# " + String.valueOf(i + 1));
                        ((TextView) v_requirement.findViewById(R.id.tv_date_value)).setText(requerimiento.getUpdated_at());
                        ((TextView) v_requirement.findViewById(R.id.tv_desc_value)).setText(requerimiento.getDescription_request());
                        ReproductorAudioView rav_audio_desc_value = ((ReproductorAudioView) v_requirement.findViewById(R.id.rav_audio_desc_value));

                        if (requerimiento.getAudioClinica() == null)
                            rav_audio_desc_value.setVisibility(View.GONE);
                        else
                            rav_audio_desc_value.load(new ArchivoDescarga(
                                    requerimiento.getAudioClinica(),
                                    Constantes.DIRECTORIO_TEMPORAL,
                                    String.format(Constantes.NOMBRE_ARCHIVO_CONTROL_AUDIO_CLINICA, requerimiento.getIdServidor())
                            ));

                        layDatosControlMedico.addView(v_requirement);

                    }
                }
            }
        } else {
            layDatosControlMedico.setVisibility(View.GONE);
        }
    }

    private void verMasDatosPaciente() {
//        ScaleAnimation scale = new ScaleAnimation(1f, 1f, 1.0f, getView().findViewById(R.id.datos_paciente_content).getHeight());
//        scale.setFillAfter(true);
//        scale.setDuration(500);
//        datosPacienteContent.startAnimation(scale);

        new VerPacienteDialog(contexto, getDbHelper(), paciente).show();
        //new VerPacienteDialog(contexto, getDbHelper(), paciente).show();
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_detalle_consulta_medica_historia_clinica;
    }



}
