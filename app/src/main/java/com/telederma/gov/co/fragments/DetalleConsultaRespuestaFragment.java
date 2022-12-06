package com.telederma.gov.co.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.CustomSpinnerAdapter;
import com.telederma.gov.co.adapters.ImageArrayAdapter;
import com.telederma.gov.co.dialogs.DescartarRequerimientoDialog;
import com.telederma.gov.co.dialogs.VerFormulaDialog;
import com.telederma.gov.co.dialogs.VerPDFDialog;
import com.telederma.gov.co.http.ConsultaService;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.request.CompartirFormulaMipresRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.DescartarRequerimiento;
import com.telederma.gov.co.modelo.Diagnostico;
import com.telederma.gov.co.modelo.Especialista;
import com.telederma.gov.co.modelo.ExamenSolicitado;
import com.telederma.gov.co.modelo.Formula;
import com.telederma.gov.co.modelo.ImagenAnexo;
import com.telederma.gov.co.modelo.ImagenLesion;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.Mipres;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.modelo.Requerimiento;
import com.telederma.gov.co.modelo.RespuestaEspecialista;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.views.ExtendableGridView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.fragments.DetalleConsultaFragment.tempControlMedico;
import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

/**
 * Created by Daniel Hernández on 16/07/2018.
 */

public class DetalleConsultaRespuestaFragment extends DetalleConsultaContentFragment {

    private TextView lblEspecialista, lblNroRegistro, lblFecha, lblHora, lblTratamiento, lblSinRespuesta,
            lblDescripcionLesion, lblAnalisisCaso, tv_note_image;
    private ScrollView layRespuesta;

    private Spinner spnControles;
    private CustomSpinnerAdapter controlesAdapter;
    private ViewGroup viewDiagnosticos, viewSolicitudExamenes, viewFormulacion, viewMipres,
            viewProximoControl, viewRequerimientos ,viewRemitido ,viewRemitidoComentario;

    private Integer cantidadControles = 0;
    private List<RespuestaEspecialista> respuestaEspecialista;
    private List<Requerimiento> requerimientos;
    private String tratamiento;
    private String comentario_remision;
    private String tipo_remision;
    private Integer idDuenoRespuesta = -1;
    private Consulta consulta;
    private ControlConsulta control;
    private View rootView;
    private LinearLayout lnTratamiento, lnAnalisis,lnCaso, ll_content_note_image;
    private ExtendableGridView grvImagenes;
    private ImageArrayAdapter imagenesAdapter;
    private List<String> imagenes;
    private List<String> descriptions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        inicializarConsulta();
        inicializarSpinnerControles();

