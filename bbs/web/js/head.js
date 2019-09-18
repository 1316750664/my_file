qsh_img_server="http://img.8673h.com/";
$(document).ready(function () {
   // $("#jsonp").attr("src","http://192.168.2.21:8089/js/jsonp.js?r=" + Math.random());
    loadHeadUser();//头部加载
    //loadHotKey();                //搜索关键词加载
  /*  var cookie_uuid = $.cookie("cookie_uuid");
    if((typeof cookie_uuid=="undefined" && typeof cookieJson!="undefined") || //用户还没登陆
        (typeof cookie_uuid!="undefined" && typeof cookieJson!="undefined" && cookie_uuid!=cookieJson.value )){//用户已经登录但是 切换了账号
        SetCookie("cookie_uuid",cookieJson.value);
    }else if(typeof cookie_uuid!="undefined" && typeof cookieJson=="undefined"){//用户退出登录
        SetCookie("cookie_uuid","");
    }*/

    var cart = $.cookie("cookie_cart");
    if(typeof cart=="undefined"){
        cart = "{\"total_num\":\"0\"}";
    }
    cart = decodeURIComponent(cart);
    cart = eval('('+cart+')');
    c_cart(cart);
});

var jsonpGet = function (data) {
    var cookieJson = data.cookie_uuid;
    var cookie_cart = data.cookie_cart;
    var cookie_userInfo = data.cookie_userInfo;
    var cookie_uuid = $.cookie("cookie_uuid");
    if((checkNull(cookie_uuid)&&  !checkNull(cookieJson)) || //用户还没登陆
        (!checkNull(cookie_uuid) &&  !checkNull(cookieJson) && cookie_uuid!=cookieJson )){//用户已经登录但是 切换了账号
        SetCookie("cookie_uuid",cookieJson);
        SetCookie("cookie_cart",cookie_cart);
        SetCookie("cookie_userInfo",cookie_userInfo);
    }else if(!checkNull(cookie_uuid) &&  checkNull(cookieJson)){//用户退出登录
        SetCookie("cookie_uuid","");
        SetCookie("cookie_cart","");
        SetCookie("cookie_userInfo","");
        //页面重新刷新
        location.reload();
    }

};
function checkNull(data){
    if(typeof data=="undefined" || data=="" || data=="null"){
        return true;
    }else{
        return false;
    }
}



function loadHotKey(){
    $.ajax({
        type: 'post',
        url: '/Action/HtmlHander.do',
        data: {position:6},
        dataType: 'html',
        async: false,
        success:function(data) {
            $(".remen").html(data);
        }
    });
}
function flush_cart(cart){
    c_cart(cart);
    SetCookie("cookie_cart",JSON.stringify(cart));
}
function SetCookie(name, value) {
    $.cookie(name,value,{path:'/'});
}
function c_cart(cart){
    $(".f_left .numb").text("0");
    if(typeof cart.total_num=="undefined" || cart.total_num==0){
        $(".cart").html('<p style="padding:5px;">您的购物车里没有任何宝贝。<a href="http://www.8673h.com/order/shopping.jsp" style="background: #f60;color:#fff;padding:5px 10px;">点击查看购物车</a></p>');
        return false;
    }
    $(".cart").html("");
    var s = cart;
    var t_num = s.total_num;
    $(".f_left .numb").text(t_num);
    var goods = s.goods;

    var last_num = t_num;
    if(last_num>5){
        last_num = last_num -5;
        $(".cart").prepend('<p style="padding:5px;float:right">您的购物车里还有'+last_num+'件宝贝。<a href="http://www.8673h.com/order/shopping.jsp" style="background: #f60;color:#fff;padding:5px 10px;">点击查看购物车</a></p>');
    }
    for(var j = 0; j<goods.length;j++){
        var goodsimg =goods[j].goods_main_img;
        if(goodsimg==null||goodsimg==""){
            goodsimg = "/images/pro_pic.png";
        }else{
            goodsimg = qsh_img_server +goodsimg;
        }
        if( goods[j].property_code=="0"){
            goods[j].property_code = "";
        }
        var cart_uuid = "'"+goods[j].cart_uuid+"'";
        $(".cart").prepend('<div class="borderbt"><img src="'+goodsimg+'" class="img"><div class="cont"><div class="title"><a target="_blank" href="http://www.8673h.com/">'+goods[j].goods_name+'</a></div><div class="jiage" data-id="">价格:<strong>'+goods[j].price+'</strong></div><div class="clear"></div><div class="salpro" data-id="'+goods[j].property_code+'">'+goods[j].property_json+'</div><div class="del" ><a href="http://www.8673h.com/" >删除</a></div></div><div class="clear"></div>');
    }



}
function delmyshop(args) {
    var json ="[{\"cart_uuid\":\""+args+"\"}]";
    $.Bigo("/Action/CartServlet.do", {statu: '2', cart_goods: json}, {sucAction: f,maskFlg:false});
    function f(data) {
        if(data.error=="202"){
            $.showConfirm('您还没有登陆，请选登录',function(){
                window.location.href="/Login/loginDetect.jsp";
            });
        }else if(data.error=="201"){
            $.showTipPop('error','购物车异常！',{title:"错误"});
        }else if(data.error=="302"){
            $.showTipPop('error','产品删除失败！',{title:"错误"});
        }else if(data.error=="0"){
            flush_cart(data);
        }
    }
}
var defaultVal = "";
////搜索下拉JS
//防止中文打字的时候就进行加载
$("#kw1").focus(function(){
    keyUD("bind");
    getIndex0();
});
$("#kw1").blur(function(){
    keyUD("unbind");
});

