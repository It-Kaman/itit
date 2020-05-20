package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.ACCommentDao;
import com.itit.entry.Comment;
import com.itit.query.CommentQuery;
import com.itit.service.Ifac.ACCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/*评论*/
@Service
public class ACCommentServiceImpl implements ACCommentService {

    @Autowired
    private ACCommentDao ACcommentDao;

    @Override
    public PageInfo<Comment> findByQuery2(CommentQuery commentQuery) {
        if(commentQuery.isPagation())
        {
            PageHelper.startPage(commentQuery.getPageNum() / commentQuery.getPageSize() + 1, commentQuery.getPageSize());
            Page<Comment> list = (Page<Comment>) ACcommentDao.findByQuery2(commentQuery);
            return list.toPageInfo();
        }else {
            PageInfo<Comment> pageInfo = new PageInfo<Comment>();
            List<Comment> list = ACcommentDao.findByQuery2(commentQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    @Override
    public int add(Comment comment) {
        return ACcommentDao.add(comment);
    }

    @Override
    public int deleteById(Integer id) {
        return ACcommentDao.deleteById(id);
    }
}
