package org.reactome.server.controller;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {

    public static String getBaseUrl(HttpServletRequest req) {
        String base = req.getScheme() + "://" + req.getServerName();
        return req.getServerName().equals("localhost") ? base + ":" + req.getServerPort() + req.getContextPath() : base;
    }
}