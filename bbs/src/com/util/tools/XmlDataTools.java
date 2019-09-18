package com.util.tools;

import com.ctd.util.database.dao.XmlDBAction;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: hzm
 * Date: 14-5-3
 * Time: 下午4:17
 */
public class XmlDataTools {
    private XmlDBAction dbAction = null;

    public Document execute(Document sqlDocument) throws SQLException, NamingException {
        Document document = XmlHelper.createDocument();
        Element sElement = document.addElement("xmldata");
        if (sqlDocument == null) {
            return document;
        }
        List<Element> elementList = sqlDocument.selectNodes("//Sql");
        if (elementList == null) {
            return document;
        }
        Element rootElement = sqlDocument.getRootElement();
        String readOnly = rootElement.valueOf("@readOnly");
        if ("1".equals(readOnly)) {
            dbAction = new XmlDBAction(1);
        } else {
            dbAction = new XmlDBAction(0);
        }
        for (Element element : elementList) {
            Element rElement = null;
            String sql = element.getText();
            String cmdType = element.valueOf("@cmdType");
            if ("Query".equals(cmdType)) {
                rElement = _GetQueryData(element.valueOf("@method"), sql);
            } else if ("Page".equals(cmdType)) {
                rElement = _GetPageQueryData(element.valueOf("@method"), sql, Integer.parseInt(element.valueOf("@page")), Integer.parseInt(element.valueOf("@row")));
            } else if ("Update".equals(cmdType)) {
                rElement = _GetUpdateData(element.valueOf("@method"), sql, element.valueOf("@ide"));
            }
            sElement.add(rElement);
        }
        dbAction.close();
        return document;
    }

    private Element _GetQueryData(String method, String sql) throws SQLException, NamingException {
        if ("Attribute".equals(method)) {
            return _GetQueryAttributeData(sql).getRootElement();
        } else {
            return _GetQueryNodeData(sql).getRootElement();
        }
    }

    private Document _GetQueryAttributeData(String sql) throws SQLException, NamingException {
        return dbAction.getAttributeXmlResult(sql);
    }

    private Document _GetQueryNodeData(String sql) throws SQLException, NamingException {
        return dbAction.getNodeXmlResult(sql);
    }

    private Element _GetPageQueryData(String method, String sql, int nowPage, int pageSize) throws SQLException, NamingException {
        boolean bool = true;
        if ("Node".equals(method)) {
            bool = false;
        }
        return dbAction.getOraclePageQuery(bool, sql, nowPage, pageSize).getRootElement();
    }

    private Element _GetUpdateData(String method, String sql, String keyColumnName) throws SQLException, NamingException {
        Element element = null;
        if (keyColumnName == null || "".equals(keyColumnName)) {
            element = dbAction.update(sql).getRootElement();
        } else {
            element = dbAction.updateAndAutoKey(method, sql, keyColumnName).getRootElement();
        }
        return element;
    }
}
