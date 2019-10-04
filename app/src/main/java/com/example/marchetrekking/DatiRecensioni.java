package com.example.marchetrekking;

import java.io.Serializable;
public class DatiRecensioni implements Serializable{
    private String nomepercorso;
    private String descrizione;
    private String utente;

    public DatiRecensioni(String nomepercorso, String descrizione, String utente){
        this.nomepercorso = nomepercorso;
        this.descrizione = descrizione;
        this.utente = utente;
    }

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
