<%@ page import="com.bean.TaskBean" %>
<%@ page import="com.service.fragService.FragService" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="com.util.tools.ReadWriteProperties" %>
<%@ page import="org.codehaus.jackson.map.ObjectMapper" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.service.Service.Bbs.BbsTaskService" %>
<%@ page import="com.util.tools.ToolUtil" %>
<%@ page import="com.service.Service.ComHtmlHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <title>绍职论坛——绍兴职业技术学院</title>
    <meta charset="utf-8">
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
    <link type="text/css" href="font/iconfont.css" rel="stylesheet">
    <script type="text/javascript" src="js/jquery.min.js" charset="utf-8"></script>

</head>
<body>
<%
    out.write(ComHtmlHandler.getInstance().htmlHandler(4));
    out.flush();
%>
<div class="max_width">
<div class="index">
<div class="index_one">
    <%
        String serverCache = ReadWriteProperties.getInstance().readValue("config", "img_domain");//图片服务器
        int position = 1;
        int cmdType = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        String all = FragService.getInstance().fragment(0, new TaskBean(42,cmdType));//所有记录
        List<Map<String,String>> list1 = objectMapper.readValue(all,List.class);
        Map<String,String> map = list1.get(position - 1);//从0开始
//        String read_num = map.get("read_num");
        String position_name = map.get("position_name");
        String remark = map.get("remark");
//        String auto_load = map.get("auto_load");
//        String ids = map.get("ids");
        String main_img = serverCache+map.get("main_img");
//        String sort_field = map.get("sort_field");
    %>
    <div class="index_one_l">
        <div class="name"><span><i class="iconfont">&#xe603;</i><em><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></span><small><%=remark%></small></div>
        <ul class="new">
            <%
                String fragPath = request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt";
                BbsTaskService bbsTaskService = BbsTaskService.getInstance();
                String json = bbsTaskService.taskMap(list1.get(position - 1),fragPath);
                Pattern pattern = Pattern.compile("###num###");
                Matcher matcher = pattern.matcher(json);
                String str = null;
                String str1 = null;
                int i = 1;
                while (matcher.find()) {
                    str1 = matcher.group();//匹配部分，最后替换掉
//                    str = matcher.group(1);//匹配的值
                    json = json.replaceFirst(str1,""+i);
                    i++;
                }
                out.write(json);
                out.flush();
            %>
        </ul>
    </div>
    <% // 2
        position = 2;
        map = list1.get(position - 1);
        main_img = serverCache+map.get("main_img");
        out.write("<div class=\"index_one_r\"><img src=\""+main_img+"\"></div>");
        out.flush();
    %>
</div>
<%
    position = 3;
    map = list1.get(position - 1);
    position_name = map.get("position_name");
    main_img = map.get("main_img");
%>
<div class="index_two">
    <div class="index_two_width" style="margin-right:10px;">
        <span><em><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></span>
        <div class="nei">
            <%
                String text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                String localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
                text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
                out.write(text);
                out.flush();
            %>
        </div>
    </div>
    <div class="index_two_width" style="padding:0 10px; border-left:1px #ccc dashed;border-right:1px #ccc dashed">
        <ul class="tabs" id="tabs">
            <%
                position = 4;
                map = list1.get(position - 1);
                position_name = map.get("position_name");
                text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
                //时间
                text = ToolUtil.bbsReplaceTime(text,"MM月dd日");
            %>
            <li><a href="javascript:void(0)" tab="1" class="current"><%=position_name%></a></li>
            <%
                position = 5;
                map = list1.get(position - 1);
                position_name = map.get("position_name");
            %>
            <li><a href="javascript:void(0)" tab="2"><%=position_name%></a></li>
        </ul>
        <ul class="tab_conbox">
            <li id="tab1" >
                <div class="nei">
                    <%
                        out.write(text);
                        out.flush();
                    %>
                </div>
            </li>
            <li id="tab2" class="tab_con">
                <div class="nei">
                    <%
                        text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                        //图片
                        text = ToolUtil.bbsReplaceImg(serverCache,localImg, main_img, text);
                        //时间
                        text = ToolUtil.bbsReplaceTime(text, "MM月dd日");
                        out.write(text);
                        out.flush();
                    %>
                </div>
            </li>
        </ul>
    </div>
    <div class="index_two_width" style="margin-left:10px;">
        <%
            position = 6;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
        %>
        <span><b class="yel"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></b><%=position_name.substring(2)%></span>
        <div class="nei">
            <%
                text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
                text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
                out.write(text);
                out.flush();
            %>
        </div>
    </div>
