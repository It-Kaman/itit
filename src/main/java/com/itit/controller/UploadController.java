package com.itit.controller;

import com.github.pagehelper.PageInfo;
import com.itit.entry.Article;
import com.itit.entry.Tag;
import com.itit.entry.User;
import com.itit.entry.Video;
import com.itit.exception.FileIOException;
import com.itit.query.ArticleQuery;
import com.itit.query.VideoQuery;
import com.itit.service.Ifac.ArticleService;
import com.itit.service.Ifac.TagService;
import com.itit.service.Ifac.VideoService;
import com.itit.util.JsonModel;
import com.itit.util.StringUtil;
import com.itit.vo.UploadArticleVo;
import jdk.internal.util.xml.impl.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;
    @Value("${partPath}")
    private String PartPath;

    //视频投稿页面
    @RequestMapping(value = "/video",method = RequestMethod.GET)
    public String uploadV(HttpSession httpSession){
        if ((User) httpSession.getAttribute(StringUtil.USER_SESSION_KEY) != null){
            return "/upload/VideoUpload";
            //判断权限
        }
        return "redirect:/login";
    }

    //文章投稿页面
    @RequestMapping(value="/article",method = RequestMethod.GET)
    public String uploadA(HttpSession session){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user != null){
            return "/upload/ArticleUpload";
        }
        return "redirect:/login";
    }

    //视频投稿记录
    @RequestMapping(value = "/video/record",method = RequestMethod.GET)
    public String recordV(HttpSession session){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user == null){
            return "redirect:/login";
        }
        return "/upload/VideoRecord";
    }

    //文章投稿记录
    @RequestMapping(value = "/article/record",method = RequestMethod.GET)
    public String recordA(HttpSession session){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user == null){
            return "redirect:/login";
        }
        return "/upload/ArticleRecord";
    }

    @ResponseBody
    @RequestMapping(value = "/video/record",method = RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel getRecord(VideoQuery videoQuery,HttpSession session){
        JsonModel js = new JsonModel();
        //获取id
        User user = (User)session.getAttribute(StringUtil.USER_SESSION_KEY);
        videoQuery.setAuthor(user);
        PageInfo<Video> list = videoService.queryAll(videoQuery);
        js.setDatas(list.getList());
        js.setCount(list.getTotal());
        js.setSuccess(true);
        return js;
    }

    //文章查询
    @ResponseBody
    @RequestMapping(value = "/article/record",method = RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel getRecord(ArticleQuery articleQuery, HttpSession session){
        JsonModel js = new JsonModel();
        //获取id
        User user = (User)session.getAttribute(StringUtil.USER_SESSION_KEY);
        articleQuery.setAuthor(user);
        articleQuery.setPagation(true);
        PageInfo<UploadArticleVo> list = articleService.findByQuery(articleQuery);
        List<UploadArticleVo> articleVos = list.getList();
        js.setDatas(articleVos);
        js.setCount(list.getTotal());
        js.setSuccess(true);
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/article/tag",method = RequestMethod.POST,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel getRecord(){
        JsonModel js = new JsonModel();
        List<Tag> tags = tagService.selectAll();
        js.setDatas(tags);
        js.setSuccess(true);
        return js;
    }

    /*修改投稿记录*/
    @RequestMapping(value = "/video/record/{id}/edit",method = RequestMethod.GET)
    public String videoEdit(@PathVariable("id")Integer id,Model model,HttpSession session){
        //判断当前作者是否为当前用户
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user == null){
            return "redirect:/login";
        }
        //查询当前视频id
        Video video = videoService.queryById(id);
        if(!video.getAuthor().getId().equals(user.getId())){
            return "redirect:/upload/video/record";
        }
        //预处理
        video.setHeader("/ititFile/"+ video.getHeader().substring(PartPath.length()));
        model.addAttribute("video",video);
        return "/upload/VideoEdit";
    }


    /*修改投稿记录*/
    @RequestMapping(value = "/article/record/{id}/edit",method = RequestMethod.GET)
    public String ArticleEdit(@PathVariable("id")Integer id,Model model,HttpSession session){
        FileInputStream in = null;
        BufferedReader reader = null;
        InputStream is = null;
        String line = null;
        StringBuffer buffer = null;
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user == null){
            return "redirect:/login";
        }
        System.out.println(123);
        //查询当前视频id
        Article article = articleService.findById(id);
        if(!article.getAuthor().getId().equals(user.getId())){
            return "redirect:/upload/article/record";
        }


        try {
            //判断当前文章是否已经成功审核通过
            if(article.getStatus() == 2){
                model.addAttribute(StringUtil.MSG,"不存在当前文章");
                return "/common/error";
            }else if(article.getStatus() == 1)
            {
                model.addAttribute(StringUtil.MSG,"不允许修改当前文章");
                return "/common/error";
                /*is = new URL(article.getContent()).openStream();
                reader = new BufferedReader(new InputStreamReader(is));*/
            }else if(article.getStatus()==0){
                File file = new File(article.getContent());
                if(!file.exists()){
                    model.addAttribute(StringUtil.MSG,"没有当前文章");
                    return "/common/error";
                }
                in = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(in));
            }
            buffer = new StringBuffer();
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }
            article.setContent(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }
                if(in != null){
                    in.close();
                }
                if(reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("article",article);
        System.out.println(456);
        return "/upload/ArticleEdit";
    }


    @ResponseBody
    @PutMapping(value = "/video/record/{id}",headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel videoEdit(@PathVariable("id") Integer id,@RequestParam(value = "new_header",required = false) MultipartFile file,Video video,HttpSession session){
        JsonModel jsonModel = new JsonModel();
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user == null){
            jsonModel.setMsg("login");
            return jsonModel;
        }
        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setId(id);
        //查询当前视频id
        Video video1 = videoService.queryById(id);
        if(!video1.getAuthor().getId().equals(user.getId())){
            jsonModel.setMsg("not user");
            return jsonModel;
        }
        video.setId(id);
        //判断是否有上传图片
        if (file != null){
            //判断文件格式
            if(!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/gif") && !file.getContentType().equals("image/png")){
                jsonModel.setSuccess(false);
                jsonModel.setMsg("请上传符合要求的图片");
                return  jsonModel;
            }
            //获取当前图片的位置
            Video oldVideo = videoService.queryById(id);
            File file1 = new File(oldVideo.getHeader());
            //将新文件替换到旧文件中
            try(FileOutputStream out = new FileOutputStream(file1); InputStream io = file.getInputStream()){
                byte[] bytes = new byte[2048];
                int len = 0;
                while ((len = io.read(bytes))>0){
                    out.write(bytes,0,len);
                }
                out.flush();
            }  catch (IOException e) {
                throw new FileIOException("上传出错");
            }
        }
        //数据更新
        videoService.edit(video);
        jsonModel.setSuccess(true);
        return jsonModel;
    }

    @ResponseBody
    @PutMapping(value = "/article/record/{id}",headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel articleEdit(@PathVariable("id") Integer id,Article article){
        JsonModel jsonModel = new JsonModel();
        FileOutputStream fileOutputStream = null;
        PrintWriter pw = null;
        article.setId(id);
        //获取文件
        //判断状态
        Article oldArticle = articleService.findById(article.getId());
        if(oldArticle.getStatus() == 0){
            //获取文章
            File file = new File(oldArticle.getContent());
            if(!file.exists()){
                jsonModel.setMsg("服务器异常，请重新尝试");
                return jsonModel;
            }
            try {
                fileOutputStream = new FileOutputStream(file);
                pw = new PrintWriter(new OutputStreamWriter(fileOutputStream),true);
                pw.println(article.getContent());
                article.setContent(null);
                articleService.update(article);
                jsonModel.setSuccess(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if(pw != null){
                        pw.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(oldArticle.getStatus() == 1){
            jsonModel.setMsg("不能修改当前文章");
        }else if(oldArticle.getStatus() == 2){
            jsonModel.setMsg("不存在当前文章");
        }
        return jsonModel;
    }


    @ResponseBody
    @DeleteMapping(value = "/video/record/{ids}",headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel videoDelete(@PathVariable("ids") String str,HttpSession session){
        User user = (User)session.getAttribute(StringUtil.USER_SESSION_KEY);
        JsonModel js = new JsonModel();
        String idStr[] = str.split(",");
        int ids[] = new int[idStr.length];


        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setAuthor(user);
        for(int i = 0; i < idStr.length;i++){
            int id = Integer.parseInt(idStr[i]);
            //delete
            videoQuery.setId(id);
            if(videoService.selectCountByVideo(videoQuery) == 0){
                js.setMsg("非法视频");
                js.setSuccess(false);
                return js;
            }
            Video video = videoService.queryById(id);
            if (video.getStatus() == 0){
                //删除文件
                File url = new File(video.getUrl());
                File header = new File(video.getHeader());
                if(url.exists()){
                    url.delete();
                }
                if(header.exists()){
                    header.delete();
                }

            }else if(video.getStatus() == 1){

            }else if(video.getStatus() ==2 ){

            }else{

            }
            ids[i] = id;
        }
        videoService.delete(ids);
        js.setSuccess(true);
        return js;
    }


    @ResponseBody
    @DeleteMapping(value = "/article/record/{ids}",headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel articleDelete(@PathVariable("ids") String str,HttpSession session){
        User user = (User)session.getAttribute(StringUtil.USER_SESSION_KEY);
        JsonModel js = new JsonModel();
        String idStr[] = str.split(",");
        int ids[] = new int[idStr.length];


        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setAuthor(user);
        for(int i = 0; i < idStr.length;i++){
            int id = Integer.parseInt(idStr[i]);
            //delete
            articleQuery.setId(id);
            if(articleService.findCountByIdAndAuthor(articleQuery)== 0){
                js.setMsg("非法专栏");
                js.setSuccess(false);
                return js;
            }
            ids[i] = id;
        }


        articleService.deleteArticle(ids);
        js.setSuccess(true);
        return js;
    }
}
