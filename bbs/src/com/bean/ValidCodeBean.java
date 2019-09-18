package com.bean;

/**
 * Created by hzm on 2014/12/2.
 */
public class ValidCodeBean {
    private int type;
    private long expiry;
    private String code;

    public ValidCodeBean() {
    }

    public ValidCodeBean(int type, long expiry, String code) {
        this.type = type;
        this.expiry = expiry;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
