package com.bean;


import java.io.Serializable;

/**
 * Created by hty0705 on 2014/10/27.
 */
public class GoodsManage  implements Serializable {
    private String goodsname;
    private String bianma;
    private int parts_cateid;
    private int minp;
    private int maxp;
    private String parts_brand_id;
    private int minquant;
    private int maxquant;
    private String catename;


    private String order_id;
    private String nick;
    private int order_status = -1;
    private int pay_type = -1;
    private String order_time_min;
    private String order_time_max;
    private int hideStatus;
    private String status;

    private int global_score;
    private int detail_reviews;
    private int tj1;

    private int curPage = 1;
    private int pageSize = 20;
    private int smallPageSize = 5;
    private String goodsChecked;
    private int cate_id;

    public GoodsManage() {
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getBianma() {
        return bianma;
    }

    public void setBianma(String bianma) {
        this.bianma = bianma;
    }

    public int getParts_cateid() {
        return parts_cateid;
    }

    public void setParts_cateid(int parts_cateid) {
        this.parts_cateid = parts_cateid;
    }

    public int getMinp() {
        return minp;
    }

    public void setMinp(int minp) {
        this.minp = minp;
    }

    public int getMaxp() {
        return maxp;
    }

    public void setMaxp(int maxp) {
        this.maxp = maxp;
    }

    public String getParts_brand_id() {
        return parts_brand_id;
    }

    public void setParts_brand_id(String parts_brand_id) {
        this.parts_brand_id = parts_brand_id;
    }

    public int getMinquant() {
        return minquant;
    }

    public void setMinquant(int minquant) {
        this.minquant = minquant;
    }

    public int getMaxquant() {
        return maxquant;
    }

    public void setMaxquant(int maxquant) {
        this.maxquant = maxquant;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getHideStatus() {
        return hideStatus;
    }

    public void setHideStatus(int hideStatus) {
        this.hideStatus = hideStatus;
    }

    public String getOrder_time_min() {
        return order_time_min;
    }

    public void setOrder_time_min(String order_time_min) {
        this.order_time_min = order_time_min;
    }

    public String getOrder_time_max() {
        return order_time_max;
    }

    public void setOrder_time_max(String order_time_max) {
        this.order_time_max = order_time_max;
    }

    public int getGlobal_score() {
        return global_score;
    }

    public void setGlobal_score(int global_score) {
        this.global_score = global_score;
    }

    public int getDetail_reviews() {
        return detail_reviews;
    }

    public void setDetail_reviews(int detail_reviews) {
        this.detail_reviews = detail_reviews;
    }

    public int getTj1() {
        return tj1;
    }

    public void setTj1(int tj) {
        this.tj1 = tj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int row) {
        this.pageSize = row;
    }

    public int getSmallPageSize() {
        return smallPageSize;
    }

    public void setSmallPageSize(int smallPageSize) {
        this.smallPageSize = smallPageSize;
    }

    public String getGoodsChecked() {
        return goodsChecked;
    }

    public void setGoodsChecked(String goodsChecked) {
        this.goodsChecked = goodsChecked;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }
}
