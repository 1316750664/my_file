package com.util.tools;

import com.bean.FileCacheBean;
import com.bean.HtmlReplaceForBean;
import com.ctd.util.database.MemCachedUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzm on 2014/8/12.
 */
public class HtmlGenerator {
    public static String root = System.getProperty("bbsApp.root");
    private static MemCachedUtil memCachedUtil = MemCachedUtil.getInstance();
    private static Pattern pattern0 = Pattern.compile("###file:(\\w+\56\\w+)###");
    private static Pattern pattern1 = Pattern.compile("<!--fs:(\\w+)-->");

    private static class SingletonHtmlGenerator {
        private static HtmlGenerator INSTANCE = new HtmlGenerator();
    }

    public static HtmlGenerator getInstance() {
        return SingletonHtmlGenerator.INSTANCE;
    }

    private HtmlGenerator() {
    }

    public String replaceHtml(String html, Map<String, String> mField, Map<String, List<Map<String, String>>> mList) {
        if (html == null) {
            return "";
        }
        html = replaceFile(html);
        html = replaceField(html, mField);
        int i = 0;
        while (isContainFor(html) && mList != null && mList.size() != 0) {
            if (i++ > 1000) {//避免进入死循环，因为有些可能模板中有，但数据没有
                break;
            }
            html = replaceLoop(html, mList);
        }
        html = html.replaceAll("###file:(\\w+56\\w+)###", "");
        html = html.replaceAll("###field:\\w+###", "");
        while (isContainFor(html)) {
            html = findReplaceFor(html);
        }
        return html;
    }

