package com.service.Service.costCalculation;

import com.bean.ExpressBean;
import com.bean.GoodsBean;
import com.service.clazz.MainJsonService;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hty070503 on 2014/10/31.
 * 主要负责各类价格 邮费等 优惠计算
 */
public class Calculation {


    public static Calculation getInstance() {
        return SingletonCartService.INSTANCE;
    }

    private static class SingletonCartService {
        private static Calculation INSTANCE = new Calculation();
    }

    private Calculation() {

    }

    /**
     * cityid 收货地区id省市区用逗号隔开
     * list<map<String,String>>订单产品信息 freight_id模板id num商品数量 weight重量 volume体积 price价格
     * 基本规则  取单件商品最高邮费 加件按个件商品的叠加规则加收费用
     */
    //邮费计算
    public Map<String, String> getExpressFee(String cityid, List<Map<String, String>> listMap) throws NoSuchMethodException, IllegalAccessException, IOException, SQLException, InvocationTargetException {

        HashSet<ExpressBean> new_listMap = Merge(listMap);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String ids = getStreightids(new_listMap);
        //一次性把相关的模板信息全查出来  防止连接过多
        String json = query(new String[]{"112", "113", "114", "115"}, new String[]{"freight_id1", "freight_id2", "freight_id3", "freight_id4"}, new String[]{ids.toString(), ids.toString(), ids.toString(), ids.toString()});
        List<Map<String, List<Map<String, String>>>> list = objectMapper.readValue(json, List.class);
        List<Map<String, String>> listResult1 = list.get(0).get("rows");//tb_web_freight
        List<Map<String, String>> listResult2 = list.get(1).get("rows");//tb_web_freight_city
        List<Map<String, String>> listResult3 = list.get(2).get("rows");//tb_web_freight_city_free
        List<Map<String, String>> listResult4 = list.get(3).get("rows");//tb_web_freight_type

        ExpressBean eb = null;

        for (Map<String, String> map : listResult1) {
            String freight_id = map.get("freight_id");
            String deliverType = map.get("is_express") + "," + map.get("is_ems") + "," + map.get("is_post");
            eb = getBean(freight_id, new_listMap);
            eb.setCharge_type(Integer.parseInt(map.get("charge_type")));
            //判断是否包邮
            if (map.get("is_free_post").equals("1")) {
                //直接设置续重费为0 首重费0
                setFreeExpress(eb, deliverType);
            } else if(map.get("is_free_post").equals("0")){
                //先从type默认模板取运费
                setDefaultFee(listResult4, eb, freight_id);
                //是否是指定包邮
                if (map.get("is_designated").equals("1")) {
                    //指定包邮 取指定包邮数据  判断符合那种指定类型
                    setCityFree(listResult3, eb, cityid, freight_id, deliverType);

                } else {
                    //是否有指定城市 有就重写默认邮费
                    setCityFee(listResult2, eb, cityid, freight_id);
                }
            }
            this.Update(eb, new_listMap);//更新

        }


        return getCountFee(new_listMap, null);
    }

    public Map<String, String> getExpressFee(String cityid, List<Map<String, String>> listMap, String dType) throws NoSuchMethodException, IllegalAccessException, IOException, SQLException, InvocationTargetException {

        HashSet<ExpressBean> new_listMap = Merge(listMap);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String ids = getStreightids(new_listMap);
        //一次性把相关的模板信息全查出来  防止连接过多
        String json = query(new String[]{"112", "113", "114", "115"}, new String[]{"freight_id1", "freight_id2", "freight_id3", "freight_id4"}, new String[]{ids.toString(), ids.toString(), ids.toString(), ids.toString()});
        List<Map<String, List<Map<String, String>>>> list = objectMapper.readValue(json, List.class);
        List<Map<String, String>> listResult1 = list.get(0).get("rows");//tb_web_freight
        List<Map<String, String>> listResult2 = list.get(1).get("rows");//tb_web_freight_city
        List<Map<String, String>> listResult3 = list.get(2).get("rows");//tb_web_freight_city_free
        List<Map<String, String>> listResult4 = list.get(3).get("rows");//tb_web_freight_type

        ExpressBean eb = null;

        for (Map<String, String> map : listResult1) {
            String freight_id = map.get("freight_id");
            String deliverType = map.get("is_express") + "," + map.get("is_ems") + "," + map.get("is_post");
            eb = getBean(freight_id, new_listMap);
            eb.setCharge_type(Integer.parseInt(map.get("charge_type")));
            //判断是否包邮
            if (map.get("is_free_post").equals("1")) {
                //直接设置续重费为0 首重费0
                setFreeExpress(eb, deliverType);
            } else if(map.get("is_free_post").equals("0")){
                //先从type默认模板取运费
                setDefaultFee(listResult4, eb, freight_id);
                //是否是指定包邮
                if (map.get("is_designated").equals("1")) {
                    //指定包邮 取指定包邮数据  判断符合那种指定类型
                    setCityFree(listResult3, eb, cityid, freight_id, deliverType);

                } else {
                    //是否有指定城市 有就重写默认邮费
                    setCityFee(listResult2, eb, cityid, freight_id);
                }
            }
            this.Update(eb, new_listMap);//更新

        }


        return getCountFee(new_listMap, dType);
    }

