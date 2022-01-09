/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.admin;

import call.center.controller.utils.MessageDialog;
import call.center.controller.utils.WidgetUtils;
import call.center.controller.utils.lib.TableBean;
import call.center.model.LoginInformation;
import call.center.model.QuestionInformation;
import call.center.model.UserInformation;
import callcenter.util.ExportToExcel;
import callcenter.util.FileUtils;
import callcenter.util.InformationDAO;
import callcenter.util.SingleHibernateConnection;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Developer
 */
public class AdminController implements Initializable,ChangeListener<TreeItem<Object>>,EventHandler<ActionEvent> {

    private static AdminController controller;    
    private static  Stage adminStage;
    private final InformationDAO database;
    private final HashMap<Object, TableView> content=new HashMap<>();
    private final TreeItem firstItem=new TreeItem("Users",WidgetUtils.getCustomImageView(35, 35, FileUtils.RESOURCE_PATH+"users_icon.png"));
    private final TreeItem secondItem=new TreeItem("Questions",WidgetUtils.getCustomImageView(35, 35, FileUtils.RESOURCE_PATH+"res_icon.png"));
    private TableView active;
    @FXML
    private AnchorPane contentPane;
    
    @FXML
    private TreeView<Object> treeView;
    
    @FXML
    private ToolBar toolBar;
    
    @FXML
    private MenuBar menuBar;
    
    public static AdminController newInstance(Stage mainStage) throws IOException{
        if(controller==null){
            adminStage=mainStage;
            adminStage.setResizable(true);
            controller=new AdminController();
        }
        return controller;
    }

    private AdminController() throws IOException {
        database=SingleHibernateConnection.getInformationDAO();
        initialize();
    }
    
    private void initialize() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/call/center/view/admin/admin_page.fxml"));
        loader.setController(this);
        Parent parent=loader.load();
        adminStage.setScene(new Scene(parent));
        adminStage.show();
    }
    
    private void initTreeView(TreeItem root){
        List<UserInformation> users=database.getAll(UserInformation.class);
        users.stream().map((UserInformation user) -> {
            TreeItem userItem=new TreeItem<>(user,WidgetUtils.getCustomImageView(30, 30, FileUtils.RESOURCE_PATH+"user_icon.png"));
            Iterator<LoginInformation> logins=user.getInformations().iterator();
            while(logins.hasNext()){
                TreeItem loginItem=new TreeItem<>(logins.next(),WidgetUtils.getCustomImageView(30, 30, FileUtils.RESOURCE_PATH+"date_icon.png"));
                userItem.getChildren().add(loginItem);
            }
            return userItem;
        }).forEach(firstItem.getChildren()::add);
        root.getChildren().add(firstItem);
        root.getChildren().add(secondItem);   
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         TreeItem rootItem=new TreeItem();
         treeView.setRoot(rootItem);
         treeView.setShowRoot(false);
         initTreeView(rootItem);
         initToolBar();
         initMenuBar();
         treeView.getSelectionModel().selectedItemProperty().addListener(this);
    }    

    @Override
    public void changed(ObservableValue<? extends TreeItem<Object>> observable, TreeItem<Object> oldValue, TreeItem<Object> newValue) {     
        Object value=newValue.getValue();
        active=content.get(value);
        if(active!=null){
           active.toFront();
           return;
        }
        if(value instanceof String){
            switch(String.valueOf(value)){
                case "Users": 
                    active=getTable(UserInformation.class);
                    active.getItems().addAll(database.getAll(UserInformation.class));        
                    break;
                case "Questions":
                    active=getTable(QuestionInformation.class);
                    active.getItems().addAll(database.getAll(QuestionInformation.class));        
                    break;
            }
        }
        else if(value instanceof UserInformation){
            UserInformation userValue=(UserInformation) newValue.getValue();
            active=getTable(QuestionInformation.class);
            active.getItems().addAll(database.getQuestionInformations(userValue));
        }
        else if(newValue.getValue() instanceof LoginInformation){
            UserInformation userValue=(UserInformation) newValue.getParent().getValue();
            LoginInformation loginValue=(LoginInformation) newValue.getValue();
            active=getTable(QuestionInformation.class);
            active.getItems().addAll(database.getQuestionInformations(userValue,loginValue));
        }
        content.put(value, active);
        contentPane.getChildren().add(active);
    }
    
    private TableView getTable(Class bean){
        TableBean user=new TableBean(bean);
        TableView table=user.getTableView();
        WidgetUtils.setAnchorNode(table);
        return table;
    }
    
    private void initToolBar(){
        Button  addUser=new Button(FileUtils.getCurrentResource().getString("addUser"), WidgetUtils.getCustomImageView(30, 30, FileUtils.RESOURCE_PATH+"user_add.png"));
        addUser.setId("user");
        addUser.setOnAction(this);
        Button excelButton=new Button(FileUtils.getCurrentResource().getString("excelButton"), WidgetUtils.getCustomImageView(30, 30, FileUtils.RESOURCE_PATH+"excel.png"));
        excelButton.setId("excel");
        excelButton.setOnAction(this);
        toolBar.getItems().addAll(getButton("addUser", "user", "user_add.png"),getButton("excelButton", "excel", "excel.png"));
    
    }
    
    private Button getButton(String text,String id,String iconPath){
        Button  item=new Button(FileUtils.getCurrentResource().getString(text), WidgetUtils.getCustomImageView(30, 30, FileUtils.RESOURCE_PATH+iconPath));
        item.setId(id);
        item.setOnAction(this);
        return item;
    }

    @Override
    public void handle(ActionEvent event) {
        String cmd;
        if(event.getSource() instanceof MenuItem){
            cmd=((MenuItem) event.getSource()).getId();
        }
        else if(event.getSource() instanceof Button){
            cmd=((Button) event.getSource()).getId();
        }
        else{
         return;
        }
        switch(cmd){
            case "user": 
                try {
                    AddUserController.newInstance(adminStage).show();
                } catch (IOException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "excel":
                try{
                    String fullName=treeView.getSelectionModel().getSelectedItem().getValue().toString();
                    if(active!=null){
                        Platform.runLater(new ExportToExcel(active.getItems(), fullName));
                    }
                } catch(NullPointerException ex){
                    MessageDialog.show("#", "#", "#Please, select table");
                }
                break;
            case "exit":
                SingleHibernateConnection.exit();
                break;
        }
    }
    
    private void initMenuBar(){
        Menu file=new Menu(FileUtils.getCurrentResource().getString("fileMenu"));
        file.getItems().addAll(getMenuItem("addUser", "user", "/resources/user_add.png"),
                               getMenuItem("excelButton", "excel", "/resources/excel.png"),
                               getMenuItem("exit", "exit", "/resources/exit.png"));
        menuBar.getMenus().add(file);
    }
    
    private MenuItem getMenuItem(String text,String id, String iconPath){
        MenuItem item=new  MenuItem(FileUtils.getCurrentResource().getString(text), WidgetUtils.getCustomImageView(20, 20,iconPath));
        item.setId(id);
        item.setOnAction(this);
        return item;
    }
}
