package com.bean;

import java.io.Serializable;

/**
 * Created by hty070503 on 2014/11/3.
 */
public class ExpressBean  implements Serializable {

    private String streight_id;//模板id
    private Integer num = 1;//数量
    private Integer charge_type = 0;
    private double weight = 0.00;//重量
    private double volume = 0.00;//体积
    private double totalPrice = 0.00;//产品总价
    private double express_totalFee = 0.00;//总运费
    private double express_firstFee = -1;//快递首重
    private double express_Wight = 0.00;//快递首重
    private double express_addWight = 0.00;//续重
    private double express_addFee = 0.00;//续重
    private double EMS_firstFee = -1;//ems首重
    private double EMS_addFee = 0.00;//续重
    private double EMS_totalFee = 0.00;//总运费
    private double EMS_Wight = 0.00;//首重
    private double EMS_addWight = 0.00;//续重
    private double mail_firstFee = -1;//平邮首重
    private double mail_addFee = 0.00;//续重
    private double mail_totalFee = 0.00;//总运费
    private double mail_Wight = 0.00;//首重
    private double mail_addWight = 0.00;//续重

    public String getStreight_id() {
        return streight_id;
    }

    public void setStreight_id(String streight_id) {
        this.streight_id = streight_id;
    }

    public double getExpress_Wight() {
        return express_Wight;
    }

    public void setExpress_Wight(double express_Wight) {
        this.express_Wight = express_Wight;
    }

    public double getExpress_addWight() {
        return express_addWight;
    }

    public void setExpress_addWight(double express_addWight) {
        this.express_addWight = express_addWight;
    }

    public double getEMS_Wight() {
        return EMS_Wight;
    }

    public void setEMS_Wight(double EMS_Wight) {
        this.EMS_Wight = EMS_Wight;
    }

    public double getEMS_addWight() {
        return EMS_addWight;
    }

    public void setEMS_addWight(double EMS_addWight) {
        this.EMS_addWight = EMS_addWight;
    }

    public double getMail_Wight() {
        return mail_Wight;
    }

    public void setMail_Wight(double mail_Wight) {
        this.mail_Wight = mail_Wight;
    }

    public double getMail_addWight() {
        return mail_addWight;
    }

    public void setMail_addWight(double mail_addWight) {
        this.mail_addWight = mail_addWight;
    }

    public Integer getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(Integer charge_type) {
        this.charge_type = charge_type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getExpress_totalFee() {
        return express_totalFee;
    }

    public void setExpress_totalFee(double express_totalFee) {
        this.express_totalFee = express_totalFee;
    }

    public double getExpress_firstFee() {
        return express_firstFee;
    }

    public void setExpress_firstFee(double express_firstFee) {
        this.express_firstFee = express_firstFee;
    }

    public double getExpress_addFee() {
        return express_addFee;
    }

    public void setExpress_addFee(double express_addFee) {
        this.express_addFee = express_addFee;
    }

    public double getEMS_firstFee() {
        return EMS_firstFee;
    }

    public void setEMS_firstFee(double EMS_firstFee) {
        this.EMS_firstFee = EMS_firstFee;
    }

    public double getEMS_addFee() {
        return EMS_addFee;
    }

    public void setEMS_addFee(double EMS_addFee) {
        this.EMS_addFee = EMS_addFee;
    }

    public double getEMS_totalFee() {
        return EMS_totalFee;
    }

    public void setEMS_totalFee(double EMS_totalFee) {
        this.EMS_totalFee = EMS_totalFee;
    }

    public double getMail_firstFee() {
        return mail_firstFee;
    }

    public void setMail_firstFee(double mail_firstFee) {
        this.mail_firstFee = mail_firstFee;
    }

    public double getMail_addFee() {
        return mail_addFee;
    }

    public void setMail_addFee(double mail_addFee) {
        this.mail_addFee = mail_addFee;
    }

    public double getMail_totalFee() {
        return mail_totalFee;
    }

    public void setMail_totalFee(double mail_totalFee) {
        this.mail_totalFee = mail_totalFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressBean that = (ExpressBean) o;

        if (!streight_id.equals(that.streight_id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return streight_id.hashCode();
    }
}

