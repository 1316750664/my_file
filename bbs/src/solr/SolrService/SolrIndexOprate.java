package solr.SolrService;


import com.util.tools.ReadWriteProperties;
import com.util.tools.ToolUtil;
import solr.Bean.Articles;
import solr.Bean.IndexAticles;
import solr.Bean.Users;
import solr.SolrDAO.SolrDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SolrIndexOprate {

    private static SolrIndexOprate instance = null;

    private SolrIndexOprate() {

    }

    public static SolrIndexOprate getInstance() {
        if (instance == null) {
            instance = new SolrIndexOprate();
        }
        return instance;
    }


    private SolrDao urlArticles = new SolrDao(ReadWriteProperties.getInstance().readValue("solr", "urlArticles"));
    private SolrDao urlIndexArticles = new SolrDao(ReadWriteProperties.getInstance().readValue("solr", "urlIndexArticles"));
    private SolrDao urlUsers = new SolrDao(ReadWriteProperties.getInstance().readValue("solr", "urlIndexUsers"));


    //用户添加 修改
    public void solrUserAdd(Users user) throws Exception {
        urlUsers.add(user,Users.class);
    }

    //用户注销
    public void solrUserDel(String id) throws Exception {
        urlUsers.delBycondition("id:"+id);
    }


    //发帖后索引添加
    public void executeSolrAddPlus(String id,String title) throws Exception {

                Articles articles = new Articles(id,title);
        //从数据库获取到数据
                List<String> titles = ToolUtil.titleDic(title);
                List<IndexAticles> listIndexAticles = new ArrayList<IndexAticles>();
                for (String s : titles) {
                    //插入索引目录   为了判断其是否已存在
                    //根据name判断是否已存在
                    List<String> listId = urlIndexArticles.queryValueByCondition("indexName:" + s + " AND articleId:"+id, "id",10);
                    IndexAticles indexAticles;
                    if (listId != null && listId.size() > 0) {//存在该索引则进行替换
                        indexAticles = new IndexAticles(listId.get(0),s,id);
                    }else{//直接插入
                        indexAticles = new IndexAticles(UUID.randomUUID().toString(),s,id);
                    }
                    listIndexAticles.add(indexAticles);
                }
            //一次性插入全部的产品索引
        urlIndexArticles.add(listIndexAticles,IndexAticles.class);
            //一次性插入全部的产品
        urlArticles.add(articles, Articles.class);

    }

    //删帖 索引删除
    public void delSolrIndex(String id){
        try {
            urlArticles.delBycondition("id:"+id);//文章索引删除
            urlIndexArticles.delBycondition("articleId:"+id);//文章相关的索引id删除
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }






}
