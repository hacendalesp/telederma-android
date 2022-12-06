package com.telederma.gov.co;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.telederma.gov.co.adapters.LogAdapter;
import com.telederma.gov.co.modelo.BaseEntity;
import com.telederma.gov.co.modelo.PendienteSincronizacion;
import com.telederma.gov.co.utils.DBUtil;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

public class LogActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    LogAdapter adapter;
    Context context;
    DBUtil dbUtil;
    ArrayList data = new ArrayList();
    TextView tv_message;
    Button btn_all, btn_pendings, btn_skips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_log);
        init();

    }

    public void init(){
        tv_message = findViewById(R.id.tv_message);
        btn_all = findViewById(R.id.btn_all);
        btn_pendings = findViewById(R.id.btn_pendings);
        btn_skips = findViewById(R.id.btn_skips);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.dbUtil = DBUtil.getInstance(getDbHelper(), context);

        //data = (ArrayList) dbUtil.obtenerSincronizables();
        data = (ArrayList) obtenerSincronizables();
        if(data.size() > 0) {
            tv_message.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter = new LogAdapter(this, data);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);
        }else{
            tv_message.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }


        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAdapter("all");
            }
        });
        btn_pendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAdapter("pendings");
            }
        });
        btn_skips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAdapter("skips");
            }
        });
    }


    private void createAdapter(String scope){
        if(data.size() > 0) {
            tv_message.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if(scope != null) {
                if(scope.equals("all")){
                    data = (ArrayList) obtenerSincronizables();
                    adapter = new LogAdapter(this, data);
                }else if(scope.equals("pendings")){
                    data = (ArrayList) obtenerSincronizablesPendientes();
                    adapter = new LogAdapter(this, data);
                }else if(scope.equals("skips")){
                    data = (ArrayList) obtenerSincronizablesOmitidos();
                    adapter = new LogAdapter(this, data);
                }else{
                    adapter = new LogAdapter(this, data);
                }
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);
        }else{
            tv_message.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public List<PendienteSincronizacion> obtenerSincronizables() {
        try {
            RuntimeExceptionDao<PendienteSincronizacion, Integer> dao = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> qb = dao.queryBuilder();
            return qb.query();
        } catch (SQLException e) {
            Log.e("obtenerSincronizables", "error de exepcion ==>"+e);
        }
        return null;
    }

    public List<PendienteSincronizacion> obtenerSincronizablesPendientes() {
        try {
            RuntimeExceptionDao<PendienteSincronizacion, Integer> dao = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> qb = dao.queryBuilder();
            qb.where().eq(PendienteSincronizacion.NOMBRE_CAMPO_STATUS, 0).or().eq(PendienteSincronizacion.NOMBRE_CAMPO_STATUS, 1);
            return qb.query();
        } catch (SQLException e) {
            Log.e("obtenerSincronizables", "error de exepcion ==>"+e);
        }
        return null;
    }
    public List<PendienteSincronizacion> obtenerSincronizablesOmitidos() {
        try {
            RuntimeExceptionDao<PendienteSincronizacion, Integer> dao = getDbHelper().getPendienteSincronizacionRuntimeDAO();
            QueryBuilder<PendienteSincronizacion, Integer> qb = dao.queryBuilder();
            qb.where().eq(PendienteSincronizacion.NOMBRE_CAMPO_STATUS, 2);
            return qb.query();
        } catch (SQLException e) {
            Log.e("obtenerSincronizables", "error de exepcion ==>"+e);
        }
        return null;
    }


    @Override
    public void onBackPressed() {
        this.finish();
        //super.onBackPressed();
    }
}
