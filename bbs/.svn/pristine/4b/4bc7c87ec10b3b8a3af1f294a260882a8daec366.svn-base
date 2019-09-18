package com.service.Service.rll;

import com.bean.TaskBean;
import com.service.Service.UserService.UserService;
import com.util.tools.ToolUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 2015/8/13
 */
public class TaskBeanService {

    public static TaskBeanService getInstance() {
        return SingletonTaskBeanService.INSTANCE;
    }

    private static class SingletonTaskBeanService {
        private static TaskBeanService INSTANCE = new TaskBeanService();
    }

    private TaskBeanService() {
    }

    /**
     * 验证参数合法(不完善可补充) 同时替换user_id
     *
     * @param paramMap
     * @return null正确
     */
    public String validMessage(HttpServletRequest request, Map<String, String[]> paramMap) {
        if (null == paramMap.get("taskId") || null == paramMap.get("cmdType") || null == paramMap.get("flag")) {
            return "有重要参数为空";
        }
        int taskId = 0;
        try {
            taskId = Integer.parseInt(paramMap.get("taskId")[0]);
            Integer.parseInt(paramMap.get("cmdType")[0]);
            Integer.parseInt(paramMap.get("flag")[0]);
        } catch (Exception e) {
            return "参数类型错误";
        }
        if (taskId <= 0) {
            return "任务号不可用";
        }
        String[] taskName = paramMap.get("taskName");
        if (null == taskName || "".equals(taskName[0])) {
            return "任务参数错误";
        }
        String[] tmpValues = paramMap.get("paramValues");
        String user_id = null;
        if (null != tmpValues) {
            for (int i = 0; i < tmpValues.length; i++) { //替换user_id
                if (tmpValues[i].equals("user_id")) {
                    // user_id 替换
                    UserService userService = UserService.getInstance();
                    user_id = userService.getUidFromCookie(request);//未登录 报错?
                    if (null == user_id || "".equals(user_id)) {
                        return "非法访问";
                    }
                    tmpValues[i] = user_id;
                }
            }
        }
        return null;
    }

    /**
     * 返回格式改版
     * @param request
     * @param paramMap
     * @return
     */
    public String validMessage2(HttpServletRequest request, Map<String, String[]> paramMap) {
        if (null == paramMap.get("taskId") || null == paramMap.get("cmdType") || null == paramMap.get("flag")) {
            return ToolUtil.getReturnResultForString(700, null);
        }
        int taskId = 0;
        try {
            taskId = Integer.parseInt(paramMap.get("taskId")[0]);
            Integer.parseInt(paramMap.get("cmdType")[0]);
            Integer.parseInt(paramMap.get("flag")[0]);
        } catch (Exception e) {
            return ToolUtil.getReturnResultForString(701, null);
        }
        if (taskId <= 0) {
            return ToolUtil.getReturnResultForString(702, null);
        }
        String[] taskName = paramMap.get("taskName");
        if (null == taskName || "".equals(taskName[0])) {
            return ToolUtil.getReturnResultForString(700, null);
        }
        String[] tmpValues = paramMap.get("paramValues");
        String user_id = null;
        if (null != tmpValues) {
            for (int i = 0; i < tmpValues.length; i++) { //替换user_id
                if (tmpValues[i].equals("user_id")) {
                    // user_id 替换
                    UserService userService = UserService.getInstance();
                    user_id = userService.getUidFromCookie(request);//未登录 报错?
                    if (null == user_id || "".equals(user_id)) {
                        return ToolUtil.getReturnResultForString(403, null);
                    }
                    tmpValues[i] = user_id;
                }
            }
        }
        return null;
    }
    /**
     * 组装taskBean
     *
     * @param paramMap
     * @return TaskBean
     */
    public TaskBean makeTaskBean(Map<String, String[]> paramMap) {
        int taskId = Integer.parseInt(paramMap.get("taskId")[0]);
        int cmdType = Integer.parseInt(paramMap.get("cmdType")[0]);
        String[] tmpValues = paramMap.get("paramValues");
        String[] tmpTypes = paramMap.get("paramTypes");
        String[] method = paramMap.get("method");
        String[] keyColumns = paramMap.get("keyColumns");
        String[] pages = paramMap.get("page");
        String[] rows = paramMap.get("row");
        TaskBean taskBean = new TaskBean(taskId, cmdType);
        taskBean.setParamValues(tmpValues);
        if (null != tmpTypes) {
            taskBean.setParamTypes(tmpTypes);
        }
        if (method != null) {
            taskBean.setMethod(Integer.parseInt(method[0]));
        }
        if (null != keyColumns) {
            taskBean.setKeyColumns(keyColumns);
        }
        if (null != pages) {
            taskBean.setPage(Integer.parseInt(pages[0]));
        }
        if (null != rows) {
            taskBean.setRow(Integer.parseInt(rows[0]));
        }
        return taskBean;
    }

