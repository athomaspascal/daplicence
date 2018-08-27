package elastic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Map;

import static elastic.GenerateAndIndex.setGeneralProperties;

public class lectureElastic {
    public String indexName;
    public String indexType;
    ElasticClient elasticClient;
    static Logger logger = LogManager.getLogger("elastic-generator");

    lectureElastic() throws IOException {
        indexName=GenerateAndIndex.setArg("indexName");
        indexType=GenerateAndIndex.setArg("indexType");
        elasticClient = new ElasticClient(indexName,indexType);
    }

    public static void main(String[] args) throws IOException {
        setGeneralProperties();
        lectureElastic l = new lectureElastic();
        l.init();
        l.lecture();
    }

    public void init()
    {

        elasticClient.connectElastic();
    }

    public void lecture()
    {
/*
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "peter").type(Type.PHRASE).minimumShouldMatch("99%");
        LocalDateTime toLocal = new LocalDateTime(2013,12,18, 0, 0);
        Date to = toLocal.toDate();
        LocalDateTime fromLocal = new LocalDateTime(2013,12,17, 0, 0);
        Date from = fromLocal.toDate();
        RangeQueryBuilder queryDate = QueryBuilders.rangeQuery("time").to(to).from(from);
        FilterBuilder filterDate = FilterBuilders.queryFilter(queryDate);
*/
        String s ="cpu_usage" ;
        SearchResponse response = elasticClient.client.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setFetchSource(new String[]{"cpu_usage","timestamp"}, null)
                .setQuery(QueryBuilders.termsQuery("metric_name", "activite_cpu"))

                .execute()
                .actionGet();
        logger.info("Resultat lecture=" + response.status());


        for (SearchHit hit : response.getHits()){


            Map map = hit.getSourceAsMap();
            logger.info("map"+ map);


        }

    }
}
