package elastic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadFile {
    List<String> listAllData = new ArrayList<String>();
    List<String> listAlreadyChoosen = new ArrayList<String>();

    int nbItem;

    public static void main(String[] args) throws IOException {
        LoadFile l = new LoadFile("prenom.txt");
        int i =0;
        while (i<20)
        {
            String trouve= l.getOneItem();
            System.out.print(i +"=" + trouve);
            System.out.println(" ");
            i++;
        }


    }

    public LoadFile(String filename) throws IOException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        BufferedReader in
                = new BufferedReader(new InputStreamReader(resourceAsStream));
        while (true)
        {
            String l=null;
            try {
                l=in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (l == null) break;
            listAllData.add(l);
        }
    }

    public String getOneItem()
    {
        Random rnd = new Random();

        int i = rnd.nextInt(listAllData.size()) ;
        String trouve=null;
        if (listAllData.size() > listAlreadyChoosen.size()) {
            if (listAlreadyChoosen.size() <10)
                trouve = findNewItem(rnd, i);
            else {
                int n = rnd.nextInt(listAlreadyChoosen.size() );
                System.out.print(n + "-->" );

                if (n > 5 && n< 7)
                    trouve= findNewItem(rnd, i);
                else
                    return listAlreadyChoosen.get(n);
            }
        }
        listAlreadyChoosen.add(trouve);
        return trouve;
    }

    private String findNewItem(Random rnd, int i) {
        String trouve;
        trouve = listAllData.get(i);
        while (listAlreadyChoosen.contains(trouve)) {
            i = rnd.nextInt(listAllData.size());
            trouve = listAllData.get(i);
        }
        return trouve;
    }
}
