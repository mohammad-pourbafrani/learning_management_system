package com.lms.learning_management_system.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IpHandler {

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // if behind proxy/load balancer
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
