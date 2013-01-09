package com.codestory;

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
    
    @GET
    @Path("/")
    @Produces("text/html")
    public String sources() throws Exception {
       return SourceReader.getSources();
    }
       
}