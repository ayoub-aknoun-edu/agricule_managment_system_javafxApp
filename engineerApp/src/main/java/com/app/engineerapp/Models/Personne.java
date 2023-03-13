package com.app.engineerapp.Models;

import java.util.Date;

public class Personne {
 private  String cni ;
 private  String nom;
 private String prenom;
 private Genre genre;
 private String tele;
 private Date date_naiss;
 private  Date date_embau;
 private String email;

 public Personne() {
 }

 public Personne(String cni, String nom, String prenom, Genre genre, String tele, Date date_naiss, Date date_embau,String email) {
  this.cni = cni;
  this.nom = nom;
  this.prenom = prenom;
  this.genre = genre;
  this.tele = tele;
  this.date_naiss = date_naiss;
  this.date_embau = date_embau;
   this.email=email;
 }

 public String getCni() {
  return cni;
 }

 public void setCni(String cni) {
  this.cni = cni;
 }

 public String getNom() {
  return nom;
 }

 public void setNom(String nom) {
  this.nom = nom;
 }

 public String getPrenom() {
  return prenom;
 }

 public void setPrenom(String prenom) {
  this.prenom = prenom;
 }

 public Genre getGenre() {
  return genre;
 }

 public void setGenre(Genre genre) {
  this.genre = genre;
 }

 public String getTele() {
  return tele;
 }

 public void setTele(String tele) {
  this.tele = tele;
 }

 public Date getDate_naiss() {
  return date_naiss;
 }

 public void setDate_naiss(Date date_naiss) {
  this.date_naiss = date_naiss;
 }

 public Date getDate_embau() {
  return date_embau;
 }

 public void setDate_embau(Date date_embau) {
  this.date_embau = date_embau;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }
}
