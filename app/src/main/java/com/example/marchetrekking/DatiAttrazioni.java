package com.example.marchetrekking;
import java.io.Serializable;


public class DatiAttrazioni implements Serializable{

    private int id;
    private String nomeattrazione;
    private String descrizione;
    private String immagine;
    private String nomePercorso;

    /*public DatiAttrazioni(String nomeattrazione ,String descrizione, String immagine ){
        this.nomeattrazione = nomeattrazione;
        this.descrizione = descrizione;
        this.immagine = immagine;
    }*/




    public DatiAttrazioni(int id, String nomeattrazione ,String descrizione, String immagine, String nomePercorso ){
        this.id = id;
        this.nomeattrazione = nomeattrazione;
        this.descrizione = descrizione;
        this.immagine = immagine;
        this.nomePercorso = nomePercorso;
    }

    public int getId() {
        return id;
    }

    public String getNomeattrazione() {
        return nomeattrazione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getImmagine() {
        return immagine;
    }

    public String getNomePercorso() {
        return nomePercorso;
    }
}
