package com.codestory;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CodeStoryUtil {

    public final static String MESSAGE_ERREUR = "Une erreur c'est produite, réessayez plus tard.";
    public final static Logger LOGGER = LoggerFactory.getLogger(CodeStoryUtil.class);

    public static void findFileDataAndSave(HttpServletRequest request){
        try{
            FileItem fileItem = getContentUploadFiles(request, 0);
            if(fileItem != null){
                String fileName = fileItem.getName().substring(fileItem.getName().lastIndexOf("\\")+1);
                saveDataAsFile(fileName, fileItem.getString());
            }      
        }catch(Exception e){
            LOGGER.warn("Pas de fichier a uploader header enctype=\"multipart/form-data\" manquant.");
        }
    }

    public static List<QuestionReponse> convertParametersToQR(HttpServletRequest request) {
        List<QuestionReponse> liste = new ArrayList<QuestionReponse>();
        Enumeration<String> names = request.getParameterNames();        
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            QuestionReponse qr = new QuestionReponse();
            qr.setQuestion(name);
            qr.setReponse(request.getParameter(name));
            liste.add(qr);

        } 
        return  liste;
    }
    
    public static String getListeQRAsString(List<QuestionReponse> listeQR) {
        StringBuilder sb = new StringBuilder();
        if(listeQR != null){
          for (QuestionReponse questionReponse : listeQR) {
              sb.append(questionReponse.getQuestion() + " : " +questionReponse.getReponse() + "\n");
          }
        }
        return sb.toString();
    }

    public static void saveQRAsFile(String numero, List<QuestionReponse> listeQR) throws Exception{
        String listeAsString = getListeQRAsString(listeQR);
        if(listeAsString.length()>0){
            saveDataAsFile(numero + "_" + System.currentTimeMillis()+".md", listeAsString);
        } 
    }

    public static FileItem getContentUploadFiles(HttpServletRequest request, int indexFile) throws Exception{
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fields = upload.parseRequest(request);
        FileItem fileItem = fields.get(indexFile);
        return fileItem;
    }



    public static void saveDataAsFile(String fileName, String content) throws Exception{
        FileWriter uploadWriter = new FileWriter(createFileInFolderUpload(fileName));
        uploadWriter.write(content);
        uploadWriter.flush();
        uploadWriter.close();
    }


    public static File createFileInFolderUpload(String fileName) throws Exception{
        File folder = new File(getUploadFolderUpload());
        folder.mkdir();
        File uploadFile = null;
        if(folder.exists()){
            uploadFile = new File(getUploadFolderUpload() + fileName);
            if(uploadFile.exists()){
                uploadFile.delete();
            }
            uploadFile.createNewFile();
        }       
        return uploadFile;
    }

    public static File getLastEnonce() throws Exception{
        File[] listFile = getListeFileUploaded();
        return listFile[listFile.length-1];
    }

    public static File getFileUploaded(String fileName) throws Exception{
        return new File(getUploadFolderUpload() + fileName);
    }

    public static File[] getListeFileUploaded() throws Exception{
        return new File(getUploadFolderUpload()).listFiles();
    }    


    public static String getUploadFolderUpload(){
        return System.getProperty("jboss.server.base.dir")+"/upload/";
    }



}
