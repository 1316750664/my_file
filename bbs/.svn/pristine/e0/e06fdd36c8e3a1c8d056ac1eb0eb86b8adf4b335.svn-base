package com.util.tools;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: hzm
 * Date: 14-5-3
 * Time: 上午9:51
 */
public class XmlSqlMap {
    public XmlSqlMap() {
    }

    public String execute(String xmlRequest) throws UnsupportedEncodingException, SQLException, NamingException {
        Document document = XmlHelper.StringToXMLDocument(xmlRequest);
        List<Element> list = document.selectNodes("//Task");
        if (list == null) {
            return XmlOutputMessage.output("0", "未找到需要处理的任务");
        }
        String taskId;
        String cmdType;//数据库命令Query/Page/Update
        String method;//返回xml结构类型attribute node
        String page = "1";//返回第几页
        String row = "20";//返回第几页的多少行
        String ide = null;
        String sql;
        Document sqlDocument = XmlHelper.createDocument();
        Element taskElement = sqlDocument.addElement("Task");
        taskElement.addAttribute("readOnly", document.getRootElement().valueOf("@readOnly"));
        for (Element element : list) {
            taskId = element.valueOf("@taskId");
            cmdType = element.valueOf("@cmdType");
            method = element.valueOf("@method");
            if ("Page".equals(cmdType)) {
                page = element.valueOf("@page");
                row = element.valueOf("@row");
            } else if ("Update".equals(cmdType)) {
                ide = element.valueOf("@ide");
            }
            sql = SqlXmlRead.getSQL(taskId);
            if ("".equals(sql)) {
                throw new RuntimeException("未找到sql语句");
            }

            // 在该SQL语句中查找参数，参数是以@开头，并以@结束；
            if (sql.indexOf("@") >= 0) {
                List<Element> argList = element.selectNodes("Arg");
                if (argList == null || argList.isEmpty()) {
                    throw new RuntimeException("缺失的sql参数");
                }
                // 在更新形式下，insert，delete,update语句可能有多个Argument节点，形成多条批处理SQL
                for (Element paramElm : argList) {
                    String sqlTemp = sql;
                    for (int i = 0; i < paramElm.nodeCount(); i++) {
                        Node node = paramElm.node(i);
                        String nodeName = node.getName();//字段
                        if (nodeName == null || "".equals(nodeName)) {
                            continue;
                        }
                        String nodeText = node.getText();//值
                        String dataType = node.valueOf("@dataType").toLowerCase();
                        if ("string".equals(dataType)) {//数据库''相连则表示'，因为'是数据库的转义符
                            if (nodeText.indexOf("''") == -1 && nodeText.indexOf("'") >= 0) {
                                nodeText = nodeText.replaceAll("'", "''");
                            }
                            nodeText = "'" + nodeText + "'";
                        } else if ("date".equals(dataType)) {
                            nodeText = "'" + nodeText + "'";
                        } else if ("number".equals(dataType)) {
                            if ("".equals(nodeText)) {
                                nodeText = "null";
                            } else {//防止SQL注入
                                if (!CheckTools.checkIsNum(nodeText)) {
                                    throw new RuntimeException("非法数值参数");
                                }
                            }
                        }
                        nodeName = "@" + nodeName + "@";
                        sqlTemp = sqlTemp.replaceAll(nodeName, nodeText);
                    }
                    Element sqlElement = taskElement.addElement("Sql");
                    sqlElement.addAttribute("cmdType", cmdType);
                    sqlElement.addAttribute("method", method);
                    if ("Page".equals(cmdType)) {
                        sqlElement.addAttribute("page", page);
                        sqlElement.addAttribute("row", row);
                    } else if ("Update".equals(cmdType)) {
                        if (ide != null && !"".equals(ide)) {
                            sqlElement.addAttribute("ide", ide);
                        }
                    }
                    sqlElement.addText(sqlTemp);
                }
            } else {
                Element sqlElement = taskElement.addElement("Sql");
                sqlElement.addAttribute("cmdType", cmdType);
                sqlElement.addAttribute("method", method);
                if ("Page".equals(cmdType)) {
                    sqlElement.addAttribute("page", page);
                    sqlElement.addAttribute("row", row);
                } else if ("Update".equals(cmdType)) {
                    if (ide != null && !"".equals(ide)) {
                        sqlElement.addAttribute("ide", ide);
                    }
                }
                sqlElement.addText(sql);
            }
        }
        return XmlOutputMessage.output("1", "执行成功", new XmlDataTools().execute(sqlDocument));
    }
}
