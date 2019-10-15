package com.example.marchetrekking;

import java.io.Serializable;

public class DatiPercorsi implements Serializable {

    private int id;
    private String nome;
    private String mappa;
    private String descrizione;
    private double lunghezza;
    private int livello;
    private String durata;
    private String immagine;

    public DatiPercorsi(String nome, String mappa, String descrizione, double lunghezza, int livello, String durata, String immagine){
        this.nome=nome;
        this.mappa=mappa;
        this.descrizione=descrizione;
        this.lunghezza=lunghezza;
        this.livello=livello;
        this.durata=durata;
        this.immagine=immagine;
    }

    public DatiPercorsi(int id, String nome, String mappa, String descrizione, double lunghezza, int livello, String durata, String immagine){
        this.id = id;
        this.nome=nome;
        this.mappa=mappa;
        this.descrizione=descrizione;
        this.lunghezza=lunghezza;
        this.livello=livello;
        this.durata=durata;
        this.immagine=immagine;
    }

    public int getId(){return id;}

    public DatiPercorsi(String nome){
        this.nome = nome;
    }


    public String getNome() {
        return nome;
    }

    public String getMappa() {
        return mappa;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public double getLunghezza() {
        return lunghezza;
    }

    public int getLivello() {
        return livello;
    }

    public String getDurata() {
        return durata;
    }

    public String getImmagine() {
        return immagine;
    }


}
