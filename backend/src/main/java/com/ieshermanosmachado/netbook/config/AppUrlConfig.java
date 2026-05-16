package com.ieshermanosmachado.netbook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUrlConfig {

    private static String publicBaseUrl = "http://localhost:8080";

    @Value("${app.public-base-url:http://localhost:8080}")
    public void setPublicBaseUrl(String publicBaseUrl) {
        AppUrlConfig.publicBaseUrl = publicBaseUrl;
    }

    public static String getPublicBaseUrl() {
        return publicBaseUrl;
    }
}