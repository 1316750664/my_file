package com.util.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created with IntelliJ IDEA.
 * User: T410
 * Date: 13-3-6
 * Time: 上午10:07
 * 获取汉字的拼音
 */
public class GetPinYin {
    /**
     * 得到 全拼
     *
     * @param src 汉字
     * @return String 汉字全拼
     */
    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (char aT1 : t1) {
                //判断是否为汉字字符
                if (Character.toString(aT1).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(aT1, t3);
                    t4 += t2[0];
                } else {
                    t4 += Character.toString(aT1);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    /**
     * 得到中文首字母
     *
     * @param str 汉字
     * @return String 得到中文首字母
     */
    public static String getPinYinHeadChar(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr 汉字
     * @return String 将字符串转移为ASCII码
     */
    public static String getCnASCII(String cnStr) {
        StringBuilder strBuf = new StringBuilder();
        byte[] bGBK = cnStr.getBytes();
        for (byte aBGBK : bGBK) {
            //System.out.println(Integer.toHexString(bGBK[i]&0xff));
            strBuf.append(Integer.toHexString(aBGBK & 0xff));
        }
        return strBuf.toString();
    }

    public static void main(String[] args) {
        String cnStr = "hello";
        System.out.println(cnStr.charAt(0));
        System.out.println(getPinYinHeadChar(cnStr));
        String[] results = new String[]{"hello,mimi", "yes"};
        System.out.println(ToolUtil.arrayToString(results, ','));

    }
}
