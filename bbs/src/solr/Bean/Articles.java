package solr.Bean;

/**
 * Created by hty070503 on 2015/4/1.
 */
public class Articles {
    private String id;
    private String name;

    public Articles(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
