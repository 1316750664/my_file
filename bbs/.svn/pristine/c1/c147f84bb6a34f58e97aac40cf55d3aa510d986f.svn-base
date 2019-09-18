package com.util.tools;

import com.bean.TaskBean;
import com.service.fragService.FragService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bangong on 2015/6/25.
 * 用做反射选择器
 */
public class TaskBeanUtil {
    public TaskBeanUtil() {
    }

    private static String json(String text) {
        if (CheckTools.isNull(text)) {
            return "[]";
        }
        int last = text.lastIndexOf(",");
        return "[" + text.substring(0, last) + "]";
    }

    //有user_id方法
    //直接返回数据
    public static String backData(HttpServletRequest request, int flag, TaskBean taskBean, String user_id) {
        return FragService.getInstance().fragment(flag, taskBean);
    }

    //无user_id方法
    //直接返回数据
    public static String backData(HttpServletRequest request, int flag, TaskBean taskBean) {
        return FragService.getInstance().fragment(flag, taskBean);
    }

    public static String postHost(HttpServletRequest request, int flag, TaskBean taskBean) {
        String fragPath = request.getSession().getServletContext().getRealPath("/frag");
        String text = FragService.getInstance().fragment(flag, fragPath + "/post_host.txt", taskBean, "xunhuan");
        text = DataReplaceUtil.replacePoints("###jifen:(.*?)###", text);//换会员头像图
        return json(text);
    }
}
