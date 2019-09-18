$(function () {
    //InitLeftMenu();
    tabClose();
    tabCloseEven();
});

function addTab(subtitle, url, icon) {
    if ($('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('select', subtitle);
    } else {
        $('#tabs').tabs('add', {
            title: subtitle,
            content: createFrame(url),
            closable: true,
            iconCls: icon
        });
    }
    tabClose();
}

//在TAB内页窗新建TAB,由于该弹出可能会是多个,所以没有判断是否存在,均为新TAB弹出
//params:标题,地址,图标
//modify by Slimzhou 2013-08-30 8:26
function addTabInside(title, url, icon) {
    var jq = top.jQuery;
    jq("#tabs").tabs('add', {
        title: title,
        content: createFrame(url),
        iconCls: icon,
        closable: true
    });
}

function tabClose() {
    /*双击关闭TAB选项卡*/
    $(".tabs-inner").dblclick(function () {
        if ($(this).children(".tabs-closable").length > 0) {
            var subtitle = $(this).children(".tabs-closable").text();
            $('#tabs').tabs('close', subtitle);
        }
        return false;
    })
    /*为选项卡绑定右键*/
    $(".tabs-inner").bind('contextmenu', function (e) {
        $('#tab_menu').menu('show', {
            left: e.pageX,
            top: e.pageY
        });
        if ($(this).children(".tabs-closable").length > 0) {
            var subtitle = $(this).children(".tabs-closable").text();
            $('#tab_menu').data("currtab", subtitle);
            $('#tabs').tabs('select', subtitle);
        } else {
            $('#tabs').tabs('select', 0);
        }
        return false;
    });
}
//绑定右键菜单事件
function tabCloseEven() {
    //刷新
    $('#tab_update').click(function () {
        var currTab = $('#tabs').tabs('getSelected');
        var index = $('#tabs').tabs('getTabIndex', currTab);
        if (index == 0) {
            $("#orderList").datagrid('reload');
            $("#consultList").datagrid('reload');
            $("#backList").datagrid('reload');
            $("#smsList").datagrid('reload');
            $("#RecordList").datagrid('reload');
        } else {
            var url = $(currTab.panel('options').content).attr('src');
            $('#tabs').tabs('update', {
                tab: currTab,
                options: {
                    content: createFrame(url)
                }
            });
            return false;
        }
        /*var url = $(currTab.panel('options').content).attr('src');
         $('#tabs').tabs('update', {
         tab: currTab,
         options: {
         content: createFrame(url)
         }
         });
         return false;*/
    });
    //关闭当前
    $('#tab_close').click(function () {
        var currtab_title = $('#tab_menu').data("currtab");
        if (currtab_title) {
            $('#tabs').tabs('close', currtab_title)
        } else {
            alert('当前选项卡不允许关闭');
        }
        return false;
    });
    //全部关闭
    $('#tab_close_all').click(function () {
        $('.tabs-inner .tabs-closable').each(function (i, n) {
            var t = $(n).text();
            $('#tabs').tabs('close', t);
        });
        return false;
    });
    //关闭除当前之外的TAB
    $('#tab_close_other').click(function () {
        $('#tab_close_right').click();
        $('#tab_close_left').click();
    });
    //关闭当前左侧的TAB
    $('#tab_close_left').click(function () {
        var prevall = $('.tabs-selected').prevAll();
        if (prevall.length == 0) {
            alert('到头了，前边没有啦~~');
            return false;
        }
        prevall.each(function (i, n) {
            var c = $('a:eq(0) span', $(n));
            if ($(c).hasClass('tabs-closable')) {
                var t = $(c).text();
                $('#tabs').tabs('close', t);
            }
        });
        return false;
    });
    //关闭当前右侧的TAB
    $('#tab_close_right').click(function () {
        var nextall = $('.tabs-selected').nextAll();
        if (nextall.length == 0) {
            alert('到头了，后边没有啦~~');
            return false;
        }
        nextall.each(function (i, n) {
            var c = $('a:eq(0) span', $(n));
            if ($(c).hasClass('tabs-closable')) {
                var t = $(c).text();
                $('#tabs').tabs('close', t);
            }
        });
        return false;
    });
}

function createFrame(url, id) {
    var s = '<iframe style="width: 100%;height: 98.8%;" marginwidth="0" marginheight="0" frameborder="no" scrolling="auto" src="' + url;
    if (id) {
        s += '" id="' + id;
    }
    s += '"></iframe>';
    return s;
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
    parent.$.messager.alert(title, msgString, msgType);
}

//创建对话
function CreateDetailDlg(obj) {
    var ifrId = $('body').find('iframe').length + 1;
    var iframeHtml = createFrame(obj.url, 'iframe_' + ifrId);
    var win = $('<div id="winIframe_' + ifrId + '">' + iframeHtml + '</div>').appendTo('body');
    win.dialog({
        title: obj.title,
        width: obj.width,
        height: obj.height,
        cache: false,
        modal: true,
        buttons: [
            {
                text: '确定',
                iconCls: 'icon-ok',
                handler: obj.func
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    win.dialog('close');
                }
            }
        ],
        onClose: function () {
            win.dialog('destroy');
            win = undefined;
        }
    });
    return ifrId;
}
//创建对话
function CreateDetailReadDlg(obj) {
    var ifrId = $('body').find('iframe').length + 1;
    var iframeHtml = createFrame(obj.url, 'iframe_' + ifrId);
    var win = $('<div id="winIframe_' + ifrId + '">' + iframeHtml + '</div>').appendTo('body');
    win.dialog({
        title: obj.title,
        width: obj.width,
        height: obj.height,
        cache: false,
        modal: true,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    win.dialog('close');
                }
            }
        ],
        onClose: function () {
            win.dialog('destroy');
            win = undefined;
        }
    });
}
/**
 * 弹出可编辑对话框
 */
