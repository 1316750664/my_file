package com.service.Service.CartService;

import com.bean.OrderSellBean;
import com.bean.TaskBean;
import com.business.TaskHandler;
import com.service.clazz.MainJsonService;
import com.util.tools.StaticUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hty070503 on 2014/10/30.
 */
public class CartService {
    public static CartService getInstance() {
        return SingletonCartService.INSTANCE;
    }

    private static class SingletonCartService {
        private static CartService INSTANCE = new CartService();
    }

    private CartService() {
    }

    private static MainJsonService mainJsonService = new MainJsonService();

    //创建购物车
    public int createCart(HttpServletResponse response, String uid) {

        String json = getCartJson(uid);
        if (json != null) {
            try {
                json = java.net.URLEncoder.encode(json, "UTF-8");//转码
            } catch (Exception e) {
                return 1;
            }

        } else {
            json = "{\"error\":\"0\",\"total_num\":\"0\"}";
        }
        Cookie cookie_cart = new Cookie("cookie_cart", json);
        cookie_cart.setPath("/");
        response.addCookie(cookie_cart);

        return 0;

    }

    /**
     * @param uid 为头部购物车提供
     * @return
     */
    public String getCartJson(String uid) {
        List<Map<String, String>> goods = getCart(uid);

        return headJson(goods);
    }

    public String headJson(List<Map<String, String>> goods) {
        int num = 0;
        if (goods==null||(num=goods.size()) == 0) {
            return "{\"error\":\"0\",\"total_num\":\"0\"}";
        }

        //json格式{"error":"0","total_num":"8","goods":[{"gn":"a"},{"gn":"b"}]}
        StringBuffer sb = new StringBuffer("{\"error\":\"0\",\"total_num\":\"" + num + "\",\"goods\":");
        if (num > 5) {        //只存5件商品否则cookie会失效
            num = 5;
        }
        List<Map<String, String>> cart_goods = new ArrayList<Map<String, String>>();
        for (int i = (num - 1); i >= 0; i--) {
            cart_goods.add(goods.get(i));
        }
        try {
            String goods_json = StaticUtil.objectMapper.writeValueAsString(cart_goods);
            sb.append(goods_json + "}");
        } catch (Exception e) {
            return "{\"error\":\"0\",\"total_num\":\"0\"}";
        }
        return sb.toString();
    }

    /**
     * 统一执行查询新购物车   保证数据一致性,及时
     *
     * @param paramMap
     * @return
     */
    public String getCartRs(Map<String, String[]> paramMap) {
        paramMap.put("taskId_2", new String[]{"111"});
        paramMap.put("cmdType_2", new String[]{"Query"});
        String rs = null;
        List<Map<String, String>> godds;
        try {
            rs = mainJsonService.invoke(paramMap);
            List<Map> list = StaticUtil.objectMapper.readValue(rs, List.class);
            Map map1;
            if (list.get(0).get("taskId").equals("111")) {
                map1 = list.get(0);
            } else {
                map1 = list.get(1);
            }
            godds = (List<Map<String, String>>) map1.get("rows");
            godds = merge2(godds);//合并重复
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"300\"}";
        }
        return headJson(godds);
    }

    //清空购物车
    public String delAllMyCart(String uid) {
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("taskId_1", new String[]{"132"});
        paramMap.put("cmdType_1", new String[]{"Update"});
        paramMap.put("user_id", new String[]{uid});
        return getCartRs(paramMap);
    }

    //产品添加
    public String addGoodsInCart(List<Map<String, String>> listMap, String uid) {
        TaskBean taskBean = null;
        String rs = null;
        List<TaskBean> list = new ArrayList<TaskBean>();
        if (listMap.size() > 0) {
            for (Map<String, String> map : listMap) {
                String num = map.get("num");
                String price = map.get("price");
                if (num == null || "".equals(num)) {
                    num = "1";
                }
                if (price == null || "".equals(price)) {
                    price = "0";
                }
                taskBean = new TaskBean(133, 2, 1, new String[]{uid, num, price, map.get("property_code"), map.get("property_json"), map.get("goods_id")}, new String[]{"number", "number", "string", "string", "string", "number"});
                list.add(taskBean);
            }
        }
        try {
            rs = TaskHandler.getInstance().handler(0, list);
        } catch (Exception e) {
            return null;
        }
        return rs;
    }

    //产品添加  返回新购物车数据
    public String addGoodsInCart(Map<String, String> map, String uid) {
        String num = map.get("num");
        String price = map.get("price");
        if (num == null || "".equals(num)) {
            num = "1";
        }
        if (price == null || "".equals(price)) {
            price = "0";
        }
        String goods_id = map.get("goods_id");
        String car_property = map.get("car_property");
        String property_code = map.get("property_code");
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("taskId_1", new String[]{"133"});
        paramMap.put("user_id", new String[]{uid});
        paramMap.put("num", new String[]{num});
        paramMap.put("price", new String[]{price});
        paramMap.put("property_code", new String[]{property_code});
        paramMap.put("property_json", new String[]{map.get("property_json")});
        paramMap.put("car_property", new String[]{car_property});
        paramMap.put("goods_id", new String[]{goods_id});
        paramMap.put("cmdType_1", new String[]{"Update"});
        //根据goods_id property_code car_property生成一个hashcode
        String hashString = goods_id + car_property + property_code;
        paramMap.put("cart_hashcode", new String[]{hashString.hashCode() + ""});
        paramMap.put("cart_uuid", new String[]{UUID.randomUUID().toString()});//作为唯一标识

        return getCartRs(paramMap);
    }

