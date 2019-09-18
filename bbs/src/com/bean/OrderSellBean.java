package com.bean;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hty070503 on 2014/11/7.
 */
public class OrderSellBean implements Serializable {

    private ObjectMapper objectMapper = new ObjectMapper();
    private String sellerId;
    private String sellerName;
    private String goodsJson;
    private List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

    public OrderSellBean(String sellerId, String sellerName, String goodsJson) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.goodsJson = goodsJson;
    }

    public OrderSellBean() {
    }

    public OrderSellBean(Map<String, String> map) throws IOException {
        this.sellerId = map.get("seller_user_id");
        this.sellerName = map.get("company");
        this.addGoods(map);
    }


    public List<Map<String, String>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, String>> listMap) {
        this.listMap = listMap;
    }

    public void mergeGoods(List<Map<String, String>> lm) {
        for (Map<String, String> map : lm) {
            this.addGoods(map);
        }
    }

    public void addGoods(Map<String, String> goods) {
       /* //判断是否有该商品
        if (listMap.size() != 0) {
            for (Map<String, String> map : listMap) {
                if (map.get("goods_id").equals(goods.get("goods_id")) && map.get("property_code").equals(goods.get("property_code"))) {
                    map.put("goods_num", (Integer.parseInt(map.get("goods_num")) + Integer.parseInt(goods.get("goods_num"))) + "");
                    return;
                }
            }
        }*/

        listMap.add(goods);
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getGoodsJson() {

        if (listMap.size() <= 0)
            return "[]";
        try {
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
            objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.goodsJson = objectMapper.writeValueAsString(listMap);
        } catch (IOException e) {
            goodsJson = "[]";
        }
        return goodsJson;
    }

    public void setGoodsJson(String goodsJson) {
        this.goodsJson = goodsJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderSellBean that = (OrderSellBean) o;

        if (sellerId != null ? !sellerId.equals(that.sellerId) : that.sellerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sellerId != null ? sellerId.hashCode() : 0;
    }
}