/// 小键盘上下
function keyUD(type){
    if(type=='bind'){
        $(document).bind("keyup",function(e){
            /// 小键盘往下
            if(e.which == 40){
                selectLi("next");
                /// 小键盘往上
            }else if(e.which == 38){
                selectLi("up");
            }else  {
                getIndex0();
            }
        });
    }else{
        $(document).unbind("keyup");
        ///修复点击li无效
        $(".xialakuang li").click(function(e){
            if(e.target != $(this)[0]){
                $(".xialakuang").hide();
            }
        })
    }
}

/// 获取指定的li
function selectLi(type){
    if($(".xialakuang").css("display") =="none"){
        return false;
    }
    var liObj = $(".xialakuang li");
    var indexLi;
    var active;
    liObj.each(function(i){
        if($(this).attr("class") == "on"){
            indexLi = i;
        }
    });
    var liLength = liObj.length;
    if(type == "next"){
        if(indexLi == liLength - 1){
            active = -1;
        }else{
            active = indexLi < liLength - 1 ? indexLi + 1:0;
        }
    }else{
        if(indexLi == 0){
            active = -1;
        }else{
            active = indexLi > 0 ? indexLi - 1 : liLength - 1;
        }
    }
    liObj.removeClass("on");
    if(active == -1){
        $("#kw1").val(defaultVal);
    }else{
        $(".xialakuang li:eq(" + active +")").addClass("on");
        $("#kw1").val($(".xialakuang li:eq(" + active +")").text());
        $("#indexid").attr("value",$(".xialakuang li:eq(" + active +")").attr("data"));
    }
    var sel = document.getElementById("kw1");
    /// 兼容IE
    if(+[1,]){
        if(type == "next"){
            sel.setSelectionRange(sel.value.length,sel.value.length);
        }else{
            sel.setSelectionRange(0,0);
        }
    }
}
function getIndex0(){
    var name = $("#kw1").val();
    if( name == ""){
        $(".xialakuang").hide();
        return false;
    }
    defaultVal = $("#kw1").val();
    var s_type = $(".shoubig ul").find("li:eq(0)").attr("data-id");
    if(typeof s_type == "undefined"){
        s_type = "1";
    }
    $.ajax({
        type: 'post',
        url: '/Action/SearchServlet.do',
        data: { name: name ,type: "getIndex",q_type:s_type},
        dataType: 'json',
        async: true,
        success: function (data) {
            var str = "";
            for(var o in data){
                var val = o;
                str += "<li data=" + data[o] + " onclick=\"typeindata('" + data[o] + "','" + o + "')\">";
                var startIndex = val.indexOf(name);
                str += val.substring(0,startIndex) + "<em style=\"color:red\">";
                str += val.substring(startIndex,startIndex + name.length) + "</em>";
                str += val.substring(startIndex + name.length,val.length) + "</li>";

            }
            str += "";
            if(str != ""){
                $(".xialakuang").html(str);
                $(".xialakuang").css("display","block");
            }else{
                $(".xialakuang").html("");
                $(".xialakuang").hide();
            }
        },
        complete:function(){
            $(".xialakuang li").each(function(){
                $(this).hover(function(){
                    $(".xialakuang li").removeClass("on");
                    $(this).addClass("on");
                },function(){
                    $(".xialakuang li").removeClass("on");
                })
            })
        },
        error: function() {
            $(".xialakuang").html("");
            $(".xialakuang").hide();
        }
    });
}
function tijiao(){
    var s_type = $(".shoubig ul").find("li:eq(0)").attr("data-id");
    if(typeof s_type == "undefined"){
        s_type = "1";
    }
    $("#s_type").val(s_type);
    $("#ss").submit();
}

