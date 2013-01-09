package com.codestory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/")
@RequestScoped
public class AskRESTService {
    private static Map<String, String> responseMap = new HashMap<String, String>();
    static {
        responseMap.put("Quelle est ton adresse email", "code.story@gmail.com");
    }

    @GET
    @Path("/")
    @Produces("text/html")
    public String doGet(@QueryParam(value="q")String param) {
        String response = getResponse(param);
        response += SourceReader.getSourcesLink();
        return response;
    }

    private String getResponse(String key){
        if(responseMap.containsKey(key)){
           return responseMap.get(key);
        }else{
            return "Aucune réponse trouvée.";
        }
    }
    
    @GET
    @Path("/sources")
    @Produces("text/html")
    public String sources() throws Exception {
       return SourceReader.getSources();
    }
    
    
}