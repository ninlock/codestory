package com.codestory;

import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.ant.Groovy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/")
@RequestScoped
public class CodeStoryService {

    private static final Logger logger = LoggerFactory.getLogger(CodeStoryService.class.getName());
    @Inject
    QuestionReponseEJB questionReponseEJB;

    @GET
    @Path("/")
    @Produces("text/html")
    public String doGet(@QueryParam(value="q")String param) {
        String response = "";
        try{
           response = getResponse(param);
        }catch (Exception e){
            logger.error("Erreur technique : ", e);
            response = CodeStoryUtil.MESSAGE_ERREUR;
        }
        //        response += SourceReader.getSourcesLink();
        return response;
    }
        
    private Number calculOperation(String operation) throws ScriptException{
        operation = operation.replaceAll(",", ".");
//      ScriptEngineManager sem = new ScriptEngineManager();
//      sem.getEngineFactories();
//      ScriptEngine se = sem.getEngineByName("js");
//      Double r = Double.parseDouble(se.eval(operation));    
        return (Number)new GroovyShell().evaluate(operation);
    }

    @POST
    @Path("/enonce/{num}")
    @Produces("text/html")
    public Response doPost(@Context HttpServletRequest request, @PathParam(value="num") String numero) {
        Response reponse;
        List<QuestionReponse> listeParams = null;
        try {        
            request.setCharacterEncoding("UTF-8");
            
            listeParams = CodeStoryUtil.convertParametersToQR(request);
            CodeStoryUtil.saveQRAsFile(numero, listeParams);
            
            CodeStoryUtil.findFileDataAndSave(request);
            request.getServletContext().log("Numer = " + numero + ", Params : " + CodeStoryUtil.getListeQRAsString(listeParams));
            reponse = Response.status(201).entity("OK pour enonce " + numero + " : " + CodeStoryUtil.getListeQRAsString(listeParams)).build();
        } catch (Exception e) {
            request.getServletContext().log("Un probleme c'est produit lors de la récupération des parametres : " + CodeStoryUtil.getListeQRAsString(listeParams), e);
            reponse = Response.status(404).entity("OK pour enonce " + numero + " : " + e.getMessage()).build();
        }
        return reponse;
    }

    @GET
    @Path("/readEnonce")
    @Produces("text/html")
    public String doGetReadEnonce(@QueryParam(value="fileName")String fileName) {
        String reponse = "";
        try {
            String currentFileName = "";
            if(fileName != null){
                File uploadedFile = CodeStoryUtil.getFileUploaded(fileName);        
                FileInputStream is = new FileInputStream(uploadedFile);
                reponse = "<div>" + uploadedFile.getName() + ":</div><pre>"+IOUtils.toString(is)+"</pre>";
                currentFileName = uploadedFile.getName();
                is.close();
            }
            reponse += "<h1>Liste des autres fichiers uploadés:</h1>";
            for (File file : CodeStoryUtil.getListeFileUploaded()) {
                if(!file.getName().equals(currentFileName)){
                    reponse += "<br/><a href='readEnonce?fileName="+file.getName()+"'>"+file.getName()+"</a>";
                }
            }
            reponse += "<a href='fileName?file='></a>";			
        } catch (Exception e) {
            logger.error(CodeStoryUtil.MESSAGE_ERREUR, e);
            reponse = CodeStoryUtil.MESSAGE_ERREUR;
        }
        return reponse;
    }

    private String getResponse(String q) throws Exception{      
        String reponse;
        if(q == null){
            reponse = "Le paramètre \"q\" n'est pas renseigné";
        }else{ 
            String operation = q.replaceAll(" ","+");
            try{
                reponse = calculOperation(operation).toString();
                if(reponse.contains(".")){
                    reponse = reponse.replaceAll("0*$", "").replaceAll("\\.$", "").replaceAll("\\.", ",");
                }
            }catch(Exception e) {
                QuestionReponse qr = loadQuestionReponse(q);
                reponse = (qr == null)?null:qr.getReponse();
                if(reponse != null){
                    return reponse;
                }else{
                    if(!q.equals("")){
                        reponse = "Pas encore de réponse pour cette question";
                    }else{
                        reponse = "Le paramètre ne doit pas être vide.";
                    }
                } 
            }
        }
        return reponse;
    }

    private QuestionReponse loadQuestionReponse(String question) throws Exception{      
        return questionReponseEJB.findQuestionReponseByQuestion(question);
    }

}