package com.util.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckTools {
    public static boolean isNull(String s) {
        return (s == null) || ("".equals(s.trim()));
    }

    /**
     * 验证email
     *
     * @param s
     * @return
     */
    public static boolean checkEmail(String s) {
        if (isNull(s)) {
            return false;
        } else {
            Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            Matcher m = p.matcher(s);
            boolean b = m.matches();
            return b;
        }
    }

    /**
     * 验证url
     *
     * @param s
     * @return
     */
    public static boolean checkUrl(String s) {
        if (isNull(s)) {
            return false;
        }
        s = s.toLowerCase();
        Pattern p = Pattern.compile("^[a-zA-Z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        return b;
    }

    /**
     * 验证固定电话号码
     *
     * @param s
     * @return
     */
    public static boolean checkTel(String s) {
        if (isNull(s)) {
            return false;
        }
        if (s.length() >= 40) {
            return false;
        }
        Pattern p = Pattern.compile("((\\+)?[0-9]{0,4}(\\-)?[0-9]{6,8}(\\-[0-9]{3,5})?(\\,)?)*");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        return b;
    }

    /**
     * 验证是否是手机号格式
     * 该方法还不是很严谨,只是可以简单验证
     *
     * @param mobile 手机号码
     * @return true表示是正确的手机号格式, false表示不是正确的手机号格式
     */
    public static boolean checkMobile(String mobile) {
        //当前运营商号段分配
        //中国移动号段 1340-1348 135 136 137 138 139 150 151 152 157 158 159 187 188 147
        //中国联通号段 130 131 132 155 156 185 186 145
        //中国电信号段 133 1349 153 180 189
        //虚拟运营商号 170
        if (isNull(mobile)) {
            return false;
        } else {
            String regular = "1[3,4,5,7,8]{1}\\d{9}";
            Pattern pattern = Pattern.compile(regular);
            boolean flag = false;
            if (mobile != null) {
                Matcher matcher = pattern.matcher(mobile);
                flag = matcher.matches();
            }
            return flag;
        }
    }

    /**
     * 验证邮政编码
     *
     * @param Code
     * @return
     */
    public static boolean checkZipCode(String Code) {
        if (isNull(Code)) {
            return false;
        }
        Pattern p = Pattern.compile("[0-9]{6}");
        Matcher m = p.matcher(Code);
        return m.matches();
    }

    /**
     * 验证正负整数
     *
     * @param num
     * @return
     */
    public static boolean checkIsNum(String num) {
        if (isNull(num)) {
            return false;
        } else {
            Pattern p = Pattern.compile("[-]{0,1}\\d{1,}");
            Matcher m = p.matcher(num);
            return m.matches();
        }
    }

    /**
     * 验证浮点数
     *
     * @param Code
     * @return
     */
    public static boolean checkIsPoint(String Code) {
        if (isNull(Code)) {
            return false;
        }
        try {
            Float.parseFloat(Code);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证浮点数
     *
     * @param Code
     * @return
     */
    public static boolean checkIsDouble(String Code) {
        if (isNull(Code)) {
            return false;
        }
        try {
            Double.parseDouble(Code);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证正整数
     *
     * @param Code
     * @return
     */
    public static boolean checkPositiveNum(String Code) {
        if (isNull(Code)) {
            return false;
        } else {
            Pattern p = Pattern.compile("\\d{1,}");
            Matcher m = p.matcher(Code);
            return m.matches();
        }
    }

    /**
     * 判断是否是日期格式字符串
     *
     * @param dateStr
     * @return
     */
    public static boolean isDate(String dateStr) {
        if (isNull(dateStr)) {
            return false;
        } else {
            String datePattern1 = "\\d{4}-\\d{1,2}-\\d{1,2}";
            String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                    + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                    + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                    + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                    + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                    + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
            if ((dateStr != null)) {
                Pattern pattern = Pattern.compile(datePattern1);
                Matcher match = pattern.matcher(dateStr);
                if (match.matches()) {
                    pattern = Pattern.compile(datePattern2);
                    match = pattern.matcher(dateStr);
                    return match.matches();
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * 判断是否英文文字符
     *
     * @param strName
     * @return
     */
    public static boolean isEnglish(String strName) {
        if (isNull(strName)) {
            return false;
        }
        Pattern p = Pattern.compile("[\\w]+");
        Matcher m = p.matcher(strName);
        return m.matches();
    }

    /**
     * 判断是否中文字符
     *
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        if (isNull(strName)) {
            return false;
        }
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}