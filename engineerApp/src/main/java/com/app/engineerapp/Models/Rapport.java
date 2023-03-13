package com.app.engineerapp.Models;

import java.util.Date;

public class Rapport {
    private String idZone;
    private String data;
    private Date genDate;


    public Rapport(String idRapport,Date genDate) {
        this.idZone = idRapport;
        this.genDate=genDate;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }
}
