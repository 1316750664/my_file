<%--
  Created by IntelliJ IDEA.
  User: TTL
  Date: 2018/2/17
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>


<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("utf-8");
    response.setHeader("content-type", "text/html;charset=utf-8");

%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <title>论坛管理系统</title>
    <jsp:include page="/include/comScript.jsp" flush="true"/>
    <script type="text/javascript" src="/js/jquery.cookie.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/highcharts.js" charset="utf-8"></script>
    <script type=text/javascript src="/js/lightbox.js"></script>
    <script type=text/javascript src="/js/rotate.js"></script>
    <script type=text/javascript src="/js/jquery-photo-gallery/jquery.photo.gallery.js"></script>
</head>
<style type="text/css">
    #lt00 p {display: inline}
    .aa{float: right}
    #lt00 div .datagrid-row {
        height: 45px;
    }
</style>
<body class="easyui-layout">
<div region="north" split="false" border="false"style="background: url(#) repeat-x;height: 30px;line-height: 30px;color: #000000;font-family: '微软雅黑';font-size:14px;overflow: hidden;">
    <span style="float:right; padding-right:20px;">欢迎您<script type="text/javascript">document.write(sessionStorage.getItem('admin_name'));</script>
        <a href="javascript:void(0);" class="" id="editpass" onclick="modPwd();">修改密码</a>
        <a href="/Action/UserLoginOutAction.action" class="" id="loginOut">安全退出</a>
    </span>
    <span style="padding-left:10px;">
        <img src="/images/logo.png" width="40px" height="25px" align="absmiddle"/>欢迎进入论坛管理系统</span>
</div>

<div id="west" region="west" hide="true" split="true" border="false" title="导航菜单" style="width:180px;">
    <div id="menuAccordion">
        <div class="easyui-accordion" style="width:175px;height:589px; border:0px 0px;; ">
            <div title="用户管理" iconCls="icon-ok" style="overflow:auto;padding:10px;word-break:break-all; width:50px; overflow:auto;">

                管理已经注册的用户，可升级权限为管理。也可降级权限为普通用户。或是注册账号等等功能
            </div>
            <div title="帖子管理" iconCls="icon-reload" selected="true"  style="padding:10px;">
                管理相应的贴子，给帖子加精，删除不文明帖子等等
            </div>
        </div>
    </div>
</div>
<div id="mainPanle" region="center">
    <div id="tabs" class="easyui-tabs" fit="true" border="false">
        <div title="首页">
            <div id="lt00">
                <div id="lt0_center" data-options="region:'center',border:false">
                    <div id="toolbar">
                        <a id="view" href="javascript:void(0);" class="easyui-menubutton"
                           data-options="menu:'#menu1',iconCls:'icon-view'" plain="true">操作</a>

                        <div id="menu1" style="display: inline-block">
                            <div id="add" onclick="adduser();" data-options="iconCls:'icon-add'">新增用户</div>
                            <div id="mod" onclick="moduser();" data-options="iconCls:'icon-edit'">修改用户</div>
                            <div id="del" onclick="del();" data-options="iconCls:'icon-cancel'">删除用户</div>

                        </div>
                        <tr>
                            <td>用户名称</td>
                            <input id="user_name" name="user_name" style="width: 160px"/>
                        </tr>

                        <a id="search_status" href="javascript:void(0);" class="easyui-linkbutton" plain="true"
                           icon="icon-search">查询</a>
                    </div>
                    <table id="user_data"></table>
                </div>
                </div>
        </div>
        <div title="帖子管理">
            <div id="lt02">
                <div id="lt2_center" data-options="region:'center',border:false">
                    <div id="toolbar_post">
                        <a id="post_mod" href="javaScript:void(0)" onclick="post_mod()" class="easyui-linkbutton" plain="true"
                           icon="icon-edit">修改</a>
                        <a id="post_del" href="javaScript:void(0)" onclick="post_del()" class="easyui-linkbutton" plain="true"
                           icon="icon-cancel">删除</a>
                        <tr>
                            <td>用户名称</td>
                            <input id="post_name" name="post_name" style="width: 160px"/>
                        </tr>

                        <a id="search_post" href="javascript:void(0);" class="easyui-linkbutton" plain="true"
                           icon="icon-search">查询</a>
                    </div>
                    <table id="post_data" style="height: 580px;"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="tab_menu" class="easyui-menu" style="width:150px;display: none;">
    <div id="tab_update">刷新当前选项卡</div>
    <div class="menu-sep"></div>
    <div id="tab_close">关闭当前选项卡</div>
    <div id="tab_close_all">关闭全部选项卡</div>
    <div id="tab_close_other">关闭其它选项卡</div>
    <div class="menu-sep"></div>
    <div id="tab_close_left">当前左侧全部关闭</div>
    <div id="tab_close_right">当前右侧全部关闭</div>
