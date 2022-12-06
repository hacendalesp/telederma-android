package com.telederma.gov.co.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.telederma.gov.co.R;
import com.telederma.gov.co.dialogs.VerTextoDialog;
import com.telederma.gov.co.interfaces.IOpcionMenu;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class GestionFragment extends BaseFragment implements IOpcionMenu, View.OnClickListener {

    private Button btnEditarPerfil, btnMesaAyuda, btnCreditos, btnAcercaDe, btnAvisoLegal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        btnEditarPerfil = rootView.findViewById(R.id.btn_editar_perfil);
        btnMesaAyuda = rootView.findViewById(R.id.btn_mesa_ayuda);
        btnCreditos = rootView.findViewById(R.id.btn_creditos);
        btnAcercaDe = rootView.findViewById(R.id.btn_acerca_de);
        btnAvisoLegal = rootView.findViewById(R.id.btn_aviso_legal);

        btnEditarPerfil.setOnClickListener(this);
        btnMesaAyuda.setOnClickListener(this);
        btnCreditos.setOnClickListener(this);
        btnAcercaDe.setOnClickListener(this);
        btnAvisoLegal.setOnClickListener(this);

        return rootView;
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_gestion;
    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_gestion;
    }

    @Override
    public void onClick(View view) {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        switch (view.getId()) {
            case R.id.btn_editar_perfil:
                ft.replace(R.id.menu_content, new MiPerfilFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();

                break;
            case R.id.btn_mesa_ayuda:
                ft.replace(R.id.menu_content, new MesaAyudaFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();

                break;
            case R.id.btn_creditos:
                ft.replace(R.id.menu_content, new GestionCreditosFragment()).addToBackStack(
                        Constantes.TAG_MENU_ACTIVITY_BACK_STACK
                ).commit();

                break;
            case R.id.btn_acerca_de:
                String acerca_de_telederma = contexto.getString(R.string.acerca_de_telederma) + "<br/><p style='text-align: center;'>"+contexto.getString(R.string.version_app)+"</p>";
                //new VerTextoDialog(contexto, R.string.action_acerca_de, getString(R.string.acerca_de_telederma)).show();
                new VerTextoDialog(contexto, R.string.action_acerca_de, acerca_de_telederma).show();

                break;
            case R.id.btn_aviso_legal:
                final String tcRaw = FileUtils.leerArchivoRaw(contexto, Constantes.RAW_FILE_TERMINOS_Y_CONDICIONES);
                new VerTextoDialog(contexto, R.string.dialog_terminos_condiciones_title, tcRaw).show();

                break;
        }
    }
}
