package solr.SolrService;

import com.util.tools.ReadWriteProperties;
import org.apache.solr.client.solrj.SolrQuery;
import solr.Bean.Page;
import solr.Bean.WordCount;
import solr.SolrDAO.SolrDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hty070503 on 2015/1/17.
 */
public class SearchWordService {

    public List<Object> getHotWord(int size) throws Exception {
        String url_Count = ReadWriteProperties.getInstance().readValue("solr", "url_Count");
        SolrDao count_solr = new SolrDao(url_Count);
        Page page = new Page();
        page.setPageNow(1);
        page.setPageSize(size);
        Map<String,Object> conditions = new HashMap<String,Object>();
        conditions.put("query_conditions","-indexid:0");
        conditions.put("sort_field","wordCount");
        conditions.put("sort_type", SolrQuery.ORDER.desc);
        page.setConditions(conditions);
        page = count_solr.query(WordCount.class,page);

        return page.getListResult();
    }
}
