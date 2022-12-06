package com.telederma.gov.co;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by Daniel Hernández on 11/08/2018.
 */

public class ReproducirVideoActivity extends BaseActivity {

    public static final String EXTRA_RUTA_VIDEO = "EXTRA_RUTA_VIDEO";
    public static final String ACTION_REPRODUCIR_VIDEO = "com.telederma.gov.co.ReproducirVideoActivity";

    private VideoView videoview;
    private MediaController mediaController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproducir_video);

        if(getIntent().hasExtra("orientation")){
            String orientation = getIntent().getStringExtra("orientation");
            if(orientation.equals("portrait")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        Uri uri = Uri.fromFile(new File(getIntent().getStringExtra(EXTRA_RUTA_VIDEO)));

        videoview = findViewById(R.id.vdv_reproducir_video);

        mediaController = new MediaController(ReproducirVideoActivity.this);
        mediaController.setMediaPlayer(videoview);
        videoview.setMediaController(mediaController);
        videoview.setVideoURI(uri);
        videoview.setOnPreparedListener(mp -> videoview.start());
        videoview.setOnCompletionListener(mp -> {
            setResult(RESULT_OK, new Intent());
            finish();
        });
        videoview.requestFocus();



    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
