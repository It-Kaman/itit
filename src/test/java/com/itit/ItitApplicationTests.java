package com.itit;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.entry.*;
import com.itit.query.ArticleQuery;
import com.itit.query.CommentQuery;
import com.itit.query.FileQuery;
import com.itit.service.Ifac.*;
import com.itit.util.COSUtil;
import com.itit.util.StringUtil;
import com.itit.vo.UploadArticleVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ItitApplicationTests {
    private TransferManager transferManager;
    @Autowired
    private COSClient cosClientUpload;

    @Autowired
    private COSClient cosClientDownload;
    @Test
    public void TestConnection(){
        System.out.println(cosClientUpload);
        System.out.println(cosClientDownload);

//        String BucketName = "itit-1300622267";
//        String key = "folder/images/header/default.jpg";
//        GeneratePresignedUrlRequest requestUrl = new GeneratePresignedUrlRequest(BucketName,key,HttpMethodName.GET);
//        URL url = cosClientDownload.generatePresignedUrl(requestUrl);
//        System.out.println(url.toString());
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName("itit-1300622267");
        String path = "folder/article";
        List<String> pathList = COSUtil.selectCOS_Mkdir(cosClientUpload,path,"itit-1300622267");
        System.out.println(pathList + "-1");
        if(pathList.size()==0){
            //创建文件夹
            InputStream input = new ByteArrayInputStream(new byte[0]);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0);

            PutObjectRequest putObjectRequest = new PutObjectRequest("itit-1300622267",path,input,objectMetadata);
            PutObjectResult putObjectResult = cosClientUpload.putObject(putObjectRequest);

        }


    }

    @Test
    public void Deletetest() throws UnsupportedEncodingException {
        List<String> deleteFiles = new ArrayList<>();
        deleteFiles.add("folder/test/36骆家民.xlsx");
        deleteFiles.add(URLDecoder.decode("folder/test/%E9%AA%86%E5%AE%B6%E6%B0%91.jpg","utf-8"));
        COSUtil.deleteObject(cosClientUpload,"itit-1300622267",deleteFiles);
    }

    @Autowired
    private void setTranferManager(){
        this.transferManager = new TransferManager(cosClientUpload,Executors.newFixedThreadPool(10));
    }

    //高级接口
    @Test
    public void HeigSDK() throws InterruptedException {
        String bucketName = "itit-1300622267";
        String key = "test2.mp4";
        File localFile = new File("G:\\itit\\PartPath\\wu_1e6tvmb2869riga1jh21fvp77e0\\8、保留字&关键字&标识符\\8、保留字&关键字&标识符.wmv");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        Upload upload = transferManager.upload(putObjectRequest);
        UploadResult uploadResult = upload.waitForUploadResult();
        System.out.println(uploadResult.getRequestId());
        transferManager.shutdownNow();
    }

   @Autowired
   private VideoService videoService;
   @Autowired
   private UserService userService;
    @Autowired
    private SecurityService securityService;

    @Autowired
    private Security security;
   @Test
    public void testJDBC(){
//       videoService.SelectVideoNum(1);
//       System.out.println(StringUtil.NumFormate(10258205));
       System.out.println(security);

       User user = new User();
       user.setId(1);


       Security security = new Security();
       security.setId(2);
       security.setSecurity_q1((byte)1);
       security.setSecurity_a1("123");
       security.setSecurity_q2((byte)2);
       security.setSecurity_a2("111");

       userService.updateSecruityId(3,2);
//       userService.updateSecruityId(user);

//       System.out.println(securityService.add(security));
//       System.out.println(security.getId());
   }


   @Autowired
   private VCommentService vcommentService;
   //分页
  /* @Test
   public void testJDBC2(){
      *//* PageHelper.startPage(1,2);*//*
       CommentQuery cq = new CommentQuery();
       cq.setVideo_id(1);
       cq.setPageNumKey(1);
       cq.setPageSizeKey(2);
       List<Comment> comments = commentService.queryCommentByIdOrderByDate(cq);
       Page pageList = (Page)comments;
       System.out.println(pageList.getResult());
       Page pagetwo = pageList.pageNum(2);
       System.out.println(pagetwo);
       System.out.println(pageList.get(0));
   }*/


   //读取评论文件
    @Test
    public void testFile(){
        try {
            URL url = new URL("https://itit-1300622267.cos.ap-guangzhou.myqcloud.com/comment/video/1/vid%3D1_2020-4-16.txt");
           InputStream io = url.openStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(io));
           String str = null;
            while ((str  = reader.readLine()) != null ){
                System.out.println(str);
            }
            reader.close();
            io.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        File file = new File("https://itit-1300622267.cos.ap-guangzhou.myqcloud.com/comment/video/1/vid%3D1_2020-4-16.txt")
    }




    @Autowired
    private FileService fileService;
    @Test
    public void test(){
        FileQuery articleQuery = new FileQuery();
        PageInfo<ItitFile> vo = fileService.findByQuery(articleQuery);
        System.out.println(vo.getList());
        System.out.println(123);
        for(ItitFile v :vo.getList()){
            System.out.println(v.toString());
        }
    }
}
