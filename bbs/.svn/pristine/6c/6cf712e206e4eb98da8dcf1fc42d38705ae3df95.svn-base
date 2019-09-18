package com.ctd.util.database.dao;

import com.bean.TaskBean;
import com.ctd.util.database.TaoDbConnection;
import com.util.tools.CheckTools;
import com.util.tools.SqlXmlRead;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hzm on 2014/9/19.
 */
public class DBUpdate {

    private static class SingletonDBUpdate {
        private static DBUpdate INSTANCE = new DBUpdate();
    }

    public static DBUpdate getInstance() {
        return SingletonDBUpdate.INSTANCE;
    }

    private DBUpdate() {

    }

    /**
     * @param dbType       0 mysql 1 oracle 2 mongodb
     * @param taskBeanList
     * @return
     */
    public String _GetMultiBatch(int dbType, List<TaskBean> taskBeanList) throws UnsupportedEncodingException, SQLException {
        String result = "[]";
        if (taskBeanList == null || taskBeanList.size() == 0) {
            return result;
        }
        switch (dbType) {
            case 0: {
                result = multiBatch(taskBeanList);
                break;
            }
            default:
                break;
        }
        return result;
    }

    public String multiBatch(List<TaskBean> taskBeanList) throws UnsupportedEncodingException, SQLException {
        TaoDbConnection writeTaoDbConnection = null;
        String result = "[]";
        StringBuilder sb = new StringBuilder("");
        int success = -1;//-4 sql参数或语句不正确 -3 语句执行出错 -2 驱动不支持返回值
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            ResultSetMetaData rsmd = null;
            writeTaoDbConnection = new TaoDbConnection(0);
            TaskBean taskBean = null;
            Iterator<TaskBean> iterator0 = taskBeanList.iterator();
            int taskId;
            String sql = null;
            int method = 0;
            String[] paramValues = null;
            String[] paramTypes = null;
            String[] keyColumns = null;
            String paramValue = null;
            String paramType = null;
            int i = 0, j = 0, k = 0;
            int rows = 0;
            while (iterator0.hasNext()) {
                taskBean = iterator0.next();
                taskId = taskBean.getTaskId();
                sql = SqlXmlRead.getSQL(taskId);
                if (sql == null || "".equals(sql)) {
                    success = -4;
                    break;
                }
                method = taskBean.getMethod();
                paramValues = taskBean.getParamValues();
                paramTypes = taskBean.getParamTypes();
                keyColumns = taskBean.getKeyColumns();
                if (method == 1 && paramValues != null) {
                    if (paramTypes == null) {
                        success = -4;
                        throw new RuntimeException("参数类型不允许为空");
                    }
                    if (paramValues.length != paramTypes.length) {
                        success = -4;
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
                                    success = -4;
                                    throw new RuntimeException("非法数值参数");
                                }
                            }
                        } else if ("object".equals(paramType)) {
                            //paramValue = paramValue;
                        } else {
                            success = -4;
                            throw new RuntimeException("非法参数类型");
                        }
                        sql = sql.replaceFirst("@\\w+@", paramValue);
                    }
                }
                if (keyColumns != null && keyColumns.length != 0) {
                    stmt = writeTaoDbConnection.getStmt(sql);
                } else {
                    stmt = writeTaoDbConnection.getStmt(sql, keyColumns);
                }
                writeTaoDbConnection.setAutoCommit(false);
                if (method == 0 && paramValues != null) {
                    int sqlParamNum = sql.split("\\?").length;
                    if (!"?".equals(sql.substring(sql.length() - 1))) {
                        sqlParamNum = sqlParamNum - 1;
                    }
                    int paramLen = paramValues.length;
                    if (paramLen < sqlParamNum) {//传送来的值的个数不能小于?的个数
                        success = -4;
                        throw new SQLException("缺失SQL参数");
                    }
                    if (paramTypes == null) {
                        for (i = 0; i < sqlParamNum; i++) {
                            stmt.setObject(i + 1, paramValues[i]);
                        }
                    } else {
                        int len = Math.min(sqlParamNum, paramTypes.length);
                        j = 0;
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
                stmt.addBatch();
                int[] executeResult = stmt.executeBatch();
                sb.append("{").append("\"").append("taskId").append("\"").append(":").append("\"").append(taskId).append("\"");
                k = 0;
                if (keyColumns != null && keyColumns.length != 0) {
                    rs = stmt.getGeneratedKeys();
                    rsmd = rs.getMetaData();
                    while (rs.next()) {
                        sb.append(",").append("\"").append(rsmd.getColumnName(k++)).append("\"").append(":").append("\"").append(rs.getLong(1)).append("\"");
                    }
                    rs.close();
                } else {
                    if (executeResult == null || executeResult[0] == -2) {
                        success = -2;//防止数据库驱动不支持返回值的情况
                        rows = 0;
                    } else {
                        rows = executeResult[0];
                    }
                    sb.append(",").append("\"").append("result").append("\"").append(":").append("\"").append(rows).append("\"");
                }
                sb.append("},");
                stmt.clearBatch();
                iterator0.remove();
            }
            writeTaoDbConnection.commit();
        } catch (SQLException e) {
            try {
                if (writeTaoDbConnection != null) {
                    writeTaoDbConnection.rollback();
                }
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }
            success = -3;
            throw e;
        } finally {
//            System.out.println("success:" + success);
            if (success < -2) {
                result = "[{\"error\":\"" + success + "\"}]";
            } else {
                int sbLen = sb.length();
                if (sbLen > 0 && (",").equals(sb.substring(sbLen - 1))) {
                    result = sb.substring(0, sbLen - 1);
                } else {
                    result = sb.substring(0, sbLen);
                }
                sb.delete(0, sbLen);
                result = "[" + result + "]";
            }
            try {
                if (writeTaoDbConnection != null) {
                    writeTaoDbConnection.setAutoCommit(true);
                    writeTaoDbConnection.closeConn();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }

        return result;
    }
}
