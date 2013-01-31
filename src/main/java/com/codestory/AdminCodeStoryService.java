package com.codestory;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/admin")
@RequestScoped
public class AdminCodeStoryService {
    private static final Logger logger = LoggerFactory.getLogger(AdminCodeStoryService.class.getName());

    @Inject
    QuestionReponseEJB questionReponseEJB;

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

    @GET
    @Path("/add")
    @Produces("text/html")
    public String doGetAdd(@QueryParam(value="q")String q, @QueryParam(value="r")String r) {
        String result;
        try{
            addQuestionReponseInDB(q, r);
            result = "Sauvegarde ok pour : q = " + q + " / r = " + r;
        }catch(Exception e){
            result = "Echec de la sauvegarde";
        }
        return result;
    }

    @GET
    @Path("/delete")
    @Produces("text/html")
    public String doGetDelete(@QueryParam(value="q")String q) {
        String result;
        try{
            questionReponseEJB.deleteByQuestion(q);
            result = "Suppression ok pour la question" + q;
        }catch(Exception e){
            result = "Echec de la Suppression, vérifiez que la question existe.";
        }
        return result;
    }
    
    @GET
    @Path("/all")
    @Produces("text/html")
    public String doGetAll(@QueryParam(value="q")String q) {
        String result;
        try{
            List<QuestionReponse> liste = questionReponseEJB.findAllQuestionReponse();
            result = "<div>Liste des questions/réponse</div>";
            for (QuestionReponse questionReponse : liste) {
                result += "<br><a href=\"../?q="+questionReponse.getQuestion()+"\">" + questionReponse.getQuestion() + " = " + questionReponse.getReponse() + "</a>";
            }
        }catch(Exception e){
            result = CodeStoryUtil.MESSAGE_ERREUR;
        }
        return result;
    }

    @GET
    @Path("/deleteFile")
    @Produces("text/html")
    public String doGetDeleteFile(@QueryParam(value="name")String fileName) {
        String result;
        try{
            if(CodeStoryUtil.getFileUploaded(fileName).delete()){
                result = "Suppression ok pour le fichier : " + fileName;
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            result = "Echec de la suppression du fichier : " + fileName;
        }
        return result;
    }


    private void addQuestionReponseInDB(String question, String reponse) throws Exception{ 
        QuestionReponse qr = questionReponseEJB.findQuestionReponseByQuestion(question);
        if(qr == null){
            save(question, reponse);
        } else{
            qr.setReponse(reponse);
            questionReponseEJB.updateQuestionReponse(qr);
        }
    }

    private void save(String question, String reponse){
        QuestionReponse qr = new QuestionReponse();
        qr.setQuestion(question);
        qr.setReponse(reponse);
        questionReponseEJB.saveQuestionReponse(qr);
    }
}