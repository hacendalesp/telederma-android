package com.telederma.gov.co;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.telederma.gov.co.fragments.AcercaDeFragment;
import com.telederma.gov.co.fragments.AprendeFragment;
import com.telederma.gov.co.fragments.BaseFragment;
import com.telederma.gov.co.fragments.BusquedaPacienteFragment;
import com.telederma.gov.co.fragments.ConsultasFragment;
import com.telederma.gov.co.fragments.CreditosFragment;
import com.telederma.gov.co.fragments.GestionFragment;
import com.telederma.gov.co.fragments.MiPerfilFragment;
import com.telederma.gov.co.fragments.PatologiaFragment;
import com.telederma.gov.co.fragments.RegistrarRequerimientoFragment;
import com.telederma.gov.co.interfaces.IOpcionMenu;
//import com.telederma.gov.co.modules.GlideApp;
import com.telederma.gov.co.services.LogoutService;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Session;

import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.signature.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.telederma.gov.co.dialogs.VerTextoDialog;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.FileUtils;

import static com.telederma.gov.co.utils.Constantes.RAW_FILE_PREGUNTAS_FRECUENTES;


import static com.telederma.gov.co.utils.Utils.MSJ_ERROR;

/**
 * Actividad que muestra el menú y maneja la navegación entre las opciones del menú
 * De esta actividad deben heredar las actividades que requieran mostrar el menú en pantalla
 */
public abstract class MenuActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private FrameLayout viewContent;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_menu);

        viewContent = (FrameLayout) findViewById(R.id.menu_content);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = Session.getInstance(MenuActivity.this);

        final View headerView = navigationView.getHeaderView(0);
        final TextView headerText = headerView.findViewById(R.id.lbl_nombre_usuario);
        final ImageView headerImage = headerView.findViewById(R.id.img_imagen_usuario);

        headerText.setText(session.getCredentials().getNombreUsuario());

        Glide.with(MenuActivity.this)
                .load(session.get(Session.SESSION_IMAGEN_URL_USUARIO))
                .placeholder(R.drawable.img_perfil)
                .fitCenter()
                .into(headerImage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }

        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void setContentView(int layoutResID) {
        if (viewContent != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            View contentView = inflater.inflate(layoutResID, viewContent, false);
            viewContent.addView(contentView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (viewContent != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            viewContent.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (viewContent != null) {
            viewContent.addView(view, params);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Se actualiza la opción del menú seleccionada al presionar botón atrás
            super.onBackPressed();

            final FragmentManager fm = getSupportFragmentManager();
            final Fragment fragmentActual = fm.findFragmentById(R.id.menu_content);

            if (fm.getBackStackEntryCount() > 0 && fragmentActual != null && fragmentActual instanceof IOpcionMenu) {
                final int idItemMenuSeleccionado = ((IOpcionMenu) fragmentActual).getOpcionMenu();
                actualizarMenuItemSeleccionado(idItemMenuSeleccionado);
                findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * Se sobreescribe el método para que muestre el menú cuando se presione el botón Ver actividades recientes
     */
    @Override
    protected void onPause() {
        super.onPause();
        /*
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //TODO Corregir no abrir menú en cambio de activity
            navigationView.dispatchSetSelected(true);
            navigationView.setCheckedItem(selectedMenuItem);
            drawer.openDrawer(GravityCompat.START);
        }
        */
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Class fragment = null;

        final int idItemMenuSeleccionado = item.getItemId();
        if(idItemMenuSeleccionado != R.id.nav_db) //todo: Sebas--Remover esta linea temporal
            actualizarMenuItemSeleccionado(idItemMenuSeleccionado);

        switch (idItemMenuSeleccionado) {

            case R.id.nav_consultas:
                fragment = ConsultasFragment.class;
                break;

            case R.id.nav_nueva_consulta:
                fragment = BusquedaPacienteFragment.class;
                break;

            case R.id.nav_pacientes:
                fragment = BusquedaPacienteFragment.class;
                break;

            case R.id.nav_aprende:
                fragment = AprendeFragment.class;
                break;

            case R.id.nav_gestion:
                fragment = GestionFragment.class;
                break;

            case R.id.nav_mi_perfil:
                fragment = MiPerfilFragment.class;
                break;

            case R.id.nav_acerca_de:
                fragment = AcercaDeFragment.class;
                break;

            case R.id.nav_creditos:
                fragment = CreditosFragment.class;
                break;
            case R.id.nav_acerca_de_preguntas_frecuentes:
                new VerTextoDialog(MenuActivity.this, R.string.acerca_de_preguntas_frecuentes,
                        FileUtils.leerArchivoRaw(MenuActivity.this, RAW_FILE_PREGUNTAS_FRECUENTES)
                ).show();
                return true ;
            case R.id.nav_salir:
                //TODO Implementar lógica para cerrar sesión
                session.invalidate();
                startActivity(new Intent(MenuActivity.this, SplashActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

                return true;
            case R.id.nav_db:
                Intent dbmanager = new Intent(this,LogActivity.class);
                startActivity(dbmanager);
                //Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_cuerpo:
                fragment = PatologiaFragment.class;

                break;


            default:
                fragment = ConsultasFragment.class;//fragment = PatologiaFragment.class;
                break;
        }

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_content, ((BaseFragment) fragment.newInstance()))
                    .addToBackStack(Constantes.TAG_MENU_ACTIVITY_BACK_STACK)
                    .commit();
        } catch (ReflectiveOperationException e) {
            Log.e(Constantes.TAG_ERROR_MENU_ACTIVITY, "Error instanciando el fragment", e);
            mostrarMensaje(R.string.msj_error, MSJ_ERROR);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    protected void actualizarMenuItemSeleccionado(int idItemMenuSeleccionado) {
        navigationView.setCheckedItem(idItemMenuSeleccionado);
        setTitle(navigationView.getMenu().findItem(idItemMenuSeleccionado).getTitle());
    }


}
