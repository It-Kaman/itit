package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.FileDao;
import com.itit.entry.ItitFile;
import com.itit.query.FileQuery;
import com.itit.service.Ifac.FileService;
import com.itit.util.COSUtil;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private COSClient cosClientUpload;
    @Autowired
    private COSClient cosClientDownload;

    @Value("${BUCKET_NAME}")
    private String BuckName;

    @Value("${COS_FILE}")
    private String COS_FILE;
    @Autowired
    private FileDao fileDao;
    @Override
    public PageInfo<ItitFile> findByQuery(FileQuery fileQuery) {
        if (fileQuery.isPagation()){
            PageHelper.startPage(fileQuery.getPageNum()/fileQuery.getPageSize()+1,fileQuery.getPageSize());
            Page<ItitFile> list = (Page<ItitFile>)fileDao.findByQuery(fileQuery);
            return  list.toPageInfo();
        }else {
            PageInfo<ItitFile> list = new PageInfo<>();
            list.setList(fileDao.findByQuery(fileQuery));
            return  list;
        }
    }

    @Override
    public ItitFile queryById(Integer id) {
        return fileDao.queryById(id);
    }

    @Override
    public int add(ItitFile ititFile) {
        return fileDao.add(ititFile);
    }

    @Override
    public int delete(Integer id) {

        ItitFile file = fileDao.queryById(id);
        int reuslt =  fileDao.delete(id);

        String PartName = file.getUrl();
        PartName = PartName.substring(PartName.indexOf("folder/"));
        List<String> list = new ArrayList<>();
        list.add(PartName);
        COSUtil.deleteObject(cosClientUpload,BuckName,list);
        return reuslt;
    }
}
