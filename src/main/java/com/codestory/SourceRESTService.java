package com.codestory;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/sources")
@RequestScoped
public class SourceRESTService {
    
    public SourceRESTService(){
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "test init servlet");
    }

    @GET
    @Path("/")
    @Produces("text/html")
    public String sources() throws Exception {
       return SourceReader.getSources();
    }
    
    
}