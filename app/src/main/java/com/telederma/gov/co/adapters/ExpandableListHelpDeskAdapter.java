package com.telederma.gov.co.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.MenuActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.dialogs.VerTextoDialog;
import com.telederma.gov.co.dialogs.VerTicketDialog;
import com.telederma.gov.co.fragments.DetalleConsultaFragment;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.HelpDesk;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;

import java.util.List;

import static com.telederma.gov.co.utils.Constantes.RAW_FILE_PREGUNTAS_FRECUENTES;

public class ExpandableListHelpDeskAdapter extends BaseExpandableListAdapter {

    private AppCompatActivity contexto;
    private List<HelpDesk> listaConsultas;

    public ExpandableListHelpDeskAdapter(AppCompatActivity context, List<HelpDesk> listaConsultas) {
        this.contexto = context;
        this.listaConsultas = listaConsultas;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return "";
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;//listaConsultas.get(groupPosition).getControles().size();
    }

    @Override
    public HelpDesk getGroup(int groupPosition) {
        if (listaConsultas.size() > 0)
            return listaConsultas.get(groupPosition);
        else
            return null;
    }

    @Override
    public int getGroupCount() {
        return listaConsultas.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final HelpDesk group = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = inflater.inflate(R.layout.list_help_desk, null);
        }

        if (group == null)
            return convertView;

        ((TextView) convertView.findViewById(R.id.txt_group_nombre)).setText((group.getIdServidor()!=null)?group.getIdServidor().toString():"");
        ((TextView) convertView.findViewById(R.id.txt_group_fecha)).setText(group.getSubject().toString());
        asignarIcono(
                group.getStatus(),
                convertView.findViewById(R.id.img_group_estado),
                convertView.findViewById(R.id.txt_group_texto_estado)
        );

        LinearLayout layItemGroup = convertView.findViewById(R.id.layout_item_group);
        //LinearLayout layItemCompartir = convertView.findViewById(R.id.layout_item_compartir);

        layItemGroup.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f
        ));



                convertView.setOnClickListener(view -> {
                    new VerTicketDialog(this.contexto,group).show();
                });


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    private void asignarIcono(Integer estado, ImageView imgEstado, TextView lblEstado) {
        int icono = 0, color = 0, textoEstado = 0;

        if (HelpDesk.ESTADO_MESA_AYUDA_RESUELTO.equals(estado)) {
            icono = R.drawable.ic_check;
            color = R.color.green;
            textoEstado = R.string.estado_consulta_1;
        } else if (HelpDesk.ESTADO_MESA_AYUDA_PENDIENTE.equals(estado)) {
            icono = R.drawable.ic_hourglass;
            color = R.color.grayLight;
            textoEstado = R.string.estado_consulta_3;
        } else if (HelpDesk.ESTADO_MESA_AYUDA_SIN_ENVIAR.equals(estado)) {
            icono = R.drawable.ic_hourglass_empty;
            color = R.color.grayDark;
            textoEstado = R.string.estado_consulta_0;
        }
        else if(HelpDesk.ESTADO_MESA_AYUDA_EVALUANDO.equals(estado))
        {
            icono = R.drawable.ic_evaluation;
            color = R.color.green;
            textoEstado = R.string.estado_consulta_8;
        }

        if((estado != null) && estado != -1) {

            if (lblEstado != null)
                lblEstado.setText(contexto.getResources().getString(textoEstado));

            imgEstado.setBackgroundResource(icono);
            imgEstado.setBackgroundTintList(
                    contexto.getResources().getColorStateList(color, contexto.getTheme())
            );
        }
    }




}
