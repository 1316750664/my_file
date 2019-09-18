package com.util.tools;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-5
 * Time: 下午12:55
 * To change this template use File | Settings | File Templates.
 */
public final class AutoBean {
    /**
     * 数据库结果集自动赋值bean
     *
     * @param rs
     * @param tClass
     */
    public static List setSqlBean(ResultSet rs, Class<?> tClass) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List list = new ArrayList();
        if (rs != null) {
            StringBuilder sb = new StringBuilder("");
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                String colName = rsmd.getColumnName(i);
                sb.append(colName + ",");
            }
            String colNames = sb.toString().toLowerCase();
            Field[] fields = tClass.getDeclaredFields();
            while (rs.next()) {
                Object bean = tClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
                for (Field field : fields) {
                    Class<?> fieldType = field.getType();//返回类属性类型
                    String fieldName = field.getName();//返回类属性名
                    if (colNames.indexOf(fieldName.toLowerCase() + ",") == -1) {
                        continue;
                    }
                    Object meta = null;
                    if (fieldType.equals(String.class)) {
                        meta = rs.getString(fieldName);
                    } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                        meta = rs.getInt(fieldName);
                    } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                        meta = rs.getLong(fieldName);
                    } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                        meta = rs.getFloat(fieldName);
                    } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                        meta = rs.getDouble(fieldName);
                    } else if (fieldType.equals(java.sql.Date.class)) {
                        meta = rs.getDate(fieldName);
                    } else if (fieldType.equals(Timestamp.class)) {
                        meta = rs.getTimestamp(fieldName);
                    } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                        meta = rs.getBoolean(fieldName);
                    } else if (fieldType.equals(java.sql.NClob.class) || fieldType.equals(java.sql.Clob.class)) {
                        meta = rs.getClob(fieldName);
                    }
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = tClass.getMethod(methodName, new Class[]{fieldType});
                    method.invoke(bean, new Object[]{meta});
                }
                list.add(bean);
            }
        }
        return list;
    }

    public static String setBeanJson(Object bean) throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("{");
        Method[] methods = bean.getClass().getDeclaredMethods();//返回bean对象的所有public方法数组
        for (Method method : methods) {
            String methodName = method.getName();//返回方法名
            if (methodName.startsWith("get")) {
                String beanAtrr = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                if ("class".equals(beanAtrr)) {
                    continue;
                }
                if (method.getParameterTypes().length == 0) {
                    Object getObject = method.invoke(bean, new Object[]{});
                    String getValue = "";
                    if (getObject != null) {
                        getValue = String.valueOf(getObject);
                    }
                    sb.append("\"").append(beanAtrr).append("\"").append(":").append("\"").append(getValue).append("\"").append(",");
                }
            }
        }
        int sblen = sb.length();
        if ((",").equals(sb.substring(sblen - 1))) {
            sb.delete(sblen - 1, sblen);
        }
        sb.append("}");
        String result = sb.toString();
        sb.delete(0, sb.length());
        return result;
    }

    /**
     * 表单数据自动赋值给bean
     *
     * @param request
     * @param bean
     */
    public static void setFormBean(HttpServletRequest request, Object bean) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Method[] methods = bean.getClass().getMethods();//返回bean对象的所有public方法数组
        for (Method method : methods) {
            String methodName = method.getName();//返回方法名
            if (methodName.startsWith("set")) {
                Class[] parameterTypes = method.getParameterTypes();//返回方法参数类型数组
                if (parameterTypes.length == 1) {
                    Class<?> fieldType = parameterTypes[0];//返回方法参数类型
                    String beanAtrr = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                    String arrValue = request.getParameter(beanAtrr);
                    Object value = stringConvertType(arrValue, fieldType);
                    if (value == null) {
                        continue;
                    }
                    method.invoke(bean, new Object[]{value});
                }
            }
        }
    }

    /**
     * 将请求参数封装成Map数据结构
     *
     * @param request
     * @param mapSeparator   指定多值分隔符
     * @param datetimeFields 指定哪些列是oracle时间列，请使用,分隔列名 不需要请传入null
     * @return
     */
    public static Map<String, Object> setFormMap(HttpServletRequest request, char mapSeparator, String datetimeFields) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Iterator<String> keyIterator = requestParameterMap.keySet().iterator();
        Iterator<String[]> parmIterator = requestParameterMap.values().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = "";
            String[] parm = parmIterator.next();
            if (parm != null) {
                if (parm.length == 1) {
                    value = parm[0];
                    if (datetimeFields != null) {
                        datetimeFields = datetimeFields + ",";
                        if (datetimeFields.indexOf(key + ",") > -1) {
                            long timestamp = FormatDate.getStringTimestamp(String.valueOf(value));
                            if (timestamp == 0) {
                                timestamp = FormatDate.getStringDate(String.valueOf(value));
                            }
                            value = new Timestamp(timestamp);
                        }
                    }
                } else {
                    value = ToolUtil.arrayToString(parm, mapSeparator);
                }
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object[]> getJsonArray(String json, String extraColumn, Map<String, Integer> columnOrderMap, String datetimeFields) {
        if (json == null || "".equals(json) || "[]".equals(json)) {
            return null;
        }
        if (columnOrderMap == null || columnOrderMap.size() == 0) {
            return null;
        }
        List<Object[]> valueList = new ArrayList<Object[]>();
        int maxColumn = columnOrderMap.size();
        json = json.replace("[{", "");
        json = json.replace("}]", "");
        json = json.replaceAll("'|\"", "");//替换单、双引号
        String[] _strs1 = json.split("\\},\\{");//分隔多组json数据
        for (String str1 : _strs1) {
            String temp = null;
            if (extraColumn != null) {
                temp = extraColumn + str1;
            } else {
                temp = str1;
            }
            Object[] values = new Object[maxColumn];
            String[] _str2 = temp.split(",");//将一组数据分隔成多列
            for (String str2 : _str2) {
                String[] _str3 = str2.split(":");//将名-值分隔
                Integer columnOrder = columnOrderMap.get(_str3[0]);
                if (columnOrder == null || columnOrder >= maxColumn) {
                    continue;
                }
                if (_str3.length == 1) {
                    values[columnOrder] = null;
                } else {
                    values[columnOrder] = _str3[1].trim();
                    if (datetimeFields != null) {
                        datetimeFields = datetimeFields + ",";
                        if (datetimeFields.indexOf(_str3[0] + ",") > -1) {
                            long timestamp = FormatDate.getStringTimestamp(_str3[1].trim());
                            if (timestamp == 0) {
                                timestamp = FormatDate.getStringDate(_str3[1].trim());
                            }
                            values[columnOrder] = new Timestamp(timestamp);
                        }
                    }
                }
            }
            valueList.add(values);
        }
        return valueList;
    }

    public static List<Object> getJsonBean(String json, String extraColumn, Class<?> tClass) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (json == null || "".equals(json) || "[]".equals(json)) {
            return null;
        }
        Field[] fields = tClass.getDeclaredFields();
        List<Object> beanList = new ArrayList<Object>();
        json = json.replace("[{", "");
        json = json.replace("}]", "");
        json = json.replaceAll("'|\"", "");//替换单、双引号
        String[] _strs1 = json.split("\\},\\{");//分隔多组json数据
        for (String str1 : _strs1) {
            String temp = null;
            if (extraColumn != null) {
                temp = extraColumn + str1;
            } else {
                temp = str1;
            }
            Object bean = tClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
            String[] _str2 = temp.split(",");//将一组数据分隔成多列
            for (String str2 : _str2) {
                String[] _str3 = str2.split(":");//将名-值分隔
                String object = null;
                if (_str3.length == 2) {
                    object = _str3[1];
                }
                Object value = null;
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (_str3[0].equals(fieldName)) {
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Class<?> fieldType = field.getType();
                        value = stringConvertType(object, fieldType);
                        if (value == null) {
                            continue;
                        }
                        Method method = tClass.getMethod(methodName, new Class[]{fieldType});
                        method.invoke(bean, new Object[]{value});
                        break;
                    }
                }
            }
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 根据指定映射关系进行bean复制
     *
     * @param fieldMap
     * @param sourceBean
     * @param targetBean
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void mapCopyBean(Map<String, String> fieldMap, Object sourceBean, Object targetBean) throws InvocationTargetException, IllegalAccessException {
        Method[] sourceMethods = sourceBean.getClass().getMethods();
        Method[] targetMethods = targetBean.getClass().getMethods();
        String sourceMethodName = null;
        String targetMethodName = null;
        for (Method targetMethod : targetMethods) {
            targetMethodName = targetMethod.getName();
            if (!targetMethodName.startsWith("set")) {
                continue;
            }
            Class<?>[] targetParameterTypes = targetMethod.getParameterTypes();
            if (targetParameterTypes.length != 1) {
                continue;
            }
            String targetAtrr = targetMethodName.substring(3);
            for (Method sourceMethod : sourceMethods) {
                sourceMethodName = sourceMethod.getName();
                if (!sourceMethodName.startsWith("get")) {
                    continue;
                }
                String sourceAtrr = sourceMethodName.substring(3);
                String mapAtrr = fieldMap.get(sourceAtrr.toLowerCase());
                if (mapAtrr == null) {
                    continue;
                }
                if (mapAtrr.equals(targetAtrr.toLowerCase())) {
                    Object sourceValue = sourceMethod.invoke(sourceBean);
                    Class<?> sourceReturnType = sourceMethod.getReturnType();
                    Class<?> targetParameterType = targetParameterTypes[0];
                    if (!targetParameterType.equals(sourceReturnType)) {
                        sourceValue = stringConvertType(sourceValue, targetParameterType);
                    }
                    if (sourceValue == null) {
                        continue;
                    }
                    targetMethod.invoke(targetBean, new Object[]{sourceValue});
                    break;
                }
            }
        }
    }

    /**
     * 根据bean的相同字段进行复制
     *
     * @param sourceBean
     * @param targetBean
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void copyBean(Object sourceBean, Object targetBean) throws InvocationTargetException, IllegalAccessException {
        Method[] sourceMethods = sourceBean.getClass().getMethods();
        Method[] targetMethods = targetBean.getClass().getMethods();
        String sourceMethodName = null;
        String targetMethodName = null;
        for (Method targetMethod : targetMethods) {
            targetMethodName = targetMethod.getName();
            if (!targetMethodName.startsWith("set")) {
                continue;
            }
            Class<?>[] targetParameterTypes = targetMethod.getParameterTypes();
            if (targetParameterTypes.length != 1) {
                continue;
            }
            String targetAtrr = targetMethodName.substring(3);
            for (Method sourceMethod : sourceMethods) {
                sourceMethodName = sourceMethod.getName();
                if (!sourceMethodName.startsWith("get")) {
                    continue;
                }
                String sourceAtrr = sourceMethodName.substring(3);
                if (sourceAtrr.equals(targetAtrr)) {
                    Object sourceValue = sourceMethod.invoke(sourceBean);
                    Class<?> sourceReturnType = sourceMethod.getReturnType();
                    Class<?> targetParameterType = targetParameterTypes[0];
                    if (!targetParameterType.equals(sourceReturnType)) {
                        sourceValue = stringConvertType(sourceValue, targetParameterType);
                    }
                    if (sourceValue == null) {
                        continue;
                    }
                    targetMethod.invoke(targetBean, new Object[]{sourceValue});
                    break;
                }
            }
        }
    }

    public static Object stringConvertType(Object sourceValue, Class<?> targetType) {
        if (sourceValue == null) {
            return null;
        }
        Object targetValue = null;
        if (targetType.equals(String.class)) {
            targetValue = String.valueOf(sourceValue);
        } else {
            String temp = "0";
            if (!"".equals(sourceValue)) {
                temp = String.valueOf(sourceValue);
            }
            if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
                targetValue = Integer.valueOf(temp);
            } else if (targetType.equals(long.class) || targetType.equals(Long.class)) {
                targetValue = Long.valueOf(temp);
            } else if (targetType.equals(float.class) || targetType.equals(Float.class)) {
                targetValue = Float.valueOf(temp);
            } else if (targetType.equals(double.class) || targetType.equals(Double.class)) {
                targetValue = Double.valueOf(temp);
            } else if (targetType.equals(java.sql.Date.class)) {
                targetValue = ToolUtil.stringToSqlDate(temp);
            } else if (targetType.equals(Timestamp.class)) {
                long timestamp = FormatDate.getStringTimestamp(temp);
                if (timestamp == 0) {
                    timestamp = FormatDate.getStringDate(temp);
                }
                targetValue = new Timestamp(timestamp);
            } else if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
                targetValue = Boolean.valueOf(temp);
            }
        }
        return targetValue;
    }

    public static Object[] sqlBeanToArray(String sql, Object bean) throws InvocationTargetException, IllegalAccessException {
        String[] sqlParams = updateSqlParamsToArray(sql);
        if (sqlParams == null) {
            return null;
        }
        Method[] methods = bean.getClass().getMethods();
        String methodName = null;
        int len = sqlParams.length;
        Object[] objects = new Object[len];
        for (int i = 0; i < len; i++) {
            String param = sqlParams[i];
            if (param == null || "".equals(param)) {
                continue;
            }
            for (Method method : methods) {
                methodName = method.getName();
                if (!methodName.startsWith("get")) {
                    continue;
                }
                String atrr = methodName.substring(3);
                if (atrr.toLowerCase().equals(param.toLowerCase())) {
                    objects[i] = method.invoke(bean);
                    break;
                }
            }
        }
        return objects;
    }

    public static String[] updateSqlParamsToArray(String sql) {
        if (sql == null || "".equals(sql)) {
            return null;
        }
        sql = sql.trim();
        Pattern pattern = null;
        String regex = null;
        if (sql.startsWith("insert")) {
            regex = "\\((\\w+[,\\w+]*)\\)";//matcher.group(1) name,sex
        } else if (sql.startsWith("update") || sql.startsWith("delete")) {
            regex = "[\\p{Blank}+|,](\\w+)=\\?";//matcher.group(1)
        }
        StringBuilder sb = new StringBuilder("");
        pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            sb.append(matcher.group(1)).append(",");
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString().split(",");
    }
}
