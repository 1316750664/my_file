package com.ctd.util.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 声明为抽象类，防止被实例化
 * Created by hzm on 2014/11/10.
 */
public abstract class AbstractDBConnection {
    private Connection conn = null;
    private CallableStatement callStmt = null;
    private PreparedStatement preStmt = null;
    private Statement stmt = null;

    private int dsType;//0 druid 1 dbcp
    private int sourceType;//0 返回读写数据源，1 返回只读数据源 2返回备份数据源

    public static AbstractDbPool dbPool = null;

    public AbstractDBConnection(int dsType, int sourceType) {
        this.dsType = dsType;
        this.sourceType = sourceType;
    }

    /**
     * 执行存储过程调用，可带输入、输出参数，oracle可以执行普通的sql，mysql不可以
     *
     * @param sql
     * @return
     */
    public CallableStatement getCallStmt(String sql) throws SQLException {
        if (conn == null) {
            getConn(dsType);
        }
        callStmt = conn.prepareCall(sql);
        return callStmt;
    }

    /**
     * 执行存储过程调用，可带输入参数，当然也可以执行普通的sql
     *
     * @param sql
     * @return
     */
    public PreparedStatement getStmt(String sql) throws SQLException {
        if (conn == null) {
            getConn(dsType);
        }
        preStmt = conn.prepareStatement(sql);
        return preStmt;
    }

    /**
     * 执行存储过程调用，可带输入参数，当然也可以执行普通的sql
     *
     * @param sql
     * @param keyColumnNames 请求驱动程序应返回的主键列名
     * @return
     */
    public PreparedStatement getStmt(String sql, String[] keyColumnNames) throws SQLException {
        if (conn == null) {
            getConn(dsType);
        }
        preStmt = conn.prepareStatement(sql, keyColumnNames);
        return preStmt;
    }

    /**
     * 用于执行一次性或者是不需要进行数据库预编译缓存的查询或更新语句
     *
     * @return
     * @throws SQLException
     */
    public Statement getStmt() throws SQLException {
        if (conn == null) {
            getConn(dsType);
        }
        stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        return stmt;
    }

    public void closeConn() throws SQLException {
        if (callStmt != null) {
            callStmt.close();
            callStmt = null;
        }
        if (preStmt != null) {
            preStmt.close();
            preStmt = null;
        }
        if (stmt != null) {
            stmt.close();
            stmt = null;
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    public void setAutoCommit(boolean auto) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(auto);
        }
    }

    public void commit() throws SQLException {
        if (conn != null) {
            conn.commit();
        }
    }

    public void rollback() throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    public boolean isClosed() throws SQLException {
        if (conn == null) {
            return true;
        }
        return conn.isClosed();
    }

    private void getConn(int dsType) throws SQLException {
        this.conn = dbPool.getDataSource(dsType, sourceType).getConnection();
    }
}