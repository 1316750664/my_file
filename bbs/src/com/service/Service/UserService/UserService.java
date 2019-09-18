package com.service.Service.UserService;

import com.bean.TaskBean;
import com.business.TaskHandler;
import com.ctd.util.database.MemCachedUtil;
import com.service.Service.loginDetection.LoginDetection;
import com.service.fragService.FragService;
import com.util.tools.AutoBean;
import com.util.tools.CheckTools;
import com.util.tools.FingerUtil;
import com.util.tools.ReadWriteProperties;
import com.util.tools.SqlXmlRead;
import com.util.tools.ToolUtil;
import com.util.tools.UrlUtil;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hty070503 on 2014/11/12.
 */
public class UserService {
    private ObjectMapper objectMapper;
    private int creditsMin= Integer.parseInt(ReadWriteProperties.getInstance().readValue("config","creditsMin"));
    private int creditsMid= Integer.parseInt(ReadWriteProperties.getInstance().readValue("config","creditsMid"));
    private int creditsMax= Integer.parseInt(ReadWriteProperties.getInstance().readValue("config","creditsMax"));


    public static UserService getInstance() {
        return SingletonCartService.INSTANCE;
    }

    private static class SingletonCartService {
        private static UserService INSTANCE = new UserService();
    }

    private UserService() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * @param uuid
     * @param user
     * @param response 登陆后使用
     * @throws IOException
     */
    public void setCookieUser(String uuid, Map<String, String> user, HttpServletResponse response) throws IOException {
        String uid = user.get("user_id");
        String login_name = user.get("login_name");
        String ip = user.get("last_ip");
        //写入cookie和memecahe
        String cookie_uuid = login_name + "$" + uid + "$" + ip + "$" + uuid;
        Cookie cookie = new Cookie("cookie_uuid", FingerUtil.encryptAES(cookie_uuid));
        cookie.setPath("/");
        response.addCookie(cookie);

        user.put("session_uuid", uuid);
        //缓存与cookie更新
        cookieAndMemcacheUpdate(response, user);
    }

    /**
     *
     * @param userid  主站验证通过后，判断该用户是否bbs用户，不是则新增
     * @return
     */
    public int userExist(String userid,Cookie[] cookies,HttpServletResponse response){
        //判断是否有bbs cookie信息
        String bbs_userinfo = ToolUtil.getRequestCookie(cookies,"bbs_userinfo");
        if(CheckTools.isNull(bbs_userinfo) || !userid.equals(getUserFromBbsCookie(cookies,"user_id"))) {
            //状态为登录   继而判断bbs系统是否有信息，第一次登录则为其插入一条
            TaskBean taskBean_ifuser = new TaskBean(26, 0, new String[]{userid});
            String if_user = FragService.getInstance().fragment(0, taskBean_ifuser);
            Map<String, String> map = new HashMap<String, String>();
                try {

                    if (if_user == null || "[]".equals(if_user)) {
                    //用户不存在就进行插入
                    List<TaskBean> taskBeans = new ArrayList<TaskBean>();
                    Map<String, String> cookie_userinfo = getUserMapFromCookie(cookies);
                    String nick = cookie_userinfo.get("nick");
                    String avatar = cookie_userinfo.get("avatar");
                    if (CheckTools.isNull(nick)) {
                        nick = cookie_userinfo.get("login_name");
                        if (CheckTools.isNull(nick)) {
                            //异常情况
                            return 6;//cookie信息丢失
                        }
                    }
                    if (CheckTools.isNull(avatar)) {
                        avatar = ReadWriteProperties.getInstance().readValue("imageConfig", "no_pic");
                    }
                    taskBeans.add(new TaskBean(27, 1, new String[]{userid, nick, avatar}, new String[]{"string", "string", "string"}));
                    String  rs = TaskHandler.getInstance().handler(0, taskBeans);
                    List<Map<String,String>> listm = objectMapper.readValue(rs, List.class);
                      if(listm.size()>0 && listm.get(0).containsKey("error")){
                          return 4;//系统异常
                      }
                        map.put("user_id",userid);
                        map.put("nick",nick);
                        map.put("avatar",avatar);
                        map.put("posts","0");
                        map.put("replies","0");
                        map.put("bests","0");
                        map.put("cheers","0");
                        map.put("points","0");

                }else{
                    List<Map<String, String>> list = null;
                        list = objectMapper.readValue(if_user, List.class);
                        map = list.get(0);
                }

                    cookieAdd(response,map,"bbs_userinfo");
            } catch (Exception e) {
                return 4;//系统异常
            }
        }


        return 0;
    }



