package com.service.servlet.zxx.BBS_new;

import com.service.clazz.MainJsonService;
import com.util.tools.StaticUtil;
import com.util.tools.StatuCode;
import com.util.tools.ToolUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-02-14.
 */
@WebServlet(name = "BbsPostLook", urlPatterns = {"/Action/BbsPostLook.do"})
public class BbsPostLook extends HttpServlet{
    private final static String default_return = "{\"error\":\"%d\",\"msg\":\"%s\",\"data\":\"%s\"}";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Throwable exception = null;
        String errorCode = "未知错误";
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/json;charset=utf-8");
            out = response.getWriter();
            String post_id=request.getParameter("post_id");
            Map<String,String[]> paramMap=new HashMap<String,String[]>();
            paramMap.put("post_id",new String[]{post_id});
            paramMap.put("taskId",new String[]{"1156"});
            paramMap.put("cmdType", new String[]{"Update"});
            String res =new MainJsonService().invoke(paramMap);
            paramMap.put("taskId",new String[]{"1155"});
            paramMap.put("cmdType", new String[]{"Query"});
            String real =new MainJsonService().invoke(paramMap);
            List<Map<String,Object>> list= StaticUtil.objectMapper.readValue(real,List.class);

            out.write(ToolUtil.getReturnResultForList(StatuCode.SUCCESS, list));
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
