package com.ctd.util.database;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.util.tools.ReadWriteProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by hzm on 2014/7/5.
 */
public class TaoDbPool extends AbstractDbPool {
    static {
        DSPoolFactory dsPoolFactory = DSPoolFactory.getInstance();
        List<String> poolIdList = dsPoolFactory.getAllSlavePoolId();
        druidDataSources = new HashMap<String, DataSource>(poolIdList.size() + 1);
        ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
        Properties properties = null;
        DataSource dataSource = null;
        //添加主库
        String masterId = dsPoolFactory.getMasterPoolId();
        properties = propertiesUtil.getProperties("datasource" + masterId + ".properties");
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
            druidDataSources.put(masterId, dataSource);
        } catch (Exception e) {
            System.out.println("主数据源" + masterId + "初始化失败");
        }
        //添加所有从库
        Iterator<String> iterator = poolIdList.iterator();
        while (iterator.hasNext()) {
            String slaveId = iterator.next();
            properties = propertiesUtil.getProperties("datasource" + slaveId + ".properties");
            try {
                dataSource = DruidDataSourceFactory.createDataSource(properties);
            } catch (Exception e) {
                System.out.println("从数据源" + slaveId + "初始化失败");
                continue;
            }
            druidDataSources.put(slaveId, dataSource);
        }
    }

    private static class SingletonTaoDbPool {
        private static TaoDbPool INSTANCE = new TaoDbPool();
    }

    public static TaoDbPool getInstance() {
        return SingletonTaoDbPool.INSTANCE;
    }

    private TaoDbPool() {
    }
}
