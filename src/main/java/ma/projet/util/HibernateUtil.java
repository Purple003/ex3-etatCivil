package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties props = new Properties();
            try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
                props.load(in);
            }

            Configuration cfg = new Configuration();

            // classes annotées
            cfg.addAnnotatedClass(ma.projet.beans.Homme.class);
            cfg.addAnnotatedClass(ma.projet.beans.Femme.class);
            cfg.addAnnotatedClass(ma.projet.beans.Mariage.class);

            // paramètres Hibernate
            cfg.setProperty("hibernate.connection.url", props.getProperty("jdbc.url"));
            cfg.setProperty("hibernate.connection.username", props.getProperty("jdbc.user"));
            cfg.setProperty("hibernate.connection.password", props.getProperty("jdbc.password"));
            cfg.setProperty("hibernate.dialect", props.getProperty("hibernate.dialect"));
            cfg.setProperty("hibernate.hbm2ddl.auto", props.getProperty("hibernate.hbm2ddl.auto", "update"));
            cfg.setProperty("hibernate.show_sql", props.getProperty("hibernate.show_sql", "true"));
            cfg.setProperty("hibernate.format_sql", props.getProperty("hibernate.format_sql", "true"));
            cfg.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

            StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
            return cfg.buildSessionFactory(ssrb.build());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown(){
        getSessionFactory().close();
    }
}
