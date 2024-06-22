package com.soutra.soutramoi.models;

import java.util.List;

public class Egaree {
    String id;
    String titre;
    String descr;
    String nom;
    String numero;
    String ville;
    String date;
    private List<String> imageUris;

    public Egaree() {
    }

    public Egaree(String id, String titre, String descr, String nom, String numero, String ville, String date, List<String> imageUris) {
        this.id = id;
        this.titre = titre;
        this.descr = descr;
        this.nom = nom;
        this.numero = numero;
        this.ville = ville;
        this.date = date;
        this.imageUris = imageUris;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<String> imageUris) {
        this.imageUris = imageUris;
    }
}