    public Map<String, String> getExpressFee2(String cityid, List<GoodsBean> listGoods, String dType) throws NoSuchMethodException, IllegalAccessException, IOException, SQLException, InvocationTargetException {

        HashSet<ExpressBean> new_listMap = Merge2(listGoods);
        ObjectMapper objectMapper = new ObjectMapper();

        String ids = getStreightids(new_listMap);
        //一次性把相关的模板信息全查出来  防止连接过多
        String json = query(new String[]{"112", "113", "114", "115"}, new String[]{"freight_id1", "freight_id2", "freight_id3", "freight_id4"}, new String[]{ids.toString(), ids.toString(), ids.toString(), ids.toString()});
        List<Map<String, List<Map<String, String>>>> list = objectMapper.readValue(json, List.class);
        List<Map<String, String>> listResult1 = list.get(0).get("rows");//tb_web_freight
        List<Map<String, String>> listResult2 = list.get(1).get("rows");//tb_web_freight_city
        List<Map<String, String>> listResult3 = list.get(2).get("rows");//tb_web_freight_city_free
        List<Map<String, String>> listResult4 = list.get(3).get("rows");//tb_web_freight_type

        ExpressBean eb = null;

        for (Map<String, String> map : listResult1) {
            String freight_id = map.get("freight_id");
            String deliverType = map.get("is_express") + "," + map.get("is_ems") + "," + map.get("is_post");
            eb = getBean(freight_id, new_listMap);
            eb.setCharge_type(Integer.parseInt(map.get("charge_type")));
            //判断是否包邮
            if (map.get("is_free_post").equals("1")) {
                //直接设置续重费为0 首重费0
                setFreeExpress(eb, deliverType);
            } else if(map.get("is_free_post").equals("0")){
                //先从type默认模板取运费
                setDefaultFee(listResult4, eb, freight_id);
                //是否是指定包邮
                if (map.get("is_designated").equals("1")) {
                    //指定包邮 取指定包邮数据  判断符合那种指定类型
                    setCityFree(listResult3, eb, cityid, freight_id, deliverType);

                } else {
                    //是否有指定城市 有就重写默认邮费
                    setCityFee(listResult2, eb, cityid, freight_id);
                }
            }
            this.Update(eb, new_listMap);//更新

        }


        return getCountFee(new_listMap, dType);
    }

    //指定包邮
    public void setCityFree(List<Map<String, String>> listResult3, ExpressBean eb, String cityid, String freight_id, String deliverType) {
        for (Map<String, String> map2 : listResult3) {
            if (freight_id.equals(map2.get("freight_id"))) {
                if (containCity(map2.get("city_ids"), cityid)) {//地区条件已经符合(先判断地区是否包含，如不则判断省)
                    String charge_type = map2.get("charge_type");//0按件数 1按重量 2按体积
                    String statu = map2.get("designated");//0按件/按重/按积1按金额2两者同时
                    String numCondition = map2.get("first_condition");
                    String feeCondition = map2.get("second_contition");
                    int num = eb.getNum();//件数
                    double weight = eb.getWeight();//重量
                    double price = eb.getTotalPrice();//实际金额总价
                    double volume = eb.getVolume();//体积
                    double con = 0.00;//实际购买情况

                    if (charge_type.equals("0")) {
                        con = num;
                    } else if (charge_type.equals("1")) {
                        con = weight * num;
                    } else {
                        con = volume * num;
                    }

                    if (statu.equals("0") && Double.parseDouble(numCondition) <= con || (statu.equals("1") && price >= Double.parseDouble(feeCondition)) || (statu.equals("2") && Double.parseDouble(numCondition) <= con && price >= Double.parseDouble(feeCondition))) {
                        //直接设置续重费为0 首重费0
                        setFreeExpress(eb, deliverType);
                    }
                    //该产品指定包邮地区已符合则跳出循环
                    break;
                }
            }
        }
    }

