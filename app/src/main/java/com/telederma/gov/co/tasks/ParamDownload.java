package com.telederma.gov.co.tasks;

import com.telederma.gov.co.interfaces.IDownloadView;
import com.telederma.gov.co.utils.ArchivoDescarga;

/**
 * Created by Daniel Hernández on 7/08/2018.
 */

public class ParamDownload {

    private IDownloadView downloadView;
    private ArchivoDescarga archivoDescarga;

    public ParamDownload(ArchivoDescarga archivoDescarga) {
        this.archivoDescarga = archivoDescarga;
    }

    public ParamDownload(IDownloadView downloadView, ArchivoDescarga archivoDescarga) {
        this.downloadView = downloadView;
        this.archivoDescarga = archivoDescarga;
    }

    public IDownloadView getDownloadView() {
        return downloadView;
    }

    public void setDownloadView(IDownloadView downloadView) {
        this.downloadView = downloadView;
    }

    public ArchivoDescarga getArchivoDescarga() {
        return archivoDescarga;
    }

    public void setArchivoDescarga(ArchivoDescarga archivoDescarga) {
        this.archivoDescarga = archivoDescarga;
    }
}
