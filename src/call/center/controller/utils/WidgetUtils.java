/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.utils;

import callcenter.util.FileUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
/**
 *
 * @author developer
 */
public class WidgetUtils {
    private static ToggleGroup group=new  ToggleGroup();
    public static MenuItem getMenuItem(String text,double height,double width,String iconPath){
        return new MenuItem(text,getCustomImageView(height, width, iconPath));
    }
    
    public static ImageView getCustomImageView(double height,double width,String iconPath){
        ImageView imageView = getCustomImageView(height, width);
        imageView.setImage(new Image(WidgetUtils.class.getResourceAsStream(iconPath)));
        return imageView;
    }
    public static ImageView getCustomImageView(double height,double width){
        ImageView imageView = new ImageView();
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        return imageView;
    }
    
    public static RadioMenuItem getRadioMenuItem(EventHandler<ActionEvent> value,String text,String id,String iconPath,boolean selected){
        RadioMenuItem item=new RadioMenuItem(text, getCustomImageView(30, 30, iconPath));
        item.setOnAction(value);
        item.setId(id);
        item.setSelected(selected);
        item.setToggleGroup(group);
        return item;
    }
    private static Image icon;
    public static Image getIcon(){
        if(icon==null){
          icon=new Image(WidgetUtils.class.getResourceAsStream(FileUtils.RESOURCE_PATH+"call_icon.png"));
        }
        return icon;
    } 
    
    private static DateConverter pattern;

    public static DateConverter getPattern() {
       if(pattern==null){
          pattern=new DateConverter();
       } 
       return pattern;
    }
    
    
    private static class DateConverter extends StringConverter<LocalDate>{
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override 
    public String toString(LocalDate date) {
        return date != null ? dateFormatter.format(date):"";
    }

     @Override 
     public LocalDate fromString(String string) {
        return string != null && !string.isEmpty()? LocalDate.parse(string, dateFormatter):null;
     }
 }
    
    public static void setAnchorNode(Node node,double top,double bottom,double left,double right){
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setRightAnchor(node, right);
    }
    public static void setAnchorNode(Node node){
        setAnchorNode(node, 0, 0, 0, 0);
    }
    
    
   
    
}
