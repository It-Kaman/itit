package com.itit.dao.Ifac;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TVmiddleDao {
    public int add(@Param("tag_id")Integer tag_id,@Param("video_id")Integer video_id);

    public int delete(int[] ids);
}
