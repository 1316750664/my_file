package com.bean;

import java.io.Serializable;

/**
 * Created by hzm on 2014/7/28.
 */
public class AyncCacheBean implements Serializable {
    private String taskId;
    private String sqlKey;
    private String sqlData;

    public AyncCacheBean() {
    }

    public AyncCacheBean(String taskId, String sqlKey, String sqlData) {
        this.taskId = taskId;
        this.sqlKey = sqlKey;
        this.sqlData = sqlData;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSqlKey() {
        return sqlKey;
    }

    public void setSqlKey(String sqlKey) {
        this.sqlKey = sqlKey;
    }

    public String getSqlData() {
        return sqlData;
    }

    public void setSqlData(String sqlData) {
        this.sqlData = sqlData;
    }
}
