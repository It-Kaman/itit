package com.itit.service.Ifac;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.itit.entry.Article;
import com.itit.query.ArticleQuery;
import com.itit.vo.ArticlePageVo;
import com.itit.vo.ArticleSearchVo;
import com.itit.vo.UploadArticleVo;

import java.util.List;

public interface ArticleService {
    public int SelectUpdateNum(Integer id);

    public int add(UploadArticleVo articleVo);

    public PageInfo<UploadArticleVo> findByQuery(ArticleQuery articleQuery);

    public int findCountByIdAndAuthor(ArticleQuery articleQuery);

    public int deleteArticle(int[] ids);

    public Article findById(Integer id);

    public int update(Article article);

    public PageInfo<ArticleSearchVo> searchArticle(ArticleQuery articleQuery);

    public PageInfo<ArticlePageVo> findByQuery_second(ArticleQuery articleQuery);

    public int addClickNum(Integer article_id,Integer num);

    public int  updateClickNum(Integer article_id,Integer num);

    public int  deleteClickNum(int[] ids);

    public int aduitByIds(int[] ids,Byte status,String reason);

    public PageInfo<UploadArticleVo> findByQueryLikename(ArticleQuery articleQuery);
}
