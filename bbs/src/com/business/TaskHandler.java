package com.business;

import com.bean.TaskBean;
import com.ctd.util.database.dao.DBQuery;
import com.ctd.util.database.dao.DBUpdate;
import com.util.tools.SqlXmlRead;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by hzm on 2014/9/19.
 */
public class TaskHandler {
    private static class SingletonTaskHandler {
        private static TaskHandler INSTANCE = new TaskHandler();
    }

    public static TaskHandler getInstance() {
        return SingletonTaskHandler.INSTANCE;
    }

    private TaskHandler() {

    }

    /**
     * sql配置在sqlconfig.xml中，参数使用?
     *
     * @param dbType
     * @param taskBean
     * @throws UnsupportedEncodingException
     */
    public String handler(int dbType, TaskBean taskBean) throws UnsupportedEncodingException, SQLException {
        String result = null;
        int taskId = taskBean.getTaskId();
        String sql = SqlXmlRead.getSQL(taskId);
        switch (taskBean.getCmdType()) {
            case 0: {
                result = DBQuery.getInstance()._GetSingleSqlResult(dbType, taskId, sql, taskBean.getMethod(), taskBean.getParamValues(), taskBean.getParamTypes());
                break;
            }
            case 1: {
                result = DBQuery.getInstance()._GetSingleSqlResult(dbType, taskId, sql, taskBean.getMethod(), taskBean.getParamValues(), taskBean.getParamTypes());
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                result = DBQuery.getInstance()._GetPageSqlResult(dbType, taskId, sql, taskBean.getMethod(), taskBean.getParamValues(), taskBean.getParamTypes());
                break;
            }
        }

        return result;
    }

    /**
     * 只限于批处理更新、删除、插入
     *
     * @param dbType
     * @param taskBeanList
     * @return
     * @throws UnsupportedEncodingException
     */
    public String handler(int dbType, List<TaskBean> taskBeanList) throws UnsupportedEncodingException, SQLException {
        return DBUpdate.getInstance()._GetMultiBatch(dbType, taskBeanList);
    }
}
