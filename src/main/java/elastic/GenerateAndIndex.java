package elastic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.StringTokenizer;

public class GenerateAndIndex {
        static DateFormat formatterDefault = new SimpleDateFormat("dd/MM/yyyy");
        static String[] listRandom = new String[50];
        static String[] listValue = new String[50];
        static int MAX_DOCUMENT=1000;
        static String templateJson = "template.json";
        static String indexName= "new-index";
        static String indexType= "new-type";
        static String separator = "|";
        static Properties general = new Properties();

        


        public static void main(String[] args) throws IOException, ParseException, InterruptedException {
            setGeneralProperties();

            int i = 50;
            int n = 0;
            ElasticClient elasticClient = new ElasticClient(indexName,indexType);
            String templateJson=null;
            String indexName=null;
            String indexType=null;

            templateJson=GenerateAndIndex.setArg("templateJson");
            indexName=GenerateAndIndex.setArg("indexName");
            indexType=GenerateAndIndex.setArg("indexType");
            elasticClient.connectElastic(indexName,indexType);
            while (n<MAX_DOCUMENT) {
                i = 50;
                while (--i > 0) {listRandom[i]="" ; listValue[i]="";}
                String json = writeJsonDocument(templateJson);
                if (general.getProperty("Sleep_" + templateJson) !=null)
                    Thread.sleep(Integer.parseInt(general.getProperty("Sleep_" + templateJson)) *(int)(Math.random() * 5));
                else if (general.getProperty("Sleep") !=null)
                    Thread.sleep(Integer.parseInt(general.getProperty("Sleep")) *(int)(Math.random() * 5));
                else
                    Thread.sleep(1000 *(int)(Math.random() * 5));

                if (System.getProperty("noindex") == null ||
                        (System.getProperty("noindex") != null && !System.getProperty("noindex").equalsIgnoreCase("true")))
                   elasticClient.indexDocument(json);
                n++;
            }
            elasticClient.shutdown();
    }

