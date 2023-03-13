package com.app.engineerapp.Models;

import java.util.Date;

public class Responsable extends Personne{

    private String type;
    private int is_verified;

    public Responsable() {
    }



    public Responsable(String cni, String nom, String prenom, Genre genre, String tele, Date date_naiss, Date date_embau, String email,String type) {
        super(cni, nom, prenom, genre, tele, date_naiss, date_embau,email);
        this.type=type;

    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
