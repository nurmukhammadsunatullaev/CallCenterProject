/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.model;

import call.center.controller.utils.WidgetUtils;
import call.center.controller.utils.lib.FXColumnTable;
import callcenter.util.FileUtils;
import callcenter.util.IExcel;
import java.io.Serializable;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.control.DatePicker;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 *
 * @author developer
 */
@Entity
public class UserInformation implements Serializable,IExcel{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    
    @FXColumnTable
    @Column(length = 255, nullable = false )
    private String fullName;
    
    @FXColumnTable
    @Column(length = 128, unique = true, nullable = false)
    private String userLogin;
    
    @Column(length = 255, nullable = false)
    private String userPassword;
    
    @Column
    @FXColumnTable
    private boolean admin;
    
    @Column
    private Date userBirthday;
    
    @FXColumnTable
    @Transient
    private final DatePicker birthday=new DatePicker();
    
    @OneToMany(mappedBy = "user")
    private Set<LoginInformation> informations;
    
   
    public UserInformation() {
    }

    public UserInformation(String userFullName, String userLogin, String userPassword, boolean admin, Date userBirthday) {
        this.fullName = userFullName.trim();
        this.userLogin = userLogin.trim();
        this.userPassword = userPassword.trim();
        this.admin = admin;
        this.userBirthday = userBirthday;
        
    }
 @Override
    public  String[] getHeaders(){
        ResourceBundle bundle=FileUtils.getCurrentResource();
        return new String[]{
            bundle.getString("id"),
            bundle.getString("fullName"),
            bundle.getString("loginText"),
            bundle.getString("birthday"),
            bundle.getString("admin"),
        };
    }
    
    @Override
    public String[] getFullData(){
        return new String[]{
            userId.toString(),
            fullName,
            userLogin,
            userBirthday.toString(),
            admin ? "+" : "-",
        };
    }
   
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    /**
     * @return the userLogin
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * @param userLogin the userLogin to set
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * @return the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword the userPassword to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return the informations
     */
    public Set<LoginInformation> getInformations() {
        return informations;
    }

    /**
     * @param informations the informations to set
     */
    public void setInformations(Set<LoginInformation> informations) {
        this.informations = informations;
    }
    
    public void addLoginInformation(LoginInformation information){
        informations.add(information);
    }

    @Override
    public String toString() {
        return getFullName(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the userBirthday
     */
    public Date getUserBirthday() {
        return userBirthday;
    }

    /**
     * @param userBirthday the userBirthday to set
     */
    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    /**
     * @return the birthday
     */
    public DatePicker getBirthday() {
        birthday.setEditable(false);
        birthday.setConverter(WidgetUtils.getPattern());
        birthday.setValue(userBirthday.toLocalDate());
        return birthday;
    }
    
    
}
