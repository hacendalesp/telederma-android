package com.telederma.gov.co.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.telederma.gov.co.utils.Constantes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Observable;


public class SincronizarTask extends AsyncTask<Void, Void, String> {

    private byte[] image = null;

    private final Reference<Listener> refListener;

    public interface Listener {
        void onPostExecuted(String url);
    }

    public SincronizarTask(Object service, Observable observable, @NonNull Listener listener) {

        this.refListener = new WeakReference<>(listener);
    }


    @Override
    protected String doInBackground(Void... voids) {


        if (this.image != null) {

            File path = new File(Constantes.DIRECTORIO_TEMPORAL_HISTORIA);
            if (!path.exists()) {
                path.mkdirs();
            }
            String name = "temporal" + System.currentTimeMillis() + ".png";
            return saveImage(this.image, path.getPath(), name);
        }
        return "";
    }

    @Override
    final protected void onPostExecute(String url) {
        final Listener listener = this.refListener.get();
        if (listener != null) {
            listener.onPostExecuted(url);

        }
    }

    private String saveImage(byte[] thumbnail, String path, String name) {
        String respuesta = "";
        try {
            if (thumbnail != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(thumbnail.length);
                bytes.write(thumbnail, 0, thumbnail.length);
                File destination = new File(path + "/" + name);
                FileOutputStream fo;
                respuesta = destination.getPath();
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            }
        } catch (IOException e) {
            String l = "";
        }
        return respuesta;
    }

}