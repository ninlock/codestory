package com.codestory;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

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
            for (Commande commande : listeCommande) {
                System.out.println(commande.getVol());
            }
            
            String r = g.toJson(new Offre());
            response = Response.ok(r).build();
        }catch (Exception e){
            response = Response.status(404).build();
            new WebApplicationException(e, response);           
        }
        //        response += SourceReader.getSourcesLink();
        return response;
    }
    
    private Offre getOffre(Commande[] commandes){
        Offre o = new Offre();
        for (Commande commande : commandes) {
            //TODO commande.getDepart()
        }
        return o;
    }

    public static class  Commande {
        private String vol;
        private int depart;
        private int duree;
        private int prix;

        public String getVol() {
            return vol;
        }

        public void setVol(String vol) {
            this.vol = vol;
        }

        public int getDepart() {
            return depart;
        }

        public void setDepart(int depart) {
            this.depart = depart;
        }

        public int getDuree() {
            return duree;
        }

        public void setDuree(int duree) {
            this.duree = duree;
        }

        public int getPrix() {
            return prix;
        }

        public void setPrix(int prix) {
            this.prix = prix;
        }


    }

    public static class Offre {
        private int gain;
        private String[] path;

    }
}