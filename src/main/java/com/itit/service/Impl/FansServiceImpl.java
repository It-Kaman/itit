package com.itit.service.Impl;

import com.itit.dao.Ifac.FansDao;
import com.itit.entry.Fans;
import com.itit.service.Ifac.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FansServiceImpl implements FansService {
    @Autowired
    private FansDao fansDao;

    public int SelectFansNum(Integer id){
        return fansDao.selectCountByUserId(id);
    }

    @Override
    public int add(Integer userId, Integer fanId) {
        return fansDao.add(userId,fanId);
    }

    @Override
    public int delByUserFanId(Integer userId, Integer fanId) {
        return fansDao.delByUserFanId(userId, fanId);
    }

    @Override
    public Fans selectByUserIdFansId(Integer userId, Integer fanId) {
        return fansDao.selectByUserIdFansId(userId,fanId);
    }
}
