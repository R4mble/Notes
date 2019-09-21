import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author Wangyl
 * @date 2019/9/9
 */
public class Main {


    RestHighLevelClient restHighLevelClient;



    public static void main(String[] args) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.filter(QueryBuilders.termQuery("mainUniqueId", ""));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);

        SearchRequest searchRequest = new SearchRequest("chat_record_20190909");

        searchRequest.source(sourceBuilder);

        SearchResponse response =
    }
}
