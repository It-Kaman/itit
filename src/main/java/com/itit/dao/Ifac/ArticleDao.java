package com.itit.dao.Ifac;

import com.itit.entry.Article;
import com.itit.entry.Tag;
import com.itit.query.ArticleQuery;
import com.itit.vo.ArticlePageVo;
import com.itit.vo.ArticleSearchVo;
import com.itit.vo.UploadArticleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao {
    public int selectCountByUserId(Integer id);

    public int add(UploadArticleVo articleVo);

    public int addToColumn(@Param("tagId") Integer tagId,@Param("articleId") Integer articleId);

    public List<UploadArticleVo> findByQuery(ArticleQuery articleQuery);

    public List<Tag> findTagByArticleId(Integer id);

    public List<Integer> findTagIdByArticleId(Integer id);

    public int findCountByIdAndAuthor(ArticleQuery articleQuery);
    public int delete(int[] ids);
    public int deleteTagByArticleId(int[] ids);
    public int deleteCommentByArticleId(int[] ids);
    public Article findById(Integer id);
    public int update(Article article);
    public List<ArticleSearchVo> findByQuerySearchVo(ArticleQuery articleQuery);
    public List<ArticlePageVo> findByQuery_second(ArticleQuery articleQuery);
    public int addClickNum(@Param("article_id")Integer article_id,@Param("num")Integer num);
    public int  updateClickNum(@Param("article_id")Integer article_id,@Param("num")Integer num);
    public int  deleteClickNum(int[] article_id);
    public List<UploadArticleVo> findByQueryLikename(ArticleQuery articleQuery);
}
