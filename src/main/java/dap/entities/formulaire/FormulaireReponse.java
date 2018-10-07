package dap.entities.formulaire;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FORMULAIRE_REPONSE", schema = "PUBLIC", catalog = "H2")
public class FormulaireReponse {
    private int id;
    private String reponse;
    private int idResultat;
    private int idQuestion;

    public FormulaireReponse()
    {

    }


    public FormulaireReponse (int idresultat, int idquestion, String rep)
    {
        idResultat = idresultat;
        idQuestion = idquestion ;
        reponse = rep;
    }

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "REPONSE", nullable = true, length = 2000)
    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaireReponse that = (FormulaireReponse) o;
        return id == that.id &&
                Objects.equals(reponse, that.reponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reponse);
    }

    @Basic
    @Column(name = "ID_RESULTAT", nullable = true)
    public int getIdResultat() {
        return idResultat;
    }

    public void setIdResultat(int idreponse) {
        this.idResultat = idreponse;
    }


    @Basic
    @Column(name = "ID_QUESTION", nullable = true)
    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }
}