    public String replaceFile(String html) {
        String replaceStr = null;
        String filename = null;
        String fileContent = null;
//        StringBuilder fileStr = null;
//        BufferedReader in = null;
//        String line = null;
        try {
            Matcher matcher = pattern0.matcher(html);
            while (matcher.find()) {
                replaceStr = matcher.group();
                filename = matcher.group(1);
                filename = root + "/" + filename;
                filename = java.net.URLDecoder.decode(filename, "UTF-8");
//                in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
//                fileStr = new StringBuilder();
//                while ((line = in.readLine()) != null) {
//                    fileStr.append(line);
//                }
//                in.close();
//                html = html.replace(replaceStr, fileStr.toString());
//                fileStr.delete(0, fileStr.length());//清理内存
                fileContent = _GetFileBean(filename);
                if (fileContent == null) {
                    continue;
                }
                html = html.replace(replaceStr, fileContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    /**
     * filename为文件全路径
     *
     * @param filename
     * @return
     */
    public String _GetFileBean(String filename) {
        String fileContent = null;
        try {
            File file = new File(filename);
            long lastTime = file.lastModified();
            int fileCode = filename.hashCode();
            String key = "file_" + fileCode;
            FileCacheBean fileCacheBean = null;
            Object object = memCachedUtil.get(key);
            if (object != null) {
                fileCacheBean = StaticUtil.objectMapper.readValue(object.toString(), FileCacheBean.class);
                if (lastTime <= fileCacheBean.getLastTime()) {//文件未修改
                    return fileCacheBean.getContent();
                }
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder fileBuilder = new StringBuilder("");
            String line = null;
            while ((line = in.readLine()) != null) {
                fileBuilder.append(line);
            }
            in.close();
            fileContent = fileBuilder.toString();
            fileBuilder.delete(0, fileBuilder.length());//清理内存
            fileCacheBean = new FileCacheBean(fileCode, lastTime, fileContent);
            memCachedUtil.add(key, StaticUtil.objectMapper.writeValueAsString(fileCacheBean));//将结果缓存
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public String replaceField(String html, Map<String, String> map) {
        if (map != null) {
            String key = null;
            String value = null;
            Map.Entry<String, String> entry = null;
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                entry = iterator.next();
                key = entry.getKey();
                value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                html = html.replace("###field:" + key + "###", value);
            }
        }
        return html;
    }

    public String replaceLoop(String html, Map<String, List<Map<String, String>>> mList) {
        if (mList == null || mList.isEmpty()) {
            return html;
        }
        Map<String, List<HtmlReplaceForBean>> replaceForBeanMap = findFor(html);
        if (replaceForBeanMap.size() == 0) {
            return html;
        }
        HtmlReplaceForBean bean = null;
        String express = null;
        String content = null;
        Map<String, String> row = null;
        int rowSize = 0;
        int repeatCount = 1;
        Map.Entry<String, List<HtmlReplaceForBean>> entry0 = null;
        String key0 = null;
        List<HtmlReplaceForBean> value0 = null;
        Map.Entry<String, String> entry1 = null;
        String key1 = null;
        String value1 = null;
        Iterator<HtmlReplaceForBean> iterator2 = null;
        Iterator<Map.Entry<String, List<HtmlReplaceForBean>>> iterator0 = replaceForBeanMap.entrySet().iterator();
        while (iterator0.hasNext()) {
            entry0 = iterator0.next();
            key0 = entry0.getKey();//for name
            value0 = entry0.getValue();// for bean
            repeatCount = value0.size();
            List<Map<String, String>> rowList = mList.get(key0);
            if (rowList == null || (rowSize = rowList.size()) == 0) {
                return html;
            }
            for (int i = 0; i < rowSize; i = i + repeatCount) {
                for (int j = 0; j < repeatCount; j++) {//单
                    if (i + j == rowSize) {
                        break;
                    }
                    bean = value0.get(j);
                    express = bean.getExpress();
                    content = bean.getContent();
                    row = rowList.get(i + j);
                    Iterator<Map.Entry<String, String>> iterator1 = row.entrySet().iterator();
                    while (iterator1.hasNext()) {
                        entry1 = iterator1.next();
                        key1 = entry1.getKey();
                        value1 = entry1.getValue();
                        if (value1 == null) {
                            value1 = "";
                        }
                        //key0==name
                        content = content.replace("###" + key0 + ":" + key1 + "###", value1);
                    }
//                    System.out.println(express);
                    html = html.replace(express, content + express);
                }
            }
            iterator2 = value0.iterator();
            while (iterator2.hasNext()) {
                bean = iterator2.next();
                express = bean.getExpress();
                html = html.replace(express, "");
                iterator2.remove();
            }
            iterator0.remove();
        }
        return html;
    }

    public Map<String, List<HtmlReplaceForBean>> findFor(String html) {
        Map<String, List<HtmlReplaceForBean>> map = new HashMap<String, List<HtmlReplaceForBean>>();
        String name = null;
        String express = null;
        String header = null;
        String footer = null;
        String content = null;
        int headerLen = 0;
        int subHeaderIndex = 0;
        int subFooterIndex = 0;
        int nextFindStartIndex = 0;
        String subString = null;
//        Pattern pattern = Pattern.compile("<!--fs:(\\w+)-->((.|\\n)+?)<!--fe:(\\1)-->");
        Matcher matcher = pattern1.matcher(html);
        while (matcher.find()) {
//            express=matcher.group(0);
//            name=matcher.group(1);
//            content=matcher.group(2);
            header = matcher.group(0);
            name = matcher.group(1);
            footer = "<!--fe:" + name + "-->";

            headerLen = header.length();
            subString = html.substring(nextFindStartIndex);
            subHeaderIndex = subString.indexOf(header);
            if (subHeaderIndex < 0) {
                continue;
            }
            subString = subString.substring(subHeaderIndex);//再次缩减到起for标志
            subFooterIndex = subString.indexOf(footer);
            if (subFooterIndex < 0) {
                continue;
            }
            content = subString.substring(headerLen, subFooterIndex);
            express = header + content + footer;//拼接表达式字符串

            nextFindStartIndex = html.indexOf(express) + headerLen;//计算出后一个for循环在原字符串中的位置

            List<HtmlReplaceForBean> beans = map.get(name);
            if (beans == null) {
                beans = new ArrayList<HtmlReplaceForBean>();
                map.put(name, beans);
            }
            beans.add(new HtmlReplaceForBean(express, content));
//            System.out.println(matcher.group(0));//express
//            System.out.println(matcher.group(1));//name
//            System.out.println(matcher.group(2));//content
//            System.out.println(matcher.group(3));
//            System.out.println(matcher.group(4));
//            System.out.println("======");
        }

        return map;
    }

    public String findReplaceFor(String html) {
        Matcher matcher = pattern1.matcher(html);
        String express = null;
        String name = null;
        String header = null;
        String footer = null;
        if (matcher.find()) {
            header = matcher.group(0);
            name = matcher.group(1);
            footer = "<!--fe:" + name + "-->";
            express = html.substring(html.indexOf(header), html.indexOf(footer) + footer.length());
            html = html.replace(express, "");
        }

        return html;
    }

    private boolean isContainFor(String html) {
        if (html == null) {
            return false;
        }
        Matcher matcher = pattern1.matcher(html);
        return matcher.find();
    }
}