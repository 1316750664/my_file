package com.bean;

import java.io.Serializable;

/**
 * Created by hzm on 2015/4/18.
 */
public class FileCacheBean implements Serializable {
    private int fileCode;//文件名的hashCode
    private long lastTime;//文件修改时间
    private String content;//文件内容

    public FileCacheBean() {
    }

    public FileCacheBean(int fileCode, long lastTime, String content) {
        this.fileCode = fileCode;
        this.lastTime = lastTime;
        this.content = content;
    }

    public int getFileCode() {
        return fileCode;
    }

    public void setFileCode(int fileCode) {
        this.fileCode = fileCode;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}