    /**
     * 缓存与cookie更新
     *
     * @param user
     */

    public void cookieAndMemcacheUpdate(HttpServletResponse response, Map<String, String> user) {
        onlyCookieUpdate(response, user);
        onlyMemcacheUpdate(user);
    }

    public void cookieAdd(HttpServletResponse response, Map<String, String> map,String cookiename){
        String cookieInfo = "";
        try {
            cookieInfo = objectMapper.writeValueAsString(map);
            cookieInfo = java.net.URLEncoder.encode(cookieInfo, "UTF-8");//编码
        } catch (IOException e) {
            //e.printStackTrace();
        }
        Cookie cookie2 = new Cookie(cookiename, cookieInfo);
        cookie2.setPath("/");
        response.addCookie(cookie2);
    }

    private void onlyCookieUpdate(HttpServletResponse response, Map<String, String> user) {
        //用户简单登陆信息存储
        Map<String, String> cookieUser = new HashMap<String, String>();
        cookieUser.put("login_name", user.get("login_name"));
        cookieUser.put("nick", user.get("nick"));
        cookieUser.put("avatar", user.get("avatar"));
        cookieUser.put("province_id", user.get("province_id"));
        cookieUser.put("city_id", user.get("city_id"));
        cookieUser.put("area_id", user.get("area_id"));
        cookieUser.put("real_name", user.get("real_name"));
        cookieUser.put("sex", user.get("sex"));
        cookieUser.put("qq", user.get("q"));
        cookieAdd(response,cookieUser,"cookie_userInfo");
    }

