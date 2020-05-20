package com.itit.controller;

import ch.qos.logback.classic.Logger;
import com.github.pagehelper.PageInfo;
import com.itit.entry.User;
import com.itit.entry.Video;
import com.itit.entry.admin.Admin;
import com.itit.query.VideoQuery;
import com.itit.service.Ifac.VCommentService;
import com.itit.service.Ifac.VideoService;
import com.itit.util.*;

import com.itit.vo.UploadVideoVo;
import com.itit.vo.VideoPageVo;
import com.itit.vo.VideoSearchVo;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class VideoController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger("itit");
    @Value("${tempPath}")
    private String tempPath;
    @Value("${partPath}")
    private String partPaths;
    @Autowired
    private VCommentService vCommentService;
    @Autowired
    private VideoService videoService;
    @Value("${partPath}")
    private String PartPath;
    @Value("${VideosavePath}")
    private String savePath;

    @RequestMapping(value = "/videos/page/VO{num}",method = RequestMethod.GET)
    public String VideoPage(@PathVariable("num") String num, Model model,HttpSession session)
    {
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        Admin admin = (Admin)session.getAttribute(StringUtil.ADMIN_SESSION_KEY);
        VideoPageVo vo= null;
        //根据编号查询对应的视频
        if (StringUtil.isEmpty(num)){
            return "/common/error";
        }
        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setPagation(false);
        videoQuery.setNum(num);
        PageInfo<VideoPageVo> pageInfo = videoService.findByQuery_second(videoQuery);
        if(pageInfo.getList().size()>0) {
            vo = pageInfo.getList().get(0);
            System.out.println(vo);
            System.out.println(user);
            if (vo.getStatus() == 2) {
                //不通过的视频
                model.addAttribute(StringUtil.MSG, "不存在当前视频");
                return "common/error";
            }else if (vo.getStatus() == 0 && !(admin != null || (user != null && vo.getAuthor().getId().equals(user.getId())))){
                return "redirect:/index";
            }
        }else {
            return "common/error";
        }

        if(!vo.getUrl().startsWith("http")){
            vo.setUrl("ititFile/"+vo.getUrl().substring(PartPath.length()));
        }
        model.addAttribute("video",vo);
        if(vo.getStatus() == 1){
            videoService.updateClickNum(vo.getId(),1);
            Integer count = videoService.selectClickNumById(vo.getId());
            model.addAttribute("clickNum",count);
        }
        return "/video/VideoPage";
    }

    @ResponseBody
    @RequestMapping(value = "/VideoUpload",method = RequestMethod.POST)
    public void upload(@RequestParam("file")MultipartFile file,String guid, Integer chunk,Integer chunks,HttpServletRequest request){
        File tempPartFile = null;
        boolean isMultipartFile = ServletFileUpload.isMultipartContent(request);
        if(isMultipartFile){
            try{
                //创建临时文件进行分片
                String PrefixName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
                //文件打散
                String tempPartFilePath = tempPath + File.separator + guid + File.separator + PrefixName;
                tempPartFile = new File(tempPartFilePath);
                if(!tempPartFile.exists())
                {
                    tempPartFile.mkdirs();
                }
                String FileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
                // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                //如果文件小于20M 则进行默认分片
                if(chunks == null){
                    chunk = 0;
                }
                File tempFile = new File(tempPartFilePath,FileName+"_"+chunk+".part");
                FileUtils.copyInputStreamToFile(file.getInputStream(),tempFile);
            }catch (IOException ex){
                try {
                    FileUtils.deleteDirectory(tempPartFile);
                    throw new Exception("文件出错");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //合并
    @ResponseBody
    @RequestMapping(value = "/VideoMerge",method = RequestMethod.POST)
    public Object upload(UploadVideoVo videoVo, @RequestParam(required = false,value = "temp_header") MultipartFile header, HttpSession session) throws IOException {
        JsonModel js = new JsonModel();
        User user = (User)session.getAttribute(StringUtil.USER_SESSION_KEY);
        if(user == null){
            return js;
        }
        File tempPartFile = null;
        //文件名字
        String prefixName = videoVo.getFileName().substring(0,videoVo.getFileName().lastIndexOf("."));
        //临时空间
        String tempPartFilePath = tempPath + File.separator + videoVo.getGuid() + File.separator + prefixName;
        //真实存储空间
        String realPartFilePath = savePath  + videoVo.getGuid() + File.separator + prefixName;

        File realPath = new File(realPartFilePath);
        File realFile = new File(realPath + File.separator + videoVo.getFileName());

        if(!realPath.exists()){
            realPath.mkdirs();
        }
        if (realPath.isDirectory()){
            try {
                realFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件无法创建");
            }
        }
        //合成文件
        File tempPath = new File(tempPartFilePath);

        for (int i = 0; i < tempPath.listFiles().length; i++) {
            tempPartFile = new File(tempPartFilePath, prefixName + "_" + i + ".part");
            //判断是否存在该文件
            if(!tempPartFile.exists()){
                js.setMsg("不存在临时文件");
                FileUtils.deleteDirectory(tempPath);
                return js;
            }
            FileOutputStream relos=null;
            try {
                relos = new FileOutputStream(realFile,true);
                FileUtils.copyFile(tempPartFile,relos);
                relos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                js.setMsg("网络异常!");
                //上传完成，删除临时文件夹
                FileUtils.deleteDirectory(new File(tempPartFilePath));
                FileUtils.deleteDirectory(new File(realPartFilePath + File.separator + prefixName));
                return js;
            }finally {
                relos.close();
            }
        }
        //上传完成，删除临时文件夹
        FileUtils.deleteDirectory(new File(tempPartFilePath));

        /**
         * 下一步任务：
         *      进行将数据插入视频表和标签视频中间表
         *      //图片存放在itit/video_img/上传视频名字/图片名
         */
        String headerPath;
        if(header != null){
            String FileName = header.getOriginalFilename();
            String prefixFileName = FileName.substring(0,FileName.lastIndexOf("."));
            String suffixFileName = FileName.substring(FileName.lastIndexOf("."));

            headerPath =realPartFilePath + File.separator + prefixFileName + UUID.randomUUID().toString().replace("-","") + suffixFileName;
            header.transferTo(new File(headerPath));
        }else {
            //获取第五帧图片
            Map<String,Object> map =  VideoUtil.getScreenshot(realFile.getAbsolutePath(),ConstrainUtil.GET_FRAMES_LENGTH);
            headerPath = map.get("imgPath").toString();
        }


        //下一步：进行数据库存储
        videoVo.setUrl(realFile.getAbsolutePath());
        videoVo.setHeader(headerPath);
        videoVo.setAuthor(user);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        videoVo.setCreatedate(date);
        videoVo.setNum(dateFormat.format(date) + user.getId());
        videoService.uploadVideo(videoVo);
        js.setSuccess(true);
        return js;
    }
    @ResponseBody
    @RequestMapping(value = "/videos/recommend",method = RequestMethod.POST)
    public Map<String,Object> recommend(Integer tagId){
        System.out.println(tagId);
        Map<String,Object> map = new HashMap<String,Object>();
        VideoQuery query = new VideoQuery();
        Integer[] tags = {tagId};
        query.setColumn(tags);
        query.setPageNum(0);
        query.setPageSize(10);
        query.setStatus((byte)1);
        query.setPagation(true);
        PageInfo<VideoSearchVo> pageInfo = videoService.queryByRand(query);
        for (VideoSearchVo vvo : (List<VideoSearchVo>)pageInfo.getList()){
            int num = 0;
            if(!StringUtil.isEmpty(vvo.getHotNum())){
                num = Integer.parseInt(vvo.getHotNum());
                vvo.setHotNum(StringUtil.NumFormate(num));
            }
            else {
                vvo.setHotNum(""+num);
            }
            if(StringUtil.isEmpty(vvo.getNum())){
                vvo.setNum("");
            }
        }
        map.put("datas",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        map.put("success",true);
        map.put("tagId",tagId);
        return map;
    }

    @RequestMapping(value = "/videos/admin")
    public String list(){
        return "admin/video";
    }

    @ResponseBody
    @RequestMapping(value = "/videos/admin",method = RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel getRecord(VideoQuery videoQuery){
        JsonModel js = new JsonModel();
        videoQuery.setPagation(true);
        PageInfo<UploadVideoVo> list = videoService.findByQueryLikename(videoQuery);
        js.setDatas(list.getList());
        js.setCount(list.getTotal());
        js.setSuccess(true);
        return js;
    }


    @ResponseBody
    @RequestMapping(value ="/videos/admin/delete/{num}",method = RequestMethod.DELETE,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel delete(@PathVariable("num") String str)
    {
        JsonModel js = new JsonModel();
        String idStr[] = str.split(",");
        int ids[] = new int[idStr.length];
        for(int i = 0; i < idStr.length;i++){
            int id = Integer.parseInt(idStr[i]);
            Video video = videoService.queryById(id);
            if(video == null){
                js.setMsg("请刷新页面，不存在数据");
                return js;
            }
            ids[i] = id;
        }
        videoService.delete(ids);
        js.setSuccess(true);
        return js;
    }

    @ResponseBody
    @PutMapping(value = "/videos/admin/aduit/{ids}",headers ={"X-Requested-With=XMLHttpRequest"})
    public JsonModel aduit(@PathVariable("ids") String idsStr,Byte teacher_status,@RequestParam(value = "reason",required = false) String reason){
        System.out.println(reason);
        JsonModel jsonModel = new JsonModel();
        String[] strs = idsStr.split(",");
        int[] ids = new int[strs.length];
        for(int i = 0 ; i < strs.length;i++){
            ids[i] = Integer.parseInt(strs[i]);
        }
        if(videoService.aduitByIds(ids,teacher_status,reason) == 0)
        {
            jsonModel.setSuccess(false);
            return jsonModel;
        }
        jsonModel.setSuccess(true);
        return jsonModel;
    }
}

