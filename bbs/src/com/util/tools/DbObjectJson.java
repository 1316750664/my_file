package com.util.tools;

import com.mongodb.DBObject;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hzm on 2015/2/15.
 */
public class DbObjectJson {

    public static String toJson(DBObject dbObject) {
        if (dbObject == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        String key = null;
        Set<String> keySet = dbObject.keySet();
        Iterator<String> iterator0 = keySet.iterator();
        while (iterator0.hasNext()) {
            key = iterator0.next().toLowerCase();
            String s_temp = "";
            Object value = dbObject.get(key);
            if (value != null) {
                s_temp = value.toString().trim();
            }
            s_temp = ("".equals(s_temp) || "null".equals(s_temp)) ? " " : s_temp.replace("\"", "\\\"");
            sb.append(",").append("\"").append(key).append("\"").append(":").append("\"").append(s_temp).append("\"");
        }
        sb.append("]");

        String result = sb.toString().replaceFirst(",", "");
        sb.delete(0, sb.length());

        return result;
    }

    public static String toJson(List<DBObject> dbObjectList) {
        if (dbObjectList == null || dbObjectList.isEmpty()) {
            return "[]";
        }
        DBObject dbObject = dbObjectList.get(0);
        if (dbObject == null) {
            return "[]";
        }

        Set<String> keySet = dbObject.keySet();
        int max = keySet.size();
        String[] keys = new String[max];
        int index = 0;
        Iterator<String> iterator0 = keySet.iterator();
        while (iterator0.hasNext()) {
            keys[index] = iterator0.next().toLowerCase();
            index++;
        }
        StringBuilder sb = new StringBuilder("[");
        int size = dbObjectList.size();
        int last = max - 1;
        for (int i = 0; i < size; i++) {
            dbObject = dbObjectList.get(i);
            if (dbObject == null) {
                continue;
            }
            sb.append("{");
            for (index = 0; index < max; index++) {
                String s_temp = "";
                String columnName = keys[index];
                Object value = dbObject.get(columnName);
                if (value != null) {
                    s_temp = value.toString().trim();
                }
                s_temp = ("".equals(s_temp) || "null".equals(s_temp)) ? " " : s_temp.replace("\"", "\\\"");
                sb.append("\"").append(columnName).append("\"").append(":").append("\"").append(s_temp).append("\"");
                if (index < last) {
                    sb.append(",");
                }
            }
            sb.append("},");
        }
        int sbLen = sb.length();
        if (sb.lastIndexOf(",") == sbLen - 1) {
            sb.delete(sbLen - 1, sbLen);
        }
        sb.append("]");

        String result = sb.toString();
        sb.delete(0, sb.length());

        return result;
    }
}
