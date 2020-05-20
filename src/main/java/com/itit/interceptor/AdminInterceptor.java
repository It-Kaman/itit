package com.itit.interceptor;

import com.itit.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if(request.getSession().getAttribute(StringUtil.ADMIN_SESSION_KEY) != null && requestURI.startsWith("/admin/login")){
            response.sendRedirect("/admin/main");
            return true;
        }
        else if(request.getSession().getAttribute(StringUtil.ADMIN_SESSION_KEY) != null || requestURI.startsWith("/admin/login")){
            return true;
        }
        response.sendRedirect("/admin/login");
        return false;
    }
}
