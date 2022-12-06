package com.telederma.gov.co.utils;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public class ArchivoDescarga {

    private Integer key;
    public String url;
    public String directorio;
    public String nombre;

    private boolean descargado;

    public ArchivoDescarga(String url, String directorio, String nombre) {
        this.url = url;
        this.directorio = directorio;
        this.nombre = nombre;
    }

    public String getRutaAbsoluta() {
        return String.format(Constantes.FORMATO_DIRECTORIO_ARCHIVO, directorio, nombre);
    }

    public boolean isDescargado() {
        this.descargado = FileUtils.existeArchivoLocal(getRutaAbsoluta());

        return descargado;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
