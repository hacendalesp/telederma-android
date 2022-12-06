package com.telederma.gov.co.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.modelo.Municipio;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.modelo.Parametro;
import com.telederma.gov.co.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Hernández on 9/08/2018.
 */

public class VerPacienteDialog extends TeledermaDialog {

    private final DBHelper dbHelper;
    private Paciente paciente;
    DBUtil dbUtil = null;

    private TextView pacienteNombre, consentimiento, tipoIdentificacion, valorCondicion,
            numeroIdentificacion, asegurador, estadoCivil, edad, fechaNacimiento, sexo, ocupacion, celular,
            email, direccionDomicilio, departamento, municipio, zonaResidencial, acompananteNombre, acompananteCelular,
            responsableNombre, responsableCelular, responsableParentesco, tipoUsuario, numeroAutorizacion,
            finalidadConsulta, causaExterna;

    private View condicion, acompanante, responsable, autorizacion;

    public VerPacienteDialog(@NonNull Context contexto, DBHelper dbHelper, Paciente paciente) {
        super(contexto);
        this.paciente = paciente;
        this.dbHelper = dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inicializarViews();
        inicializarValores();
    }


    private void inicializarViews() {
        pacienteNombre = findViewById(R.id.id_paciente_nombre);
        consentimiento = findViewById(R.id.id_paciente_consentimiento);
        tipoIdentificacion = findViewById(R.id.id_paciente_tipo_documento);
        numeroIdentificacion = findViewById(R.id.id_paciente_numero_identificacion);
        asegurador = findViewById(R.id.id_paciente_asegurador);
        estadoCivil = findViewById(R.id.id_paciente_estado_civil);
        edad = findViewById(R.id.id_paciente_edad);
        fechaNacimiento = findViewById(R.id.id_paciente_fecha_nacimiento);
        sexo = findViewById(R.id.id_paciente_opcion_sexo);
        ocupacion = findViewById(R.id.id_paciente_ocupacion);
        celular = findViewById(R.id.id_paciente_celular);
        email = findViewById(R.id.id_paciente_email);
        direccionDomicilio = findViewById(R.id.id_paciente_direccion);
        departamento = findViewById(R.id.id_paciente_departamento);
        municipio = findViewById(R.id.id_paciente_municipio);
        zonaResidencial = findViewById(R.id.id_paciente_opcion_residencial);
        tipoUsuario = findViewById(R.id.id_paciente_tipo_usuario);
        finalidadConsulta = findViewById(R.id.id_paciente_finalidad);
        causaExterna = findViewById(R.id.id_paciente_causa_externa);

        if (paciente.getType_condition() != null) {
            condicion = findViewById(R.id.id_paciente_condicion);
            condicion.setVisibility(View.VISIBLE);
            valorCondicion = findViewById(R.id.txt_id_paciente_condicion);
        }

        if (paciente.getInformacionPaciente().getCompanion()) {
            acompanante = findViewById(R.id.id_paciente_persona_acompanante);
            acompanante.setVisibility(View.VISIBLE);
            acompananteNombre = findViewById(R.id.id_paciente_acompanante_nombre);
            acompananteCelular = findViewById(R.id.id_paciente_acompanante_celular);
        }

        if (paciente.getInformacionPaciente().getResponsible()) {
            responsable = findViewById(R.id.id_paciente_persona_responsable);
            responsable.setVisibility(View.VISIBLE);
            responsableNombre = findViewById(R.id.id_paciente_responsable_nombre);
            responsableCelular = findViewById(R.id.id_paciente_responsable_celular);
            responsableParentesco = findViewById(R.id.id_paciente_responsable_parentesco);
        }

//        if (!TextUtils.isEmpty(paciente.getInformacionPaciente().getAuthozation_number())) {
            autorizacion = findViewById(R.id.id_paciente_autorizacion);
            autorizacion.setVisibility(View.VISIBLE);
            numeroAutorizacion = findViewById(R.id.id_paciente_numero_autorizacion);
//        }
    }

