//删除图片
function DelImg() {
    $("#ImgTag").html("");
    $("#goodsimgpath").val("");
}

/**
 * 图片上传，不带参数
 */
function ajaxImgFileUpload() {
    $.ajaxFileUpload({
        url: '/Action/SingleUpload.do',             //需要链接到服务器地址
        secureuri: false,
        fileElementId: 'filename',                        //文件选择框的id属性
        dataType: 'text',
        //服务器返回的格式，可以是json
        success: function (data, status)             //相当于java中try语句块的用法
        {
            if (data != "")//data是从服务器返回来的值
            {
                if (data.indexOf("FileNull") != -1) {
                    parent.$.messager.alert('系统提示', '请选择正确的图片', 'info');
                }
                else if (data.indexOf("FileTypeNull") != -1) {
                    parent.$.messager.alert('系统提示', '抱歉，没有上传控件！', 'info');
                }
                else if (data.split(",")[0].indexOf("UpOk") != -1)//表示返回的是图片路径
                {
                    $("#goodsimgpath").val(data.split(",")[1]);
                    $("#ImgTag").html("<br><img src=\"/upload/goods/goods_thumb/" + data.split(",")[1] + "\" width=\"105\"><br/>" +
                        "<a href=\"javascript:void('');\" onclick=\"DelImg()\">删除</a>");
                }
                else if (data.indexOf("UpErr") != -1) {
                    parent.$.messager.alert('系统提示', '抱歉，上传失败,请检查图片格式跟大小！', 'info');
                }
                else if (data.indexOf("UpExce") != -1) {
                    parent.$.messager.alert('系统提示', '抱歉，请选择正确的图片路径', 'info');
                }
                else {
                    parent.$.messager.alert('系统提示', '抱歉，发生错误的请求！', 'error');
                }
            }
            else {
                parent.$.messager.alert('系统提示', '抱歉，上传图片异常！', 'error');
            }
        },
        error: function (data, status, e)//相当于java中catch语句块的用法
        {
            parent.$.messager.alert('系统提示', '抱歉，上传图片出错！', 'error');
        }
    });
}

/**
 * 图片上传，可以带参数的上传
 * @param vid　参数值
 * @param urlpath　需要链接到服务器地址
 * @param imgwidth 图片的宽
 * @constructor
 */
function SupajaxImgFileUpload(vid, urlpath, imgwidth) {
    $.ajaxFileUpload(
        {
            url: urlpath,             //需要链接到服务器地址
            secureuri: false,
            fileElementId: 'filename_' + vid,                        //文件选择框的id属性
            dataType: 'text',
            data: {//加入的文本参数
                "filemark": vid
            },  //服务器返回的格式，可以是json
            success: function (data, status)             //相当于java中try语句块的用法
            {
                if (data != "")//data是从服务器返回来的值
                {
                    if (data.indexOf("FileNull") != -1) {
                        parent.$.messager.alert('系统提示', '请选择正确的图片', 'info');
                    }
                    else if (data.indexOf("FileTypeNull") != -1) {
                        parent.$.messager.alert('系统提示', '抱歉，没有上传控件！', 'info');
                    }
                    else if (data.indexOf("FileMarkNull") != -1) {
                        parent.$.messager.alert('系统提示', '抱歉，上传文件名出错！', 'info');
                    }
                    else if (data.split(",")[0].indexOf("UpOk") != -1)//表示返回的是图片路径
                    {
                        var marktype = data.split(",")[1];//即文件文件框后缀
                        var inputname = data.split(",")[1].split("_")[0];//文本框name
                        var dirpath = data.split(",")[2];//存放路径
                        var filename = data.split(",")[3];//文件名称
                        var inputstr = "<input type='hidden' name='" + inputname + "' value='" + filename + "'>";
                        $("#" + marktype + "ImgTag").html("<br/><img src=\"" + dirpath + filename + "\" width=" + imgwidth + "><br/>" +
                            "<a href=\"javascript:void('');\" onclick=\"DelSupImg('" + marktype + "')\">删除</a>" + inputstr);
                    }
                    else if (data.indexOf("UpErr") != -1) {
                        parent.$.messager.alert('系统提示', '抱歉，上传失败,请检查图片格式跟大小！', 'info');
                    }
                    else if (data.indexOf("UpExce") != -1) {
                        parent.$.messager.alert('系统提示', '抱歉，请选择正确的图片路径', 'info');
                    }
                    else if (data.indexOf("ProxyNull") != -1) {
                        parent.$.messager.alert('系统提示', '抱歉，您无权操作此模块', 'info');
                    }
                    else {
                        parent.$.messager.alert('系统提示', '抱歉，发生错误的请求！', 'error');
                    }
                }
                else {
                    parent.$.messager.alert('系统提示', '抱歉，上传图片异常！', 'error');
                }
            },
            error: function (data, status, e)//相当于java中catch语句块的用法
            {
                parent.$.messager.alert('系统提示', '抱歉，上传图片出错！', 'error');
            }
        });
}

/**
 * 删除上传图片
 * @param marktype　ID参数
 * @constructor
 */
function DelSupImg(marktype) {
    $("#" + marktype + "ImgTag").html("");
}

/**
 * 弹出窗口
 * @param title 窗口标题
 * @param url　窗体地址
 * @param width　宽度
 * @param height　高度
 * @constructor
 */
function OpenDetail(title, url, width, height) {
    var win = '<div id="gd" style="padding:2px;"></div>';
    $(document.body).append(win);
    $("#gd").window({
        title: title,
        width: width,
        height: height,
        shadow: true,
        modal: true,
        iconCls: 'icon-add',
        closed: true,
        minimizable: false,
        maximizable: false,
        collapsible: false
    });
    $('#gd').html("<iframe style='width: 100%;height: 100%;' marginwidth='0' marginheight='0' frameborder='no' scrolling='no' src='" + url + "'></iframe>");
    $('#gd').window('open');
}
