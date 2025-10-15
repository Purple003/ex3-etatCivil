package ma.projet.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.util.HibernateUtil;

import java.util.List;

public class GenericDao<T, K> {
    private Class<T> clazz;
    public GenericDao(Class<T> clazz) { this.clazz = clazz; }

    public T save(T t) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.persist(t);
        tx.commit();
        s.close();
        return t;
    }

    public T update(T t) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.merge(t);
        tx.commit();
        s.close();
        return t;
    }

    public void delete(K id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        T obj = s.get(clazz, (java.io.Serializable) id);
        if (obj != null) s.remove(obj);
        tx.commit();
        s.close();
    }

    public T findById(K id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        T obj = s.get(clazz, (java.io.Serializable) id);
        s.close();
        return obj;
    }

    public List<T> findAll() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<T> list = s.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
        s.close();
        return list;
    }
}
