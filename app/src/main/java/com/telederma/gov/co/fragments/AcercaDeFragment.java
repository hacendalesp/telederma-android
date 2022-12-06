package com.telederma.gov.co.fragments;

import com.telederma.gov.co.R;
import com.telederma.gov.co.interfaces.IOpcionMenu;

/**
 * Created by Daniel Hernández on 6/6/2018.
 */

public class AcercaDeFragment extends BaseFragment implements IOpcionMenu {

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_acerca_de;
    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_acerca_de;
    }
}
