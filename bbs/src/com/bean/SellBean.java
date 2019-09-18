package com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hty070503 on 2014/12/5.
 */
public class SellBean implements Serializable {

    private String order_id;
    private String user_id_buyer;
    private String order_time;
    private int buy_user_type;
    private String user_id_supplier;
    private String is_seller_reviews;
    private double goods_rec_amount = 0.00;
    private String goods_extra_amount;
    private String delivery_type;
    private String freight_rec_amount;
    private String freight_extra_amount;
    private String promotion_id = "0";
    private String promotion_amount = "0";
    private double order_amount = 0.00;
    private String is_insurance;
    private String insurance_amount;
    private String order_remark;
    private String shippment_time;
    private String shippment_ip;
    private String express_company;
    private String express_no;
    private String express_num;
    private String express_remark;
    private String cityid;

    private HashSet<GoodsBean> hs = new HashSet<GoodsBean>();
    private List<GoodsBean> listGoods = new ArrayList<GoodsBean>();


    public SellBean() {

    }

    public SellBean(String order_id, String user_id_buyer, String order_time, String cityid, String uid) {
        this.order_id = order_id;
        this.user_id_buyer = user_id_buyer;
        this.order_time = order_time;
        this.cityid = cityid;
        this.user_id_supplier = uid;
    }

    public void calculation() {
        //计算总费用
        for (GoodsBean goodsBean : listGoods) {
            goods_rec_amount += goodsBean.getAmounts();
        }
        order_amount = goods_rec_amount + Double.parseDouble(freight_rec_amount) + Double.parseDouble(insurance_amount);
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public int addGood(GoodsBean goodsBean) {
        goodsBean.setOrder_id(order_id);
        listGoods.add(goodsBean);
        return 1;

    }

    public List<GoodsBean> getListGoods() {
        return listGoods;
    }

    public int getBuy_user_type() {
        return buy_user_type;
    }

    public void setBuy_user_type(int buy_user_type) {
        this.buy_user_type = buy_user_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id_buyer() {
        return user_id_buyer;
    }

    public void setUser_id_buyer(String user_id_buyer) {
        this.user_id_buyer = user_id_buyer;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getUser_id_supplier() {
        return user_id_supplier;
    }

    public void setUser_id_supplier(String user_id_supplier) {
        this.user_id_supplier = user_id_supplier;
    }

    public String getIs_seller_reviews() {
        return is_seller_reviews;
    }

    public void setIs_seller_reviews(String is_seller_reviews) {
        this.is_seller_reviews = is_seller_reviews;
    }


    public String getGoods_extra_amount() {
        return goods_extra_amount;
    }

    public void setGoods_extra_amount(String goods_extra_amount) {
        this.goods_extra_amount = goods_extra_amount;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getFreight_rec_amount() {
        return freight_rec_amount;
    }

    public void setFreight_rec_amount(String freight_rec_amount) {
        this.freight_rec_amount = freight_rec_amount;
    }

    public String getFreight_extra_amount() {
        return freight_extra_amount;
    }

    public void setFreight_extra_amount(String freight_extra_amount) {
        this.freight_extra_amount = freight_extra_amount;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getPromotion_amount() {
        return promotion_amount;
    }

    public void setPromotion_amount(String promotion_amount) {
        this.promotion_amount = promotion_amount;
    }

    public double getGoods_rec_amount() {
        return goods_rec_amount;
    }

    public void setGoods_rec_amount(double goods_rec_amount) {
        this.goods_rec_amount = goods_rec_amount;
    }

    public double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(double order_amount) {
        this.order_amount = order_amount;
    }

    public String getIs_insurance() {
        return is_insurance;
    }

    public void setIs_insurance(String is_insurance) {
        this.is_insurance = is_insurance;
    }

    public String getInsurance_amount() {
        return insurance_amount;
    }

    public void setInsurance_amount(String insurance_amount) {
        this.insurance_amount = insurance_amount;
    }

    public String getOrder_remark() {
        return order_remark;
    }

    public void setOrder_remark(String order_remark) {
        this.order_remark = order_remark;
    }

    public String getShippment_time() {
        return shippment_time;
    }

    public void setShippment_time(String shippment_time) {
        this.shippment_time = shippment_time;
    }

    public String getShippment_ip() {
        return shippment_ip;
    }

    public void setShippment_ip(String shippment_ip) {
        this.shippment_ip = shippment_ip;
    }

    public String getExpress_company() {
        return express_company;
    }

    public void setExpress_company(String express_company) {
        this.express_company = express_company;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

    public String getExpress_num() {
        return express_num;
    }

    public void setExpress_num(String express_num) {
        this.express_num = express_num;
    }

    public String getExpress_remark() {
        return express_remark;
    }

    public void setExpress_remark(String express_remark) {
        this.express_remark = express_remark;
    }

}
