/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.controller.utils.lib;

import callcenter.util.FileUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Developer
 * 
 */
public class TableBean {
    private final Class bean;
    public TableBean(Class bean) {
        this.bean = bean;
    }
    public  TableView getTableView(){
        TableView table=new TableView<>();
        HashMap<String,Object> headers=getColumns();
        headers.entrySet().stream().forEach((next) -> {
            String keyValue=next.getKey();
            Object value=next.getValue();
            if(value instanceof String){
                table.getColumns().add(getColumn(value.toString()));
            }
            else if(value instanceof List){
                List<String> children=(List<String>) value;
                TableColumn parentColumn=getColumn(keyValue);
                children.stream().forEach((child) -> {
                    parentColumn.getColumns().add(getColumn(child));
                });
                table.getColumns().add(parentColumn);
            } 
        });
        return table;
    }
   
    private  HashMap<String,Object> getColumns(){
        HashMap<String,Object> headers=new HashMap<>();
        List<Field> allAttributes=getAllColumnFields(bean);
        allAttributes.stream().forEach((Field field) -> {
            FXColumnTable annotation=field.getAnnotation(FXColumnTable.class);
            if(annotation.joinColumn()){
                List<String> value = getAllJoinColumnFields(allAttributes, annotation.joinColumnName());
                if(!headers.containsKey(annotation.joinColumnName())){
                    headers.put(annotation.joinColumnName(), value);
                }
            }else{
                if(annotation.fromResourceBundle()){
                    if(annotation.columnName().equals("NOT_SET")){
                       headers.put(field.getName(), field.getName());
                    }else{
                       headers.put(annotation.columnName(), annotation.columnName());
                    }
                }
                else{
                    headers.put(annotation.columnName(), annotation.columnName());
                }
            }
        });
        return headers;
    }
    
    private  List<Field> getAllColumnFields(Class item){
        List<Field> items=new ArrayList<>(100);
        Field [] allFields=item.getDeclaredFields();
        for (Field field : allFields) {
            if(field.isAnnotationPresent(FXColumnTable.class)){
               items.add(field);         
            }
        }
        return items;
    }
    
    private  List<String> getAllJoinColumnFields(List<Field> items, String joinColumnName){
        List<String> joinColumns=new ArrayList<>();
        items.stream().forEach((Field field) -> {
            FXColumnTable annotation=field.getAnnotation(FXColumnTable.class);
            if (annotation.joinColumnName().equals(joinColumnName)) {
                if(annotation.columnName().equals("NOT_SET")){
                    joinColumns.add(field.getName()); 
                }else{
                    joinColumns.add(annotation.columnName()); 
                }
            }
        });
        return joinColumns;
    }
    
    private TableColumn getColumn(String property){
            TableColumn column=new TableColumn<>(FileUtils.getCurrentResource().getString(property));
            column.setCellValueFactory(new PropertyValueFactory<>(property));
            return column;
    }
}
