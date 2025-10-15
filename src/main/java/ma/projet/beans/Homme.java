package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homme")
public class Homme extends Personne {

    @OneToMany(
        mappedBy = "homme",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER // 👈 Ajoute cette ligne
    )
    private List<Mariage> mariages = new ArrayList<>();

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }
}
