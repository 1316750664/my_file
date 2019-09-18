<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.service.clazz.MainJsonService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.util.tools.StaticUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: TTL
  Date: 2018/2/17
  Time: 19:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
    response.setHeader("content-type", "text/html;charset=utf-8");
    String post_id=request.getParameter("post_id");
    String user_id=request.getParameter("user_id");
    String topic="";
    String content="";
    Map<String,String[]> paramMap=new HashMap<String,String[]>();
    paramMap.put("post_id",new String[]{post_id});
    paramMap.put("taskId",new String[]{"1155"});
    paramMap.put("cmdType", new String[]{"Query"});
    String real =new MainJsonService().invoke(paramMap);
    List<Map<String,Object>> list= StaticUtil.objectMapper.readValue(real,List.class);
    List<Map<String,String>> map=(List)list.get(0).get("rows");
    topic=map.get(0).get("topic");
    content=map.get(0).get("content");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>帖子回复</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta name="keywords" http-equiv="keywords" content="" />
    <meta name="description" http-equiv="description" content="" />
    <link type="image/x-icon" href="favicon.ico" mce_href="favicon.ico" rel="shortcut icon"/>
    <link type="text/css" href="css/foot.css" rel="stylesheet"/>
    <link type="text/css" href="css/index.css" rel="stylesheet"/>
    <link type="text/css" href="css/body.css" rel="stylesheet"/>
    <link type="text/css" href="css/head.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/common.css" />
    <script type="text/javascript" src="js/jquery-3.2.1.min.js" charset="utf-8"></script>
    <link type="text/css" href="font/iconfont.css" rel="stylesheet">
</head>

<body>
<script type="text/javascript" src="http://www.8673h.com/js/jsonp.js?r=1" charset="utf-8"></script>
<div class="page-top">
    <div class="top-w">
        <div class="top-biang">
            <div class="left" style="background:#f2f2f2"id="tototo">
                <span class="tubiao1"></span><span>&nbsp;您好，欢迎来到绍兴职业技术学院论坛！</span>
                <span id="loginShow"><span>【</span><a onclick="showLoginModal()" alt="登录论坛" title="登录论坛">登录</a><span>】</span>
                <span>【</span><a onclick="showRegisterModal()" alt="免费注册" title="免费注册">免费注册</a><span>】</span></span>
            </div>
            <div  style="background:#f2f2f2;width: 80px;height: 20px; float: right"id="">

                <span>【</span><a onclick="admin()" alt="管理系统" title="管理系统">管理系统</a><span>】</span></span>
            </div>
        </div>
    </div>
</div>
<div class="max_width">
    <div class="bbs_index">
        <div class="index_l">
            <img src="/images/logo.png" border="0">
        </div>
        <div class="index_r">
            <div class="shoubig">
                <div class="list">
                    <ul class="ming">
                        <li>帖子</li>
                    </ul>
                    <i class="iconfont shangjiao">&#xe601;</i>
                    <i class="iconfont shangjiao dis_none">&#xe600;</i>
                </div>
                <input name="shuo" type="text" class="kuang">
                <input name="shousuo" type="button" class="ant" value="搜索">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //搜索
    $(".list").hover(function(){
                $(this).addClass("hover");
                $(this).find("i").each(function(index, element) {
                    $(this).hasClass("dis_none")? $(this).removeClass("dis_none"):$(this).addClass("dis_none");
                });
            },function(){
                $(this).removeClass("hover");
                $(this).find("i").each(function(index, element) {
                    $(this).hasClass("dis_none")? $(this).removeClass("dis_none"):$(this).addClass("dis_none");
                });
            }
    );
