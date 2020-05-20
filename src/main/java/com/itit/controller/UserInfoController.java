package com.itit.controller;

import com.itit.entry.Fans;
import com.itit.entry.Security;
import com.itit.entry.User;
import com.itit.service.Ifac.*;
import com.itit.util.COSUtil;
import com.itit.util.FileUtil;
import com.itit.util.StringUtil;
import com.itit.util.TagUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Controller
@RequestMapping("/UserCenter")
public class UserInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SecurityService securityService;
    @RequestMapping("")
    public String Page(Model model, @RequestParam(value = "id",required = false) Integer id, HttpServletRequest request) {
        //判断当前请求id是否为用户id
        User user = (User)request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);

        if (id == null){
            if(user == null){
                model.addAttribute(StringUtil.MSG,"不存在当前用户");
                return "common/error";
            }else{
                model.addAttribute("id",user.getId());
                return "user/UserCenter";
            }
        }else {
            if(user != null && user.getId() == id){
                model.addAttribute("id",id);
                return "user/UserCenter";
            }else {
                User user1 = new User();
                user1.setId(id);
                if(userService.SelectCountByUser(user1)!= 0){
                    user1 = userService.SelectAllByUserId(user1.getId());
                    System.out.println(user1);
                    //查询当前用户已的粉丝数
                    int fans = fansService.SelectFansNum(user1.getId());
                    //查询当前用户视频投稿数
                    int videos = videoService.SelectVideoUpdateNum(user1.getId());
                    //专栏投稿
                    int article = articleService.SelectUpdateNum(user1.getId());
                    model.addAttribute("user",user1);
                    model.addAttribute("video",videos);
                    model.addAttribute("fans",fans);
                    model.addAttribute("article",article);
                    if(user != null){
                        //查询是否关注
                        if(fansService.selectByUserIdFansId(id,user.getId()) != null){
                            model.addAttribute("Isfans",1);
                        }else {
                            model.addAttribute("Isfans",0);
                        }
                    }
                    return "user/SeeUserCenter";
                }else {
                    model.addAttribute(StringUtil.MSG,"不存在当前用户");
                    return "common/error";
                }
            }
        }
    }

    /**
     * 首页
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,@RequestParam(value = "id",required = false) Integer id)
    {
        //查询数据库
        //粉丝数
        //数据处理
        String fansNum = StringUtil.NumFormate(fansService.SelectFansNum(id));
        //文字发表数
        Integer articleNum = articleService.SelectUpdateNum(id);
        //视频发表数
        Integer videoNum = videoService.SelectVideoUpdateNum(id);
        request.setAttribute(TagUtil.FANS_NUM,fansNum);
        request.setAttribute(TagUtil.VIDEO_UPDATE_NUM,videoNum);
        request.setAttribute(TagUtil.ARTICLE_UPDATE_NUM,articleNum);
        return "user/UserIndex";
    }

    /**
     * 个人信息
     * @param request
     * @return
     */
    @RequestMapping("/info")
    public String UserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer user_id = ((User)request.getSession().getAttribute(StringUtil.USER_SESSION_KEY)).getId();
        if (Integer.parseInt(request.getParameter("id"))==user_id){
            request.setAttribute(StringUtil.USER_SESSION_KEY,userService.SelectAllByUserId(user_id));
            return "user/UserInfo";
        }
        return "user/UserError";
    }

    /**
     * 更新个人信息
     * @param user
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateInfo")
    public Boolean UserInfoUpdate(@RequestBody User user,HttpServletRequest request){
        Integer id = ((User)request.getSession().getAttribute(StringUtil.USER_SESSION_KEY)).getId();
        user.setId(id);
        if(userService.UpdateByUser(user) > 0)
        {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 头像
     * @param request
     * @return
     */
    @RequestMapping("/header")
    public String UserImg(HttpServletRequest request)
    {
        User user = new User();
        return "user/UserImg";
    }

    /**
     * 安全
     * @param request
     * @return
     */
    @RequestMapping("/security")
    public String UserSecurity(HttpServletRequest request)
    {
        User user = (User)request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);
        String password = userService.SecurityPassword(user.getId());
        //安全强度
        if(password.length() >= 13){
            request.setAttribute("strong-content","强");
        }else if(password.length() > 8 && password.length() < 13){
            request.setAttribute("strong-content","中");
        }else {
            request.setAttribute("strong-content","弱");
        }
        //检查是否存在安全问题
        Security security = userService.selectSecurityByUserId(user.getId()).getSecurity();
        if (security == null){
            request.setAttribute("secuity-answer","无");
        }else {
            request.setAttribute("secuity-answer","有");
        }
        return "/User/UserSecurity";
    }

    /**
     * 修改密码
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyPassword",method = RequestMethod.GET)
    public String ModifyPassword(Integer id,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);
        if (user == null){
            return "forward:/login";
        }
        id = user.getId() == id?id:null;
        if(id == null){
            request.setAttribute(StringUtil.MSG,"无法修改其他用户密码");
            return "/common/error";
        }else {
            return "/security/SecurityModifyPwd";
        }
    }


    /*
    *   关注/取消
    * */
    @ResponseBody
    @RequestMapping(value = "/follow",method = RequestMethod.GET)
    public void follow(@RequestParam("user_id") Integer user_id, HttpSession session,HttpServletResponse response){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        System.out.println(123);
        if(user != null){
            Integer fan_id = user.getId();
            fansService.add(user_id,fan_id);
        }
    }

    //取消关注
    @ResponseBody
    @RequestMapping(value = "/unfollow",method = RequestMethod.GET)
    public void unfollow(@RequestParam("user_id") Integer user_id, HttpSession session){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user != null){
            Integer fan_id = user.getId();
            fansService.delByUserFanId(user_id,fan_id);
        }
    }
}
