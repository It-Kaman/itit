package com.itit.dao.Ifac;

import com.itit.entry.Comment;
import com.itit.query.CommentQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VCommentDao {
    public  List<Comment> queryCommentByIdOrderByDate(CommentQuery commentQuery);

    public Integer queryCountByVideoId(Integer video_Id);



    public List<Comment> findByQuery2(CommentQuery commentQuery);

    public int add(Comment comment);

    public int deleteById(Integer id);

    public int deleteByVideoIds(int[] ids);
}
