package com.itit.service.Ifac;

import com.itit.entry.Fans;

public interface FansService {
    public int SelectFansNum(Integer id);
    public int add(Integer userId,Integer fanId);
    public int delByUserFanId(Integer userId,Integer fanId);
    Fans selectByUserIdFansId(Integer userId, Integer fanId);
}
