package com.telederma.gov.co.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.StyleableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.interfaces.IAudioView;
import com.telederma.gov.co.tasks.DescargarArchivoTask;
import com.telederma.gov.co.tasks.ParamDownload;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Daniel Hernández on 19/07/2018.
 */

public class ReproductorAudioView extends RelativeLayout implements IAudioView, SeekBar.OnSeekBarChangeListener {

    private static final String TAG_REPRODUCTOR_AUDIO_VIEW = "TAG_REPRODUCTOR_AUDIO_VIEW";

    private Button btnPlay, btnRemove;
    private SeekBar sbrPlay;
    private TextView lblPlayTime;
    private LinearLayout layDescarga;
    private TextView lblDescarga;
    private MediaPlayer mediaPlayer;
    public ArchivoDescarga audioFile;

    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask;

    @StyleableRes
    private final int index0 = 0;

    private boolean showRemoveButton;


    public ReproductorAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ReproductorAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReproductorAudioView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.reproductor_audio_view, this);
        mediaPlayer = new MediaPlayer();

        int[] sets = {R.attr.showRemoveButton};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);
        showRemoveButton = typedArray.getBoolean(index0, false);
        typedArray.recycle();

        initComponents();
        disable();
    }

    private void initComponents() {
        btnPlay = findViewById(R.id.btn_play);
        btnRemove = findViewById(R.id.btn_remove);
        sbrPlay = findViewById(R.id.sbr_play);
        lblPlayTime = findViewById(R.id.lbl_play_tiempo);
        lblDescarga = findViewById(R.id.lbl_descarga);
        layDescarga = findViewById(R.id.lay_descarga);

        btnPlay.setOnClickListener(view -> onPlayClick());
        sbrPlay.setOnSeekBarChangeListener(this);

        if (showRemoveButton) {
            btnRemove.setVisibility(VISIBLE);
            setOnRemoveClickListener(v -> remove());
        }
    }

    private void onPlayClick() {
        if (!mediaPlayer.isPlaying())
            play();
        else
            pause();
    }

    private void descargarAudios() {
        new DescargarArchivoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                new ParamDownload(this, this.audioFile)
        );
    }

    @Override
    public void enable() {
        btnPlay.setEnabled(true);
        btnPlay.setBackgroundTintList(getContext().getColorStateList(R.color.blue));
        lblPlayTime.setTextColor(getContext().getColorStateList(R.color.blue));
        sbrPlay.setEnabled(true);

        if (showRemoveButton) {
            btnRemove.setEnabled(true);
            btnRemove.setBackgroundResource(R.drawable.round_button_red);
        }
    }

    @Override
    public void disable() {
        pause();
        btnPlay.setEnabled(false);
        btnPlay.setBackgroundTintList(getContext().getColorStateList(R.color.grayDark));
        lblPlayTime.setTextColor(getContext().getColorStateList(R.color.grayDark));
        sbrPlay.setEnabled(false);

        if (showRemoveButton) {
            btnRemove.setEnabled(false);
            btnRemove.setBackgroundResource(R.drawable.round_button_gray);
        }
    }

    @Override
    public void play() {
        btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_circle_outline, getContext().getTheme()));
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();

        btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle_outline, getContext().getTheme()));
    }

    @Override
    public void onFinishDownload(Integer key, Boolean fileDownloaded) {
        if (fileDownloaded) {
            try (FileInputStream fis = new FileInputStream(audioFile.getRutaAbsoluta())) {
                mediaPlayer.setOnCompletionListener(mp -> {
                    pause();
                });
                mediaPlayer.reset(); // Sebas P. agregado
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(fis.getFD());
                mediaPlayer.prepare();

                initSeekBarTimer(true);
            } catch (FileNotFoundException e) {
                Log.d(TAG_REPRODUCTOR_AUDIO_VIEW,
                        String.format("Ocurrió un error cargando el audio \n%s", audioFile.getRutaAbsoluta()), e
                );
            } catch (IOException e) {
                Log.d(TAG_REPRODUCTOR_AUDIO_VIEW,
                        String.format("Ocurrió un error cargando el audio \n%s", audioFile.getRutaAbsoluta()), e
                );
            }
            enable();
            layDescarga.setVisibility(GONE);
        } else {
            disable();
            lblDescarga.setText(R.string.reproductor_audio_descarga_fallida);
            Log.d(TAG_REPRODUCTOR_AUDIO_VIEW, String.format("No se terminó la desarga del audio \n%s", audioFile));
        }
    }

    private void initSeekBarTimer(boolean fileLoaded) {
        if (!fileLoaded) {
            lblPlayTime.setText(Utils.getInstance(getContext()).millisecondsToMMSS(0));

            // Updating progress bar
            sbrPlay.setMax(0);
            sbrPlay.setProgress(0);

            return;
        }

        final int totalDuration = mediaPlayer.getDuration();
        sbrPlay.setMax(totalDuration);

        //Background Runnable thread
        mUpdateTimeTask = new Runnable() {
            public void run() {
                final int currentDuration = mediaPlayer.getCurrentPosition();
                // Displaying Total Duration time
                lblPlayTime.setText(Utils.getInstance(getContext()).millisecondsToMMSS(currentDuration));
                // Updating progress bar
                sbrPlay.setProgress(currentDuration);
                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        };
        updateProgressBar();
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mediaPlayer.seekTo(seekBar.getProgress());
        updateProgressBar();
    }

    @Override
    public void updateDownloadProgress(Integer key, Integer progress) {
        lblDescarga.setText(String.valueOf(progress));
    }

    public void load(ArchivoDescarga audioFile) {
        pause();
        disable();

        if (this.mediaPlayer.getDuration() > 0) {
            this.mediaPlayer.release();
            this.mediaPlayer = new MediaPlayer();
            initSeekBarTimer(false);
        }

        this.audioFile = audioFile;
        descargarAudios();
    }

    public void remove() {
        disable();
        mHandler.removeCallbacks(mUpdateTimeTask);
        final File file = new File(audioFile.getRutaAbsoluta());

        if (file.exists()) {
            file.delete();
        }

        setVisibility(GONE);
    }

    public void setOnRemoveClickListener(OnClickListener onRemoveClickListener) {
        btnRemove.setOnClickListener(view -> {
            remove();

            if (onRemoveClickListener != null)
                onRemoveClickListener.onClick(view);
        });
    }

}