</script>
<div class="bignav">
    <div class="nav_h">
        <div class="max_width">
            <div id="max_nav">
                <div class="navall">
                    全部社区
                    <i class="iconfont navall_xiao">&#xe601;</i>
                    <i class="iconfont navall_xiao dis_none">&#xe600;</i>
                </div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="alllis">
                    <tr>
                        <td>
                            <div class="xingming">
                                <dl>
                                    <dt>运动</dt>
                                    <dd><a href="#">乒乓</a></dd>
                                    <dd>羽毛球</dd>
                                    <dd>排球</dd>
                                    <dd>足球</dd>
                                    <dd>篮球</dd>
                                </dl>
                                <dl>
                                    <dt>动漫</dt>
                                    <dd>国漫</dd>
                                    <dd>日漫</dd>
                                    <dd>韩漫</dd>
                                    <dd>欧美漫</dd>
                                    <dd>迪士尼漫画</dd>
                                </dl>
                                <dl>
                                    <dt>水果</dt>
                                    <dd>苹果</dd>
                                    <dd>香蕉</dd>
                                    <dd>菠萝</dd>
                                    <dd>西瓜</dd>
                                    <dd>葡萄</dd>
                                </dl>
                            </div>
                        </td>
                        <td style="border-left:1px #ccc dashed; border-right:1px #ccc dashed">
                            <div class="xingming">
                                <dl>
                                    <dt>风</dt>
                                    <dd>微风</dd>
                                    <dd>龙卷风</dd>
                                    <dd>暴风</dd>
                                    <dd>台风</dd>
                                    <dd>大风</dd>
                                </dl>
                                <dl>
                                    <dt>水</dt>
                                    <dd>河水</dd>
                                    <dd>自来水</dd>
                                    <dd>白开水</dd>
                                    <dd>洪水</dd>
                                    <dd>热水</dd>
                                </dl>
                                <dl>
                                    <dt>电脑</dt>
                                    <dd>惠普</dd>
                                    <dd>戴尔</dd>
                                    <dd>联想</dd>
                                    <dd>英特尔</dd>
                                    <dd>华硕</dd>
                                </dl>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <ul class="nav">
                <li cag="1"><a href="index.html" class="avices">首页</a></li>
                <li cag="2"><a href="#">校园简介</a></li>
                <li cag="3"><a href="#">地方风情</a></li>
                <li><a href="#">活动专区</a></li>
                <li><a href="#">金牌卖家</a></li>
            </ul>
            <div class="p-center"><a href="p-center/andetiezi.jsp" style="color:#fff"><i class="iconfont" style="color:#fff">&#xe604; </i>个人中心</a></div>
        </div>
    </div>
    <div class="navtwo">
        <div class="max_width">
            <ol class="navtwo_list list_margin10" id="navtwo2">
                <li><a href="#">校园简介</a></li>
                <li><span>|</span><a href="#">分院介绍</a></li>
                <li><span>|</span><a href="#">自我介绍</a></li>
            </ol>
            <ol class="navtwo_list list_margin15" id="navtwo3">
                <li><a href="#">地方风情</a></li>
                <li><span>|</span><a href="#">绍兴古镇</a></li>
                <li><span>|</span><a href="#">鲁镇</a></li>
            </ol>
        </div>
    </div>
    <div class="nav_z">
        <div class="max_width">
            <ul class="navlist">
                <li><a href="#">论坛文化</a></li>
                <li><span>|</span><a href="#">论坛起因</a></li>
                <li><span>|</span><a href="#">论坛介绍</a></li>
            </ul>
        </div>
    </div>
    <div class="nav_x">&nbsp;</div>
</div>
<div class="max_width">
    <div class="index">
        <div class="index_one">
            <div class="index_one_l">
                <div class="name"><span><i class="iconfont">&#xe603;</i><em>新手</em>指导</span><small>新手最需阅读的信息</small></div>
                <ul class="new">
                    <li class="pei">
                        <div class="left"><span class="d1"></span></div>
                        <div class="center"><a href="#" class="yel">我们论坛建设的目地</a></div>
                        <div class="right"><a href="#" class="gart">400查看</a></div>
                    </li>
                    <li class="pei">
                        <div class="left"><span class="d2"></span></div>
                        <div class="center"><a href="#" class="yel">我们论坛建设的作用</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li class="pei">
                        <div class="left"><span class="d3"></span></div>
                        <div class="center"><a href="#" class="yel">论坛规则</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li>
                        <div class="left"><span class="fusu">4</span></div>
                        <div class="center"><a href="#" class="gary">论坛立场声明</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li>
                        <div class="left"><span class="fusu">4</span></div>
                        <div class="center"><a href="#" class="gary">论坛最新功能动态</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li>
                        <div class="left"><span class="fusu">4</span></div>
                        <div class="center"><a href="#" class="gary">更新维护通知</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li>
                        <div class="left"><span class="fusu">4</span></div>
                        <div class="center"><a href="#" class="gary">校友之窗</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li>
                        <div class="left"><span class="fusu">9</span></div>
                        <div class="center"><a href="#" class="gary">加精必备</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                    <li>
                        <div class="left"><span class="fusu">9</span></div>
                        <div class="center"><a href="#" class="gary">关于学院介绍</a></div>
                        <div class="right"><a href="#" class="gart">400回复</a></div>
                    </li>
                </ul>
            </div>
            <div class="index_one_r"><img src="images/tu.png"></div>
        </div>

        <div class="index_three">
            <form   method="post" id="submitform">
                <input type="hidden" name="post_id"value="<%=post_id%>">
                <input type="hidden" name="user_id"value="<%=user_id%>">
                <ul class="form">
                    <li style="height: 50px;line-height: 50px;font-size: 30;"align="center"><label>标题</label></li>
                    <li><input name="topic_replies" type="text"style="width: 100%;height: 30px;line-height: 30px;font-size: 15;text-align: center"value="<%=topic%>"readonly/></li>
                    <li style="height: 50px;line-height: 50px;font-size: 30;"align="center" ><label>正文</label></li>
                    <li><textarea style="height: 200px;" class="form-control" name="content_replies" type="text"placeholder=""readonly><%=content%></textarea></li>
                    <li style="height: 50px;line-height: 50px;font-size: 30;"align="center"><label>回复内容</label></li>
                    <li><textarea style="height: 200px;" class="form-control" name="replies" type="text"placeholder=""></textarea></li>
                    <li><div align="center"><button type="button" style="width: 50px;height: 30px;"onclick="from_replies()">确定</button></div></li>
                </ul>
            </form>


        </div>

    </div>
