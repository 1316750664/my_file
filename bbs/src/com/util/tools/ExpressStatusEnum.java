package com.util.tools;

/**
 * Created by bangong on 2015/3/2.
 */
public enum ExpressStatusEnum {
    state200("200", "正常"), state201("201", "单号不存在或者已经过期"), state400("400", "参数错误"),
    state403("403", "单号格式错误");
    private String validType;
    private String message;

    private ExpressStatusEnum(String validType, String message) {
        this.validType = validType;
        this.message = message;
    }

    public static String getMessage(String validType) {
        for (ExpressStatusEnum expressStatusEnum : ExpressStatusEnum.values()) {
            if (validType.equals(expressStatusEnum.getValidType())) {
                return expressStatusEnum.getMessage();
            }
        }
        return "参数异常";
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
