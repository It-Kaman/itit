package com.itit.controller.Admin;

import com.itit.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @RequestMapping(value = "/admin/main")
    public String main(HttpSession session){
        System.out.println(session.getAttribute(StringUtil.ADMIN_SESSION_KEY));
        return "admin/main";
    }
}
