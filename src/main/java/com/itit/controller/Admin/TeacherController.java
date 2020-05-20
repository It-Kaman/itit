package com.itit.controller.Admin;

import com.github.pagehelper.PageInfo;
import com.itit.entry.Teacher;
import com.itit.entry.User;
import com.itit.query.TeacherQuery;
import com.itit.service.Ifac.TeacherService;
import com.itit.util.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String list(){
        return "admin/teacher";
    }


    @ResponseBody
    @RequestMapping(value = "/admin",method = RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel list(TeacherQuery teacherQuery){
        JsonModel js = new JsonModel();
        teacherQuery.setPagation(true);
        PageInfo<Teacher> pageInfo = teacherService.findByQuery(teacherQuery);
        js.setSuccess(true);
        js.setDatas(pageInfo.getList());
        js.setCount(pageInfo.getTotal());
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/{id}/aduit",method = RequestMethod.POST,headers ={"X-Requested-With=XMLHttpRequest"})
    public JsonModel aduit(@PathVariable("id")Integer id, TeacherQuery teacherQuery){
        JsonModel jsonModel = new JsonModel();
        System.out.println(teacherQuery);
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setTeacher_status(teacherQuery.getTeacher_status());
        teacher.setUser(new User(teacherQuery.getUser_id()));
        teacherService.aduit(teacher);
        jsonModel.setSuccess(true);
        return jsonModel;
    }


    @ResponseBody
    @PutMapping(value = "/admin/aduit/{ids}",headers ={"X-Requested-With=XMLHttpRequest"})
    public JsonModel aduit(@PathVariable("ids") String idsStr,Byte teacher_status){
        JsonModel jsonModel = new JsonModel();
        String[] strs = idsStr.split(",");
        int[] ids = new int[strs.length];
        for(int i = 0 ; i < strs.length;i++){
            ids[i] = Integer.parseInt(strs[i]);
        }
        teacherService.aduitByIds(ids,teacher_status);
        jsonModel.setSuccess(true);
        return jsonModel;
    }

    @ResponseBody
    @DeleteMapping(value = "/admin/delete",headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel delete(){
        JsonModel jsonModel = new JsonModel();
        Teacher teacher = new Teacher();
        teacher.setTeacher_status((byte) 2);
        teacherService.deleteByTeacher(teacher);
        jsonModel.setSuccess(true);
        return jsonModel;
    }
}
