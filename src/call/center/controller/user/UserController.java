/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.user;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author developer
 */
public class UserController implements Initializable,EventHandler<Event>{

    @FXML
    private ScrollPane contentPane;
    
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private ToolBar toolBar;
    
    private TabPane tabPane;
    private static UserController controller;
    private final UserInformation userValue;
    private final LoginInformation loginValue;
    private static Stage userStage;
    private AddCallController callController;
    private final InformationDAO informationDAO=SingleHibernateConnection.getInformationDAO();
    private final ResourceBundle bundle;
    private final HashMap<String,AnchorPane> contentItems=new HashMap<>();
    private final ExcelExport export=new ExcelExport();
    
    
    public static UserController newInstance(Stage mainStage, UserInformation userValue,LoginInformation loginValue) throws IOException{
        if(controller==null){
            userStage=mainStage;
            userStage.setResizable(true);
            controller=new UserController(userValue,loginValue);
        }
        return controller;
    }
    
    private UserController(UserInformation userValue,LoginInformation loginValue) throws IOException {
        this.userValue = userValue;
        this.loginValue=loginValue; 
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/call/center/view/user/user_page.fxml"));
        bundle=FileUtils.getCurrentResource();
        loader.setResources(bundle);
        loader.setController(this);
        userStage.setScene(new Scene(loader.load()));  
        userStage.show();
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            callController=AddCallController.newInstance(userValue,loginValue, userStage);        
            initMenuBar();
            initToolBar();
            initContent();
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initToolBar(){
        toolBar.getItems().addAll(
                getFullButton("addCall", "/resources/phone-add.png", value->{ addCallListener(); }),
                getFullButton("excelButton", "/resources/excel.png", export));
    }
    
    private Button getFullButton(String text,String imagePath,EventHandler<ActionEvent> handler){
        Button item=new Button(bundle.getString(text), WidgetUtils.getCustomImageView(30, 30,imagePath));
        item.setOnAction(handler);
        return item;
    }
    
    private void addCallListener(){
        AnchorPane todayContent=contentItems.get(loginValue.toString());
        if(todayContent!=null){
            TableView currentTable=(TableView) todayContent.getChildren().get(0);
            callController.show(currentTable.getItems());
        } 
    }
    
    private void initContent(){
        tabPane=new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        contentPane.setContent(tabPane);    
        List<LoginInformation> list=new ArrayList(userValue.getInformations());
        Collections.sort(list);
        for(int i=list.size()-1;i>=0;i--){
            contentItems.put(list.get(i).toString(), null);
            Tab tab=new Tab(list.get(i).toString());
            tab.setOnSelectionChanged(this);
            tabPane.getTabs().add(tab);   
        }
        
    }
    
    private void initMenuBar(){
        Menu file=new Menu(FileUtils.getCurrentResource().getString("fileMenu"));
        file.getItems().addAll(getMenuItem("addCall", "call", "/resources/phone-add.png"),
                               getMenuItem("excelButton", "excel", "/resources/excel.png"),
                               getMenuItem("exit", "exit", "/resources/exit.png"));
        menuBar.getMenus().add(file);
    }
    
    private MenuItem getMenuItem(String text,String id, String iconPath){
        MenuItem item=new  MenuItem(FileUtils.getCurrentResource().getString(text), WidgetUtils.getCustomImageView(20, 20,iconPath));
        item.setId(id);
        item.setOnAction(event->itemClicked(event));
        return item;
    }
    private void itemClicked(ActionEvent event){
        if(event.getSource() instanceof MenuItem){
            String id=((MenuItem)(event.getSource())).getId();
            switch(id){
                case "call": addCallListener();break;
                case "excel": export.handle(event);break;
                case "exit":SingleHibernateConnection.exit();break;
            }
        }
    }
    
    @Override
    public void handle(Event event) {
        Tab selectedTab=(Tab) event.getSource();
        AnchorPane current_Content;
        if(selectedTab.isSelected()){
            current_Content=contentItems.get(selectedTab.getText());
            if(current_Content==null){
                current_Content=getContent(selectedTab.getText());
                contentItems.replace(selectedTab.getText(), current_Content);
            }
            selectedTab.setContent(current_Content);
        }
    }
    
    
    private AnchorPane getContent(String value){
            AnchorPane content=new AnchorPane();
            TableBean bean=new TableBean(QuestionInformation.class);
            TableView<QuestionInformation> table=bean.getTableView();
            WidgetUtils.setAnchorNode(table);
            content.getChildren().add(table);
            table.getItems().addAll(informationDAO.getQuestionInformations(userValue, value));
            return content;
    }
    private class ExcelExport implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            Tab pane=tabPane.getSelectionModel().getSelectedItem();
            if(pane != null && pane.getContent() instanceof AnchorPane){
               AnchorPane panel=(AnchorPane) pane.getContent();
               TableView table=(TableView) panel.getChildren().get(0);
               Platform.runLater(new ExportToExcel(table.getItems(), pane.getText()));
            }
        }
    }
    
    
}