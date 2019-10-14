package com.example.marchetrekking;

import java.io.Serializable;
public class DatiRecensioni implements Serializable{
    private int id;
    private String nomepercorso;
    private String descrizione;
    private String utente;

    public DatiRecensioni(int id, String nomepercorso, String descrizione, String utente){
        this.id = id;
        this.nomepercorso = nomepercorso;
        this.descrizione = descrizione;
        this.utente = utente;
    }

    public int getId(){return id;}

    public String getNomepercorso() {
        return nomepercorso;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getUtente() {
        return utente;
    }


}
