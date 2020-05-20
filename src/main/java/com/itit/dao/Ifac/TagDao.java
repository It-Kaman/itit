package com.itit.dao.Ifac;

import com.itit.entry.Tag;
import com.itit.query.TagQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TagDao {
    public List<Tag> selectAll();
    public Tag findByQuery(TagQuery tagQuery);
    public List<Tag> findByIds(Integer[] ids);
}
