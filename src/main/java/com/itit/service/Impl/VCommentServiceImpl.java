package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.VCommentDao;
import com.itit.entry.Comment;
import com.itit.query.CommentQuery;
import com.itit.service.Ifac.VCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/*评论*/
@Service
public class VCommentServiceImpl implements VCommentService {

    @Autowired
    private VCommentDao vCommentDao;


    @Override
    public Integer queryCountByVideoId(Integer video_Id) {
        return vCommentDao.queryCountByVideoId(video_Id);
    }


    @Override
    public PageInfo<Comment> findByQuery2(CommentQuery commentQuery) {
        if(commentQuery.isPagation())
        {
            PageHelper.startPage(commentQuery.getPageNum() / commentQuery.getPageSize() + 1, commentQuery.getPageSize());
            Page<Comment> list = (Page<Comment>) vCommentDao.findByQuery2(commentQuery);
            return list.toPageInfo();
        }else {
            PageInfo<Comment> pageInfo = new PageInfo<Comment>();
            List<Comment> list = vCommentDao.findByQuery2(commentQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    @Override
    public int add(Comment comment) {
        return vCommentDao.add(comment);
    }

    @Override
    public int deleteById(Integer id) {
        return vCommentDao.deleteById(id);
    }
}
