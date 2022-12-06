package com.telederma.gov.co.patologia;

public class LocationZone {
    private int id;
    private float x;
    private float y;
    private int id_iamgen_derecho;
    private int id_iamgen_izquierdo;

    public LocationZone(int id, float x, float y, int id_iamgen_derecho, int id_iamgen_izquierdo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.id_iamgen_derecho = id_iamgen_derecho;
        this.id_iamgen_izquierdo = id_iamgen_izquierdo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId_iamgen_derecho() {
        return id_iamgen_derecho;
    }

    public void setId_iamgen_derecho(int id_iamgen_derecho) {
        this.id_iamgen_derecho = id_iamgen_derecho;
    }

    public int getId_iamgen_izquierdo() {
        return id_iamgen_izquierdo;
    }

    public void setId_iamgen_izquierdo(int id_iamgen_izquierdo) {
        this.id_iamgen_izquierdo = id_iamgen_izquierdo;
    }
}
