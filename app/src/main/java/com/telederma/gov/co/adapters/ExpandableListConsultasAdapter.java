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

import com.telederma.gov.co.R;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.fragments.DetalleConsultaFragment;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.ConsultaEnfermeria;
import com.telederma.gov.co.modelo.ConsultaMedica;
import com.telederma.gov.co.modelo.ControlConsulta;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Utils;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;


import java.util.List;

public class ExpandableListConsultasAdapter extends BaseExpandableListAdapter {

    private AppCompatActivity contexto;
    private List<Consulta> listaConsultas;

    public ExpandableListConsultasAdapter(AppCompatActivity context, List<Consulta> listaConsultas) {
        this.contexto = context;
        this.listaConsultas = listaConsultas;
    }

    @Override
    public ControlConsulta getChild(int groupPosition, int childPosition) {
        return listaConsultas.get(groupPosition).getControles().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        final ControlConsulta control = getChild(groupPosition, childPosition);
        if (control != null) {
            LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = inflater.inflate(R.layout.list_item, null);

            convertView.findViewById(R.id.txt_item_descripcion).setVisibility(View.GONE);
            convertView.findViewById(R.id.txt_item_cantidad).setVisibility(View.GONE);

            ((TextView) convertView.findViewById(R.id.txt_item_nombre)).setText(
                    String.format("%s %d", contexto.getResources().getString(R.string.control), childPosition+1)
            );
            ((TextView) convertView.findViewById(R.id.txt_item_fecha)).setText(
                    // TODO: 4/12/19 created_at
                    (control.getCreated_at() == null ? "Sincronizando..." : control.getCreated_at().toString())
            );
            asignarIcono(
                    control.getEstado(), convertView.findViewById(R.id.img_item_estado), null
            );

            convertView.setOnClickListener(view -> {
                if(control.getIdConsulta() > 0 && control.getIdServidor() != null) {
                    DBHelper util = new DetalleConsultaFragment().getDbUtil();
                    final DBUtil dbUtil = DBUtil.getInstance(util, contexto);
                    List<Lesion>  l  = dbUtil.obtenerLesiones(DBUtil.TipoConsulta.CONSULTA ,control.getIdConsulta() ) ;

                    if(HttpUtils.validarConexionApi() || l.size() >0)
                    {
                        final Fragment fragment = new DetalleConsultaFragment();
                        final Bundle args = new Bundle();
                        final FragmentManager fm = contexto.getSupportFragmentManager();
                        final FragmentTransaction ft = fm.beginTransaction();

                        args.putInt(DetalleConsultaFragment.ARG_ID_CONSULTA, control.getIdConsulta());
                        args.putInt(DetalleConsultaFragment.ARG_ID_CONTROL, control.getIdServidor());
                        //args.putInt(DetalleConsultaFragment.ARG_ID_CONSULTA, control.getIdServidor());
                        fragment.setArguments(args);
                        ft.replace(R.id.menu_content, fragment).addToBackStack(
                                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                        ).commit();
                    }
                    else {
                        String mensaje = contexto.getResources().getString((R.string.mensaje_conexion_internet_sin_datos_locales));
                        Utils.getInstance(contexto).mostrarMensaje(contexto,mensaje,MSJ_ERROR,3,null);
                     }
                }else{
                    Toast.makeText(contexto, "Control sincronizando...", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listaConsultas.get(groupPosition).getControles().size();
    }

    @Override
    public Consulta getGroup(int groupPosition) {
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
        final Consulta group = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        if (group == null)
            return convertView;


        if(group.getRespuestaEspecialista() !=null && group.getRespuestaEspecialista().size()> 0 && group.getRespuestaEspecialista().get(0).getDiagnosticos().size()>0)
            group.setImpresionDiagnostica(group.getRespuestaEspecialista().get(0).getDiagnosticos().get(0).getDiagnosticoPrincipal());
        else  if (TextUtils.isEmpty(group.getImpresionDiagnostica()))
            group.setImpresionDiagnostica(contexto.getString(R.string.consultas_pendiente_diagnostico));

        ((TextView) convertView.findViewById(R.id.txt_group_nombre)).setText(group.getImpresionDiagnostica());
        // TODO: 4/12/19 created_at
        ((TextView) convertView.findViewById(R.id.txt_group_fecha)).setText(group.getCreated_at().toString());
        asignarIcono(
                group.getEstado(),
                convertView.findViewById(R.id.img_group_estado),
                convertView.findViewById(R.id.txt_group_texto_estado)
        );

        LinearLayout layItemGroup = convertView.findViewById(R.id.layout_item_group);
        //LinearLayout layItemCompartir = convertView.findViewById(R.id.layout_item_compartir);

        layItemGroup.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f
        ));


        if(group.getCantidadControles() == 0) {
            convertView.setOnClickListener(view -> {
                int idConsulta = 0;
                if (group instanceof ConsultaMedica ) {
                    idConsulta = ((ConsultaMedica) group).getIdConsulta();
                }else if(group instanceof ConsultaEnfermeria) {
                    idConsulta = ((ConsultaEnfermeria) group).getIdConsulta();
                }
                if (group.getIdServidor() > 0 && group.getIdServidor() != null && idConsulta > 0) {


                    DBHelper util = new DetalleConsultaFragment().getDbUtil();
                    final DBUtil dbUtil = DBUtil.getInstance(util, contexto);

                    List<Lesion>  l  = dbUtil.obtenerLesiones(DBUtil.TipoConsulta.CONSULTA ,idConsulta ) ;

                    if(HttpUtils.validarConexionApi() || l.size() >0)
                    {
                        final Fragment fragment = new DetalleConsultaFragment();
                        final Bundle args = new Bundle();
                        final FragmentManager fm = contexto.getSupportFragmentManager();
                        final FragmentTransaction ft = fm.beginTransaction();

                        args.putInt(DetalleConsultaFragment.ARG_ID_CONSULTA, idConsulta);
                        //args.putInt(DetalleConsultaFragment.ARG_ID_CONSULTA, control.getIdServidor());
                        fragment.setArguments(args);
                        ft.replace(R.id.menu_content, fragment).addToBackStack(
                                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                        ).commit();
                    }
                    else
                    {
                        String mensaje= contexto.getResources().getString((R.string.mensaje_conexion_internet_sin_datos_locales));
                        Utils.getInstance(contexto).mostrarMensaje(contexto,mensaje,MSJ_ERROR,3,null);
                    }
                } else {
                    Toast.makeText(contexto, "Consulta sincronizando...", Toast.LENGTH_SHORT).show();
                }
            });
        }


        /*layItemCompartir.setVisibility(View.VISIBLE);
        layItemCompartir.setOnClickListener(v -> {
            new CompartirConsultaDialog(contexto, new ArrayList<CompartirConsultaDialog.CompartirConsulta>() {{
                add(new CompartirConsultaDialog.CompartirConsulta(
                        group.getImpresionDiagnostica(),
                        group.getIdServidor()
                ));
            }}).show();
        });*/

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

        if (Consulta.ESTADO_CONSULTA_RESUELTO.equals(estado)) {
            icono = R.drawable.ic_check;
            color = R.color.green;
            textoEstado = R.string.estado_consulta_1;
        } else if (Consulta.ESTADO_CONSULTA_REQUERIMIENTO.equals(estado)) {
            icono = R.drawable.ic_warning;
            color = R.color.yellow;
            textoEstado = R.string.estado_consulta_2;
        } else if (Consulta.ESTADO_CONSULTA_PENDIENTE.equals(estado)) {
            icono = R.drawable.ic_hourglass;
            color = R.color.grayLight;
            textoEstado = R.string.estado_consulta_3;
        } else if (Consulta.ESTADO_CONSULTA_ARCHIVADO.equals(estado)) {
            icono = R.drawable.ic_unarchive;
            color = R.color.indigo;
            textoEstado = R.string.estado_consulta_4;
        } else if (Consulta.ESTADO_CONSULTA_PROCESO.equals(estado)) {
            icono = R.drawable.ic_assignment;
            color = R.color.grayDark;
            textoEstado = R.string.estado_consulta_5;
        } else if (Consulta.ESTADO_CONSULTA_SIN_CREDITOS.equals(estado)) {
            icono = R.drawable.ic_sin_creditos;
            color = R.color.red;
            textoEstado = R.string.estado_consulta_6;
        } else if (Consulta.ESTADO_CONSULTA_SIN_ENVIAR.equals(estado)) {
            icono = R.drawable.ic_hourglass_empty;
            color = R.color.grayDark;
            textoEstado = R.string.estado_consulta_0;
        }
        else if(Consulta.ESTADO_CONSULTA_REMISION.equals(estado))
        {
            icono = R.drawable.ic_remision;
            color = R.color.red;
            textoEstado = R.string.estado_consulta_7;
        }
        else if(Consulta.ESTADO_CONSULTA_EVALUANDO.equals(estado))
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
