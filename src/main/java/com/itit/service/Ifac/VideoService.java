package com.itit.service.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.Video;
import com.itit.query.VideoQuery;
import com.itit.vo.UploadVideoVo;
import com.itit.vo.VideoPageVo;
import com.itit.vo.VideoSearchVo;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface VideoService {
    public int SelectVideoUpdateNum(Integer id);

    public boolean uploadVideo(UploadVideoVo videoVo);

    public PageInfo<Video> queryAll(VideoQuery video);

    public Video queryById(Integer id);

    public int edit(Video video);

    public int delete(int[] ids);

    public int selectCountByVideo(VideoQuery videoQuery);

    public PageInfo<VideoSearchVo> queryByVideoQuery(VideoQuery videoQuery);

    public PageInfo<VideoSearchVo> queryByRand(VideoQuery videoQuery);

    public PageInfo<VideoPageVo> findByQuery_second(VideoQuery videoQuery);

    public int updateClickNum(Integer videoId,Integer num);

    public int selectClickNumById(Integer id);

    public PageInfo<UploadVideoVo> findByQueryLikename(VideoQuery videoQuery);

    public int aduitByIds(int[] ids, Byte status,String reason);
}
