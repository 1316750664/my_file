/**
 * Created by xiayongwei on 2014/12/1.
 * version 2.0
 */
(function($){
    /**
     * left : 窗口左偏移
     * top : 窗口上偏移
     * width :  弹出框宽度
     * height :  弹出框高度
     * title ；标题名
     * resize  :
     * @type {{width: number, height: number, top: number, left: number, title: string,fixed:boolean,resize:boolean}}
     */
    var defaultConfig = {
        width : 0,
        height : 0,
        heightStatus : true,
        top : 0,
        left : 0,
        title : "",
        titleFont : "12px/24px Microsoft soft",
        titleHeight : "24px",
        titleCloseTop :"5px",
        fixed : true,
        resize: false,
        maskStatus: true,
        btn:true,
        tipType:"null"
    };
    $.fn.extend({
        showDialog:function(o,f){
            showDialog(this,o,f);
        },
        showTipAlt:function(t,l){
            tipShow(this,t,l);
        }
    });
    var pops = new Array();
    $.extend({
        showConfirm: function (v,o,f){
            typeof o == 'function' ? f = o : null;
            var o = potionsInit(o);
            var popObj = $("<div class='Bigokuang'></div>");
            popObj.appendTo($("body"));
            $("body").append('<div class="mask1"></div>');
            popObj.html(returnTitle(o));
            var h = "<div class='content'>";
            if(o.tipType == "tip"){
                h += "<span class='tishi2'></span>"
            }else if(o.tipType == "error"){
                h += "<span class='tishi3'></span>"
            }else if(o.tipType == "right"){
                h += "<span class='tishi1'></span>"
            }

            popObj.append(h + "<p>" + v + "</p></div><input class='bt1' type='button' value='取消'/>" +
                "<input class='bt1' type='button' value='确定'/>");
            popObj.find("input:eq(0)").click(function(){$.hidePop(popObj)});
            popObj.find("input:eq(1)").click(function(){f();$.hidePop(popObj);});
            popCommon(popObj,o);
            popObj.fadeIn(1000);
        },
        showTipPop: function (t,v,o,f) {
            typeof o == "function" ? f = o : null;
            var o = potionsInit(o);
            $("body").append('<div class="mask1"></div>');
            var popObj = $("<div class='Bigokuang'></div>");
            popObj.appendTo($("body"));
            popObj.html(returnTitle(o));
            var h = "<div class='content'>";
            if(t == "tip"){
                h += "<span class='tishi2'></span>"
            }else if( t == "error"){
                h += "<span class='tishi3'></span>"
            }else if(t == "right"){
                h += "<span class='tishi1'></span>"
            }
            h += "<p>" + v + "</p></div>";
            if(o.btn){
                h += "<input class='bt1' name='确定' type='button' value='确定' />";
            }
            popObj.append(h);
            popObj.find("input").click(function(){
                $.hidePop(popObj,f)
            });
            popCommon(popObj,o);
            if(o.btn){
                popObj.fadeIn(1000);
            }else{
                popObj.show();
                popObj.fadeOut(2500,function(){
                    $(".Bigokuang").length > 1 ? null : $(".mask1").remove();
                    f == undefined ? null : f();
                    popObj.remove();
                });
            }
        },
        showLoading:function(v){
            var popObj = $("<div class='Bigokuang' id='loading'></div>");
            popObj.appendTo($("body"));
            $("body").append('<div class="mask1"></div>');
            popObj.append("<div class='content'><img style='width:44px;height:44px;float:left;' src='/images/loading.gif'><p>" + v + "</p></div>");
            popCommon(popObj,potionsInit());
            popObj.css("display","block");
        },
        hidePop: function(obj,f){
            hide(obj,f);
        }
    });
    /***
     *
     * @param obj div
     * @param o 配置
     * @param f 消失后事件
     */
    function showDialog(obj,o,f){
        var o = potionsInit(o);
        var popObj;
        if($(".popupcontent").length == 0){
            popObj = $("<div class='ui-dialog popupcontent'></div>");
            popObj.appendTo($("body"));
        }else{
            popObj = $(".popupcontent");
        }
        $(".mask").length == 0 ? $("body").append('<div class="mask"></div>'):null;
        if($("#popHeader").length == 1){
            $("#popHeader").next().css("display","none")
            $("#popHeader").next().appendTo("body")
        }else{
            $(".popupcontent").children().css("display","none");
            $(".popupcontent").children().appendTo("body");
        }
        popObj.html(returnTitle(o,f));
        obj.appendTo(popObj);
        if(pops.length == 0){
            pops.push(obj.attr("id"));
        }else{
            $.inArray(obj.attr("id"),pops) == -1 && obj.attr("id") != undefined ? pops.push(obj.attr("id")):null;
        }
        popCommon(popObj,o,obj);
        o.maskStatus == true ? $(".mask").css("display","block"): null;
        obj.css("display","block");
        popObj.fadeIn(1000);
    };
    /// 返回titleHtml
    function returnTitle(o,f){
        var baseHtml = "";
        if(o.title != "" && o.title != undefined){
            baseHtml = "<div id='popHeader' style='height:"+ o.titleHeight +"; position:relative;'><div class='ui-head' style='height:"+ o.titleHeight +"'>" + "<h3 style='font:" + o.titleFont + "'>" + o.title + "</h3></div><a class='ui-dialog-close' title='Close' href='javascript:void(0);' style='display: inline;top:"+o.titleCloseTop+"' onclick='$.hidePop($(this).parent().parent(),"+ f +")'>×</a></div>";
        }
        return baseHtml;
    }
    function potionsInit(o){
        o = o || {};
        o = $.extend({},defaultConfig,o);
        return o;
    }
    function popCommon(obj,o,dom){
        if(dom){
            obj.css("width",dom.width());
            o.heightStatus ? obj.css("height",dom.height() + parseInt(o.titleHeight.substring(0,o.titleHeight.length - 2))) : null;
        }
        var width = o.width == 0 ? obj.width() : o.width;
        var height = o.height == 0 ? obj.height() : o.height;
        var top = resetTop(o.top,height, o.fixed);
        var left = o.left == 0 ? ($(window).width() - width)/2 + "px" : o.left + "px";
        if(o.fixed){
            $(window).resize(function(){
                var wtop = resetTop(o.top,height,o.fixed);
                var wleft = o.left == 0 ? ($(window).width() - width)/2 + "px" : o.left + "px";
                obj.css("top",wtop);
                obj.css("left",wleft);
            });
            $(window).scroll(function(){
                var wtop = resetTop(o.top,height,o.fixed);
                var wleft = o.left == 0 ? ($(window).width() - width)/2 + "px" : o.left + "px";
                obj.css("top",wtop);
                obj.css("left",wleft);
            });
        }
        obj.css("top",top);
        obj.css("left",left);

    }
    function resetTop(t,h,f){
        var scrollTop = 0;
        var top ;
        f == true ? scrollTop = $(document).scrollTop() : null;
        if(t == 0){
            top = $(window).height() > h ? ($(window).height() - h)/3 + scrollTop : scrollTop;
        }else{
            top = scrollTop + t;
        }
        return top + "px";
    }
    function hide(obj,func){
        var f = false;
        if(obj == undefined || obj.attr("class") == "ui-dialog popupcontent"){
            obj = $(".popupcontent");
            f = true;
        }
        obj.fadeOut(300,function(){
            if(f){
                $("body").append("<form id='resetFrom' style='display: none'></form>")
                for(var i = 0 ;i < pops.length;i++){
                    $("#"+ pops[i]).css("display","none");
                    $("#"+ pops[i]).find("form").length == 1 ? $("#"+ pops[i]).find("form")[0].reset():null;
                    $("#"+ pops[i]).appendTo("#resetFrom");
                }
                $("#resetFrom")[0].reset();
                $("#resetFrom").children().appendTo("body");
                $("#resetFrom").remove();
                pops = new Array();
                $(".mask").remove();
            }
            obj.remove();
            $(".Bigokuang").length > 1 ? null : $(".mask1").remove();
            func == undefined ? null : func();
        });
    };
    /// tip 提示框初始化
    function tipHtml(content,t,l){
        var tipHtml = "";
        tipHtml += "<div class='SD-tipbox' style='top:" + t +";left:"+ l +"'><div class='cntBox'><p style='font-size: 12px;color: red'>";
        tipHtml += content;
        tipHtml +="</p></div><div class='SD-tipbox-direction SD-tipbox-up'><em>&#9670;</em><span>&#9670;</span></div></div>";
        return tipHtml;
    }
    /// tip显示方法
    function tipShow(obj,t,l){
        obj.hover(function(){
            var tipVal = $(this).attr("data-tip");
            var tipHtmls = tipHtml(tipVal,t,l);
            $(this).append(tipHtmls);
        },function(){
            $(".SD-tipbox").remove();
        })
    }
})(jQuery);
(function($){
    var config = {
        sucAction : function(){},
        comAction : function (){},
        errAction : function(){},
        errAcFlag : false,
        dataType : "json",
        async : true ,
        maskFlg: true,
        maskValue:"正在加载中...",
        type:"post",
        timeout: 60000
    };
    $.extend({
        Bigo:function(url,data,o){
            o = o || {};
            o = $.extend({},config,o);
            bigo(url,data,o);
        }
    });
    function bigo(url,data,o) {
        $.ajax({
            type: o.type,
            url:url,
            data:data,
            dateType : o.dateType,
            async : o.async,
            timeout: o.timeout,
            beforeSend: function (xhr) {
                if(o.maskFlg){
                    $.showLoading(o.maskValue);
                }
            },
            success: function (msg) {
                o.sucAction(msg);
            },
            complete: function (xhr) {
                if (o.maskFlg) {
                    hideMask();
                }
                o.comAction(xhr);
            },
            error: function (xhr, ts, err) {
                if(!o.errAcFlag){
                    if ("timeout" == ts) {
                        $.showTipPop('null','请求时间过长');
                    } else if ("error" == ts) {
                        $.showTipPop('null','请求路径不对');
                    } else if ("notmodified" == ts) {
                        $.showTipPop('null', 'notmodified');
                    } else if ("parsererror" == ts) {
                        $.showTipPop('null','请求格式出错');
                    }
                }else{
                    o.errAction(xhr, ts, err);
                }
            }
        })
    }

    function hideMask() {
        $("#loading").remove();
        $(".Bigokuang").length > 1 ? null : $(".mask1").remove();
        $(".Bigokuang").length == 0 && $(".popupcontent").css("display") != "block" ? $(".mask").remove() : null;
    }
})(jQuery);