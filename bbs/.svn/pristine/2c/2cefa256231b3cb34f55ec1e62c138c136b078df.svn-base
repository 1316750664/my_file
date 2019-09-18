package solr.Servlets;

import com.util.tools.CheckTools;
import com.util.tools.ReadWriteProperties;
import org.codehaus.jackson.map.ObjectMapper;
import solr.Bean.IndexAticles;
import solr.Bean.Page;
import solr.Bean.Users;
import solr.SolrDAO.SolrDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rcl on 2015/4/1
 */
@WebServlet(name = "SearchServlet.do", urlPatterns = {"/Action/SearchServlet.do"}, asyncSupported = true)
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setHeader("content-type", "text/html;charset=UTF-8");
            out = response.getWriter();

            String name = request.getParameter("name");
            String search_key = request.getParameter("search_key");
            if (CheckTools.isNull(search_key)) {
                search_key = "企商汇";
            }
            String type = request.getParameter("type");
            String q_type = request.getParameter("q_type");
            String urlIndexArticles = ReadWriteProperties.getInstance().readValue("solr", "urlIndexArticles");
            String urlIndexUsers = ReadWriteProperties.getInstance().readValue("solr", "urlIndexUsers");
            Page page = new Page();
            SolrDao indexArticles;
            SolrDao indexUsers;
            if(CheckTools.isNull(q_type)){
                q_type = "1";
            }

            if (type != null && type.equals("getIndex")) {
                //判断是用户查询还是帖子查询
                if("1".equals(q_type)){
                    indexArticles  = new SolrDao(urlIndexArticles);
                    Map<String,Object> conditions = new HashMap<String,Object>();
                    conditions.put("query_conditions","indexName:(" + name + "*)");
                    page.setConditions(conditions);
                    page.setRowCount(10);
                    page = indexArticles.query(IndexAticles.class,page);
                    if (page == null) {
                        page = new Page();
                        page.setJsonResult("未找到该产品");
                    }else{
                        //把同名的索引进行归类 去重复 合并索引号
                        List list = page.getListResult();
                        Map<String,String> ci_map = new HashMap<String, String>();
                        IndexAticles indexAticles;
                        for(Object o : list){
                            indexAticles = (IndexAticles)o;
                            String ci_name = indexAticles.getIndexName();
                            String ci_id = indexAticles.getArticleId();
                            if(ci_map.get(ci_name)==null){
                                ci_map.put(ci_name,ci_id);
                            }else{
                                ci_map.put(ci_name,ci_map.get(ci_name)+","+ci_id);
                            }
                        }
                        String json = new ObjectMapper().writeValueAsString(ci_map);
                        page.setJsonResult(json);
                    }
                }else {
                     indexUsers = new SolrDao(urlIndexUsers);
                    Map<String, Object> conditions = new HashMap<String, Object>();
                    conditions.put("query_conditions", "name:(" + name + "*)");
                    page.setConditions(conditions);
                    page.setRowCount(10);
                    page = indexUsers.query(Users.class, page);
                    if (page == null) {
                        page = new Page();
                        page.setJsonResult("未找到该产品");
                    } else {
                        //把同名的索引进行归类 去重复 合并索引号
                        List list = page.getListResult();
                        Map<String, String> ci_map = new HashMap<String, String>();
                        Users user;
                        for (Object o : list) {
                            user = (Users) o;
                            String ci_name = user.getName();
                            String ci_id = user.getId();
                            if (ci_map.get(ci_name) == null) {
                                ci_map.put(ci_name, ci_id);
                            } else {
                                ci_map.put(ci_name, ci_map.get(ci_name) + "," + ci_id);
                            }
                        }
                        String json = null;
                        try {
                            json = new ObjectMapper().writeValueAsString(ci_map);
                        } catch (IOException e) {
                            page.setJsonResult("未找到该产品");
                        }
                        page.setJsonResult(json);
                    }
                }
                out.write(page.getJsonResult());
                out.flush();
            } else {
                request.setAttribute("s_type",q_type);//吧查询的种类传给结果页
                if("请输入您要搜索的名称".equals(search_key) || "".equals(search_key)){
                    request.setAttribute("articleIds", null);
                    request.getRequestDispatcher("/sousuo/search_result.jsp").forward(request, response);
                }
                //搜索词过滤
                search_key = search_key.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]+","");//过滤出中文 字母 数字


                String indexid = request.getParameter("indexid");
                //搜索词访问量增加
                /*String url_Count = ReadWriteProperties.getInstance().readValue("solr", "url_Count");
                SolrDao count_solr = new SolrDao(url_Count);
                count_solr.countWord(search_key,indexid);*/


                //索引快速查询
                if (indexid != null && !"".equals(indexid)  ) {
                        request.setAttribute("articleIds", indexid);
                } else {
                    List<String> ids;
                    if("1".equals(q_type)) {
                        //先直接进行全字匹配
                        String url_Articles = ReadWriteProperties.getInstance().readValue("solr", "urlArticles");
                        SolrDao urlArticles = new SolrDao(url_Articles);
                        //直接查整个词
                        ids = urlArticles.queryValueByCondition("name:(" + search_key + ") OR name:(*" + search_key + "*)", "id", 1000);
                    }else{
                        SolrDao urlUsers = new SolrDao(urlIndexUsers);
                        //直接查整个词
                        ids = urlUsers.queryValueByCondition("name:(" + search_key + ") OR name:(*" + search_key + "*)", "id", 1000);

                    }
                    String articles = "0";
                    if(ids!=null && ids.size()>0){
                        articles = turnListToString(ids);
                    }
                    request.setAttribute("articleIds", articles);

                }
                request.setAttribute("key_w", search_key);
                request.getRequestDispatcher("/sousuo/search_result.jsp").forward(request, response);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String turnListToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (list.size() <= 0)
            return null;
        for (String s : list) {
            sb.append(s + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }




}
