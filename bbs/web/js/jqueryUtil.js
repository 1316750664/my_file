(function ($) {
    //全局系统对象
    window['jqueryUtil'] = {};
    //修改ajax默认设置
    $.ajaxSetup({
        type: 'POST',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $.messager.progress('close');
            $.messager.alert('错误', XMLHttpRequest.responseText);
        }
    });

    var easyuiErrorFunction = function (XMLHttpRequest) {
        $.messager.progress('close');
        $.messager.alert('错误', XMLHttpRequest.responseText);
    };
    $.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
    $.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
    $.fn.tree.defaults.onLoadError = easyuiErrorFunction;
    $.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
    $.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
    $.fn.form.defaults.onLoadError = easyuiErrorFunction;
    /**
     * 取消easyui默认开启的parser
     * 在页面加载之前，先开启一个进度条
     * 然后在页面所有easyui组件渲染完毕后，关闭进度条
     * @requires jQuery,EasyUI
     */
    $.parser.auto = false;
    $(function () {
        $.messager.progress({
            text: '加载中....',
            interval: 100
        });
        $.parser.parse(window.document);
        window.setTimeout(function () {
            $.messager.progress('close');
            if (self != parent) {
                window.setTimeout(function () {
                    try {
                        parent.$.messager.progress('close');
                    } catch (e) {
                    }
                }, 500);
            }
        }, 1);
        $.parser.auto = true;
    });
    //IE检测
    jqueryUtil.isLessThanIe8 = function () {
        return ($.browser.msie && $.browser.version < 8);
    };
    /**
     * 使panel和datagrid在加载时提示
     * @requires jQuery,EasyUI
     */
    $.fn.panel.defaults.loadingMessage = '加载中....';
    $.fn.datagrid.defaults.loadMsg = '加载中....';

    /**
     * @requires jQuery,EasyUI
     * 防止panel/window/dialog组件超出浏览器边界
     * @param left
     * @param top
     */
    var easyuiPanelOnMove = function (left, top) {
        var l = left;
        var t = top;
        if (l < 1) {
            l = 1;
        }
        if (t < 1) {
            t = 1;
        }
        var width = parseInt($(this).parent().css('width')) + 14;
        var height = parseInt($(this).parent().css('height')) + 14;
        var right = l + width;
        var buttom = t + height;
        var browserWidth = $(window).width();
        var browserHeight = $(window).height();
        if (right > browserWidth) {
            l = browserWidth - width;
        }
        if (buttom > browserHeight) {
            t = browserHeight - height;
        }
        $(this).parent().css({/* 修正面板位置 */
            left: l,
            top: t
        });
    };
    $.fn.dialog.defaults.onMove = easyuiPanelOnMove;
    $.fn.window.defaults.onMove = easyuiPanelOnMove;
    $.fn.panel.defaults.onMove = easyuiPanelOnMove;

    /**
     * @author 孙宇
     * @requires jQuery,EasyUI
     * 为datagrid、treegrid增加表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中
     */
    var createGridHeaderContextMenu = function (e, field) {
        e.preventDefault();
        var grid = $(this);
        /* grid本身 */
        var headerContextMenu = this.headerContextMenu;
        /* grid上的列头菜单对象 */
        if (!headerContextMenu) {
            var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
            var fields = grid.datagrid('getColumnFields');
            for (var i = 0; i < fields.length; i++) {
                var fildOption = grid.datagrid('getColumnOption', fields[i]);
                if (!fildOption.hidden) {
                    $('<div iconCls="icon-ok" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
                } else {
                    $('<div iconCls="icon-empty" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
                }
            }
            headerContextMenu = this.headerContextMenu = tmenu.menu({
                onClick: function (item) {
                    var field = $(item.target).attr('field');
                    if (item.iconCls == 'icon-ok') {
                        grid.datagrid('hideColumn', field);
                        $(this).menu('setIcon', {
                            target: item.target,
                            iconCls: 'icon-empty'
                        });
                    } else {
                        grid.datagrid('showColumn', field);
                        $(this).menu('setIcon', {
                            target: item.target,
                            iconCls: 'icon-ok'
                        });
                    }
                }
            });
        }
        headerContextMenu.menu('show', {
            left: e.pageX,
            top: e.pageY
        });
    };
    $.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
    $.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;

    /**
     * @requires jQuery,EasyUI
     * panel关闭时回收内存
     */
    $.fn.panel.defaults.onBeforeDestroy = function () {
        var frame = $('iframe', this);
        try {
            if (frame.length > 0) {
                frame[0].contentWindow.document.write('');
                frame[0].contentWindow.close();
                frame.remove();
                if ($.browser.msie) {
                    CollectGarbage();
                }
            }
        } catch (e) {
        }
    };
    /**
     * @author 孙宇
     * @requires jQuery,EasyUI
     * 扩展treegrid，使其支持平滑数据格式
     */
    $.fn.treegrid.defaults.loadFilter = function (data, parentId) {
        var opt = $(this).data().treegrid.options;
        var idField, textField, parentField;
        if (opt.parentField) {
            idField = opt.idField || 'id';
            textField = opt.textField || 'text';
            parentField = opt.parentField;
            var i, l, treeData = [], tmpMap = [];
            for (i = 0, l = data.length; i < l; i++) {
                tmpMap[data[i][idField]] = data[i];
            }
            for (i = 0, l = data.length; i < l; i++) {
                if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
                    if (!tmpMap[data[i][parentField]]['children'])
                        tmpMap[data[i][parentField]]['children'] = [];
                    data[i]['id'] = data[i][idField];
                    data[i]['text'] = data[i][textField];
                    tmpMap[data[i][parentField]]['children'].push(data[i]);
                } else {
                    data[i]['id'] = data[i][idField];
                    data[i]['text'] = data[i][textField];
                    treeData.push(data[i]);
                }
            }
            return treeData;
        }
        return data;
    };

    /**
     * @author 夏悸
     * @requires jQuery,EasyUI
     * 扩展tree，使其支持平滑数据格式
     */
    $.fn.tree.defaults.loadFilter = function (data, parent) {
        var opt = $(this).data().tree.options;
        var idField, textField, parentField;
        if (opt.parentField) {
            idField = opt.idField || 'id';
            textField = opt.textField || 'text';
            parentField = opt.parentField;
            var i, l, treeData = [], tmpMap = [];
            for (i = 0, l = data.length; i < l; i++) {
                tmpMap[data[i][idField]] = data[i];
            }
            for (i = 0, l = data.length; i < l; i++) {
                if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
                    if (!tmpMap[data[i][parentField]]['children'])
                        tmpMap[data[i][parentField]]['children'] = [];
                    data[i]['id'] = data[i][idField];
                    data[i]['text'] = data[i][textField];
                    tmpMap[data[i][parentField]]['children'].push(data[i]);
                } else {
                    data[i]['id'] = data[i][idField];
                    data[i]['text'] = data[i][textField];
                    treeData.push(data[i]);
                }
            }
            return treeData;
        }
        return data;
    };
    /**
     * @author sy
     * @requires jQuery,EasyUI
     * 扩展combotree，使其支持平滑数据格式
     */
    $.fn.combotree.defaults.loadFilter = function (data, parent) {
        var opt = $(this).data().tree.options;
        var idField, textField, parentField, levelField;
        if (opt.parentField) {
            idField = opt.idField || 'id';
            textField = opt.textField || 'text';
            parentField = opt.parentField;
            levelField = opt.levelField;

            var i, l, treeData = [], tmpMap = [];
            for (i = 0, l = data.length; i < l; i++) {
                if (levelField) {
                    data[i]['attributes'] = [];
                    data[i]['attributes'].push({'level': data[i][levelField]});
                }
                tmpMap[data[i][idField]] = data[i];
            }
            for (i = 0, l = data.length; i < l; i++) {
                if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
                    if (!tmpMap[data[i][parentField]]['children'])
                        tmpMap[data[i][parentField]]['children'] = [];
                    data[i]['id'] = data[i][idField];
                    data[i]['text'] = data[i][textField];
                    tmpMap[data[i][parentField]]['children'].push(data[i]);
                } else {
                    data[i]['id'] = data[i][idField];
                    data[i]['text'] = data[i][textField];
                    treeData.push(data[i]);
                }
                //alert(JSON.stringify(data[i]));
            }
            //alert(JSON.stringify(treeData));
            return treeData;
        }
        return data;
    };
    //序列化表单到对象
    jqueryUtil.serializeObject = function (form) {
        //console.dir(form.serializeArray());
        var o = {};
        $.each(form.serializeArray(), function (index) {
            if (o[this['name']]) {
                o[this['name']] = o[this['name']] + "," + (this['value'] == '' ? ' ' : this['value']);
            } else {
                o[this['name']] = this['value'] == '' ? ' ' : this['value'];
            }
        });
        //console.dir(o);
        return o;
    };

    /**
     * 扩展树表格级联选择（点击checkbox才生效）：
     * 自定义两个属性：
     * cascadeCheck ：普通级联（不包括未加载的子节点）
     * deepCascadeCheck ：深度级联（包括未加载的子节点）
     */
    $.extend($.fn.treegrid.defaults, {
        onLoadSuccess: function () {
            var target = $(this);
            var opts = $.data(this, "treegrid").options;
            var panel = $(this).datagrid("getPanel");
            var gridBody = panel.find("div.datagrid-body");
            var idField = opts.idField;//这里的idField其实就是API里方法的id参数
            gridBody.find("div.datagrid-cell-check input[type=checkbox]").unbind(".treegrid").click(function (e) {
                if (opts.singleSelect) return;//单选不管
                if (opts.cascadeCheck || opts.deepCascadeCheck) {
                    var id = $(this).parent().parent().parent().attr("node-id");
                    var status = false;
                    if ($(this).attr("checked")) {
                        target.treegrid('select', id);
                        status = true;
                    } else {
                        target.treegrid('unselect', id);
                    }
                    //级联选择父节点
                    selectParent(target, id, idField, status);
                    selectChildren(target, id, idField, opts.deepCascadeCheck, status);
                }
                e.stopPropagation();//停止事件传播
            });
        }
    });

    /**
     * 扩展树表格级联勾选方法：
     * @param {Object} container
     * @param {Object} options
     * @return {TypeName}
     */
    $.extend($.fn.treegrid.methods, {
        /**
         * 级联选择
         * @param {Object} target
         * @param {Object} param
         * param包括两个参数:
         * id:勾选的节点ID
         * deepCascade:是否深度级联
         * @return {TypeName}
         */
        cascadeCheck: function (target, param) {
            var opts = $.data(target[0], "treegrid").options;
            if (opts.singleSelect)
                return;
            var idField = opts.idField;//这里的idField其实就是API里方法的id参数
            var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选
            var selectNodes = $(target).treegrid('getSelections');//获取当前选中项
            for (var i = 0; i < selectNodes.length; i++) {
                if (selectNodes[i][idField] == param.id)
                    status = true;
            }
            //级联选择父节点
            selectParent(target, param.id, idField, status);
            selectChildren(target, param.id, idField, param.deepCascade, status);
        }
    });

    /**
     * 级联选择父节点
     * @param {Object} target
     * @param {Object} id 节点ID
     * @param {Object} status 节点状态，true:勾选，false:未勾选
     * @return {TypeName}
     */
    function selectParent(target, id, idField, status) {
        var parent = target.treegrid('getParent', id);
        if (parent) {
            var parentId = parent[idField];
            if (status)
                target.treegrid('select', parentId);
            /*else
                target.treegrid('unselect', id);*/
            selectParent(target, parentId, idField, status);
        }
    }

    /**
     * 级联选择子节点
     * @param {Object} target
     * @param {Object} id 节点ID
     * @param {Object} deepCascade 是否深度级联
     * @param {Object} status 节点状态，true:勾选，false:未勾选
     * @return {TypeName}
     */
    function selectChildren(target, id, idField, deepCascade, status) {
        //深度级联时先展开节点
        if (status && deepCascade)
            target.treegrid('expand', id);
        //根据ID获取下层孩子节点
        var children = target.treegrid('getChildren', id);
        for (var i = 0; i < children.length; i++) {
            var childId = children[i][idField];
            if (status)
                target.treegrid('select', childId);
            else
                target.treegrid('unselect', childId);
            selectChildren(target, childId, idField, deepCascade, status);//递归选择子节点
        }
    }
    //扩展datagrid截取内容鼠标经过时提示
    $.extend($.fn.datagrid.methods, {
        /**
         * 开打提示功能（基于1.3.3版本）
         * @param {} jq
         * @param {} params 提示消息框的样式
         * @return {}
         */
        doCellTip:function (jq, params) {
            function showTip(showParams, td, e, dg) {
                //无文本，不提示。
                if ($(td).text() == "") return;
                params = params || {};
                var options = dg.data('datagrid');
                showParams.content = '<div class="tipcontent">' + showParams.content + '</div>';
                $(td).tooltip({
                    content:showParams.content,
                    trackMouse:true,
                    position:params.position,
                    onHide:function () {
                        $(this).tooltip('destroy');
                    },
                    onShow:function () {
                        var tip = $(this).tooltip('tip');
                        if(showParams.tipStyler){
                            tip.css(showParams.tipStyler);
                        }
                        if(showParams.contentStyler){
                            tip.find('div.tipcontent').css(showParams.contentStyler);
                        }
                    }
                }).tooltip('show');
            };
            return jq.each(function () {
                var grid = $(this);
                var options = $(this).data('datagrid');
                if (!options.tooltip) {
                    var panel = grid.datagrid('getPanel').panel('panel');
                    //panel.find('.datagrid-body').each(function () {//hzm 2017-08-14 14:14
                    panel.find('.datagrid-view2').each(function () {
                        //var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;//hzm 2017-08-14 14:14
                        var delegateEle = $(this).find('> div.datagrid-body').length ? $(this).find('> div.datagrid-body')[0] : this;
                        $(delegateEle).undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove').delegate('td[field]', {
                            'mouseover':function (e) {
                                //if($(this).attr('field')===undefined) return;
                                if (!$(this).children('div').hasClass('datagrid-cell')) {//hzm 2017-08-14 14:14
                                    $(this).undelegate();
                                    return;
                                }
                                var that = this;
                                var setField = null;
                                if(params.specialShowFields && params.specialShowFields.sort){
                                    for(var i=0; i<params.specialShowFields.length; i++){
                                        if(params.specialShowFields[i].field == $(this).attr('field')){
                                            setField = params.specialShowFields[i];
                                        }
                                    }
                                }
                                if(setField==null){
                                    $("body").find('> div.datagrid-cell').remove();//hzm 2017-08-14 14:14
                                    options.factContent = $(this).find('>div').clone().css({'margin-left':'-5000px', 'width':'auto', 'display':'inline', 'position':'absolute'}).appendTo('body');
                                    var factContentWidth = options.factContent.width();
                                    params.content = $(this).text();
                                    if (params.onlyShowInterrupt) {
                                        if (factContentWidth > $(this).width()) {
                                            showTip(params, this, e, grid);
                                        }
                                    } else {
                                        showTip(params, this, e, grid);
                                    }
                                }else{
                                    //panel.find('.datagrid-body').each(function(){//hzm 2017-08-14 14:14
                                    panel.find('.datagrid-view2').each(function () {
                                        var trs = $(this).find('tr[datagrid-row-index="' + $(that).parent().attr('datagrid-row-index') + '"]');
                                        trs.each(function(){
                                            var td = $(this).find('> td[field="' + setField.showField + '"]');
                                            if(td.length){
                                                params.content = td.text();
                                            }
                                        });
                                    });
                                    showTip(params, this, e, grid);
                                }
                            },
                            'mouseout':function (e) {
                                if (options.factContent) {
                                    options.factContent.remove();
                                    options.factContent = null;
                                }
                            }
                        });
                    });
                }
            });
        },
        /**
         * 关闭消息提示功能（基于1.3.3版本）
         * @param {} jq
         * @return {}
         */
        cancelCellTip:function (jq) {
            return jq.each(function () {
                var data = $(this).data('datagrid');
                if (data.factContent) {
                    data.factContent.remove();
                    data.factContent = null;
                }
                var panel = $(this).datagrid('getPanel').panel('panel');
                //panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove');
                panel.find('.datagrid-view2').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove')
            });
        }
    });
    //cookies
    jqueryUtil.cookies = (function () {
        var fn = function () {
        };
        fn.prototype.get = function (name) {
            var cookieValue = "";
            var search = name + "=";
            if (document.cookie.length > 0) {
                offset = document.cookie.indexOf(search);
                if (offset != -1) {
                    offset += search.length;
                    end = document.cookie.indexOf(";", offset);
                    if (end == -1) end = document.cookie.length;
                    cookieValue = decodeURIComponent(document.cookie.substring(offset, end))
                }
            }
            return cookieValue;
        };
        fn.prototype.set = function (cookieName, cookieValue, DayValue) {
            var expire = "";
            var day_value = 1;
            if (DayValue != null) {
                day_value = DayValue;
            }
            expire = new Date((new Date()).getTime() + day_value * 86400000);
            expire = "; expires=" + expire.toGMTString();
            document.cookie = cookieName + "=" + encodeURIComponent(cookieValue) + ";path=/" + expire;
        }
        fn.prototype.remvoe = function (cookieName) {
            var expire = "";
            expire = new Date((new Date()).getTime() - 1);
            expire = "; expires=" + expire.toGMTString();
            document.cookie = cookieName + "=" + escape("") + ";path=/" + expire;
            /*path=/*/
        };
        return new fn();
    })();
    //获取随机时间
    jqueryUtil.getRandTime = function () {
        var nowDate = new Date();
        var str = "";
        var hour = nowDate.getHours();//HH
        str += ((hour < 10) ? "0" : "") + hour;
        var min = nowDate.getMinutes();//MM
        str += ((min < 10) ? "0" : "") + min;
        var sec = nowDate.getSeconds(); //SS
        str += ((sec < 10) ? "0" : "") + sec;
        return Number(str);
    };
    //切换皮肤
    jqueryUtil.chgSkin = function (selectId, cookiesColor) {
        docchgskin(document, selectId, cookiesColor);
        $("iframe").each(function () {
            var dc = this.contentWindow.document;
            docchgskin(dc, selectId, cookiesColor);
        });
        function docchgskin(dc, selectId, cookiesColor) {
            removejscssfile(dc, "/css/easyui/" + cookiesColor + "/easyui.css", "css");
            createLink(dc, "/css/easyui/" + selectId + "/easyui.css");
        }

        function createLink(dc, url) {
            var urls = url.replace(/[,]\s*$/ig, "").split(",");
            var links = [];
            for (var i = 0; i < urls.length; i++) {
                links[i] = dc.createElement("link");
                links[i].rel = "stylesheet";
                links[i].href = urls[i];
                dc.getElementsByTagName("head")[0].appendChild(links[i]);
            }
        }

        function removejscssfile(dc, filename, filetype) {
            var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"
            var targetattr = (filetype == "js") ? "src" : (filetype == "css") ? "href" : "none"
            var allsuspects = dc.getElementsByTagName(targetelement)
            for (var i = allsuspects.length; i >= 0; i--) {
                if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null && allsuspects[i].getAttribute(targetattr).indexOf(filename) != -1)
                    allsuspects[i].parentNode.removeChild(allsuspects[i])
            }
        }
    };
    /*
     * 格式化字符串
     */
    $.formatString = function (str) {
        for (var i = 0; i < arguments.length - 1; i++) {
            str = str.replace("{" + i + "}", arguments[i + 1]);
        }
        return str;
    };

    //高级查询
    jqueryUtil.gradeSearch = function ($dg, formId, url) {
        $("<div/>").dialog({
            href: url,
            modal: true,
            title: '高级查询',
            top: 120,
            width: 480,
            buttons: [
                {
                    text: '增加一行',
                    iconCls: 'icon-add',
                    handler: function () {
                        var currObj = $(this).closest('.panel').find('table');
                        currObj.find('tr:last').clone().appendTo(currObj);
                        currObj.find('tr:last a').show();
                    }
                },
                {
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $dg.datagrid('reload', jqueryUtil.serializeObject($(formId)));
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $(this).closest('.window-body').dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    };

    /**
     * @author 孙宇
     * @requires jQuery,EasyUI
     * 创建一个模式化的dialog
     * @returns $.modalDialog.handler 这个handler代表弹出的dialog句柄
     * @returns $.modalDialog.xxx 这个xxx是可以自己定义名称，主要用在弹窗关闭时，刷新某些对象的操作，可以将xxx这个对象预定义好
     */
    window.handlerArray=new Array();
    $.modalDialog = function (options) {
        if ($.modalDialog.handler == undefined||$.modalDialog.handler.dialog('options').title!=options.title) {// 避免重复弹出
            var opts = $.extend({
                title: '',
                width: 840,
                height: 680,
                modal: true
            }, options);
            opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
            var dialogHandler = $('<div/>').dialog(opts);
            handlerArray.push(dialogHandler);
            return $.modalDialog.handler = dialogHandler;
        }
    };

    //验证规则-----------------以下
    $.extend($.fn.validatebox.defaults.rules,{
        IsNumber:{
            validator:function(value,parm){
                return !isNaN(value);
            }
        },
        message:'请输入数字'
    });
    //验证规则-----------------以上

    $.extend($.fn.datagrid.defaults.editors, {
        combogrid: {
            init: function(container, options){
                var input = $('<input type="text" class="datagrid-editable-input"/>').appendTo(container);
                input.combogrid(options);
                return input;
            },
            destroy: function(target){
                $(target).combogrid('destroy');
            },
            getValue: function(target){
                return $(target).combogrid('getValue');
            },
            setValue: function(target, value){
                $(target).combogrid('setValue',value);
            },
            resize: function(target, width){
                $(target).combogrid('resize',width);
            }
        }
    });

    /*
     * 定义图标样式的数组
     */
    $.iconData = [
        {
            value: 'icon-blank',
            text: '默认'
        },
        {
            value: 'icon-add',
            text: '新增'
        },
        {
            value: 'icon-edit',
            text: '修改'
        },
        {
            value: 'icon-remove',
            text: '移除'
        },
        {
            value: 'icon-save',
            text: '保存'
        },
        {
            value: 'icon-cut',
            text: '剪切'
        },
        {
            value: 'icon-ok',
            text: '确认'
        },
        {
            value: 'icon-no',
            text: '取消'
        },
        {
            value: 'icon-cancel',
            text: '删除'
        },
        {
            value: 'icon-reload',
            text: '重载'
        },
        {
            value: 'icon-search',
            text: '查询'
        },
        {
            value: 'icon-print',
            text: '打印'
        },
        {
            value: 'icon-help',
            text: '帮助'
        },
        {
            value: 'icon-undo',
            text: '撤消'
        },
        {
            value: 'icon-redo',
            text: '重做'
        },
        {
            value: 'icon-back',
            text: '返回'
        },
        {
            value: 'icon-sum',
            text: '统计'
        },
        {
            value: 'icon-tip',
            text: '提示'
        },
        {
            value: 'icon-mini-add',
            text: '增加'
        },
        {
            value: 'icon-mini-edit',
            text: '编辑'
        },
        {
            value: 'icon-mini-refresh',
            text: '刷新'
        },
        {
            value: 'icon-up',
            text: '上传'
        },
        {
            value: 'icon-down',
            text: '下载'
        },
        {
            value: 'icon-sale',
            text: '销售'
        },
        {
            value: 'icon-view',
            text: '查看'
        },
        {
            value: 'icon-audit',
            text: '审核'
        },
        {
            value: 'icon-money',
            text: '回款'
        },
        {
            value: 'icon-exchange',
            text: '换货'
        },
        {
            value: 'icon-returns',
            text: '退货'
        },
        {
            value: 'icon-delivery',
            text: '发货'
        },
        {
            value: 'icon-reject',
            text: '回退'
        },
        {
            value: 'icon-cartin',
            text: '入库'
        },
        {
            value: 'icon-lock',
            text:'锁定'
        },
        {
            value: 'icon-unlock',
            text:'解锁'
        },
        {
            value: 'icon-address',
            text:'物流'
        },
        {
            value: 'icon-log',
            text:'日志'
        },
        {
            value: 'icon-user',
            text:'用户'
        },
        {
            value: 'icon-backward',
            text:'向后'
        },
        {
            value: 'icon-foward',
            text:'向前'
        },
        {
            value: 'icon-excel',
            text:'导出excel'
        },{
            value: 'icon-timeadd',
            text:'时间添加'
        },{
            value: 'icon-timedel',
            text:'时间删除'
        },{
            value: 'icon-timeedit',
            text:'时间编辑'
        },{
            value: 'icon-xcz',
            text:'修车仔'
        },{
            value: 'icon-wchat',
            text:'微信'
        },{
            value: 'icon-prize',
            text:'奖牌'
        },{
            value: 'icon-set',
            text:'设置'
        },{
            value: 'icon-invoice',
            text:'发票'
        },{
            value: 'icon-panDian',
            text:'盘点'
        },{
            value: 'icon-panKui',
            text:'盘亏'
        },{
            value: 'icon-panGet',
            text:'盘盈'
        },{
            value: 'icon-panAll',
            text:'全部'
        },{
            value: 'icon-checkAll',
            text:'全盘'
        }
    ];
    //定义kindEditor可用功能菜单
    $.kindMenu=[
        'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'cut', 'copy', 'paste',
        'plainpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
        'superscript', 'clearhtml', 'quickformat', 'selectall', '|','formatblock', 'fontname',
        'fontsize', '|', 'forecolor', 'hilitecolor', 'bold','italic', 'underline', 'strikethrough',
        'lineheight', 'removeformat', '|', 'image', 'multiimage',
        'table', 'hr', 'emoticons', 'baidumap', 'pagebreak', 'anchor', 'link', 'unlink', '|','fullscreen', 'about'
    ];
})(jQuery);