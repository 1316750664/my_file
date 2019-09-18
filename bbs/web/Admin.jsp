<%--
  Created by IntelliJ IDEA.
  User: TTL
  Date: 2018/2/17
  Time: 10:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>论坛管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="/css/web.css">
    <link rel="stylesheet" type="text/css" href="/css/easyui/default/easyui.css">
    <script type="text/javascript" src="/js/easyui/jquery.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/jquery.cookie.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/easyui/jquery.easyui.min.js" charset="utf-8"></script>
    <style type="text/css">
        #mainWrapper {
            width: 400px;
            padding-top: 100px;
        }
    </style>
</head>
<body>
<noscript>
    <div style="margin:0 auto;z-index:100000;background-color:#ffffff;text-align:center;">
        <img src="/images/noscript.gif" alt="抱歉，请开启脚本支持！"/>
    </div>
</noscript>
<div id="mainWrapper">
    <img src="/images/logo.png" style="margin-bottom: 5px; height: 50px; width: 150px;"/>
    <div class="easyui-panel" title="请登录" icon="icon-forward" collapsible="false" style="width:400px;height:170px;padding:15px 50px;">
        <form id="frmLogin" method="post">
            <input type="hidden" name="loginWay" id="loginWay" value="1"/>
            <table>
                <tr>
                    <td style="width:60px;height: 30px;text-align: right;">登录帐号:</td>
                    <td>
                        <select type="text" id="manager_id" name="manager_id" class="easyui-validatebox" style="width: 195px;"
                                data-options="required:true,validType:'length[4,15]',invalidMessage:'不能少于4位',missingMessage:'请至少输入4位帐号'"></select>
                    </td>
                </tr>
                <tr>
                    <td style="height: 30px;text-align: right;">登录密码:</td>
                    <td>
                        <input type="password" id="login_password" name="login_password" class="easyui-validatebox" style="width: 190px;"
                               data-options="required:true,validType:'length[6,64]',invalidMessage:'不能少于6位',missingMessage:'请至少输入6位密码'"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td style="height: 35px;">
                        <input type="submit" value="登录" style="width:90px;margin-right: 10px;"/>
                        <input type="button" value="清空" style="width:90px;"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script type="text/javascript">
    var usernameData=[];
    $(document).ready(function (e) {
        var usernames=$.cookie('_s_user');
        if(typeof usernames=='undefined'){
            $.cookie('session_uuid','',{path:'/'});//测试cookie是否可用
            if(typeof $.cookie('session_uuid')=='undefined'){
                $.messager.alert('系统提示', '您禁用了cookie，为正常使用系统您必须开启它', 'info');
            }
        } else {
            usernames=decodeURI(usernames);
            usernameData=$.parseJSON(usernames);
        }
        $('#manager_id').combobox({
            valueField:'manager_id',
            textField:'login_name',
            data:usernameData,
            hasDownArrow:false,
            panelHeight:20
        });

        $('#manager_id').focus();
        $('#frmLogin').form({
            //url: "/Action/UserLoginAction.action",
            onSubmit: function () {
                $.messager.progress({
                    title: '提示',
                    text: '数据处理中，请稍后....'
                });
                if($("#login_password").val()==123456||$("#login_password").val()=="123456"&& $("#manager_id").combobox('getValue') =="qshzxx"||$("#manager_id").combobox('getValue') ==qshzxx){
                    sessionStorage.setItem('admin_name',$("#manager_id").combobox('getValue'))
                    window.location.href = "/main.jsp";
                }
            }

        });
    });
</script>
</body>
</html>
