package com.telederma.gov.co;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.telederma.gov.co.fragments.IniciarSesionFragment;
import com.telederma.gov.co.utils.Constantes;

/**
 * Actividad que maneja la navegación entre los fragments para
 * iniciar sesión, recuperar contraseña y registrar nuevo usuario
 */
public class LoginActivity extends BaseActivity {
//public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //cambia el color del statusBar
//        Window window = this.getWindow();
//        //clear flag_translucent_status_flag
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //add flag_draws_system_bar_backgrounds flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //finally change the color
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));



        // Se inicia el fragment de inicio de sesión por defecto
        final Fragment fragment = new IniciarSesionFragment();

        if (getIntent().hasExtra(IniciarSesionFragment.ARG_DOCUMENTO)
                && getIntent().hasExtra(IniciarSesionFragment.ARG_CONTRASENA)) {
            final Bundle args = new Bundle();
            args.putString(
                    IniciarSesionFragment.ARG_DOCUMENTO,
                    getIntent().getStringExtra(IniciarSesionFragment.ARG_DOCUMENTO)
            );
            args.putString(
                    IniciarSesionFragment.ARG_CONTRASENA,
                    getIntent().getStringExtra(IniciarSesionFragment.ARG_CONTRASENA)
            );
            fragment.setArguments(args);
        }

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.login_content, fragment).addToBackStack(
                Constantes.TAG_LOGIN_ACTIVITY_BACK_STACK
        ).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        FullScreencall();
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


}