function typeindata(id,name){
    $("#kw1").val(name);
    //$("#indexid").val(id);
    $(".xialakuang").hide();
    tijiao();
};

//汽车分类
$(".list").mouseenter(function(e){
    $(".hideMap").css("display","none");//隐藏所有
    var tg=$(this).attr("tag");
    $("#JS_dvmenu"+tg).prev(".cat").addClass("cat1");
    $("#JS_dvmenu"+tg).css  ("display","block");//显示当前
}).mouseleave(function(e){
    $(".cat").removeClass("cat1");
    $(".hideMap").css("display","none");
})
//汽车分类下拉
$("#hide").mouseenter(function(e){
    $(".all_cat").css("display","block");
}).mouseleave(function(e){
    $(".all_cat").css("display","none");
})
//头部导航
$(".f_left").mouseenter(function(e){
    $(".menu_hide").css("display","none");
    var cg=$(this).attr("cag");
    $("#JS_hdmenu"+cg).css("display","block");
    $(this).addClass("bg_color");
}).mouseleave(function(e){
    $(".menu_hide").css("display","none");
    $(this).removeClass("bg_color");
});
//搜索tabs
$(".search_tab").mouseenter(function(e){
    $(".search_tab").css("overflow","visible");
}).mouseleave(function(e){
    $(".search_tab").css("overflow","hidden");
});
$(".search_tab ul li").click(function(e) {
    var clickId=$(this).attr("data-id");
    var clickHtml=$(".search_tab ul li[data-id='"+clickId+"']").detach();//先删除自己
    var tempHtml=$(".search_tab ul li").detach();//再删除其它
    $(".search_tab ul").append(clickHtml).append(tempHtml);
});
function ppnavhander(ele){
    var id = $(ele).attr("id");
    var name = $(ele).text();
    $("#pingpai .pp").val(id);
    $("#pingpai .ppm").val(name);
    $("#pingpai").submit();
}

function valid(){
    var name = $("#kw1").val();
    name = name.replace(/(^\s*)/g, "");//删除左边空格
    if (name == "")
        return false;
    else
        return true;
}


//登陆头部加载
function loadHeadUser(){
    var userInfo = $.cookie("cookie_userInfo");
    if(undefined != userInfo){
        userInfo = decodeURIComponent(userInfo);
        var obj = eval('(' + userInfo + ')');
        var name = obj.nick;
        if(name==null || name==""){
            name = obj.login_name;
        }
        var hrefA = "<a href=\"javascript:void(0);\" alt="+name+" title="+name+">"+name+"</a> <a href='/Login/logout.jsp' >退出</a>";
        $("#loginShow").html(hrefA);
    }
}
function mengMD() {
    $.showTipPop('null','此功能正在由苦逼的程序员没日没夜的赶工中');
}
//百度统计
var _hmt = _hmt || [];
(function() {
    var hm = document.createElement("script");
    hm.src = "//hm.baidu.com/hm.js?6f4b8c6ba96caeef1e7b93d7ab7d5694";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})();
