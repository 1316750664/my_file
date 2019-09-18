package com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hty070503 on 2014/12/5.
 */
public class TotalOrderBean implements Serializable {

    private String order_id;
    private String order_status;
    private String is_buyer_reviews;
    private String is_seller_reviews;
    private String user_id;
    private String user_nick;
    private int user_type;
    private String is_anony;
    private String order_time;
    private String order_refer;
    private String refer_web;
    private double order_amount = 0.00;
    private double coupon_amount = 0.00;
    private double credits_amount = 0.00;
    private double amounts = 0.00;
    private String is_invoice;
    private String invoice_id;
    private String receiver;
    private String phone;
    private String address;
    private String pay_time;
    private String pay_user_id;
    private String pay_type;
    private String pay_bank;
    private String pay_sn;
    private String cityid;

    private List<SellBean> sell = new ArrayList<SellBean>();

    public TotalOrderBean() {

    }

    public TotalOrderBean(String order_id, String user_id, String is_anony, String user_nick, int user_type, String order_time, String is_invoice, String invoice_id, String receiver, String phone, String address, String pay_type, String cityid) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.is_anony = is_anony;
        this.user_nick = user_nick;
        this.user_type = user_type;
        this.order_time = order_time;
        this.is_invoice = is_invoice;
        this.invoice_id = invoice_id;
        this.receiver = receiver;
        this.phone = phone;
        this.address = address;
        this.pay_type = pay_type;
        this.cityid = cityid;
    }

    public TotalOrderBean(String order_id, String user_id, String order_time, String cityid) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.order_time = order_time;
        this.cityid = cityid;
    }

    public List<SellBean> getSell() {
        return sell;
    }

    public void calculation() {
        for (SellBean sellBean : sell) {
            order_amount += sellBean.getOrder_amount();
        }
        amounts = order_amount - coupon_amount - credits_amount;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public void setSell(List<GoodsBean> listGoods) {
        sell = new ArrayList<SellBean>();

        for (GoodsBean goodsBean : listGoods) {
            goodsBean.setUser_id_buyer(user_id);
            goodsBean.setOrder_time(order_time);

            if (!contain(goodsBean)) {
                SellBean sellBean = new SellBean(order_id, user_id, order_time, cityid, goodsBean.getUser_id());
                sellBean.setBuy_user_type(user_type);
                sellBean.addGood(goodsBean);
                sell.add(sellBean);
            }

        }
    }

    public boolean contain(GoodsBean goodsBean) {
        String id = goodsBean.getUser_id();
        if (sell.size() == 0) {
            SellBean sellBean = new SellBean(order_id, user_id, order_time, cityid, id);
            sellBean.setBuy_user_type(user_type);

            sellBean.addGood(goodsBean);
            sell.add(sellBean);
            return true;
        } else {
            for (SellBean s : sell) {
                if (s.getUser_id_supplier().equals(id)) {
                    s.addGood(goodsBean);
                    return true;
                }
            }
        }
        return false;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getIs_buyer_reviews() {
        return is_buyer_reviews;
    }

    public void setIs_buyer_reviews(String is_buyer_reviews) {
        this.is_buyer_reviews = is_buyer_reviews;
    }

    public String getIs_seller_reviews() {
        return is_seller_reviews;
    }

    public void setIs_seller_reviews(String is_seller_reviews) {
        this.is_seller_reviews = is_seller_reviews;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getIs_anony() {
        return is_anony;
    }

    public void setIs_anony(String is_anony) {
        this.is_anony = is_anony;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_refer() {
        return order_refer;
    }

    public void setOrder_refer(String order_refer) {
        this.order_refer = order_refer;
    }

    public String getRefer_web() {
        return refer_web;
    }

    public void setRefer_web(String refer_web) {
        this.refer_web = refer_web;
    }

    public double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(double order_amount) {
        this.order_amount = order_amount;
    }

    public double getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(double coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public double getCredits_amount() {
        return credits_amount;
    }

    public void setCredits_amount(double credits_amount) {
        this.credits_amount = credits_amount;
    }

    public double getAmounts() {
        return amounts;
    }

    public void setAmounts(double amounts) {
        this.amounts = amounts;
    }

    public String getIs_invoice() {
        return is_invoice;
    }

    public void setIs_invoice(String is_invoice) {
        this.is_invoice = is_invoice;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPay_user_id() {
        return pay_user_id;
    }

    public void setPay_user_id(String pay_user_id) {
        this.pay_user_id = pay_user_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_bank() {
        return pay_bank;
    }

    public void setPay_bank(String pay_bank) {
        this.pay_bank = pay_bank;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }
}
