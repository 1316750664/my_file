package com.ctd.util.database;

/**
 * Created by hzm on 2014/11/27.
 */
public class DbcpDbConnection extends AbstractDBConnection {
    static {
        dbPool = DbcpPool.getInstance();
    }

    /**
     * 0 返回读写数据源，1 返回只读数据源 2返回备份数据源
     *
     * @param sourceType
     */
    public DbcpDbConnection(int sourceType) {
        super(1, sourceType);
    }
}
