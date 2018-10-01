package dap.entities.team;

import dap.entities.enterprise.Division;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "TEAM")
public class Team {
    @NotNull
    @Id
    @GeneratedValue
    private long id;

    @Basic
    @Column(name = "NOMTEAM", nullable = true, length = 50)
    private String nomteam;


    @ManyToOne
    private Division userDivision;

    @Basic
    @Column(name = "TEAMBOSSNAME", nullable = true, length = 50)
    private String teamBossName;
    @Basic
    @Column(name = "TEAMBOSSEMAIL", nullable = true, length = 50)
    private String teamBossEmail;

    public String getTeamBossEmail() {
        return teamBossEmail;
    }

    public void setTeamBossEmail(String teamBossEmail) {
        this.teamBossEmail = teamBossEmail;
    }


    public String getTeamBossName() {
        return teamBossName;
    }

    public void setTeamBossName(String teamBossName) {
        this.teamBossName = teamBossName;
    }


    public String getNomteam() {
        return nomteam;
    }
    public void setNomteam(String nomTeam) {
        this.nomteam = nomTeam;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    public Division getUserDivision() {
        return userDivision;
    }

    public void setUserDivision(Division userDivision) {
        this.userDivision = userDivision;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id &&
                Objects.equals(nomteam, team.nomteam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomteam, id);
    }
}
