package dap.vaadin.crudui.entities.formulaire;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "formulaire_list")
public class Formulaire {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    @Id
    @GeneratedValue
    private long id;

    public String getLibelleFormulaire() {
        return libelleFormulaire;
    }

    public void setLibelleFormulaire(String libelleFormulaire) {
        this.libelleFormulaire = libelleFormulaire;
    }

    @Column(name = "LIBELLE_FORMULAIRE", nullable = true, length = 50)
    String libelleFormulaire;


}
