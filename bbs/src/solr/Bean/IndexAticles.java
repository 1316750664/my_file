package solr.Bean;

/**
 * Created by hty070503 on 2015/4/1.
 */
public class IndexAticles {

    private String id;
    private String indexName;
    private String articleId;

    public IndexAticles(String id, String indexName, String articleId) {
        this.id = id;
        this.indexName = indexName;
        this.articleId = articleId;
    }

    public IndexAticles(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
