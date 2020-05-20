package com.itit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;

@Controller
public class TestController {

    @RequestMapping("/comment")
    public String CommentTest(){
        return "Editor";
    }
}
