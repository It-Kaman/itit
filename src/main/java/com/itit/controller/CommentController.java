package com.itit.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.itit.entry.Article;
import com.itit.entry.Comment;
import com.itit.entry.User;
import com.itit.entry.Video;
import com.itit.exception.FileIOException;
import com.itit.query.CommentQuery;
import com.itit.query.VideoQuery;
import com.itit.service.Ifac.ACCommentService;
import com.itit.service.Ifac.ArticleService;
import com.itit.service.Ifac.VCommentService;
import com.itit.service.Ifac.VideoService;
import com.itit.util.COSUtil;
import com.itit.util.FileUtil;
import com.itit.util.JsonModel;
import com.itit.util.StringUtil;
import com.itit.vo.VideoSearchVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * 评论
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private VCommentService vcommentService;
    @Autowired
    private ACCommentService acCommentService;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private COSClient cosClientUpload;
    @Autowired
    private COSClient cosClientDownload;
    @Value(value = "${BUCKET_NAME}")
    private String buckName;


    @Value(value = "${commentPageNum}")
    private String pageSize;

   @ResponseBody
   @RequestMapping(value = "/article/{num}",method = RequestMethod.POST)
   public Map<String,Object> accomment(@PathVariable("num")Integer num, CommentQuery query){
       BufferedReader reader =null;
       Map<String,Object> map = new HashMap<>();
       Article article = articleService.findById(num);
       if(article == null || article.getStatus() != 1){
           map.put("false",true);
           return map;
       }


       query.setPagation(true);
       query.setObject_id(num);
       PageInfo<Comment> pageInfo = acCommentService.findByQuery2(query);
       List<Comment> list = pageInfo.getList();
       for(Comment c : list){
            if(c.getContent().startsWith("http")){
                File file = new File(c.getContent());
                try {
                    reader = new BufferedReader(new InputStreamReader(new URL(c.getContent()).openStream()));
                    String str=null;
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine())!=null){
                        buffer.append(str + "\n");
                    }
                    System.out.println(buffer);
                    c.setContent(buffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileIOException("网络繁忙！");
                }finally {
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                continue;
            }
       }
       map.put("success",true);
       map.put("count",pageInfo.getTotal());
       map.put("datas",list);
       map.put("MaxPageNum",pageInfo.getPages());
       return map;
   }

    @ResponseBody
    @RequestMapping(value = "/video/{num}",method = RequestMethod.POST)
    public Map<String,Object> vocomment(@PathVariable("num")Integer num, CommentQuery query){
        BufferedReader reader =null;
        Map<String,Object> map = new HashMap<>();
        Video video = videoService.queryById(num);
        if(video == null || video.getStatus() != 1){
            map.put("false",true);
            return map;
        }


        query.setPagation(true);
        query.setObject_id(num);

        PageInfo<Comment> pageInfo = vcommentService.findByQuery2(query);
        List<Comment> list = pageInfo.getList();
        for(Comment c : list){
            if(c.getContent().startsWith("http")){
                File file = new File(c.getContent());
                try {
                    reader = new BufferedReader(new InputStreamReader(new URL(c.getContent()).openStream()));
                    String str=null;
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine())!=null){
                        buffer.append(str + "\n");
                    }
                    c.setContent(buffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileIOException("网络繁忙！");
                }finally {
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                continue;
            }
        }
        System.out.println(list);
        map.put("success",true);
        map.put("count",pageInfo.getTotal());
        map.put("datas",list);
        map.put("MaxPageNum",pageInfo.getPages());
        return map;
    }


   @ResponseBody
   @RequestMapping(value = "/add",method = RequestMethod.POST,headers = {"X-Requested-With=XMLHttpRequest"})
   public JsonModel add(Comment comment, HttpSession session){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        JsonModel js = new JsonModel();
        if(user == null){
            js.setMsg("请登录");
            js.setDatas("login");
            return js;
        }
        if(StringUtil.isEmpty(comment.getContent())){
            js.setMsg("请输入内容");
            return js;
        }
        Date data = new Date();
        comment.setFrom_id(user);
        String FileDatename = StringUtil.DateFormate("yyyyMMddHHmmss",data);
        comment.setCreate_date(data);
       System.out.println(FileDatename);

       //判断当前文件夹是否存在
       //封装文件
       File file = new File("aid="+comment.getObject_id()+"_"+FileDatename+user.getId()+".txt");
       try(PrintWriter pw = new PrintWriter(new FileWriter(file),true)) {
           pw.println(comment.getContent());
           //载入完成

           String bucketName = "itit-1300622267";
           if (!bucketName.equals("${BUCKET_NAME}") || !StringUtil.isEmpty(bucketName)){
               bucketName = buckName;
           }
           //创建文件夹进行文件打散
           String path = "comment/article";
           String filepath = StringUtil.dateFiles();
           path += filepath+"/";
           //创建文件夹
           if(COSUtil.selectCOS_Mkdir(cosClientUpload,path,bucketName).size() == 0)
           {
               COSUtil.Create_Mkdir(cosClientUpload,bucketName,path);
           }
           COSUtil.UpdateFile(cosClientUpload,bucketName,path+"/"+file.getName(),file);
           //获取当前已上传URI
           //显示
           GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,path+"/"+file.getName(),HttpMethodName.GET);
           URL url = cosClientDownload.generatePresignedUrl(urlRequest);
           comment.setContent(url.toString());

           if(comment.getStatus() == 1){
               if (acCommentService.add(comment) == 1){
                   js.setSuccess(true);
               }
           }else if(comment.getStatus() == 0){
                if(vcommentService.add(comment) == 1){
                    js.setSuccess(true);
                }
           }else {
               js.setSuccess(false);
               return js;
           }
       } catch (IOException  | InterruptedException e) {
           e.printStackTrace();
           throw new FileIOException("网络资源异常,请重新尝试");
       }finally {
           file.delete();
       }
       return js;
   }

   @ResponseBody
   @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
   public JsonModel delete(@PathVariable("id") Integer id,@RequestParam("user_id") Integer user_id,@RequestParam("status") Byte status,HttpSession session){
       JsonModel js = new JsonModel();
       User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
       if(status == 0){
           if(user.getId().equals(user_id)){
               //删除
               acCommentService.deleteById(id);
           }
       }else if(status == 1){
           if(user.getId().equals(user_id)){
               //删除
               vcommentService.deleteById(id);
           }
       }else {
           js.setSuccess(false);
           return js;
       }
       js.setSuccess(true);
       return js;
   }
}
