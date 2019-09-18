package solr.Bean;

import java.util.List;
import java.util.Map;

/**
 * Created by hty070503 on 2014/8/29.
 */
public class Page {

    private int pageSize = 10;
    private int pageNow = 1;
    private int pageCount;
    private int rowCount;

    private Map<String, Object> conditions;

    private List<Object> listResult;

    private String JsonResult = "";

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageCount() {
        return rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;
    }


    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    //从第几条开始
    public int getStart() {
        return (pageNow - 1) * pageSize;
    }


    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public List<Object> getListResult() {
        return listResult;
    }

    public void setListResult(List<Object> listResult) {
        this.listResult = listResult;
    }

    public String getJsonResult() {
        return JsonResult;
    }

    public void setJsonResult(String jsonResult) {
        JsonResult = jsonResult;
    }
}
