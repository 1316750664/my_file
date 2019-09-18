package com.util.tools;

import com.service.Service.UserService.UserService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bangong on 2015/6/25.
 * 替换方法
 */
public final class DataReplaceUtil {

    /**
     * 替换余额 2位小数
     */
    public static String replaceYue(String reg, String userId, String text) {
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            text = text.replaceFirst(st1, ToolUtil.getMathDecimal(UserService.getInstance().decryptUserBalance(userId, st2), 2));
        }
        return text;
    }

    /**
     * 匿名 请注意匿名标记使用 例子xiang_review.txt
     */
    public static String replaceNiMing(String regName, String text) {
        Pattern pn = Pattern.compile(regName);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        String st3 = null;
        String st4 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            Pattern pn1 = Pattern.compile("###niming" + st2 + ":(.*?)###");
            Matcher mr1 = pn1.matcher(text);
            while (mr1.find()) {
                st3 = mr1.group();//匹配部分，最后替换掉
                st4 = mr1.group(1);//匹配的值
                text = text.replaceAll(st3, st4);
                if (st4.equals("1")) {//匿名
                    text = text.replaceAll(st1, ToolUtil.hideName(st2));
                } else {
                    text = text.replaceAll(st1, st2);
                }
            }
        }
        return text;
    }

    /**
     * 替换Enum
     * Enum必须有getValidType getMessage
     */
    public static String replaceEnum(String reg, String userId, Class enumClass, String text) {
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            text = text.replaceFirst(st1, getEnumMessage(enumClass, st2));
        }
        return text;
    }

    /**
     * 替换Enum 无user_id
     * Enum必须有getValidType getMessage
     */
    public static String replaceEnum(String reg, Class enumClass, String text) {
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            text = text.replaceFirst(st1, getEnumMessage(enumClass, st2));
        }
        return text;
    }

    /**
     * 枚举类获取参数
     *
     * @param c
     * @param stat
     * @return
     */
    private static String getEnumMessage(Class c, String stat) {
        if (c.isEnum()) {
            try {
                //得到enum的所有实例
                Object[] objs = c.getEnumConstants();
                for (Object obj : objs) {
                    Method m1 = obj.getClass().getDeclaredMethod("getValidType", new Class[]{});
                    Method m2 = obj.getClass().getDeclaredMethod("getMessage", new Class[]{});
                    String result1 = (String) m1.invoke(obj);
                    String result2 = (String) m2.invoke(obj);
                    if (stat.equals(result1)) {
                        return result2;
                    }
                }
                return "Enum无匹配";
            } catch (Exception e) {
//                e.printStackTrace();
                return "Enum无对应方法";
            }
        } else {
            return "非Enum类";
        }
    }

    /**
     * 替换图片 type:0 会员 1 产品
     */
    public static String replaceImg(String type, String reg, String text) {
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            if (st2.equals("")) {//默认
                if ("0".equals(type)) {
                    text = text.replaceFirst(st1, StaticUtil.properties.readValue("imageConfig", "user_default"));
                } else if ("1".equals(type)) {
                    text = text.replaceFirst(st1, StaticUtil.properties.readValue("imageConfig", "good_default"));
                }
            } else {//加服务器路径
                text = text.replaceFirst(st1, StaticUtil.properties.readValue("config", "img_domain") + st2);
            }
        }
        return text;
    }

    /**
     * 号码隐藏
     */
    public static String hideString(String reg, String text) {
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            if (st2.length() == 11) {//手机
                text = text.replaceFirst(st1, ToolUtil.hidePhone(st2, 2));
            } else if (st2.length() >= 15) {//身份证 银行卡
                text = text.replaceFirst(st1, ToolUtil.hidePhone(st2, 3));
            } else {
                text = text.replaceFirst(st1, st2);
            }
        }
        return text;
    }

    /**
     * 用户名隐藏
     */
    public static String hideLogin(String reg, String text) {
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            st1 = mr.group();//匹配部分，最后替换掉
            st2 = mr.group(1);//匹配的值
            int leng = st2.length();
            if (leng >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < leng - 2; i++) {
                    sb.append("*");
                }
                st2 = st2.substring(0, 1) + sb.toString() + st2.substring(leng - 1);
            } else {
                st2 = st2 + "****";
            }
            text = text.replaceFirst(st1, st2);
        }
        return text;
    }

    /**
     * 替换积分
     *
     * @param reg
     * @param text
     * @return
     */
    public static String replacePoints(String reg, String text) {

        String jiFenJson = StaticUtil.properties.readValue("config", "creditJson");
        Pattern pn = Pattern.compile(reg);
        Matcher mr = pn.matcher(text);
        String st1 = null;
        String st2 = null;
        while (mr.find()) {
            try {
                st1 = mr.group();//匹配部分，最后替换掉
                st2 = mr.group(1);//匹配的值
                List<Map<String, String>> list = StaticUtil.objectMapper.readValue(jiFenJson, List.class);
                long jiFen = Long.parseLong(st2);
                int i = 1;
                for (Map<String, String> map : list) {// cre lv
                    String credit = map.get("cre");
                    long cre = Long.parseLong(credit);
                    if (jiFen < cre) {
                        text = text.replaceFirst(st1, map.get("lv"));
                    } else if (i == list.size()) {//比较个数到达上限
                        text = text.replaceFirst(st1, map.get("lv"));
                    }
                    i++;
                }
            } catch (IOException e) {
                text = text.replaceFirst(st1, "信息出错");
                return text;
            }
        }
        return text;
    }
}
