package com.ctd.util.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hzm on 2014/11/10.
 */
public abstract class AbstractDbPool {
    public static Map<String, DataSource> druidDataSources = null;
    public static Map<String, DataSource> dbcpDataSources = null;

    /**
     * 此方法不可调用，必须通过其子类进行调用
     * 0 druid 1 dbcp
     * 0 返回读写数据源，1 返回只读数据源 2返回备份数据源
     *
     * @return
     */
    public final DataSource getDataSource(int dsType, int sourceType) {
        DataSource dataSource = null;
        switch (sourceType) {
            case 0:
                dataSource = getMasterDataSource(dsType);
                break;
            case 1:
                dataSource = getSlaveDataSource(dsType);
                break;
        }

        return dataSource;
    }

    public final static void closeDataSource() {
        if (druidDataSources != null) {
            Iterator<Map.Entry<String, DataSource>> iterator = druidDataSources.entrySet().iterator();
            Map.Entry<String, DataSource> entry = null;
            DruidDataSource dataSource = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                dataSource = (DruidDataSource) entry.getValue();
                if (dataSource == null || !dataSource.isEnable()) {
                    continue;
                }
                dataSource.close();
            }
        }
        if (dbcpDataSources == null) {
            return;
        }
        try {
            Iterator<Map.Entry<String, DataSource>> iterator = dbcpDataSources.entrySet().iterator();
            Map.Entry<String, DataSource> entry = null;
            BasicDataSource dataSource = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                dataSource = (BasicDataSource) entry.getValue();
                if (dataSource == null || !dataSource.isClosed()) {
                    continue;
                }
                dataSource.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DataSource getMasterDataSource(int dsType) {
        String poolId = DSPoolFactory.getInstance().getMasterPoolId();
        DataSource dataSource = null;
        switch (dsType) {
            case 0: {
                dataSource = druidDataSources.get(poolId);
                break;
            }
            case 1: {
                dataSource = dbcpDataSources.get(poolId);
                break;
            }
            default: {
                break;
            }
        }
        return dataSource;
    }

    private DataSource getSlaveDataSource(int dsType) {
        String poolId = DSPoolFactory.getInstance().getSlavePoolId();
        DataSource dataSource = null;
        switch (dsType) {
            case 0: {
                dataSource = druidDataSources.get(poolId);
                break;
            }
            case 1: {
                dataSource = dbcpDataSources.get(poolId);
                break;
            }
            default: {
                break;
            }
        }
        return dataSource;
    }
}
