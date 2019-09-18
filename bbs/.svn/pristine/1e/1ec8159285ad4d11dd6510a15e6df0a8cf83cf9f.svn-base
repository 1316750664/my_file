package com.ctd.util.database;

/**
 * Created by hzm on 2014/7/11.
 */
public class TaoDbConnection extends AbstractDBConnection {
    static {
        dbPool = TaoDbPool.getInstance();
    }

    /**
     * 0 返回读写数据源，1 返回只读数据源 2返回备份数据源
     *
     * @param sourceType
     */
    public TaoDbConnection(int sourceType) {
        super(0, sourceType);
    }
}
