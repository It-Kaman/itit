package com.itit.interceptor;

import com.itit.entry.User;
import com.itit.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TeacherInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        User user  = (User) request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);
        //登录了才能访问主界面
        if(user!=null && user.getRole() == 2) {
            return true;
        }
        response.sendRedirect("/index");
        return false;
    }
}
