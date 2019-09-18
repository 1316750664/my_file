package com.ctd.util.database.dao;

import com.bean.AyncCacheBean;
import com.ctd.util.database.MemCachedUtil;
import com.ctd.util.database.TaoDbConnection;
import com.util.thread.CacheSaveThread;
import com.util.thread.ThreadPoolExecutorUtil;
import com.util.tools.CheckTools;
import com.util.tools.FingerUtil;
import com.util.tools.FormatDate;
import com.util.tools.ReadWriteProperties;
import com.util.tools.ToolUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzm on 2014/9/19.
 */
public class DBQuery {
    private static Pattern pattern1 = Pattern.compile("((\\s+order\\s+by\\s+)|(\\s+limit\\s+))");

    private static class SingletonDBQuery {
        private static DBQuery INSTANCE = new DBQuery();
    }

    public static DBQuery getInstance() {
        return SingletonDBQuery.INSTANCE;
    }

    private DBQuery() {

    }

    /**
     * @param dbType      0 mysql 1 oracle 2 mongodb 10 mysql主 11 oracle主 12 mongodb主
     * @param taskId
     * @param sql
     * @param method
     * @param paramValues
     * @param paramTypes
     * @return
     * @throws SQLException
     */
    public String _GetSingleSqlResult(int dbType, int taskId, String sql, int method, String[] paramValues, String[] paramTypes) throws SQLException {
        sql = sql.trim();
        String result = null;
        result = _GetMemCacheResult(taskId, sql, paramValues);
        boolean is_cache = true;
        if (result != null && !"0".equals(result)) {
            return result;
        }
        if (result == null) {//此时不需要缓存数据
            is_cache = false;
        }
        switch (dbType) {
            case 0: {
                result = _GetTaoSingleSqlResult(null, sql, method, paramValues, paramTypes);
                break;
            }
            case 10: {
                result = _GetTaoSingleSqlResult(new TaoDbConnection(0), sql, method, paramValues, paramTypes);
                break;
            }
            default: {
                break;
            }
        }
        if (is_cache && result != null) {//启动线程保存缓存数据
            String sqlMd5 = FingerUtil.md5(sql + "," + ToolUtil.arrayToString(paramValues, ','));
            List<AyncCacheBean> cacheBeanList = new ArrayList<AyncCacheBean>();
            cacheBeanList.add(new AyncCacheBean(String.valueOf(taskId), sqlMd5, result));
            ThreadPoolExecutorUtil.getInstance().addTask(new CacheSaveThread(cacheBeanList));
        }
        return result;
    }

    private String _GetMemCacheResult(int taskId, String sql, String[] paramValues) {
        String result = null;
        String cacheIds = ReadWriteProperties.getInstance().readValue("cacheUpdateKey", "cache");
        String taskIds = String.valueOf(taskId);
        if (taskIds.equals(cacheIds) || cacheIds.indexOf("," + taskId + ",") > -1 || taskIds.equals(cacheIds.substring(cacheIds.lastIndexOf(",") + 1))) {//此时是可缓存的数据
            MemCachedUtil memCachedUtil = MemCachedUtil.getInstance();
            String sqlMd5 = FingerUtil.md5(sql + "," + ToolUtil.arrayToString(paramValues, ','));
            Object memData = memCachedUtil.get(sqlMd5);
            if (memData != null) {
                result = memData.toString();
            } else {//找不到缓存
                result = "0";
            }
        }
        return result;
    }

