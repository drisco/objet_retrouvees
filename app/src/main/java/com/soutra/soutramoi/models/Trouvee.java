package com.soutra.soutramoi.models;

public class Trouvee {
    String id;
    String nom;
    String photo;
    String ville;
    String numero;
    String descr;
    String titre;
    String date;

    public Trouvee() {
    }

    public Trouvee(String id, String nom, String photo, String ville, String numero, String descr, String titre, String date) {
        this.id = id;
        this.nom = nom;
        this.photo = photo;
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
