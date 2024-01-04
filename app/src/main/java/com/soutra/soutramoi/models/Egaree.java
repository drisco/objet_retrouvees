package com.soutra.soutramoi.models;

public class Egaree {
    String id;
    String titre;
    String descr;
    String nom;
    String numero;
    String ville;
    String date;

    String photo;

    public Egaree() {
    }

    public Egaree(String id, String titre, String descr, String nom, String numero, String ville, String date, String photo) {
        this.id = id;
        this.titre = titre;
        this.descr = descr;
        this.nom = nom;
        this.numero = numero;
        this.ville = ville;
        this.date = date;
        this.photo = photo;
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

    public String getnom() {
        return nom;
    }

    public void setnom(String nom) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
