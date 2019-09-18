package com.service.Service.loginDetection;

import com.bean.TaskBean;
import com.service.Service.UserService.UserService;
import com.service.fragService.FragService;
import com.util.tools.FingerUtil;
import com.util.tools.StaticUtil;
import com.util.tools.ToolUtil;
import com.util.tools.UrlUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by rcl on 2014/10/7.
 */
public class LoginDetection {
    private static LoginDetection instance = null;

    private LoginDetection() {
    }

    public static LoginDetection getInstance() {
        if (instance == null) {
            instance = new LoginDetection();
        }
        return instance;
    }

    /**
     * 获取指定cookie的值
     *
     * @param cookies
     * @param cookieName
     * @return cookie_uuid   id$ip$uuid
     * session_uuid   uuid
     */
    public String getRequestCookie(Cookie[] cookies, String cookieName) {
        String cookieValue = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }

    /*
    * 从cookie获取用户id
    * */

    public String getUserid(String cookie) {
        if (cookie == null || "".equals(cookie)) {
            return null;//cookie不存在
        }
        String unlock_cookie_uuid = FingerUtil.decryptAES(cookie);
        String[] arr_cookie_uuid = unlock_cookie_uuid.split("\\$");
        if (arr_cookie_uuid.length != 4) {
            return null;//cookie不合法
        }
        return arr_cookie_uuid[1];
    }

    public int loginCheck(Cookie[] cookies, String ip, HttpServletResponse response) {
        int redirect = 1;
        int result = loginCheck(cookies, ip);
        try {
            switch (result) {
                case 0: {
                    redirect = 0;
                    break;
                }
                case 1: {
                    response.sendRedirect("/Login/login.html");
                    break;
                }
                case 2: {
                    response.sendRedirect(UrlUtil.sysError("501", "请务非法修改系统cookie操作", "/Login/login.html"));
                    break;
                }
                case 3: {
                    response.sendRedirect(UrlUtil.sysError("401", "请确认是否本人登录", "/Login/login.html"));
                    break;
                }
                case 4: {
                    response.sendRedirect(UrlUtil.sysError("601", "系统繁忙，请刷新重试", "/"));
                    break;
                }
                case 5: {
                    response.sendRedirect("/error/abnormal.html");
                    break;
                }
                case 99: {
                    response.sendRedirect(UrlUtil.sysError("502", "请确认您的用户名是否已注册或通过审核", "/Login/login-per.html"));
                    break;
                }
                default: {
                    redirect = 0;
                    break;
                }
            }
        } catch (IOException e) {
        }
        return redirect;
    }

    public int loginCheck(Cookie[] cookies, String ip) {
        if (cookies == null) {
            return 1;//cookie不存在
        }
        String cookie_uuid = ToolUtil.getRequestCookie(cookies, "cookie_uuid");
        //判断cookie是否有值
        if (cookie_uuid == null || "".equals(cookie_uuid)) {
            return 1;//cookie不存在
        }
        //cookie_uuid解密  IP对比
        String unlock_cookie_uuid = FingerUtil.decryptAES(cookie_uuid);
        //System.out.println("cookie解密后:"+unlock_cookie_uuid);
        String[] arr_cookie_uuid = unlock_cookie_uuid.split("\\$");
        if (arr_cookie_uuid.length != 4) {
            return 2;//cookie不合法
        }
        //String uname = arr_cookie_uuid[0];
        String uid = arr_cookie_uuid[1];
        String IP = arr_cookie_uuid[2];
        String uuid = arr_cookie_uuid[3];
        if (!IP.equals(ip)) {
            return 3;//非本机操作
        }
        //吧cookieuuid和memcached的sessionuuid对比
        String session_uuid = UserService.getInstance().getUserMemcacheOneColum(uid, "session_uuid");
        //如果娶不到 就去数据库查询 成功则更新缓存   否则  跳到账号异常登录页面
        //System.out.println("seesion_uuid:"+ session_uuid);
        if (session_uuid == null) {//缓存失效
            TaskBean taskBean = new TaskBean(99, 0, new String[]{uid});
            String json = FragService.getInstance().fragment(0, taskBean);
            if (json == null || "[]".equals(json)) {
                return 99;
            }
            List<Map<String, String>> list = null;
            try {
                list = StaticUtil.objectMapper.readValue(json, List.class);
                Map<String, String> map = list.get(0);
                String db_uuid = map.get("session_uuid");
                if (!uuid.equals(db_uuid)) {
                    return 5;////异常登录或账号在其它地方登录
                }
                UserService.getInstance().onlyMemcacheUpdate(map);
            } catch (IOException e) {
                return 4;//系统异常
            }
        } else if (!uuid.equals(session_uuid)) {
            return 5;//异常登录或账号在其它地方登录
        }

        return 0;
    }
}
