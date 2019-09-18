package com.bean;

import java.io.Serializable;

/**
 * Created by xiayonwei on 2014/9/9.
 * 汽车品牌Bean
 */
public class CarBrandBean implements Serializable {
    private String brand_id = "";
    private String brand_name = "";
    private String brand_py = "";
    private String uri = "";
    private Boolean clickFlg = false;

    public Boolean getClickFlg() {
        return clickFlg;
    }

    public void setClickFlg(Boolean clickFlg) {
        this.clickFlg = clickFlg;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_py() {
        return brand_py;
    }

    public void setBrand_py(String brand_py) {
        this.brand_py = brand_py;
    }
}
