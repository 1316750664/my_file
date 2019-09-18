package com.util.tools;

import com.bean.AyncCacheBean;
import com.bean.JsonCommandBean;
import com.ctd.util.database.MemCachedUtil;
import com.ctd.util.database.TaoDbConnection;
import com.util.thread.CacheSaveThread;
import com.util.thread.ThreadPoolExecutorUtil;

import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hzm on 2014/7/14.
 */
public class JsonDataTools {
    private TaoDbConnection writeTaoDbConnection = null;
    private TaoDbConnection readTaoDbConnection = null;

    public JsonDataTools() {

    }

    //先做更新后做查询，更新后需要更新缓存，查询时先从缓存中取再从数据库取
    //定义一级数据两个缓存，一个为缓存存放着<taskId,同taskId但不同参数的Md5(sql)>，主要用于后台更新数据时可以同步到前台缓存
    //另一个缓存存放着<Md5(sql),sql返回的json列数据>，主要用于存放实际前台数据
    public String execute(List<JsonCommandBean> queryTaskBeanList, List<JsonCommandBean> updateTaskBeanList) throws SQLException {
        StringBuilder sb = new StringBuilder("[");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
        String cacheIds = propertiesUtil.readValue("cacheUpdateKey", "cache");
        MemCachedUtil memCachedUtil = MemCachedUtil.getInstance();
        List<AyncCacheBean> cacheBeanList = null;

        if (updateTaskBeanList != null && updateTaskBeanList.size() > 0) {
            try {
                writeTaoDbConnection = new TaoDbConnection(0);
                readTaoDbConnection = writeTaoDbConnection;
                Iterator<JsonCommandBean> iterator1 = updateTaskBeanList.iterator();
                JsonCommandBean updateBean = null;
                String ide = null;
                String sql = null;
                String taskId = null;
                String relativeTaskIds = null;
                Object memTaskId = null;
                List<String> memTaskList = null;
                Iterator<String> memIt = null;
                while (iterator1.hasNext()) {
                    updateBean = iterator1.next();
                    ide = updateBean.getIde();
                    sql = updateBean.getSql();
                    taskId = updateBean.getTaskId();
//                System.out.println("taskId:::" + taskId);
                    relativeTaskIds = propertiesUtil.readValue("cacheUpdateKey", taskId);
                    if (relativeTaskIds != null) {//更新时删除相对应的的缓存
                        for (String relativeTaskId : relativeTaskIds.split(",")) {
                            if (relativeTaskId == null || "".equals(relativeTaskId)) {
                                continue;
                            }
                            memTaskId = memCachedUtil.get(relativeTaskId);
                            if (memTaskId == null) {
                                continue;
                            }
                            memTaskList = (List<String>) memTaskId;
                            memIt = memTaskList.iterator();
                            while (memIt.hasNext()) {
                                String itstr = memIt.next();
                                memCachedUtil.delete(itstr);
                                memIt.remove();
                            }
                            memCachedUtil.delete(relativeTaskId);
                        }
                    }
                    if (ide == null || "".equals(ide)) {
                        stmt = writeTaoDbConnection.getStmt(sql);
                    } else {
                        stmt = writeTaoDbConnection.getStmt(sql, new String[]{ide});
                    }
                    writeTaoDbConnection.setAutoCommit(false);
//                stmt.executeUpdate();
//                sb.append("{").append("\"").append("taskId").append("\"").append(":").append("\"").append(updateBean.getTaskId()).append("\"");
//                if (ide != null && !"".equals(ide)) {
//                    rs = stmt.getGeneratedKeys();
//                    if (rs.next()) {
//                        sb.append(",").append("\"").append(ide).append("\"").append(":").append("\"").append(rs.getLong(1)).append("\"");
//                    }
//                    rs.close();
//                }
//                sb.append("},");
                    stmt.addBatch();
                    int[] executeResult = stmt.executeBatch();
                    sb.append("{").append("\"").append("taskId").append("\"").append(":").append("\"").append(taskId).append("\"");
                    if (ide != null && !"".equals(ide)) {
                        rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            sb.append(",").append("\"").append(ide).append("\"").append(":").append("\"").append(rs.getLong(1)).append("\"");
                        }
                        rs.close();
                    } else {
                        sb.append(",").append("\"").append("result").append("\"").append(":").append("\"").append(executeResult[0]).append("\"");
                    }
                    sb.append("},");
                    stmt.clearBatch();
                }
            } catch (SQLException e) {
                writeTaoDbConnection.rollback();
                throw e;
            } finally {
                try {
                    if (writeTaoDbConnection != null) {
                        writeTaoDbConnection.setAutoCommit(true);
                        writeTaoDbConnection.closeConn();
                    }
                } catch (SQLException e) {
//                    e.printStackTrace();
                }
            }
        }
        if (queryTaskBeanList != null && queryTaskBeanList.size() > 0) {
            if (readTaoDbConnection == null) {
                readTaoDbConnection = new TaoDbConnection(1);
            }

            cacheBeanList = new ArrayList<AyncCacheBean>();

            boolean is_cached = true;
            Iterator<JsonCommandBean> iterator2 = queryTaskBeanList.iterator();
            JsonCommandBean queryBean = null;
            String taskId = null;
            String sql = null;
            String sqlMd5 = null;
            Object memData = null;
            while (iterator2.hasNext()) {
                queryBean = iterator2.next();
                taskId = queryBean.getTaskId();
                sql = queryBean.getSql();
                sqlMd5 = FingerUtil.md5(sql);
                if (taskId.equals(cacheIds) || cacheIds.indexOf("," + taskId + ",") > -1 || taskId.equals(cacheIds.substring(cacheIds.lastIndexOf(",") + 1))) {//此时是可缓存的数据
                    memData = memCachedUtil.get(sqlMd5);
                    if (memData != null) {
                        sb.append("{").append("\"").append("taskId").append("\"").append(":").append("\"").append(queryBean.getTaskId()).append("\"");
                        sb.append(",").append("\"").append("rows").append("\"").append(":").append(memData.toString());
                        sb.append("},");
                        continue;
                    } else {//找不到缓存
                        is_cached = false;
//                        System.out.println("no cached");
                    }
                }

                stmt = readTaoDbConnection.getStmt(sql);
                rs = stmt.executeQuery();
                String queryResult = getRsJson(rs);
                sb.append("{").append("\"").append("taskId").append("\"").append(":").append("\"").append(queryBean.getTaskId()).append("\"");
                sb.append(",").append("\"").append("rows").append("\"").append(":").append(queryResult);
                sb.append("},");
                rs.close();

                if (!is_cached) {//找不到缓存，需要进行缓存处理
//                    System.out.println("cached...");
                    cacheBeanList.add(new AyncCacheBean(taskId, sqlMd5, queryResult));
//                    memCachedUtil.add(sqlMd5, queryResult);//缓存查询结果
//                    Object memTaskIdObj = memCachedUtil.get(taskId);//准备缓存taskId对应的sqlMd5
//                    if (memTaskIdObj == null) {
////                        System.out.println("memTaskIdObj null");
//                        List<String> temList = new ArrayList<String>();
//                        temList.add(sqlMd5);
//                        memCachedUtil.add(taskId, temList);
//                    } else {
//                        boolean is_contain = false;
////                        System.out.println("memTaskIdObj:"+memTaskIdObj);
//                        List<String> memTaskList = (List<String>) memTaskIdObj;
//                        Iterator<String> memIt = memTaskList.iterator();
//                        while (memIt.hasNext()) {
//                            String itstr = memIt.next();
//                            if (itstr.equals(sqlMd5)) {
//                                is_contain = true;
//                                break;
//                            }
//                        }
//                        if (!is_contain) {
//                            memTaskList.add(sqlMd5);
//                        }
//                    }
                }
                is_cached = true;
            }
        }
        int sbLen = sb.length();
        if (sb.lastIndexOf(",") == sbLen - 1) {
            sb.delete(sbLen - 1, sbLen);
        }
        sb.append("]");

