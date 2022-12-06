package com.telederma.gov.co.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.interfaces.ISincronizable;
import com.telederma.gov.co.modelo.DataSincronizacion;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Utils;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by juan on 6/01/18.
 */


public class LogAdapter extends RecyclerView.Adapter {
    private ArrayList<PendienteSincronizacion> dataSet;
    private ArrayList<PendienteSincronizacion> dataSetCopy = new ArrayList<PendienteSincronizacion>();
    public Context context;
    String tag = "AdapterLog";
    int total_types;
    SharedPreferences settings;
    public SharedPreferences.Editor editor;
    LogAdapter self;
    Dialog dialog_request_contact;
    boolean call_first_time = false;
    public String term = "";
    int page = 1, per = 50;
    DBUtil dbUtil;
    RecyclerView recyclerView;

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_date, tv_status, tv_id_local;
        Button btn_delete, btn_retry, btn_skip;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            this.btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            this.btn_retry = (Button) itemView.findViewById(R.id.btn_retry);
            this.btn_skip = (Button) itemView.findViewById(R.id.btn_skip);
        }
    }

    public LogAdapter(Context context) {
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        this.dbUtil = DBUtil.getInstance(dbHelper, context);

        recyclerView = recyclerView;
        Log.i(tag, "inside==>LogAdapter");
        this.dataSet =  (ArrayList)dbUtil.obtenerSincronizables();
        if(dataSet != null) {
            this.dataSetCopy.addAll(dataSet);
        }else{
           this.dataSet = new ArrayList<>();
        }
        this.context = context;
        self = this;
//        total_types = dataSet.size();
        total_types = 0;

    }

    public LogAdapter(Context context, ArrayList data) {
        Log.i(tag, "inside==>LogAdapter");
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        this.dbUtil = DBUtil.getInstance(dbHelper, context);


        this.dataSet =  data;
        if(dataSet != null) {
            this.dataSetCopy.addAll(dataSet);
        }else{
           this.dataSet = new ArrayList<>();
        }
        this.context = context;
        self = this;
        total_types = dataSet.size();
        //total_types = 0;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log2, parent, false);
        return new ListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final PendienteSincronizacion object = (PendienteSincronizacion) dataSet.get(listPosition);
        if (object != null) {

            ((ListViewHolder) holder).tv_date.setText(object.getHumanDate());
            ((ListViewHolder) holder).tv_name.setText(object.getTable());
            ((ListViewHolder) holder).tv_status.setText(String.valueOf(object.getStatusName()));
            ((ListViewHolder) holder).btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Eliminar...", Toast.LENGTH_SHORT).show();
//                    DataSincronizacion ds = new DataSincronizacion();
//                    ds.eliminarPendiente(Integer.valueOf(object.getId_local()), object.getTable());
                    //dbUtil.eliminarPendiente(Integer.valueOf(object.getId_local()), object.getTable());
                    if(object.getId() == null){
                        Toast.makeText(context, "Id null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dbUtil.eliminarPendienteById(object.getId());
                    dataSet.remove(object);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Registro eliminado correctamente...", Toast.LENGTH_SHORT).show();
                }
            });
            ((ListViewHolder) holder).btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Reintentar...", Toast.LENGTH_SHORT).show();
                    DataSincronizacion ds = new DataSincronizacion();
                    ds.sincronizar_registro( object, context);
                }
            });

            ((ListViewHolder) holder).btn_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Omitir...", Toast.LENGTH_SHORT).show();
                    DataSincronizacion ds = new DataSincronizacion();
                    ds.actualizarPendiente(object.getId(), PendienteSincronizacion.NOMBRE_CAMPO_STATUS, "2");
                    object.setStatus(2);
                    dataSet.set(listPosition, object);
                    notifyDataSetChanged();
                }
            });
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

//    public void setData(JSONArray jsonArray) {
//        Log.i(tag, "resultListProducts===" + jsonArray);
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//
//                PendienteSincronizacion gift = new PendienteSincronizacion(jsonArray.getJSONObject(i));
//                this.dataSet.add(gift);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
////        this.dataSet = data;
//        this.dataSetCopy.addAll(dataSet);
//        total_types = jsonArray.length();
//        self.notifyDataSetChanged();
//
//    }


}
