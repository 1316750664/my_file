package com.service.servlet.zxx.BBS_admin;

import com.service.clazz.MainJsonService;
import com.util.tools.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TTL on 2018/2/17.
 */
@WebServlet(name = "adminUserAdd", urlPatterns = {"/Action/adminUserAdd.do"})
public class adminUserAdd extends HttpServlet{
    private final static String default_return = "{\"error\":\"%d\",\"msg\":\"%s\",\"data\":\"%s\"}";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Throwable exception = null;
        String errorCode = "未知错误";
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/json;charset=utf-8");
            out = response.getWriter();
            String user_name=request.getParameter("admin_user_name");
            String password=request.getParameter("admin_password");
            String password2=request.getParameter("admin_password2");
            String email=request.getParameter("admin_email");
            String admin_create_time=request.getParameter("admin_create_time");
            if(CheckTools.isNull(user_name)||CheckTools.isNull(password)||CheckTools.isNull(password2)){
                out.write("301");
                out.flush();
                return;
            }
            if(!password.equals(password2)){
                out.write(String.format("302"));
                out.flush();
                return;
            }
            password= FingerUtil.md5(password);
            Map<String,String[]> paramMap=new HashMap<String,String[]>();
            Map<String,String> map=new HashMap<>();
            List<Map<String,String>> ret=new ArrayList<>();
            paramMap.put("taskId",new String[]{"1152"});
            paramMap.put("cmdType", new String[]{"Update"});
            paramMap.put("user_name",new String[]{user_name});
            paramMap.put("password",new String[]{password});
            paramMap.put("email",new String[]{email});
            paramMap.put("create_timestamp",new String[]{admin_create_time});
            paramMap.put("ide",new String[]{"user_id"});
            String res =new MainJsonService().invoke(paramMap);
            List<Map<String,String>> list= StaticUtil.objectMapper.readValue(res,List.class);
            String user_id =  list.get(0).get("user_id");
            map.put("user_id",user_id);
            ret.add(map);
            out.write("201");
            out.flush();
        } catch (NoSuchMethodException e) {
            exception = e.getCause();
            errorCode = "请求serviceId不存在";
        } catch (UnsupportedEncodingException e) {
            exception = e.getCause();
            errorCode = "不支持的编码格式";
        } catch (SQLException e) {
            exception = e.getCause();
            errorCode = e.getMessage();
        } catch (IllegalAccessException e) {
            exception = e.getCause();
            errorCode = "私有serviceId方法请求";
        } catch (InvocationTargetException e) {
            exception = e.getTargetException();
            errorCode = "系统错误";
        } catch (NullPointerException e) {
            exception = e.getCause();
            errorCode = "空指针错误";
        } catch (RuntimeException e) {
            exception = e.getCause();
            errorCode = e.getMessage();
        } catch (IOException e) {
            exception = e.getCause();
            errorCode = e.getMessage();
        } finally {
            if (exception != null) {
                exception.printStackTrace();//正式环境应该删除，或转至出错页
                if (out != null) {
                    out.write("{\"error\":\"601\",\"msg\":\"" + errorCode + "\"}");
                    out.flush();
                }
            }
        }
    }
}
