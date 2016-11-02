package com.lionsquare.canisovismanager.Beans;

/**
 * Created by archivaldo on 04/01/16.
 */
public class Titular {
    private String titulo;
    private String subtitulo;

    public Titular(String tit, String sub){
        titulo = tit;
        subtitulo = sub;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getSubtitulo(){
        return subtitulo;
    }
}
