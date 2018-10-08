package dap.app;

import dap.entities.enterprise.Division;
import dap.entities.enterprise.DivisionRepository;
import dap.entities.team.Team;
import dap.entities.team.TeamRepository;

import javax.persistence.EntityManager;


public class test  {
    public static void main(String[] args) throws Exception {
        JPAService.init();
        EntityManager em = JPAService.getFactory().createEntityManager();
        //User u = UserRepository.getById(1L, em);

        Team t = new Team();

        t.setNomteam("TEAM 1");
        t.setTeamBossName("BOSS 1");
        t.setTeamBossEmail("boss@email.com");
        Division div = DivisionRepository.getById(0,em);


        t.setUserDivision(div);

        TeamRepository.save(t);

        //FormulaireResultatRepository.getById(1185,JPAService.getFactory().createEntityManager());

        JPAService.close();
    }
}
