package com.codestory;

<<<<<<< HEAD
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
=======
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
>>>>>>> 577392a606477acecca772aaf358138f548e76ca
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
<<<<<<< HEAD

    @Inject
    QuestionReponseEJB questionReponseEJB;
=======
    private static Map<String, String> responseMap = new HashMap<String, String>();
    static {
        responseMap.put("Quelle est ton adresse email", "ninlock37@gmail.com");
    }
>>>>>>> 577392a606477acecca772aaf358138f548e76ca

    @GET
    @Path("/")
    @Produces("text/html")
    public String doGet(@QueryParam(value="q")String param) {
<<<<<<< HEAD
        String response = "";
        try{
            response = getResponse(param);
        }catch (Exception e){
            Logger.getLogger(AskRESTService.class.getName()).log(Level.SEVERE, "Erreur technique : ", e);
            response = "Une erreur c'est produite, réessayez plus tard.";
        }
        //        response += SourceReader.getSourcesLink();
        return response;
    }

    @POST
    @Path("/")
    @Produces("text/html")
    public String doPost() {
        return "Réponse a un post";
    }

    @GET
    @Path("/admin")
    @Produces("text/html")
    public String doGetAdmin(@QueryParam(value="q")String q, @QueryParam(value="r")String r) {
        String result;
        try{
            addQuestionReponseInDB(q, r);
            result = "Sauvegarde ok pour : q = " + q + " / r = " + r;
        }catch(Exception e){
            result = "Echec de la sauvegarde";
        }
        return result;
    }

    private String getResponse(String q) throws Exception{      
        String reponse;
        if(q == null){
            reponse = "Le paramètre \"q\" n'est pas renseigné";
        }else{
            reponse = loadAndGetReponse(q);
            if(reponse != null){
                return reponse;
            }else{
                if(!q.equals("")){
                    reponse = "Pas encore de réponse";
                    addQuestionReponseInDB(q, reponse);
                }
                reponse = "Le paramètre ne doit pas être vide.";
            } 
        }
        return reponse;
    }


    private String loadAndGetReponse(String key) throws Exception{      
        QuestionReponse qr = questionReponseEJB.findQuestionReponseByQuestion(key);
        String reponse = null;
        if(qr != null){
            reponse = qr.getReponse();
        }
        return reponse;
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

=======
        String response = getResponse(param);
//        response += SourceReader.getSourcesLink();
        return response;
    }

    private String getResponse(String key){
        if(responseMap.containsKey(key)){
           return responseMap.get(key);
        }else{
            return "Aucune réponse trouvée.";
        }
    }
    
    
>>>>>>> 577392a606477acecca772aaf358138f548e76ca
}