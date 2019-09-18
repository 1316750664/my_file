package com.util.tools;

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-5
 * Time: 下午2:10
 * To change this template use File | Settings | File Templates.
 */
public final class ToolUtil {

    /**
     * 将浮点数无效的尾零去掉
     *
     * @param str
     * @return
     */
    public static String deleteZeroAndDot(String str) {
        if (CheckTools.isNull(str)) {
            str = "0";
        }
        int dot = str.indexOf(".");
        if (dot != -1) {
            str = str.replaceAll("0+?$", "").replaceAll("[.]$", "");
        }
        return str;
    }

    /**
     * 四舍五入取整 如0.01返回1.0
     *
     * @param Str 要进行操作的数据
     * @return float
     */
    public static float getMathDecimal(String Str) {
        BigDecimal decimal = new BigDecimal(Str);
        return (decimal.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue()) * 100;
    }

    /**
     * 四舍五入并保留指定位置的小数
     *
     * @param num
     * @param scale
     * @return
     */
    public static String getMathDecimal(double num, int scale) {
        String format = "";
        for (int i = 0; i < scale; i++) {
            format += "0";
        }
        if (scale > 0) {
            format = "##0." + format;
        } else {
            format = "##0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(num);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, round_mode).doubleValue();
    }


    /**
     * 字符串转换成数据库java.sql.Date型日期
     *
     * @param str
     * @return
     */
    public static Date stringToSqlDate(String str) {
        return Date.valueOf(str);
    }

    /**
     * 字符串转换成数据库java.sql.Timestamp型日期
     *
     * @param str
     * @return
     */
    public static Timestamp stringToSqlTimestamp(String str) {
        return Timestamp.valueOf(str);
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取客户端请求IP地址
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    ip = "127.0.0.1";
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) { //"***.***.***.***".length() = 15
            ip = ip.split(",")[0];
        }

        return ip;
    }

    public static long inet_aton(String ip) {
        if (CheckTools.isNull(ip)) {
            return 0;
        }
        String[] ips = ip.split("\\.");
        int len = ips.length;
        if (len != 4) {
            return 0;
        }
        long[] ints = new long[4];
        for (int i = 0; i < len; i++) {
            if (CheckTools.checkPositiveNum(ips[i])) {
                ints[i] = Long.parseLong(ips[i]);
            } else {
                return 0;
            }
        }
//        return ints[0] * 256 * 256 * 256 + ints[1] * 256 * 256 + ints[2] * 256 + ints[3];
        return (ints[0] << 24) + (ints[1] << 16) + (ints[2] << 8) + ints[3];
    }

    public static String inet_ntoa(long ip) {
        StringBuilder sb = new StringBuilder();
        sb.append((ip >> 24) & 0xff).append(".");
        sb.append((ip >> 16) & 0xff).append(".");
        sb.append((ip >> 8) & 0xff).append(".");
        sb.append(ip & 0xff);

        return sb.toString();
    }

    /**
     * 获取操作系统类型
     *
     * @return
     */
    public static String getOS() {
        Properties pros = System.getProperties();
        String osName = pros.getProperty("os.name");
        //System.out.println(osName);
        return osName;
    }

    /**
     * 将对象数组转换成按mapSeparator字符分隔的字符串
     *
     * @param array        对象数组
     * @param mapSeparator 分隔字符
     * @return
     */
    public static String arrayToString(Object[] array, char mapSeparator) {
        StringBuilder buf = new StringBuilder("");
        if (array != null) {
            int arrayLength = array.length;
            for (int i = 0; i < arrayLength; i++) {
                buf.append(array[i]);
                if (i < arrayLength - 1) {
                    buf.append(mapSeparator);
                }
            }
        }
        return buf.toString();
    }

    /**
     * 将对象数组转换成字符串数组
     *
     * @param objects
     * @return
     */
    public static String[] objectsToStrings(Object[] objects) {
        String[] strings = null;
        if (objects != null) {
            int arrayLength = objects.length;
            strings = new String[arrayLength];
            for (int i = 0; i < arrayLength; i++) {
                strings[i] = String.valueOf(objects[i]);
            }
        }
        return strings;
    }

