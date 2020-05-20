package com.itit.controller;

import com.itit.entry.Tag;
import com.itit.service.Ifac.TagService;
import com.itit.util.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TagController {
    @Autowired
    private TagService tagService;

    @ResponseBody
    @RequestMapping(value = "/tags",method = RequestMethod.POST)
    public JsonModel getTags(){
        JsonModel js = new JsonModel();
        List<Tag> tag = tagService.selectAll();
        if(tag != null){
            js.setDatas(tag);
            js.setCount(tag.size());
            js.setSuccess(true);
        }else {
            js.setMsg("不存在数据");
        }
        return js;
    }
}
