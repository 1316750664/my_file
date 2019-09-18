package com.util.tools;

import com.bean.JsonCommandBean;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hzm on 2014/7/5.
 */
public class JsonSqlMap {

    public JsonSqlMap() {
    }

    public String execute(Map<String, String[]> params) throws UnsupportedEncodingException, InvocationTargetException, IllegalAccessException, SQLException {
        String taskId = null;
        String sql = null;

        //根据请求参数，查找任务生成分类Map
        Map<String, JsonCommandBean> taskMap = requestHandler(params);
        //taskId,可能批处理的sql
        Map<String, List<String>> batchSQLTask = new HashMap<String, List<String>>(taskMap.size());
        //遍历命令参数，组织sql命令
        Iterator<Map.Entry<String, JsonCommandBean>> taskIterator = taskMap.entrySet().iterator();
        Map.Entry<String, JsonCommandBean> entry = null;
        JsonCommandBean entryValue = null;
        List<String> batchSQL = null;
        Document paramDoc = null;
        Element root = null;
        Iterator<Element> it = null;
        Element paramElement = null;
        String paramName = null;
        String paramDataType = null;
        String[] paramValues = null;
        int batchLen = 0;
        int paramLen = 0;
        String comSQL = null;
        String paramValue = null;
        while (taskIterator.hasNext()) {
            entry = taskIterator.next();
            entryValue = entry.getValue();
            taskId = entryValue.getTaskId();
            sql = SqlXmlRead.getSQL(taskId);
            if (sql == null || "".equals(sql)) {
                throw new RuntimeException("未找到SQL语句");
            }
            batchSQL = batchSQLTask.get(taskId);
            if (batchSQL == null) {
                batchSQL = new ArrayList<String>();
                batchSQL.add(sql);//存入默认可能带@的原sql
                batchSQLTask.put(taskId, batchSQL);
            }
            if (sql.indexOf("@") >= 0) {//替换成实际参数值
                paramDoc = SqlXmlRead.getSqlParamFile(taskId);
                if (paramDoc == null) {
                    throw new RuntimeException("未找到相应SQL参数配置文件");
                }
                root = paramDoc.getRootElement();
                for (it = root.elementIterator(); it.hasNext(); ) {//遍历参数配置文件中的结点，根据结点数据类型进行参数替换
                    paramElement = it.next();
                    paramName = paramElement.getName();
                    paramDataType = paramElement.getTextTrim().toLowerCase();
                    paramValues = params.get(paramName);//判断是否是多语句批处理
                    if (paramValues == null) {
                        throw new RuntimeException("缺失参数：" + paramName);
                    } else {
                        paramName = "@" + paramName + "@";//参数替换准备
                        batchLen = batchSQL.size();//已存入的相同任务值的SQL
                        comSQL = batchSQL.get(0);//用于当参数值大于batchLen时的临时用SQL，大于说明前面的参数值相同，只是此次遍历的值不同
                        paramLen = paramValues.length;
                        for (int i = 0; i < Math.max(batchLen, paramLen); i++) {
                            if (i >= paramLen) {
                                paramValue = paramValues[paramLen - 1];
                            } else {
                                paramValue = paramValues[i];
                            }
                            if ("string".equals(paramDataType)) {//数据库''相连则表示'，因为'是数据库的转义符
                                if (paramValue.indexOf("''") == -1 && paramValue.indexOf("'") >= 0) {
                                    paramValue = paramValue.replaceAll("'", "''");
                                }
                                paramValue = "'" + paramValue + "'";
                            } else if ("date".equals(paramDataType)) {
                                if ("".equals(paramValue)) {
                                    paramValue = "null";
                                } else {//防止SQL注入
//                                    if (!CheckTools.isDate(paramValue)) {
//                                        throw new RuntimeException("非法日期参数");
//                                    }
                                    paramValue = "'" + paramValue + "'";
                                }
                            } else if ("number".equals(paramDataType)) {
                                if ("".equals(paramValue)) {
                                    paramValue = "null";
                                } else {//防止SQL注入
                                    if (!CheckTools.checkIsPoint(paramValue)) {
                                        throw new RuntimeException("非法数值参数");
                                    }
                                }
                            } else if ("object".equals(paramDataType)) {
                                //paramValue = paramValue;
                            } else {
                                throw new RuntimeException("非法参数类型");
                            }
//                            System.out.println("i::"+i+"---sql::" + paramValue);
                            if (i >= batchLen) {
                                //                        System.out.println("大:"+comSQL.replaceAll(paramName, paramValue));
                                batchSQL.add(comSQL.replaceAll(paramName, paramValue));
                            } else {
//                                System.out.println("小:"+batchSQL.get(i).replaceAll(paramName, paramValue));
                                batchSQL.set(i, batchSQL.get(i).replaceAll(paramName, paramValue));
                            }
                        }
                    }

                }
            }
        }
        //构造任务处理数据
        List<JsonCommandBean> queryTaskBeanList = new ArrayList<JsonCommandBean>();
        List<JsonCommandBean> updateTaskBeanList = new ArrayList<JsonCommandBean>();
        Iterator<Map.Entry<String, JsonCommandBean>> taskIterator1 = taskMap.entrySet().iterator();
        Map.Entry<String, JsonCommandBean> entry1 = null;
//        String entryKey1 = null;
        JsonCommandBean entryValue1 = null;
        Iterator<String> it1 = null;
        String sql1 = null;
        JsonCommandBean taskBean = null;
        while (taskIterator1.hasNext()) {
            entry1 = taskIterator1.next();
//          entryKey1 = entry1.getKey();
            entryValue1 = entry1.getValue();
            List<String> list = batchSQLTask.get(entryValue1.getTaskId());
            if (list == null) {
                continue;
            }
            it1 = list.iterator();
            while (it1.hasNext()) {
                sql1 = it1.next();
                taskBean = new JsonCommandBean();
                AutoBean.copyBean(entryValue1, taskBean);
                taskBean.setSql(sql1);
//                System.out.println("sql::" + sql1);
                if ("Update".equals(taskBean.getCmdType())) {//数据库命令Query/Page/Update
                    updateTaskBeanList.add(taskBean);
                } else {
                    queryTaskBeanList.add(taskBean);
                }
                it1.remove();
            }
            taskIterator1.remove();
        }
        String returnResult = new JsonDataTools().execute(queryTaskBeanList, updateTaskBeanList);

        batchSQLTask.clear();
        batchSQLTask = null;
        queryTaskBeanList.clear();
        queryTaskBeanList = null;
        updateTaskBeanList.clear();
        updateTaskBeanList = null;
        //System.gc();

        return returnResult;
    }

