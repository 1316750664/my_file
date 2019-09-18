package com.service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by bangong on 2015/2/28.
 */
public class ExpressQueryService {

    private String url;

    private ExpressQueryService() {
    }

    public ExpressQueryService(String url) {
        this.url = url;
    }

    public String expressQuery() throws IOException {
        if (null == this.url) {
            return null;
        }
        URL url = new URL(this.url);
        InputStreamReader isr = new InputStreamReader(url.openStream(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }
}
