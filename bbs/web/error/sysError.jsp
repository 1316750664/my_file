<%@ page import="com.service.Service.ComHtmlHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%
    String code = request.getParameter("code");
    String message = request.getParameter("message");
    String gotoUrl = request.getParameter("gotoUrl");
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>访问失败</title>
<link type="text/css" rel="stylesheet" href="../css/shared.css"/>
<link type="text/css" rel="stylesheet" href="../css/foot.css"/>
<link type="text/css" href="/css/head.css" rel="stylesheet"/>
<link type="text/css" href="/css/foot.css" rel="stylesheet"/>
<style>
    .failed{width:470px;height:220px; margin:0 auto; padding:60px 0;}
    .failed .part1{width:370px;float:left; }
.failed .part1 img{ float:left}
.failed .part1 h2{padding-left:10px;color:#C00; float:left;font:600 22px/48px Microsoft yahei;}
.failed ul{width:470px;padding:30px 0 15px 0;;float:left}
.failed ul li{width:470px; height:28px; font:bold 14px/28px "宋体";color:#244153}
.failed ul li span{ float:left}
.failed ul li p{width:300px; float:left}
.failed a{width:74px; height:26px; text-align:center; line-height:26px; color:#fff; margin-left:180px;float:left;cursor:pointer; background:url(/images/faliedbt.png)}

</style>
</head>
<body>
<%
    out.write(ComHtmlHandler.getInstance().htmlHandler(4));
    out.flush();
%>
<div class="max_width">
    <div class="failed">
        <div class="part1">
            <img src="../images/net_48.png" width="48" height="48" />
            <h2>系统错误！</h2>
        </div>
        <ul>
            <li><span>提示代码：</span><p><%=code%></p></li>
            <li><span>提示信息：</span><p><%=message%></p></li>
        </ul>
        <a href="<%=gotoUrl%>">返回</a>
    </div>
</div>
<%
    out.write(ComHtmlHandler.getInstance().htmlHandler(3));
    out.flush();
%>
</body>
</html>
