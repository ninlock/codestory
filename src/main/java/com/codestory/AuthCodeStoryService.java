package com.codestory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/auth")
@Stateless
public class AuthCodeStoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCodeStoryService.class.getName());
    public static String MESSAGE_ALREADY_LOGIN = "Skip logged because already logged in: ";
    public static String MESSAGE_SUCCESS_LOGIN = "Authentication : successfully logged in ";
    public static String MESSAGE_ERROR_LOGIN = "Authentication error";

    @EJB
    QuestionReponseEJB questionReponseEJB;

    @GET
    @Path("/login/{login}/{password}")
    @Produces("text/html")
    public Response doGetReadEnonce(@PathParam("login") String login, @PathParam("password") String password, @Context HttpServletRequest req) {
        Response r = Response.ok().entity(CodeStoryUtil.MESSAGE_ERREUR).build();
        if(req.getUserPrincipal() == null){
            try {
                req.login(login, password);
                req.getServletContext().log(MESSAGE_SUCCESS_LOGIN + login);
            } catch (ServletException e) {
                LOGGER.error(MESSAGE_ERROR_LOGIN, e);
                r = Response.ok().entity(MESSAGE_ERROR_LOGIN).build();
            }
        }else{
            req.getServletContext().log(MESSAGE_ALREADY_LOGIN+login);
            r = Response.serverError().entity(MESSAGE_ALREADY_LOGIN+login).build();
        }
        return r;
    }
    

}