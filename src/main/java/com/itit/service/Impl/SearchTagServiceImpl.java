package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.SearchTagDao;
import com.itit.entry.SearchTag;
import com.itit.query.SearchQuery;
import com.itit.query.SearchTagQuery;
import com.itit.service.Ifac.SearchTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SearchTagServiceImpl implements SearchTagService {
    @Autowired
    private SearchTagDao searchTagDao;
    @Override
    public PageInfo<SearchTag> queryByQuery(SearchTagQuery searchTagQuery) {
        if(searchTagQuery.isPagation())
        {
            PageHelper.startPage(searchTagQuery.getPageNum() / searchTagQuery.getPageSize() + 1, searchTagQuery.getPageSize());
            Page<SearchTag> list = (Page<SearchTag>) searchTagDao.queryByQuery(searchTagQuery);
            return list.toPageInfo();
        }else {
            PageInfo<SearchTag> pageInfo = new PageInfo<SearchTag>();
            List<SearchTag> list = searchTagDao.queryByQuery(searchTagQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }

    public int add(SearchTag searchTag) {
        return 0;
    }

    public int updateById(Integer ids) {
        return 0;
    }

    public int updateByName(String searchName) {
        return 0;
    }

    public int deletebyIds(List<Integer> id) {
        return 0;
    }

    public int deleteByGroup(BigInteger num) {
        return 0;
    }

    @Override
    public int deleteByQuery(SearchTagQuery searchTagQuery) {
        return 0;
    }

    public int deleteByQuery(SearchQuery searchQuery) {
        return 0;
    }
}
