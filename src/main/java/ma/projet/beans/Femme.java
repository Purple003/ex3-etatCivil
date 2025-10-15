package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "femme")
@NamedQueries({
    @NamedQuery(name = "Femme.findMarriedAtLeastTwice",
                query = "SELECT f FROM Femme f JOIN f.mariages m GROUP BY f HAVING COUNT(m) >= 2")
})
public class Femme extends Personne {
    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages = new ArrayList<>();

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}
