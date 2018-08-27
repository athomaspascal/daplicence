package elastic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ElasticClient {
    public static  String configElastic = "elasticsearch-config.properties";
    Properties elasticProperties = new Properties();
    public String indexName;
    public String indexType;
    public TransportClient client;
    static Logger logger = LogManager.getLogger("elastic-generator");


    public ElasticClient() throws IOException {
        if (System.getProperty("configElastic") != null)
            configElastic = System.getProperty("configElastic");
        elasticProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configElastic));

    }

    public ElasticClient(String indexName,String indexType) throws IOException {
        this.indexName = indexName;
        this.indexType = indexType;
        if (System.getProperty("configElastic") != null)
            configElastic = System.getProperty("configElastic");
        elasticProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configElastic));
    }


    public void connectElastic(String newIndexName, String newIndexType) {
        this.indexName = newIndexName;
        this.indexType = newIndexType;
        connectElastic();
    }

    public void connectElastic() {
        logger.info("Connecting Properties: Cluster-name=" + elasticProperties.getProperty("cluster-name"));

        Settings settings = Settings.builder()
                .put("cluster.name", elasticProperties.getProperty("cluster-name"))
                .put("client.transport.sniff", true)
                .put("xpack.security.user", "elastic:vRJrTIU12KyGOiLS7nEt")
                .build();


        try {
            String hostname= elasticProperties.getProperty("hostname");

            logger.info("Connecting Properties: Hostname=" + hostname);


            int port = Integer.parseInt(elasticProperties.getProperty("port"));
            logger.info("Connecting Properties: Port=" + port);

//            client = new PreBuiltTransportClient(settings)
            client = new PreBuiltXPackTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), port));
        } catch (UnknownHostException e) {
            logger.error("Connexion Elastic Error");
            e.printStackTrace();
        }
        logger.info("Client="+ client);

    }
    public void shutdown()
    {
        client.close();
    }

    public void indexDocument(String index, String type, String json)
    {
        System.out.println("Index:"+index);
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(json, XContentType.JSON)
                .get();

        String _index = response.getIndex();
        String _type = response.getType();
        String _id = response.getId();
        long _version = response.getVersion();
        RestStatus status = response.status();
        System.out.println("  _index=" + _index);
        System.out.println("   _type=" + _type);
        System.out.println("     _id=" + _id);
        System.out.println("_version=" + _version);
        System.out.println("  status=" + status.getStatus());


    }

    public void indexDocument(String json)
    {
        this.indexDocument(this.indexName,this.indexType,json);

    }

    public static void connectAndIndex(String document){
        ElasticClient elasticClient = null;
        String templateJson=null;
        String indexName=null;
        String indexType=null;

        try {
            elasticClient = new ElasticClient();

            templateJson=GenerateAndIndex.setArg("templateJson");
            indexName=GenerateAndIndex.setArg("indexName");
            indexType=GenerateAndIndex.setArg("indexType");
            elasticClient.connectElastic(indexName,indexType);
            elasticClient.indexDocument("{" + document +"}");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