</div>
<div class="index_three">
    <div class="left">
        <%
            position = 7;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
            main_img = map.get("main_img");
        %>
        <span><em><i class="iconfont">&#xe602;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></span>
        <ul class="nei">
            <%
                fragPath = request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt";
                json = bbsTaskService.taskMap(list1.get(position - 1),fragPath);
                json = ToolUtil.bbsReplaceImg(serverCache,localImg, main_img, json);
                json = ToolUtil.bbsReplaceTime(json,"yyyy-MM-dd HH:mm:ss");
                out.write(json);
                out.flush();
            %>
        </ul>
    </div>
    <ul class="right">
        <%
            position = 8;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
            main_img = map.get("main_img");
        %>
        <li>
            <h4><em class="yel"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
            <img src="<%=main_img%>" border="0">
            <%
                fragPath = request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt";
                json = bbsTaskService.taskMap(list1.get(position - 1),fragPath);
                out.write(json);
                out.flush();
            %>
        </li>
        <%
            position = 9;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
            main_img = map.get("main_img");
        %>
        <li>
            <h4><em class="yel"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
            <img src="<%=main_img%>" border="0">
            <%
                fragPath = request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt";
                json = bbsTaskService.taskMap(list1.get(position - 1),fragPath);
                out.write(json);
                out.flush();
            %>
        </li>
        <%
            position = 10;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
            main_img = map.get("main_img");
        %>
        <li>
            <h4><em class="red"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
            <%
                text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
                text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
                out.write(text);
                out.flush();
            %>
        </li>
        <%
            position = 11;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
            main_img = map.get("main_img");
        %>
        <li>
            <h4><em class="red"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
            <%
                text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
                text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
                out.write(text);
                out.flush();
            %>
        </li>
    </ul>
</div>
<div class="index_four">
<ul class="index_four_width">
    <%
        position = 12;
        map = list1.get(position - 1);
        position_name = map.get("position_name");
        remark = map.get("remark");
        main_img = map.get("main_img");
    %>
    <li class="nei_padding">
        <div class="top"><%=position_name%><a href="javascript:void(0);" class="yel"><%=remark%></a></div>
        <div class="bottom">
            <%
                text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
                localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
                text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
                out.write(text);
                out.flush();
            %>
        </div>
    </li>
    <%
        position = 13;
        map = list1.get(position - 1);
        position_name = map.get("position_name");
        remark = map.get("remark");
        main_img = map.get("main_img");
    %>
    <li style="padding:0 10px; border-left:1px #ccc dashed;border-right:1px #ccc dashed">
        <h4><em class="bule"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
        <%
            text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
            localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
            text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
            out.write(text);
            out.flush();
        %>
    </li>
    <%
        position = 14;
        map = list1.get(position - 1);
        position_name = map.get("position_name");
        remark = map.get("remark");
        main_img = map.get("main_img");
    %>
    <li style="padding-left:10px;">
        <h4><em class="bule"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
        <%
            text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
            localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
            text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
            out.write(text);
            out.flush();
        %>
    </li>
</ul>
<ul class="index_four_width nei_padding_t">
    <li class="nei_padding">
        <%
            position = 15;
            map = list1.get(position - 1);
            position_name = map.get("position_name");
            main_img = map.get("main_img");
        %>
        <h2><em><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h2>
        <%
            text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
            //图片
            text = ToolUtil.bbsReplaceImg(serverCache,localImg, main_img, text);
            //第三样式去掉
            pattern = Pattern.compile("###yang:(.*?)###");
            matcher = pattern.matcher(text);
            i = 0;
            while(matcher.find()){
                str1 = matcher.group();//匹配部分
                str = matcher.group(1);
                if(i == 2){
                    text = text.replaceFirst(str1,"");
                }else{
                    text = text.replaceFirst(str1,str);
                }
                i++;
            }
            out.write(text);
            out.flush();
        %>
    </li>
    <%
        position = 16;
        map = list1.get(position - 1);
        position_name = map.get("position_name");
        remark = map.get("remark");
        main_img = map.get("main_img");
    %>
    <li style="padding:0 10px; border-left:1px #ccc dashed;border-right:1px #ccc dashed">
        <h4><em class="bule"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
        <%
            text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
            localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
            text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
            out.write(text);
            out.flush();
        %>
    </li>
    <%
        position = 17;
        map = list1.get(position - 1);
        position_name = map.get("position_name");
        remark = map.get("remark");
        main_img = map.get("main_img");
    %>
    <li style="padding-left:10px;">
        <h4><em class="bule"><i class="iconfont">&#xe603;</i><%=position_name.substring(0,2)%></em><%=position_name.substring(2)%></h4>
        <%
            text =bbsTaskService.taskMapSomePic(map,request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+"_p.txt",request.getSession().getServletContext().getRealPath("/frag")+ "/index_position_"+position+".txt",main_img);
            localImg = ReadWriteProperties.getInstance().readValue("imageConfig","no_pic");
            text = ToolUtil.bbsReplaceImg(serverCache,localImg,main_img,text);
            out.write(text);
            out.flush();
        %>
    </li>
