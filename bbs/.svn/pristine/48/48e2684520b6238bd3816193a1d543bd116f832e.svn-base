package com.util.tools;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 返回消息封装成指定XML格式
 *
 * @author Administrator
 */
public class XmlOutputMessage {

    public static String output(String as_code, String msg) {
        Document lDocument = XmlHelper.createDocument();
        Element sElement = lDocument.addElement("xmldata");
        sElement.addAttribute("code", as_code);
        sElement.addAttribute("msg", msg);
        return lDocument.getRootElement().asXML();
    }

    public static String output(String as_code, String msg, String s_arg[], String s_value[]) {
        Document lDocument = XmlHelper.createDocument();
        Element sElement = lDocument.addElement("xmldata");
        sElement.addAttribute("code", as_code);
        sElement.addAttribute("msg", msg);
        for (int i = 0; i < s_arg.length; i++) {
            sElement.addAttribute(s_arg[i], s_value[i]);
        }
        return lDocument.getRootElement().asXML();

    }

    public static String output(String as_code, String msg, HashMap<String, String> hashMap) {
        Document lDocument = XmlHelper.createDocument();
        Element sElement = lDocument.addElement("xmldata");
        sElement.addAttribute("code", as_code);
        sElement.addAttribute("msg", msg);
        if (hashMap != null) {
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = (Entry) it.next();
                sElement.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        return lDocument.getRootElement().asXML();
    }

    public static String output(String as_code, String msg, HashMap<String, String> hashMap, Element aElement) {
        Document lDocument = XmlHelper.createDocument();
        Element sElement = lDocument.addElement("xmldata");
        sElement.addAttribute("code", as_code);
        sElement.addAttribute("msg", msg);
        if (hashMap != null) {
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = (Entry) it.next();
                sElement.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        if (aElement != null) {
            sElement.add(aElement);
        }
        return lDocument.getRootElement().asXML();
    }

    public static String output(String as_code, String msg, Element aElement) {
        return output(as_code, msg, null, aElement);
    }

    public static String output(String as_code, String msg, Document document) {
        Element rootElement = document.getRootElement();
        rootElement.addAttribute("code", as_code);
        rootElement.addAttribute("msg", msg);
        return document.getRootElement().asXML();
    }
}