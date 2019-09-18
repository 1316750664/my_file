package com.util.tools;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;


public class FingerUtil {
    private static String defaultKey = "1234567890AbcDeF";//密钥必须是16位

    /**
     * 根据key配置文件密钥解密
     *
     * @param password
     * @param propertiesKey
     * @return
     */
    public static String decryptPropertiesAES(String password, String propertiesKey) {
        String codeKey = ReadWriteProperties.getInstance().readValue("key", propertiesKey);
        return decryptAES(password, codeKey);
    }

    /**
     * AES解密
     *
     * @param password
     * @param key
     * @return
     */
    public static String decryptAES(String password, String key) {
        if (key == null || key.length() % 8 > 0) {
            key = defaultKey;
        }
        //判断Key是否正确
        if (key == null) {
            return null;
        }
        //判断Key是否为16位
        if (key.length() % 8 > 0) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("ASCII"), "AES"));
            return new String(cipher.doFinal(hex2byte(password)));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param password
     * @return
     */
    public static String decryptAES(String password) {
        return decryptAES(password, null);
    }

    /**
     * 根据key配置文件密钥加密
     *
     * @param source
     * @param propertiesKey
     * @return
     */
    public static String encryptPropertiesAES(String source, String propertiesKey) {
        String codeKey = ReadWriteProperties.getInstance().readValue("key", propertiesKey);
        return encryptAES(source, codeKey);
    }

    /**
     * AES加密
     *
     * @param source
     * @param key
     * @return
     */
    public static String encryptAES(String source, String key) {
        if (key == null || key.length() % 8 > 0) {
            key = defaultKey;
        }
        if (key == null) {
            return null;
        }
        //判断Key是否为16位
        if (key.length() % 8 > 0) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("ASCII"), "AES"));
            return byte2hex(cipher.doFinal(source.getBytes())).toLowerCase();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param source
     * @return
     */
    public static String encryptAES(String source) {
        return encryptAES(source, null);
    }

    /**
     * 十六制转二进制
     *
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        if (hex == null) {
            return null;
        }
        int l = hex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * 二进制转十六制
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String hex = "";
        for (int n = 0; n < b.length; n++) {
            hex = (Integer.toHexString(b[n] & 0XFF));
            if (hex.length() == 1) {
                hs = hs + "0" + hex;
            } else {
                hs = hs + hex;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * MD5加密
     *
     * @param source 要加密码的字符串
     * @return String 加密之后的字符串
     */
    public static String md5(String source) {
        if (source == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes("utf-8"));
            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }

    /**
     * base64解码
     *
     * @param password
     * @return
     */
    public static String decryptBase64(String password) {
        String source = null;
        try {
            source = new String(new BASE64Decoder().decodeBuffer(password), "UTF-8");
        } catch (IOException e) {
            return null;
        }
        return source;
    }

    /**
     * base64编码
     *
     * @param source
     * @return
     */
    public static String encryptBase64(String source) {
        String password = null;
        try {
            password = new BASE64Encoder().encodeBuffer(source.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return password;
    }

    public static void main(String args[]) {
//        System.out.println(decryptBase64("PE1TRz48TWVzc2FnZT48VHJ4UmVzcG9uc2U+PFJldHVybkNvZGU+MDAwMDwvUmV0dXJuQ29kZT48RXJyb3JNZXNzYWdlPjwvRXJyb3JNZXNzYWdlPjxFQ01lcmNoYW50VHlwZT5CMkM8L0VDTWVyY2hhbnRUeXBlPjxNZXJjaGFudElEPjIzMzAxMTEwMDYxNEEwMzwvTWVyY2hhbnRJRD48VHJ4VHlwZT5QYXlSZXN1bHQ8L1RyeFR5cGU+PE9yZGVyTm8+MjAxNDEyMzEwMDAwMDAwMjwvT3JkZXJObz48QW1vdW50PjkuMDA8L0Ftb3VudD48QmF0Y2hObz4wMDAwMDE8L0JhdGNoTm8+PEhvc3REYXRlPjIwMTQxMjMxPC9Ib3N0RGF0ZT48SG9zdFRpbWU+MDkxMjQ1PC9Ib3N0VGltZT48TWVyY2hhbnRSZW1hcmtzPnsidHlwZSI6IjEiLCJwYXlJZCI6IjIwMTQxMjMxMDAwMDAwMDIiLCJvcmRlcklkIjoiMjAxNDEyMzEwMDAwMDAwMSIsInVzZXJJZCI6IjM5MyJ9PC9NZXJjaGFudFJlbWFya3M+PFBheVR5cGU+UEFZMTk8L1BheVR5cGU+PE5vdGlmeVR5cGU+MTwvTm90aWZ5VHlwZT48UGF5SVA+MjIxLjEyLjExMS4xMTQ8L1BheUlQPjwvVHJ4UmVzcG9uc2U+PC9NZXNzYWdlPjxTaWduYXR1cmUtQWxnb3JpdGhtPlNIQTF3aXRoUlNBPC9TaWduYXR1cmUtQWxnb3JpdGhtPjxTaWduYXR1cmU+MkR3ZTh0MWZ5Z2szRkdSQTEzMnlvb0dNSnExZms3UUJETXZMRTZ6RFlla1V4M0ZvR3JrcUs0d1JRYVVnVWhQOFF0NlVaQmNMNHczcEdya1lmalRkU3FQQ1Z5TkozS2NlcFZnVWg0ckdiVWluSmV1VjBaQ1dnS0w3aE40ZE14QnVqS2NKL0dEaldTdktzc0hrcVczSEpmYWVGWTlqdzc5QUpVU0xEejZ0S3BnPTwvU2lnbmF0dXJlPjwvTVNHPg=="));
        System.out.println(FingerUtil.decryptAES("eabad9232991a628add30cfb468c1de7", FingerUtil.md5("6565a81d-a1ac-45e7-bf52-8438e9eef85e").substring(8, 24)));
        System.out.println(FingerUtil.decryptAES("f4a92c9005c5bb3cea483f30206f8dbf", FingerUtil.md5("90da7dbb-4f79-4b4d-ac44-9fe107982e48").substring(8, 24)));
        System.out.println(FingerUtil.encryptAES("0.0", FingerUtil.md5("6565a81d-a1ac-45e7-bf52-8438e9eef85e").substring(8, 24)));
        System.out.println(FingerUtil.encryptAES("0.0", FingerUtil.md5("90da7dbb-4f79-4b4d-ac44-9fe107982e48").substring(8, 24)));
    }
}