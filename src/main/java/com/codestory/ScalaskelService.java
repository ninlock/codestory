package com.codestory;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/scalaskel")
@RequestScoped
public class ScalaskelService {
    
    @Inject
    QuestionReponseEJB questionReponseEJB;

    @GET
    @Path("/change/{X}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(@PathParam(value="X")int valueToChange) {        
        Response response;
        try{
            if(valueToChange < 1 || valueToChange>100 ){
                throw new Exception();
            }
            response = Response.ok(getMoneyFromCent(valueToChange)).build();
        }catch (Exception e){
            response = Response.ok("[{\"Error\" : \"La valeur de changement doit être comprise entre 1 et 100. Valeur reçu : \"" + valueToChange + "}]").build();
            new WebApplicationException(e, response);           
        }
        //        response += SourceReader.getSourcesLink();
        return response;
    }
    
    private String getMoneyFromCent(int valueToChange){       
        List<String> changes = new ArrayList<String>();
        changes.add(getChangeAsJson(Cent.FOO, valueToChange));
        changes.add(getChangeAsJson(Cent.BAR, valueToChange));
        changes.add(getChangeAsJson(Cent.QIX, valueToChange));
        changes.add(getChangeAsJson(Cent.BAZ, valueToChange));
        
        String result = "";
        String curentChange = "";
        for (String change : changes) {
            if(change != null && !change.isEmpty() && !curentChange.equals(change)){
                result += ((!result.isEmpty())?",":"") + "{" +  change + "}";
            }
            curentChange = change;
        } 
        return "[" + result + "]";
    }
    
    private String getChangeAsJson(Cent cent, int value){ 
        List<String> results =  getChange(cent, value);
        String result = "";
        for (int i = results.size()-1; i >= 0 ; i--) {
            if(!result.isEmpty()){
                result += ",";
            }
            result += results.get(i);
        }
        return result;
    }
    
    private List<String> getChange(Cent cent, int value){
        List<String> results = new ArrayList<String>();          
        int multiple = cent.getMultipleCent(value);
        int reste = cent.getRestCent(value);
        if(value >= cent.getValue() && multiple != 0){
            results.add("\""+cent.getName()+"\":" + multiple);
        }
        if(reste != 0){
            Cent prevCent = cent.getCentByOrder(cent.getOrder()-1);
            if(prevCent != null){
                results.addAll(getChange(prevCent, reste));
            }
        }
        
        return results;
    }
}