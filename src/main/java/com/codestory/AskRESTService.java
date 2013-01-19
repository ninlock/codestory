package com.codestory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/")
@RequestScoped
public class AskRESTService {
	final static String MESSAGE_ERREUR = "Une erreur c'est produite, réessayez plus tard.";
	private static final Logger logger = LoggerFactory.getLogger(AskRESTService.class.getName());
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
			response = MESSAGE_ERREUR;
		}
		//        response += SourceReader.getSourcesLink();
		return response;
	}

	@POST
	@Path("/enonce")
	@Produces("text/html")
	public Response doPost(@Context HttpServletRequest request) {
		Response reponse;
		try {        
			request.setCharacterEncoding("UTF-8");
			findFileDataAndSave(request);
			findParametersDataAndSave(request);
			logger.info(request.getQueryString());
			reponse = Response.status(201).entity("OK").build();
		} catch (Exception e) {
			logger.error("Un probleme c'est produit lors de la sauvegarde du fichier", e);
			reponse = Response.status(404).entity("KO").build();
		}
		return reponse;
	}

	private void findFileDataAndSave(HttpServletRequest request){
		try{
		FileItem fileItem = getContentUploadFiles(request, 0);
		if(fileItem != null){
			String fileName = fileItem.getName().substring(fileItem.getName().lastIndexOf("\\")+1);
			saveDataAsFile(fileName, fileItem.getString());
		}      
		}catch(Exception e){
			logger.warn("Pas de fichier a uploader header enctype=\"multipart/form-data\" manquant.");
		}
	}

	private void findParametersDataAndSave(HttpServletRequest request) throws Exception{
		Enumeration<String> names = request.getParameterNames();
		StringBuilder sb = new StringBuilder();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			sb.append(name + " : " + request.getParameter(name) + "\n");
			
		} 
		if(sb.length()>0){
			saveDataAsFile(System.currentTimeMillis()+".md", sb.toString());
		}        
	}

	private void saveDataAsFile(String fileName, String content) throws Exception{
		FileWriter uploadWriter = new FileWriter(createFileInFolderUpload(fileName));
		uploadWriter.write(content);
		uploadWriter.flush();
		uploadWriter.close();
	}


	@GET
	@Path("/readEnonce")
	@Produces("text/html")
	public String doGetReadEnonce(@QueryParam(value="fileName")String fileName) {
		String reponse = "";
		try {
			String currentFileName = "";
			if(fileName != null){
				File uploadedFile = getFileUploaded(fileName);        
				FileInputStream is = new FileInputStream(uploadedFile);
				reponse = "<div>" + uploadedFile.getName() + ":</div><pre>"+IOUtils.toString(is)+"</pre>";
				currentFileName = uploadedFile.getName();
				is.close();
			}
			reponse += "<h1>Liste des autres fichiers uploadés:</h1>";
			for (File file : getListeFileUploaded()) {
				if(!file.getName().equals(currentFileName)){
					reponse += "<br/><a href='readEnonce?fileName="+file.getName()+"'>"+file.getName()+"</a>";
				}
			}
			reponse += "<a href='fileName?file='></a>";			
		} catch (Exception e) {
			logger.error(MESSAGE_ERREUR, e);
			reponse = MESSAGE_ERREUR;
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
	@Path("/deleteFile")
	@Produces("text/html")
	public String doGetDeleteFile(@QueryParam(value="name")String fileName) {
		String result;
		try{
			if(getFileUploaded(fileName).delete()){
				result = "Suppression ok pour le fichier : " + fileName;
			}else{
				throw new Exception();
			}
		}catch(Exception e){
			result = "Echec de la suppression du fichier : " + fileName;
		}
		return result;
	}

	private FileItem getContentUploadFiles(HttpServletRequest request, int indexFile) throws Exception{
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fields = upload.parseRequest(request);
		FileItem fileItem = fields.get(indexFile);
		return fileItem;
	}

	private File createFileInFolderUpload(String fileName) throws Exception{
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

	private File getLastEnonce() throws Exception{
		File[] listFile = getListeFileUploaded();
		return listFile[listFile.length-1];
	}

	private File getFileUploaded(String fileName) throws Exception{
		return new File(getUploadFolderUpload() + fileName);
	}

	private File[] getListeFileUploaded() throws Exception{
		return new File(getUploadFolderUpload()).listFiles();
	}    

	private String getResponse(String q) throws Exception{      
		String reponse;
		if(q == null){
			reponse = "Le paramètre \"q\" n'est pas renseigné";
		}else{
			QuestionReponse qr = loadQuestionReponse(q);
			reponse = (qr == null)?null:qr.getReponse();
			if(reponse != null){
				return reponse;
			}else{
				if(!q.equals("")){
					reponse = "Pas encore de réponse";
					addQuestionReponseInDB(q, reponse);
				}else{
					reponse = "Le paramètre ne doit pas être vide.";
				}
			} 
		}
		return reponse;
	}

	private String getUploadFolderUpload(){
		return System.getProperty("jboss.server.base.dir")+"/upload/";
	}

	private QuestionReponse loadQuestionReponse(String question) throws Exception{      
		return questionReponseEJB.findQuestionReponseByQuestion(question);
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