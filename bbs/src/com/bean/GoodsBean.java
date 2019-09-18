package com.bean;

import java.io.Serializable;

/**
 * Created by hty070503 on 2014/12/5.
 */
public class GoodsBean  implements Serializable {


    private String order_id;
    private String order_item;
    private String goods_id;
    private String goods_name;
    private String user_id;
    private String user_id_buyer;
    private String order_time;
    private int credits;
    private int user_type;
    private String weight;
    private String volume;
    private String goods_num = "1";
    private String goods_main_img;
    private String property_ids;
    private String property_json;
    private String property_code;
    private String freight_id;
    private String is_discount = "0";
    private String sale_price;
    private String discount_price = "0.00";
    private String promotion_id;
    private double cost_amounts = 0.00;
    private double amounts = 0.00;
    private double profit_amounts = 0.00;
    private String snapshot;
    private int snap_status;//是否需要生成快照 1是0否
    private String price1;
    private String price2;
    private String price3;
    private String price4;
    private String price5;
    private int min_num_3;
    private int min_num_4;
    private int min_num_5;
    private String store_num;

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {

        this.user_type = user_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getFreight_id() {
        return freight_id;
    }

    public void setFreight_id(String freight_id) {

        if(freight_id==null || "".equals(freight_id)){
            freight_id = "0";
        }

        this.freight_id = freight_id;
    }

    public int getSnap_status() {
        return snap_status;
    }

    public void setSnap_status(int snap_status) {
        this.snap_status = snap_status;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStore_num() {
        return store_num;
    }

    public void setStore_num(String store_num) {
        this.store_num = store_num;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }


    public String getOrder_item() {
        return order_item;
    }

    public void setOrder_item(String order_item) {
        this.order_item = order_item;
    }

    public String getPrice4() {
        return price4;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_priceByutype(int utype) {
        //现在根据起订量计算
        String pric = "0.00";
        switch (utype){
            case 3:
                pric = this.price3;
                break;
            case 4:
                pric = this.price4;
                break;
            case 5:
                pric = this.price5;
                break;
        }


    }
    public void setDiscount_price(String discount_price) {

        this.discount_price = discount_price;
    }

    public void setPrice4(String price4) {
        this.price4 = price4;
    }

    public String getPrice5() {
        return price5;
    }

    public void setPrice5(String price5) {
        this.price5 = price5;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        int num = Integer.parseInt(goods_num);

        if(num>=min_num_5 && user_type==5){
            discount_price = price5;
        }else if(num>=min_num_4 && num<min_num_5 && user_type>=4){
            discount_price = price4;
        }else{
            discount_price = price3;
        }

        sale_price = discount_price;

        this.goods_num = goods_num;
    }

    public int getMin_num_3() {
        return min_num_3;
    }

    public void setMin_num_3(int min_num_3) {
        this.min_num_3 = min_num_3;
    }

    public int getMin_num_4() {
        return min_num_4;
    }

    public void setMin_num_4(int min_num_4) {
        this.min_num_4 = min_num_4;
    }

    public int getMin_num_5() {
        return min_num_5;
    }

    public void setMin_num_5(int min_num_5) {
        this.min_num_5 = min_num_5;
    }

    public String getGoods_main_img() {
        return goods_main_img;
    }

    public void setGoods_main_img(String goods_main_img) {
        this.goods_main_img = goods_main_img;
    }

    public String getProperty_ids() {
        return property_ids;
    }

    public void setProperty_ids(String property_ids) {
        this.property_ids = property_ids;
    }

    public String getProperty_json() {
        return property_json;
    }

    public void setProperty_json(String property_json) {
        this.property_json = property_json;
    }

    public String getProperty_code() {
        return property_code;
    }

    public void setProperty_code(String property_code) {
        this.property_code = property_code;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getIs_discount() {
        return is_discount;
    }

    public void setIs_discount(String is_discount) {
        this.is_discount = is_discount;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;

    }

    public void calculation() {
        //进行小计
        this.setCost_amounts();
        this.setAmounts();
        this.setProfit_amounts();
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public double getCost_amounts() {
        return cost_amounts;
    }

    public void setCost_amounts() {
        this.cost_amounts = Double.parseDouble(price1) * Double.parseDouble(goods_num);

    }

    public double getAmounts() {
        return amounts;
    }

    public void setAmounts() {
        this.amounts = Double.parseDouble(discount_price) * Double.parseDouble(goods_num);
    }

    public double getProfit_amounts() {
        return profit_amounts;
    }

    public void setProfit_amounts() {
        this.profit_amounts = amounts - cost_amounts;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
