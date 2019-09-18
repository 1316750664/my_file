package com.bean;

import java.io.Serializable;

/**
 * Created by hty070505 on 2014/12/11.
 */
public class BankBean implements Serializable {
    private int b_id;//系统银行编号
    private String b_name="";
    private String b_no = "";//支付银行卡号
    private int b_pay;//1个人网银2企业网银3汇付天下4银联在线(4暂未开通)
    private int b_bank;//接口银行代码
    private int b_clazz;//银行接口支付方式
    private int b_card = 99;//银行卡种类
    private int b_type;//交易类型1消费2销售3充值4提现
    private String b_IP = "";//购买人IP

    public BankBean() {
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_no() {
        return b_no;
    }

    public void setB_no(String b_no) {
        this.b_no = b_no;
    }

    public int getB_pay() {
        return b_pay;
    }

    public void setB_pay(int b_pay) {
        this.b_pay = b_pay;
    }

    public int getB_bank() {
        return b_bank;
    }

    public void setB_bank(int b_bank) {
        this.b_bank = b_bank;
    }

    public int getB_clazz() {
        return b_clazz;
    }

    public void setB_clazz(int b_clazz) {
        this.b_clazz = b_clazz;
    }

    public int getB_card() {
        return b_card;
    }

    public void setB_card(int b_card) {
        this.b_card = b_card;
    }

    public int getB_type() {
        return b_type;
    }

    public void setB_type(int b_type) {
        this.b_type = b_type;
    }

    public String getB_IP() {
        return b_IP;
    }

    public void setB_IP(String b_IP) {
        this.b_IP = b_IP;
    }
}