    /**
     * 仅仅更新缓存
     *
     * @param user
     */
    public void onlyMemcacheUpdate(Map<String, String> user) {
        String uid = user.get("user_id");
        //用户信息存入缓存
        Map<String, String> memCacheUser = new HashMap<String, String>();
        memCacheUser.put("user_id", user.get("user_id"));
        memCacheUser.put("user_type", user.get("user_type"));
        memCacheUser.put("is_seller", user.get("is_seller"));
        memCacheUser.put("valid_mobile", user.get("valid_mobile"));
        memCacheUser.put("valid_email", user.get("valid_email"));
        memCacheUser.put("idcard", user.get("idcard"));
        memCacheUser.put("login_salt", user.get("login_salt"));
        memCacheUser.put("check_status", user.get("check_status"));
        memCacheUser.put("last_time", user.get("last_time"));
        memCacheUser.put("last_ip", user.get("last_ip"));
        memCacheUser.put("session_uuid", user.get("session_uuid"));
        try {
            String json = objectMapper.writeValueAsString(memCacheUser);
            if (MemCachedUtil.getInstance().get("u_" + uid) == null)
                MemCachedUtil.getInstance().add("u_" + uid, json);
            else
                MemCachedUtil.getInstance().replace("u_" + uid, json);
//            System.out.println(json+":::"+uid+"::"+MemCachedUtil.getInstance().get("u_" + uid));
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * @param uid 获取用户数据  可用于cookie memcache
     * @return
     */
    private Map<String, String> getUser(String uid) {
        if (uid == null || "".equals(uid)) {
            return null;
        }
        String json = FragService.getInstance().fragment(0, new TaskBean(99, 0, new String[]{uid}));
        if (json != null && "[]".equals(json)) {
            return null;
        }
        Map<String, String> user = null;
        try {
            List<Map<String, String>> tmp = objectMapper.readValue(json, List.class);
            if (tmp.size() <= 0) {
                return null;
            } else {
                user = tmp.get(0);
                return user;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @param response 更新某个用户的信息
     * @param uid
     * @return
     */
    public boolean updateCookieAndMemcahe(HttpServletResponse response, String uid) {
        Map<String, String> map = getUser(uid);
        if (map == null) {
            return false;
        } else {
            cookieAndMemcacheUpdate(response, map);
        }
        return true;
    }


    /**
     * @param uid 根据用户类型获取价格类型
     * @return
     */
    public int getPriceIdByUserType(String uid) {
        String u_type = getUserMemcacheOneColum(uid, "user_type");
        String check_status = getUserMemcacheOneColum(uid, "check_status");
        int user_type = 1;
        if (CheckTools.isNull(u_type) || CheckTools.isNull(check_status) || "0".equals(check_status) || "2".equals(check_status)) {
            return 3;
        }
        user_type = Integer.parseInt(u_type);

        int query_utp = 3;
        if (user_type > 1 && user_type < 4) {
            query_utp = 4;
        } else if (user_type > 3 && user_type < 6) {
            query_utp = 5;
        }

        return query_utp;
    }

    /**
     *
     * @param num    下单量
     * @param uid   会员id
     * @param num4   经销
     * @param num5   出厂
     * @return
     */
    public int getPriceIdByMin_num(int num,int num4,int num5,String uid){
        int utype = getPriceIdByUserType(uid);
        if(num<=0){
            return 3;
        }
        if(num>=num5 && utype==5){
            return 5;
        }else if(num>=num4 && num<num5 && utype>=4){
            return 4;
        }else{
            return 3;
        }
    }

    /**
     * 登录验证缓存清除
     *
     * @param uid
     */
    public void loginDetectMemcacheDel(String uid) {
        MemCachedUtil.getInstance().delete("u_" + uid);
    }

    /**
     * @param uid 更新用户缓存信息    user_id/user_type/is_seller/valid_mobile/valid_email/idcard
     * @return
     */
    public int userMemcacheUpdate(String uid) {

        Map<String, String> user = getUser(uid);
        if (user != null) {
            onlyMemcacheUpdate(user);
            return 1;
        } else {
            return 0;
        }


    }

    /**
     * @param uid 用户缓存失效
     * @return
     */
    public int userMemcacheUnuse(String uid) {
        if (uid == null || "".equals(uid)) {
            return 0;
        }
        MemCachedUtil.getInstance().delete("u_" + uid);
        return 1;
    }

    /**
     * @param uid 获取用户缓存
     * @return
     */
    public String getUserMemcache(String uid) {
        Object userCache = MemCachedUtil.getInstance().get("u_" + uid);
        if (userCache == null) {
            int result = userMemcacheUpdate(uid);
            if (result == 0) {
                userMemcacheUpdate(uid);//再取一遍，仍取不到则放弃
            }
            userCache = MemCachedUtil.getInstance().get("u_" + uid);
        }
        if (userCache == null) {
            return null;
        } else {
            return userCache.toString();
        }
    }

    /**
     * @param uid
     * @param colum 获取用户缓存中的某个字段
     * @return
     */
    public String getUserMemcacheOneColum(String uid, String colum) {
        String json = getUserMemcache(uid);
//        System.out.println(uid+":::json:::"+json);
        if (json == null) {
            return null;
        }
        Map<String, String> map = null;
        try {
            map = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            return null;
        }
        if (map == null) {
            return null;
        }

        return map.get(colum);
    }

    /**
     * @param request 获取用户id
     * @return
     */
    public String getUidFromCookie(HttpServletRequest request) {
        String uuidCookie = ToolUtil.getRequestCookie(request.getCookies(), "cookie_uuid");
        return LoginDetection.getInstance().getUserid(uuidCookie);
    }

    /**
     * @param request 获取购物车数据
     * @return
     */
    public String getUserCartInfo(HttpServletRequest request) {
        return ToolUtil.getRequestCookie(request.getCookies(), "cookie_cart");
    }

    /**
     * @param request
     * @param colum   获取用户信息cookie的指定信息   login_name/avatar/nick/province_id/province_id/area_id/real_name/sex/qq
     * @return
     */
    public String getUserFromCookie(HttpServletRequest request, String colum) {
        return getcolumFromCookie(request,colum,"cookie_userInfo");
    }
    public String getUserFromCookie(Cookie[] cookies, String colum) {
        return getcolumFromCookie(cookies,colum,"cookie_userInfo");
    }

    /**
     *
     * @param request
     * @param colum
     * @param cookiename  根据字段名 和cookie名来获取 某个cookie里面的字段
     * @return
     */
    public String getcolumFromCookie(HttpServletRequest request, String colum,String cookiename){
        String cookie = ToolUtil.getRequestCookie(request.getCookies(), cookiename);
        if (cookie == null) {
            return null;
        } else {
            try {
                Map<String, String> map = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), Map.class);
                if (map == null) {
                    return null;
                }
                return map.get(colum);
            } catch (IOException e) {
                return null;
            }
        }
    }
    public String getcolumFromCookie(Cookie[] cookies, String colum,String cookiename){
        String cookie = ToolUtil.getRequestCookie(cookies, cookiename);
        if (cookie == null) {
            return null;
        } else {
            try {
                Map<String, String> map = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), Map.class);
                if (map == null) {
                    return null;
                }
                return map.get(colum);
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * @param request
     * @param colum   获取bbs用户信息cookie的指定信息   user_id/nick/avatar/posts/replies/bests/cheers/points/
     * @return
     */
    public String getUserFromBbsCookie(HttpServletRequest request, String colum) {
        return getcolumFromCookie(request,colum,"bbs_userinfo");
    }
    public String getUserFromBbsCookie(Cookie[] cookies, String colum) {
        return getcolumFromCookie(cookies,colum,"bbs_userinfo");
    }


    /**
     * @param cookies  需要多个参数时候可以直接返回整个map
     * @return
     */
    public Map<String, String> getUserMapFromCookie(Cookie[] cookies) {
        String cookie = ToolUtil.getRequestCookie(cookies, "cookie_userInfo");
        if (cookie == null) {
            return null;
        } else {
            try {
                Map<String, String> map = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), Map.class);
                if (map == null) {
                    return null;
                }
                return map;
            } catch (IOException e) {
                return null;
            }
        }
    }


    /**
     * @param cookies  需要多个参数时候可以直接返回整个map
     * @return
     */
    public Map<String, String> getMapFromCookie(Cookie[] cookies,String cookiename) {
        String cookie = ToolUtil.getRequestCookie(cookies, cookiename);
        if (cookie == null) {
            return null;
        } else {
            try {
                Map<String, String> map = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), Map.class);
                if (map == null) {
                    return null;
                }
                return map;
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     *
     * @param count 总价 用户积分获取
     * @return
     */
    public int creditCalulat(int count){
        if(count<=creditsMin){
            return 1;
        }else if(count>=creditsMax){
            return creditsMax/creditsMid;
        }else{
            return count/creditsMid;
        }
    }

    /**
     * 解码用户余额
     *
     * @param userId  可以是用户编号也可以是加密密钥
     * @param balance
     * @return
     */
    public double decryptUserBalance(String userId, String balance) {
        String password = null;
        if (CheckTools.checkIsNum(userId)) {
            password = getUserMemcacheOneColum(userId, "login_salt");
        } else {
            password = userId;
        }
        if (CheckTools.isNull(password)) {
            return 0d;
        } else {
            balance = FingerUtil.decryptAES(balance, FingerUtil.md5(password).substring(8, 24));
        }
        if (CheckTools.checkIsDouble(balance)) {
            return Double.parseDouble(balance);
        }
        return 0d;
    }

    /**
     * 加密用户余额
     *
     * @param userId  可以是用户编号也可以是加密密钥
     * @param balance
     * @return
     */
    public String encryptUserBalance(String userId, double balance) {
        String password = null;
        if (CheckTools.checkIsNum(userId)) {
            password = getUserMemcacheOneColum(userId, "login_salt");
        } else {
            password = userId;
        }
        if (CheckTools.isNull(password)) {
            return null;//无法加密
        } else {
            return FingerUtil.encryptAES(String.valueOf(balance), FingerUtil.md5(password).substring(8, 24));
        }
    }
}
