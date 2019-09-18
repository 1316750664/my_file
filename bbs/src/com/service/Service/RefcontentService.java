package com.service.Service;

import com.bean.TaskBean;
import com.service.fragService.FragService;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by uygh on 2015/3/21.
 */
public class RefcontentService {

    private RefcontentService() {

    }

    private static class SingletonRefcontentService {
        private static RefcontentService INSTANCE = new RefcontentService();
    }

    public static RefcontentService getInstance() {
        return SingletonRefcontentService.INSTANCE;
    }

    public static List<Map<String, String>> getContentAslist(String postId, String floor) {
        List<Map<String, String>> list = null;
        List<Map<String, String>> list2 = null;
        String jsonArry = null;
        String[] param = {postId, floor};
        TaskBean taskBean = new TaskBean(310, 0, param);
        String result = FragService.getInstance().fragment(10, taskBean);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
                list = objectMapper.readValue(result, List.class);

                for (Map<String, String> map : list) {
                    jsonArry = map.get("ref_content");
                }
                if (jsonArry !="")
                    list2 = objectMapper.readValue(jsonArry, List.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list2;
    }

}


