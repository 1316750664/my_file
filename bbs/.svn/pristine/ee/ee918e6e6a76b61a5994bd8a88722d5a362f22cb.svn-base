package com.util.tools;

/**
 * Created by bangong on 2015/3/2.
 */
public enum ExpressStateEnum {
    state0("0", "在途"), state1("1", "揽件"), state2("2", "疑难"),
    state3("3", "签收"), state4("4", "退签"),state5("5", "派件"),state6("6","退回");
    private String validType;
    private String message;

    private ExpressStateEnum(String validType, String message) {
        this.validType = validType;
        this.message = message;
    }

    public static String getMessage(String validType) {
        for (ExpressStateEnum expressStateEnum : ExpressStateEnum.values()) {
            if (validType.equals(expressStateEnum.getValidType())) {
                return expressStateEnum.getMessage();
            }
        }
        return "未知状态";
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
