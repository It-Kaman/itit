package com.itit.controller;

import com.itit.entry.Security;

import com.itit.entry.User;
import com.itit.service.Ifac.SecurityService;
import com.itit.service.Ifac.UserService;
import com.itit.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SecurityController {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;
    @Autowired
    private Security security;

    /**
     *  修改密保--校验问题页面
     * */
    @RequestMapping(value = "/Security/checkQuestion",method = RequestMethod.GET)
    public String QuestionPage(HttpServletRequest request, @ModelAttribute("my_security")Security Mysecurity){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if (user == null){
            return "redirect:/login";
        }
        Integer id = user.getId();
        Security security = userService.selectSecurityByUserId(id).getSecurity();
        System.out.println(security);
        if(security != null){
            session.setAttribute("user_security",security);
            return "/security/checkQuestion";
        }else {
            session.removeAttribute("user_security");
            return "/security/SecurityQuestion";
        }
    }

    /*
    *       验证
    * */
    @RequestMapping(value = "/Security/checkQuestion",method = RequestMethod.POST)
    public String check(HttpServletRequest request, Security security){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if (user == null){
            return "redirect:/login";
        }
        Security security1 = (Security) request.getSession().getAttribute("user_security");
        if(security.getSecurity_a1().equals(security1.getSecurity_a1()) &&
                security.getSecurity_a2().equals(security1.getSecurity_a2())){
            session.removeAttribute("user_security");
            return "/security/SecurityQuestion";
        }
        else{
            request.setAttribute("my_security",security);
            return "/security/checkQuestion";
        }
    }


    @RequestMapping(value = "/Security/questionSet",method = RequestMethod.POST)
    public String questionSet(HttpServletRequest request,Security security1){
        User user  = (User)request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);
        //先查询是否存在问题
        if(userService.selectSecurityByUserId(user.getId()).getSecurity() == null){
            securityService.add(security1);
            Integer securityId = security1.getId();
            //绑定
            userService.updateSecruityId(securityId,user.getId());
        }else {
            security.setId(userService.selectSecurityByUserId(user.getId()).getSecurity().getId());
            security.setSecurity_q1(security1.getSecurity_q1());
            security.setSecurity_a1(security1.getSecurity_a1());
            security.setSecurity_q2(security1.getSecurity_q2());
            security.setSecurity_a2(security1.getSecurity_a2());
            securityService.updateQ(security);
        }
        return "redirect:/UserCenter";
    }

}
