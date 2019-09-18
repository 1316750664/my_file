//去空格
String.prototype.Trim = function () {
    var m = this.match(/^\s*(\S+(\s+\S+)*)\s*$/);
    return (m == null) ? "" : m[1];
}

//验证电话
function checkTel(str) {
    str = str.Trim();
    if (checkSj(str) || checkDh(str)) {
        return true;
    }
    return false;
}
function checkSj(str){
    str = str.Trim();
    //手机号码
    var patrn1 = /^1[3,4,5,8]\d{9}$/;
    return patrn1.exec(str);
}
function checkDh(str){
    str = str.Trim();
    //固定电话
    var patrn = /^((\+?[0-9]{2,4}\-[0-9]{3,4}\-)|([0-9]{3,4}\-))?([0-9]{7,8})(\-[0-9]+)?$/;
    return patrn.exec(str);
}

//姓名验证
function checkName(str) {
    str = str.Trim();
    if (str == "" || str.length < 2) {
        return false;
    }
    return true;
}

//图片格式
function checkPicture(str) {
    var tpType="/.jpg/.jpeg/.png/.gif";
    return tpType.indexOf(str.substring(str.lastIndexOf(".")).toLowerCase());
}

//验证数字
function isNum(str) {
    str = str.Trim();
    var reg = /^(0|[1-9][0-9]*)$/;
    return reg.test(str);
}

//验证邮箱
function isEmail(str){
    var reg =/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    return reg.test(str);
}
/**
 * 判断是否有中文
 * @param s
 * @returns {boolean}true 有中文 false 没中文
 */
function isChinese(s) {
    if (s == "") {
        return false;
    }
    else {
        var ret = true;
        for (var i = 0; i < s.length; i++)
            ret = ret && (s.charCodeAt(i) >= 10000);
        return ret;
    }
}

/**
 * 限制文本框输入几个字
 * @param message 文本框的值
 * @param remain  显示字数的ID
 * @param maxleng 最多字数
 * @constructor
 */
function AboutCount(message, remain, maxleng) {
    if (getValueLen(message.value) > maxleng) {
        message.value = message.value.substring(0, maxleng);
        $("#" + remain).html(0);
        parent.$.messager.alert('系统提示', "内容不能超过" + maxleng + "个字！", 'info');
    }
    else {
        $("#" + remain).html(getValueLen(message.value));
    }
}
/**
 * 返回字符长度,一个中文算二个字符
 * @param nameValue
 * @returns {number}
 */
function getValueLen(nameValue) {
    var nameStr = nameValue;
    var len = 0;
    for (var i = 0; i < nameStr.length; i++) {
        //str = markerStr.charAt(i);
        if (nameStr.charCodeAt(i) > 255 || nameStr.charCodeAt(i) < 0) {
            len += 2;
        } else {
            len++;
        }
    }
    return len;
}

/**
 * 身份证验证
 * @param socialNo
 * @returns {boolean}
 */
