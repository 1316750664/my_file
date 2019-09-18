package com.bean;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;

/**
 * Created by hzm on 2014/12/23.
 */
public class OrderPayOrder implements Serializable {
    @NotNull
    private String payId;
    @NotNull
    private String userId;
    @NotNull
    private String outId;
    @NotNull
    private String orderIds;//用于合并支付，使用逗号分隔
    @NotNull
    private String orderNames;
    private double amount;
    private double balance;
    @NotNull
    private BankBean bankBean;

    public OrderPayOrder() {
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getOrderNames() {
        return orderNames;
    }

    public void setOrderNames(String orderNames) {
        this.orderNames = orderNames;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public BankBean getBankBean() {
        return bankBean;
    }

    public void setBankBean(BankBean bankBean) {
        this.bankBean = bankBean;
    }
}
