package com.service.clazz;

import com.util.tools.JsonSqlMap;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by hzm on 2014/7/4.
 */
public class MainJsonService {

    public MainJsonService() {
    }

    public String invoke(Map<String, String[]> paramMap) throws NoSuchMethodException, InvocationTargetException, SQLException, IllegalAccessException, UnsupportedEncodingException {
        return execute(paramMap);
    }

    public String execute(Map<String, String[]> paramMap) throws NoSuchMethodException, InvocationTargetException, SQLException, IllegalAccessException, UnsupportedEncodingException {
        String serviceId = null;
        String[] serviceIds = paramMap.get("serviceId");
        if (serviceIds == null || serviceIds.length == 0 || "".equals(serviceIds[0])) {
            serviceId = "jsonSqlMap";
        } else {
            serviceId = serviceIds[0];
        }
        // 根据服务号调用各处理函数,注意函数名称与服务号要一致
        return this.getClass().getMethod(serviceId, new Class[]{Map.class}).invoke(this, new Object[]{paramMap}).toString();
    }

    public String jsonSqlMap(Map<String, String[]> paramMap) throws InvocationTargetException, SQLException, IllegalAccessException, UnsupportedEncodingException {
        return new JsonSqlMap().execute(paramMap);
    }
}
