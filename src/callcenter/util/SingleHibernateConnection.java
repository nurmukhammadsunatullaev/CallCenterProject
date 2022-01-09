/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callcenter.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author developer
 */
public class SingleHibernateConnection {
    private static SessionFactory sessionFactory;
    private static final ServiceRegistry SERVICE_REGISTRY;
    private static final ThreadLocal<Session> THREADLOCAL;
    private static InformationDAO informationDao;

    static {
    try {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/call_center?autoReconnect=true&useUnicode=true&amp;characterEncoding=UTF-8");
        configuration.setProperty("hibernate.connection.username", "call_admin" );
        configuration.setProperty("hibernate.connection.password", "call_admin123" );
        SERVICE_REGISTRY = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(SERVICE_REGISTRY);
        THREADLOCAL = new ThreadLocal<>();
        } catch(HibernateException t){
            throw new ExceptionInInitializerError(t);
        }
    }
    public static Session getSession() {
        Session session = THREADLOCAL.get();
        if(session == null){
            session = sessionFactory.openSession();
            informationDao=new InformationDAO(session);
            THREADLOCAL.set(session);
        }
        return session;
    }

    public static InformationDAO getInformationDAO(){
        return informationDao;
    }

    public static void closeSession() {
        Session session = THREADLOCAL.get();
        if(session != null){
            session.close();
            THREADLOCAL.set(null);
        }
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(SERVICE_REGISTRY);
    }
    public static void exit(){
          closeSession();
          closeSessionFactory();
          System.exit(0);
    }
}
    
    
    
    
    
    
    
    
    

