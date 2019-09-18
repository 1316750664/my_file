package com.util.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hzm on 2014/12/18.
 */
public class HttpRequestClientUtil {
    private static final RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).setMaxRedirects(2).build();

    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");
    }

    /**
     * HttpClient连接SSL
     */
    public static String doSSL(String url, Map<String, String> params, String charset, String storeFilePath, String password) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String body = null;
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream in = new FileInputStream(new File(URLDecoder.decode(storeFilePath, "UTF-8")));
            try {
                // 加载keyStore d:\\tomcat.keystore
                trustStore.load(in, password.toCharArray());
            } finally {
                in.close();
            }
            // 相信自己的CA和所有自己签名的证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            // 只允许使用TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                String key = null;
                String value = null;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(key, value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            // 创建httpGet.
            httpGet = new HttpGet(url);
            httpGet.releaseConnection();
            // 执行get请求.
            CloseableHttpResponse response = httpClient.execute(httpGet);
            //获取响应状态
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("Https error status code :" + statusCode);
            }
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity, charset);
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }


        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return body;
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String body = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            // 创建参数队列
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                String key = null;
                String value = null;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(key, value));
                    }
                }
            }
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            // 创建httpPost
            httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("Http error status code :" + statusCode);
            }
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity, charset);
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String body = null;
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                String key = null;
                String value = null;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(key, value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            // 创建httpGet.
            httpGet = new HttpGet(url);
            // 执行get请求.
            CloseableHttpResponse response = httpClient.execute(httpGet);
            //获取响应状态
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("Http error status code :" + statusCode);
            }
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity, charset);
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return body;
    }

    public static String doFile(String url, Map<String, String> params, Map<String, String> files, String charsetName) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if ((params == null || params.isEmpty()) && (files == null || files.isEmpty())) {
            return null;
        }
        String body = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            Charset charset = Charset.forName(charsetName);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.setCharset(charset);
            // 创建参数队列
//            if (params != null && !params.isEmpty()) {
//                String key = null;
//                String value = null;
//                for (Map.Entry<String, String> entry1 : params.entrySet()) {
//                    key = entry1.getKey();
//                    value = entry1.getValue();
//                    if (value != null) {
//                        multipartEntityBuilder.addTextBody(key, value, ContentType.create("text/plain", charset));//无法使用request.getParameter获取到
//                    }
//                }
//            }
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                String key = null;
                String value = null;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(key, value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            // 创建文件队列
            if (files != null && !files.isEmpty()) {
                String fileParam = null;
                String filePath = null;
                File file = null;
                for (Map.Entry<String, String> entry2 : files.entrySet()) {
                    fileParam = entry2.getKey();
                    filePath = entry2.getValue();
                    if (filePath != null) {
                        file = new File(filePath);
                        if (file.exists()) {
                            multipartEntityBuilder.addBinaryBody(fileParam, new File(filePath), ContentType.create("application/octet-stream", charset), file.getName());
                        }
                    }
                }
            }
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            // 创建httpPost
            httpPost = new HttpPost(url);
            httpPost.setEntity(multipartEntityBuilder.build());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("Http error status code :" + statusCode);
            }
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity, charset);
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }
}