package com.itit.service.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.Comment;
import com.itit.query.CommentQuery;

import java.util.List;

public interface ACCommentService {
    public PageInfo<Comment> findByQuery2(CommentQuery commentQuery);

    public int add(Comment comment);

    public int deleteById(Integer id);
}
