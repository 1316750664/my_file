package com.bean;

import java.io.Serializable;

/**
 * Created by hzm on 2014/7/7.
 */
public class JsonCommandBean implements Serializable {
    private String taskId = null;
    private String cmdType = null;//数据库命令Query/Page/Update
    private String page = "1";//返回第几页
    private String row = "20";//返回第几页的多少行
    private String ide = null;
    private String sql = null;

    public JsonCommandBean() {
    }

    public JsonCommandBean(String taskId, String cmdType, String page, String row, String ide, String sql) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.page = page;
        this.row = row;
        this.ide = ide;
        this.sql = sql;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getIde() {
        return ide;
    }

    public void setIde(String ide) {
        this.ide = ide;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