        return rootView;
    }

    private void inicializarSpinnerControles() {

        limpiarControlesErroneos();

        if (cantidadControles < 1)
            spnControles.setEnabled(false);

        if(consultaMedicina != null)
            controlesAdapter = new CustomSpinnerAdapter<>(
                    getActivity(), consultaMedicina.getControlesMedicos(), ControlMedico.class
            );
        else
            controlesAdapter = new CustomSpinnerAdapter<>(
                    getActivity(), consultaEnfermeria.getControlesEnfermeria(), ControlEnfermeria.class
            );

        spnControles.setAdapter(controlesAdapter);
        spnControles.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(consultaMedicina != null)
                            controlMedico = consultaMedicina.getControlesMedicos().get(i);
                        else if(consultaEnfermeria != null)
                            controlEnfermeria = consultaEnfermeria.getControlesEnfermeria().get(i);

                        inicializarControl();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        if(tempControlMedico!= null && consultaMedicina.getControlesMedicos().size() > 1) {
            int temp_index = controlesAdapter.getIndex(tempControlMedico.getId());
            spnControles.setSelection(temp_index);
        }
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

            if(lista.size() > 0){
                consultaMedicina.setControlesMedicos(lista);
            }

        } catch (Exception e){

        }

    }

    private boolean tieneRespuesta(Integer estado) {
        return Consulta.ESTADO_CONSULTA_PENDIENTE.equals(estado)
                || Consulta.ESTADO_CONSULTA_PROCESO.equals(estado)
                || Consulta.ESTADO_CONSULTA_SIN_CREDITOS.equals(estado);
    }

    private void inicializarConsulta() {
        consulta = consultaMedicina != null ? consultaMedicina : consultaEnfermeria;
        consulta.setIdServidor(
                consultaMedicina != null ?
                        consultaMedicina.getIdConsulta() : consultaEnfermeria.getIdConsulta()
        );

        cantidadControles = consulta.getCantidadControles();
        respuestaEspecialista = consulta.getRespuestaEspecialista();
        requerimientos = consulta.getRequerimientos();
        tratamiento = consulta.getTratamiento();
        comentario_remision = consulta.getRemission_comments();
        tipo_remision = consulta.getType_remission();
        idDuenoRespuesta = consulta.getIdServidor();

        inicializarViews(consulta.getEstado());
        inicializarValores(consulta.getEstado());
    }

    private void inicializarControl() {
        control = controlMedico != null ? controlMedico : controlEnfermeria;
        if(control == null) {
            inicializarConsulta();

            return;
        }

        control.setIdServidor(
                controlMedico != null ?
                        controlMedico.getIdControlConsulta() : controlEnfermeria.getIdControlConsulta()
        );

        respuestaEspecialista = control.getRespuestaEspecialista();
        if(control.getRequerimientos()!= null)
        {
            List<Requerimiento> list_requerimientos = new ArrayList<>();
            list_requerimientos = (control.getRequerimientos());
            requerimientos =  list_requerimientos ;
        }
        else
            {
                requerimientos = null;
            }

        idDuenoRespuesta = control.getIdServidor();
        tratamiento = control.getTratamiento();
        comentario_remision = control.getComentario_remision();
        tipo_remision = control.getTipo_remision();


        inicializarViews(control.getEstado());
        inicializarValores(control.getEstado());
    }

    private void inicializarViews(Integer estado) {
        lblSinRespuesta = rootView.findViewById(R.id.lbl_sin_respuesta);
        layRespuesta = rootView.findViewById(R.id.lay_respuesta);

        if (tieneRespuesta(estado)) {
            lblSinRespuesta.setVisibility(View.VISIBLE);
            layRespuesta.setVisibility(View.GONE);
        } else {
            lblSinRespuesta.setVisibility(View.GONE);
            layRespuesta.setVisibility(View.VISIBLE);
        }


        grvImagenes = rootView.findViewById(R.id.grv_imagenes_consulta);
        ll_content_note_image = rootView.findViewById(R.id.ll_content_note_image);
        tv_note_image = rootView.findViewById(R.id.tv_note_image);
        lblEspecialista = rootView.findViewById(R.id.lbl_especialista);
        lblNroRegistro = rootView.findViewById(R.id.lbl_nro_registro);
        lblFecha = rootView.findViewById(R.id.lbl_fecha_respuesta);
        lblHora = rootView.findViewById(R.id.lbl_hora_respuesta);
        lblTratamiento = rootView.findViewById(R.id.lbl_tratamiento);
        lblDescripcionLesion = rootView.findViewById(R.id.lbl_descripcion_lesion);
        lblAnalisisCaso = rootView.findViewById(R.id.lbl_analisis_caso);

        lnAnalisis = rootView.findViewById(R.id.lnAnalisis);
        lnTratamiento = rootView.findViewById(R.id.lntratamiento);
        lnCaso = rootView.findViewById(R.id.lncaso);

        viewDiagnosticos = rootView.findViewById(R.id.diagnosticos_content);
        viewDiagnosticos.removeAllViews();
        viewFormulacion = rootView.findViewById(R.id.formulacion_content);
        viewFormulacion.removeAllViews();
        viewMipres = rootView.findViewById(R.id.mipres_content);
        viewMipres.removeAllViews();
        viewProximoControl = rootView.findViewById(R.id.proximo_control_content);
        viewProximoControl.removeAllViews();
        viewRequerimientos = rootView.findViewById(R.id.requerimientos_content);
        viewRequerimientos.removeAllViews();
        viewSolicitudExamenes = rootView.findViewById(R.id.solicitud_examenes_content);
        viewSolicitudExamenes.removeAllViews();

        viewRemitido = rootView.findViewById(R.id.remitido_content);
        viewRemitido.removeAllViews();


        viewRemitidoComentario = rootView.findViewById(R.id.remitido_comentario_content);
        viewRemitidoComentario.removeAllViews();


        spnControles = rootView.findViewById(R.id.spn_controles_consulta);
    }

    private void inicializarValores(Integer estado) {
        if (tieneRespuesta(estado))
            return;

        LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );


        inicializarListaLesiones();
        imagenesAdapter = new ImageArrayAdapter(
                getActivity(),
                imagenes, descriptions
        );
        grvImagenes.setAdapter(imagenesAdapter);



        if(respuestaEspecialista != null && !respuestaEspecialista.isEmpty()) {
            final RespuestaEspecialista respuesta = respuestaEspecialista.get(0);


            if(respuesta.getDescripcionLesion()==null)
                lnAnalisis.setVisibility(View.GONE);
            else
                lnAnalisis.setVisibility(View.VISIBLE);

            lblDescripcionLesion.setText(respuesta.getDescripcionLesion());
            if(respuesta.getAnalisisCaso()==null)
                lnCaso.setVisibility(View.GONE);
            else
                lnCaso.setVisibility(View.VISIBLE);
            lblAnalisisCaso.setText(respuesta.getAnalisisCaso() );

            if(respuesta.getControlRecomendado() != null)
             viewProximoControl.setVisibility(View.VISIBLE);
            else
                viewProximoControl.setVisibility(View.GONE);
            View viewItem = inflater.inflate(R.layout.respuesta_consulta_item, null);
            ((TextView) viewItem.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_proximo_control);
            ((TextView) viewItem.findViewById(R.id.lbl_contenido)).setText(respuesta.getControlRecomendado());
            viewProximoControl.addView(viewItem);

            if(tipo_remision!=null)
              viewRemitido.setVisibility(View.VISIBLE);
            else
                viewRemitido.setVisibility(View.GONE);
            View viewRemitidoTipo = inflater.inflate(R.layout.respuesta_consulta_item, null);
            ((TextView) viewRemitidoTipo.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_remision_tipo);
            ((TextView) viewRemitidoTipo.findViewById(R.id.lbl_contenido)).setText(tipo_remision);
            viewRemitido.addView(viewRemitidoTipo);

            if(comentario_remision!=null)
                viewRemitidoComentario.setVisibility(View.VISIBLE);
            else
                viewRemitidoComentario.setVisibility(View.GONE);
            View viewComentario = inflater.inflate(R.layout.respuesta_consulta_item, null);
            ((TextView) viewComentario.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_remision_comentario);
            ((TextView) viewComentario.findViewById(R.id.lbl_contenido)).setText(comentario_remision);
            viewRemitidoComentario.addView(viewComentario);

            if (respuesta.getDiagnosticos() != null && !respuesta.getDiagnosticos().isEmpty()) {

                final Iterator<Diagnostico> i = respuesta.getDiagnosticos().iterator();
                viewDiagnosticos.setVisibility(View.VISIBLE);
                int temp_index = 0;
                while (i.hasNext()) {
                    final Diagnostico diagnostico = i.next();
                    final StringBuilder sb = new StringBuilder();
                    sb.append(contexto.getResources().getString(R.string.detalle_consulta_diagnostico_enfermedad));
                    sb.append(" " + diagnostico.getDiagnosticoPrincipal());
                    sb.append("\n");
                    sb.append(contexto.getResources().getString(R.string.detalle_consulta_diagnostico_tipo));
                    sb.append(" " + diagnostico.getTipoDiagnostico());
                    sb.append("\n");
                    viewItem = inflater.inflate(R.layout.respuesta_consulta_item, null);
                    if(temp_index == 0)
                        ((TextView) viewItem.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_diagnostico_principal);
                    else
                        ((TextView) viewItem.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_diagnostico_relacionado);
                    ((TextView) viewItem.findViewById(R.id.lbl_contenido)).setText(sb.toString());
                    viewDiagnosticos.addView(viewItem);

                    temp_index += 1;
                }
            }

            if (respuesta.getExamenesSolicitados() != null && !respuesta.getExamenesSolicitados().isEmpty()) {
                viewSolicitudExamenes.setVisibility(View.VISIBLE);
                final Iterator<ExamenSolicitado> i = respuesta.getExamenesSolicitados().iterator();
                while (i.hasNext()) {
                    final ExamenSolicitado examenSolicitado = i.next();
                    viewItem = inflater.inflate(R.layout.respuesta_consulta_item, null);
                    ((TextView) viewItem.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_solicitud_examenes);
                    ((TextView) viewItem.findViewById(R.id.lbl_contenido)).setText(examenSolicitado.getNombreTipoExamen());
                    viewSolicitudExamenes.addView(viewItem);
                }
            }
            else
                    viewSolicitudExamenes.setVisibility(View.GONE);

            if (respuesta.getFormulas() != null && !respuesta.getFormulas().isEmpty()) {
                viewFormulacion.setVisibility(View.VISIBLE);
                final Iterator<Formula> i = respuesta.getFormulas().iterator();
                while (i.hasNext()) {
                    final Formula formula = i.next();
                    viewItem = inflater.inflate(R.layout.formulacion_item, null);
                    ((TextView) viewItem.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_formulacion);
                    (viewItem.findViewById(R.id.btn_ver_formula)).setOnClickListener(v -> verFormula(formula));
                    (viewItem.findViewById(R.id.btn_compartir_formula)).setOnClickListener(v -> compartirFormula(formula));
                    viewFormulacion.addView(viewItem);
                }
            }
            else
                viewFormulacion.setVisibility(View.GONE);

            if (respuesta.getMipres() != null && !respuesta.getMipres().isEmpty()) {
                viewMipres.setVisibility(View.VISIBLE);
                final Iterator<Mipres> i = respuesta.getMipres().iterator();
                while (i.hasNext()) {
                    final Mipres mipres = i.next();
                    viewItem = inflater.inflate(R.layout.mipres_item, null);
                    ((TextView) viewItem.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_mipres);
                    (viewItem.findViewById(R.id.btn_ver_mipres)).setOnClickListener(v -> verMipres(mipres));
                    (viewItem.findViewById(R.id.btn_compartir_mipres)).setOnClickListener(v -> compartirMipres(mipres));
                    viewMipres.addView(viewItem);
                }
            }
            else
                viewMipres.setVisibility(View.GONE);

            if (respuesta.getEspecialista() != null) {
                lblEspecialista.setText(respuesta.getEspecialista().getNombreCompleto());
                //lblEspecialista.setSelected(true);
                lblNroRegistro.setText(String.valueOf(respuesta.getEspecialista().getDocumento()));
                //lblFecha.setText(respuesta.getEspecialista().getFecha());

                if(respuesta.getCreated_at() != null){
                    lblFecha.setText(respuesta.getCreated_at().toString());
                    lblHora.setText("");
                    if(respuesta.getHour() != null) {
                        lblFecha.setText(respuesta.getCreated_at_no_time().toString());
                        lblHora.setText(" - " + respuesta.getHour());
                    }
                } else if(respuesta.getUpdated_at() != null){
                    lblFecha.setText(respuesta.getUpdated_at());
                    lblHora.setText("");
                }

                //if(respuesta.getUpdated_at() != null){
                //    lblFecha.setText(respuesta.getUpdated_at());
                //    lblHora.setText("");
                //} else {
                //    lblFecha.setText(respuesta.getCreated_at().toString());
                //    lblHora.setText("");
                //    if(respuesta.getHour() != null) {
                //        lblFecha.setText(respuesta.getCreated_at_no_time().toString());
                //        lblHora.setText(" - " + respuesta.getHour());
                //    }
                //}
            }
        }

        // TODO: 4/22/19 validar y recorrer la lista de requermientos 
        if (requerimientos != null && !requerimientos.isEmpty() ) {

            viewRequerimientos.setVisibility(View.VISIBLE);
            View viewTitleRequermiento = inflater.inflate(R.layout.respuesta_consulta_item, null);
            ((TextView) viewTitleRequermiento.findViewById(R.id.lbl_titulo)).setTypeface(null, Typeface.BOLD);
            ((TextView) viewTitleRequermiento.findViewById(R.id.lbl_titulo)).setText("Requerimientos \n" );
            ((TextView) viewTitleRequermiento.findViewById(R.id.lbl_contenido)).setVisibility(View.GONE);
            viewRequerimientos.addView(viewTitleRequermiento);

            int indexRequerimiento =0;
            final Iterator<Requerimiento> i = requerimientos.iterator();
            while (i.hasNext()) {
                final Requerimiento requerimiento = i.next();
                View v_data_medical = getLayoutInflater().inflate(R.layout.data_medical, null);
                Especialista especialista = dbUtil.obtenerEspecialista(requerimiento.getSpecialist_id());
                if(especialista != null){
                    if(especialista.getId() != null) {
                        indexRequerimiento += 1;

                        View viewRequermientoIndex = inflater.inflate(R.layout.respuesta_consulta_item, null);
                        ((TextView) viewRequermientoIndex.findViewById(R.id.lbl_titulo)).setText((indexRequerimiento > 1 ? "\n" : "")+"# " +String.valueOf(indexRequerimiento) );
                        ((TextView) viewRequermientoIndex.findViewById(R.id.lbl_contenido)).setVisibility(View.GONE);
                        viewRequerimientos.addView(viewRequermientoIndex);

                        ((TextView) v_data_medical.findViewById(R.id.tv_label_medico)).setText("Especialista: ");
                        ((TextView) v_data_medical.findViewById(R.id.tv_name_medico)).setText(especialista.getNombreCompleto());
                        ((TextView) v_data_medical.findViewById(R.id.tv_number_professional_card)).setText(especialista.getDocumento());
                        viewRequerimientos.addView(v_data_medical);
                    }else{
                        continue;
                    }
                }

                View viewRequermientoFecha = inflater.inflate(R.layout.respuesta_consulta_item, null);

                ((LinearLayout) viewRequermientoFecha.findViewById(R.id.ll_root)).setOrientation(LinearLayout.HORIZONTAL);
                ((TextView) viewRequermientoFecha.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_fecha);
                ((TextView) viewRequermientoFecha.findViewById(R.id.lbl_contenido)).setText(" "+requerimiento.getCreated_at());
                viewRequerimientos.addView(viewRequermientoFecha);


                View viewRequermiento = inflater.inflate(R.layout.respuesta_consulta_item, null);
                ((TextView) viewRequermiento.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_consulta_tipo_requerimiento);
                ((TextView) viewRequermiento.findViewById(R.id.lbl_contenido)).setText(requerimiento.getTipoRequerimiento());
                viewRequerimientos.addView(viewRequermiento);

                View viewComentario = inflater.inflate(R.layout.respuesta_consulta_item, null);
                ((TextView) viewComentario.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_remision_comentario);
                ((TextView) viewComentario.findViewById(R.id.lbl_contenido)).setText(requerimiento.getComentario());
                viewRequerimientos.addView(viewComentario);


                // ocultar botones de descartar y responder cuando fue descartado el requerimiento
                // mostrar comentario de razon de descarte
                DescartarRequerimiento descartarRequerimiento = dbUtil.obtenerDescartarRequerimientoByRequerimientoIdServidor(requerimiento.getIdServidor().toString());
                // TODO: 4/16/19 validar si ya se respondió el requerimiento no mostrar los botones
                // TODO: 4/16/19 La info de descartar el requerimiento debe mostrarse en la historia clinica con la info del medico y no mostrarse en la respuesta
                // TODO: 4/16/19 La info de descartar el requerimiento debe mostrarse en la historia clinica
                View viewItem = inflater.inflate(R.layout.respuesta_requerimiento_item, null);
                if(descartarRequerimiento != null ){
                    (viewItem.findViewById(R.id.btn_resolver)).setVisibility(View.GONE);
                    (viewItem.findViewById(R.id.btn_descartar_requerimiento)).setVisibility(View.GONE);

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
                        viewRequerimientos.addView(viewReason);
                    }else if(!StringUtils.isEmpty(descartarRequerimiento.getOtherReason())){
                        ((TextView) viewReason.findViewById(R.id.lbl_contenido)).setText(descartarRequerimiento.getOtherReason());
                        viewRequerimientos.addView(viewReason);
                    }

                    if(reason != null && StringUtils.isEmpty(descartarRequerimiento.getOtherReason()) == false ){
                        View viewOtherReason= inflater.inflate(R.layout.respuesta_consulta_item, null);
                        ((TextView) viewOtherReason.findViewById(R.id.lbl_titulo)).setText(R.string.detalle_remision_requerimiento_descartado_otra_rason);
                        ((TextView) viewOtherReason.findViewById(R.id.lbl_contenido)).setText(descartarRequerimiento.getOtherReason());
                        viewRequerimientos.addView(viewOtherReason);
                    }



                }else {
                    if(requerimiento.getDescription_request() != null){
                        (viewItem.findViewById(R.id.btn_resolver)).setVisibility(View.GONE);
                        (viewItem.findViewById(R.id.btn_descartar_requerimiento)).setVisibility(View.GONE);
                    }else {
                        (viewItem.findViewById(R.id.btn_resolver)).setOnClickListener(v -> resolverRequerimiento(requerimiento));
                        (viewItem.findViewById(R.id.btn_descartar_requerimiento)).setOnClickListener(v -> descartarRequerimiento(requerimiento));
                        viewRequerimientos.addView(viewItem);
                    }
                }
            }
        }
        else
            viewRequerimientos.setVisibility(View.GONE);
        if(tratamiento== null)
            lnTratamiento.setVisibility(View.GONE);
        else
            lnTratamiento.setVisibility(View.VISIBLE);
        lblTratamiento.setText(tratamiento);

        if(imagenes.size() > 0){
            if(imagenes.size() == 1)
                tv_note_image.setText(contexto.getString(R.string.comment_specialist_from_images_singular));
            ll_content_note_image.setVisibility(View.VISIBLE);

        }else
            ll_content_note_image.setVisibility(View.GONE);

    }

    private void verFormula(Formula formula) {
        new VerFormulaDialog(contexto, formula).show();
    }

    private void compartirFormula(Formula formula) {
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);

        Observable<Response<BaseResponse>> observable = service.compartirFormlaMipres(
                new CompartirFormulaMipresRequest(
                        Session.getInstance(contexto).getCredentials().getToken(),
                        Session.getInstance(contexto).getCredentials().getEmail(),
                        formula.getIdServidor(),
                        idDuenoRespuesta,
                        ConsultaService.FORMULA
                )
        );

        HttpUtils.configurarObservable(
                contexto, observable,
                this::procesarRespuestaCompartir,
                this::procesarExcepcionServicio
        );
    }

    private void verMipres(Mipres mipres) {
        new VerPDFDialog(contexto, new ArchivoDescarga(
                mipres.getMipres(),
                Constantes.DIRECTORIO_TEMPORAL,
                String.format("mipres_%s_consulta_%s", mipres.getIdServidor(), mipres.getIdRespuesta())
        )).show();
    }

    private void compartirMipres(Mipres mipres) {
        ConsultaService service = (ConsultaService) HttpUtils.crearServicio(ConsultaService.class);

        Observable<Response<BaseResponse>> observable = service.compartirFormlaMipres(
                new CompartirFormulaMipresRequest(
                        Session.getInstance(contexto).getCredentials().getToken(),
                        Session.getInstance(contexto).getCredentials().getEmail(),
                        mipres.getIdServidor(),
                        mipres.getIdRespuesta(),
                        ConsultaService.MIPRES
                )
        );

        HttpUtils.configurarObservable(
                contexto, observable,
                this::procesarRespuestaCompartir,
                this::procesarExcepcionServicio
        );
    }

    public void procesarRespuestaCompartir(Response<BaseResponse> response) {
        if(validarRespuestaServicio(response, null)) {
            mostrarMensajeEspera(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar sb) {
                    if (validarRespuestaServicio(response, null))
                        mostrarMensaje(response.body().message, MSJ_INFORMACION);
                }
            });
        }
    }

    private void descartarRequerimiento(Requerimiento requerimiento) {
        new DescartarRequerimientoDialog(contexto, dbUtil, requerimiento.getIdServidor()).show();
        //new DescartarRequerimientoDialog(contexto, idDuenoRespuesta).show();
    }

    private void resolverRequerimiento(Requerimiento requerimiento) {
        Fragment fragment = new RegistrarRequerimientoFragment();
        final Bundle args = new Bundle();
        args.putInt("id_content",R.id.menu_content);
        args.putInt("genre", paciente.getGenre());
        args.putInt(RegistrarRequerimientoFragment.ARG_ID_REQUERIMIENTO, requerimiento.getIdServidor());
        fragment.setArguments(args);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.menu_content, fragment).addToBackStack(
                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        ).commit();
    }

    private void inicializarListaLesiones() {
        imagenes = new ArrayList<>();
        descriptions = new ArrayList<>();

        final List<Lesion> lesiones;
        if(controlMedico != null)
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONTROL, controlMedico.getIdControlConsulta()
            );
        else if(controlEnfermeria != null)
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONTROL, controlEnfermeria.getIdControlConsulta()
            );
        else if(consultaMedicina != null)
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONSULTA, consultaMedicina.getIdConsulta()
            );
        else
            lesiones = dbUtil.obtenerLesiones(
                    DBUtil.TipoConsulta.CONSULTA, consultaEnfermeria.getIdConsulta()
            );

        Iterator<Lesion> iLesion = lesiones.iterator();
        while (iLesion.hasNext()) {
            final List<ImagenLesion> imagenesLesion = iLesion.next().getImagenesLesion();

            if(imagenesLesion != null) {
                Iterator<ImagenLesion> iImagenes = imagenesLesion.iterator();
                ImagenLesion imagenLesion;
                while (iImagenes.hasNext()) {
                    imagenLesion = iImagenes.next();

                    if(!TextUtils.isEmpty(imagenLesion.getEditedPhoto())) {
                        //valida que solo la imagen editada contenga el comentario.
                        descriptions.add(imagenLesion.getDescription());
                        imagenes.add(imagenLesion.getEditedPhoto());
                    }
                }
            }
        }
    }



    @Override
    protected int getIdLayout() {
        return R.layout.fragment_detalle_consulta_respuesta;
    }
}
