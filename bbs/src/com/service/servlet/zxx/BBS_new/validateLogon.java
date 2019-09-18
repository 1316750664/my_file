package com.service.servlet.zxx.BBS_new;

import com.bean.TaskBean;
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
import java.util.*;

/**
 * Created by Administrator on 2018-02-14.
 */

//注册
@WebServlet(name = "validateLogon", urlPatterns = {"/Action/validateLogon.do"})
public class validateLogon extends HttpServlet{
    private final static String default_return = "{\"error\":\"%d\",\"msg\":\"%s\",\"data\":\"%s\"}";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Throwable exception = null;
        String errorCode = "未知错误";
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/json;charset=utf-8");
            out = response.getWriter();
            String user_name=request.getParameter("user_name");
            String password=request.getParameter("password");
            String password2=request.getParameter("password2");
            String email=request.getParameter("email");
            if(CheckTools.isNull(user_name)||CheckTools.isNull(password)||CheckTools.isNull(password2)){
                out.write(String.format(default_return, 301, "缺少参数", ""));
                out.flush();
                return;
            }
            if(!password.equals(password2)){
                out.write(String.format(default_return, 301, "密码不一致", ""));
                out.flush();
                return;
            }
            password=FingerUtil.md5(password);
            Map<String,String[]> paramMap=new HashMap<String,String[]>();
            Map<String,String> map=new HashMap<>();
            List<Map<String,String>> ret=new ArrayList<>();
            paramMap.put("taskId",new String[]{"1152"});
            paramMap.put("cmdType", new String[]{"Update"});
            paramMap.put("user_name",new String[]{user_name});
            paramMap.put("password",new String[]{password});
            paramMap.put("email",new String[]{email});
            paramMap.put("create_timestamp",new String[]{System.currentTimeMillis()+""});
            paramMap.put("ide",new String[]{"user_id"});
            String res =new MainJsonService().invoke(paramMap);
            List<Map<String,String>> list= StaticUtil.objectMapper.readValue(res,List.class);
            String user_id =  list.get(0).get("user_id");
            map.put("user_id",user_id);
            ret.add(map);
            out.write(ToolUtil.getReturnResultForList(StatuCode.SUCCESS, ret));
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
