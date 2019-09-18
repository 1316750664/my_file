package com.bean;

import java.io.Serializable;

/**
 * Created by hty070505 on 2014/12/22.
 */
public class GoodsCateIdBean implements Serializable {
    private long goods_id;
    private String goods_name;
    private short goods_name_sp;
    private String goods_name_py;
    private String parts_cate_name;
    private int parts_cate_id;
    private String brand_name;
    private int brand_id;
    private String car_brand_name;
    private String car_brand_ids;
    private String car_model_name;
    private String car_model_ids;
    private String car_conf_name;
    private String car_conf_ids;

    public GoodsCateIdBean() {
    }

    public long getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(long goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public short getGoods_name_sp() {
        return goods_name_sp;
    }

    public void setGoods_name_sp(short goods_name_sp) {
        this.goods_name_sp = goods_name_sp;
    }

    public String getGoods_name_py() {
        return goods_name_py;
    }

    public void setGoods_name_py(String goods_name_py) {
        this.goods_name_py = goods_name_py;
    }

    public String getParts_cate_name() {
        return parts_cate_name;
    }

    public void setParts_cate_name(String parts_cate_name) {
        this.parts_cate_name = parts_cate_name;
    }

    public int getParts_cate_id() {
        return parts_cate_id;
    }

    public void setParts_cate_id(int parts_cate_id) {
        this.parts_cate_id = parts_cate_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getCar_brand_name() {
        return car_brand_name;
    }

    public void setCar_brand_name(String car_brand_name) {
        this.car_brand_name = car_brand_name;
    }

    public String getCar_brand_ids() {
        return car_brand_ids;
    }

    public void setCar_brand_ids(String car_brand_ids) {
        this.car_brand_ids = car_brand_ids;
    }

    public String getCar_model_name() {
        return car_model_name;
    }

    public void setCar_model_name(String car_model_name) {
        this.car_model_name = car_model_name;
    }

    public String getCar_model_ids() {
        return car_model_ids;
    }

    public void setCar_model_ids(String car_model_ids) {
        this.car_model_ids = car_model_ids;
    }

    public String getCar_conf_name() {
        return car_conf_name;
    }

    public void setCar_conf_name(String car_conf_name) {
        this.car_conf_name = car_conf_name;
    }

    public String getCar_conf_ids() {
        return car_conf_ids;
    }

    public void setCar_conf_ids(String car_conf_ids) {
        this.car_conf_ids = car_conf_ids;
    }
}
