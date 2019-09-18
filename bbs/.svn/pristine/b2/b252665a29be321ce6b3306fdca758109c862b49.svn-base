package solr.SolrDAO;

import com.util.tools.CheckTools;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import solr.Bean.Page;
import solr.Bean.WordCount;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrDao {

    private HttpSolrServer server = null;

    public SolrDao(String url) {
        try {
            server = new HttpSolrServer(url);
            server.setSoTimeout(10000);
            server.setConnectionTimeout(10000);
            server.setDefaultMaxConnectionsPerHost(100);
            server.setMaxTotalConnections(100);
            server.setFollowRedirects(false);
            server.setAllowCompression(true);
            server.setMaxRetries(1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void countWord(String word,String indexid) throws Exception {

        int new_count = 1;
        WordCount wordCount = new WordCount(word,new_count);
        if(!CheckTools.isNull(indexid)){
            wordCount.setIndexid(indexid);
        }
        if (!CheckTools.isNull(word)) {
            //判断该词是否存在
            List<Object> list = queryByCondition("name:" + word,WordCount.class);
            if(list!=null && list.size()>0){
                wordCount =  (WordCount)list.get(0);
                new_count = wordCount.getWordCount() + 1;
                wordCount.setWordCount(new_count);
                if(!CheckTools.isNull(indexid)){
                    wordCount.setIndexid(indexid);
                }
                del(word);
            }
            add(wordCount, WordCount.class);

        }

    }
    //索引批量添加
    public void add(List<?> list, Class<?> clazz) throws Exception {
        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        SolrInputDocument doc = null;
        String fieldName = "";
        String getMethod = "";
        Field[] fs = clazz.getDeclaredFields();
        if (list.size() > 0 && list != null) {
            for (Object o : list) {
                doc = new SolrInputDocument();

                for (Field ff : fs) {
                    fieldName = ff.getName();
                    getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    doc.addField(fieldName, clazz.getMethod(getMethod).invoke(o));
                }
                docs.add(doc);
            }
            server.add(docs);
            server.commit();
        }
    }

    //索引单个添加
    public void add(Object o, Class<?> clazz) throws Exception {
        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        SolrInputDocument doc = new SolrInputDocument();
        String fieldName = null;
        String getMethod = null;
        Field[] fs = clazz.getDeclaredFields();
        for (Field ff : fs) {
            fieldName = ff.getName();
            getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            doc.addField(fieldName, clazz.getMethod(getMethod).invoke(o));
            System.out.print(doc.getField(fieldName));
        }
        docs.add(doc);

        server.add(docs);
        server.commit();
    }

    //索引修改
    public void update() {
        //设置id unique 当同个id重复添加时后面覆盖前面 则起到了修改的作用
    }

    //索引删除
    public void del(List<String> ids) throws Exception {
        if (ids != null && ids.size() > 0) {
            server.deleteById(ids);
            server.commit();
        }
    }

    //索引删除
    public void del(String id) throws Exception {
        server.deleteById(id);//好像有问题
        server.commit();
    }

    public void delBycondition(String condition) throws IOException, SolrServerException {
        server.deleteByQuery(condition);
        server.commit();

    }

    //查询返回json 带分页
    public Page queryByJson(Page page, String conditions, String hightlight) {

        if (conditions == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        //获得结果  分页待实现
        SolrQuery solrQuery = new SolrQuery();
        String id = "";

        solrQuery.setQuery(conditions);
        solrQuery.setStart(page.getStart());
        solrQuery.setRows(page.getPageSize());
        solrQuery.setHighlight(true);//设置高亮
        solrQuery.setHighlightSimplePre("<font color='red'>");//高亮部分前缀
        solrQuery.setHighlightSimplePost("</font>");//高亮后缀
        solrQuery.setParam("hl.requireFieldMatch", true);
        solrQuery.setParam("hl.usePhraseHighlighter", true);
        solrQuery.setParam("hl.highlightMultiTerm", true);
        solrQuery.setParam("hl.fl", hightlight == null ? "id" : hightlight);


        //读取结果
        try {
            QueryResponse qr = server.query(solrQuery);
            SolrDocumentList docs = qr.getResults();

            page.setRowCount(docs.size());
            if (docs.size() <= 0)
                return null;
            //当前默认查询什么  高亮显示什么
//获取高亮字段
            Map<String, Map<String, List<String>>> hightLighting = qr.getHighlighting();
            for (SolrDocument doc : docs) {
                id = doc.getFieldValue("id").toString();
                sb.append("{");
                for (Map.Entry<String, Object> entry : doc.entrySet()) {
                    if (entry.getKey().equals(hightlight))
                        sb.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(hightLighting.get(id).get(hightlight).get(0)).append("\"").append(",");
                    else
                        sb.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"").append(",");
                }
                sb.delete(sb.length() - 1, sb.length());
                sb.append("}").append(",");
            }
            if (sb.length() > 1) {
                sb.delete(sb.length() - 1, sb.length());
            }
            sb.append("]");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        page.setJsonResult(sb.toString());

        return page;
    }


    //查询 带分页
    public Page query(Class<?> clazz, Page page) {
        List list = new ArrayList();
        //获得结果  分页待实现
        SolrQuery solrQuery = new SolrQuery();
        Map<String,Object> conditions = page.getConditions();
        if(conditions!=null) {
            if(conditions.get("query_conditions")!=null)
                solrQuery.setQuery(conditions.get("query_conditions").toString());
            if(conditions.get("sort_field")!=null)
                solrQuery.setSort(conditions.get("sort_field").toString(), (SolrQuery.ORDER) conditions.get("sort_type"));
        }
        solrQuery.setStart(page.getStart());
        solrQuery.setRows(page.getRowCount());
        solrQuery.setHighlight(true);//设置高亮
        solrQuery.setHighlightSimplePre("<font color='red'>");//高亮部分前缀
        solrQuery.setHighlightSimplePost("</font>");//高亮后缀
        solrQuery.setHighlightSnippets(2);//结果分片数 默认是1
        solrQuery.setHighlightFragsize(1000);//分片的长度
       /* solrQuery.setFacet(true)
                .setFacetMinCount(1)
                .setFacetLimit(5)//段
                .addFacetField("name")//分片字段
        ;//分片信息*/

        //读取结果
        try {
            QueryResponse qr = server.query(solrQuery);
            SolrDocumentList docs = qr.getResults();
            page.setRowCount(docs.size());
            if (docs.size() <= 0)
                return null;
            Field[] fs = clazz.getDeclaredFields();
            String fieldName = "";
            String getMethod = "";
            //String id = "";
            Object o = null;
            //获取高亮字段
            Map<String, Map<String, List<String>>> hightLighting = qr.getHighlighting();

            //当前默认查询什么  高亮显示什么
            for (SolrDocument doc : docs) {
                o = clazz.getConstructor(new Class[]{}).newInstance(new Object[]{});
                for (Field ff : fs) {
                    fieldName = ff.getName();
                    //if (!fieldName.equals(hightlight)) {
                    getMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Object value = doc.getFieldValue(fieldName);
                    clazz.getMethod(getMethod, ff.getType())
                            .invoke(o, value);


                   /* } else {
                        getMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        //高亮字段   默认是查询字段 该方法待扩展升级
                        clazz.getMethod(getMethod, ff.getType())
                                .invoke(o, hightLighting.get(id).get(fieldName).get(0));
                    }*/
                }
                list.add(o);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        page.setListResult(list);

        return page;
    }
   /* //根据产品id 得到关联的索引号
    public List<String> queryIdsByGoodsId(String id) throws Exception{
        List<String> list = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("contentId:"+id);
        SolrDocumentList docs = server.query(solrQuery).getResults();
        if(docs!=null && docs.size()>0){
            for(SolrDocument doc : docs ){
                list.add(doc.getFieldValue("id").toString());
            }
        }
        return list;
    }*/


    //查询得到最后一个id
    public int queryLastId() throws Exception {

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");

        SolrDocumentList docs = server.query(solrQuery).getResults();
        if (docs != null && docs.size() > 0) {

            return Integer.parseInt(docs.get(docs.size() - 1).getFieldValue("id").toString());
        }
        return 0;
    }

    //查询该商品是否已存在 根据id
    public boolean queryIfExist(String condition) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(condition);

        if (server.query(solrQuery).getResults().size() > 0)
            return true;
        else
            return false;
    }

    //查询商品
    public List<Object> queryByCondition(String condition,Class clazz) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(condition);

        SolrDocumentList docs = server.query(solrQuery).getResults();
        List<Object> list = new ArrayList<Object>();
        Object o = null;
        Field[] fs = clazz.getDeclaredFields();
        String fieldName = "";
        String getMethod = "";
        if (docs != null && docs.size() > 0) {
            for (SolrDocument doc : docs) {
                o = clazz.getConstructor(new Class[]{}).newInstance(new Object[]{});
                for (Field ff : fs) {
                    fieldName = ff.getName();
                    getMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    clazz.getMethod(getMethod, ff.getType())
                            .invoke(o, doc.getFieldValue(fieldName));
                }
                list.add(o);
            }
            return list;
        }
        else
            return null;
    }

    //查询该商品是否已存在 并返回需求信息 override
    public String queryIfExist(String condition, String get) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(condition);
        SolrDocumentList doc = server.query(solrQuery).getResults();
        if (doc.size() > 0) {
            return doc.get(0).getFieldValue(get).toString();
        } else
            return null;
    }

    //根据条件查单个属性  只支持单个输出属性查询
    public List<String> queryValueByCondition(String condition, String getValue,int rows) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(condition);
        solrQuery.setRows(rows);
        SolrDocumentList docs = server.query(solrQuery).getResults();
        List<String> list = new ArrayList<String>();
        for (SolrDocument doc : docs) {
            list.add(doc.getFieldValue(getValue).toString());
        }
        return list;

    }


}