    //指定城市邮费
    public void setCityFee(List<Map<String, String>> listResult2, ExpressBean eb, String cityid, String freight_id) {
        for (Map<String, String> map3 : listResult2) {
            if (map3.get("freight_id").equals(freight_id)) {
                if (containCity(map3.get("city_ids"), cityid)) {//地区条件已经符合 从指定城市模板取运费
                    switch (Integer.parseInt(map3.get("delivery_type"))) {
                        case 0:
                            eb.setExpress_firstFee(Double.parseDouble(map3.get("default_first_fee")));
                            eb.setExpress_addFee(Double.parseDouble(map3.get("default_second_fee")));
                            eb.setExpress_addWight(Double.parseDouble(map3.get("default_second_weight")));
                            eb.setExpress_Wight(Double.parseDouble(map3.get("default_first_weight")));
                            break;
                        case 1:
                            eb.setEMS_firstFee(Double.parseDouble(map3.get("default_first_fee")));
                            eb.setEMS_addFee(Double.parseDouble(map3.get("default_second_fee")));
                            eb.setEMS_addWight(Double.parseDouble(map3.get("default_second_weight")));
                            eb.setEMS_Wight(Double.parseDouble(map3.get("default_first_weight")));
                            break;
                        case 2:
                            eb.setMail_firstFee(Double.parseDouble(map3.get("default_first_fee")));
                            eb.setMail_addFee(Double.parseDouble(map3.get("default_second_fee")));
                            eb.setMail_addWight(Double.parseDouble(map3.get("default_second_weight")));
                            eb.setMail_Wight(Double.parseDouble(map3.get("default_first_weight")));
                            break;
                    }
                    //符合条件了就跳出循环
                    break;
                }
            }
        }
    }

    //默认运费
    public void setDefaultFee(List<Map<String, String>> listResult4, ExpressBean eb, String freight_id) {
        //先从type默认模板取运费
        for (Map<String, String> map4 : listResult4) {
            if (map4.get("freight_id").equals(freight_id)) {
                switch (Integer.parseInt(map4.get("delivery_type"))) {
                    case 0:
                        eb.setExpress_firstFee(Double.parseDouble(map4.get("default_first_fee")));
                        eb.setExpress_addFee(Double.parseDouble(map4.get("default_second_fee")));
                        eb.setExpress_Wight(Double.parseDouble(map4.get("default_first_weight")));
                        eb.setExpress_addWight(Double.parseDouble(map4.get("default_second_weight")));
                        break;
                    case 1:
                        eb.setEMS_firstFee(Double.parseDouble(map4.get("default_first_fee")));
                        eb.setEMS_addFee(Double.parseDouble(map4.get("default_second_fee")));
                        eb.setEMS_Wight(Double.parseDouble(map4.get("default_first_weight")));
                        eb.setEMS_addWight(Double.parseDouble(map4.get("default_second_weight")));
                        break;
                    case 2:
                        eb.setMail_firstFee(Double.parseDouble(map4.get("default_first_fee")));
                        eb.setMail_addFee(Double.parseDouble(map4.get("default_second_fee")));
                        eb.setMail_Wight(Double.parseDouble(map4.get("default_first_weight")));
                        eb.setMail_addWight(Double.parseDouble(map4.get("default_second_weight")));
                        break;
                }
            }
        }
    }

