package com.itit.service.Impl;

import com.itit.dao.Ifac.TagDao;
import com.itit.entry.Tag;
import com.itit.query.TagQuery;
import com.itit.service.Ifac.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public List<Tag> selectAll(){
        return tagDao.selectAll();
    }

    @Override
    public List<Tag> findByIds(Integer[] ids) {
        return tagDao.findByIds(ids);
    }

    @Override
    public Tag findByQuery(TagQuery tagQuery) {
        return tagDao.findByQuery(tagQuery);
    }
}
