package com.service.clazz;

import com.util.tools.XmlHelper;
import com.util.tools.XmlOutputMessage;
import com.util.tools.XmlSqlMap;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * User: Administrator
 * Date: 14-4-30
 * Time: 下午3:14
 */
public class MainXmlService {
    private HttpServletRequest request;

    public MainXmlService() {
    }

    public String invoke(String xmlRequest, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, SQLException, NamingException {
        this.request = request;
        return execute(xmlRequest);
    }

    public String execute(String xmlRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UnsupportedEncodingException, SQLException, NamingException {
        Document document = XmlHelper.StringToXMLDocument(xmlRequest);
        if (document == null) {
            return XmlOutputMessage.output("0", "传入的服务信息无法解析");
        }
        Element root = document.getRootElement();
        String serviceId = root.valueOf("@serviceId");
        if (serviceId == null || "".equals(serviceId)) {
            return XmlOutputMessage.output("0", "请求方式参数不正确");
        }
        // 根据服务号调用各处理函数,注意函数名称与服务号要一致
        return this.getClass().getMethod(serviceId, new Class[]{String.class}).invoke(this, new Object[]{xmlRequest}).toString();
    }

    public String SQLMAP(String xmlRequest) throws UnsupportedEncodingException, SQLException, NamingException {
        return new XmlSqlMap().execute(xmlRequest);
    }
}
