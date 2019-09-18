package solr.Bean;

import java.io.Serializable;

/**
 * Created by hty070503 on 2015/1/7.
 */
public class WordCount implements Serializable {
    private String name;
    private int wordCount;
    private String indexid = "0";

    public WordCount(String name, int wordCount,String indexid) {
        this.name = name;
        this.wordCount = wordCount;
        this.indexid = indexid;
    }

    public WordCount(String name, int wordCount) {
        this.name = name;
        this.wordCount = wordCount;
    }

    public WordCount(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  /*  public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }*/

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getIndexid() {
        return indexid;
    }

    public void setIndexid(String indexid) {
        this.indexid = indexid;
    }
}
