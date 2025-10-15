package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.GenericDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class MariageService {
    private GenericDao<Mariage, Integer> dao = new GenericDao<>(Mariage.class);

    public Mariage save(Mariage m) { return dao.save(m); }

    public long countHommesWithFourFemmesBetween(LocalDate from, LocalDate to) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Mariage> r = cq.from(Mariage.class);

        Expression<Integer> hommeId = r.get("homme").get("id");
        Expression<Integer> femmeId = r.get("femme").get("id");

        cq.multiselect(hommeId, cb.countDistinct(femmeId));
        cq.where(cb.between(r.get("dateDebut"), from, to));
        cq.groupBy(hommeId);
        cq.having(cb.equal(cb.countDistinct(femmeId), 4L));

        List<Object[]> res = s.createQuery(cq).getResultList();
        s.close();
        return res.size();
    }
}
