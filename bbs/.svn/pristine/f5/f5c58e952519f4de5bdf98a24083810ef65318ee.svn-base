package com.util.tools;

/**
 * Created by hzm on 2014/9/15.
 */
public enum ErrorMsgEnum {
    IsNumber("number", "请输入有效数字"), IsEmail("email", "请输入有效的email地址"), NonNull("null", "信息不能为空"),
    IsPhone("phone", "请输入正确格式的联系号码"),IsMobile("mobile", "请输入正确格式的手机号码"),IsTel("telephone", "请输入正确格式的电话号码"),
    IsChinese("chinese", "请输入中文"), FloatNumber("float", "请输入有效数字"), Car_No("car", "车牌长度7位");
    private String validType;
    private String message;

    private ErrorMsgEnum(String validType, String message) {
        this.validType = validType;
        this.message = message;
    }

    public static String getMessage(String validType) {
        for (ErrorMsgEnum errorMsgEnum : ErrorMsgEnum.values()) {
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
