package com.codestory;

public class Planning implements Comparable<Planning>, Cloneable {
    private int gain;

    private String[] path = new String[0];

    public Planning() {
    }

    public Planning(Commande commande) {
        super();
        gain = commande.getPrix();            
        addPath(commande.getVol());
    }

    private void addPath(String path){
        String[] pathCopy = this.path;
        this.path = new String[pathCopy.length + 1];            
        for(int i =0; i<pathCopy.length; i++) {
            this.path[i] = pathCopy[i];
        }
        this.path[pathCopy.length] = path;
    }

    public void add(Commande commande){
        gain += commande.getPrix();
        addPath(commande.getVol());
    }

    public boolean containsPath(String path){
        for(String currentPath : this.path) {
            if(currentPath.equals(path)){
                return true;
            }
        }
        return false;
    }

    public int getGain() {
        return gain;
    }


    public void setGain(int gain) {
        this.gain = gain;
    }


    public String[] getPath() {
        return path;
    }


    public void setPath(String[] path) {
        this.path = path;
    }

    @Override
    public int compareTo(Planning o) {
        return new Integer(o.getGain()).compareTo(gain);
    }

    public Planning clone() throws CloneNotSupportedException {
        Planning p = new Planning();
        p.gain = gain;
        p.path = path;
        return p;
    }
}