package com.service.Service.Bbs;

import com.util.tools.StaticUtil;
import com.util.tools.ToolUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bangong on 2015/4/30.
 */
public class BbsMcenterLeftService {
    public static BbsMcenterLeftService getInstance() {
        return SingletonBbsMcenterLeftService.INSTANCE;
    }

    private static class SingletonBbsMcenterLeftService {
        private static BbsMcenterLeftService INSTANCE = new BbsMcenterLeftService();
    }

    private BbsMcenterLeftService() {

    }
    public String makeLeft(String fragPath,String tou, String nick, String bei){
        if (tou.equals("")) {
            tou = StaticUtil.properties.readValue("config", "user_img");
        } else {
            String serverCache = StaticUtil.properties.readValue("config", "server");
            tou = serverCache + tou;
        }
        if (nick.equals("")) {
            nick = "无昵称";
        }
        if (bei.equals("")) {
            bei = "此人啥都没留";
        }
        try {
            String txt = ToolUtil.readFile(fragPath);
            txt = patternMatch(txt,"###tou###",tou);
            txt = patternMatch(txt,"###nick###",nick);
            txt = patternMatch(txt,"###bei###",bei);
            return txt;
        } catch (IOException e) {
//            e.printStackTrace();
            return "左侧加载出错";
        }
    }
    private String patternMatch(String txt, String reg, String nei){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(txt);
        String str1 = null;
        while (matcher.find()) {
            str1 = matcher.group();//匹配部分，最后替换掉
            txt = txt.replaceFirst(str1, nei);
        }
        return txt;
    }
}
