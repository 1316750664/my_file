package com.bean;

import java.io.Serializable;

/**
 * Created by hzm on 2014/9/18.
 */
public class TaskBean implements Serializable {
    private int taskId;
    private int cmdType;//0查询1小分页2更新3数据库大分页
    private int method = 0;//默认使用？参数方式，1@@
    private int page;//当前页
    private int row;//页大小
    private String[] paramValues;
    private String[] paramTypes;
    private String[] keyColumns;

    public TaskBean() {
    }

    public TaskBean(int taskId, int cmdType, int page, int row, String[] paramValues) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.page = page;
        this.row = row;
        this.paramValues = paramValues;
    }

    public TaskBean(int taskId, int cmdType, int method, int page, int row, String[] paramValues) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.method = method;
        this.page = page;
        this.row = row;
        this.paramValues = paramValues;
    }

    public TaskBean(int taskId, int cmdType, int page, int row, String[] paramValues, String[] paramTypes) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.page = page;
        this.row = row;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
    }

    public TaskBean(int taskId, int cmdType, int method, int page, int row, String[] paramValues, String[] paramTypes) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.method = method;
        this.page = page;
        this.row = row;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
    }

    public TaskBean(int taskId, int cmdType, String[] paramValues) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.paramValues = paramValues;
    }

    public TaskBean(int taskId, int cmdType, int method, String[] paramValues) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.method = method;
        this.paramValues = paramValues;
    }

    public TaskBean(int taskId, int cmdType, String[] paramValues, String[] paramTypes) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
    }

    public TaskBean(int taskId, int cmdType, int method, String[] paramValues, String[] paramTypes) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.method = method;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
    }

    public TaskBean(int taskId, String[] paramValues) {
        this.taskId = taskId;
        this.paramValues = paramValues;
    }

    public TaskBean(int taskId, String[] paramValues, String[] paramTypes, String[] keyColumns) {
        this.taskId = taskId;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
        this.keyColumns = keyColumns;
    }

    public TaskBean(int taskId, String[] paramValues, String[] paramTypes) {
        this.taskId = taskId;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
    }

    public TaskBean(int taskId, int method, String[] paramValues, String[] paramTypes, String[] keyColumns) {
        this.taskId = taskId;
        this.method = method;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
        this.keyColumns = keyColumns;
    }

    public TaskBean(int taskId, int cmdType, int method, String[] paramValues, String[] paramTypes, String[] keyColumns) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.method = method;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;
        this.keyColumns = keyColumns;
    }

    public TaskBean(int taskId, int cmdType) {
        this.taskId = taskId;
        this.cmdType = cmdType;
    }

    public TaskBean(int taskId, int cmdType, int method) {
        this.taskId = taskId;
        this.cmdType = cmdType;
        this.method = method;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(String[] paramValues) {
        this.paramValues = paramValues;
    }

    public String[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(String[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String[] getKeyColumns() {
        return keyColumns;
    }

    public void setKeyColumns(String[] keyColumns) {
        this.keyColumns = keyColumns;
    }
}
