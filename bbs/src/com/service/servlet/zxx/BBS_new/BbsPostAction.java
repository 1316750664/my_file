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
 * Created by Administrator on 2018-02-13.
 */
//发帖
@WebServlet(name = "BbsPostAction", urlPatterns = {"/Action/BbsPostAction.do"})
public class BbsPostAction extends HttpServlet{
    private final static String default_return = "{\"error\":\"%d\",\"msg\":\"%s\",\"data\":\"%s\"}";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Throwable exception = null;
        String errorCode = "未知错误";
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/json;charset=utf-8");
            out = response.getWriter();
            String user_id=request.getParameter("user_id");
            String topic=request.getParameter("topic");
            String content=request.getParameter("content");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            long time=System.currentTimeMillis();
            Date date=new Date(time);
            String create_time=simpleDateFormat.format(date);
            Map<String,String[]> paramMap=new HashMap<String,String[]>();
            paramMap.put("user_id",new String[]{user_id});
            paramMap.put("topic",new String[]{topic});
            paramMap.put("content",new String[]{content});
            paramMap.put("create_time",new String[]{create_time});
            paramMap.put("taskId",new String[]{"1154"});
            paramMap.put("cmdType", new String[]{"Update"});
            String res =new MainJsonService().invoke(paramMap);
            List<Map<String,Object>> list= StaticUtil.objectMapper.readValue(res,List.class);

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
