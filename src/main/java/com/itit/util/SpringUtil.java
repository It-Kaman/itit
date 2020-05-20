package com.itit.util;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

public class SpringUtil {
    private static ServletContext servletContext;

    public static void init(ServletContext servletContext){
        SpringUtil.servletContext = servletContext;
    }

    public static <T> T getBean(String name){
        return (T) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(name);
    }
}
