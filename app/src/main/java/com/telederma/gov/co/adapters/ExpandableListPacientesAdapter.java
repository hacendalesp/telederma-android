package com.telederma.gov.co.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.dialogs.VerOpcionesDialog;
import com.telederma.gov.co.fragments.BusquedaPacienteFragment;
import com.telederma.gov.co.fragments.ConsultasContentFragment;
import com.telederma.gov.co.fragments.ConsultasFragment;
import com.telederma.gov.co.fragments.DetalleConsultaFragment;
import com.telederma.gov.co.fragments.HistoriaFragment;
import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.modelo.Consulta;
import com.telederma.gov.co.modelo.Lesion;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;
import static com.telederma.gov.co.utils.Utils.MSJ_INFORMACION;

public class ExpandableListPacientesAdapter extends BaseExpandableListAdapter {

    private AppCompatActivity contexto;
    //Parent list or header list.
    public List<Paciente> lista_pacientes;
    //Child list inside the header list or the parent list.
    public Map<Integer, List<Consulta>> mapa_consultas;

    private boolean mostrarOpcionCompartir, mostrarBotonNuevaConsulta, activarEventoLongClick;
    public int tabWorking=0, tabSelected;

    public ExpandableListPacientesAdapter(AppCompatActivity context, List<Paciente> lista_pacientes, Map<Integer,
            List<Consulta>> mapa_consultas, boolean mostrarOpcionCompartir, boolean mostrarBotonNuevaConsulta) {
        this.contexto = context;
        this.lista_pacientes = lista_pacientes;
        this.mapa_consultas = mapa_consultas;
        this.mostrarOpcionCompartir = mostrarOpcionCompartir;
        this.mostrarBotonNuevaConsulta = mostrarBotonNuevaConsulta;
        this.activarEventoLongClick = true;
        this.tabSelected = -1;
    }

    public ExpandableListPacientesAdapter(AppCompatActivity context, List<Paciente> lista_pacientes, Map<Integer,
            List<Consulta>> mapa_consultas, boolean mostrarOpcionCompartir, boolean mostrarBotonNuevaConsulta, boolean activarEventoLongClick, int tabSelected) {
        this.contexto = context;
        this.lista_pacientes = lista_pacientes;
        this.mapa_consultas = mapa_consultas;
        this.mostrarOpcionCompartir = mostrarOpcionCompartir;
        this.mostrarBotonNuevaConsulta = mostrarBotonNuevaConsulta;
        this.activarEventoLongClick = activarEventoLongClick;
        this.tabSelected = tabSelected;
    }

