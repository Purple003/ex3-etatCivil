package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.GenericDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Arrays;


import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class HommeService {
    private GenericDao<Homme, Integer> dao = new GenericDao<>(Homme.class);

    public Homme save(Homme h) { return dao.save(h); }
    public Homme findById(int id) { return dao.findById(id); }
    public List<Homme> findAll() { return dao.findAll(); }

    public List<Femme> findEpousesBetweenDates(int hommeId, LocalDate from, LocalDate to) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT m.femme FROM Mariage m WHERE m.homme.id = :hid AND m.dateDebut BETWEEN :from AND :to";
        TypedQuery<Femme> q = s.createQuery(jpql, Femme.class);
        q.setParameter("hid", hommeId);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Femme> result = q.getResultList();
        s.close();
        return result;
    }

    public List<Mariage> findMariagesAvecDetails(int hommeId) {
        Homme h = dao.findById(hommeId);
        if (h == null) return Arrays.asList();
        return h.getMariages();
    }
}
