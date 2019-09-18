package com.bean;

import java.io.Serializable;

/**
 * Created by hty0705 on 2014/10/11.
 */
public class PartsCarBean implements Serializable {
    private int parts_cate_parent = 0;
    private int curPage = 1;
    private String parts_cate_name;
    private String brandstr;

    private String parts_brand_id;
    private String doublecheck;

    private int brand_id;
    private String brand_name = "";
    private String brand_pys;

    private int car_model_id;
    private String car_model_name;
    private String manufacture_year;
    private String car_conf_name;
    private String car_conf_id;

    private String mfg_no;
    private int show = 0;//0全部，1部分
    private int listshow;//0,列表；1,图片

    private int provinceid;
    private int zonghe;
    private int renqi;
    private int xinpin;
    private int xil;
    private int price;

    private String goods;
    private String sgoods;
    private String mfgno;
    private int startp;
    private int endp;
    private int rank = 0;
    private int forall = 0;

    private String search_id;//导航搜索
    private String key_w;//导航搜索


    private String parts_cate_ids;
    private String together;

    //汽车用品相关
    private int parts_cate_parents=270000;
    private int parts_cate_id;
    private String hashcode;
    private String attrname;
    private String attrid;
    private String propername;


    public PartsCarBean() {
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getParts_cate_parent() {
        return parts_cate_parent;
    }

    public void setParts_cate_parent(int parts_cate_parent) {
        this.parts_cate_parent = parts_cate_parent;
    }

    public String getParts_cate_name() {
        return parts_cate_name;
    }

    public void setParts_cate_name(String parts_cate_name) {
        this.parts_cate_name = parts_cate_name;
    }

    public String getParts_brand_id() {
        return parts_brand_id;
    }

    public String getBrandstr() {
        return brandstr;
    }

    public void setBrandstr(String brandstr) {
        this.brandstr = brandstr;
    }

    public void setParts_brand_id(String parts_brand_id) {
        this.parts_brand_id = parts_brand_id;
    }

    public String getDoublecheck() {
        return doublecheck;
    }

    public void setDoublecheck(String doublecheck) {
        this.doublecheck = doublecheck;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_pys() {
        return brand_pys;
    }

    public void setBrand_pys(String brand_pys) {
        this.brand_pys = brand_pys;
    }

    public int getCar_model_id() {
        return car_model_id;
    }

    public void setCar_model_id(int car_model_id) {
        this.car_model_id = car_model_id;
    }

    public String getCar_model_name() {
        return car_model_name;
    }

    public void setCar_model_name(String car_model_name) {
        this.car_model_name = car_model_name;
    }

    public String getManufacture_year() {
        return manufacture_year;
    }

    public void setManufacture_year(String manufacture_year) {
        this.manufacture_year = manufacture_year;
    }

    public String getCar_conf_name() {
        return car_conf_name;
    }

    public void setCar_conf_name(String car_conf_name) {
        this.car_conf_name = car_conf_name;
    }

    public String getCar_conf_id() {
        return car_conf_id;
    }

    public void setCar_conf_id(String car_conf_id) {
        this.car_conf_id = car_conf_id;
    }

    public String getMfg_no() {
        return mfg_no;
    }

    public void setMfg_no(String mfg_no) {
        this.mfg_no = mfg_no;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getListshow() {
        return listshow;
    }

    public void setListshow(int listshow) {
        this.listshow = listshow;
    }

    public int getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(int provinceid) {
        this.provinceid = provinceid;
    }

    public int getZonghe() {
        return zonghe;
    }

    public void setZonghe(int zonghe) {
        this.zonghe = zonghe;
    }

    public int getRenqi() {
        return renqi;
    }

    public void setRenqi(int renqi) {
        this.renqi = renqi;
    }

    public int getXinpin() {
        return xinpin;
    }

    public void setXinpin(int xinpin) {
        this.xinpin = xinpin;
    }

    public int getXil() {
        return xil;
    }

    public void setXil(int xil) {
        this.xil = xil;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getSgoods() {
        return sgoods;
    }

    public void setSgoods(String sgoods) {
        this.sgoods = sgoods;
    }

    public String getMfgno() {
        return mfgno;
    }

    public void setMfgno(String mfgno) {
        this.mfgno = mfgno;
    }

    public int getStartp() {
        return startp;
    }

    public void setStartp(int startp) {
        this.startp = startp;
    }

    public int getEndp() {
        return endp;
    }

    public void setEndp(int endp) {
        this.endp = endp;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getForall() {
        return forall;
    }

    public void setForall(int forall) {
        this.forall = forall;
    }

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }

    public String getKey_w() {
        return key_w;
    }

    public void setKey_w(String key_w) {
        this.key_w = key_w;
    }

    public int getParts_cate_parents() {
        return parts_cate_parents;
    }

    public void setParts_cate_parents(int parts_cate_parents) {
        this.parts_cate_parents = parts_cate_parents;
    }

    public String getParts_cate_ids() {
        return parts_cate_ids;
    }

    public void setParts_cate_ids(String parts_cate_ids) {
        this.parts_cate_ids = parts_cate_ids;
    }

    public String getTogether() {
        return together;
    }

    public void setTogether(String together) {
        this.together = together;
    }

    public int getParts_cate_id() {
        return parts_cate_id;
    }

    public void setParts_cate_id(int parts_cate_id) {
        this.parts_cate_id = parts_cate_id;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrid() {
        return attrid;
    }

    public void setAttrid(String attrid) {
        this.attrid = attrid;
    }

    public String getPropername() {
        return propername;
    }

    public void setPropername(String propername) {
        this.propername = propername;
    }
}
