package com.telederma.gov.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.Cie10;

import java.util.List;

public class AutocompleteAdapterCie10 extends ArrayAdapter {

    private List<Cie10> dataList;
    private Context mContext;
    private int itemLayout;
    private Filter listFilter;
    private String tag = "AutocompleteAdapterCie10";

    public AutocompleteAdapterCie10(Filter filter, Context context, int resource, List<Cie10> dataLst) {
        super(context, resource, dataLst);
        dataList = dataLst;
        mContext = context;
        itemLayout = resource;
        listFilter = filter;
    }

    public void setDataList(List<Cie10> dataLst) {
        dataList = dataLst;
    }

    @Override
    public int getCount() {
        return ((dataList == null) ? 0 : dataList.size());

    }

    @Override
    public Cie10 getItem(int position) {
        return dataList.get(position);
    }


    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.autocomplete_asegurador);
        strName.setText(String.format("- %s", getItem(position).getName())+ " "+ String.format("- %s", getItem(position).getCode()));
        Log.i(tag, "cie10_selected from adapter =>"+strName.getText().toString());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }


}
