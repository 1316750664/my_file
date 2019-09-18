<%@ page import="java.util.logging.SimpleFormatter" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%--
  Created by IntelliJ IDEA.
  User: TTL
  Date: 2018/2/17
  Time: 13:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("utf-8");
    response.setHeader("content-type", "text/html;charset=utf-8");
%>

<html>
<head>
    <title></title>
</head>
<body>
<script type="text/javascript" src="js/tools.js" charset="utf-8"></script>
<form id="formdepot" method="post">
    <input type="hidden" name="post_id">
    <table class="table-edit" style="margin-top: 15px;" width="98%">
        <tr>
            <td width="110px"align="right">帖子名称</td>
            <td width="200px">
                <input type="text" name="topic" id="admin_topic"  style="width: 170px;" class="easyui-validatebox"readonly>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">正文</td>
            <td width="200px">
                <input type="password" name="content" id="admin_content"  style="width: 170px;" class="easyui-validatebox" readonly>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">发帖用户编号</td>
            <td width="200px">
                <input type="text" name="user_id" id="admin_user_id"  style="width: 170px;" class="easyui-validatebox" readonly>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">发帖用户名称</td>
            <td width="200px">
                <input type="text" name="user_name" id="admin_user_name"  style="width: 170px;" class="easyui-validatebox" readonly>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">是否加精</td>
            <td width="200px">
                <label><input type="radio" name="is_elite" id="is_elite1" value="1" style="height:22px; line-height:22px;"/>是</label>
                <label style="margin-left: 8px;"><input type="radio" name="is_elite" id="is_elite2" value="0" style="height:22px; line-height:22px;" checked/>否</label>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">点击数</td>
            <td width="200px">
                <input type="text" name="clicks" id="clicks"  style="width: 170px;" class="easyui-validatebox"readonly>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">回复数</td>
            <td width="200px">
                <input type="text" name="replies" id="replies"  style="width: 170px;" class="easyui-validatebox"readonly>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">创建时间</td>
            <td width="200px">
                <input type="text" name="create_time" id="admin_create_time"  style="width: 170px;" value=""readonly>
            </td>
        </tr>
    </table>

</form>
<script type="text/javascript">
    $('#formdepot').form({
        url: '/Action/adminBbsMod.do',
        onSubmit: function () {
            parent.$.messager.progress({
                title:'提示',
                text:'信息处理中，请稍后'
            });
            var isValid = $(this).form("validate");

            if (isValid) {
                return true;
            } else {
                parent.$.messager.progress("close");
                return false;
            }
        },
        success: function (msg) {

            parent.$.messager.progress('close');

            if (msg == "101")//缺少参数
            {
                parent.$.messager.alert('系统提示', '请输入完整的用户信息', 'info');
            }
            else if (msg == "102")//失败
            {
                parent.$.messager.alert('系统提示', '抱歉，用户账户重复，请重试', 'info');
            }
            else if (msg == "201")//正确
            {
                parent.reload;
                parent.$.modalDialog.openner.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner这个对象，是因为页面预定义好了
                parent.$.modalDialog.handler.dialog('close');
                parent.$.messager.alert('系统提示', '用户信息更新成功', 'info');
            }
            else if (msg == "301")//失败
            {
                parent.$.messager.alert('系统提示', '抱歉，用户信息更新失败，请重试', 'info');
            }
            else if (msg == "302")//登录超时
            {
                parent.$.messager.alert('系统提示', '前后密码不一致', 'info');
            }
            else if (msg == "402")//登录超时
            {
                parent.$.messager.alert('系统提示', '抱歉，您没有操作该功能的权限', 'info');
            }
            else if (msg == "501")//非法访问
            {
                parent.$.messager.alert('系统提示', '请勿非法访问', 'info');
            }
            else {
                parent.$.messager.alert('系统提示', '未知异常', 'error');
            }
        }
    });
</script>

</body>
</html>
