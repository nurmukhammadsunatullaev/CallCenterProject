package call.center.controller;


import call.center.controller.admin.AdminController;
import call.center.controller.utils.MessageDialog;
import call.center.controller.user.UserController;
import call.center.controller.utils.WidgetUtils;
import call.center.model.LoginInformation;
import call.center.model.UserInformation;
import callcenter.util.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * Created by developer on 11/23/17.
 */
public class MainController implements Initializable,EventHandler<ActionEvent>{
    
    @FXML
    private TextField login,password;
    
    @FXML
    private DatePicker loginDate;
    
    @FXML
    private Button loginButton; 
    
    @FXML
    private MenuButton lang;
    
    @FXML
    private Label loginText,passwordText,dayText;
    
    @FXML
    private AnchorPane anchorPane;
    
    private final Stage primaryStage;
    private InformationDAO database;
    private ResourceBundle bundle;
    private final Font font=new Font(18);

    public MainController(Stage primaryStage) throws IOException {
        this.primaryStage=primaryStage;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/call/center/view/main_page.fxml"));
        bundle=FileUtils.getCurrentResource();
        loader.setResources(bundle);
        this.primaryStage.getIcons().add(WidgetUtils.getIcon());
        loader.setController(this);
        Parent parent=loader.load();
        showWindow(parent);
    }
     
    private void showWindow(Parent parent){
        primaryStage.setTitle(bundle.getString("title"));
        primaryStage.setScene(new Scene(parent));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((WindowEvent value) -> {
            SingleHibernateConnection.exit();
        });
        
        primaryStage.show();   
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle=resources; 
        loginDate.setEditable(false);
        loginDate.setValue(LocalDate.now());
        loginButton.setOnAction(this);
        database=SingleHibernateConnection.getInformationDAO(); 
        loginDate.setConverter(WidgetUtils.getPattern());
        anchorPane.setStyle("-fx-background-color : #FFFFFF;");
        initLanguageItem();
        initFont();
    }
    private void initFont(){
    
        ObservableList<Node> allItems=anchorPane.getChildren();
        allItems.forEach((item) -> {
            if(item instanceof TextField){
                ((TextField) item).setFont(font);
            }
            else if(item instanceof Button){
                ((Button) item).setFont(font);
            }
            else if(item instanceof Label){
                ((Label) item).setFont(font);
            }
        });
      
    }
    
    private void onClick(ActionEvent e) throws IOException{
       if(notEmpty()){
           UserInformation user = database.getUserByData(login.getText(), password.getText());
            if(user != null){
                if(user.isAdmin()){
                    AdminController controller=AdminController.newInstance(primaryStage);   
                }
                else{
                    Date currentTime=Date.valueOf(loginDate.getValue());
                    LoginInformation loginInformation=database.getLoginInformation(user, currentTime);
                    if(loginInformation==null){
                        loginInformation=new LoginInformation(user,currentTime);
                        Long loginId=database.saveEntity(loginInformation);
                        loginInformation.setLoginId(loginId);
                        user.getInformations().add(loginInformation);
                    }
                    UserController controller=UserController.newInstance(primaryStage,user,loginInformation);
                } 
            }
            else{
               MessageDialog.show("loginMessageTitle", "loginMessageHeader", "loginMessageContent");
            }
        }else{
           MessageDialog.show("loginMessageTitle", "loginMessageHeader", "loginMessageContent");
        }   
    }

    private boolean notEmpty() {
        return !login.getText().trim().isEmpty()&& !password.getText().trim().isEmpty(); 
    }
    
    public void initLanguageItem(){
       lang.setGraphic(WidgetUtils.getCustomImageView(30, 30, "/resources/uz.png"));
       lang.getItems().add(WidgetUtils.getRadioMenuItem(this,"Ўзбек", "uz", "/resources/uz.png",true));
       lang.getItems().add(WidgetUtils.getRadioMenuItem(this,"Русский", "ru", "/resources/ru.png",false));
       lang.getItems().add(WidgetUtils.getRadioMenuItem(this,"English", "en", "/resources/en.png",false));
    }

    @Override
    public void handle(ActionEvent event) {
       if(event.getSource() instanceof Button){
           try {
               onClick(event);
           } catch (IOException ex) {
               Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
           }
       }else if(event.getSource() instanceof RadioMenuItem){
           RadioMenuItem selectedItem=(RadioMenuItem) event.getSource();
           if(selectedItem.isSelected()){
                lang.setGraphic(WidgetUtils.getCustomImageView(30, 30, "/resources/"+selectedItem.getId()+".png"));
                FileUtils.setCurrentResource(selectedItem.getId());
                bundle=FileUtils.getCurrentResource();
                languageChanged();
           } 
       }
    }
    
    public void languageChanged(){
       lang.setText(bundle.getString("lang"));
       loginButton.setText(bundle.getString("loginButton"));
       loginText.setText(bundle.getString("loginText"));
       passwordText.setText(bundle.getString("passwordText"));
       dayText.setText(bundle.getString("dayText"));
       password.setPromptText(bundle.getString("password"));
       login.setPromptText(bundle.getString("login"));
       primaryStage.setTitle(bundle.getString("title")); 
    }
  
}