    /**
     * 默认必须order by 后面的查询语句不影响查询结果
     *
     * @param taskId
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    public String _GetPageSqlResult(int dbType, int taskId, String sql, int method, String[] paramValues, String[] paramTypes) throws SQLException {
        sql = sql.trim();
        String result = null;
        result = _GetMemCacheResult(taskId, sql, paramValues);
        boolean is_cache = true;
        if (result != null && !"0".equals(result)) {
            return result;
        }
        if (result == null) {//此时不需要缓存数据
            is_cache = false;
        }
        String sqlForCount = sql.toLowerCase();
        int firstSelect = sqlForCount.indexOf("select ");
        int firstFrom = sqlForCount.indexOf(" from ");
        if (firstSelect == -1 || firstFrom == -1) {
            return "[]";
        }
        int selects = 1;
        int froms = 0;
        sqlForCount = sqlForCount.substring(firstSelect + 7);
        while (selects > froms) {
            firstSelect = sqlForCount.indexOf("select ");
            firstFrom = sqlForCount.indexOf(" from ");
            if (firstSelect > -1 && firstSelect < firstFrom) {//select在前面，说明select...from之间有子查询
                sqlForCount = sqlForCount.substring(firstSelect + 7);
                selects++;
            } else {
                sqlForCount = sqlForCount.substring(firstFrom + 6);
                froms++;
            }
        }

        int lastOrderLimit = -1;
        String lastOrder = null;
        if (dbType == 0 || dbType == 10) {
            String tempSqlForCount = sqlForCount.toLowerCase();
            Matcher matcher = pattern1.matcher(tempSqlForCount);
            if (matcher.find()) {
                lastOrder = matcher.group();
            }
            if (lastOrder != null) {
                lastOrderLimit = tempSqlForCount.lastIndexOf(lastOrder);
            }
        }
        if (lastOrderLimit > -1) {
            sqlForCount = sqlForCount.substring(0, lastOrderLimit);
        }
        sqlForCount = "select count(*) from ".concat(sqlForCount);
        int totalRecord = 0;
        TaoDbConnection readTaoDbConnection = null;
        switch (dbType) {
            case 0: {
                readTaoDbConnection = new TaoDbConnection(1);
                break;
            }
            case 10: {
                readTaoDbConnection = new TaoDbConnection(0);
                break;
            }
            default: {
                break;
            }
        }
        if (readTaoDbConnection == null && (dbType == 0 || dbType == 10)) {
            return "[]";
        }
        totalRecord = _GetTaoSqlPage(readTaoDbConnection, sqlForCount, method, paramValues, paramTypes);
        if (readTaoDbConnection.isClosed()) {
            return "[]";
        } else {
            result = _GetTaoSingleSqlResult(readTaoDbConnection, sql, method, paramValues, paramTypes);
        }

        result = "{\"itemCount\":\"" + totalRecord + "\"}".concat("#@#@#").concat(result == null ? "[]" : result);

        if (is_cache && result != null) {//启动线程保存缓存数据
            String sqlMd5 = FingerUtil.md5(sql + "," + ToolUtil.arrayToString(paramValues, ','));
            List<AyncCacheBean> cacheBeanList = new ArrayList<AyncCacheBean>();
            cacheBeanList.add(new AyncCacheBean(String.valueOf(taskId), sqlMd5, result));
            ThreadPoolExecutorUtil.getInstance().addTask(new CacheSaveThread(cacheBeanList));
        }

        return result;
    }

    private int _GetTaoSqlPage(TaoDbConnection readTaoDbConnection, String sql, int method, String[] paramValues, String[] paramTypes) throws SQLException {
        int totalRecord = 0;
        ResultSet rs = null;
        try {
            if (readTaoDbConnection == null) {
                readTaoDbConnection = new TaoDbConnection(1);
            }
            rs = returnResultSet(readTaoDbConnection, sql, method, paramValues, paramTypes);
            if (rs != null && rs.next()) {
                totalRecord = rs.getInt(1);//总记录数
            }
        } catch (SQLException e) {
            if (readTaoDbConnection != null) {//发生异常时直接关闭
                readTaoDbConnection.closeConn();
            }
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
//            此处不关闭，后面还要返回当前页码数据_GetTaoSingleSqlResult中关闭即可
//            if (readTaoDbConnection != null) {
//                readTaoDbConnection.closeConn();
//            }
        }

        return totalRecord;
    }

    public String _GetTaoSingleSqlResult(TaoDbConnection readTaoDbConnection, String sql, int method, String[] paramValues, String[] paramTypes) throws SQLException {
        String result = null;
        ResultSet rs = null;
        try {
            if (readTaoDbConnection == null) {
                readTaoDbConnection = new TaoDbConnection(1);
            }
            rs = returnResultSet(readTaoDbConnection, sql, method, paramValues, paramTypes);
            result = _GenerateJson(rs);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (readTaoDbConnection != null) {
                readTaoDbConnection.closeConn();
            }
        }

        return result;
    }

    public ResultSet returnResultSet(TaoDbConnection readTaoDbConnection, String sql, int method, String[] paramValues, String[] paramTypes) throws SQLException {
        ResultSet rs = null;
        sql = sql.trim();
        PreparedStatement stmt = null;
        String paramValue = null;
        String paramType = null;
        if (method == 0) {
            stmt = readTaoDbConnection.getStmt(sql);
            if (paramValues != null) {
                int sqlParamNum = sql.split("\\?").length;
                if (!"?".equals(sql.substring(sql.length() - 1))) {
                    sqlParamNum = sqlParamNum - 1;
                }
                int paramLen = paramValues.length;
                if (paramLen < sqlParamNum) {//传送来的值的个数不能小于?的个数
                    throw new SQLException("缺失SQL参数");
                }
                if (paramTypes == null) {
                    for (int i = 0; i < sqlParamNum; i++) {
                        stmt.setObject(i + 1, paramValues[i]);
                    }
                } else {
                    int len = Math.min(sqlParamNum, paramTypes.length);
                    int j = 0;
                    for (; j < len; j++) {
                        paramValue = paramValues[j];
                        paramType = paramTypes[j].toLowerCase();
                        if ("tinyint".equals(paramType) || "smallint".equals(paramType)) {
                            stmt.setShort(j + 1, Short.valueOf(paramValue));
                        } else if ("int".equals(paramType) || "mediumint".equals(paramType)) {
                            stmt.setInt(j + 1, Integer.valueOf(paramValue));
                        } else if ("bigint".equals(paramType) || "integer".equals(paramType)) {
                            stmt.setLong(j + 1, Long.valueOf(paramValue));
                        } else if ("float".equals(paramType)) {
                            stmt.setFloat(j + 1, Float.valueOf(paramValue));
                        } else if ("double".equals(paramType)) {
                            stmt.setDouble(j + 1, Double.valueOf(paramValue));
                        } else if ("decimal".equals(paramType) || "numeric".equals(paramType)) {
                            stmt.setBigDecimal(j + 1, new BigDecimal(paramValue));
                        } else if ("char".equals(paramType)
                                || "varchar".equals(paramType)
                                || "tinytext".equals(paramType)
                                || "text".equals(paramType)
                                || "longtext".equals(paramType)
                                || "mediumtext".equals(paramType)
                                || "nchar".equals(paramType)
                                || "nvarchar".equals(paramType)) {
                            stmt.setString(j + 1, paramValue);
                        } else if ("date".equals(paramType)) {
                            stmt.setDate(j + 1, Date.valueOf(paramValue));
                        } else if ("time".equals(paramType)) {
                            stmt.setTime(j + 1, Time.valueOf(paramValue));
                        } else if ("timestamp".equals(paramType)) {
                            stmt.setTimestamp(j + 1, Timestamp.valueOf(paramValue));
                        } else {
                            stmt.setObject(j + 1, paramValue);
                        }
                    }
                    for (; j < sqlParamNum; j++) {//防止参数值与参数类型数组大小不一致
                        stmt.setObject(j + 1, paramValues[j]);
                    }
                }
            }
        } else if (method == 1) {
            if (paramValues != null) {
                if (paramTypes == null) {
                    throw new RuntimeException("参数类型不允许为空");
                }
                if (paramValues.length != paramTypes.length) {
                    throw new RuntimeException("参数值与参数类型个数不匹配");
                }
                for (int t = 0; t < paramValues.length; t++) {
                    paramValue = paramValues[t];
                    paramType = paramTypes[t].toLowerCase();
                    if ("string".equals(paramType)) {//数据库''相连则表示'，因为'是数据库的转义符
                        if (paramValue.indexOf("''") == -1 && paramValue.indexOf("'") >= 0) {
                            paramValue = paramValue.replaceAll("'", "''");
                        }
                        paramValue = "'" + paramValue + "'";
                    } else if ("date".equals(paramType)) {
                        paramValue = "'" + paramValue + "'";
                    } else if ("number".equals(paramType)) {
                        if ("".equals(paramValue)) {
                            paramValue = "null";
                        } else {//防止SQL注入
                            if (!CheckTools.checkIsPoint(paramValue)) {
                                throw new RuntimeException("非法数值参数");
                            }
                        }
                    } else if ("object".equals(paramType)) {
                        //paramValue = paramValue;
                    } else {
                        throw new RuntimeException("非法参数类型");
                    }
                    sql = sql.replaceFirst("@\\w+@", paramValue);
                }
            }
            stmt = readTaoDbConnection.getStmt(sql);
        }
        if (stmt == null) {
            throw new RuntimeException("抱歉参数不合法");
        }
        rs = stmt.executeQuery();

        return rs;
    }

    private String _GenerateJson(ResultSet rs) throws SQLException {
        if (rs == null) {
            return "[]";
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        if (cols == 0) {
            return "[]";
        }
        String[] colNameArr = new String[cols];
        String[] colTypeArr = new String[cols];
        for (int i = 1; i <= cols; i++) {
            colNameArr[i - 1] = rsmd.getColumnLabel(i).toLowerCase();
            colTypeArr[i - 1] = rsmd.getColumnTypeName(i).toLowerCase();
        }
        Object value = null;
        NClob nClob = null;
        String colValue = null;
        StringBuilder sb = new StringBuilder("[");
        while (rs.next()) {
            sb.append("{");
            for (int j = 1; j <= cols; j++) {
                colValue = "";
                value = rs.getObject(j);
                if (value != null) {
                    colValue = value.toString().trim();
                    if (value instanceof Timestamp) {
                        colValue = FormatDate.getTimestampString(Timestamp.valueOf(colValue));
                    } else if (value instanceof Date) {
                        colValue = FormatDate.getDateString(Date.valueOf(colValue));
                    } else if (value instanceof Time) {
                        colValue = FormatDate.getTimeString(Time.valueOf(colValue));
                    } else if (value instanceof NClob) {
                        nClob = (NClob) value;
                        if (nClob.length() != 0) {
                            colValue = nClob.getSubString(1L, (int) nClob.length());
                        }
                    }
                }
                colValue = (colValue == null || "null".equals(colValue)) ? " " : colValue.replace("\"", "\\\"");
                sb.append("\"").append(colNameArr[j - 1]).append("\"").append(":").append("\"").append(colValue).append("\"").append(",");
//				System.out.println(sb.toString());
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append("}").append(",");
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]");

        String temp = sb.toString();
        sb.delete(0, sb.length());

        return temp;
    }
}