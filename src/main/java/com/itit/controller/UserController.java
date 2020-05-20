package com.itit.controller;

import com.itit.entry.User;
import com.itit.service.Ifac.*;
import com.itit.util.COSUtil;
import com.itit.util.FileUtil;
import com.itit.util.StringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private COSClient cosClientUpload;
    @Autowired
    private COSClient cosClientDownload;


    @RequestMapping(value = "/passwordUpdate",method = RequestMethod.POST)
    public String PasswordUPdate(HttpServletRequest request, @RequestParam("oldPassword") String OldPassword,
                                 @RequestParam("newPassword") String NewPassword                     )
    {
        User user = (User) request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);
        if(!OldPassword.equals(userService.SecurityPassword(user.getId()))){
            request.setAttribute("passwordError",true);
            return "/security/SecurityModifyPwd";
        }
        User user1 = new User();
        user1.setId(user.getId());
        user1.setPassword(NewPassword);
        System.out.println(user1);
        if(userService.UpdateByUser(user1)>0){
            request.getSession().invalidate();
            request.setAttribute(StringUtil.MSG,"修改密码成功");
            return "forward:/success";
        }
        request.setAttribute(StringUtil.MSG,"内部出现异常，请重新修改密码");
        return "/common/success";
    }


    @ResponseBody
    @RequestMapping(value = "/imageUpdate",method = RequestMethod.POST)
    public Boolean ImgUpdate(@RequestPart("myImg") MultipartFile image, HttpServletRequest request)
    {
        Properties p = new Properties();
        String bucketName = "itit-1300622267";
        try {
            p.load(UserController.class.getClassLoader().getResourceAsStream("config\\properties\\SecretInfo.properties"));
            bucketName = p.getProperty("BUCKET_NAME");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String FileName = image.getOriginalFilename();
        //获取后缀
        String suffix = FileName.substring(FileName.lastIndexOf("."));
        if (!suffix.equals(".pjp")&&!suffix.equals(".tiff")&&!suffix.equals(".pjpeg")&&!suffix.equals(".jfif")
                &&!suffix.equals(".tif")&&!suffix.equals(".gif")&&!suffix.equals(".svg")&&!suffix.equals(".bmp")
                &&!suffix.equals(".png")&&!suffix.equals(".jpeg")&&!suffix.equals(".svgz")&&!suffix.equals(".jpg")
                &&!suffix.equals(".webp")&&!suffix.equals(".ico")&&!suffix.equals(".xbm")&&!suffix.equals(".dib")){
            return false;
        }
        //获取前缀
        String prefix = FileName.substring(0,FileName.lastIndexOf("."));
        //重组文件
        FileName = prefix +"_" + UUID.randomUUID().toString().replace("-","") + suffix;
        System.out.println(FileName);

        //文件打散
        String dates = StringUtil.dateFiles();
        //获取路径名
        String BasePath = "/folder/images/header";
        if(COSUtil.selectCOS_Mkdir(cosClientUpload,BasePath + dates,bucketName).size() == 0){
            COSUtil.Create_Mkdir(cosClientUpload,bucketName,BasePath+dates);
        }
        //上传
        try {
            File file = new File(FileName);
            FileUtil.multiPartFileToFile(image.getInputStream(),file);
            COSUtil.UpdateFile(cosClientUpload,bucketName,BasePath+dates+"/"+FileName,file);
            //上传成功后获取下载链接
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,BasePath+dates+"/"+FileName,HttpMethodName.GET);
            URL url = cosClientDownload.generatePresignedUrl(urlRequest);
            System.out.println(url.toString());
            User sessionUser = (User)request.getSession().getAttribute(StringUtil.USER_SESSION_KEY);
            User user = new User();
            user.setId(sessionUser.getId());
            user.setHeader(url.toString());
            //修改数据库
            if(userService.UpdateByUser(user)>0){
                sessionUser.setHeader(url.toString());
                request.getSession().setAttribute(StringUtil.USER_SESSION_KEY,sessionUser);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
