package com.codestory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;


public class SourceReader {

    /**
     * @param args
     * @throws Exception 
     */
    public static String getSources() throws Exception {
        String path = SourceReader.class.getResource("").getPath().replaceAll("/WEB-INF.*", "");
        String sources = "<script type='text/javascript'>" +
                "function openClasse(id){" +
                "   var elem = document.getElementById(id);" +
                "   if(elem.style.display == 'none'){" +
                "     elem.style.display = 'block';" +
                "   }else{" +
                "     elem.style.display = 'none';" +
                "   }" +
                "}" +
                "</script>";
        sources += getSources(path);
        //        sources += getResources(path);
        //        sources += getPomMaven(path);
        return sources;
    }

    public static String getSourcesLink() {
        return "<br/><a href='sources' style='text-decoration:none; color:white;' title='source project'>sources java</a>";
    }

    public static String getPomMaven(String sourcesPath) throws Exception {
        String pomPath = sourcesPath + "/META-INF/maven/com.codestory/ask-project/pom.xml";
        String source = encodeHtml(getInputStreamToString(new FileInputStream(new File(pomPath))));
        return createDynaBlock(source, "pom", "pom.xml");
    }    

    private static String encodeHtml(String datas){
        return datas.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    public static String getResources(String rootPath) throws Exception {
        String resourcesPath = rootPath + "/WEB-INF/";
        File directory = new File(resourcesPath);
        String result = "";
        for (File file : directory.listFiles()) {
            if(file.isFile()){
                result +=  createDynaBlock(encodeHtml(getInputStreamToString(new FileInputStream(file))), file.getName(), file.getName());
            }
        }
        return result;
    }

    public static String getSources(String sourcesPath) throws Exception {
        VirtualFile vf = VFS.getChild(sourcesPath);
        String result = "";
        if(vf.asDirectoryURI() != null || vf.asFileURI() != null){
            for (VirtualFile file : vf.getChildren()) {
                if(!file.getName().toLowerCase().endsWith("classes")){
                    if(file.isFile()){
                        result +=  createDynaBlock(encodeHtml(getInputStreamToString(file.openStream())), file.getName(), file.getName());
                    }else if(file.isDirectory()){
                        result += getSources(file.getPathName());
                    }

                }
            }
        }
        return result;
    }


    private static String createDynaBlock(String content, String idBlock, String label){
        String block = "<div id='open_" + idBlock + "' style='color:blue;text-decoration:underline;cursor:pointer' onclick=\"openClasse('" + idBlock + "')\">Source de : " + label + "</div>";
        block += "<div id='" + idBlock + "' style='display:none'><pre>";
        block += content;
        block += "</pre></div>";
        return block;
    }

    private static String getInputStreamToString(InputStream is) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line + "\n");
        }
        reader.close();
        return out.toString();
    }
}
