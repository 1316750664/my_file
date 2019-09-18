package com.service.fragService;

import com.bean.TaskBean;
import com.business.TaskHandler;
import com.util.tools.HtmlGenerator;
import com.util.tools.ToolUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hty070503 on 2014/9/19.
 */
public class FragService {
    private ObjectMapper objectMapper = null;

    private static class SingletonFragService {
        private static FragService INSTANCE = new FragService();
    }

    public static FragService getInstance() {
        return SingletonFragService.INSTANCE;
    }

    private FragService() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String fragment(int dbFlag, TaskBean taskBean) {
        try {
            String jsonTypeData = TaskHandler.getInstance().handler(dbFlag, taskBean);
            if (jsonTypeData == null || "".equals(jsonTypeData)) {
                return "[]";
            }
            return jsonTypeData;
        } catch (SQLException e) {
            return "[]";
        } catch (UnsupportedEncodingException e) {
            return "[]";
        }
    }

    public String fragment(int dbFlag, String templatePath, TaskBean taskBean, String tagName) {
        try {
            String jsonTypeData = TaskHandler.getInstance().handler(dbFlag, taskBean);
            if (jsonTypeData == null) {
                return "";
            }
            int cmdType = taskBean.getCmdType();
            Map<String, String> mField = null;
            List<Map<String, String>> listMap = null;
            if (tagName == null) {
                if ("[]".equals(jsonTypeData)) {
                    jsonTypeData = "{}";
                } else {
                    jsonTypeData = jsonTypeData.substring(1, jsonTypeData.length() - 1);
                }
                mField = objectMapper.readValue(jsonTypeData, Map.class);
            } else {
                if (cmdType == 3) {//数据库分页
                    String[] jsonData = jsonTypeData.split("#@#@#");
                    mField = objectMapper.readValue(jsonData[0], Map.class);
                    listMap = objectMapper.readValue(jsonData[1], List.class);
                } else {
                    listMap = objectMapper.readValue(jsonTypeData, List.class);
                }
            }
            int items = 0;
            if (listMap != null) {
                items = listMap.size();
            }
            List<Map<String, String>> subList = null;
            if (cmdType == 1) {//小数据分页
                mField = new HashMap<String, String>(1);
                mField.put("itemCount", String.valueOf(items));
                int curPage = taskBean.getPage();
                int pageSize = taskBean.getRow();
                int fromIndex = (curPage - 1) * pageSize;
                int toIndex = fromIndex + pageSize;
                if (toIndex > items) {
                    toIndex = items;
                }
                if (fromIndex < items) {//否则为空
                    subList = listMap.subList(fromIndex, toIndex);
                }
                if (fromIndex == 0 && fromIndex == toIndex) {//此时全页查看
                    subList = listMap;
                }
            } else if (cmdType == 0 || cmdType == 3) {
                subList = listMap;
            }
            if (items == 0) {
                return HtmlGenerator.getInstance().replaceHtml(ToolUtil.readFile(templatePath), mField, null);
            } else {
                Map<String, List<Map<String, String>>> mapList = new HashMap<String, List<Map<String, String>>>();
                mapList.put(tagName, subList);
                return HtmlGenerator.getInstance().replaceHtml(ToolUtil.readFile(templatePath), mField, mapList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }/*catch (UnsupportedEncodingException e) {
            return "";
        } catch (SQLException e) {
            return "";
        } catch (JsonParseException e) {
            return "";
        } catch (JsonMappingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }*/
    }
}