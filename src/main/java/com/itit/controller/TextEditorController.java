package com.itit.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itit.util.COSUtil;
import com.itit.util.FileUtil;
import com.itit.util.StringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;


//评论富文本框工具控制器
@RestController
public class TextEditorController {

    @Autowired
    private COSClient cosClientDownload;

    @Autowired
    private COSClient cosClientUpload;

    @Value(value = "${BUCKET_NAME}")
    private String buckName;

    @RequestMapping("/imageUpload")
    @ResponseBody
    public Map imageUpload(@RequestParam(value = "editormd-image-file",required = true)MultipartFile file, HttpServletRequest request){
        Properties p = new Properties();
        String bucketName = "itit-1300622267";
        if (!bucketName.equals("${BUCKET_NAME}") || !StringUtil.isEmpty(bucketName)){
            bucketName = buckName;
        }

        //获取图片
        String FileName = file.getOriginalFilename();
        //获取后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        Map map = new HashMap();
        //判断是否是图片
        if (!suffix.equals(".pjp")&&!suffix.equals(".tiff")&&!suffix.equals(".pjpeg")&&!suffix.equals(".jfif")
                &&!suffix.equals(".tif")&&!suffix.equals(".gif")&&!suffix.equals(".svg")&&!suffix.equals(".bmp")
                &&!suffix.equals(".png")&&!suffix.equals(".jpeg")&&!suffix.equals(".svgz")&&!suffix.equals(".jpg")
                &&!suffix.equals(".webp")&&!suffix.equals(".ico")&&!suffix.equals(".xbm")&&!suffix.equals(".dib")){
            map.put("url","");
            map.put("success",0);
            map.put("message","upload fail");
            return map;
        }else {
//            组装文件名字
            String prefix = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            FileName = prefix  +  "_" + UUID.randomUUID().toString().replace("-","")+suffix;
            //创建文件夹进行文件打散
            String path = "folder/images/comment";
            String filepath = StringUtil.dateFiles();
            path += filepath;
            //创建文件夹
            if(COSUtil.selectCOS_Mkdir(cosClientUpload,path,bucketName).size() == 0)
            {
                COSUtil.Create_Mkdir(cosClientUpload,bucketName,path);
            }
            //上传文件
            try {
                //转为File类型
                File transformFile = new File(file.getOriginalFilename());
                FileUtil.multiPartFileToFile(file.getInputStream(),transformFile);

                COSUtil.UpdateFile(cosClientUpload,bucketName,path+"/"+FileName,transformFile);

                //显示
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,path+"/"+FileName,HttpMethodName.GET);
                URL url = cosClientDownload.generatePresignedUrl(urlRequest);
                map.put("url",url.toString());
                map.put("success",1);
                map.put("message","upload success");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
