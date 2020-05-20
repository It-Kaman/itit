package com.itit.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itit.entry.User;
import com.itit.entry.admin.Swiper;
import com.itit.service.Ifac.IndexSwiperService;
import com.itit.service.Ifac.UserService;
import com.itit.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String loginCheck(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        System.out.println(username);
        System.out.println(password);
        User user =userService.login(username,password);
        response.setContentType("application/json;charset=utf-8");
        System.out.println(request.getServletContext().getContextPath());
        System.out.println(user);
        if (user == null){
            return "false";
        }else{
            //测试用
            user.setHeader(user.getHeader());
            request.getSession().setAttribute(StringUtil.USER_SESSION_KEY,user);
            return "success";
        }
    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession seesion){
        seesion.invalidate();
        return "redirect:/index";
    }
}
