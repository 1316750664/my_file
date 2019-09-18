package com.util.tools;

/**
 * Created by rcl on 2015/9/9.
 */
public enum StatuCode {
//活动
    ACTIVE_TIMEOUT(101,"活动过期"),ACTIVE_NOT_EXIST(102,"活动不存在"),
//成功
    SUCCESS(201,"成功"),NO_AVAILABLE_INFO(202,"查询无可用结果"),

    SYSTEM_ERROR(301,"系统异常"),CODE_ERROR(302,"代码运行出错"),
//用户
    USER_NOT_EXIST(401,"用户不存在"),USER_TYPE_NOT_SATISFIED(402,"用户类型不满足活动条件"),
    USER_UNLOGIN(403,"未登录"),NO_USER_LOGIN_NAME(404,"请输入用户名"),NO_USER_PASSWORD(405,"请输入密码"),
    USER_LOCK(406,"用户已锁定，请联系管理员"),
//产品
    GOODS_NOT_EXIST(501,"产品不存在"),GOODS_ERROR(502,"产品有误"),GOODS_TIMEOUT(503,"产品已过期"),CART_IS_NULL(504,"购物车是空的"),
//订单
    ORDER_UNKNOWN(601,"未知订单类型"),ORDER_PARA_ERROR(602,"订单参数有误"),DELIVERY_ERROR(603,"运费异常"),GOODS_NOT_ENOUGH(604,"库存不足"),
//保养
    MAIN_NOMATER(630,"工时所需材料查询失败"),MAIN_NOGOOD(631,"工时所需产品未能匹配到"),

//参数
    PARAMETER_ERR0(700,"有参数为空"),PARAMETER_ERR1(701,"参数类型错误"),PARAMETER_ERR2(702,"参数不可用"),
    PARAMETER_ERR3(703,"参数校验不通过"),PWD_ERR(704,"两次密码不同"),
    //发票
    INVOICE_NOT_EXIST(800,"发票不存在"),
    //发送
    SEND_SUCCESS(900,"发送成功"),SEND_FAILED(901,"发送失败"),SEND_REPEAT(902,"请勿重复发送"),
    //验证码
    VALID_CODE0(10,"验证码错误"),VALID_CODE1(11,"验证码正确"),CODE_BEFORE(12,"请先发送验证码")
    ;



    private int statusCode;
    private String statusMsg;


    StatuCode(int statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public static String getMessage(int code) {
        for (StatuCode stat : StatuCode.values()) {
            if (code == stat.getStatusCode()) {
                return stat.getStatusMsg();
            }
        }
        return "返回状态未定义";
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

}
