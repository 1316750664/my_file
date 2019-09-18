package com.ctd.util.database.dao;

import com.ctd.util.database.TaoDbConnection;
import com.util.tools.FormatDate;
import com.util.tools.XmlHelper;
import oracle.jdbc.OracleTypes;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * User: huhoo
 * Date: 14-4-30
 * Time: 下午1:37
 */
public class XmlDBAction {
    private TaoDbConnection dbConn;
    private int flag;

    public XmlDBAction(int flag) {
        this.flag = flag;
    }

    private void getConnection() {
        if (flag == 1) {//只读
            dbConn = new TaoDbConnection(1);
        } else {
            dbConn = new TaoDbConnection(0);
        }
    }

    public Document getAttributeXmlResult(String sql) throws SQLException, NamingException {
        Document sDocument = XmlHelper.createDocument();
        Element sElement = sDocument.addElement("result");
        if (sql == null || "".equals(sql)) {
            sElement.addAttribute("row", "0");
            return sDocument;
        }
        if (dbConn == null) {
            getConnection();
        }
        ResultSet rs = dbConn.getCallStmt(sql).executeQuery();
        int row = getXml(rs, sElement, true);
        rs.close();
        sElement.addAttribute("row", String.valueOf(row));
        return sDocument;
    }

    public Document getNodeXmlResult(String sql) throws SQLException, NamingException {
        Document sDocument = XmlHelper.createDocument();
        Element sElement = sDocument.addElement("result");
        if (sql == null || "".equals(sql)) {
            sElement.addAttribute("row", "0");
            return sDocument;
        }
        if (dbConn == null) {
            getConnection();
        }
        ResultSet rs = dbConn.getCallStmt(sql).executeQuery();
        int row = getXml(rs, sElement, false);
        rs.close();
        sElement.addAttribute("row", String.valueOf(row));
        return sDocument;
    }

    public Document getOraclePageQuery(boolean method, String sql, int nowPage, int pageSize) throws SQLException, NamingException {
        Document sDocument = XmlHelper.createDocument();
        Element sElement = sDocument.addElement("result");
        if (sql == null || "".equals(sql)) {
            sElement.addAttribute("total", "0");
            sElement.addAttribute("page", "0");
            sElement.addAttribute("row", "0");
            return sDocument;
        }
        if (dbConn == null) {
            getConnection();
        }
        String callSql = "{call SqlPageCursor.DataSqlPage(?,?,?,?,?,?)}";
        CallableStatement callStmt = dbConn.getCallStmt(callSql);
        callStmt.setString(1, sql);
        callStmt.setInt(2, nowPage);
        callStmt.setInt(3, pageSize);
        callStmt.registerOutParameter(4, OracleTypes.INTEGER);
        callStmt.registerOutParameter(5, OracleTypes.INTEGER);
        callStmt.registerOutParameter(6, OracleTypes.CURSOR);
        callStmt.executeQuery();
        int totalRecord = callStmt.getInt(4);//总记录数
        int totalPage = callStmt.getInt(5);//总页数
        ResultSet rs = (ResultSet) callStmt.getObject(6);
        int row = getXml(rs, sElement, method);//当前页记录数
        rs.close();
        sElement.addAttribute("total", String.valueOf(totalRecord));
        sElement.addAttribute("page", String.valueOf(totalPage));
        sElement.addAttribute("row", String.valueOf(row));
        return sDocument;
    }

    /**
     * 执行单条插入，返回执行结果
     * -2 sql语句或参数不正确 -1执行失败
     *
     * @param sql
     * @return
     */
    public Document update(String sql) throws SQLException, NamingException {
        Document sDocument = XmlHelper.createDocument();
        Element sElement = sDocument.addElement("result");
        int row = -1;
        if (sql != null && !"".equals(sql)) {
            if (dbConn == null) {
                getConnection();
            }
            PreparedStatement stmt = dbConn.getStmt(sql);
            row = stmt.executeUpdate();
        } else {
            row = -2;
        }
        sElement.addAttribute("row", String.valueOf(row));
        return sDocument;
    }

    /**
     * 执行单条插入，返回执行结果
     * -2 sql语句或参数不正确 -1执行失败
     *
     * @param method
     * @param sql
     * @param keyColumnName
     * @return
     */
    public Document updateAndAutoKey(String method, String sql, String keyColumnName) throws SQLException, NamingException {
        Document sDocument = XmlHelper.createDocument();
        Element sElement = sDocument.addElement("result");
        int row = -1;
        if (sql != null && !"".equals(sql)) {
            PreparedStatement stmt = dbConn.getStmt(sql, new String[]{keyColumnName});
            row = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                Element rElement = sElement.addElement("row");
                String key = String.valueOf(rs.getLong(1));
                if ("Attribute".equals(method)) {
                    rElement.addAttribute(keyColumnName, key);
                } else {
                    Element kElement = sElement.addElement(keyColumnName);
                    kElement.addText(key);
                }
            }
            rs.close();
        } else {
            row = -2;
        }
        sElement.addAttribute("row", String.valueOf(row));
        return sDocument;
    }

    public void setAutoCommit(boolean auto) throws SQLException {
        if (dbConn == null) {
            getConnection();
        }
        dbConn.setAutoCommit(auto);
    }

    public void commit() throws SQLException {
        if (dbConn == null) {
            return;
        }
        dbConn.commit();
    }

    public void rollback() throws SQLException {
        if (dbConn == null) {
            return;
        }
        dbConn.rollback();
    }

    public void close() throws SQLException {
        if (dbConn != null) {
            dbConn.closeConn();
        }
    }

    private int getXml(ResultSet rs, Element sElement, boolean method) throws SQLException {
        int row = 0;
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        while (rs.next()) {
            row++;
            Element rElement = sElement.addElement("row");
            for (int i = 1; i <= cols; i++) {
                String s_temp = "";
                String columnName = rsmd.getColumnName(i).toLowerCase();
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
                if (method) {
                    rElement.addAttribute(columnName, s_temp);
                } else {
                    Element node = rElement.addElement(columnName);
                    s_temp = s_temp.replaceAll("<", "﹤");
                    s_temp = s_temp.replaceAll(">", "﹥");
                    node.addText(s_temp);
                }
            }
        }
        return row;
    }

    private static String nclob2String(NClob nClob) throws SQLException, IOException {
        String result = "";
        Reader reader = nClob.getCharacterStream(); // 取得大字侧段对象数据输出流
        StringBuilder sb = new StringBuilder("");
        int length = -1;
        char[] buffer = new char[10];
        while ((length = reader.read(buffer)) != -1) // 读取数据库//每10个10个读取
        {
            for (int i = 0; i < length; i++) {
                sb.append(buffer[i]);
            }
        }
        reader.close();
        result = sb.toString();
        sb.delete(0, sb.length());
        return result;
    }
}