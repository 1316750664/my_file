package solr.SolrDAO;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 * Created by hty070503 on 2014/10/24.
 */
public class SolrClient {

    private String SOLR_ADMIN_URL = null;
    private static HttpSolrServer server = null;
    private volatile static SolrClient solrClient = null;


    private SolrClient() {

    }

    public HttpSolrServer setUrlAndInit(String url) {
        this.SOLR_ADMIN_URL = url;
        return this.getServer();
    }


    /**
     * SolrClient 是线程安全的 需要采用单例模式
     * 此处实现方法适用于高频率调用查询
     *
     * @return SolrClient
     */
    public static SolrClient getInstance() {
        if (solrClient == null) {
            synchronized (SolrClient.class) {
                if (solrClient == null) {
                    solrClient = new SolrClient();
                }
            }
        }
        return solrClient;
    }


    /**
     * 初始化的HttpSolrServer 对象,并获取此唯一对象
     * 配置按需调整
     *
     * @return 此server对象
     */
    private HttpSolrServer getServer() {
        if (server == null) {
            server = new HttpSolrServer(SOLR_ADMIN_URL);
            server.setConnectionTimeout(3000);
            server.setDefaultMaxConnectionsPerHost(100);
            server.setMaxTotalConnections(100);
        }
        return server;

    }


}