    //购物车产品删除
    public String delGoodsInCart(List<Map<String, String>> listMap, String uid) {
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> map : listMap) {
            sb.append("'" + map.get("cart_uuid") + "'").append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("taskId_1", new String[]{"131"});
        paramMap.put("user_id", new String[]{uid});
        paramMap.put("cart_uuid", new String[]{sb.toString()});
        paramMap.put("cmdType_1", new String[]{"Update"});
        return getCartRs(paramMap);
    }

    //查询
    public List<Map<String, String>> getCart(String uid) {
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("taskId", new String[]{"111"});
        paramMap.put("user_id", new String[]{uid});
        paramMap.put("cmdType", new String[]{"Query"});
        String json = null;
        Map map1 = null;
        List<Map<String, String>> godds = null;
        try {
            json = mainJsonService.invoke(paramMap);
            List<Map> list = StaticUtil.objectMapper.readValue(json, List.class);
            map1 = list.get(0);
            if (!map1.containsKey("taskId")) {
                return null;
            }
            godds = (List<Map<String, String>>) map1.get("rows");
        } catch (Exception e) {
            return null;
        }

        return merge2(godds);
    }

    //通过hashcode进行分辨合并
    public List<Map<String, String>> merge2(List<Map<String, String>> godds) {
        List<Map<String, String>> new_list = new ArrayList<Map<String, String>>();
        int statu = 0;
        for (Map<String, String> map3 : godds) {
            statu = 0;
            String cart_hashcode = map3.get("cart_hashcode");
            String num = map3.get("goods_num");
            for (Map<String, String> map2 : new_list) {
                if (cart_hashcode.equals(map2.get("cart_hashcode"))) {
                    //合并
                    map2.put("goods_num", Integer.parseInt(num) + Integer.parseInt(map2.get("goods_num")) + "");
                    statu = 1;
                    break;
                }
            }
            if (statu == 0) {
                new_list.add(map3);
            }
        }

        return new_list;

    }

    /* public List<Map<String, String>> merge(List<Map<String, String>> godds){
         List<Map<String, String>> new_list = new ArrayList<Map<String, String>>();
         int statu = 0;
         for(Map<String,String> map3 : godds){
             statu = 0;
             String user_id = map3.get("user_id");
             String goods_id = map3.get("goods_id");
             String property_code = map3.get("property_code");
             String num = map3.get("goods_num");
             for(Map<String,String> map2 : new_list){

                 if (user_id.equals(map2.get("user_id")) && goods_id.equals(map2.get("goods_id")) && property_code.equals(map2.get("property_code"))) {
                     //合并
                     map2.put("goods_num", Integer.parseInt(num) + Integer.parseInt(map2.get("goods_num")) + "");
                     statu = 1;
                     break;
                 }
             }
             if (statu == 0) {
                 new_list.add(map3);
             }
         }

         return new_list;
     }

     /**
      * @param uid 购物车页面专用
      * @return
      */
    public String getAllCartJson(String uid) {
        List<Map<String, String>> goods = getCart(uid);
        if (goods.size() <= 0) {
            return null;
        }
        return total_cart_json(goods);
    }

    /**
     * @param godds 此处为所有购物车产品分好类之后的json
     * @return
     */
    public String total_cart_json(List<Map<String, String>> godds) {
        StringBuffer sb = new StringBuffer("[");
        HashSet<OrderSellBean> hs = new HashSet<OrderSellBean>();

        try {
            //根据商家分好类
            OrderSellBean orderSellBean = null;
            for (Map<String, String> map : godds) {

                orderSellBean = new OrderSellBean(map);

                if (!hs.contains(orderSellBean)) {
                    hs.add(orderSellBean);
                } else {
                    Iterator<OrderSellBean> it = hs.iterator();
                    while (it.hasNext()) {
                        OrderSellBean o = it.next();
                        if (o.equals(orderSellBean)) {
                            o.addGoods(map);
                        }
                    }
                }
            }
        } catch (IOException e) {
            return null;
        }
        //分好类封成json
        for (OrderSellBean o : hs) {
            sb.append("{\"sell_id\":\"" + o.getSellerId() + "\",\"sell_name\":\"" + o.getSellerName() + "\",\"goods\":" + o.getGoodsJson() + "}").append(",");
        }
        if (!sb.toString().equals("{")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");

        return sb.toString();

    }
}
