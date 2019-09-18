package com.bean;

import java.io.Serializable;

/**
 * Created by gfgh on 2014/9/10.
 */
public class PartsBrandBean implements Serializable {
    private String parts_brand_id = "";
    private String parts_brand_name = "";
    private Boolean checkFlg = false;

    public Boolean getCheckFlg() {
        return checkFlg;
    }

    public void setCheckFlg(Boolean checkFlg) {
        this.checkFlg = checkFlg;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String uri = "";

    public String getParts_brand_name() {
        return parts_brand_name;
    }

    public void setParts_brand_name(String parts_brand_name) {
        this.parts_brand_name = parts_brand_name;
    }

    public String getParts_brand_id() {
        return parts_brand_id;
    }

    public void setParts_brand_id(String parts_brand_id) {
        this.parts_brand_id = parts_brand_id;
    }


}