    //设置指定快递类型包邮
    private void setFreeExpress(ExpressBean eb, String deliverType) {
        String[] statu = deliverType.split(",");
        if (statu[0].equals("1")) {
            eb.setExpress_firstFee(0.00);
            eb.setExpress_addFee(0.00);
        } else if (statu[1].equals("1")) {
            eb.setEMS_addFee(0.00);
            eb.setEMS_firstFee(0.00);
        } else if (statu[2].equals("1")) {
            eb.setMail_addFee(0.00);
            eb.setMail_firstFee(0.00);
        }
    }


    private HashSet<ExpressBean> Merge(List<Map<String, String>> listMap) {
        //多个产品可能使用一个运费模板 合并map
        List<Map<String, String>> newListMap = new ArrayList<Map<String, String>>();
        HashSet<ExpressBean> hs = new HashSet<ExpressBean>();
        ExpressBean eb = null;
        String freight_id = null;
        Integer num = null;
        double weight = 0.00;
        double price = 0.00;
        double volume = 0.00;
        Map<String, String> nMap = null;
        for (Map<String, String> map : listMap) {
            freight_id = map.get("freight_id");
            String goods_num_s = map.get("goods_num");
            String weight_s = map.get("weight");
            String price_s = map.get("sale_price");
            String volume_s = map.get("volume");
            if (freight_id == null || "".equals(freight_id)) {
                freight_id = "0";
            }
            if (goods_num_s == null || "".equals(goods_num_s)) {
                num = 0;//件数
            } else {
                num = Integer.parseInt(goods_num_s);
            }
            if (weight_s == null || "".equals(weight_s)) {
                weight = 0;
            } else {
                weight = Double.parseDouble(weight_s);//重量
            }
            if (price_s == null || "".equals(price_s)) {
                price = 0;
            } else {
                price = Double.parseDouble(price_s);//单价
            }
            if (volume_s == null || "".equals(volume_s)) {
                volume = 0;
            } else {
                volume = Double.parseDouble(price_s);//体积
            }


            eb = new ExpressBean();
            eb.setStreight_id(freight_id);


            if (!hs.contains(eb)) {

                eb.setNum(num);
                eb.setTotalPrice(price * num);
                eb.setVolume(volume);
                eb.setWeight(weight);
                hs.add(eb);

            } else {
                hs.remove(eb);//移除 并重新放入更新后的
                price = eb.getTotalPrice() + num * price;
                num = num + eb.getNum();
                weight = weight + eb.getWeight();
                volume = volume + eb.getVolume();
                eb.setNum(num);
                eb.setTotalPrice(price * num);
                eb.setVolume(volume);
                eb.setWeight(weight);
                hs.add(eb);
            }
        }
        return hs;
    }

    private HashSet<ExpressBean> Merge2(List<GoodsBean> listMap) {
        //多个产品可能使用一个运费模板 合并map
        List<Map<String, String>> newListMap = new ArrayList<Map<String, String>>();
        HashSet<ExpressBean> hs = new HashSet<ExpressBean>();
        ExpressBean eb = null;
        String freight_id = null;
        Integer num = null;
        double weight = 0.00;
        double price = 0.00;
        double volume = 0.00;
        Map<String, String> nMap = null;
        for (GoodsBean goodsBean : listMap) {
            freight_id = goodsBean.getFreight_id();
            String goods_num_s = goodsBean.getGoods_num();
            String weight_s = goodsBean.getWeight();
            String price_s = goodsBean.getSale_price();
            String volume_s = goodsBean.getVolume();
            if (freight_id == null || "".equals(freight_id)) {
                freight_id = "0";
            }
            if (goods_num_s == null || "".equals(goods_num_s)) {
                num = 0;//件数
            } else {
                num = Integer.parseInt(goods_num_s);
            }
            if (weight_s == null || "".equals(weight_s)) {
                weight = 0;
            } else {
                weight = Double.parseDouble(weight_s);//重量
            }
            if (price_s == null || "".equals(price_s)) {
                price = 0;
            } else {
                price = Double.parseDouble(price_s);//单价
            }
            if (volume_s == null || "".equals(volume_s)) {
                volume = 0;
            } else {
                volume = Double.parseDouble(price_s);//体积
            }


            eb = new ExpressBean();
            eb.setStreight_id(freight_id);


            if (!hs.contains(eb)) {

                eb.setNum(num);
                eb.setTotalPrice(price * num);
                eb.setVolume(volume);
                eb.setWeight(weight);
                hs.add(eb);

            } else {
                hs.remove(eb);//移除 并重新放入更新后的
                price = eb.getTotalPrice() + num * price;
                num = num + eb.getNum();
                weight = weight + eb.getWeight();
                volume = volume + eb.getVolume();
                eb.setNum(num);
                eb.setTotalPrice(price * num);
                eb.setVolume(volume);
                eb.setWeight(weight);
                hs.add(eb);
            }
        }
        return hs;
    }


