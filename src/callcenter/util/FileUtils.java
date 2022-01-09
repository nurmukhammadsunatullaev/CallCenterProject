/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callcenter.util;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author developer
 */
public class FileUtils {
    public static final String ALL_PDF_FILES_FOLDER=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"CallCenterDocuments"+File.separator+"pdf"+File.separator;
    public static final String ALL_EXCEL_FILES_FOLDER=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"CallCenterDocuments"+File.separator+"excel"+File.separator;
    public static final String RESOURCE_PATH="/resources/";
    private static ResourceBundle currentResource=ResourceBundle.getBundle("resources.lang.Locale", new Locale("uz"));
    
    public static ResourceBundle getCurrentResource() {
        return currentResource;
    }

    public static void setCurrentResource(String lang) {
        FileUtils.currentResource = ResourceBundle.getBundle("resources.lang.Locale", new Locale(lang));
    }
    
    
    static {
        
            File file_pdf=new File(ALL_PDF_FILES_FOLDER);
            File file_excel=new File(ALL_EXCEL_FILES_FOLDER);
            if(!file_excel.exists()){
              file_excel.mkdirs();
            }
            if(!file_pdf.exists()){
              file_pdf.mkdirs();
            }
        }
    
    
    
    
    
}
