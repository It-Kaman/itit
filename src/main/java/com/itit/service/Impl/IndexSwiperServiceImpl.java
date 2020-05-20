package com.itit.service.Impl;

import com.itit.dao.Ifac.IndexSwiperDao;
import com.itit.entry.admin.Swiper;
import com.itit.service.Ifac.IndexSwiperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexSwiperServiceImpl implements IndexSwiperService {
    @Autowired
    private IndexSwiperDao swiperDao;

    @Override
    public List<Swiper> queryAll() {
        return swiperDao.queryAll();
    }
}
