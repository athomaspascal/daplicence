package dap.vaadin.load;

import dap.vaadin.crudui.app.JPAService;
import dap.vaadin.crudui.entities.user.User;
import dap.vaadin.crudui.entities.user.UserRepository;
import elastic.ElasticClient;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class LoadUsers {
    public static void main(String[] args) throws IOException {
        LoadUsers loadUsers = new LoadUsers();
        loadUsers.init();
        List<User> all = UserRepository.findAllOderById("");


        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new FileReader("C:\\Users\\pascalthomas\\IdeaProjects\\users-data.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int n = 0;
        ElasticClient el1 = new ElasticClient();
        el1.connectElastic();
        Properties general = new Properties();
        String fic = "general.properties";
        general.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fic));

        for(User user : all){
            n = n + 1;
            //if (user.getMainProduct() != null)
            //if (!user.getMainProduct().getName().equalsIgnoreCase("SAS BASE"))
            String nomProduit = user.getMainProduct().getName();
            System.out.println(user.getId()+":"+ user.getDateCreation().toString() + ":" +
                    user.getTeamid().getNomteam() + ":" +
                    user.getTeamid().getUserDivision().getNameDivision() + ":" +
                    user.getNom() + ":" + nomProduit);


            //Set<Product> products = user.getProducts();
            try {
                el1.client.prepareDelete(general.getProperty("indexName"), general.getProperty("indexType"),
                        user.getId().toString()).get();
                if (n<123)
                el1.client.prepareIndex(
                        general.getProperty("indexName"),
                        general.getProperty("indexType"),user.getId().toString())
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("name",user.getNom())
                                .field("team", user.getTeamid().getNomteam())
                                .field("division",user.getTeamid().getUserDivision().getNameDivision())
                                .field("product_name",nomProduit)
                                .field("timestamp",user.getDateCreation())
                                .endObject())
                        .get();

            }
            catch(Exception e)
            {
                System.out.print("Erreur : "+ e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Fin");
        JPAService.close();

    }

    private static void updateUser(BufferedReader br, User user) {
        try {

            String thisLine = br.readLine();
            StringTokenizer s = new StringTokenizer(thisLine,",");
            s.nextToken();s.nextToken();
            user.setNom(s.nextToken());


            Date d = new Date();
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.DAY_OF_YEAR, 1);

            long random = ThreadLocalRandom.current().nextLong(cal.getTime().getTime(), d.getTime());
            Date date = new Date(random);
            user.setDateCreation(date);
            UserRepository.save(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  EntityManagerFactory factory;

    public  void init() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("test-pu");
           
        }
    }
}
