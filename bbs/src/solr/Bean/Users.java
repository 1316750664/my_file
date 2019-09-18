package solr.Bean;

/**
 * Created by hty070503 on 2015/4/6.
 */
public class Users {
    private String id;
    private String name;

    public Users(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Users(){}

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
