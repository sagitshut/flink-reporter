package com.thorntree.bigdata.flink.reporter;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @description:
 * @author: lxs
 */
public class HttpUtils {

    /**
     * Do Post
     * @param url
     * @return
     */
    public static Boolean doPost(String url,Map<String,Object> map) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getConfig());
        String json = JsonUtil.toJSONString(map);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode()==200){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Request Config
     * @return
     */
    private static RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(10000)
                .build();
        return config;
    }
}