    @Override
    public Consulta getChild(int groupPosition, int childPosition) {
        if (lista_pacientes.size() > 0 && mapa_consultas.get(lista_pacientes.get(groupPosition).getIdServidor()) != null)
            return mapa_consultas.get(
                    lista_pacientes.get(groupPosition).getIdServidor()
            ).get(childPosition);

        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        //TODO Refactorizar y comentar
        final Consulta consulta = getChild(groupPosition, childPosition);
        if (consulta != null) {
            LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            if (isLastChild && mostrarBotonNuevaConsulta) {
                convertView = inflater.inflate(R.layout.list_item_nueva_consulta, null);
                LinearLayout llNuevaConsulta = convertView.findViewById(R.id.ll_nueva_consulta);
                llNuevaConsulta.setOnClickListener(v -> {
                    abrirNuevaConsulta(consulta);
                });

                Button btn1 = convertView.findViewById(R.id.btn1);
                btn1.setOnClickListener(v -> {
                    abrirNuevaConsulta(consulta);
                });

                ImageButton btn2 = convertView.findViewById(R.id.btn2);
                btn2.setOnClickListener(v -> {
                    abrirNuevaConsulta(consulta);
                });

                Button btn3 = convertView.findViewById(R.id.btn3);
                btn3.setOnClickListener(v -> {
                    abrirNuevaConsulta(consulta);
                });


            } else
                convertView = inflater.inflate(R.layout.list_item, null);

            if(consulta.getRespuestaEspecialista() !=null &&consulta.getRespuestaEspecialista().size()> 0 && consulta.getRespuestaEspecialista().get(0).getDiagnosticos().size()>0)
                consulta.setImpresionDiagnostica(consulta.getRespuestaEspecialista().get(0).getDiagnosticos().get(0).getDiagnosticoPrincipal());

            TextView nombre_consulta = convertView.findViewById(R.id.txt_item_nombre);
            nombre_consulta.setText(TextUtils.isEmpty(consulta.getImpresionDiagnostica()) ?
                    contexto.getString(R.string.consultas_pendiente_diagnostico) : consulta.getImpresionDiagnostica());


            TextView controles = convertView.findViewById(R.id.txt_item_cantidad);
            String controles_ = "0";
            if(consulta.getCantidadControles()!=null)
                controles_ = String.valueOf(consulta.getCantidadControles());

            controles.setText(controles_);

            TextView fecha_consulta = convertView.findViewById(R.id.txt_item_fecha);
            fecha_consulta.setText(consulta.getCreated_at().toString());

            ImageView consulta_estado = convertView.findViewById(R.id.img_item_estado);

            asignarIcono(consulta.getEstado(), consulta_estado, null);


            if(this.activarEventoLongClick){
                if (consulta.getArchivado() == null) {
                    convertView.setOnLongClickListener(view -> {
                        if(tabWorking != ConsultasContentFragment.TAB_PENDIENTES) {
                            VerOpcionesDialog opciones_dialog = new VerOpcionesDialog(contexto, contexto.getString(R.string.archivar));
                            opciones_dialog.setConsulta(consulta);
                            opciones_dialog.setAdapter(this);
                            opciones_dialog.show();
                        }
                        return true;
                    });
                } else if (consulta.getArchivado() != null) {
                    convertView.setOnLongClickListener(view -> {
                        if(tabWorking != ConsultasContentFragment.TAB_PENDIENTES) {
                            VerOpcionesDialog opciones_dialog = new VerOpcionesDialog(contexto, contexto.getString(R.string.desarchivar));
                            opciones_dialog.setConsulta(consulta);
                            opciones_dialog.setAdapter(this);
                            opciones_dialog.show();
                        }
                        return true;
                    });
                }
            }


            if (!Consulta.ESTADO_CONSULTA_ARCHIVADO.equals(consulta.getEstado())) {
                convertView.setOnClickListener(view -> {

                    DBHelper util = new DetalleConsultaFragment().getDbUtil();
                    final DBUtil dbUtil = DBUtil.getInstance(util, contexto);

                    List<Lesion>  l  = dbUtil.obtenerLesiones(DBUtil.TipoConsulta.CONSULTA ,consulta.getIdServidor() ) ;

                    if(HttpUtils.validarConexionApi() || l.size() >0)
                    {
                        final Fragment fragment = new DetalleConsultaFragment();
                        final Bundle args = new Bundle();
                        final FragmentManager fm = contexto.getSupportFragmentManager();
                        final FragmentTransaction ft = fm.beginTransaction();

                        args.putInt(DetalleConsultaFragment.ARG_ID_CONSULTA, consulta.getIdServidor());
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


                });
            }

        }

        return convertView;
    }

    public void abrirNuevaConsulta(Consulta consulta){
        final Fragment fragment = new HistoriaFragment();
        final Bundle args = new Bundle();
        final FragmentManager fm = contexto.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        args.putInt("paciente_id", consulta.getIdPaciente());
        args.putBoolean("resumen", false);
        fragment.setArguments(args);
        ft.replace(R.id.menu_content, fragment).addToBackStack(
                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        ).commit();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        final Integer idPaciente = lista_pacientes.get(groupPosition).getIdServidor();

        return mapa_consultas.get(idPaciente) != null ? mapa_consultas.get(idPaciente).size() : 0;
    }

    @Override
    public Paciente getGroup(int groupPosition) {
        if (lista_pacientes.size() > 0)
            return lista_pacientes.get(groupPosition);
        else
            return null;
    }

    @Override
    public int getGroupCount() {
        return lista_pacientes.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }




    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final Paciente paciente = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.contexto.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = inflater.inflate(R.layout.list_group, null);
        }


