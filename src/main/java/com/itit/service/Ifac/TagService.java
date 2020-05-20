package com.itit.service.Ifac;

import com.itit.entry.Tag;
import com.itit.query.TagQuery;

import java.util.List;

public interface TagService {
    public List<Tag> selectAll();
    public List<Tag> findByIds(Integer[] ids);
    public Tag findByQuery(TagQuery tagQuery);
}
