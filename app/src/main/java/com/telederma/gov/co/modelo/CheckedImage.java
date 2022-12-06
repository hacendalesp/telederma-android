package com.telederma.gov.co.modelo;

public class CheckedImage {
    private boolean mChecked = false;
    private String url_image;

    public boolean getChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }


    public CheckedImage(boolean checked, String url) {
        mChecked = checked;
        url_image = url;
    }

    public String getUrl() {
        return url_image;
    }

}
