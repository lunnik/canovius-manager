package com.lionsquare.canisovismanager.Beans;

/**
 * Created by archivaldo on 29/11/15.
 */
public class Lista_Dispositivo {

    private int idImagen;
    private String divice;
    private String lat;
    private String lon;
    private String direct;
    private String numList;
    private String emei;
    private String fecha;


    private String des;

    // se crean los objetos de la infromacion que no llegara de la clase principal
    //handeler o manejadores de informacion
    public Lista_Dispositivo(int idImagen, String divice, String lat, String lon, String dirct, String num_list, String emei, String fecha) {
        this.idImagen = idImagen;
        this.divice = divice;
        this.lat = lat;
        this.lon = lon;
        this.direct = dirct;
        this.numList = num_list;
        this.emei = emei;
        this.fecha = fecha;


    }


    public String getNumList() {
        return numList;
    }

    public String getEmei() {
        return emei;
    }

    public String getLat() {
        return lat;
    }


    public String getDivice() {
        return divice;
    }

    public String getDirect() {
        return direct;
    }

    public String getLon() {
        return lon;
    }

    public int get_idImagen() {
        return idImagen;
    }

    public String getFecha() {
        return fecha;
    }


}
