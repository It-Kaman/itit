package com.itit.controller;

import com.itit.entry.admin.Swiper;
import com.itit.service.Ifac.IndexSwiperService;
import com.itit.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页
 */
@Controller
public class IndexController {
    @Autowired
    private IndexSwiperService swiperService;
    @RequestMapping(value = {"/","/index"})
    public String Index(HttpServletRequest request){
        if(request.getSession().getAttribute(StringUtil.USER_SESSION_KEY)== null){
            request.getSession().setAttribute(StringUtil.USER_SESSION_KEY,null);
        }
        List<Swiper> swipers = swiperService.queryAll();
        System.out.println(swipers);
        request.setAttribute("swipers",swipers);
        return "Index";
    }
}
