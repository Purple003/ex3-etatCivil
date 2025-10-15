package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.GenericDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class FemmeService {
    private GenericDao<Femme, Integer> dao = new GenericDao<>(Femme.class);

    public Femme save(Femme f) { return dao.save(f); }
    public List<Femme> findAll() { return dao.findAll(); }

    public int countChildrenBetweenDatesNative(int femmeId, LocalDate from, LocalDate to) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT COALESCE(SUM(nbr_enfant),0) FROM mariage WHERE femme_id = :fid AND date_debut BETWEEN :d1 AND :d2";
        Query q = s.createNativeQuery(sql);
        q.setParameter("fid", femmeId);
        q.setParameter("d1", java.sql.Date.valueOf(from));
        q.setParameter("d2", java.sql.Date.valueOf(to));
        Number total = (Number) q.getSingleResult();
        s.close();
        return total == null ? 0 : total.intValue();
    }

    public List<Femme> findFemmesMarriedAtLeastTwice() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        TypedQuery<Femme> q = s.createNamedQuery("Femme.findMarriedAtLeastTwice", Femme.class);
        List<Femme> res = q.getResultList();
        s.close();
        return res;
    }
}
