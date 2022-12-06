package com.telederma.gov.co.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.telederma.gov.co.utils.Constantes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.UUID;


public class SavePhotoTask extends AsyncTask<Void, Void, String> {

    private byte[] image = null;

    private final Reference<Listener> refListener;

    public interface Listener {
        void onPostExecuted(String url);
    }

    public SavePhotoTask(byte[] bitmap, @NonNull Listener listener) {
        this.image = bitmap;
        this.refListener = new WeakReference<>(listener);
    }


    @Override
    protected String doInBackground(Void... voids) {
        if (this.image != null) {

            try {
                Thread.currentThread().sleep(100);
                File path = new File(Constantes.DIRECTORIO_TEMPORAL_HISTORIA);
                if (!path.exists()) {
                    path.mkdirs();
                }
                String name = "temporal" + System.currentTimeMillis() + UUID.randomUUID().toString() + ".png";

                return saveImage(this.image, path.getPath(), name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

    synchronized private String saveImage(byte[] thumbnail, String path, String name) {
        String respuesta = "";
        if (thumbnail != null) {
            File photo=new File(path + "/" + name);
            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());
                respuesta = photo.getPath();
                fos.write(thumbnail);
                fos.close();
            }
            catch (java.io.IOException e) {

            }
        }
        return respuesta;
    }

}