    private void inicializarValores() {
        dbUtil = DBUtil.getInstance(dbHelper, contexto);

        if (paciente.getType_condition() != null) {
            Parametro parametroCondicion = dbUtil.obtenerParametro(paciente.getType_condition(), Parametro.TIPO_PARAMETRO_TIPO_CONDICION);
            if(parametroCondicion != null)
                valorCondicion.setText(parametroCondicion.getNombre());
            else
                condicion.setVisibility(View.GONE);
        }

        pacienteNombre.setText(paciente.getNombreCompleto());
        consentimiento.setText(Utils.getInstance(contexto).obtenerValorBooleano(paciente.getInformacionPaciente().getTerms_conditions()));
        tipoIdentificacion.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_TIPO_DOCUMENTO, paciente.getType_document()
        ));
        numeroIdentificacion.setText(paciente.getNumber_document());
        asegurador.setText(dbUtil.obtenerNombreAseguradora(paciente.getInformacionPaciente().getInsurance_id()));
        estadoCivil.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_ESTADO_CIVIL, paciente.getInformacionPaciente().getCivil_status()
        ));
        edad.setText(String.format("%d %s",
                paciente.getInformacionPaciente().getAge(),
                dbUtil.obtenerNombreParametro(
                        Parametro.TIPO_PARAMETRO_UNIDAD_DE_MEDIDA,
                        paciente.getInformacionPaciente().getUnit_measure_age()
                )
        ));
        fechaNacimiento.setText(paciente.getBirthdate());
        sexo.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_GENERO, paciente.getGenre()
        ));
        ocupacion.setText(paciente.getInformacionPaciente().getOccupation());
        celular.setText(paciente.getInformacionPaciente().getPhone());
        email.setText(paciente.getInformacionPaciente().getEmail());
        direccionDomicilio.setText(paciente.getInformacionPaciente().getAddress());

        final Municipio municipioBD = dbUtil.obtenerMunicipio(paciente.getInformacionPaciente().getMunicipality_id());
        departamento.setText(dbUtil.obtenerNombreDepartamento(municipioBD.getIdDepartamento()));
        municipio.setText(municipioBD.getNombre());

        zonaResidencial.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_ZONA_URBANA, paciente.getInformacionPaciente().getUrban_zone()
        ));
        tipoUsuario.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_TIPO_USUARIO, paciente.getInformacionPaciente().getType_user()
        ));
        finalidadConsulta.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_PROPOSITO_DE_CONSULTA, paciente.getInformacionPaciente().getPurpose_consultation()
        ));
        causaExterna.setText(dbUtil.obtenerNombreParametro(
                Parametro.TIPO_PARAMETRO_CAUSA_EXTERNA, paciente.getInformacionPaciente().getExternal_cause()
        ));

        if (paciente.getInformacionPaciente().getCompanion()) {
            acompananteNombre.setText(paciente.getInformacionPaciente().getName_companion());
            acompananteCelular.setText(paciente.getInformacionPaciente().getPhone_companion());
        }

        if (paciente.getInformacionPaciente().getResponsible()) {
            responsableNombre.setText(paciente.getInformacionPaciente().getName_responsible());
            responsableCelular.setText(paciente.getInformacionPaciente().getPhone_responsible());
            responsableParentesco.setText(paciente.getInformacionPaciente().getRelationship());
        }

        if (!TextUtils.isEmpty(paciente.getInformacionPaciente().getAuthozation_number())) {
            String number_authorization = paciente.getInformacionPaciente().getAuthozation_number();
            numeroAutorizacion.setText(((number_authorization == null) ? "No reporta" : number_authorization));
        }else{
            numeroAutorizacion.setText("No reporta");
        }
    }



    @Override
    protected int getIdLayout() {
        return R.layout.dialog_ver_paciente;
    }

}
