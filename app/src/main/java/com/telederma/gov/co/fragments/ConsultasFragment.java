package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.telederma.gov.co.R;
import com.telederma.gov.co.interfaces.IOpcionMenu;
import com.telederma.gov.co.utils.Constantes;

/**
 * Created by Daniel Hernández on 6/28/2018.
 */

public class ConsultasFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, IOpcionMenu {

    private TabLayout tabsConsultas;
    private Button btnNuevoPaciente;
    private LinearLayout layHeader;
    //private LinearLayout layHeaderCompartir;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_consultas;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        layHeader = view.findViewById(R.id.layout_header);
        //layHeaderCompartir = view.findViewById(R.id.layout_header_compartir);

        btnNuevoPaciente = (Button) view.findViewById(R.id.btn_nuevo_paciente);
        btnNuevoPaciente.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_content, new BusquedaPacienteFragment())
                    .addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK)
                    .commit();
        });

        tabsConsultas = (TabLayout) view.findViewById(R.id.tabs_consultas);
        tabsConsultas.addTab(tabsConsultas.newTab().setText(R.string.lista_consultas_tab_resueltas_title));
        tabsConsultas.addTab(tabsConsultas.newTab().setText(R.string.lista_consultas_tab_pendientes_title));
        tabsConsultas.addTab(tabsConsultas.newTab().setText(R.string.lista_consultas_tab_archivadas_title));

        replaceFragment(ConsultasContentFragment.TAB_RESUELTAS);

        tabsConsultas.addOnTabSelectedListener(this);

        return view;
    }

    private void replaceFragment(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new ConsultasContentFragment();

        final Bundle args = new Bundle();
        args.putInt(ConsultasContentFragment.ARG_TAB_SELECTED, position);
        fragment.setArguments(args);

        transaction.replace(R.id.tabs_consultas_content, fragment);

        transaction.commit();

        layHeader.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f
        ));
        /*if (position == ConsultasContentFragment.TAB_RESUELTAS) {
            layHeader.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f
            ));
            //layHeaderCompartir.setVisibility(View.VISIBLE);
        } else {
            layHeader.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f
            ));
            //layHeaderCompartir.setVisibility(View.GONE);
        }*/

        if (position == ConsultasContentFragment.TAB_ARCHIVADAS) {
            layHeader.setVisibility(View.GONE);
        } else {
            layHeader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        replaceFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_consultas;
    }
}
