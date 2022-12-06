package com.telederma.gov.co.fragments;

import com.telederma.gov.co.R;
import com.telederma.gov.co.interfaces.IOpcionMenu;

/**
 * Created by Sebastian noreña
 */

public class CreditosFragment extends BaseFragment implements IOpcionMenu {

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_creditos;
    }

    @Override
    public int getOpcionMenu() {
        return R.id.nav_creditos;
    }


}
