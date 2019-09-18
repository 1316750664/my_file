package com.service.Service.Bbs;

import com.bean.TaskBean;
import com.service.fragService.FragService;
import com.util.tools.HtmlGenerator;
import com.util.tools.StaticUtil;
import com.util.tools.ToolUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.tools.Tool;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bangong on 2015/4/18.
 */
public class BbsTaskService {
    public static BbsTaskService getInstance() {
        return SingletonBbsTaskService.INSTANCE;
    }

    private static class SingletonBbsTaskService {
        private static BbsTaskService INSTANCE = new BbsTaskService();
    }

    private BbsTaskService() {

    }

    /**
     * 只有文字内容
     *
     * @param map
     * @param frag
     * @return
     */
    public String taskMap(Map<String, String> map, String frag) {
        String auto_load = map.get("auto_load");
        String ids = map.get("ids");
        if (auto_load.equals("") || ids.equals("")) {
            return "参数出错";
        }
        TaskBean taskBean = makeTaskBean(map);
        return FragService.getInstance().fragment(0, frag, taskBean, "index");
    }

    /**
     * 上下结构带大图
     */
    public String taskMapSomePic(Map<String, String> map, String tuFrag,String neiFrag, String mapUrls) {
        try {
            String auto_load = map.get("auto_load");
            String ids = map.get("ids");
            if (auto_load.equals("") || ids.equals("")) {
                return "参数出错";
            }
            TaskBean taskBean = makeTaskBean(map);
            String jsonTypeData = FragService.getInstance().fragment(0, taskBean);
            //分析图片
            String[] maps = mapUrls.split(",");
            int size = maps.length;//图片数
            List<Map<String, String>> list = StaticUtil.objectMapper.readValue(jsonTypeData, List.class);
            List<Map<String, String>> listTu = new ArrayList<Map<String, String>>();
            List<Map<String, String>> listNei = new ArrayList<Map<String, String>>();
            for (int i = 0; i < size; i++) {
                listTu.add(list.get(i));
            }
            for(;size < list.size(); size++){
                listNei.add(list.get(size));
            }
            Map<String, List<Map<String, String>>> mapList = new HashMap<String, List<Map<String, String>>>();
            mapList.put("index", listTu);
            String tuText = HtmlGenerator.getInstance().replaceHtml(ToolUtil.readFile(tuFrag), null, mapList);
            mapList.put("index", listNei);
            String neiText = HtmlGenerator.getInstance().replaceHtml(ToolUtil.readFile(neiFrag), null, mapList);
            return tuText+neiText;
        } catch (IOException e) {
//            e.printStackTrace();
            return "查询结果出错";
        }
    }

    private TaskBean makeTaskBean(Map<String, String> map){
        String read_num = map.get("read_num");
        String auto_load = map.get("auto_load");
        String ids = map.get("ids");
        String sort_field = map.get("sort_field");
        StringBuilder sb = new StringBuilder();
        int taskId = 0;
        if (auto_load.equals("0")) {
            taskId = 40;
        } else {
            taskId = 41;
        }
        if (!sort_field.equals("")) {//多个逗号分开 desc自带
            sb.append("order by ");
            String[] fields = sort_field.split(",");
            for(String s : fields){
                sb.append(s + " ,");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        if (!read_num.equals("")) {
            read_num = " limit " + read_num;
        }
        return new TaskBean(taskId, 0, 1, new String[]{ids, sb.toString(), read_num}, new String[]{"object", "object", "object"});
    }

    private List<TaskBean> makeTaskBeanList(List<Map<String, String>> list) {
        List<TaskBean> listBean = new ArrayList<TaskBean>();
        for (Map<String, String> map : list) {
            listBean.add(makeTaskBean(map));
        }
        return listBean;
    }
}
