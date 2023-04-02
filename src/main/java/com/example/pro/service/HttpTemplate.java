package com.example.pro.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpTemplate {
    private static RestTemplate restTemplate = new RestTemplate();

    private static RestTemplate getRestTemplate() {
        if(restTemplate == null) {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(6000);
            httpRequestFactory.setConnectTimeout(6000);
            httpRequestFactory.setReadTimeout(60000);
            restTemplate = new RestTemplate(httpRequestFactory);
        }
        return restTemplate;
    }

    public static String httpGet(String url) {
        return getRestTemplate().exchange(url, HttpMethod.GET, null, String.class).getBody();
    }

    public static String httpPost(String url, Map<String, Object> paramMap) {
        return getRestTemplate().postForObject(url, paramMap, String.class);
    }

}
