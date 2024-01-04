package com.soutra.soutramoi.models;

public class PersonTrouvee {
    private String titre;
    private String description;
    private String photo;
    private String ville;
    private String nom;
    private String numero;
    private String date;

    public PersonTrouvee() {
    }

    public PersonTrouvee(String titre, String description, String photo, String ville, String nom,String numero, String date) {
        this.titre = titre;
        this.description = description;
        this.photo = photo;
        this.ville = ville;
        this.nom = nom;
        this.numero = numero;
        this.date = date;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getDate() {
        return date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

