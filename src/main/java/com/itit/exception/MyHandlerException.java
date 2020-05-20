package com.itit.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyHandlerException implements HandlerExceptionResolver {
    /**
     * Try to resolve the given exception that got thrown during handler execution,
     * returning a {@link ModelAndView} that represents a specific error page if appropriate.
     * <p>The returned {@code ModelAndView} may be {@linkplain ModelAndView#isEmpty() empty}
     * to indicate that the exception has been resolved successfully but that no view
     * should be rendered, for instance by setting a status code.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or {@code null} if none chosen at the
     *                 time of the exception (for example, if multipart resolution failed)
     * @param ex       the exception that got thrown during handler execution
     * @return a corresponding {@code ModelAndView} to forward to,
     * or {@code null} for default processing in the resolution chain
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();

        String errorMsg;

        //如果当前请求是ajax的请求的时候，就要响应json格式
        if(ex instanceof FileIOException){
            errorMsg = ex.getMessage();
        }else {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
            errorMsg = "服务器繁忙，请销后再试";
        }

        //判断是否ajax请求
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().print("{\"success\":false,\"msg\":\""+errorMsg+"\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return modelAndView;
        }else {
            //普通请求
            //这里的数据是放在request作用域中的
            modelAndView.addObject("errorMsg", errorMsg);
            modelAndView.setViewName("forward:/common/error.html");
            return modelAndView;
        }
    }
}
