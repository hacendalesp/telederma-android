package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telederma.gov.co.R;


public class HistoriaFragment extends BaseFragment {
    Bundle arguments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_historia);

        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

        RegistrarPacienteFragment fragment = new RegistrarPacienteFragment();
        fragment.setArguments(getArguments());
        transaction.replace(R.id.paso1, fragment).commit();


        arguments = getArguments();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hola", "hola");
            }
        });
        return rootView;
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_historia;
    }
}