    public static void setGeneralProperties() throws IOException {
        general.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("general.properties"));
        if (general.getProperty("separator") !=null)
            GenerateAndIndex.separator = general.getProperty("separator");
    }

    public static String setArg(String param)
    {
        String p= null;
        if (System.getProperty(param)!=null) {
            return System.getProperty(param);
        }
        if (general.getProperty(param) !=null) {
            return general.getProperty(param);
        }
        switch (param) {
            case "templateJson":
                p = GenerateAndIndex.templateJson;
                break;
            case "indexName":
                p = GenerateAndIndex.indexName;
                break;
            case "indexType":
                p = GenerateAndIndex.indexType;
                break;
        }
        return p;
    }

    private static String  writeJsonDocument(String templateJson) throws IOException, ParseException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateJson);
        if (resourceAsStream == null)
            resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateJson + ".properties");
        if (resourceAsStream == null)
            resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateJson + ".json");

        BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream));
        String json=null;
        int nbIteration=0;
        String GU="\"";
        String EL= "\n";

        String nameField =null;
        String value = null;
        String typeField = null;

        int numValue=0;
        while(true)
        {
            String l=in.readLine();


            if (l!=null && l.startsWith("#")) continue;


            if (nbIteration ==0)
            {
                json = "{" + EL;
            }

            if (l==null ||l.trim().equalsIgnoreCase(""))
            {
                if (nameField != null)
                    if (typeField.equalsIgnoreCase("int"))
                        json = json + GU+nameField+GU+ ": "+ value + EL;
                    else
                        json = json + GU+nameField+GU+ ": "+ GU+ value + GU + EL;
                json = json + "}";
                break;
            }

            if (nameField !=null)
                if (typeField.equalsIgnoreCase("int"))
                    json = json + GU+nameField+GU+ ": "+ value + "," + EL;
                else
                    json = json + GU+nameField+GU+ ": "+ GU+ value + GU + "," + EL;
            nbIteration++;

            StringTokenizer s = new StringTokenizer(l,":");

            String numField = s.nextToken(GenerateAndIndex.separator);
            nameField = s.nextToken(GenerateAndIndex.separator); //System.out.println(numField+":" + name);
            String debug;
            typeField = s.nextToken(GenerateAndIndex.separator);
            String randomAttribute = s.nextToken(GenerateAndIndex.separator);


            String randomNumber = s.nextToken(GenerateAndIndex.separator);
            String debut = s.nextToken(GenerateAndIndex.separator);
            String formatDate=null;

            String fin = null;
            if (s.hasMoreTokens()) {
                fin = s.nextToken(GenerateAndIndex.separator);
            } else fin= " ";


            if (typeField.equalsIgnoreCase("date") && s.hasMoreTokens())
                formatDate = s.nextToken(GenerateAndIndex.separator);

            DateFormat formatter=formatterDefault;
            if (formatDate!=null)
                formatter = new SimpleDateFormat(formatDate);

            int r=0;
            if (randomNumber.equalsIgnoreCase(""))
                r=0;
            else if (randomNumber==null)
                r=0;
            else
            r= Integer.parseInt(randomNumber);
            LoadFile loadFile =null;

            value = null;
            if (nameField.equalsIgnoreCase("group"))
                debug= "true";
            switch (typeField) {
                case "date":
                    if (randomAttribute.equalsIgnoreCase("random"))
                        value = formatter.format(randomDate(debut, fin,r,formatDate).getTime()).toString();
                    if (randomAttribute.equalsIgnoreCase("from"))
                        value = formatter.format(fromDate(debut, fin,r,formatDate).getTime()).toString();
                    if (randomAttribute.equalsIgnoreCase("thisyear"))
                        value = formatter.format(fromThisYear(debut, fin,r,formatDate).getTime()).toString();

                    if (GenerateAndIndex.general.getProperty("Timezone") != null)
                            value= value + GenerateAndIndex.general.getProperty("Timezone");
                    break;
                case "string":
                    if (randomAttribute.equalsIgnoreCase("file"))
                    {
                        if (loadFile ==null) loadFile = new LoadFile(debut);
                        value = loadFile.getOneItem();
                    }
                    else
                        value = randomString(debut, Integer.parseInt(randomNumber));

                    break;
                case "int":
                    value = String.valueOf(randomInt(debut,fin,Integer.parseInt(randomNumber)));
                    break;
            }
            numValue++;
            listValue[numValue]=value;

        }

        System.out.println(json);
        in.close();
        return json;
    }

    public static String randomString(String chaine,int n) {
        StringTokenizer s = new StringTokenizer(chaine);
        int nb = 0;
        String value=null;

        nb = (int) (1 + Math.random() * (s.countTokens()));
        if (n> 0 && listRandom[n].equalsIgnoreCase("")) {
            listRandom[n] = String.valueOf(nb);
        }
        else if (n >0 ) {
            int i = 0;
            while (i<Integer.parseInt(listRandom[n]))
            {
                value = s.nextToken();
                i++;
            }
        }

        if (value == null)
        {
            int i = 0;
            while (i<nb)
            {
                value = s.nextToken();
                i++;
            }
        }


        return value;
    }

    public static int randomInt(String debut, String fin,int n) throws ParseException {
        int value3 =0;
        if (fin.startsWith("field")) {
            fin = fin.substring(5);
            fin = listValue[Integer.parseInt(fin)];

        }

        if (n >0 && listRandom[n].equalsIgnoreCase("")) {
            value3 = (int)(Integer.parseInt(debut) + Math.random()*( Integer.parseInt(fin) - Integer.parseInt(debut)));
            listRandom[n] = String.valueOf(value3);
        }
        else if (n >0) {
            StringTokenizer s= new StringTokenizer(debut);
            int i=0;
            while (i<Integer.parseInt(listRandom[n]))
            {
                value3 = Integer.parseInt(s.nextToken());
                i++;
            }

        }
        else if (n==0)
            value3 = (int)(Integer.parseInt(debut) + Math.random()*( Integer.parseInt(fin) - Integer.parseInt(debut)));


        return value3;
    }

    public static Calendar randomDate(String debut, String fin,int n,String formatDate) throws ParseException {
        DateFormat formatter=formatterDefault;
        if (formatDate!=null)
            formatter = new SimpleDateFormat(formatDate);

        Calendar cal=Calendar.getInstance();


        cal.setTime(formatter.parse(debut));
        Long value1 = cal.getTimeInMillis();

        cal.setTime(formatter.parse(fin));
        Long value2 = cal.getTimeInMillis();

        long value3 = (long)(value1 + Math.random()*(value2 - value1));
        cal.setTimeInMillis(value3);
        return cal;
    }

    public static Calendar fromDate(String debut, String fin,int n,String formatDate) throws ParseException {
        DateFormat formatter=formatterDefault;
        if (formatDate!=null)
            formatter = new SimpleDateFormat(formatDate);

        Calendar cal=Calendar.getInstance();

        if (!debut.equalsIgnoreCase("now"))
            cal.setTime(formatter.parse(debut));
        else
            cal.add(Calendar.MINUTE, Integer.parseInt(fin));


        Long value1 = cal.getTimeInMillis();

        if (!debut.equalsIgnoreCase("now"))
            cal.setTime(formatter.parse(debut));
        else
            cal=Calendar.getInstance();
        Long value2 = cal.getTimeInMillis();

        long value3 = (long)(value1 + Math.random()*(value2 - value1));
        cal.setTimeInMillis(value3);
        return cal;
    }

    public static Calendar fromThisYear(String debut, String fin,int n,String formatDate) throws ParseException {
        DateFormat formatter=formatterDefault;
        if (formatDate!=null)
            formatter = new SimpleDateFormat(formatDate);

        Calendar cal=Calendar.getInstance();

        if (!debut.equalsIgnoreCase("now"))
            cal.setTime(formatter.parse(debut));
        else
            cal.add(Calendar.DATE, Integer.parseInt(fin));


        Long value1 = cal.getTimeInMillis();

        if (!debut.equalsIgnoreCase("now"))
            cal.setTime(formatter.parse(debut));
        else
            cal=Calendar.getInstance();
        Long value2 = cal.getTimeInMillis();

        long value3 = (long)(value1 + Math.random()*(value2 - value1));
        cal.setTimeInMillis(value3);
        return cal;
    }


}
