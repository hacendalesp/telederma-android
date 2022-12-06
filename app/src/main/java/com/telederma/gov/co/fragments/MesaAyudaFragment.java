package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telederma.gov.co.R;
import com.telederma.gov.co.dialogs.VerTextoDialog;
import com.telederma.gov.co.modelo.Paciente;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;

import static com.telederma.gov.co.utils.Constantes.RAW_FILE_PREGUNTAS_FRECUENTES;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class MesaAyudaFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        inicializarViews(rootView);

        return rootView;
    }

    private void inicializarViews(View rootView) {
        rootView.findViewById(R.id.btn_historial_tickets).setOnClickListener(v -> {
            new MesaAyudaVerTickets();


            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = new MesaAyudaVerTickets();
            //fragment.setArguments(args);
            ft.replace(R.id.menu_content, fragment).addToBackStack(
                    Constantes.TAG_MENU_ACTIVITY_BACK_STACK
            ).commit();
        });

        rootView.findViewById(R.id.btn_enviar).setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(
                    R.id.menu_content, new MesaAyudaCrearTicketFragment()).addToBackStack(
                    Constantes.TAG_LOGIN_ACTIVITY_BACK_STACK
            ).commit();
        });
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_mesa_ayuda;
    }

}
