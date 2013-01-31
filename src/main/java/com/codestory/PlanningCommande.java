package com.codestory;

public class PlanningCommande implements Comparable<PlanningCommande>, Cloneable {
    private Planning planning = new Planning();
    private int fin;

    public PlanningCommande() {
    }

    public Planning getPlanning() {
        return planning;
    }
        
    public void add(Commande commande){
        if(commande.getDepart() >= fin && !planning.containsPath(commande.getVol())){
          fin += commande.getDuree();
          planning.add(commande);
        }
     }

    public boolean containsPath(String path){
        return planning.containsPath(path);
    }

    @Override
    public int compareTo(PlanningCommande o) {
        return planning.compareTo(o.getPlanning());
    }
    
    public int getGain() {
        return planning.getGain();
    }

    public int getFin() {
        return fin;
    }
    
    public void setFin(int fin) {
        this.fin = fin;
    }
    
    public PlanningCommande clone() throws CloneNotSupportedException {
        PlanningCommande pc = new PlanningCommande();
        pc.fin = fin;
        pc.planning = planning.clone();
        return pc;
    }

}