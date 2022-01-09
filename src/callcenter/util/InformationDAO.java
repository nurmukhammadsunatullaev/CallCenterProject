/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callcenter.util;

import call.center.model.LoginInformation;
import call.center.model.QuestionInformation;
import call.center.model.UserInformation;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


public class InformationDAO {
    
    private final Session session;

    public InformationDAO(Session session) {
        this.session=session;
    }
    
   public Long saveEntity(Object information){
        session.beginTransaction();
        Long  insertedId = (Long) session.save(information);
        session.getTransaction().commit();    
        return insertedId;
    }
    
    public Object getEntityById(Class type,Long id){
        return  session.get(type, id);
    }
    
    
    
    public boolean existsLogin(String login){
        Criteria criteria=session.createCriteria(UserInformation.class);
        criteria.add(Restrictions.eq("userLogin", login));
        return criteria.uniqueResult() != null;
    }
    
    public LoginInformation getLoginInformation(UserInformation user, Date loginDate){
        Criteria criteria=session.createCriteria(LoginInformation.class);
        criteria.add(Restrictions.and(Restrictions.eq("loginDate", loginDate),Restrictions.eq("user", user)));
        Object result = criteria.uniqueResult();
        return result != null ? (LoginInformation)result: null;    
    }
    
    public UserInformation getUserByData(String login, String password){
        Criteria criteria=session.createCriteria(UserInformation.class).add(Restrictions.and(Restrictions.eq("userLogin", login),Restrictions.eq("userPassword", password)));
        return (UserInformation) criteria.uniqueResult();
    }
    
    public List getAll(Class type){
        return session.createCriteria(type).list();
    }
    
    public List<QuestionInformation> getQuestionInformations(UserInformation user,String loginDate){
        Criteria criteria=session.createCriteria(LoginInformation.class);
        criteria.add(Restrictions.and(Restrictions.eq("user", user),Restrictions.eq("loginDate", java.sql.Date.valueOf(loginDate))));
        LoginInformation loginInformation=(LoginInformation) criteria.uniqueResult();
         
        Criteria questions=session.createCriteria(QuestionInformation.class);
        questions.add(Restrictions.and(Restrictions.eq("operatorId", user),Restrictions.eq("questionDate", loginInformation)));
        return questions.list();
    }
    
    public List<QuestionInformation> getQuestionInformations(UserInformation user,LoginInformation loginDate){
        Criteria questions=session.createCriteria(QuestionInformation.class);
        questions.add(Restrictions.and(Restrictions.eq("operatorId", user),Restrictions.eq("questionDate", loginDate)));
        return questions.list();
    }
    
    public List<QuestionInformation> getQuestionInformations(UserInformation user){
        Criteria questions=session.createCriteria(QuestionInformation.class);
        questions.add(Restrictions.eq("operatorId", user));
        return questions.list();
    }
}
