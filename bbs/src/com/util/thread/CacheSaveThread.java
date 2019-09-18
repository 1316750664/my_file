package com.util.thread;

import com.bean.AyncCacheBean;
import com.ctd.util.database.MemCachedUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hzm on 2014/7/28.
 */
public class CacheSaveThread implements Runnable {
    private List<AyncCacheBean> cacheBeanList;

    public CacheSaveThread(List<AyncCacheBean> cacheBeanList) {
        this.cacheBeanList = cacheBeanList;
    }

    @Override
    public void run() {
        if (cacheBeanList == null || cacheBeanList.size() == 0) {
            return;
        }
//        System.out.println("cache...:"+System.currentTimeMillis());
        MemCachedUtil memCachedUtil = MemCachedUtil.getInstance();
        Iterator<AyncCacheBean> iterator = cacheBeanList.iterator();
        String taskId = null;
        String sqlMd5 = null;
        String queryResult = null;
        AyncCacheBean bean = null;
        boolean is_contain = false;
        String memTasks = null;
        while (iterator.hasNext()) {
            bean = iterator.next();
            taskId = bean.getTaskId();
            sqlMd5 = bean.getSqlKey();
            queryResult = bean.getSqlData();

            memCachedUtil.add(sqlMd5, queryResult);//缓存查询结果
            Object memTaskIdObj = memCachedUtil.get(taskId);//准备缓存taskId对应的sqlMd5
            if (memTaskIdObj == null) {
//                List<String> temList = new ArrayList<String>();
//                temList.add(sqlMd5);
                memCachedUtil.add(taskId, sqlMd5);
            } else {
                is_contain = false;
//                List<String> memTaskList = (List<String>) memTaskIdObj;
//                Iterator<String> memIt = memTaskList.iterator();
//                while (memIt.hasNext()) {
//                    String itstr = memIt.next();
//                    if (itstr.equals(sqlMd5)) {
//                        is_contain = true;
//                        break;
//                    }
//                }
//                if (!is_contain) {
//                    memTaskList.add(sqlMd5);
//                }
                memTasks = memTaskIdObj.toString();
                for (String memTask : memTasks.split(",")) {
                    if (memTask.equals(sqlMd5)) {
                        is_contain = true;
                        break;
                    }
                }
                if (!is_contain) {
                    memCachedUtil.replace(taskId, memTasks + "," + sqlMd5);
                }
            }

            iterator.remove();
        }
//        System.out.println("cached...:"+System.currentTimeMillis());
    }
}
