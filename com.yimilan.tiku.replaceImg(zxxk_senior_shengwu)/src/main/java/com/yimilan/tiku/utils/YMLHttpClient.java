package com.yimilan.tiku.utils;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@ThreadSafe
public class YMLHttpClient {
    private final int CONNECTION_TIMEOUT = 10 * 1000;
    private final int SO_TIMEOUT = 10 * 1000;

    private final String ContentType = "application/json";

    public YMLHttpClient() {

    }

    /**
     * @param url
     * @param headerMap
     * @param paramMap
     * @return created by xf
     */
    public String doHttpsPost(String url, Map<String, Object> headerMap, Map<String, Object> paramMap) {

        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            //请求头
            httpPost.addHeader("Content-Type", ContentType);
            if (headerMap != null) {
                Iterator iteratorHeader = headerMap.entrySet().iterator();
                while (iteratorHeader.hasNext()) {
                    Entry<String, String> elem = (Entry<String, String>) iteratorHeader.next();
                    httpPost.addHeader(elem.getKey(), elem.getValue());
                }
            }

            //设置请求参数
            String json = JsonUtils.toJson(paramMap);
            if (json != null) {
                StringEntity httpEntity = new StringEntity(json, "UTF-8");
                httpPost.setEntity(httpEntity);
            }

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * @param url
     * @param headerMap
     * @return created by xf
     */
    public String doHttpsDelete(String url, Map<String, Object> headerMap) {

        HttpClient httpClient = null;
        HttpDelete httpDelete = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpDelete = new HttpDelete(url);
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            //请求头
            httpDelete.addHeader("Content-Type", ContentType);
            if (headerMap != null) {
                Iterator iteratorHeader = headerMap.entrySet().iterator();
                while (iteratorHeader.hasNext()) {
                    Entry<String, String> elem = (Entry<String, String>) iteratorHeader.next();
                    httpDelete.addHeader(elem.getKey(), elem.getValue());
                }
            }

            HttpResponse response = httpClient.execute(httpDelete);
            if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * @param url
     * @param headerMap
     * @param entity
     * @return created by xf
     */
    public String doHttpsPostEntity(String url, Map<String, Object> headerMap, String entity) {

        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            //请求头
            httpPost.addHeader("Content-Type", ContentType);
            if (headerMap != null) {
                Iterator iteratorHeader = headerMap.entrySet().iterator();
                while (iteratorHeader.hasNext()) {
                    Entry<String, String> elem = (Entry<String, String>) iteratorHeader.next();
                    httpPost.addHeader(elem.getKey(), elem.getValue());
                }
            }

            //设置请求参数
            httpPost.setEntity(new StringEntity(entity));

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public String doHttpsRequest(String url, Map<String, String> headerMap, Map<String, String> paramsMap, String method) {
        if (null == url || url.length() <= 0) {
            throw new IllegalArgumentException("url");
        }

        if (null == method || method.length() <= 0)
            method = "GET";
        String result = null;
        org.apache.commons.httpclient.HttpClient http = new org.apache.commons.httpclient.HttpClient();
        http.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

        Protocol myhttps = new Protocol("https", new CustomSSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);

        PostMethod post = null;
        GetMethod get = null;
        if (method == "GET") {
            if (paramsMap != null && paramsMap.size() > 0) {
                ArrayList list = new ArrayList();
                for (String key : paramsMap.keySet()) {
                    list.add(key + "=" + paramsMap.get(key));
                }
                String parms = org.apache.commons.lang.StringUtils.join(list, "&");
                url += "?" + parms;
            }
            get = new GetMethod(url);
            //请求头
            if (headerMap != null && headerMap.size() > 0) {
                for (String key : headerMap.keySet()) {
                    get.addRequestHeader(key, headerMap.get(key));
                }
            }
        } else {
            post = new PostMethod(url);
            //请求头
            post.addRequestHeader("Content-Type", ContentType);
            if (headerMap != null && headerMap.size() > 0) {
                for (String key : headerMap.keySet()) {
                    post.addRequestHeader(key, headerMap.get(key));
                }
            }
            if (paramsMap != null && paramsMap.size() > 0) {
                org.apache.commons.httpclient.NameValuePair[] datas = new org.apache.commons.httpclient.NameValuePair[paramsMap.size()];
                int index = 0;
                for (String key : paramsMap.keySet()) {
                    datas[index++] = new org.apache.commons.httpclient.NameValuePair(key, paramsMap.get(key));
                }
                post.setRequestBody(datas);
            }
            HttpClientParams httparams = new HttpClientParams();
            httparams.setSoTimeout(60000);
            post.setParams(httparams);
        }

        try {
            int statusCode = (post != null) ? http.executeMethod(post) : http.executeMethod(get);
            if (statusCode == org.apache.commons.httpclient.HttpStatus.SC_OK) {
                result = (post != null) ? post.getResponseBodyAsString() : get.getResponseBodyAsString();
            } else {
                System.out.println(" http response status is " + statusCode);
            }

        } catch (HttpException e) {
            System.out.println("error url=" + url + ":" + e);
        } catch (IOException e) {
            System.out.println("error url=" + url + ":" + e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return result;
    }
}