    private boolean containCity(String city_ids, String reciveid) {

        if (city_ids == null)
            return false;
        String[] ids = city_ids.split(",");
        String[] citys = reciveid.split(",");
        for (String c : citys) {
            for (String s : ids) {
                if (s.equals(c))
                    return true;
            }
        }
        return false;
    }

    /* //设置每件物品的总运费
     private void setCountFee(){
         double f = 0.00;
         double a = 0.00;
         int numbers = 1;
         double total = 0.00;
         ExpressBean eb = null;
         for(Iterator<ExpressBean> it=listMap.iterator();it.hasNext();)
         {
             eb = it.next();
              f = eb.getFirstFee();
              a = eb.getAddFee();
              numbers = eb.getNum();

             if(numbers>1)
                 total = f+a*(numbers-1);
             else
                 total = f;

             //保留后两位
             DecimalFormat df = new DecimalFormat("#.00");
             eb.setTotalFee(Double.parseDouble(df.format(total)));
         }
     }*/
    //返回总运费
    private Map<String, String> getCountFee(HashSet<ExpressBean> listMap, String deliverType) {
        ExpressBean ebFirst = getFirstWeight(listMap);
        double f1 = ebFirst.getExpress_firstFee();
        double a1 = 0.00;
        double f2 = ebFirst.getEMS_firstFee();
        double a2 = 0.00;
        double f3 = ebFirst.getMail_firstFee();
        double a3 = 0.00;
        int numbers = 1;
        double total1 = 0;
        double total2 = 0;
        double total3 = 0;
        double price1 = 0.00;
        double price2 = 0.00;
        double price3 = 0.00;
        ExpressBean eb = null;
        for (Iterator<ExpressBean> it = listMap.iterator(); it.hasNext(); ) {
            eb = it.next();
            if (eb.getStreight_id().equals("0")) {
                Map<String, String> map_null = new HashMap<String, String>();
                map_null.put("到付", "运费买家到付");
               // map_null.put("商议", "等待双方商议");
                return map_null;
            }
            // f1 = eb.getExpress_firstFee();
            a1 = eb.getExpress_addFee();
            // f2 = eb.getEMS_firstFee();
            a2 = eb.getEMS_addFee();
            //  f3 = eb.getMail_firstFee();
            a3 = eb.getMail_addFee();
            numbers = eb.getNum();


            //根据计费类型计算总费用
            switch (eb.getCharge_type()) {
                case 0:
                    if (numbers > eb.getExpress_Wight()) {//超重运费
                        price1 = a1 * (numbers - eb.getExpress_Wight());
                        price2 = a2 * (numbers - eb.getExpress_Wight());
                        price3 = a3 * (numbers - eb.getExpress_Wight());
                    }

                    break;
                case 1:
                    if (eb.getWeight() > eb.getExpress_Wight()) {
                        price1 = a1 * (eb.getWeight() - eb.getExpress_Wight());
                        price2 = a2 * (eb.getWeight() - eb.getExpress_Wight());
                        price3 = a3 * (eb.getWeight() - eb.getExpress_Wight());
                    } else {
                        price1 = a1 * (eb.getWeight() - eb.getExpress_Wight());
                        price2 = a2 * (eb.getWeight() - eb.getExpress_Wight());
                        price3 = a3 * (eb.getWeight() - eb.getExpress_Wight());
                    }

                    break;
                case 2:
                    if (eb.getVolume() > eb.getExpress_Wight()) {
                        price1 = a1 * (eb.getVolume() - eb.getExpress_Wight());
                        price2 = a2 * (eb.getVolume() - eb.getExpress_Wight());
                        price3 = a3 * (eb.getVolume() - eb.getExpress_Wight());
                    }

                    break;
            }


            total1 += price1;
            total2 += price2;
            total3 += price3;
        }
        //多商品只计算一次首重费用
        total1 += f1;
        total2 += f2;
        total3 += f3;
        //保留后两位
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, String> map = new HashMap<String, String>();
        if (deliverType != null) {
            if (deliverType.equals("0")) {
                map.put("fee", total1 + "");
            } else if (deliverType.equals("1")) {
                map.put("fee", total2 + "");
            } else if (deliverType.equals("2")) {
                map.put("fee", total3 + "");
            } else {
                //类型错误
            }

        } else {
            if (total1 == 0 && f1 != -1) {
                map.put("快递", "包邮");
            } else if (f1 != -1 && total1 > 0) {
                map.put("快递", df.format(total1) + "元");
            }
            if (f2 != -1 && total2 == 0) {
                map.put("EMS", "包邮");
            } else if (f2 != -1 && total2 > 0) {
                map.put("EMS", df.format(total2) + "元");
            }
            if (f3 != -1 && total3 == 0)
                map.put("平邮", "包邮");
            else if (f3 != -1 && total3 > 0) {
                map.put("平邮", df.format(total3) + "元");
            }

        }
        if (map == null || map.size() == 0) {
            Map<String, String> map_null = new HashMap<String, String>();
            map_null.put("到付", "运费买家到付");
           // map_null.put("商议", "等待双方商议");
            return map_null;
        }
        map.put("到付", "运费买家到付");

        return map;
    }

