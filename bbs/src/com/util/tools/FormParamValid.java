package com.util.tools;

import org.dom4j.Document;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hzm on 2014/9/12.
 */
public class FormParamValid {
    private static class SingletonFormParamValid {
        private static FormParamValid INSTANCE = new FormParamValid();
    }

    public static FormParamValid getInstance() {
        return SingletonFormParamValid.INSTANCE;
    }

    private FormParamValid() {
    }

    public String valid(Map<String, String[]> paramMap) throws UnsupportedEncodingException {
        String message = null;
        String key = null;
        String[] values = null;
        Document paramDoc = null;
        Element root = null;
        Iterator<Element> iterator2 = null;
        Element paramElement = null;
        String paramName = null;
        String[] paramValues = null;
        String paramValue = null;
        String validType = null;
        int i = 0;
        Iterator<Map.Entry<String, String[]>> iterator1 = paramMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<String, String[]> entry = iterator1.next();
            key = entry.getKey();
            if (!key.startsWith("taskId")) {
                continue;
            }
            values = entry.getValue();
            if (values == null) {
                throw new RuntimeException("任务号参数为空");
            }
            paramDoc = SqlXmlRead.getSqlParamFile(values[0]);
            if (paramDoc == null) {
                continue;//无参数需要验证
            }
            root = paramDoc.getRootElement();
            if (root == null) {
                continue;
            }
            iterator2 = root.elementIterator();
            while (iterator2.hasNext()) {//遍历参数配置文件中的结点，根据结点数据类型进行参数替换
                paramElement = iterator2.next();
                paramName = paramElement.getName();
                validType = paramElement.valueOf("@valid");
                if (validType == null) {
                    continue;
                }
                paramValues = paramMap.get(paramName);
                if (paramValues == null) {
                    paramValue = null;
                    return valid(paramName, validType, paramValue);
                } else {
                    for (i = 0; i < paramValues.length; i++) {
                        paramValue = paramValues[i];
                        message = valid(paramName, validType, paramValue);
                        if (message != null) {
                            return message;
                        }
                    }
                }
            }
        }
        return message;
    }

    //新返回格式
    public String valid2(Map<String, String[]> paramMap) throws UnsupportedEncodingException {
        String message = null;
        String key = null;
        String[] values = null;
        Document paramDoc = null;
        Element root = null;
        Iterator<Element> iterator2 = null;
        Element paramElement = null;
        String paramName = null;
        String[] paramValues = null;
        String paramValue = null;
        String validType = null;
        int i = 0;
        Iterator<Map.Entry<String, String[]>> iterator1 = paramMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<String, String[]> entry = iterator1.next();
            key = entry.getKey();
            if (!key.startsWith("taskId")) {
                continue;
            }
            values = entry.getValue();
            if (values == null) {
                throw new RuntimeException("任务号参数为空");
            }
            paramDoc = SqlXmlRead.getSqlParamFile(values[0]);
            if (paramDoc == null) {
                continue;//无参数需要验证
            }
            root = paramDoc.getRootElement();
            if (root == null) {
                continue;
            }
            iterator2 = root.elementIterator();
            while (iterator2.hasNext()) {//遍历参数配置文件中的结点，根据结点数据类型进行参数替换
                paramElement = iterator2.next();
                paramName = paramElement.getName();
                validType = paramElement.valueOf("@valid");
                if (validType == null) {
                    continue;
                }
                paramValues = paramMap.get(paramName);
                if (paramValues == null) {
                    paramValue = null;
                    return valid2(paramName, validType, paramValue);
                } else {
                    for (i = 0; i < paramValues.length; i++) {
                        paramValue = paramValues[i];
                        message = valid2(paramName, validType, paramValue);
                        if (message != null) {
                            return message;
                        }
                    }
                }
            }
        }
        return message;
    }

    public String valid(String validName, String validType, String validValue) {
        String message = null;
        if ("number".equals(validType) && !CheckTools.checkIsNum(validValue)) {
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("email".equals(validType) && !("").equals(validValue) && !CheckTools.checkEmail(validValue)) {
            //如果填写邮箱，判断格式
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("null".equals(validType) && CheckTools.isNull(validValue)) {
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("phone".equals(validType) && (("").equals(validValue) || (!CheckTools.checkTel(validValue) && !CheckTools.checkMobile(validValue)) )) {
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("float".equals(validType) && !CheckTools.checkIsPoint(validValue)) {
            message = ErrorMsgEnum.getMessage(validType);
        }
        if (message == null) {
            return null;
        } else {
            return "{\"error\":\"101\",\"msg\":\"" + validName + message + "\",\"field\":\"" + validName + "\"}";
        }
    }
    //新返回格式
    public String valid2(String validName, String validType, String validValue) {
        String message = null;
        if ("number".equals(validType) && !CheckTools.checkIsNum(validValue)) {
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("email".equals(validType) && !("").equals(validValue) && !CheckTools.checkEmail(validValue)) {
            //如果填写邮箱，判断格式
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("null".equals(validType) && CheckTools.isNull(validValue)) {
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("mobile".equals(validType) && (("").equals(validValue) || (!CheckTools.checkMobile(validValue)))) {
            message = ErrorMsgEnum.getMessage(validType);
        }else if ("telephone".equals(validType) && (("").equals(validValue) || (!CheckTools.checkTel(validValue)))) {
            message = ErrorMsgEnum.getMessage(validType);
        }else if ("phone".equals(validType) && (("").equals(validValue) || (!CheckTools.checkTel(validValue) && !CheckTools.checkMobile(validValue)))) {
            message = ErrorMsgEnum.getMessage(validType);
        } else if ("float".equals(validType) && !CheckTools.checkIsPoint(validValue)) {
            message = ErrorMsgEnum.getMessage(validType);
        }else if ("car".equals(validType) && !(validValue.length() == 7)) {
            message = ErrorMsgEnum.getMessage(validType);
        }
        if (message == null) {
            return null;
        } else {
            return ToolUtil.getReturnResultForString(707,validName + message,"{\"res\":\"参数验证\"}");
        }
    }
}
