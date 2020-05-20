package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.*;
import com.itit.entry.Article;
import com.itit.entry.Video;
import com.itit.exception.FileIOException;
import com.itit.exception.LogicException;
import com.itit.query.VideoQuery;
import com.itit.service.Ifac.FansService;
import com.itit.service.Ifac.VCommentService;
import com.itit.service.Ifac.VideoService;
import com.itit.util.COSUtil;
import com.itit.util.StringUtil;
import com.itit.vo.UploadVideoVo;
import com.itit.vo.VideoPageVo;
import com.itit.vo.VideoSearchVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoDao videoDao;

    @Autowired
    private FileDao fileDao;
    @Value("${BUCKET_NAME}")
    private String BucketName;

    @Autowired
    private COSClient cosClientUpload;

    @Autowired
    private TVmiddleDao tVmiddleDao;
    @Autowired
    private VCommentDao vCommentDao;
    @Autowired
    private VideoClickNumDao videoClickNumDao;

    @Value("${partPath}")
    private String PartPath;

    @Value("${COS_VIDEO}")
    private String COS_VIDEO;

    @Value("${COS_ARTICLE}")
    private String COS_ARTICLE;

    @Value("${COS_VIDEO_HEADER}")
    private String COS_VIDEO_HEADER;

    @Autowired
    private COSClient cosClientDownload;

    public int SelectVideoUpdateNum(Integer id){
        return videoDao.selectCountByUserId(id);
    }

    //上传操作
    @Override
    public boolean uploadVideo(UploadVideoVo videoVo) {
        videoVo.setStatus((byte)0);
        videoDao.add(videoVo);
        //根据标签进行存储中间表
        for (String i : videoVo.getColumn()){
            //插入
            tVmiddleDao.add(Integer.parseInt(i),videoVo.getId());
        }
        videoDao.addClickNum(videoVo.getId(),1);
        return true;
    }

    @Override
    public PageInfo<Video> queryAll(VideoQuery video) {
        PageHelper.startPage(video.getPageNum()/video.getPageSize()+1,video.getPageSize());
        List<Video> videos= videoDao.queryAll(video);
        if(videos.size() > 0){
            for(Video v :videos){
                if(v.getHeader().indexOf(PartPath)==0){
                    v.setHeader("\\ititFile\\" + v.getHeader().substring(PartPath.length()));
                }
                if(v.getUrl().indexOf(PartPath)==0){
                    v.setUrl("\\ititFile\\" + v.getUrl().substring(PartPath.length()));
                }
            }
        }
        Page<Video> list = (Page<Video>) videos;
        return list.toPageInfo();
    }

    @Override
    public Video queryById(Integer id) {
        return videoDao.queryById(id);
    }

    @Override
    public int edit(Video video) {
        return videoDao.edit(video);
    }

    @Override
    public int delete(int[] ids) {
        //查询所有要删除的视频
        List<Video> list = videoDao.queryInId(ids);
        List<String> deleteLists = new ArrayList<>();
        if (list.size() == 0){
            return 0;
        }
        for(Video v: list){
            //对象存储上面
            if(v.getUrl().startsWith("http")){
                String path = v.getUrl().substring(v.getUrl().indexOf("/folder")+1);
                deleteLists.add(path);
            }else if(v.getUrl().startsWith("G:\\itit\\")){
                new File(v.getUrl()).delete();
            }
            if(v.getHeader().startsWith("http")){
                String path = v.getHeader().substring(v.getHeader().indexOf("/folder")+1);
                deleteLists.add(path);
            }else if(v.getHeader().startsWith("G:\\itit\\")){
                new File(v.getUrl()).delete();
            }
            if(deleteLists.size() > 0){
                COSUtil.deleteObject(cosClientUpload,BucketName,deleteLists);
            }
            fileDao.deltetByobject_IdAndStatus(v.getId(), (byte) 0);
        }
        //删除中间表数据
        tVmiddleDao.delete(ids);
        //删除评论
        vCommentDao.deleteByVideoIds(ids);
        //删除点击数
        videoClickNumDao.deleteByIds(ids);
        int result = videoDao.delete(ids);
        return result;
    }

    @Override
    public int selectCountByVideo(VideoQuery videoQuery) {
        return videoDao.selectCountByVideo(videoQuery);
    }

    @Override
    public PageInfo<VideoSearchVo> queryByVideoQuery(VideoQuery videoQuery) {
        if(videoQuery.isPagation())
        {
            PageHelper.startPage(videoQuery.getPageNum() / videoQuery.getPageSize() + 1, videoQuery.getPageSize());
            Page<VideoSearchVo> list = (Page<VideoSearchVo>) videoDao.queryByVideoQuery(videoQuery);
            return list.toPageInfo();
        }else {
            PageInfo<VideoSearchVo> pageInfo = new PageInfo<VideoSearchVo>();
            List<VideoSearchVo> list = videoDao.queryByVideoQuery(videoQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    @Override
    public PageInfo<VideoSearchVo> queryByRand(VideoQuery videoQuery) {
        System.out.println(videoQuery);
        if(videoQuery.isPagation())
        {
            PageHelper.startPage(videoQuery.getPageNum() / videoQuery.getPageSize() + 1, videoQuery.getPageSize());
            Page<VideoSearchVo> list = (Page<VideoSearchVo>) videoDao.queryByRand(videoQuery);
            return list.toPageInfo();
        }else {
            PageInfo<VideoSearchVo> pageInfo = new PageInfo<VideoSearchVo>();
            List<VideoSearchVo> list = videoDao.queryByRand(videoQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    @Override
    public PageInfo<VideoPageVo> findByQuery_second(VideoQuery videoQuery) {
        if(videoQuery.isPagation())
        {
            PageHelper.startPage(videoQuery.getPageNum() / videoQuery.getPageSize() + 1, videoQuery.getPageSize());
            Page<VideoPageVo> list = (Page<VideoPageVo>) videoDao.findByQuery_second(videoQuery);
            return list.toPageInfo();
        }else {
            PageInfo<VideoPageVo> pageInfo = new PageInfo<VideoPageVo>();
            List<VideoPageVo> list = videoDao.findByQuery_second(videoQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    @Override
    public int updateClickNum(Integer videoId, Integer num) {
        return videoDao.updateClickNum(videoId,num);
    }

    @Override
    public int selectClickNumById(Integer id) {
        return videoDao.selectClickNumById(id);
    }

    @Override
    public PageInfo<UploadVideoVo> findByQueryLikename(VideoQuery videoQuery) {
        PageInfo<UploadVideoVo> pageInfo = new PageInfo<UploadVideoVo>();;
        List<UploadVideoVo> videos = null;
        if(videoQuery.isPagation())
        {
            PageHelper.startPage(videoQuery.getPageNum()/videoQuery.getPageSize()+1,videoQuery.getPageSize());
        }
        videos= videoDao.findByQueryLikename(videoQuery);
        if(videos == null){
            pageInfo.setList(null);
            return pageInfo;
        }
        if(videos.size() > 0){
            for(Video v :videos){
                if(v.getHeader().indexOf(PartPath)==0){
                    v.setHeader("\\ititFile\\" + v.getHeader().substring(PartPath.length()));
                }
                if(v.getUrl().indexOf(PartPath)==0){
                    v.setUrl("\\ititFile\\" + v.getUrl().substring(PartPath.length()));
                }
            }
        }
        pageInfo.setList(videos);
        return pageInfo;
    }


    @Override
    public int aduitByIds(int[] ids, Byte status,String reason) {
        List<Video> list = new ArrayList<>();
        List<Video> deleteList =new ArrayList<>();
        for(int i = 0 ; i < ids.length ; i++){
            int id = ids[i];
            Video video = videoDao.queryById(id);
            if(video == null){
                throw  new LogicException("不存在用户，请刷新");
            }else if(video.getStatus() == 2 || video.getStatus() == 1){
                continue;
            }
            list.add(video);
        }
        if(list.size() ==0){
            return 0;
        }else {
            deleteList = (List<Video>) ((ArrayList<Video>) list).clone();
            System.out.println(deleteList);
        }
        if(status == 2){
            //不通过,删除文件
            for(Video video : list){
                File header = new File(video.getUrl());
                File url = new File(video.getHeader());
                if(header.exists()){
                    header.delete();
                    video.setHeader("null");
                }
                if(url.exists()){
                    url.exists();
                    video.setUrl("null");
                }
                video.setStatus((byte) 2);
                video.setReason(reason);
                videoDao.edit(video);
                fileDao.deltetByobject_IdAndStatus(video.getId(), (byte) 0);
            }
        }else if(status == 1)
        {
            if (BucketName == "${BUCKET_NAME}"){
                BucketName = "itit-1300622267";
            }

            String datePath = StringUtil.dateFiles();
            String UrlPath = COS_VIDEO + datePath+"/";
            String HeaderPath = COS_VIDEO_HEADER + datePath+"/";
            if(COSUtil.selectCOS_Mkdir(cosClientUpload,UrlPath,BucketName).size() == 0)
            {
                COSUtil.Create_Mkdir(cosClientUpload,BucketName,UrlPath);
            }
            if(COSUtil.selectCOS_Mkdir(cosClientUpload,HeaderPath,BucketName).size() == 0)
            {
                COSUtil.Create_Mkdir(cosClientUpload,BucketName,HeaderPath);
            }
            for(Video video : list){
                File header = new File(video.getHeader());
                File videoFile = new File(video.getUrl());
                if(!header.exists() || !videoFile.exists()){
                    throw new FileIOException("文件不存在!");
                }
                String urlName = videoFile.getName();
                String headerName = header.getName();
                UrlPath =  UrlPath + urlName;
                HeaderPath =  HeaderPath + headerName;
                try {
                    COSUtil.UpdateFile(cosClientUpload,BucketName,UrlPath,videoFile);
                    COSUtil.UpdateFile(cosClientUpload,BucketName,HeaderPath,header);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    cosClientUpload.shutdown();
                }
                //获取当前已上传URI
                //显示
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BucketName,UrlPath,HttpMethodName.GET);
                URL url = cosClientDownload.generatePresignedUrl(urlRequest);
                video.setUrl(url.toString());
                urlRequest = new GeneratePresignedUrlRequest(BucketName,HeaderPath,HttpMethodName.GET);
                url = cosClientDownload.generatePresignedUrl(urlRequest);
                video.setHeader(url.toString());
                video.setStatus((byte) 1);
            }
        } else {
            return 0;
        }
        for(Video video : list){
            videoDao.edit(video);
        }
        for(Video video : deleteList){
            new File(video.getUrl()).delete();
            new File(video.getHeader()).delete();
        }
        return list.size();
    }
}