    //获取
    private ExpressBean getBean(String streight_id, HashSet<ExpressBean> listMap) {
        ExpressBean eb = null;
        for (Iterator<ExpressBean> it = listMap.iterator(); it.hasNext(); ) {
            eb = it.next();
            if (streight_id.equals(eb.getStreight_id())) {
                return eb;
            }
        }

        return null;
    }

    //更新
    private void Update(ExpressBean eb, HashSet<ExpressBean> listMap) {
        if (listMap.contains(eb)) {
            listMap.remove(eb);
            listMap.add(eb);
        }
    }

    //获取所有模板id
    private String getStreightids(HashSet<ExpressBean> listMap) {
        StringBuffer sb = new StringBuffer();
        for (Iterator<ExpressBean> it = listMap.iterator(); it.hasNext(); ) {
            sb.append(it.next().getStreight_id() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    public String query(String[] taskIds, String[] paramName, String[] param) throws NoSuchMethodException, IllegalAccessException, IOException, SQLException, InvocationTargetException {
        MainJsonService mainJsonService = new MainJsonService();
        Map<String, String[]> paramMap = new HashMap<String, String[]>();

        for (int i = 0; i < paramName.length; i++) {
            paramMap.put("taskId_" + (i + 1), new String[]{taskIds[i]});
            paramMap.put(paramName[i], new String[]{param[i]});
            paramMap.put("cmdType_" + (i + 1), new String[]{"Query"});
        }

        return mainJsonService.invoke(paramMap);
    }

    //使用排序 返回最高的首重邮费对象
    private ExpressBean getFirstWeight(HashSet<ExpressBean> listMap) {
        double temp = 0;
        double temp1 = 0;
        double temp2 = 0;
        double express_f = -1;
        double ems_f = -1;
        double email_f = -1;
        ExpressBean eb = null;
        ExpressBean eb1 = new ExpressBean();
        for (Iterator<ExpressBean> it = listMap.iterator(); it.hasNext(); ) {
            eb = it.next();
            eb1 = eb;
            express_f = eb.getExpress_firstFee();
            if (express_f >= temp) {
                temp = express_f;
                eb1.setExpress_firstFee(temp);
            }

            ems_f = eb.getEMS_firstFee();
            if (express_f >= temp1) {
                temp1 = ems_f;
                eb1.setEMS_firstFee(temp1);
            }

            email_f = eb.getMail_firstFee();
            if (express_f >= temp2) {
                temp2 = email_f;
                eb1.setMail_firstFee(temp2);
            }

        }

        return eb1;
    }


}
