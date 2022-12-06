package com.telederma.gov.co.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.Formula;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public class VerFormulaDialog extends TeledermaDialog {

    private Formula formula;

    public VerFormulaDialog(@NonNull Context contexto, Formula formula) {
        super(contexto);

        this.formula = formula;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.txt_codigo_medicamento)).setText(formula.getCodigoMedicamento());
        ((TextView) findViewById(R.id.txt_tipo_medicamento)).setText(formula.getTipoMedicamento());
        ((TextView) findViewById(R.id.txt_medicamento_generico)).setText(formula.getNombreGenericoMedicamento());
        ((TextView) findViewById(R.id.txt__forma_farmaceutica)).setText(formula.getFormaFarmaceutica());
        ((TextView) findViewById(R.id.txt_concentracion_medicamento)).setText(formula.getConcentracionDroga());
        ((TextView) findViewById(R.id.txt_unidad_medida_medicamento)).setText(formula.getUnidadMedidaMedicamento());
        ((TextView) findViewById(R.id.txt_numero_unidades)).setText(formula.getNumeroUnidades());
        ((TextView) findViewById(R.id.txt_comentarios)).setText(formula.getComentarios());
    }

    @Override
    protected int getIdLayout() {
        return R.layout.dialog_ver_formula;
    }
}
