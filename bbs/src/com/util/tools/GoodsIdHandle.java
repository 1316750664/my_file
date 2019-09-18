package com.util.tools;

import com.service.clazz.MainJsonService;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsIdHandle {

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public GoodsIdHandle() {
    }

    /**
     * 产品id产生规则:8位年月日+8位日流水
     */
    public static synchronized String generateGoodId(String table_name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, SQLException, ParseException {
        int sn = getCurrentGoodsId(table_name);
        updateGoodsId(sn + 1, table_name);
        String result = getGoodsSn(sn);

        return result;
    }

    public static synchronized int generateGoodId(int increment, String table_name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, SQLException, ParseException {
        int sn = getCurrentGoodsId(table_name);
        updateGoodsId(sn + increment, table_name);

        return sn;
    }

    public static String getGoodsSn(int sn) {
        return ToolUtil.getFormatSn("yyyyMMdd", null, 8, sn);
    }

    private static int getCurrentGoodsId(String table_name) throws IOException, ParseException, NoSuchMethodException, IllegalAccessException, SQLException, InvocationTargetException {
        Map<String, String[]> paramMap = new HashMap<String, String[]>(7);
        paramMap.put("taskId_1", new String[]{"2132"});
        paramMap.put("cmdType_1", new String[]{"Update"});
        paramMap.put("taskId_2", new String[]{"2134"});
        paramMap.put("cmdType_2", new String[]{"Query"});
        paramMap.put("sql_id", new String[]{"2134"});
        paramMap.put("params", new String[]{"table_name:" + table_name});
        paramMap.put("table_name", new String[]{table_name});
        String jsonSqlMap = new MainJsonService().invoke(paramMap);
        List<Map<String, Object>> list = objectMapper.readValue(jsonSqlMap, List.class);
        List<Map<String, String>> listMap = (List) list.get(1).get("rows");
        int sn = Integer.parseInt(listMap.get(0).get("serial_number"));
        String date = listMap.get(0).get("update_time");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date updateTime = sdf.parse(date);
        if (FormatDate.dateCompare(updateTime, "yyyy-MM-dd")) {
            sn = 1;
        }

        return sn;
    }

    private static void updateGoodsId(int nextSn, String table_name) throws NoSuchMethodException, IllegalAccessException, UnsupportedEncodingException, SQLException, InvocationTargetException {
        //String table_name = "tb_product_offline";
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("taskId", new String[]{"2027"});
        paramMap.put("cmdType", new String[]{"Update"});
        paramMap.put("update_time", new String[]{ToolUtil.getCurrentTimestamp().toString()});
        paramMap.put("serial_number", new String[]{String.valueOf(nextSn)});
        paramMap.put("table_name", new String[]{table_name});
        new MainJsonService().invoke(paramMap);
    }
}
