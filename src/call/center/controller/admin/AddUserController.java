/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.admin;

import call.center.controller.utils.MessageDialog;
import call.center.controller.utils.WidgetUtils;
import call.center.model.UserInformation;
import callcenter.util.FileUtils;
import callcenter.util.InformationDAO;
import callcenter.util.SingleHibernateConnection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author developer
 */
public class AddUserController implements Initializable{
    
    @FXML
    private TextField fullName,userLogin,userPassword,repeatPassword;
    
    @FXML
    private DatePicker birthday;
    
    @FXML
    private CheckBox admin;
    
    @FXML
    private Button addButton;
    
    @FXML
    private AnchorPane anchorPane; 
   
    private final  Stage addStage=new Stage();;
     
    private final InformationDAO database=SingleHibernateConnection.getInformationDAO();;
    
    private static AddUserController controller;
    
    public static AddUserController newInstance(Stage owner) throws IOException{
        if(controller==null){
           controller=new AddUserController(owner);
        }
        return controller;
    }
    
    private AddUserController(Stage owner) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/call/center/view/admin/addUser.fxml"));
        loader.setResources(FileUtils.getCurrentResource());
        loader.setController(this);
        Parent parent=loader.load();
        initDialog(parent, owner);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addButton.setOnAction(value->onClick(value));
        birthday.setValue(LocalDate.now());
        birthday.setConverter(WidgetUtils.getPattern());
    }
    
    private void initDialog(Parent parent,Stage owner){
           addStage.setScene(new Scene(parent));
           addStage.setOnCloseRequest(value->clearAllComponent());
           addStage.getIcons().add(WidgetUtils.getIcon());
           addStage.setTitle(FileUtils.getCurrentResource().getString("addUser"));
           addStage.initOwner(owner);
           addStage.initModality(Modality.WINDOW_MODAL);           
    }
    
    public void show(){
       addStage.showAndWait();
    }
    
   
    private void onClick(ActionEvent e){
        if(isFullData()){
            if(isEmptyLogin()){
                if(samePassword()){
                    UserInformation newUser=new UserInformation(fullName.getText(),userLogin.getText(),userPassword.getText(),admin.isSelected(),java.sql.Date.valueOf(birthday.getValue()));
                    database.saveEntity(newUser);
                    clearAllComponent();
                    MessageDialog.show("#", "#", "#User Added");
                    addStage.close();
                }else{
                    userPassword.requestFocus();
                    userPassword.selectAll();
                    repeatPassword.selectAll();
                    MessageDialog.show("#Add User Message", "#", "#Password not same!!!");
                }
            }else{
                userLogin.requestFocus();
                userLogin.selectAll();
                MessageDialog.show("#", "#", "#This login is used!!!");
                
            }
        }
        else{
            MessageDialog.show("#", "#", "#Please,fill all data");
        }
    }
    
    private boolean isFullData() {
        ObservableList<Node> children=anchorPane.getChildren();
        for (Node child:children) {
            if(child instanceof TextField){
               TextField text=(TextField) child;
               if(text.getText().trim().isEmpty()){
                   text.requestFocus();
                   return false;
               }
            }
        }
        return true;
    
    }
    
    private void clearAllComponent() {
        fullName.clear();
        userLogin.clear();
        userPassword.clear();
        repeatPassword.clear();
        birthday.setValue(LocalDate.now());
        admin.setSelected(false);
    }

    private boolean isEmptyLogin() {
       return !database.existsLogin(userLogin.getText());
    }

    private boolean samePassword() {
       return userPassword.getText().trim().equals(repeatPassword.getText().trim());
    }

}
    

