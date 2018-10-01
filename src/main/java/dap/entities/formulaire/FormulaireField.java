package dap.entities.formulaire;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "formulaire_field")
public class FormulaireField {
    @NotNull
    @Id
    @GeneratedValue
    int id;



    @Column(name = "ID_FORMULAIRE", nullable = true, length = 50)
    String idFormulaire;

    @Column(name = "CODE_PARAMETER", nullable = true, length = 50)
    String codeParameter;
    @Column(name = "LIBELLE", nullable = true, length = 50)
    String libelleField;
    @Column(name = "DESCRIPTION", nullable = true, length = 50)
    String descriptionField;
    @Column(name = "FLAG_MANDATORY", nullable = true, length = 50)
    int flagObligatoire;

    public String getTypeField() {
        return typeField;
    }

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }

    @Column(name="TYPE_FIELD")
    String typeField;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeParameter() {
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    public String getLibelleField() {
        return libelleField;
    }

    public void setLibelleField(String libelleField) {
        this.libelleField = libelleField;
    }

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public int getFlagObligatoire() {
        return flagObligatoire;
    }

    public void setFlagObligatoire(int flagObligatoire) {
        this.flagObligatoire = flagObligatoire;
    }

    public String getIdFormulaire() {
        return idFormulaire;
    }

    public void setIdFormulaire(String idFormulaire) {
        this.idFormulaire = idFormulaire;
    }
}
