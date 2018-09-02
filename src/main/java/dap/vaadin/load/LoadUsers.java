package dap.vaadin.load;

import dap.vaadin.crudui.entities.user.User;
import dap.vaadin.crudui.entities.user.UserRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class LoadUsers {
    public static void main(String[] args) {
        LoadUsers loadUsers = new LoadUsers();
        loadUsers.init();
        List<User> all = UserRepository.findAll("");
        for(User user : all){
            System.out.println(user.getDateCreation().toString() + ":" +
                    user.getTeamid().getNomteam() + ":" +
                    user.getTeamid().getUserDivision().getNameDivision() + ":" +
                    user.getNom());
        }
        System.out.println("Fin");
        
    }

    private  EntityManagerFactory factory;

    public  void init() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("test-pu");
           
        }
    }
}
