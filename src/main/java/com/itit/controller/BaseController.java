package com.itit.controller;

import com.itit.util.StringUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 *  处理页面跳转成功失败事务
 * */
@Controller
public class BaseController implements ErrorController {

    @RequestMapping("/success")
    public String success(){
        return "/common/success";
    }

    @RequestMapping("/error")
    public String PageNotFound(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 404){
            request.setAttribute(StringUtil.MSG,"页面不存在");
            return "/common/error";
        }else {
            request.setAttribute(StringUtil.MSG,"拒绝进入");
            return "/common/error";
        }
    }

    @RequestMapping("/pageError")
    public String error(HttpServletRequest request){
        if(request.getAttribute(StringUtil.MSG) == null){
            request.setAttribute(StringUtil.MSG,null);
        }else if(request.getParameter(StringUtil.MSG) != null){
            request.setAttribute(StringUtil.MSG,request.getParameter(StringUtil.MSG));
        }
        return "/common/error";
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
