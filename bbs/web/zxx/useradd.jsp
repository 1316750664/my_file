<%@ page import="java.util.logging.SimpleFormatter" %>
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
    long time=System.currentTimeMillis();
%>

<html>
<head>
    <title></title>
</head>
<body>
<script type="text/javascript" src="js/tools.js" charset="utf-8"></script>
<form id="formdepot" method="post">
    <table class="table-edit" style="margin-top: 15px;" width="98%">
        <tr>
            <td width="110px"align="right">用户名称</td>
            <td width="200px">
                <input type="text" name="admin_user_name" id="admin_user_name"  style="width: 170px;" class="easyui-validatebox"data-options="required:true">
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">输入密码</td>
            <td width="200px">
                <input type="password" name="admin_password" id="admin_password"  style="width: 170px;" class="easyui-validatebox" data-options="required:true">
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">确认密码</td>
            <td width="200px">
                <input type="password" name="admin_password2" id="admin_password2"  style="width: 170px;" class="easyui-validatebox"data-options="required:true">
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">邮箱</td>
            <td width="200px">
                <input type="text" name="admin_email" id="admin_email"  style="width: 170px;" class="easyui-validatebox">
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">管理</td>
            <td width="200px">
                <label><input type="radio" name="is_usable" id="is_usable1" value="1" style="height:22px; line-height:22px;"/>是</label>
                <label style="margin-left: 8px;"><input type="radio" name="is_usable" id="is_usable2" value="0" style="height:22px; line-height:22px;" checked/>否</label>
            </td>
        </tr>
        <tr>
            <td width="110px"align="right">创建时间</td>
            <td width="200px">
                <input type="text" name="admin_create_time" id="admin_create_time"  style="width: 170px;" value="<%=time%>"readonly>
            </td>
        </tr>
    </table>

</form>
<script type="text/javascript">
    $('#formdepot').form({
        url: '/Action/adminUserAdd.do',
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
