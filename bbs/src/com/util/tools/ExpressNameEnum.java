package com.util.tools;

/**
 * 快递名称
 * Created by bangong on 2015/3/3.
 */
public enum ExpressNameEnum {
    state0("huitongkuaidi", "百世汇通"), state1("ems", "EMS"), state2("debangwuliu", "德邦物流"),
    state3("huitongkuaidi", "汇通快运"), state4("quanfengkuaidi", "全峰快递"),state5("rufengda", "如风达快递"),
    state6("shunfeng","顺丰"),state7("wanxiangwuliu","万象物流"),state8("tiantian","天天快递"),state9("yuantong","圆通"),
    state10("yunda","韵达"),state11("youzhengguonei","邮政"),state12("youshuwuliu","优速物流"),state13("zhongtong","中通"),
    state14("zhaijisong","宅急送");
    private String validType;
    private String message;

    private ExpressNameEnum(String validType, String message) {
        this.validType = validType;
        this.message = message;
    }

    public static String getMessage(String validType) {
        for (ExpressNameEnum expressNameEnum : ExpressNameEnum.values()) {
            if (validType.equals(expressNameEnum.getValidType())) {
                return expressNameEnum.getMessage();
            }
        }
        return "未收录";
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
