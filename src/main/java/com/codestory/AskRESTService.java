package com.codestory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/")
@RequestScoped
public class AskRESTService {
    private static Map<String, String> responseMap = new HashMap<String, String>();

    @GET
    @Path("/")
    @Produces("text/html")
    public String doGet(@QueryParam(value="q")String param) {

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
        return "Réponse a un post";
    }

    private String getResponse(String key) throws Exception{       
        if(key == null){
            return "Le paramètre \"q\" n'est pas renseigné";
        }
        
        String reponse = loadAndGetReponse(key);
        if(reponse != null){
            return reponse;
        }else{
            if(!key.equals("")){
                   addQuestion(key);
            }
            return "Aucune réponse trouvée.";
        }
    }

    private String loadAndGetReponse(String key) throws Exception{
        if(responseMap.isEmpty()){
            InputStream stream = getClass().getClassLoader().getResourceAsStream("data.json");
            Gson gson = new GsonBuilder().create();
            List<QR> liste = (List<QR>) gson.fromJson(IOUtils.toString(stream), QR.class);
            for (QR qr : liste) {
                if(!responseMap.containsKey(qr.getQuestion())){
                    responseMap.put(qr.getQuestion(), qr.getReponse());
                }
            }
        }
        return responseMap.get(key);
    }

    private void addQuestion(String key) throws Exception{
        InputStream stream = getClass().getClassLoader().getResourceAsStream("question.txt");
        File file;
        String result = "";
        if(stream == null){
            file = new File("question.txt");
            file.createNewFile();           
        }else{
            result = IOUtils.toString(stream);
        }
        file = new File("question.txt");
        result += key;
        FileWriter fw = new FileWriter(file);
        fw.write(result + "\n");
        fw.close();
    }
    
    private String getQuestions() throws Exception{
        InputStream stream = getClass().getClassLoader().getResourceAsStream("question.txt");
        String result = "";
        if(stream != null){
            result = IOUtils.toString(stream).replaceAll("\n", "<br>");
        }
        return result;
    }
    
    private void addQuestionReponse(String question, String reponse) throws Exception{
        InputStream stream = getClass().getClassLoader().getResourceAsStream("data.json");
        Gson gson = new GsonBuilder().create();
        List<QR> liste = Arrays.asList(gson.fromJson(IOUtils.toString(stream), QR.class));
        QR qr = new QR();
        qr.setQuestion(question);
        qr.setReponse(reponse);
        liste.add(qr);
//        gson.toJson(liste, writer);
    }


   public static class QR {
        String question;
        String reponse;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getReponse() {
            return reponse;
        }

        public void setReponse(String reponse) {
            this.reponse = reponse;
        }

    }
}