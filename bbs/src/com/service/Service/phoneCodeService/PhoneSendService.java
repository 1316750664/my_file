package com.service.Service.phoneCodeService;

import com.util.tools.CheckTools;
import com.util.tools.HttpRequestClientUtil;
import com.util.tools.ReadWriteProperties;

import java.net.URLEncoder;

/**
 * on 2014/12/6.
 */
public class PhoneSendService {
    private String url;
    private String usr;//用户名
    private String pwd;//加密密钥
    private String srcId;//接入号
    private String charset;

    private static class SingletonPhoneSendService {
        private static PhoneSendService INSTANCE = new PhoneSendService();
    }

    public static PhoneSendService getInstance() {
        return SingletonPhoneSendService.INSTANCE;
    }

    private PhoneSendService() {
        ReadWriteProperties rw = ReadWriteProperties.getInstance();
        url = rw.readValue("email", "mobile_url");
        usr = rw.readValue("email", "mobile_usr");
        pwd = rw.readValue("email", "mobile_pwd");
        srcId = rw.readValue("email", "mobile_srcid");
        charset = rw.readValue("email", "mobile_charset");
    }

    public boolean sendMessage(String mobile, String message) {
        boolean result = false;
        try {
            if (CheckTools.isNull(mobile)) {
                return false;
            }
            StringBuilder sb = new StringBuilder("");
            String[] mobiles = mobile.split(",");
            for (int i = 0; i < mobiles.length; i++) {
                if (CheckTools.checkMobile(mobiles[i])) {
                    sb.append(mobiles[i]).append(",");
                }
            }
            if (sb.length() == 0) {
                return false;
            }
            String recMobile = sb.toString();
            sb.delete(0, sb.length());
            String[] recMobiles = recMobile.split(",");
            int yzm = Integer.valueOf(recMobiles[0].substring(mobiles[0].length() - 4)) * 3 + Integer.parseInt(pwd);
            String sendUrl = url + "?usr=" + usr + "&srcid=" + srcId + "&mobile=" + recMobile + "&msg="
                    + URLEncoder.encode(message, charset) + "&yzm=" + yzm + "&tjpc=" + System.currentTimeMillis();
            String sendResult = HttpRequestClientUtil.doGet(sendUrl, null, charset);
            if (!CheckTools.isNull(sendResult)) {
                sendResult = sendResult.trim();
            }
            //System.out.println(result);
            if ("0".equals(sendResult)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        PhoneSendService.getInstance().sendMessage("13757692306", "浙江企商汇电子商务有限公司测试短信");
    }
}
