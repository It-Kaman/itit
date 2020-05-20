package com.itit.controller.Admin;

import com.itit.entry.admin.Admin;
import com.itit.service.Ifac.AdminService;
import com.itit.util.JsonModel;
import com.itit.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/admin")
public class AdminLoginController {

    @Autowired
    private AdminService adminService;


    @RequestMapping(value = "/login")
    public String login(){
        return "/admin/login";
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JsonModel login(@RequestParam("loginName") String loginName, @RequestParam("password") String password, String code, HttpSession session){
        JsonModel jsonModel = new JsonModel();
        if (StringUtil.isEmpty(loginName)){
            jsonModel.setMsg("账号不能为空");
            return jsonModel;
        }
        if(StringUtil.isEmpty(password)){
            jsonModel.setMsg("账号不能为空");
            return jsonModel;
        }
        System.out.println(123);
        Admin admin = adminService.login(loginName,password);
        if(admin == null){
            jsonModel.setMsg("密码或账号错误");
        }else {
            //获取当前用户角色的所有权限
          /*  List<Menu> menu = roleService.findMenuByUserId(user.getId());*/

           /* session.setAttribute(ConstrainUtil.MENU_KEY,menu);*/
            session.setAttribute(StringUtil.ADMIN_SESSION_KEY,admin);
            jsonModel.setSuccess(true);
        }
        return jsonModel;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/admin/login";
    }
}
