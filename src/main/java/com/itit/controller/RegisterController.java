package com.itit.controller;

import com.itit.entry.User;
import com.itit.service.Ifac.TeacherService;
import com.itit.service.Ifac.UserService;
import com.itit.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RegisterController{

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String RegisterPage()
    {
        return "registerLead";
    }

    /*用户注册的时候判断注册信息*/
    @ResponseBody
    @RequestMapping("/register/check")
    public String RegisterCheck(@RequestBody User user){
        int num = userService.SelectCountByUser(user);
        if(num == 0){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("/registerTo")
    public String RegisterTo(@RequestParam("role") String Strid, HttpServletRequest request){
        request.setAttribute(StringUtil.MSG,null);
        try{
            int id = Integer.parseInt(Strid);
            if(id == 0){
//            学生
                return "registerUser";
            }else if(id == 1){
//            教师
                return "registerTeacher";
            }else{
                return "registerLead";
            }
        }catch (NumberFormatException e){
            request.setAttribute(StringUtil.ERROR_SESSION_KEY,"页面不存在");
            return "error";
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public void Commit(User user, HttpServletRequest request,
                       HttpServletResponse response){
        String jobId = request.getParameter("job");
        try {
            if(!user.getRole().toString().equals("1") && !user.getRole().toString().equals("0")){
                request.setAttribute(StringUtil.MSG,"不存在当前角色！");
                request.getRequestDispatcher("/error").forward(request,response);
            }
            //检测当前用户提交的数据
            if(StringUtil.isEmpty(user.getUsername())
                    || StringUtil.isEmpty(user.getPassword())
                    || StringUtil.isEmpty(user.getSex().toString())
                    || StringUtil.isEmpty(user.getAnothername())
                    || StringUtil.isEmpty(user.getPhone())
                    || StringUtil.isEmpty(user.getEmail()))
            {
                request.setAttribute(StringUtil.MSG,"请填写所有的信息");
                request.setAttribute(StringUtil.USER_SESSION_KEY,user);
                request.getRequestDispatcher("/registerTo?role="+user.getRole()).forward(request,response);
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean flag = false;
        //如果是教师用户提交请求
        if(user.getRole().toString().equals("1")){
            flag = true;
        }

        //默认通过普通用户
        user.setRole((byte)1);
        user.setHeader("/static/img/header/default/akari.jpg");
        user.setJob_id(Integer.parseInt(jobId));
        if(userService.InsertIntoUserByRegister(user)){
            try {
                /*立即查找*/
                user = userService.login(user.getUsername(),user.getPassword());

                //老师用户注册提交申请
                if(flag){
                    teacherService.add(user.getId(),user.getJob_id());
                }

                request.getSession().setAttribute(StringUtil.USER_SESSION_KEY,user);

                request.setAttribute(StringUtil.MSG,"注册成功，3秒后自动返回首页，或者点击下面的按钮回到首页");
                request.getRequestDispatcher("/success").forward(request,response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }else {
            try {
                request.setAttribute(StringUtil.USER_SESSION_KEY,user);
                request.getRequestDispatcher("/registerTo?role="+user.getRole()).forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
