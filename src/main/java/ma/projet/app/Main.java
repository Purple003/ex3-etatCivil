package ma.projet.app;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        // 5 hommes
        for (int i=1; i<=5; i++) {
            Homme h = new Homme();
            h.setNom("SAID" + i);
            h.setPrenom("P" + i);
            h.setDateNaissance(LocalDate.of(1970 + i, 1, 1));
            hs.save(h);
        }

        // 10 femmes
        for (int i=1; i<=10; i++) {
            Femme f = new Femme();
            f.setNom("FEMME" + i);
            f.setPrenom("F" + i);
            f.setDateNaissance(LocalDate.of(1965 + i, 1, 1));
            fs.save(f);
        }

        // créer quelques mariages pour tests
        Homme h1 = hs.findAll().get(0);
        List<Femme> femmes = fs.findAll();

        Mariage m1 = new Mariage();
        m1.setHomme(h1);
        m1.setFemme(femmes.get(0));
        m1.setDateDebut(LocalDate.of(1990, 9, 3));
        m1.setNbrEnfant(4);
        ms.save(m1);

        Mariage m2 = new Mariage();
        m2.setHomme(h1);
        m2.setFemme(femmes.get(1));
        m2.setDateDebut(LocalDate.of(1995, 3, 3));
        m2.setNbrEnfant(2);
        ms.save(m2);

        Mariage m3 = new Mariage();
        m3.setHomme(h1);
        m3.setFemme(femmes.get(2));
        m3.setDateDebut(LocalDate.of(2000, 11, 4));
        m3.setNbrEnfant(3);
        ms.save(m3);

        // Exemple d'utilisation des services
        System.out.println("=== Liste des femmes ===");
        fs.findAll().forEach(f -> System.out.println(f.getId() + " - " + f.getNom()));

        // femme la plus âgée (dateNaissance la plus petite)
        Femme oldest = fs.findAll().stream().min((a,b)-> a.getDateNaissance().compareTo(b.getDateNaissance())).orElse(null);
        if (oldest != null) System.out.println("Femme la plus âgée: " + oldest.getNom());

        // épouses d'un homme donné entre 1989 et 2001
        System.out.println("Epouses de l'homme 1 entre 1989 et 2001:");
        hs.findEpousesBetweenDates(h1.getId(), LocalDate.of(1989,1,1), LocalDate.of(2001,12,31))
          .forEach(f -> System.out.println(f.getNom()));

        // nombre d'enfants d'une femme (native) entre deux dates
        int nb = fs.countChildrenBetweenDatesNative(femmes.get(0).getId(), LocalDate.of(1980,1,1), LocalDate.of(2025,12,31));
        System.out.println("Nbr enfants femme1 entre dates: " + nb);

        // femmes mariées >= 2 fois
        System.out.println("Femmes mariées >=2 fois:");
        fs.findFemmesMarriedAtLeastTwice().forEach(f -> System.out.println(f.getNom()));

        // hommes mariés à 4 femmes (criteria)
        long count4 = ms.countHommesWithFourFemmesBetween(LocalDate.of(1980,1,1), LocalDate.of(2025,12,31));
        System.out.println("Nombre d'hommes mariés à 4 femmes entre dates: " + count4);

        // afficher mariages d'un homme
        System.out.println("Mariages de l'homme 1 :");
        hs.findMariagesAvecDetails(h1.getId()).forEach(m ->
            System.out.printf("Femme: %s %s - Debut: %s - Fin: %s - Enfants: %d%n",
                    m.getFemme().getNom(), m.getFemme().getPrenom(),
                    m.getDateDebut(), m.getDateFin(), m.getNbrEnfant() == null ? 0 : m.getNbrEnfant())
        );

        HibernateUtil.shutdown();
    }
}
