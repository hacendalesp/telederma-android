package com.telederma.gov.co.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.telederma.gov.co.interfaces.IDownloadView;
import com.telederma.gov.co.utils.ArchivoDescarga;
import com.telederma.gov.co.utils.FileUtils;

import static com.telederma.gov.co.utils.Constantes.TAG_DESCARGAR_ARCHIVO;

/**
 * Created by Daniel Hernández on 18/07/2018.
 */

public class DescargarArchivoTask extends AsyncTask<ParamDownload, Integer, Boolean> {

    private IDownloadView downloadView;
    private ArchivoDescarga archivoDescarga;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(ParamDownload... params) {
        downloadView = params[0].getDownloadView();
        archivoDescarga = params[0].getArchivoDescarga();

        // params[0] => HTTP URL, params[1] => Local storage dir, params[2] => File name
        Log.d(TAG_DESCARGAR_ARCHIVO, String.format(
                "Descargando archivo: %s a %s", archivoDescarga.url,
                archivoDescarga.getRutaAbsoluta()
        ));

        if (archivoDescarga.isDescargado()) {
            Log.d(TAG_DESCARGAR_ARCHIVO, String.format(
                    "No se descarga porque archivo ya existe: %s", archivoDescarga.getRutaAbsoluta()
            ));
            publishProgress(100);

            return true;
        } else {
            try {
                FileUtils.descargarArchivo(archivoDescarga, this::publishProgress);
            } catch (Exception e) {
                Log.d(TAG_DESCARGAR_ARCHIVO, String.format("Error descargando archivo: %s a %s",
                        archivoDescarga.url, archivoDescarga.getRutaAbsoluta()), e
                );

                return false;
            }

            return true;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Log.d(TAG_DESCARGAR_ARCHIVO, String.format("Descargando archivo %s... %d%%",
                archivoDescarga.getRutaAbsoluta(), progress[0])
        );
        downloadView.updateDownloadProgress(archivoDescarga.getKey(), progress[0]);
    }

    @Override
    protected void onPostExecute(Boolean finishedDownload) {
        Log.d(TAG_DESCARGAR_ARCHIVO, String.format("Archivo %s descargado... %b",
                archivoDescarga.getRutaAbsoluta(), finishedDownload)
        );
        downloadView.onFinishDownload(archivoDescarga.getKey(), finishedDownload);
    }
}