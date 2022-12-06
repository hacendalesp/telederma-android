package com.telederma.gov.co.interfaces;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public interface IDownloadView {

    void updateDownloadProgress(Integer key, Integer progress);

    void onFinishDownload(Integer key, Boolean finished);

}
