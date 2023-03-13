package com.app.engineerapp.Models;

import java.util.Date;

public class Technicien extends Personne{
    private int is_verified;
    private String responsableName;

    public Technicien() {
    }



    public Technicien(String cni, String nom, String prenom, Genre genre, String tele, Date date_naiss, Date date_embau, String email,String responsableName) {
        super(cni, nom, prenom, genre, tele, date_naiss, date_embau,email);
        this.responsableName=responsableName;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public String getResponsableName() {
        return responsableName;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

}
