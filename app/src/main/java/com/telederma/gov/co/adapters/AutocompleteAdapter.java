package com.telederma.gov.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.Aseguradora;

import java.util.List;

public class AutocompleteAdapter extends ArrayAdapter {

    private List<Aseguradora> dataList;
    private Context mContext;
    private int itemLayout;
    private Filter listFilter;

    public AutocompleteAdapter(Filter filter, Context context, int resource, List<Aseguradora> dataLst) {
        super(context, resource, dataLst);
        dataList = dataLst;
        mContext = context;
        itemLayout = resource;
        listFilter = filter;
    }

    public void setDataList(List<Aseguradora> dataLst) {
        dataList = dataLst;
    }

    @Override
    public int getCount() {
        return dataList.size();

    }

    @Override
    public Aseguradora getItem(int position) {
        return dataList.get(position);
    }


    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.autocomplete_asegurador);
        strName.setText(String.format("- %s", getItem(position).getName()));

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }


}
