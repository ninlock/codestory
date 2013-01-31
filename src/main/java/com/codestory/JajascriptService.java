package com.codestory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/jajascript")
@RequestScoped
public class JajascriptService {

    @POST
    @Path("/optimize")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(@Context HttpServletRequest request) {        
        Response response;
        try{
            String commandes = IOUtils.toString(request.getInputStream());
            request.getServletContext().log("Request : " + commandes + " \n type : " + request.getHeader("accept"));
            Gson g = new GsonBuilder().create();
            Commande[] listeCommande =  g.fromJson(commandes, Commande[].class);
            String r = g.toJson(getBestOffre(getSortedList(listeCommande)));
            response = Response.ok(r).build();
        }catch (Exception e){
            response = Response.status(404).build();
            new WebApplicationException(e, response);           
        }
        return response;
    }

    private Planning getBestOffre(List<Commande> commandes){
        return getListeOffre(new PlanningCommande(), commandes).getPlanning();
    }
    

    private PlanningCommande getListeOffre(PlanningCommande planning, List<Commande> commandes) {  
        PlanningCommande currentPlaning;
        try{
            currentPlaning = planning.clone();
        }catch(Exception e){
            currentPlaning = new PlanningCommande();
            Logger.getLogger(JajascriptService.class).error(e);
        }
        
        for (int i=0; i<commandes.size(); i++) {
            Commande commande = commandes.get(i);       
            currentPlaning.add(commande);           
            if(i < commandes.size()-2){
                List<Commande> listeCopy = new ArrayList<Commande>(commandes.subList(i, commandes.size()));
                listeCopy.remove(commandes.get(i+1));
                if(listeCopy.size() > 0){
                    PlanningCommande subPlaning = getListeOffre(planning, listeCopy);
                    if(subPlaning.getGain() > planning.getGain()){
                        planning = subPlaning;
                    }
                }
            }            
        }
        if(currentPlaning.getGain() > planning.getGain()){
            planning = currentPlaning ;
        }
        return planning;
    }

    private List<Commande> getSortedList(Commande[] commandes){
        List<Commande> commandesListe = new ArrayList<Commande>();
        for (Commande commande : commandes) {
            commandesListe.add(commande);
        }
        Collections.sort(commandesListe);
        return commandesListe;
    }
    
}