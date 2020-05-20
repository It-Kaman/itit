package com.itit.dao.Ifac;

import com.itit.entry.ItitFile;
import com.itit.query.FileQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FileDao {
    public List<ItitFile> findByQuery(FileQuery fileQuery);

    public ItitFile queryById(Integer id);

    public int add(ItitFile ititFile);

    public int delete(Integer id);

    public int deltetByobject_IdAndStatus(@Param("object_id") Integer Object_id,@Param("status") Byte status);
}