</div>
<script type="text/javascript" src="/js/tools.js" charset="utf-8"></script>
<script type="text/javascript">

</script>
<script type="text/javascript">
    //用户管理
    function adduser() {
        var obj = {'title': '新增用户', 'url': '/zxx/useradd.jsp', 'width': 400, 'height': 300, 'formId': 'formdepot', 'grid': gridList};
        OpenDetailDlg(obj);
    }
    function moduser() {
        var selected = $('#user_data').datagrid('getSelected');
        /*console.log(selected)*/
        if (selected) {
            var obj = {'title': '修改用户', 'url': '/zxx/usermod.jsp?password2='+selected.password+'&user_id='+selected.user_id, 'width': 400, 'height': 500, 'formId': 'formdepot', row: selected, 'grid': gridList};
            OpenDetailDlg(obj);
        }else {
            parent.$.messager.alert('系统提示', '请先选择一行数据', 'info');
        }
    }
    function del() {
        var selected = $('#user_data').datagrid('getSelected');
        if (selected) {
            parent.$.messager.confirm('系统提示', '<span style="line-height: 20px;">您确定要删除该用户吗?<br>删除之后该用户的所有数据将一同删除！</span>', function (r) {
                if (r) {
                    if (selected.user_id == "") {
                        parent.$.messager.alert('系统提示', '抱歉，请先选择一行数据', 'warning');
                        return false;
                    }
                    /*console.log(selected.position_id);*/
                    $.post('/Action/adminUserDel.do?user_id=' + selected.user_id, function (msg) {
                        if (msg == "101")//缺少参数
                        {
                            parent.$.messager.alert('系统提示', '抱歉，请勿非法操作', 'info');
                        }
                        else if (msg == "201")//正确
                        {
                            parent.$.messager.alert('系统提示', '删除成功', 'info');
                            $("#user_data").datagrid('reload');
                            $("#user_data").datagrid('clearSelections');
                        }
                        else if (msg == "301")//失败
                        {
                            parent.$.messager.alert('系统提示', '抱歉删除失败，请重试', 'info');
                        }
                        else if (msg == "401")//登录超时
                        {
                            parent.$.messager.alert('系统提示', '抱歉，您无权操作此模块', 'info');
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
                    });
                }
            });
        }else {
            parent.$.messager.alert('系统提示', '请先选择一行数据', 'info');
        }
    }
    //帖子管理
    function post_mod() {
        var selected = $('#post_data').datagrid('getSelected');
        /*console.log(selected)*/
        if (selected) {
            var obj = {'title': '修改帖子', 'url': '/zxx/postmod.jsp?post_id='+selected.post_id, 'width': 400, 'height': 500, 'formId': 'formdepot', row: selected, 'grid': postList};
            OpenDetailDlg(obj);
        }else {
            parent.$.messager.alert('系统提示', '请先选择一行数据', 'info');
        }
    }
    function post_del() {
        var selected = $('#post_data').datagrid('getSelected');
        if (selected) {
            parent.$.messager.confirm('系统提示', '<span style="line-height: 20px;">您确定要删除该帖子?<br>删除之后该帖子的所有数据将一同删除！</span>', function (r) {
                if (r) {
                    if (selected.post_id == "") {
                        parent.$.messager.alert('系统提示', '抱歉，请先选择一行数据', 'warning');
                        return false;
                    }
                    /*console.log(selected.position_id);*/
                    $.post('/Action/adminBbsDel.do?post_id=' + selected.post_id, function (msg) {
                        if (msg == "101")//缺少参数
                        {
                            parent.$.messager.alert('系统提示', '抱歉，请勿非法操作', 'info');
                        }
                        else if (msg == "201")//正确
                        {
                            parent.$.messager.alert('系统提示', '删除成功', 'info');
                            $("#post_data").datagrid('reload');
                            $("#post_data").datagrid('clearSelections');
                        }
                        else if (msg == "301")//失败
                        {
                            parent.$.messager.alert('系统提示', '抱歉删除失败，请重试', 'info');
                        }
                        else if (msg == "401")//登录超时
                        {
                            parent.$.messager.alert('系统提示', '抱歉，您无权操作此模块', 'info');
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
                    });
                }
            });
        }else {
            parent.$.messager.alert('系统提示', '请先选择一行数据', 'info');
        }
    }
</script>
<script type="text/javascript">
    var gridList;
    var postList
    gridList = $('#user_data').datagrid({
        autoRowHeight: false,
        nowrap: true,
        striped: true,
        collapsible: true,//是否可折叠的
        iconCls: 'icon-save',//图标
        fit: true,
        fitColumns: false,
        pagination: true,
        singleSelect: true,
        toolbar: '#toolbar',
        rownumbers: true,
        showFooter: true,
        onLoadError: function (data) {
            $.messager.alert('系统提示', '抱歉，数据加载出错', 'info');
        },//数据加载出错
        onLoadSuccess: function () {

        },
        onClickRow: function (row) {

        },
        pageSize: 10,
        remoteSort: false,
        url: '/Action/adminUserQuery.do?',
        columns: [
            [
                /*   {field: 'cc', title: '', width: 20, align: 'center', checkbox: true},*/
                {field: 'user_id', title: '用户编号', width: 120, align: 'center'},
                {field: 'user_name', title: '用户名称', width: 140, align: 'center' },
                {field: 'email', title: '邮箱', width: 180, align: 'center'},//出入
                {field: 'is_root', title: '是否管理', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        if (value == "1") {
                            return "是";
                        }
                        else {
                            return "否";
                        }

                    }},
                {field: 'age', title: '年龄', width: 80, align: 'center'},
                {field: 'sex', title: '性别', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        if (value == "1") {
                            return "女";
                        }
                        else {
                            return "男";
                        }

                    }},
                {field: 'likes', title: '爱好', width: 150, align: 'center'},
                {field: 'journal', title: '个性签名', width: 150, align: 'center'},
                {field: 'create_timestamp', title: '创建时间', width: 120, align: 'center'}

            ]
        ]
    });
    postList = $('#post_data').datagrid({
        autoRowHeight: false,
        nowrap: true,
        striped: true,
        collapsible: false,//是否可折叠的
        iconCls: 'icon-save',//图标
        fit: false,
        fitColumns: false,
        pagination: true,
        singleSelect: true,
        toolbar: '#toolbar_post',
        rownumbers: true,
        showFooter: true,
        onLoadError: function (data) {
            $.messager.alert('系统提示', '抱歉，数据加载出错', 'info');
        },//数据加载出错
        onLoadSuccess: function () {

        },
        onClickRow: function (row) {

        },
        pageSize: 10,
        remoteSort: false,
        url: '/Action/adminBbsQuery.do?',
        columns: [
            [
                /*   {field: 'cc', title: '', width: 20, align: 'center', checkbox: true},*/
                {field: 'post_id', title: '用户编号', width: 120, align: 'center'},
                {field: 'user_id', title: '用户名称', width: 140, align: 'center' },
                {field: 'topic', title: '标题', width: 150, align: 'center'},//出入
                {field: 'content', title: '正文', width: 300, align: 'center' },
                {field: 'clicks', title: '点击数', width: 80, align: 'center'},
                {field: 'replies', title: '回帖数', width: 80, align: 'center'},
                {field: 'is_elite', title: '是否加精', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        if (value == "1") {
                            return "是";
                        }
                        else {
                            return "不是";
                        }

                    }},
                {field: 'create_time', title: '创建时间', width: 150, align: 'center'}

            ]
        ]
    });
    $('#search_status').bind("click", function () {
        $('#user_data').datagrid('load', {user_name: $("#user_name").val()});//重载
    });
