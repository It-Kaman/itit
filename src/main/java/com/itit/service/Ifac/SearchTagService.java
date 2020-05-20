package com.itit.service.Ifac;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.itit.entry.SearchTag;
import com.itit.query.SearchQuery;
import com.itit.query.SearchTagQuery;

import java.math.BigInteger;
import java.util.List;

public interface SearchTagService {
    public PageInfo<SearchTag> queryByQuery(SearchTagQuery searchTagQuery);
    public int add(SearchTag searchTag);
    public int updateById(Integer ids);
    public int updateByName(String searchName);
    public int deletebyIds(List<Integer> id);
    public int deleteByGroup(BigInteger num);
    public int deleteByQuery(SearchTagQuery searchTagQuery);
}
