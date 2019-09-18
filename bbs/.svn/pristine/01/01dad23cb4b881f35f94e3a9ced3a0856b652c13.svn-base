package com.util.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by hty070505 on 2014/12/17.
 */
public class UrlUtil {

    private UrlUtil() {
    }

    public static String payError(String code, String message, String gotoUrl) {
        String url = "/bank/PaymentFailed.jsp?";
        try {
            if (!CheckTools.isNull(code)) {
                url += "code=" + code + "&";
            }
            if (!CheckTools.isNull(code) && !CheckTools.isNull(message)) {
                url += "message=" + URLEncoder.encode(message, "UTF-8") + "&";
            }
            if (!CheckTools.isNull(gotoUrl)) {
                url += "gotoUrl=" + gotoUrl;
            }
        } catch (UnsupportedEncodingException e) {
            url = "/error/400.html";
        }

        return url;
    }

    public static String orderError(String code, String message, String gotoUrl) {
        String url = "/order/orderFaild.jsp?";
        try {
            if (!CheckTools.isNull(code)) {
                url += "code=" + code + "&";
            }
            if (!CheckTools.isNull(code) && !CheckTools.isNull(message)) {
                url += "message=" + URLEncoder.encode(message, "UTF-8") + "&";
            }
            if (!CheckTools.isNull(gotoUrl)) {
                url += "gotoUrl=" + gotoUrl;
            }
        } catch (UnsupportedEncodingException e) {
            url = "/error/400.html";
        }

        return url;
    }

    public static String sysError(String code, String message, String gotoUrl) {
        String url = "/error/sysError.jsp?";
        try {
            if (!CheckTools.isNull(code)) {
                url += "code=" + code + "&";
            }
            if (!CheckTools.isNull(code) && !CheckTools.isNull(message)) {
                url += "message=" + URLEncoder.encode(message, "UTF-8") + "&";
            }
            if (!CheckTools.isNull(gotoUrl)) {
                url += "gotoUrl=" + gotoUrl;
            }
        } catch (UnsupportedEncodingException e) {
            url = "/error/400.html";
        }

        return url;
    }

    public static String cashError(String code, String message, String gotoUrl) {
        String url = "/bank/cashFailed.jsp?";
        try {
            if (!CheckTools.isNull(code)) {
                url += "code=" + code + "&";
            }
            if (!CheckTools.isNull(code) && !CheckTools.isNull(message)) {
                url += "message=" + URLEncoder.encode(message, "UTF-8") + "&";
            }
            if (!CheckTools.isNull(gotoUrl)) {
                url += "gotoUrl=" + gotoUrl;
            }
        } catch (UnsupportedEncodingException e) {
            url = "/error/400.html";
        }

        return url;
    }
}
