/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.model;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 *
 * @author developer
 */
@Entity
public class LoginInformation implements Serializable,Comparable<LoginInformation>{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  loginId;
 
    @ManyToOne
    @JoinColumn(name="userId")
    private UserInformation user;
    
    @Column(nullable = false)
    private Date loginDate;

    public LoginInformation() {
    }

    public LoginInformation(UserInformation user, Date loginDate) {
        this.user = user;
        this.loginDate = loginDate;
    }

    /**
     * @return the loginId
     */
    public Long getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }
    
    /**
     * @return the loginDate
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * @param loginDate the loginDate to set
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * @return the user
     */
    public UserInformation getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserInformation user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return loginDate.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(LoginInformation o) {
       return this.getLoginDate().compareTo(o.getLoginDate());
    }
}