    /**
     * taskBean通用执行方法
     *
     * @param taskName TaskBeanUtil中的方法
     * @param request
     * @param flag
     * @param taskBean 务必验证传入的taskbean参数正确性
     * @param str      无可直接传null
     * @return 数据json
     */
    public String runTask(String taskName, HttpServletRequest request, int flag, TaskBean taskBean, String str) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //返回给定字符串名的类 Class 对象
        //并创建此 Class 对象所表示的类的一个新实例
        Class<?> object = Class.forName("com.util.tools.TaskBeanUtil");
        String test = null;
        if (null == str) {
            Method m = object.getMethod(taskName, new Class[]{HttpServletRequest.class, int.class, TaskBean.class});
            test = (String) m.invoke(object.newInstance(), new Object[]{request, flag, taskBean});
        } else {
            Method m = object.getMethod(taskName, new Class[]{HttpServletRequest.class, int.class, TaskBean.class, String.class});
            test = (String) m.invoke(object.newInstance(), new Object[]{request, flag, taskBean, str});
        }
        return "{\"taskId\":\"" + taskBean.getTaskId() + "\",\"rows\":" + test + "}";
    }

    /**
     * taskBean通用执行方法2  改版
     *
     * @param taskName TaskBeanUtil中的方法
     * @param request
     * @param flag
     * @param taskBean 务必验证传入的taskbean参数正确性
     * @param str      无可直接传null
     * @return 数据json
     */
    public String runTask2(String taskName, HttpServletRequest request, int flag, TaskBean taskBean, String str) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //返回给定字符串名的类 Class 对象
        //并创建此 Class 对象所表示的类的一个新实例
        Class<?> object = Class.forName("com.util.tools.TaskBeanUtil");
        String test = null;
        if (null == str) {
            Method m = object.getMethod(taskName, new Class[]{HttpServletRequest.class, int.class, TaskBean.class});
            test = (String) m.invoke(object.newInstance(), new Object[]{request, flag, taskBean});
        } else {
            Method m = object.getMethod(taskName, new Class[]{HttpServletRequest.class, int.class, TaskBean.class, String.class});
            test = (String) m.invoke(object.newInstance(), new Object[]{request, flag, taskBean, str});
        }
        return ToolUtil.getReturnResultForString(201, test);
    }

    public String runTask3(String taskName, HttpServletRequest request, int flag, TaskBean taskBean, String str) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //返回给定字符串名的类 Class 对象
        //并创建此 Class 对象所表示的类的一个新实例
        Class<?> object = Class.forName("com.util.tools.TaskBeanUtil");
        String test = null;
        if (null == str) {
            Method m = object.getMethod(taskName, new Class[]{HttpServletRequest.class, int.class, TaskBean.class});
            test = (String) m.invoke(object.newInstance(), new Object[]{request, flag, taskBean});
        } else {
            Method m = object.getMethod(taskName, new Class[]{HttpServletRequest.class, int.class, TaskBean.class, String.class});
            test = (String) m.invoke(object.newInstance(), new Object[]{request, flag, taskBean, str});
        }
        return test;
    }
}