    /**
     * 判断某个字符是否包含在数组中，返回相应数组下标，不包含返回-1
     *
     * @param subString
     * @param stringArray
     * @return
     */
    public static int arrayEquals(String subString, String[] stringArray) {
        int result = -1;
        if (stringArray != null && stringArray.length > 0) {
            for (int i = 0; i < stringArray.length; i++) {
                if (subString.equals(stringArray[i])) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 复制数组到集合
     *
     * @param arrays
     * @param collections
     * @param <T>
     */
    public static <T> void mergeArray(T[] arrays, Collection<T> collections) {
        if (arrays == null || collections == null) {
            return;
        }
        for (T e : arrays) {
            collections.add(e);
        }
    }

    /**
     * 转换初始大小为常见单位
     *
     * @param size
     * @return
     */
    public static String fileSize2string(long size) {
        final long unit = 1024;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        for (int i = 0; i < units.length; i++) {
            double d = getNumSize(size, i);
            if (d < unit) {
                return df.format(d) + units[i];
            }
        }
        return null;
    }

    //单位换算
    private static double getNumSize(long size, int n) {
        return size / Math.pow(1024, n);
    }

    /**
     * 按照中文三个字符，英文一个字符截取指定长度utf-8 gbk
     *
     * @param str
     * @param len
     */
    public static String subChineseString(String str, int len) {
        if (CheckTools.isNull(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder("");
        char ch;
        int k = 0;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (CheckTools.isChinese(String.valueOf(ch))) {
                k = k + 3;
            } else {
                k = k + 1;
            }
            if (k >= len) {
                break;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * select未取到返回的是空字符串不是null
     *
     * @param html
     * @param jq
     * @return
     */
    public static List<String> getHtmlElements(String html, String jq) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select(jq);
//		System.out.println("".equals(elements.toString())+":"+elements.size());
        if (elements == null || elements.size() == 0) {
            return null;
        }
        List<String> list = new LinkedList<String>();
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element == null) {
                continue;
            }
            list.add(element.toString());
        }
        return list;
    }

    public static String getHtmlAttributes(String html, String jq, String attr) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select(jq);
        if (elements != null && elements.size() > 0) {
            Element element = elements.first();
            if (element.hasAttr(attr)) {
                return element.attr(attr);
            }
        }
        return null;
    }

    /**
     * 获取html中的img标签中的图片路径
     *
     * @param html
     * @return
     */
    public static List<String> getHtmlImgSrc(String html) {
        List<String> picList = new ArrayList<String>();
        if (!CheckTools.isNull(html)) {
            String imgReg = "<img.*src=(.*?)[^>]*?>";
            String srcReg = "src=\"?(.*?)(\"|>|\\s+)";
            Pattern p_image = Pattern.compile(imgReg, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(html);
            Pattern p_src = Pattern.compile(srcReg);
            while (m_image.find()) {
                Matcher m_src = p_src.matcher(m_image.group());
                while (m_src.find()) {
                    picList.add(m_src.group(1));
                }
            }
        }
        return picList;
    }

    public static String replaceImgSrc(String html, String replaceHttp, String replaceFlag) {
        if (CheckTools.isNull(html) || CheckTools.isNull(replaceHttp)) {
            return html;
        }
        if (!"/".equals(replaceHttp.substring(replaceHttp.length() - 1))) {
            replaceHttp += "/";
        }
        String imgReg = "<img.*src=(.*?)[^>]*?>";
        String srcReg = "src=\"?(.*?)(\"|>|\\s+)";
        Pattern p_image = Pattern.compile(imgReg, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(html);
        Pattern p_src = Pattern.compile(srcReg);
        while (m_image.find()) {
            Matcher m_src = p_src.matcher(m_image.group());
            while (m_src.find()) {
                String temp = m_src.group(1);
                String filename = temp.substring(temp.lastIndexOf("/") + 1);
                if (CheckTools.isNull(replaceFlag)) {//所有的均要替换
                    html = html.replace(temp, replaceHttp + filename);
                } else if (temp.indexOf(replaceFlag) > -1) {
                    html = html.replace(temp, replaceHttp + filename);
                }
            }
        }
        return html;
    }

    public static String killHtml(String in) {
        char c;
        StringBuilder out = new StringBuilder();
        for (int i = 0; (in != null) && (i < in.length()); i++) {
            c = in.charAt(i);
            if (c == '\'')
                out.append("#apos;");
            else if (c == '\"')
                out.append("#quot;");
            else if (c == '<')
                out.append("＜");
            else if (c == '>')
                out.append("＞");
            else if (c == '=')
                out.append("#3D;");
            else if (c == '\r')
                out.append("#0D;");
            else if (c == '\n')
                out.append("#0A;");
            else
                out.append(c);
        }
        return out.toString();
    }

    public static String unKillHtml(String s) {
        s = s.replace("#apos;", "\'");
        s = s.replace("#quot;", "\"");
        s = s.replace("＜", "<");
        s = s.replace("＞", ">");
        s = s.replace("#3D;", "=");
        s = s.replace("#0D;", "\r");
        s = s.replace("#0A;", "\n");

        return s;
    }

    /**
     * 根据指定日期格式和流水编号规则生成流水号
     *
     * @param dateformatter
     * @param middleSymbol
     * @param zeros
     * @param sn
     * @return
     */
    public static String getFormatSn(String dateformatter, String middleSymbol, int zeros, int sn) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateformatter);
        String snDate = sdf.format(new Date(System.currentTimeMillis()));
        if (middleSymbol == null) {
            middleSymbol = "";
        }
        return snDate + middleSymbol + String.format("%0" + zeros + "d", sn);
    }

    public static String fillZeros(int zeros, long sn) {
        return String.format("%0" + zeros + "d", sn);
    }

    /**
     * 获取指定cookie的值
     *
     * @param cookies
     * @param cookieName
     * @return
     */
    public static String getRequestCookie(Cookie[] cookies, String cookieName) {
        String cookieValue = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }

    public static JavaType getCollectionType(ObjectMapper objectMapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    //对标题进行分词
    public static List<String> titleDic(String title) throws Exception {
        List<String> list = new ArrayList<String>();
        StringReader reader = new StringReader(title);
        IKSegmenter ikSegmenter = new IKSegmenter(reader, true);
        Lexeme lexeme = null;
        while ((lexeme = ikSegmenter.next()) != null) {
            list.add(lexeme.getLexemeText());
        }
        return list;
    }

    /**
     * 文件的读
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFile(String path) throws IOException {
        path = java.net.URLDecoder.decode(path, "UTF-8");
        File file = new File(path);
        if (!file.exists())
            return null;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }

    /**
     * 文件的写
     *
     * @param content
     * @param path
     * @return
     * @throws IOException
     */
    public static int writeFile(String content, String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            return 0;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

        bw.write(content);
        bw.close();
        return 1;
    }

    /**
     * @param str
     * @param pattern 1、前三星隐藏所有，显示后三位，2、中间四位星，前三，后四显示
     *                3、隐藏身份证出生月日4位
     * @return
     */
    public static String hidePhone(String str, int pattern) {
        if (str == null) {
            return "";
        }
        int len = str.length();
        String result = null;
        if (1 == pattern) {
            if (len < 3) {
                result = "***" + str;
            } else {
                result = "***" + str.substring(len - 3);
            }
        } else if (2 == pattern) {
            if (len < 4) {
                result = str.substring(0, len) + "****" + str.substring(0, len);
            } else {
                result = str.substring(0, 4) + "****" + str.substring(len - 4);
            }
        } else if (3 == pattern) {
            if (len < 10) {
                result = str.substring(0, len) + "****" + str.substring(0, len);
            } else {
                if (15 == len) {
                    result = str.substring(0, 8) + "****" + str.substring(len - 3);
                } else {
                    result = str.substring(0, 10) + "****" + str.substring(len - 4);
                }
            }
        }
        if (result == null) {
            return "";
        } else {
            return result;
        }
    }

    /**
     * 缩略图名称生成器
     *
     * @param name 文件名
     * @param size 尺寸
     * @return
     */
    public static String thumbnailNameTran(String name, String size) {
        if (CheckTools.isNull(name)) {
            return "";
        }
        if (name.indexOf("group") == -1) {
            return "pro_pic.png";
        }
        String file_ext_name = name.substring(name.lastIndexOf("."));
        name = name.replace(file_ext_name, "_" + size) + file_ext_name;
        return name;
    }

    /** 4294967295最大值
     * bbs积分转换
     * @param points
     * @return
     */
    public static String pointChinese(String points, ReadWriteProperties rwp, ObjectMapper objectMapper) {
        try {
            long jiFen = Long.parseLong(points);
            String jiFenJson = rwp.readValue("config", "creditJson");
            List<Map<String, String>> list = objectMapper.readValue(jiFenJson, List.class);
            int i = 1;
            for (Map<String, String> map : list) {// cre lv
                String credit = map.get("cre");
                long cre = Long.parseLong(credit);
                if (jiFen < cre) {
                    return map.get("lv");
                }else if (i == list.size()) {//比较个数到达上限
                    return map.get("lv");
                }
                i++;
            }
        } catch (Exception e) {
            return "无该等级";
        }
        return "无该等级";
    }
    /**
     * 功能：设置地区编码(后台身份证验证条件)
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
    /**
     * 功能：判断字符串是否为数字(后台身份证验证条件)
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 功能：判断字符串是否为日期格式(后台身份证验证条件)
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 身份证验证
     * @param IDStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String IDCardValidate(String IDStr)  {
        String errorInfo = "";// 记录错误信息
        try {
            String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                    "3", "2"};
            String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                    "9", "10", "5", "8", "4", "2"};
            String Ai = "";
            // ================ 号码的长度 15位或18位 ================
            if (IDStr.length() != 15 && IDStr.length() != 18) {
                errorInfo = "身份证号码长度应该为15位或18位。";
                return errorInfo;
            }
            // =======================(end)========================

            // ================ 数字 除最后以为都为数字 ================
            if (IDStr.length() == 18) {
                Ai = IDStr.substring(0, 17);
            } else if (IDStr.length() == 15) {
                Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
            }
            if (isNumeric(Ai) == false) {
                errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
                return errorInfo;
            }
            // =======================(end)========================

            // ================ 出生年月是否有效 ================
            String strYear = Ai.substring(6, 10);// 年份
            String strMonth = Ai.substring(10, 12);// 月份
            String strDay = Ai.substring(12, 14);// 月份
            if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
                errorInfo = "身份证生日无效。";
                return errorInfo;
            }
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                        || (gc.getTime().getTime() - s.parse(
                        strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                    errorInfo = "身份证生日不在有效范围。";
                    return errorInfo;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                errorInfo = "身份证月份无效";
                return errorInfo;
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                errorInfo = "身份证日期无效";
                return errorInfo;
            }
            // =====================(end)=====================

            // ================ 地区码时候有效 ================
            Hashtable h = GetAreaCode();
            if (h.get(Ai.substring(0, 2)) == null) {
                errorInfo = "身份证地区编码错误。";
                return errorInfo;
            }
            // ==============================================

            // ================ 判断最后一位的值 ================
            int TotalmulAiWi = 0;
            for (int i = 0; i < 17; i++) {
                TotalmulAiWi = TotalmulAiWi
                        + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                        * Integer.parseInt(Wi[i]);
            }
            int modValue = TotalmulAiWi % 11;
            String strVerifyCode = ValCodeArr[modValue];
            Ai = Ai + strVerifyCode;

            if (IDStr.length() == 18) {
                if (Ai.equals(IDStr) == false) {
                    errorInfo = "身份证无效，不是合法的身份证号码";
                    return errorInfo;
                }
            } else {
                return "";
            }
        }catch (Exception e ){
            return "身份证无效";
        }
        // =====================(end)=====================
        return "";
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber){
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^((((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * bbs替换时间模版
     * @param json
     * @param timeReg
     * @return
     */
    public static String bbsReplaceTime(String json, String timeReg){
        SimpleDateFormat sdf2 = new SimpleDateFormat(timeReg);
        Pattern pattern = Pattern.compile("###time:(.*?)###");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String str1 = matcher.group();//匹配部分
            String str = matcher.group(1);//匹配的值
            long shujushijian = Long.parseLong(str);
            str = sdf2.format(new Date(shujushijian));
            json = json.replaceFirst(str1,str);
        }
        return json;
    }

    /**
     * bbs替换图片
     * @param serverCache
     * @param localImg
     * @param main_img
     * @param text
     * @return
     */
    public static String bbsReplaceImg(String serverCache,String localImg,String main_img,String text){
        Pattern pattern = Pattern.compile("###src###");
        Matcher matcher = pattern.matcher(text);
        int j = 0;
        String[] maps = main_img.split(",");
        String img = localImg;
        while (matcher.find()) {
            String str1 = matcher.group();//匹配部分
            if(maps[j].equals("")){//没图用默认图

            }else{
                img = serverCache+maps[j];
            }
            text = text.replaceFirst(str1,img);
            if(j<maps.length){j++;}
        }
        return text;
    }

    /**
     * @param str 1、两位，显示第一个，2、取两头，隐藏中间所有
     * @return
     */
    public static String hideName(String str) {
        if (str == null || "".equals(str)) {
            return "x***x";
        }
        int len = str.length();
        String result = null;
        if (len <= 2) {
            result = str.substring(0, 1) + "***";
        } else {
            result = str.substring(0, 1) + "***" + str.substring(len - 1, len);
        }
        if (null == result) {
            return "x***x";
        } else {
            return result;
        }
    }

    /**
     * @param code
     * @param value
     * @return
     */
    public static String getReturnResultForString(int code,String value){
        return getRes(code,StatuCode.getMessage(code),value);
    }

    public static String getReturnResultForString(int code,String msg,String value){
        return getRes(code,msg,value);
    }

    private static String getRes(int code,String msg,String value){
        StringBuilder sb = new StringBuilder();

        sb.append("{\"error\":\""+code+"\",\"msg\":\""+msg+"\"");
        if(code!=201){
            sb.append("}");
        }else{
            sb.append(",\"data\":"+value+"}");
        }
        return sb.toString();
    }

    /**
     *
     * @param statuCode
     * @param
     * @param value
     * @return
     */
    public static String getReturnResultForString(StatuCode statuCode,String value){
        return getRes(statuCode.getStatusCode(),statuCode.getStatusMsg(),value);
    }

    /**
     *
     * @param statuCode

     * @param list
     * @return  可直接传入list对象
     */
    public static String getReturnResultForList(StatuCode statuCode,List list){
        String value = "";
        try {
            value = StaticUtil.objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"500\",\"msg\":\"系统出错\"}";
        }
        return getReturnResultForString(statuCode,value);
    }
}