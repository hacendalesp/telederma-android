package com.telederma.gov.co.http.response;

import com.google.gson.annotations.Expose;

/**
 * Created by Daniel Hernández on 15/08/2018.
 */

public class ResponseImage {

    @Expose
    public boolean success;
    @Expose
    public int status;
    @Expose
    public UploadedImage data;

    public static class UploadedImage {
        @Expose
        public String id;
        @Expose
        public String title;
        @Expose
        public String description;
        @Expose
        public String type;
        @Expose
        public boolean animated;
        @Expose
        public int width;
        @Expose
        public int height;
        @Expose
        public int size;
        @Expose
        public int views;
        @Expose
        public int bandwidth;
        @Expose
        public String vote;
        @Expose
        public boolean favorite;
        @Expose
        public String account_url;
        @Expose
        public String deletehash;
        @Expose
        public String name;
        @Expose
        public String link;

        @Override
        public String toString() {
            return "UploadedImage{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", type='" + type + '\'' +
                    ", animated=" + animated +
                    ", width=" + width +
                    ", height=" + height +
                    ", size=" + size +
                    ", views=" + views +
                    ", bandwidth=" + bandwidth +
                    ", vote='" + vote + '\'' +
                    ", favorite=" + favorite +
                    ", account_url='" + account_url + '\'' +
                    ", deletehash='" + deletehash + '\'' +
                    ", name='" + name + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ImageResponse{" +
                "success=" + success +
                ", status=" + status +
                ", data=" + data.toString() +
                '}';
    }

}
