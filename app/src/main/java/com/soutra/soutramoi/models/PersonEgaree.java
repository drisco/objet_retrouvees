package com.soutra.soutramoi.models;

public class PersonEgaree {
    private String id;
    private String nomComplet;
    private String age;
    private String descriptionPhysique;
    private String photoUrl;
    private String lieuDisparition;
    private String dateDisparition;
    private String informationsContact;
    private String pnom_du_parent;

    public PersonEgaree() {
    }

    public PersonEgaree(String nomComplet, String age, String descriptionPhysique, String photoUrl, String lieuDisparition, String dateDisparition, String informationsContact,String pnom_du_parent) {
        this.nomComplet = nomComplet;
        this.age = age;
        this.descriptionPhysique = descriptionPhysique;
        this.photoUrl = photoUrl;
        this.lieuDisparition = lieuDisparition;
        this.dateDisparition = dateDisparition;
        this.informationsContact = informationsContact;
        this.pnom_du_parent = pnom_du_parent;
    }

    public String getId() {
        return id;
    }
    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescriptionPhysique() {
        return descriptionPhysique;
    }

    public void setDescriptionPhysique(String descriptionPhysique) {
        this.descriptionPhysique = descriptionPhysique;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLieuDisparition() {
        return lieuDisparition;
    }

    public void setLieuDisparition(String lieuDisparition) {
        this.lieuDisparition = lieuDisparition;
    }

    public String getDateDisparition() {
        return dateDisparition;
    }

    public void setDateDisparition(String dateDisparition) {
        this.dateDisparition = dateDisparition;
    }

    public String getInformationsContact() {
        return informationsContact;
    }

    public void setInformationsContact(String informationsContact) {
        this.informationsContact = informationsContact;
    }

    public String getPnom_du_parent() {
        return pnom_du_parent;
    }

    public void setPnom_du_parent(String pnom_du_parent) {
        this.pnom_du_parent = pnom_du_parent;
    }
}

