package com.ieshermanosmachado.netbook.config;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AppUrlConfig {

    private static String configuredPublicBaseUrl = "";

    @Value("${app.public-base-url:http://localhost:8080}")
    public void setPublicBaseUrl(String publicBaseUrl) {
        AppUrlConfig.configuredPublicBaseUrl = publicBaseUrl;
    }

    public static String getPublicBaseUrl() {
        if (configuredPublicBaseUrl != null && !configuredPublicBaseUrl.isBlank()) {
            return configuredPublicBaseUrl;
        }

        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String scheme = request.getScheme();
            String host = request.getServerName();
            int port = request.getServerPort();

            boolean standardPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                    || ("https".equalsIgnoreCase(scheme) && port == 443);

            return standardPort ? scheme + "://" + host : scheme + "://" + host + ":" + port;
        }

        return "http://localhost:8080";
    }
}