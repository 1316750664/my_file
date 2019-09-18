package com.ctd.util.database;

import com.util.tools.ReadWriteProperties;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * User: huhoo
 * Date: 14-4-30
 * Time: 上午10:28
 */
public class DbcpPool extends AbstractDbPool {
    static {
        DSPoolFactory dsPoolFactory = DSPoolFactory.getInstance();
        List<String> poolIdList = dsPoolFactory.getAllSlavePoolId();
        dbcpDataSources = new HashMap<String, DataSource>(poolIdList.size() + 1);
        ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
        Properties properties = null;
        DataSource dataSource = null;
        //添加主库
        String masterId = dsPoolFactory.getMasterPoolId();
        properties = propertiesUtil.getProperties("datasource" + masterId + ".properties");
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
            dbcpDataSources.put(masterId, dataSource);
        } catch (Exception e) {
            System.out.println("主数据源" + masterId + "初始化失败");
        }
        //添加所有从库
        Iterator<String> iterator = poolIdList.iterator();
        while (iterator.hasNext()) {
            String slaveId = iterator.next();
            properties = propertiesUtil.getProperties("datasource" + slaveId + ".properties");
            try {
                dataSource = BasicDataSourceFactory.createDataSource(properties);
            } catch (Exception e) {
                System.out.println("从数据源" + slaveId + "初始化失败");
                continue;
            }
            dbcpDataSources.put(slaveId, dataSource);
        }
    }

    private static class SingletonDbcpPool {
        private static DbcpPool INSTANCE = new DbcpPool();
    }

    public static DbcpPool getInstance() {
        return SingletonDbcpPool.INSTANCE;
    }

    private DbcpPool() {
    }
}
