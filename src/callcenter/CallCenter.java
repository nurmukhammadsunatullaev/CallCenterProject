/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callcenter;

import call.center.controller.MainController;
import callcenter.util.SingleHibernateConnection;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author Developer
 */
public class CallCenter extends Application {
    
     public static void main(String[] args) {
       
         SingleHibernateConnection.getSession();
        launch(args);
    }

     @Override
    public void start(Stage primaryStage) throws Exception {
        MainController mainController=new MainController(primaryStage);
    }
    
}
