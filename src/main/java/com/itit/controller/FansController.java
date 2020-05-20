package com.itit.controller;

import com.itit.entry.User;
import com.itit.service.Ifac.FansService;
import com.itit.service.Ifac.UserService;
import com.itit.util.JsonModel;
import com.itit.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/fans")
public class FansController {


    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;
/**
 * 判断是否关注、未关注或本用户
 */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Object KnowFans(@PathVariable("id") Integer id, HttpSession session){
        JsonModel jmodel = new JsonModel();
        jmodel.setSuccess(false);
        User user = (User)session.getAttribute(StringUtil.USER_SESSION_KEY);
        if (user == null){
            jmodel.setSuccess(true);
            jmodel.setCount(1);
            //没有注册
            jmodel.setDatas(false);
            return jmodel;
        }
        Integer fan_id = user.getId();
        if(id.equals(fan_id)){
            //为当前用户
            jmodel.setSuccess(true);
            //因为是当前用户，所以没有count
            jmodel.setCount(0);
        }else{
            //判断是否存在用户
            jmodel.setCount(1);
            User user1 = new User();
            user1.setId(id);
            if (userService.SelectCountByUser(user1)==1){
                //判断是否关注
                boolean num = fansService.selectByUserIdFansId(id,fan_id)!= null?true:false;
                jmodel.setSuccess(true);
                jmodel.setCount(1);
                jmodel.setDatas(num);
            }else {
                jmodel.setMsg("系统繁忙,请稍候再试!");
                return jmodel;
            }
        }
        return jmodel;
    }


    /*
     *   关注/取消
     * */
    @ResponseBody
    @RequestMapping(value = "/follow",method = RequestMethod.POST)
    public Object follow(@RequestParam("user_id") Integer user_id, HttpSession session){
        JsonModel jsonModel = new JsonModel();
        jsonModel.setSuccess(false);
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user != null){
            //当前Id是否为用户Id
            if(user_id != user.getId()){
                //判断是否已关注
                if (fansService.selectByUserIdFansId(user_id,user.getId()) ==  null){
                    fansService.add(user_id,user.getId());
                }else {
                    fansService.delByUserFanId(user_id,user.getId());
                }
                jsonModel.setSuccess(true);
                return jsonModel;
            }
        }
        jsonModel.setMsg("系统繁忙,请稍后再试!");
        return jsonModel;
    }
}