$(document).ready(function(e){
    var user_id=$("#muser_id").val();
    if(user_id!=""||user_id!=null){
        getMessage();
        function getMessage(){
            //var mydate = new Date();
            //var mytime = mydate.toLocaleString();
            //console.info(mytime);

            setTimeout(getMessage,5*60*1000)
        }
    }
    var w = $(document).width();
    var h = $(document).height();
    $("#lt0_center").css("width", (w/2+90)+"px");
    $("#lt0_east").css("width", (w/2-90) + "px");

    $("#lt0_east_lt0_center").css("height", 280 + "px");
    $("#lt0_east_lt0_south").css("height", 280 + "px");
    $("#lt0_center_lt0_center").css("height", 280+ "px");
    $("#lt0_center_lt0_south").css("height", 280 + "px");
    $("#lt0_center_lt0_north").css("height", 280+ "px");
    $("#lt00").layout({
        fit: true,
        border: false
    });

    /*$("#lt0_center_lt0").layout({
     fit: true,
     border: false
     });

     $("#lt0_east_lt0").layout({
     fit: true,
     border: false
     });*/



});
</script>
<script type="text/javascript">
    function find(str,cha,num){    var x=str.indexOf(cha);    for(var i=0;i<num;i++){        x=str.indexOf(cha,x+i);    }    return x;    }
</script>
<script type="text/javascript">
    function Refresh(table){
        $(table).datagrid('reload')
    }
</script>
<script type="text/javascript">
    function showMessage(task_type,task_name,task_template,max,count){
        if(parseInt(max)<=parseInt(count)){

        }else{
            //var last_time=getNowFormatTime;
            $.messager.show({
                title: task_name,
                msg: task_template,
                timeout: 10000,
                showType: 'slide'
            });
            //$.post("/Action/RemindAction.do",{action:"update",task_type:task_type,count:count,last_time:last_time})
        }
    }
</script>
</body>
</html>
