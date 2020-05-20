package com.itit.service.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.ItitFile;
import com.itit.query.FileQuery;

public interface FileService {

    public PageInfo<ItitFile> findByQuery(FileQuery fileQuery);
    public ItitFile queryById(Integer id);
    public int add(ItitFile ititFile);
    public int delete(Integer id);
}