window.opennerArray = new Array();
window.serialized = null;
function OpenDetailDlg(obj) {
    //var timestamp = new Date().getTime();
    parent.$.modalDialog({
        title: obj.title,
        href: obj.url,
        width: obj.width,
        height: obj.height,
        buttons: [
            {
                //id: 'lb_ok_' + timestamp,
                text: '保存',
                iconCls: 'icon-ok',
                handler: function () {
                    //parent.$("#lb_ok_" + timestamp).linkbutton("disable");
                    //var t = setTimeout(function () {
                    //    parent.$("#lb_ok_" + timestamp).linkbutton("enable");
                    //    clearTimeout(t);
                    //}, 3000);
                    if (obj.grid) {
                        parent.$.modalDialog.openner = obj.grid;//定义子窗口打开的GRID,以便返回reload
                        opennerArray.push(obj.grid);
                    }
                    if (obj.formId) {
                        var f = parent.$.modalDialog.handler.find("#" + obj.formId);
                        if (window.serialized == null || window.serialized != f.serialize()) {
                            window.serialized = f.serialize();
                            //console.log(window.serialized);
                            f.submit();
                        } else if (window.serialized == f.serialize()) {
                            //parent.$.messager.alert('提示', '数据正在保存中，请务重复操作...', 'warning');
                            parent.$.messager.defaults = {ok: "强制保存", cancel: "继续等待"};
                            parent.$.messager.confirm('提示', '系统检测到重复数据正在提交保存...请确认？点击【强制保存】强制保存，但可能重复保存多份数据请小心使用，点击【继续等待】继续等待上次保存结果', function (r) {
                                if (r) {
                                    //window.serialized = null;
                                    f.submit();
                                }
                            });
                            parent.$.messager.defaults = {ok: "确定", cancel: "取消"};
                        }
                    }
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    parent.$.modalDialog.handler.dialog('close');
                }
            }
        ],
        onLoad: function () {
            if (obj.row && obj.formId) {
                var f = parent.$.modalDialog.handler.find("#" + obj.formId);
                f.form("load", obj.row);
            }
            if (obj.extra) {
                var tableId = obj.extra.tableId;
                var tableData = obj.extra.data;
                if (typeof(tableData) == 'undefined') {
                    tableData = [];
                }
                var t = parent.$.modalDialog.handler.find("#" + tableId);
                t.datagrid("loadData", tableData);
            }
        },
        onClose: function () {
            parent.$.modalDialog.handler.dialog('destroy');
            parent.$.modalDialog.handler = undefined;//使当前失效
            handlerArray.pop();//先删除当前
            if (obj.grid) {//有加才有弹
                //parent.$.modalDialog.openner = undefined;
                opennerArray.pop();
            }
            var len1 = handlerArray.length;
            var len2 = opennerArray.length;
            if (len1 > 0) {
                parent.$.modalDialog.handler = handlerArray[len1 - 1];
            }
            if (len2 > 0) {
                parent.$.modalDialog.openner = opennerArray[len2 - 1];
            }
        }
    });
}

