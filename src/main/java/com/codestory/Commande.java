package com.codestory;

public class  Commande implements Comparable<Commande> {
    private String VOL;
    private int DEPART;
    private int DUREE;
    private int PRIX;

    public String getVol() {
        return VOL;
    }

    public void setVol(String vol) {
        VOL = vol;
    }

    public int getDepart() {
        return DEPART;
    }

    public void setDepart(int depart) {
        DEPART = depart;
    }

    public int getDuree() {
        return DUREE;
    }

    public void setDuree(int duree) {
        DUREE = duree;
    }

    public int getPrix() {
        return PRIX;
    }

    public void setPrix(int prix) {
        this.PRIX = prix;
    }

    public int getFin() {
        return DEPART + DUREE;
    }

    @Override
    public int compareTo(Commande c) {
        int compare = new Integer(DEPART).compareTo( c.getDepart());
        if(compare == 0){
            compare =  new Integer(c.getPrix()).compareTo(PRIX);
        }
        return compare;
    }

}