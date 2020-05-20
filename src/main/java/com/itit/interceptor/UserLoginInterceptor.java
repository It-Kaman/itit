package com.itit.interceptor;

import com.itit.util.ConstrainUtil;
import com.itit.util.StringUtil;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        //登录了才能访问主界面
        if(request.getSession().getAttribute(StringUtil.USER_SESSION_KEY)==null && !requestURI.startsWith("/login")) {
            //没有登录
            response.sendRedirect(request.getContextPath()+"/login");
            return false;
        }
        return true;
    }
}
