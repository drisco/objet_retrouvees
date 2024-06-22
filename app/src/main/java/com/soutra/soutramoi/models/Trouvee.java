package com.soutra.soutramoi.models;

import java.util.List;

public class Trouvee {
    String id;
    String nom;
    private List<String> imageUris;
    String ville;
    String numero;
    String descr;
    String titre;
    String date;

    public Trouvee() {
    }

    public Trouvee(String id, String nom, List<String> imageUris, String ville, String numero, String descr, String titre, String date) {
        this.id = id;
        this.nom = nom;
        this.imageUris = imageUris;
        this.ville = ville;
        this.numero = numero;
        this.descr = descr;
        this.titre = titre;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<String> imageUris) {
        this.imageUris = imageUris;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