        if(paciente != null) {
            try{
                if (mapa_consultas.get(paciente.getIdServidor()).size() == 0) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newConsult();
                        }
                    });
                }
            } catch (Exception e){

            }

        }

        if (paciente == null)
            return convertView;

        final TextView nombre_paciente = convertView.findViewById(R.id.txt_group_nombre);
        nombre_paciente.setText(paciente.getNombreCompleto());

        final TextView fecha_paciente = convertView.findViewById(R.id.txt_group_fecha);
        // TODO: 4/12/19 created_at
        //fecha_paciente.setText(paciente.getCreated_at().toString());

        String fecha = obtenerUltimaFecha(paciente.getCreated_atFormatDate(), mapa_consultas.get(paciente.getIdServidor()));
        fecha_paciente.setText(fecha);
        if (getChildrenCount(groupPosition) < 0)
            paciente.setStatus(
                    paciente.getStatus() != null ? paciente.getStatus() : Consulta.ESTADO_CONSULTA_PROCESO
            );

       asignarIcono(
                paciente.getStatus(),
                convertView.findViewById(R.id.img_group_estado),
                convertView.findViewById(R.id.txt_group_texto_estado)
        );




        if (mostrarOpcionCompartir) {
            LinearLayout layItemGroup = convertView.findViewById(R.id.layout_item_group);
            //LinearLayout layItemCompartir = convertView.findViewById(R.id.layout_item_compartir);

            layItemGroup.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f
            ));
            /*layItemGroup.setOnLongClickListener(view -> {
                VerOpcionesDialog opciones_dialog =  new VerOpcionesDialog(contexto);
                opciones_dialog.setConsultas( mapa_consultas.get(paciente.getIdServidor()));
                opciones_dialog.setAdapter(this);
                opciones_dialog.show();
                return false;
            });*/








         /*   layItemCompartir.setVisibility(View.VISIBLE);
            layItemCompartir.setOnClickListener(v -> {
                List<CompartirConsultaDialog.CompartirConsulta> consultas = new ArrayList<>();
                for (Consulta consulta : mapa_consultas.get(paciente.getIdServidor())) {
                    if (TextUtils.isEmpty(consulta.getImpresionDiagnostica()))
                        consulta.setImpresionDiagnostica(contexto.getString(R.string.consultas_pendiente_diagnostico));

                    consultas.add(new CompartirConsultaDialog.CompartirConsulta(
                            consulta.getImpresionDiagnostica(),
                            consulta.getIdServidor()
                    ));
                }

                Dialog dialog = new CompartirConsultaDialog(contexto, consultas);
                dialog.show();
            });*/
        }

        return convertView;
    }

    public String obtenerUltimaFecha(Date fechaDefecto, List<Consulta> consultas){
        Date fecha = fechaDefecto;
        try{

            for(Consulta consulta : consultas){
                if(consulta.getCreated_atFormatDate().after(fecha)){
                    fecha = consulta.getCreated_atFormatDate();
                }

            }

        }catch (Exception e){
            fecha = fechaDefecto;
        }

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH);
        if(fecha != null)
            return f.format(fecha);
        else
            return "";


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

    @SuppressLint("RestrictedApi")
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

        if((Consulta.ESTADO_CONSULTA_PENDIENTE.equals(estado)) && (tabSelected == 0)){
            icono = R.drawable.ic_check;
            color = R.color.green;
            textoEstado = R.string.estado_consulta_1;
        }

        if(estado != -1) {
            if (lblEstado != null) lblEstado.setText(contexto.getResources().getString(textoEstado));
            imgEstado.setBackgroundResource(icono);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                ((AppCompatImageView) imgEstado).setSupportBackgroundTintList(contexto.getResources().getColorStateList(color, contexto.getTheme()));
            } else {
                imgEstado.setBackgroundTintList(
                        contexto.getResources().getColorStateList(color, contexto.getTheme())
                );
            }
        }
    }

    public Map<Integer, List<Consulta>> getMapa_consultas()
    {
        return mapa_consultas;
    }

    private void newConsult(){
        FragmentTransaction ft = contexto.getSupportFragmentManager().beginTransaction();
        Fragment fragment = new HistoriaFragment();
        Bundle arguments = new Bundle();
        if (lista_pacientes.size() > 0 && lista_pacientes.get(0).getIdServidor() != null)
            arguments.putInt("paciente_id", lista_pacientes.get(0).getIdServidor());
        else if (lista_pacientes.size() > 0)
            arguments.putInt("paciente_id_local", lista_pacientes.get(0).getId());
        arguments.putBoolean("resumen", false);
        arguments.putBoolean("cuerpo_frente", true);
        arguments.putInt("genero", 1);
        arguments.putString("cedula_paciente",lista_pacientes.get(0).getNumber_document());
        fragment.setArguments(arguments);
        ft.replace(R.id.menu_content, fragment).addToBackStack(
                Constantes.TAG_MENU_ACTIVITY_BACK_STACK
        ).commit();
    }

}