/**
 * 弹出只读对话框
 */
function OpenDetailReadDlg(obj) {
    parent.$.modalDialog({
        title: obj.title,
        href: obj.url,
        width: obj.width,
        height: obj.height,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    parent.$.modalDialog.handler.dialog('close');
                }
            }
        ],
        onLoad: function () {
            if (obj.row && obj.formId) {
                if (obj.row && obj.formId) {
                    var f = parent.$.modalDialog.handler.find("#" + obj.formId);
                    f.form("load", obj.row);
                }
                if (obj.extra) {
                    var tableId = obj.extra.tableId;
                    var tableData = obj.extra.data;
                    if (typeof(tableData) == 'undefined') {
                        tableData = [];
                    }
                    var t = parent.$.modalDialog.handler.find("#" + tableId);
                    t.datagrid("loadData", tableData);
                }
            }
        },
        onClose: function () {
            parent.$.modalDialog.handler.dialog('destroy');
            parent.$.modalDialog.handler = undefined;//使当前失效
            handlerArray.pop();//先删除当前
            var len = handlerArray.length;
            if (len > 0) {
                parent.$.modalDialog.handler = handlerArray[len - 1];
            }
        }
    });
}

function openFileUploadDlg(fileType, all_image_id) {
    var fileWin = '<div id="fileDlg" style="padding:2px;">' +
        '<iframe style="width: 100%;height: 100%;" marginwidth="0" marginheight="0" frameborder="no" scrolling="auto" src="/fileMgt/swfFileDlg.jsp?fileType=' + fileType + '&all_image_id=' + all_image_id + '"></iframe>' +
        '</div>';
    $(document.body).append(fileWin);
    $("#fileDlg").window({
        iconCls: "icon-save",
        title: "文件上传",
        width: 800,
        height: 500,
        shadow: true,
        modal: true,
        closable: true,
        minimizable: false,
        maximizable: false,
        collapsible: false,
        onClose: function () {
            $("#fileDlg").window("destroy");
        }
    });
}

function setGridEditor2Form(editors) {
    for (var i = 0; i < editors.length; i++) {
        var editorValue = '';
        var targetClass = ($(editors[i].target).attr("class"));
        if (targetClass.indexOf("combo") > -1) {
            editorValue = $(editors[i].target).combo('getValue');
        } else {
            editorValue = editors[i].target.val();
        }
        $("#" + editors[i].field).val(editorValue);
    }
}

//阻止默认回车换行
$.fn.prevEnter = function () {
    var thiz = $(this);
    thiz.bind("keydown", function (e) {
        e = e || event;
        var key = e.keyCode || e.which || e.charCode;
        if (key === 13) {
            e.preventDefault();
        }
    });
}

function Enter2Tab() {
    var $inp = $('input:text');
    $inp.bind('keydown', function (e) {
        e = e || event;
        var key = e.keyCode || e.which || e.charCode;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus();
        }
    });
}