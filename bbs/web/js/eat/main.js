//实例化日历控件
var mainFrame = {};
mainFrame.init = function(){
    //初始化参数
    this.initPara();
    //实例化日历控件
    this.initCalendar();
    //绑定操作事件
    this.bingEvent();
};

mainFrame.initPara = function(){
    this.calendar = null;
    this.data = [];
    this.checkLunch = false;
    this.checkDinner = false;
    this.type = "0";        //type 0 => 未选 1 => 只定午餐 2 => 只定晚餐 3 => 午餐和晚餐
    this.selectDate = [];   //选中的日期集合 如["2016-08-01"]
};
mainFrame.initCalendar = function(){
    var self = this;
    var calendar = new Calendar("calendar_container");
    this.data = this.getData();
    this.calendar = calendar;

    calendar.extendOnClick = this.extendOnClick;
    calendar.extendRefresh = this.extendRefresh;
    calendar.refresh();
};
mainFrame.getData = function(){
    var result = [];
//    $.ajax({
//        type: "GET",
//        url: "./json/data.json",
//        dataType: "json",
//        async: false,
//        success: function(json){
//            result = json;
//        }
//    });
    return result;
};
mainFrame.extendOnClick = function(dom) {
    var self = mainFrame;
    if(self.type === "0"){
        return;
    }
    var date = $(dom).attr("td_date");
    var str = '<div class="check_warp"><i class="iconfont icon-check"></i></div>';
    if($(dom).find(".check_warp").length > 0){
        $(dom).find(".check_warp").remove();
        var index = self.selectDate.indexOf(date);
        self.selectDate.splice(index, 1);
    }else{
        $(dom).append(str);
        self.selectDate.push(date);
    }
};

mainFrame.extendRefresh = function(date_list) {
    var self = mainFrame;
    var data = self.data;
    for (var i = 0; i < date_list.length; i++) {
        var dataStr = $(date_list[i]).attr("td_date");
        for (var j = 0; j < data.length; j++) {
            var oldDataStr = data[j]["date"];
            //判断两个date对象是否相等
            if (dataStr == oldDataStr) {
                var type = data[j]["type"],
                    str = "";
                if(self.type === "0") {
                    switch (type) {
                        case "1":
                            str = '<div class="meal in">午餐<b>1</b>份</div>';
                            break;
                        case "2":
                            str = '<div class="meal late">晚餐<b>1</b>份</div>';
                            break;
                        case "3":
                            str = '<div class="meal in">午餐<b>1</b>份</div>';
                            str += '<div class="meal late">晚餐<b>1</b>份</div>';
                            break;
                    }
                }else if((self.type === "1" && type === "1") || (self.type === "2" && type === "2") || (self.type === "3" && type === "3"))  {
                    str = '<div class="check_warp"><i class="iconfont icon-check"></i></div>';
                    self.selectDate.push(dataStr);
                }
                $(date_list[i]).append(str);
            }
        }
    }
};
mainFrame.bingEvent = function() {
    var self = this;
    $(".oper_btn").on("click",function(){
        var $iDom = $(this).find("i"),
            mealType = $(this).attr("type"),
            type = "0";
        self.selectDate = [];
        if($iDom.hasClass("icon-check")){
            $iDom.removeClass("icon-check").addClass("icon-un-check");
            mealType == "0" ? self.checkLunch = false : self.checkDinner = false;
        }else{
            $iDom.removeClass("icon-un-check").addClass("icon-check");
            mealType == "0" ? self.checkLunch = true : self.checkDinner = true;
        }
        if(self.checkLunch){
            type = "1";
        }
        if(self.checkDinner){
            type = "2";
        }
        if(self.checkLunch && self.checkDinner){
            type = "3";
        }
        self.type = type;
        self.calendar.refresh();
    });

    $("#confirm_btn").on("click",function(){
        var dateList = self.selectDate,
            type = self.type,
            checkLunch = self.checkLunch ? "1" : "0",
            checkDinner = self.checkDinner ? "1" : "0";
        var dateArr = [],
            postData = {},
            cookie = $.trim($("#cookie_tx").val());
        if(cookie == ""){
            alert("cookie不能为空");
            return;
        }
        for(var i= 0, len = dateList.length; i < len; i++){
            var date = dateList[i];
            dateArr.push(date);
        }
        if(dateArr.length == 0){
            alert("请选择至少一个日期");
            return;
        }
        postData = {
            cookie : cookie,
            in : checkLunch,
            late : checkDinner,
            date : dateArr.join(","),
            action:"0",
            JsonType:"0"
        };
        self.submitData(postData);
    });
};

mainFrame.submitData = function(data) {
    var self = this;
    $.ajax({
        type: 'post',
        url: '/Action/EatAction.do',
        data: data,
        dataType: 'json',
        async: true,//true是异步 false是同步
        success: function (data) {
            //if true
            //重绘日历{
            //self.getData();
            //self.calendar.refresh();}
            if(data=="1"){
                alert("点餐成功");
                self.selectDate = [];
                self.data=[];
                self.calendar.refresh();
            }else{
                alert("cookie错误");
            }
        },
        error: function (data) {
            alert("提交出错")
        }
    });
};
$(function(){
    //初始化
    mainFrame.init();
});
