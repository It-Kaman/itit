package com.itit.controller;

import com.github.pagehelper.PageInfo;
import com.itit.entry.ItitFile;
import com.itit.entry.User;
import com.itit.exception.FileIOException;
import com.itit.query.FileQuery;
import com.itit.service.Ifac.FileService;
import com.itit.util.COSUtil;
import com.itit.util.JsonModel;
import com.itit.util.StringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.GetObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private COSClient cosClientUpload;
    @Autowired
    private COSClient cosClientDownload;

    @Value("${BUCKET_NAME}")
    private String BuckName;

    @Value("${COS_FILE}")
    private String COS_FILE;

    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel files(FileQuery query){
        JsonModel js = new JsonModel();
        query.setPagation(true);
        PageInfo<ItitFile> pageInfo =fileService.findByQuery(query);
        List<ItitFile> list = pageInfo.getList();
        js.setSuccess(true);
        js.setDatas(pageInfo.getList());
        js.setCount(pageInfo.getTotal());
        return js;
    }

    @RequestMapping(value = "/download/{id}",method = RequestMethod.GET)
    public void downLoad(@PathVariable("id")Integer id, HttpServletRequest request, HttpServletResponse response){
       ItitFile ititFile =  fileService.queryById(id);
        OutputStream outputStream = null;
        InputStream input= null;
        try {
            URL url = new URL(ititFile.getUrl());
            String fileName = "attachment;filename="+URLEncoder.encode(ititFile.getName(),"utf-8");
            response.setHeader("content-disposition",fileName);
            input = url.openStream();
            outputStream = response.getOutputStream();

            int len = -1;
            byte [] temp = new byte[2014];
            while((len=input.read(temp))!=-1) {
                outputStream.write(temp,0,len);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input!=null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public JsonModel upload(MultipartFile file, @RequestParam("id") Integer id, HttpSession session){
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);

        JsonModel js = new JsonModel();
        String fileName = file.getOriginalFilename();

        if(user == null){
            js.setMsg("请登录");
            return js;
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        System.out.println(suffix);
        String prefix = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        ItitFile ititFile = new ItitFile();
        fileName = prefix + "_" + UUID.randomUUID().toString().replace("-","") + suffix;

        String datePath = StringUtil.dateFiles();

        String savePath = COS_FILE + datePath;
        fileName = savePath + File.separator + fileName;

        System.out.println(fileName);
        System.out.println(file.getSize());

        if(COSUtil.selectCOS_Mkdir(cosClientUpload,savePath,BuckName).size() == 0){
            COSUtil.Create_Mkdir(cosClientUpload,BuckName,savePath+"/");
        }
        File tempFile  = null;
        try {
            tempFile = File.createTempFile("G:\\itit\\tmp\\", null);
            file.transferTo(tempFile);
            COSUtil.UpdateFile(cosClientUpload,BuckName,fileName,tempFile);

            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BuckName,fileName,HttpMethodName.GET);
            URL url = cosClientDownload.generatePresignedUrl(urlRequest);
            ititFile.setUrl(url.toString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
        ititFile.setName(file.getOriginalFilename());
        ititFile.setSize(StringUtil.FileSizeFormate(file.getSize()));
        ititFile.setStatus((byte) 0);
        ititFile.setObject_id(id.toString());
        ititFile.setUser(user);
        System.out.println(ititFile);

        fileService.add(ititFile);

        js.setSuccess(true);
        return js;
    }

    @ResponseBody
    @DeleteMapping(value = "/delete/{id}",headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel delete(@PathVariable("id")Integer id){
        JsonModel js = new JsonModel();

        fileService.delete(id);

        js.setSuccess(true);
        return js;
    }
}