        if (writeTaoDbConnection != null) {
            writeTaoDbConnection.closeConn();
        }
        if (readTaoDbConnection != null) {
            readTaoDbConnection.closeConn();
        }

        String returnResult = sb.toString();
        sb.delete(0, sb.length());

        if (cacheBeanList != null && cacheBeanList.size() > 0) {//启动线程保存缓存数据
            ThreadPoolExecutorUtil.getInstance().addTask(new CacheSaveThread(cacheBeanList));
        }

        return returnResult;
    }

    private String getRsJson(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder("[");
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        while (rs.next()) {
            sb.append("{");
            for (int i = 1; i <= cols; i++) {
                String s_temp = "";
                String columnName = rsmd.getColumnLabel(i).toLowerCase();
                Object value = rs.getObject(i);
                if (value != null) {
                    s_temp = value.toString().trim();
                    if (value instanceof Timestamp) {
                        s_temp = FormatDate.getTimestampString(Timestamp.valueOf(s_temp));
                    } else if (value instanceof Date) {
                        s_temp = FormatDate.getDateString(Date.valueOf(s_temp));
                    } else if (value instanceof Time) {
                        s_temp = FormatDate.getTimeString(Time.valueOf(s_temp));
                    } else if (value instanceof NClob) {
                        NClob nClob = (NClob) value;
                        if (nClob.length() != 0) {
                            s_temp = nClob.getSubString(1L, (int) nClob.length());
                        }
                    }
                }
                s_temp = (s_temp == null || "null".equals(s_temp)) ? " " : s_temp.replace("\"", "\\\"");
                sb.append("\"").append(columnName).append("\"").append(":").append("\"").append(s_temp).append("\"");
                if (i < cols) {
                    sb.append(",");
                }
            }
            sb.append("},");
        }

        int sbLen = sb.length();
        if (sb.lastIndexOf(",") == sbLen - 1) {
            sb.delete(sbLen - 1, sbLen);
        }
        sb.append("]");

        String returnResult = sb.toString();
        sb.delete(0, sb.length());

        return returnResult;
    }
}
