package com.ctd.util.database.mongo;

import com.util.tools.ReadWriteProperties;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hzm on 2015/2/9.
 */
public class MyMongoDbSingleton {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    private ReadWriteProperties propertiesUtil = null;
    private MongoBdUtil mongoBdUtil = null;

    private static class MyMongoDb {
        private static MyMongoDbSingleton INSTANCE = new MyMongoDbSingleton();
    }

    public static MyMongoDbSingleton getInstance() {
        return MyMongoDb.INSTANCE;
    }

    private MyMongoDbSingleton() {
        propertiesUtil = ReadWriteProperties.getInstance();
        mongoBdUtil = MongoBdUtil.getInstance();
        mongoBdUtil.getDB("hty");//固定mongodb数据库名
    }

    public MongoBdUtil getMongoBdUtil() {
        return mongoBdUtil;
    }

    public void execute(int sqlId) {
        String json = propertiesUtil.readValue("sqlconfig", String.valueOf(sqlId));
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
