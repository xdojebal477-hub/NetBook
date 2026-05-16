package com.ieshermanosmachado.netbook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AppUrlConfig {

    private static final String LOCAL_BACKEND_BASE = "http://localhost:8080";
    private static final String LOCAL_LOOPBACK_BASE = "http://127.0.0.1:8080";
    private static String configuredPublicBaseUrl = "";

    @Value("${app.public-base-url:}")
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

        return LOCAL_BACKEND_BASE;
    }

    public static String normalizeBackendUrl(String url) {
        if (url == null || url.isBlank()) {
            return url;
        }

        if (url.startsWith("/api/")) {
            return getPublicBaseUrl() + url;
        }

        if (url.startsWith(LOCAL_BACKEND_BASE)) {
            return getPublicBaseUrl() + url.substring(LOCAL_BACKEND_BASE.length());
        }

        if (url.startsWith(LOCAL_LOOPBACK_BASE)) {
            return getPublicBaseUrl() + url.substring(LOCAL_LOOPBACK_BASE.length());
        }

        return url;
    }
}