    public Map<String, JsonCommandBean> requestHandler(Map<String, String[]> params) {
        Map<String, JsonCommandBean> taskMap = new HashMap<String, JsonCommandBean>();
        String key = null;
        String[] values = null;
        String suffix = null;
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            key = entry.getKey();
            values = entry.getValue();
            if (key.startsWith("taskId") || key.startsWith("cmdType") || key.startsWith("page")
                    || key.startsWith("row") || key.startsWith("ide")) {
                if (values == null || values.length == 0) {
                    continue;
                }
            } else {
                continue;
            }
            suffix = suffixContains(key);
            if (suffix == null || "".equals(suffix)) {
                suffix = "0";
            }
            JsonCommandBean suffixBean = taskMap.get(suffix);
            if (suffixBean == null) {
                suffixBean = new JsonCommandBean();
                taskMap.put(suffix, suffixBean);
            }
            if (key.startsWith("taskId")) {
                suffixBean.setTaskId(values[0]);
            } else if (key.startsWith("cmdType")) {
                suffixBean.setCmdType(values[0]);
            } else if (key.startsWith("page")) {
                suffixBean.setPage(values[0]);
            } else if (key.startsWith("row")) {
                suffixBean.setRow(values[0]);
            } else if (key.startsWith("ide")) {
                suffixBean.setIde(values[0]);
            }
        }

        return taskMap;
    }

    /**
     * 查找参数是否含有_1/2.....分组相同查询语句中的参数
     *
     * @param arg0
     * @return
     */
    public String suffixContains(String arg0) {
        if (arg0 == null) {
            return null;
        }
        int strLen = arg0.length();
        int lastIndex = arg0.lastIndexOf("_");
        if (lastIndex != -1 && lastIndex + 1 < strLen) {
            String lastStr = arg0.substring(lastIndex + 1);
            if (lastStr.matches("\\d{1,}")) {
                return lastStr;
            }
        }
        return null;
    }

    /**
     * 去掉不应有的_1/2.....后缀
     *
     * @param arg0
     * @return
     */
    private String replaceArg(String arg0) {
        if (arg0 == null) {
            return null;
        }
        int lastIndex = arg0.lastIndexOf("_");
        if (lastIndex != -1) {
            String lastStr = arg0.substring(lastIndex);
            if (lastStr.matches("_\\d{1,}")) {
                return arg0.substring(0, lastIndex);
            }
        }
        return arg0;
    }

    public static void main(String[] args) {
        System.out.println(new JsonSqlMap().suffixContains("taskId"));
    }
}
