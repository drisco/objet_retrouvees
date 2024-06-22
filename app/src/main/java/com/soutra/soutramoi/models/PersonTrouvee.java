package com.soutra.soutramoi.models;

import java.util.List;

public class PersonTrouvee {
    private String titre;
    private String description;
    private List<String> imageUris;
    private String ville;
    private String nom;
    private String numero;
    private String date;

    public PersonTrouvee(String titre, String description, List<String> imageUris, String ville, String nom, String numero, String date) {
        this.titre = titre;
        this.description = description;
        this.imageUris = imageUris;
        this.ville = ville;
        this.nom = nom;
        this.numero = numero;
        this.date = date;
    }

    public PersonTrouvee() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

