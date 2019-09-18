package com.util.tools;

import com.bean.ValidCodeBean;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hzm on 2014/12/1.
 */
public class ValidCodeCookie {
    private static final int cookieMaxAge = 30 * 24 * 60 * 60;
    public static final String RANDOM_CODE_KEY = "_code_key";// 放到cookie中的key
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    private static String codeKey = null;

    private static class SingletonValidCodeCookie {
        private static ValidCodeCookie INSTANCE = new ValidCodeCookie();
    }

    public static ValidCodeCookie getInstance() {
        return SingletonValidCodeCookie.INSTANCE;
    }

    private ValidCodeCookie() {
        codeKey = ReadWriteProperties.getInstance().readValue("key", "codeKey");
    }

    /**
     * type 1图片验证码 2手机验证码 3邮箱验证码 4订单TOKEN 5充值TOKEN 6支付编号 7支付TOKEN 8银行支付订单编号outId 9修改密码 10手机号、邮箱 expiry失效时间 code验证码值
     * [{"type":"1","expiry":"1417508559390","code":"5786"}]
     *
     * @param cookies
     * @param validCodeBean
     * @return
     */
    public void writeCodeCookie(HttpServletResponse responses, Cookie[] cookies, ValidCodeBean validCodeBean) {
        if (validCodeBean == null) {
            return;
        }
        List<ValidCodeBean> codeList = deleteCodeList(cookies, true);
        if (codeList == null) {
            codeList = new ArrayList<ValidCodeBean>(1);
        } else {
            int codeType = validCodeBean.getType();
            ValidCodeBean codeBean = null;
            Iterator<ValidCodeBean> iterator = codeList.iterator();
            while (iterator.hasNext()) {
                codeBean = iterator.next();
                if (codeBean.getType() == codeType) {
                    iterator.remove();//删除相同值
                }
            }
        }

        codeList.add(validCodeBean);
        try {
            Cookie cookie = new Cookie(RANDOM_CODE_KEY, FingerUtil.encryptAES(objectMapper.writeValueAsString(codeList), codeKey));
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath("/");
            responses.addCookie(cookie);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void writeCodeCookie(HttpServletResponse responses, Cookie[] cookies, List<ValidCodeBean> validCodeBeanList) {
        if (validCodeBeanList == null) {
            return;
        }
        List<ValidCodeBean> codeList = deleteCodeList(cookies, true);
        if (codeList == null) {
            codeList = new ArrayList<ValidCodeBean>(validCodeBeanList.size());
        } else {
            int codeType;
            ValidCodeBean codeBean = null;
            Iterator<ValidCodeBean> iterator = codeList.iterator();
            while (iterator.hasNext()) {
                codeBean = iterator.next();
                for (ValidCodeBean validCodeBean : validCodeBeanList) {
                    codeType = validCodeBean.getType();
                    if (codeBean.getType() == codeType) {
                        iterator.remove();//删除相同值
                        break;
                    }
                }

            }
        }
        for (ValidCodeBean validCodeBean : validCodeBeanList) {
            codeList.add(validCodeBean);
        }
        try {
            Cookie cookie = new Cookie(RANDOM_CODE_KEY, FingerUtil.encryptAES(objectMapper.writeValueAsString(codeList), codeKey));
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath("/");
            responses.addCookie(cookie);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public ValidCodeBean readCodeCookie(HttpServletResponse responses, Cookie[] cookies, int codeType) {
        List<ValidCodeBean> codeList = deleteCodeList(cookies, true);
        if (codeList == null || codeList.size() == 0) {
            Cookie cookie = new Cookie(RANDOM_CODE_KEY, "");
            cookie.setMaxAge(0);
            responses.addCookie(cookie);
            return null;
        }
        try {
            ValidCodeBean validCodeBean = null;
            ValidCodeBean codeBean = null;
            Iterator<ValidCodeBean> iterator = codeList.iterator();
            while (iterator.hasNext()) {
                codeBean = iterator.next();
                if (codeBean.getType() == codeType) {
                    validCodeBean = codeBean;
                    break;
                }
            }

            Cookie cookie = new Cookie(RANDOM_CODE_KEY, FingerUtil.encryptAES(objectMapper.writeValueAsString(codeList), codeKey));
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath("/");
            responses.addCookie(cookie);

            return validCodeBean;
        } catch (IOException e) {
            return null;
        }
    }

    public List<ValidCodeBean> readCodeCookie(HttpServletResponse responses, Cookie[] cookies, int[] codeTypes) {
        if (codeTypes == null) {
            return null;
        }
        List<ValidCodeBean> codeList = deleteCodeList(cookies, true);
        if (codeList == null || codeList.size() == 0) {
            Cookie cookie = new Cookie(RANDOM_CODE_KEY, "");
            cookie.setMaxAge(0);
            responses.addCookie(cookie);
            return null;
        }
        try {
            List<ValidCodeBean> validCodeBeanList = new ArrayList<ValidCodeBean>(codeTypes.length);
            ValidCodeBean codeBean = null;
            Iterator<ValidCodeBean> iterator = codeList.iterator();
            while (iterator.hasNext()) {
                codeBean = iterator.next();
                for (int codeType : codeTypes) {
                    if (codeBean.getType() == codeType) {
                        validCodeBeanList.add(codeBean);
                    }
                }
            }

            Cookie cookie = new Cookie(RANDOM_CODE_KEY, FingerUtil.encryptAES(objectMapper.writeValueAsString(codeList), codeKey));
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath("/");
            responses.addCookie(cookie);

            return validCodeBeanList;
        } catch (IOException e) {
            return null;
        }
    }

    public void deleteCodeCookie(HttpServletResponse responses, Cookie[] cookies, int codeType) {
        try {
            String codeString = "";
            List<ValidCodeBean> codeList = deleteCodeList(cookies, false);
            if (codeList != null && codeList.size() > 0) {
                ValidCodeBean codeBean = null;
                Iterator<ValidCodeBean> iterator = codeList.iterator();
                while (iterator.hasNext()) {
                    codeBean = iterator.next();
                    if (codeBean.getType() == codeType) {
                        iterator.remove();//删除
                    }
                }
                codeString = FingerUtil.encryptAES(objectMapper.writeValueAsString(codeList), codeKey);
            }
            Cookie cookie = new Cookie(RANDOM_CODE_KEY, codeString);
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath("/");
            responses.addCookie(cookie);
        } catch (IOException e) {

        }
    }

    public void deleteCodeCookie(HttpServletResponse responses, Cookie[] cookies, int[] codeTypes) {
        if (codeTypes == null) {
            return;
        }
        try {
            String codeString = "";
            List<ValidCodeBean> codeList = deleteCodeList(cookies, false);
            if (codeList != null && codeList.size() > 0) {
                ValidCodeBean codeBean = null;
                Iterator<ValidCodeBean> iterator = codeList.iterator();
                while (iterator.hasNext()) {
                    codeBean = iterator.next();
                    for (int codeType : codeTypes) {
                        if (codeBean.getType() == codeType) {
                            iterator.remove();//删除
                            break;
                        }
                    }
                }

                codeString = FingerUtil.encryptAES(objectMapper.writeValueAsString(codeList), codeKey);
            }
            Cookie cookie = new Cookie(RANDOM_CODE_KEY, codeString);
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath("/");
            responses.addCookie(cookie);
        } catch (IOException e) {

        }
    }

    /**
     * 删除过期的code cookie值
     *
     * @param cookies
     * @return
     */
    private List<ValidCodeBean> deleteCodeList(Cookie[] cookies, boolean expiryDeleted) {
        try {
            if (cookies == null) {
                return null;
            }
            String _code_value = ToolUtil.getRequestCookie(cookies, RANDOM_CODE_KEY);
            if (_code_value == null || "".equals(_code_value)) {
                return null;
            }
            String _code_json = FingerUtil.decryptAES(_code_value, codeKey);
            if (_code_json == null) {
                return null;
            }
            List<ValidCodeBean> codeList = null;
            JavaType javaType = ToolUtil.getCollectionType(objectMapper, List.class, ValidCodeBean.class);
            codeList = objectMapper.readValue(_code_json, javaType);
            if (!expiryDeleted) {
                return codeList;
            }
            ValidCodeBean codeBean = null;
            long expiry;
            long currentTimeMillis = System.currentTimeMillis();
            Iterator<ValidCodeBean> iterator = codeList.iterator();
            while (iterator.hasNext()) {
                codeBean = iterator.next();
                expiry = codeBean.getExpiry();
                if (currentTimeMillis >= expiry) {
                    iterator.remove();
                }
            }
            return codeList;
        } catch (IOException e) {
            return null;
        }
    }
}
