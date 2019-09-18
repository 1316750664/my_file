package com.util.tools;

/**
 * Created by hzm on 2014/9/15.
 */
public enum MoneyMovementEnum {

    //开头：1总记录 2支出记录、收入记录 4退款记录 5充值记录 6提现记录 7退款记录之退款原因
    State10("10", "等待支付"), State11("11", "支付成功"), State12("12", "支付失败"), State13("13", "取消支付"),State14("14", "支付过期"),
    State20("20", "进行中"), State21("21", "等待付款"), State22("22", "等待发货"), State23("23", "等待确认收货"),State24("24", "退款"), State25("25", "成功"), State26("26", "失败"), State27("27", "退款等处理"),
    State28("28", "退至银行卡失败"),State29("29", "等待买家签收"), State210("210", "等待签收确认"), State211("211", "等待系统打款给卖家"), State212("212", "维权"),
    State40("40", "买家已申请退款等待卖家确认"), State41("41", "卖家已确认等待买家退货"), State42("42", "买家已退货等待卖家确认"),
    State43("43", "卖家已确认等待系统退款"), State44("44", "系统已退款"), State45("45", "卖家已驳回退款申请"),
    State46("46", "买家已撤消退款申请"), State47("47", "买家已申请退款仲裁"), State48("48", "退款仲裁已驳回"),
    State50("50", "待支付"), State51("51", "支付成功"), State52("52", "支付失败"),
    State60("60", "等待提现"), State61("61", "提现成功"), State62("62", "提现失败"),
    State70("70", "不想要了"), State71("71", "退运费"), State72("72", "收到商品破损"),
    State73("73", "商品错发/漏发"), State74("74", "商品需要维修"), State75("75", "发票问题"),
    State76("76", "收到商品与描述不符"), State77("77", "商品质量问题"), State78("78", "未按约定时间发货"),State79("79", "收到假货"),
    State1("1", "<td style='color:red'>-"), State2("2", "<td style='color:red'>+"), State3("3", "<td style='color:#0082cc'>+"),State4("4", "<td style='color:red'>-"), State5("5", "<td style='color:#0082cc'>+"), State6("6", "<td style='color:red'>-"),State7("7", "<td style='color:#0082cc'>+"), State8("8", "<td style='color:red'>-");
    private String validType;
    private String message;

    private MoneyMovementEnum(String validType, String message) {
        this.validType = validType;
        this.message = message;
    }

    public static String getMessage(String validType) {
        for (MoneyMovementEnum errorMsgEnum : MoneyMovementEnum.values()) {
            if (validType.equals(errorMsgEnum.getValidType())) {
                return errorMsgEnum.getMessage();
            }
        }
        return null;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
