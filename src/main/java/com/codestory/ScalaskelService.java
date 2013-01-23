package com.codestory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private static final  Map<Integer,List<Map<Cent, Integer>>> listeFullSolution = initAllSolutions();

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
            //            List<Map<Cent, Integer>> liste = new CentChange(valueToChange).getAllChange();
            response = Response.ok(getMoneyFromCent(valueToChange)).build();

        }catch (Exception e){
            response = Response.ok("[{\"Error\" : \"La valeur de changement doit être comprise entre 1 et 100. Valeur reçu : \"" + valueToChange + "}]").build();
            new WebApplicationException(e, response);           
        }
        //        response += SourceReader.getSourcesLink();
        return response;
    }

    private static   Map<Integer,List<Map<Cent, Integer>>> initAllSolutions() {    
        Map<Integer,List<Map<Cent, Integer>>> listeFull = new HashMap<Integer, List<Map<Cent,Integer>>>();
        int nbFoo = 0;
        int nbBar = 0;
        int nbQix = 0;
        int nbBaz = 0;
        int lastValue = 0;
        while(nbBaz <= Cent.BAZ.getMultipleCent(100)){
            nbFoo ++;
            if(nbFoo > Cent.FOO.getMultipleCent(100)){
               nbFoo = 0;
               nbBar++;
            }            
            if(nbBar > Cent.BAR.getMultipleCent(100)){
                nbBar = 0;
                nbQix++;
            }
            if(nbQix > Cent.QIX.getMultipleCent(100)){
                nbQix = 0;
                nbBaz++;
            }
            Map<Cent, Integer> currentMap = getInitMap(nbFoo, nbBar, nbQix, nbBaz);
            lastValue = nbFoo + (nbBar * Cent.BAR.getValue()) + (nbQix * Cent.QIX.getValue()) + (nbBaz * Cent.BAZ.getValue());
            if(lastValue <= 100){
              if(listeFull.get(lastValue) == null){
                  List<Map<Cent, Integer>> listeMap = new ArrayList<Map<Cent,Integer>>();
                  listeFull.put(lastValue, listeMap);
              }
              listeFull.get(lastValue).add(currentMap);              
            }
        }
        return listeFull;
    }
    
    private String getMoneyFromCent(int valueToChange){       
        List<Map<Cent, Integer>> changes = listeFullSolution.get(valueToChange);
        List<String> listeJsonObject = new ArrayList<String>();
        for (Map<Cent, Integer> map : changes) {
            listeJsonObject.add(getCentMapToString(map));
        }
        String result = "";
        for (String jsonObject : listeJsonObject) {
            if(jsonObject != null && !jsonObject.isEmpty()){
                result += ((!result.isEmpty())?",":"") + jsonObject;
            }
        } 
        return "[" + result + "]";
    }
  
    
    private String getCentMapToString(Map<Cent, Integer> map){
        String result = "";
        for (Cent currentCent : map.keySet()) {
            Integer currentValue = map.get(currentCent);
            if(currentValue != 0){
                if(!result.isEmpty()){
                    result += ",";
                }              
                result += "\"" + currentCent.getName() + "\":" + currentValue;
            }
        }
        result = "{" + result + "}";
        return result;
    }

    private static Map<Cent, Integer> getInitMap(){
        return getInitMap(0, 0, 0, 0);
    }
    
    private static Map<Cent, Integer> getInitMap(int foo, int bar, int qix, int baz){
        Map<Cent, Integer>  map = new LinkedHashMap<Cent, Integer>();
        map.put(Cent.FOO, foo);
        map.put(Cent.BAR, bar);
        map.put(Cent.QIX, qix);
        map.put(Cent.BAZ, baz);
        return map;
    }
   
}