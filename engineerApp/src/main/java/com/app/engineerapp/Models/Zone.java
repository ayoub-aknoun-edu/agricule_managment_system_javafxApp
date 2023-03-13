package com.app.engineerapp.Models;

import java.util.Date;

public class Zone {
    private int zoneId;
    private String adress ;
    private double surface ;
    private String idResponsable;
    private String isExamineur;
    private Date last_Examin;
    private int needExamin;
    private String recolte;
    private  String produit;
    private String proprietaire;

    public Zone() {
    }

    public Zone( String adress, double surface, String idResponsable, String isExamineur, Date last_Examin, int needExamin, String recolte, String produit, String proprietaire) {

        this.adress = adress;
        this.surface = surface;
        this.idResponsable = idResponsable;
        this.isExamineur = isExamineur;
        this.last_Examin = last_Examin;
        this.needExamin = needExamin;
        this.recolte = recolte;
        this.produit = produit;
        this.proprietaire = proprietaire;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public String getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(String idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getIsExamineur() {
        return isExamineur;
    }

    public void setIsExamineur(String isExamineur) {
        this.isExamineur = isExamineur;
    }

    public Date getLast_Examin() {
        return last_Examin;
    }

    public void setLast_Examin(Date last_Examin) {
        this.last_Examin = last_Examin;
    }

    public int getNeedExamin() {
        return needExamin;
    }

    public void setNeedExamin(int needExamin) {
        this.needExamin = needExamin;
    }

    public String getRecolte() {
        return recolte;
    }

    public void setRecolte(String recolte) {
        this.recolte = recolte;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "zoneId=" + zoneId +
                ", adress='" + adress + '\'' +
                ", surface=" + surface +
                ", idResponsable='" + idResponsable + '\'' +
                ", isExamineur='" + isExamineur + '\'' +
                ", last_Examin=" + last_Examin +
                ", needExamin=" + needExamin +
                ", recolte='" + recolte + '\'' +
                ", produit='" + produit + '\'' +
                ", proprietaire='" + proprietaire + '\'' +
                '}';
    }
}
