/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.utils;

import callcenter.util.FileUtils;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

/**
 *
 * @author developer
 */
public class MessageDialog {
    private final Alert message;
    private static MessageDialog dialog;
    private static ResourceBundle bundle;
    private  MessageDialog() {
        message=new Alert(Alert.AlertType.INFORMATION);
    }
    
    public static void show(String title,String header,String content){
        bundle=FileUtils.getCurrentResource();
        if(dialog==null){
           dialog=new MessageDialog();
        }
        title=title.indexOf('#')!=-1 ? title.replace('#', ' ') : bundle.getString(title);
        header=header.indexOf('#')!=-1 ? header.replace('#', ' ') : bundle.getString(header);
        content=content.indexOf('#')!=-1 ? content.replace('#', ' ') : bundle.getString(content);
        
        dialog.message.setTitle(title);
        dialog.message.setHeaderText(header);
        dialog.message.setContentText(content);
        dialog.message.show();
    }
}