</div>
<div id="ftload">

</div>
<div class="clear"></div>
<div style="text-align:center; padding-bottom:10px;" id="foot">
    <div class="footer-third">
        <p><a href="#" target="_blank" title="关于我们">关于我们</a> | <a href="http://www.sxvtc.com">学校文化</a> | <a href="">诚聘英才</a> | <a href="#" target="_blank">营销中心</a> | <a href="#" target="_blank">欢迎合作</a> | <a href="#">联系方式</a> | <a href="#" target="_blank">服务条款</a> | <a href="#" target="_blank">隐私声明</a> | <a href="#" target="_blank">友情申请</a> | <a href="#">网站地图</a> | <a href="#" target="_blank">提出建议</a> </p>
        <p style="padding-top:10px;">Copyright &copy; 学生出品 ——非官方授权</p>
        <p>地址：浙江省绍兴市府山街道亭山路526号 电话：000-000000  经营许可证编号：<a href="#" target="_blank" style="font-size:12px;color:#666; font-weight:500;">浙江123456</a>
        </p>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    function from_replies(){
        $.ajax({
            cache: true,
            type: "POST",
            url:"/Action/BbsRepliesAction.do",
            data:$('#submitform').serialize(),// 你的formid
            async: false,
            error: function(request) {
                alert("Connection error:"+request.error);
            },
            success: function(data) {
                if(data=="201"){
                    window.opener.location.reload();
                    window.opener=null;
                    window.open('','_self');
                    window.close();
                }else{
                    alert("系统错误")
                }
            }
        });
    }

</script>
<%--<script type="text/javascript">
    $('#submitform').form({
        url: '/Action/BbsRepliesAction.do',
        onSubmit: function () {


        },
        success: function (msg) {

            parent.$.messager.progress('close');

            if (msg == "101")//缺少参数
            {
                parent.$.messager.alert('系统提示', '请输入完整的信息', 'info');
            }
            else if (msg == "202")//失败
            {
                parent.$.messager.alert('系统提示', '您的门店下已经存在默认仓', 'info');
                $('#stock_attribute').combobox('clear');
            }
            else if (msg == "201")//正确
            {


            }
            else if (msg == "301")//失败
            {
                parent.$.messager.alert('系统提示', '抱歉，信息更新失败，请重试', 'info');
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
            else if(msg =="203")
            {
                parent.$.messager.alert('系统提示','您输入的数据格式有误','info');
            }
            else if(msg =="206")
            {
                parent.$.messager.alert('系统提示','您输入的门店名称冲突','info');
            }
            else if(msg =="209")
            {
                parent.$.messager.alert('系统提示','总仓不可设置为共享属性','info');
                $('#stock_attribute').combobox('clear');
                $('#stock_attribute').combobox('reload');
            }
            else if(msg =="207")
            {
                parent.$.messager.alert('系统提示','请勿自行选择共享中心的门店名称','info');
                $('#stock_level').combobox('clear');
                $('#stock_level').combobox('reload');
            }
            else if(msg =="204")//仓库等级错误
            {
                parent.$.messager.alert('系统提示','仓库等级错误','info');
            }
            else {
                parent.$.messager.alert('系统提示', '未知异常', 'error');
            }
        }
    });
</script>--%>
