var config = {
    type : 1
};
var MyPager={
	printPage:function(currentPage,pageSize,itemCount,o){
		currentPage=parseInt(currentPage);
		pageSize=parseInt(pageSize);
		itemCount=parseInt(itemCount);
        o == undefined ? o = config:o = $.extend({},config,o);

      var sideNum=1;
        var pageCount;
        if(o.pageCount == undefined){
            pageCount = Math.ceil(itemCount / pageSize);
            if(itemCount <= pageSize ){
                return '';
            }
        }else{
            pageCount = o.pageCount
        }


        if (pageCount <= 0){
			pageCount = 1;
		}
        if (currentPage > pageCount){
			currentPage = pageCount;
		}
        if (currentPage < 1){
			currentPage = 1;
		}
		
		var html='<div class="pager">';
		html+='<ul>';
		//打印 上一页
        if (currentPage > 1) {
            html += '<li><a href="javascript:void(0);" class="page_num page_prev" title="上一页"" onclick="pageClick(\'minus\','+ o.type +')"><i class="iconfont">&#xe609;</i>上一页</a></li>';
        }
		//打印 1..
        if ((currentPage - sideNum) > 1) {
            html += '<li><a href="javascript:void(0);" class="page_num" title="第1页" onclick="pageClick(1,'+ o.type +')">1</a></li>';
			html += '<li>...</li>';
        }
        
        //打印页码
        var _minNum = currentPage - sideNum;
        if (_minNum < 1){
			_minNum = 1;
		}
        var _maxNum = sideNum * 2 + _minNum;
        if (_maxNum > pageCount){
			_maxNum = pageCount;
		}
        for (var i = _minNum; i <= _maxNum; i++) {
            if (i == currentPage) {
                html += '<li><a href="javascript:void(0);" class="page_num page_curr" title="第'+i+'页" onclick="pageClick('+i+','+ o.type +')">'+i+'</a></li>';
            }
            else{
				html += '<li><a href="javascript:void(0);" class="page_num" title="第'+i+'页" onclick="pageClick('+i+','+ o.type +')">'+i+'</a></li>';
			}
        }
        if (_maxNum < pageCount) {
            html += '<li>...</li><li><a href="javascript:void(0);" class="page_num" title="第'+pageCount+'页" onclick="pageClick('+pageCount+','+ o.type +')">'+pageCount+'</a></li>';
        }
        //下一页
        if (currentPage < pageCount) {
            html += '<li><a href="javascript:void(0);" class="page_num page_next" title="下一页"" onclick="pageClick(\'add\','+ o.type +')">下一页<i class="iconfont">&#xe60a;</i></a> </li>';
        }
		
		html+='</ul>';
		html+='</div>';
		
		return html;
	}
}