</ul>
</div>
</div>
</div>
<%
    out.write(ComHtmlHandler.getInstance().htmlHandler(3));
    out.flush();
%>
<script type="text/javascript" src="js/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="js/jquery.cookie.js" charset="utf-8"></script>
<script type="text/javascript" src="js/head.js" charset="utf-8"></script>
<script type="text/javascript">
   /* $("#tabs li a").each(function(index, element) {
        $(this).hover(function(){
            $("#tabs li a").removeClass("current");
            $(".tab_conbox li").addClass("tab_con");
            var zhi=parseInt($(this).attr("tab"));
            $("#tab"+zhi).removeClass("tab_con");
            $(this).addClass("current");
        });
    });
    $(".tab_conbox li").each(function(index, element) {
        $(this).hover(function(){
            $(".tab_conbox li").addClass("tab_con");
            $(this).removeClass("tab_con");
        })
    });
    //选项卡
    $(document).ready(function() {
        jQuery.jqtab = function(tabtit,tabcon) {
            $(tabcon).hide();
            $(tabtit+" li:first").addClass("thistab").show();
            $(tabcon+":first").show();
            $(tabtit+" li").hover(function() {
                $(tabtit+" li").removeClass("thistab");
                $(tabtit+" li a").removeClass("current");
                $(this).addClass("thistab");
                $(this).children("a").addClass("current");
                $(tabcon).hide();
                var activeTab = $(this).find("a").attr("tab");
                $("#"+activeTab).fadeIn();
                return false;
            });
        };
    });
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
    $(".ming li").each(function(index, element) {
        $(this).click(function(){
            var index=$(this).index();
            var bbj=$(".ming li").eq(0).text();
            var btr=$(".ming li").eq(0).attr("data-id");
            var abj=$(this).text();
            var atr=$(this).attr("data-id");
            if(index != 0){
                $(".ming li").eq(0).text(abj);
                $(".ming li").eq(0).attr("data-id",atr);
                $(".ming li").eq(1).text(bbj);
                $(".ming li").eq(1).attr("data-id",btr);
            }
        });
    });
    //头部二级导航
    $(".nav li").each(function(index, element) {
        $(this).mouseenter(function(e){
            $(".navtwo_list").css("display","none");
            $(".nav li").removeClass("nav_bg");
            var index = $(this).index();
            var cg = $(this).attr("cag");
            if($("#navtwo"+cg).length == 1){
                $("#navtwo"+cg).css("display","block");
                $(".navtwo").css("display","block");
                $(".navtwo").addClass("navtwo_bg");
                $(this).addClass("nav_bg");
            }else{
                $(this).addClass("nav_ye");

            }
        }).mouseleave(function(e){
            $(this).removeClass("nav_ye");
            $(".navtwo").css("display","none");
        });
    });

    $(".navtwo").mouseenter(function(e){
        var index = $(this).index();
        var cg = $(this).attr("cag");
        $(".navtwo").css("display","inline");
        $(".navtwo").addClass("navtwo_bg");

    }).mouseleave(function(e){
        $(".navtwo").css("display","none");
    });
    $(".bignav").mouseleave(function(){
        $(".nav li").removeClass("nav_bg");
    })
    //大分类
    $("#max_nav").mouseenter(function(){
        $(".alllis").css("display","block");
        $(".navall").find("i").each(function(index, element) {
            $(this).hasClass("dis_none")? $(this).removeClass("dis_none"):$(this).addClass("dis_none");
        });

    }).mouseleave(function(){
        $(".alllis").css("display","none");
        $(".navall").find("i").each(function(index, element) {
            $(this).hasClass("dis_none")? $(this).removeClass("dis_none"):$(this).addClass("dis_none");
        });
    });*/

</script>
</body>
</html>

