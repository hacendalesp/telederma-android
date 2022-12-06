package com.telederma.gov.co;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.telederma.gov.co.http.HttpUtils;
import com.telederma.gov.co.http.LoginService;
import com.telederma.gov.co.http.request.BaseRequest;
import com.telederma.gov.co.http.request.EditarUsuarioRequest;
import com.telederma.gov.co.http.request.TutorialRequest;
import com.telederma.gov.co.http.response.BaseResponse;
import com.telederma.gov.co.http.response.ResponseLogin;
import com.telederma.gov.co.http.response.ResponseUpdate;
import com.telederma.gov.co.modelo.Usuario;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.telederma.gov.co.utils.Constantes.DIRECTORIO_PERMANENTE_VIDEOS;
import static com.telederma.gov.co.utils.Constantes.FORMATO_DIRECTORIO_ARCHIVO;

public class IntroActivity extends BaseActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    private boolean videoVisto = false;
    private View viewSpace;

    private static final String VIDEO_TUTORIAL = String.format(FORMATO_DIRECTORIO_ARCHIVO, DIRECTORIO_PERMANENTE_VIDEOS, "tutorial.mp4");

    private VideoView videoview;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }

        setContentView(R.layout.activity_intro);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnNext = (Button) findViewById(R.id.btn_next);
        viewSpace = (View) findViewById(R.id.viewSpace);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.intro_slide_1,
                R.layout.intro_slide_2,
                R.layout.intro_slide_3
        };

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnNext.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });


    }

    private void prepararVideoTutorial() {

        Uri uri = Uri.fromFile(new File(VIDEO_TUTORIAL));

        videoview = findViewById(R.id.vdv_reproducir_video);
        mediaController = new MediaController(IntroActivity.this);
        mediaController.setMediaPlayer(videoview);
        videoview.setMediaController(mediaController);
        videoview.setVideoURI(uri);
        videoview.setOnPreparedListener(mp -> videoview.start());
        videoview.setOnCompletionListener(mp -> {
            videoVisto = true;
            btnNext.setEnabled(true);

            viewPager.setCurrentItem(2);
            viewPager.setCurrentItem(0);
            viewPager.setCurrentItem(2);

        });
        videoview.requestFocus();
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int colorActive = ContextCompat.getColor(this, R.color.white);
        int colorInactive = ContextCompat.getColor(this, R.color.colorPrimary);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorActive);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        mostrarMensajeEspera(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                actualizarUsuario();
            }
        });

        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    private void actualizarUsuario() {
        final DBUtil dbUtil = DBUtil.getInstance(getDbHelper(), IntroActivity.this);
        final Usuario usuario = dbUtil.obtenerUsuarioLogueado(
                Session.getInstance(IntroActivity.this).getCredentials().getIdUsuario()
        );
        usuario.setTutorial(true);
        dbUtil.crearUsuario(usuario);


        LoginService service = (LoginService) HttpUtils.crearServicio(LoginService.class);
        Observable<Response<BaseResponse>> usuarioObservable = service.marcarTutorial(
                new TutorialRequest(
                        Session.getInstance(IntroActivity.this).getCredentials().getToken(),
                        Session.getInstance(IntroActivity.this).getCredentials().getEmail()
                )
        );
        HttpUtils.configurarObservable(
                IntroActivity.this, usuarioObservable,
                this::procesarRespuestaTutorial,
                this::procesarExcepcionServicio
        );
    }


    private void procesarRespuestaTutorial(Response<BaseResponse> response) {
        String l = "";
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            dotsLayout.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            viewSpace.setVisibility(View.VISIBLE);


            if (videoview != null) {
                videoview.pause();
            }


            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.empezar));

                if (!videoVisto) {
                    viewPager.setCurrentItem(1);
                    btnNext.setText(getString(R.string.siguiente));
                }
            } else {
                btnNext.setText(getString(R.string.siguiente));
                btnNext.setEnabled(true);

                // Reproducir video
                if (position == 1) {
                    dotsLayout.setVisibility(View.GONE);
                    btnNext.setVisibility(View.GONE);
                    viewSpace.setVisibility(View.GONE);
                    prepararVideoTutorial();

                    if (!videoVisto) {
                        btnNext.setEnabled(false);
                    }
                }


            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
