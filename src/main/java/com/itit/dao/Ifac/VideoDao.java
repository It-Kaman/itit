package com.itit.dao.Ifac;

import com.itit.entry.Video;
import com.itit.query.VideoQuery;
import com.itit.vo.UploadVideoVo;
import com.itit.vo.VideoPageVo;
import com.itit.vo.VideoSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface VideoDao {
    public int selectCountByUserId(Integer id);

    public int add(UploadVideoVo videoVo);

    public List<Video> queryAll(VideoQuery video);

    public Video queryById(Integer id);

    public int edit(Video video);

    public int delete(int[] ids);
    public List<Video> queryInId(int[] id);

    public int selectCountByVideo(VideoQuery videoQuery);

    public List<VideoSearchVo> queryByVideoQuery(VideoQuery videoQuery);

    public List<VideoSearchVo> queryByRand(VideoQuery videoQuery);

    public List<VideoPageVo> findByQuery_second(VideoQuery videoQuery);

    public int addClickNum(@Param("videoId") Integer videoId,@Param("num")Integer num);

    public int updateClickNum(@Param("videoId") Integer videoId,@Param("num")Integer num);
    public int selectClickNumById(Integer id);

    public List<UploadVideoVo> findByQueryLikename(VideoQuery videoQuery);
}
