/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.user;

import call.center.controller.utils.MessageDialog;
import call.center.controller.utils.WidgetUtils;
import call.center.model.LoginInformation;
import call.center.model.QuestionInformation;
import call.center.model.UserInformation;
import callcenter.util.FileUtils;
import callcenter.util.InformationDAO;
import callcenter.util.SingleHibernateConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author developer
 */
public class AddCallController implements Initializable,EventHandler<ActionEvent> {

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TextField fullName, address,phoneNumber,homePhoneNumber;
    
    @FXML
    private TextArea content;
    
    @FXML
    private CheckBox byLetter,byEmail,byPhone;
    
    @FXML
    private Button addQuestion;
    
    private final UserInformation userInf;
    
    private final LoginInformation loginInf;
    
    private final Stage addStage=new Stage(); 
    
    private static AddCallController addCallController;
    private final InformationDAO database=SingleHibernateConnection.getInformationDAO();
    private ObservableList<QuestionInformation> items;
    public static AddCallController  newInstance(UserInformation userInf,LoginInformation loginInf, Stage owner) throws IOException{
       
        if(addCallController==null){
           addCallController=new AddCallController(userInf, loginInf, owner);
        }
        return addCallController;
    
    }
    
    private AddCallController(UserInformation userInf,LoginInformation loginInf, Stage owner) throws IOException{
      this.userInf=userInf;
      this.loginInf=loginInf;
      loadWindow(owner);
      
    }
    
    private void loadWindow(Stage owner) throws IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/call/center/view/user/addCall.fxml"));
        loader.setController(this);
        loader.setResources(FileUtils.getCurrentResource());
        Parent parent=loader.load();
        addStage.setTitle(FileUtils.getCurrentResource().getString("addCall"));
        addStage.getIcons().add(WidgetUtils.getIcon());
        addStage.setScene(new Scene(parent));
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(owner);
    }
    
    public void show(ObservableList<QuestionInformation> items){
        this.items=items;  
        addStage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addQuestion.setOnAction(this);
    }    

    @Override
    public void handle(ActionEvent event) {
        if(canSave()){
            QuestionInformation questionInformation=new QuestionInformation();
            questionInformation.setFullName(fullName.getText());
            questionInformation.setAddress(address.getText());
            questionInformation.setPhoneNumber(phoneNumber.getText());
            questionInformation.setHomePhoneNumber(homePhoneNumber.getText());
            questionInformation.setQuestionContent(content.getText());
            questionInformation.setByEmail(byEmail.isSelected());
            questionInformation.setByLetter(byLetter.isSelected());
            questionInformation.setByPhone(byPhone.isSelected());
            questionInformation.setOperatorId(userInf);
            questionInformation.setQuestionDate(loginInf);
            Long id=database.saveEntity(questionInformation);
            questionInformation.setId(id);
            items.add(questionInformation);
            clearAllComponents();
            MessageDialog.show("questionSavedTitle", "questionSavedHeader", "questionSavedMessage");
        }else{
           MessageDialog.show("#", "#", "notFullMessage");
        }
    }
    
    private void clearAllComponents(){
        ObservableList<Node> nodeItems=anchorPane.getChildren();
        nodeItems.stream().forEach((item) -> {
            if(item instanceof TextInputControl){
                ((TextInputControl) item).clear();
            }
        });
        byLetter.setSelected(false);
        byEmail.setSelected(false);
        byPhone.setSelected(false);
    }

    private boolean canSave() { 
        ObservableList<Node> nodeItems=anchorPane.getChildren();
        for(Node item:nodeItems){
            if(item instanceof TextInputControl && ((TextInputControl) item).getText().trim().isEmpty()) {
                item.requestFocus();
                return false;
            }
        }
        return byLetter.isSelected()||byEmail.isSelected()||byPhone.isSelected();
    }
    
   
}
