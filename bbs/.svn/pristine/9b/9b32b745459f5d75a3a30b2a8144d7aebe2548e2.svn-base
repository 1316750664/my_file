package com.util.tools;

import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * sqlconfig XML文件读取
 *
 * @author huhoo
 */
public class SqlXmlRead {

    public static String getSQL(String id) throws UnsupportedEncodingException {
        String value = "";
        String configPath = SqlXmlRead.class.getClassLoader().getResource("sqlconfig.xml").getFile();
        configPath = java.net.URLDecoder.decode(configPath, "UTF-8");
        File file = new File(configPath);
        Document doc = XmlHelper.getDocument(file);
        Element sqlElement = doc.elementByID(id);
        if (sqlElement != null) {
            value = sqlElement.valueOf("@value");
        }
        value = value.replaceAll("＜", "<");
        value = value.replaceAll("＞", ">");
        return value;
    }

    public static String getSQL(int id) throws UnsupportedEncodingException {
        return getSQL(String.valueOf(id));
    }

    public static Document getSqlParamFile(String id) throws UnsupportedEncodingException {
        String configPath = SqlXmlRead.class.getClassLoader().getResource("sqlmap/" + id + ".xml").getFile();
        configPath = java.net.URLDecoder.decode(configPath, "UTF-8");
        File file = new File(configPath);
        if (file.exists()) {
            return XmlHelper.getDocument(file);
        }
        return null;
    }
}