function checkCardId(socialNo) {
    socialNo=socialNo.Trim();
    if (socialNo == "") {
        //alert("身份证号码不能为空!");
        return false;
    }
    if (socialNo.length != 15 && socialNo.length != 18) {
        //alert("输入身份证号码格式不正确!");
        return false;
    }
    var area = {
        11 : "北京",
        12 : "天津",
        13 : "河北",
        14 : "山西",
        15 : "内蒙古",
        21 : "辽宁",
        22 : "吉林",
        23 : "黑龙江",
        31 : "上海",
        32 : "江苏",
        33 : "浙江",
        34 : "安徽",
        35 : "福建",
        36 : "江西",
        37 : "山东",
        41 : "河南",
        42 : "湖北",
        43 : "湖南",
        44 : "广东",
        45 : "广西",
        46 : "海南",
        50 : "重庆",
        51 : "四川",
        52 : "贵州",
        53 : "云南",
        54 : "西藏",
        61 : "陕西",
        62 : "甘肃",
        63 : "青海",
        64 : "宁夏",
        65 : "新疆",
        71 : "台湾",
        81 : "香港",
        82 : "澳门",
        91 : "国外"
    };
    if (area[parseInt(socialNo.substr(0, 2))] == null) {
        //alert("身份证号码不正确(地区非法)!");
        return false;
    }
    if (socialNo.length == 15) {
        pattern = /^\d{15}$/;
        if (pattern.exec(socialNo) == null) {
            //alert("15位身份证号码必须为数字！");
            return false;
        }
        var birth = parseInt("19" + socialNo.substr(6, 2));
        var month = socialNo.substr(8, 2);
        var day = parseInt(socialNo.substr(10, 2));
        switch (month) {
            case '01':
            case '03':
            case '05':
            case '07':
            case '08':
            case '10':
            case '12':
                if (day > 31) {
                    //alert('输入身份证号码不格式正确!');
                    return false;
                }
                break;
            case '04':
            case '06':
            case '09':
            case '11':
                if (day > 30) {
                    //alert('输入身份证号码不格式正确!');
                    return false;
                }
                break;
            case '02':
                if ((birth % 4 == 0 && birth % 100 != 0) || birth % 400 == 0) {
                    if (day > 29) {
                        //alert('输入身份证号码不格式正确!');
                        return false;
                    }
                } else {
                    if (day > 28) {
                        //alert('输入身份证号码不格式正确!');
                        return false;
                    }
                }
                break;
            default:
                //alert('输入身份证号码不格式正确!');
                return false;
        }
        var nowYear = new Date().getYear();
        if (nowYear - parseInt(birth) < 15 || nowYear - parseInt(birth) > 100) {
            //alert('输入身份证号码不格式正确!');
            return false;
        }
        return true;
    }
    //以下验证18位身份证
    var Wi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
    var lSum = 0;
    var nNum = 0;
    var nCheckSum = 0;
    for (i = 0; i < 17; ++i) {
        if (socialNo.charAt(i) < '0' || socialNo.charAt(i) > '9') {
            //alert("输入身份证号码格式不正确!");
            return false;
        } else {
            nNum = socialNo.charAt(i) - '0'
        }
        lSum += nNum * Wi[i]
    }
    if (socialNo.charAt(17) == 'X' || socialNo.charAt(17) == 'x') {
        lSum += 10 * Wi[17]
    } else if (socialNo.charAt(17) < '0' || socialNo.charAt(17) > '9') {
        //alert("输入身份证号码格式不正确!");
        return false;
    } else {
        lSum += (socialNo.charAt(17) - '0') * Wi[17]
    }
    if ((lSum % 11) == 1) {
        return true;
    } else {
        //alert("输入身份证号码格式不正确!");
        return false;
    }
}

function killHtml(replaceHtml) {
    replaceHtml=replaceHtml.replace(/</g,"＜");
    replaceHtml=replaceHtml.replace(/>/g,"＞");
    replaceHtml=replaceHtml.replace(/'/g,"#apos;");
    replaceHtml=replaceHtml.replace(/\"/g,"#quot;");
    replaceHtml=replaceHtml.replace(/=/g,"#3D;");
    replaceHtml=replaceHtml.replace(/\r/g,"#0D;");
    replaceHtml=replaceHtml.replace(/\n/g,"#0A;");
    var html="";
    for(var i=0;i<replaceHtml.length;i++){
        var ch=replaceHtml.charAt(i);
        if(replaceHtml.charCodeAt(i)==9){
            //ch="#09;";
            ch="";
        }
        html+=ch;
    }
    return html;
}

function unKillHtml(replaceHtml) {
    replaceHtml=replaceHtml.replace(/＜/g,"<");
    replaceHtml=replaceHtml.replace(/＞/g,">");
    replaceHtml=replaceHtml.replace(/#apos;/g,"'");
    replaceHtml=replaceHtml.replace(/#quot;/g,"\"");
    replaceHtml=replaceHtml.replace(/#3D;/g,"=");
    replaceHtml=replaceHtml.replace(/#0D;/g,"\r");
    replaceHtml=replaceHtml.replace(/#0A;/g,"\n");
    //replaceHtml=replaceHtml.replace(/#09;/g,"&nbsp;&nbsp;&nbsp;&nbsp;");
    return replaceHtml;
}

//定义kindEditor可用功能菜单
$.kindMenu=[
    'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'cut', 'copy', 'paste',
    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
    'superscript', 'clearhtml', 'quickformat', 'selectall', '|','formatblock', 'fontname',
    'fontsize', '|', 'forecolor', 'hilitecolor', 'bold','italic', 'underline', 'strikethrough',
    'lineheight', 'removeformat', '|', 'image', 'multiimage','flash', 'media', 'insertfile',
    'table', 'hr', 'emoticons', 'baidumap', 'pagebreak', 'anchor', 'link', 'unlink', '|', 'about'
];

function checkUser(usernameVal)
{
    usernameVal = usernameVal.Trim();
    if(chEnWordCount(usernameVal) < 4 || chEnWordCount(usernameVal) > 20){
        alert("少于4个字符或者多于20个字符！");
        return false;
    }
    if (!usernameVal.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]{2,20}$/))  {
        alert("只能由汉字、英文字母、数字、下划线组成");
        return false;
    }
return true;
}

/**
 * 中英文统计(一个中文算两个字符)
 */
function chEnWordCount(str){
    var count = str.replace(/[^\x00-\xff]/g,"**").length;
    return count;
}