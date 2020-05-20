package com.itit.service.Impl;

import ch.qos.logback.classic.Logger;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.ArticleDao;
import com.itit.dao.Ifac.TagDao;
import com.itit.entry.Article;
import com.itit.entry.Tag;
import com.itit.exception.FileIOException;
import com.itit.exception.LogicException;
import com.itit.query.ArticleQuery;
import com.itit.service.Ifac.ArticleService;
import com.itit.util.COSUtil;
import com.itit.util.StringUtil;
import com.itit.vo.ArticlePageVo;
import com.itit.vo.ArticleSearchVo;
import com.itit.vo.UploadArticleVo;
import com.qcloud.cos.COS;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl  implements ArticleService {
    private Logger logger = (Logger) LoggerFactory.getLogger("service");
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private TagDao tagDao;

    @Value(value = "${COS_ARTICLE}")
    private String COS_ARTICLE;

    @Value("${BUCKET_NAME}")
    private String BucketName;

    @Autowired
    private COSClient cosClientUpload;

    @Autowired
    private COSClient cosClientDownload;


    @Override
    public int SelectUpdateNum(Integer id) {
        return articleDao.selectCountByUserId(id);
    }

    @Override
    public int add(UploadArticleVo articleVo) {
        //上传文章信息
        int result = articleDao.add(articleVo);
        //添加到专栏中间表
        System.out.println(articleVo.getColumn());
        System.out.println(articleVo.getId());
        for(String i : articleVo.getColumn()){
            Integer tagId = Integer.parseInt(i);
            articleDao.addToColumn(tagId,articleVo.getId());
        }
        return 1;
    }

    @Override
    public PageInfo<UploadArticleVo> findByQuery(ArticleQuery articleQuery) {
        if (articleQuery.isPagation()) {
            PageHelper.startPage(articleQuery.getPageNum() / articleQuery.getPageSize() + 1, articleQuery.getPageSize());
            Page<UploadArticleVo> list = (Page<UploadArticleVo>) articleDao.findByQuery(articleQuery);
            return list.toPageInfo();
        } else {
            PageInfo<UploadArticleVo> pageInfo = new PageInfo<UploadArticleVo>();
            List<UploadArticleVo> ts = articleDao.findByQuery(articleQuery);
            pageInfo.setList(ts);
            return pageInfo;
        }
    }

    @Override
    public int findCountByIdAndAuthor(ArticleQuery articleQuery) {
        return articleDao.findCountByIdAndAuthor(articleQuery);
    }

    @Override
    public int deleteArticle(int[] ids) {
        List<Article> list = new ArrayList();
        for(int i = 0 ; i < ids.length ; i++){
            Article article = articleDao.findById(ids[i]);
            if(article == null){
                throw new FileIOException("不存在当前用户");
            }
            list.add(article);
        }
        articleDao.deleteClickNum(ids);
        articleDao.deleteCommentByArticleId(ids);
        articleDao.deleteTagByArticleId(ids);
        int result = articleDao.delete(ids);
        List<String> DeltePath =  new ArrayList<>();
        for(Article a : list){
            if(a.getContent().startsWith("http")){
                String PathName = a.getContent().substring(a.getContent().indexOf("/folder")+1);
                DeltePath.add(PathName);
            }else if(a.getContent().startsWith("G:\\itit\\")){
                File file = new File(a.getContent());
                if(!file.exists()){
                    continue;
                }else {
                    file.delete();
                }
            }
        }
        if(DeltePath.size() !=0){
            COSUtil.deleteObject(cosClientUpload,BucketName,DeltePath);
        }
        return result;
    }

    @Override
    public Article findById(Integer id) {
        return articleDao.findById(id);
    }

    @Override
    public int update(Article article) {
        return articleDao.update(article);
    }

    @Override
    public PageInfo<ArticleSearchVo> searchArticle(ArticleQuery articleQuery) {
        if(articleQuery.isPagation())
        {
            PageHelper.startPage(articleQuery.getPageNum() / articleQuery.getPageSize() + 1, articleQuery.getPageSize());
            Page<ArticleSearchVo> list = (Page<ArticleSearchVo>) articleDao.findByQuerySearchVo(articleQuery);
            for(ArticleSearchVo avo : list){
                List<Tag> tags = articleDao.findTagByArticleId(avo.getId());
                avo.setTags(tags);
            }
            return list.toPageInfo();
        }else {
            PageInfo<ArticleSearchVo> pageInfo = new PageInfo<ArticleSearchVo>();
            List<ArticleSearchVo> list = articleDao.findByQuerySearchVo(articleQuery);
            for(ArticleSearchVo avo : list){
                List<Tag> tags = articleDao.findTagByArticleId(avo.getId());
                avo.setTags(tags);
            }
            pageInfo.setList(list);
            return pageInfo;
        }
    }


    //进入对应的专栏页面
    @Override
    public PageInfo<ArticlePageVo> findByQuery_second(ArticleQuery articleQuery) {
        if(articleQuery.isPagation())
        {
            PageHelper.startPage(articleQuery.getPageNum() / articleQuery.getPageSize() + 1, articleQuery.getPageSize());
            Page<ArticlePageVo> list = (Page<ArticlePageVo>) articleDao.findByQuery_second(articleQuery);
            return list.toPageInfo();
        }else {
            PageInfo<ArticlePageVo> pageInfo = new PageInfo<ArticlePageVo>();
            List<ArticlePageVo> list = articleDao.findByQuery_second(articleQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    @Override
    public int addClickNum(Integer article_id, Integer num) {
        return articleDao.addClickNum(article_id,num);
    }

    @Override
    public int updateClickNum(Integer article_id, Integer num) {
        return articleDao.updateClickNum(article_id,num);
    }

    @Override
    public int deleteClickNum(int[] ids) {
        return articleDao.delete(ids);
    }

    @Override
    public int aduitByIds(int[] ids, Byte status,String reason) {
        List<Article> list = new ArrayList<>();
        for(int i = 0 ; i < ids.length ; i++){
            int id = ids[i];
            Article article = articleDao.findById(id);
            if(article == null){
                throw  new LogicException("不存在用户，请刷新");
            }else if(article.getStatus() == 2 || article.getStatus() == 1){
                continue;
            }
            list.add(article);
        }
        if(status == 2){
            for(Article article : list){
                File file = new File(article.getContent());
                if(file.exists()){
                    file.delete();
                    article.setContent("null");
                    article.setStatus((byte) 2);
                    article.setReason(reason);
                    articleDao.update(article);
                }else {
                    logger.error("文件不存在");
                    article.setContent("null");
                    article.setStatus((byte) 2);
                    article.setReason(reason);
                    articleDao.update(article);
                }
            }
        }else if(status == 1)
        {
            if (BucketName == "${BUCKET_NAME}"){
                BucketName = "itit-1300622267";
            }

            String datePath = StringUtil.dateFiles();
            String SavePath = COS_ARTICLE + datePath+"/";
            if(COSUtil.selectCOS_Mkdir(cosClientUpload,SavePath,BucketName).size() == 0)
            {
                COSUtil.Create_Mkdir(cosClientUpload,BucketName,SavePath);
            }
            for(Article article : list){
                File file = new File(article.getContent());
                if(!file.exists()){
                    throw new FileIOException("文件不存在");
                }
                String fileName = file.getName();
                String Path =  SavePath + fileName;
                System.out.println(Path);
                try {
                    COSUtil.UpdateFile(cosClientUpload,BucketName,Path,file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    cosClientUpload.shutdown();
                }
                //获取当前已上传URI
                //显示
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BucketName,Path,HttpMethodName.GET);
                URL url = cosClientDownload.generatePresignedUrl(urlRequest);
                article.setContent(url.toString());
                article.setStatus((byte) 1);
            }
        } else {
            return 0;
        }
        for(Article article : list){
            articleDao.update(article);
            new File(article.getContent()).delete();
        }
        return list.size();
    }

    @Override
    public PageInfo<UploadArticleVo> findByQueryLikename(ArticleQuery articleQuery) {
        if(articleQuery.isPagation())
        {
            PageHelper.startPage(articleQuery.getPageNum() / articleQuery.getPageSize() + 1, articleQuery.getPageSize());
            Page<UploadArticleVo> list = (Page<UploadArticleVo>) articleDao.findByQueryLikename(articleQuery);
            return list.toPageInfo();
        }else {
            PageInfo<UploadArticleVo> pageInfo = new PageInfo<UploadArticleVo>();
            List<UploadArticleVo> list = articleDao.findByQueryLikename(articleQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }
}
