package com.service.Service;

import com.util.tools.HtmlGenerator;

/**
 * Created by hzm on 2015/1/31.
 */
public class ComHtmlHandler {
    private HtmlGenerator htmlGenerator = HtmlGenerator.getInstance();

    private static class SingletonComHtmlHandler {
        private static ComHtmlHandler INSTANCE = new ComHtmlHandler();
    }

    public static ComHtmlHandler getInstance() {
        return SingletonComHtmlHandler.INSTANCE;
    }

    private ComHtmlHandler() {
    }

    public String htmlHandler(int position) {
        StringBuilder sb = new StringBuilder("");

        switch (position) {
            case 1:
                sb.append(htmlGenerator.replaceFile("###file:Firsthead.html###"));
                break;
            case 2:
                sb.append(htmlGenerator.replaceFile("###file:headfour.html###"));
                break;
            case 3:
                sb.append(htmlGenerator.replaceFile("###file:footThird.html###"));
                break;
            case 4:
                sb.append(htmlGenerator.replaceFile("###file:Firsthead.html###"));
                sb.append(htmlGenerator.replaceFile("###file:search.html###"));
                sb.append(htmlGenerator.replaceFile("###file:Thirdhead.html###"));
                break;
            case 5:
                sb.append(htmlGenerator.replaceFile("###file:Firsthead.html###"));
                sb.append(htmlGenerator.replaceFile("###file:search.html###"));
                break;
            case 6://搜索关键词
                sb.append(htmlGenerator.replaceFile("###file:search_key.html###"));
                break;
            case 7:
                sb.append(htmlGenerator.replaceFile("###file:foot.html###"));
                break;
            default:
                sb.append("数据加载有误，请刷新重试！！");
                break;
        }
        return sb.toString();
    }
}