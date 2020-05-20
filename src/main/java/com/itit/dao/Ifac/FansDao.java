package com.itit.dao.Ifac;

import com.itit.entry.Fans;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FansDao {
    public int selectCountByUserId(Integer id);
    public int add(@Param("User_id") Integer userId,@Param("Fan_id") Integer fanId);
    public int delByUserFanId(@Param("User_id") Integer userId,@Param("Fan_id") Integer fanId);
    Fans selectByUserIdFansId(@Param("User_id") Integer userId,@Param("Fan_id") Integer fanId);
}
